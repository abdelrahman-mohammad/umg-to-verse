const { SlashCommandBuilder } = require("discord.js");

module.exports = {
    cooldown: 5,
    data: new SlashCommandBuilder()
        .setName("ping")
        .setDescription("Check the bot's latency."),
    async execute(interaction) {
        await interaction.reply({
            content: `Ping: ${interaction.client.ws.ping}ms`,
            ephemeral: true,
        });
    },
};
