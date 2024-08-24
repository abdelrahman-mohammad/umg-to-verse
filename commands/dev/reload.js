const { SlashCommandBuilder } = require("discord.js");
const fs = require("fs");
const path = require("path");

function getCommandFiles(dir, baseDir = dir) {
    let files = [];
    const items = fs.readdirSync(dir, { withFileTypes: true });

    for (const item of items) {
        const fullPath = path.join(dir, item.name);
        if (item.isDirectory()) {
            files = [...files, ...getCommandFiles(fullPath, baseDir)];
        } else if (item.isFile() && item.name.endsWith(".js")) {
            const relativePath = path.relative(baseDir, fullPath);
            const nameWithoutExtension = relativePath.slice(0, -3); // Remove .js
            const fileName = path.parse(item.name).name;
            files.push({
                name: nameWithoutExtension,
                value: fileName,
            });
        }
    }

    return files;
}

const commandsDir = path.join(__dirname, "../");
const commandChoices = getCommandFiles(commandsDir);

module.exports = {
    data: new SlashCommandBuilder()
        .setName("reload")
        .setDescription("Reloads a command.")
        .addStringOption((option) =>
            option
                .setName("command")
                .setDescription("The command to reload.")
                .addChoices(...commandChoices)
                .setRequired(true)
        ),
    async execute(interaction) {
        const commandName = interaction.options
            .getString("command", true)
            .toLowerCase();
        const command = interaction.client.commands.get(commandName);

        if (!command) {
            return interaction.reply(
                `There is no command with name \`${commandName}\`!`
            );
        }

        delete require.cache[require.resolve(`./${command.data.name}.js`)];

        try {
            const newCommand = require(`./${command.data.name}.js`);
            interaction.client.commands.set(newCommand.data.name, newCommand);
            await interaction.reply(
                `Command \`${newCommand.data.name}\` was reloaded!`
            );
        } catch (error) {
            console.error(error);
            await interaction.reply(
                `There was an error while reloading a command \`${command.data.name}\`:\n\`${error.message}\``
            );
        }
    },
};
