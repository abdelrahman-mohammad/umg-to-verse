const { REST, Routes } = require("discord.js");
const fs = require("node:fs");
const path = require("node:path");

const globalCommands = [];
const devCommands = [];

// Grab all the command folders from the commands directory you created earlier
const foldersPath = path.join(__dirname, "commands");
const commandFolders = fs.readdirSync(foldersPath);

// Loop through each global command folder
for (const folder of commandFolders.filter((folder) => folder !== "dev")) {
    // Grab all the command files from the commands directory you created earlier
    const commandsPath = path.join(foldersPath, folder);
    const commandFiles = fs
        .readdirSync(commandsPath)
        .filter((file) => file.endsWith(".js"));
    // Grab the SlashCommandBuilder#toJSON() output of each command's data for deployment
    for (const file of commandFiles) {
        const filePath = path.join(commandsPath, file);
        const command = require(filePath);
        if ("data" in command && "execute" in command) {
            globalCommands.push(command.data.toJSON());
        } else {
            console.log(
                `[WARNING] The command at ${filePath} is missing a required "data" or "execute" property.`
            );
        }
    }
}

// Loop through each dev command folder
for (const folder of commandFolders.filter((folder) => folder === "dev")) {
    // Grab all the command files from the commands directory you created earlier
    const commandsPath = path.join(foldersPath, folder);
    const commandFiles = fs
        .readdirSync(commandsPath)
        .filter((file) => file.endsWith(".js"));
    // Grab the SlashCommandBuilder#toJSON() output of each command's data for deployment
    for (const file of commandFiles) {
        const filePath = path.join(commandsPath, file);
        const command = require(filePath);
        if ("data" in command && "execute" in command) {
            devCommands.push(command.data.toJSON());
        } else {
            console.log(
                `[WARNING] The command at ${filePath} is missing a required "data" or "execute" property.`
            );
        }
    }
}

// Construct and prepare an instance of the REST module
const rest = new REST().setToken(process.env.TOKEN);
// Get the type of deployment (deletion or deployment) from the command line arguments
const action = process.argv[2];
const commandType = process.argv[3];

if (action === "delete") {
    // ============================ Command Deletion ============================
    if (commandType === "guild") {
        // Guild commands
        rest.put(
            Routes.applicationGuildCommands(
                process.env.CLIENT_ID,
                process.env.DEV_GUILD_ID
            ),
            { body: [] }
        )
            .then(() => console.log("Successfully deleted all guild commands."))
            .catch(console.error);
    } else if (commandType == "global") {
        // Global commands
        rest.put(Routes.applicationCommands(process.env.CLIENT_ID), {
            body: [],
        })
            .then(() =>
                console.log("Successfully deleted all application commands.")
            )
            .catch(console.error);
    } else {
        console.error(
            "Please provide a valid command type (either 'guild' or 'global')."
        );
    }
} else if (action === "deploy") {
    // ============================ Command Deployment ============================
    (async () => {
        try {
            if (commandType === "global") {
                console.log(
                    `Started refreshing ${globalCommands.length} global commands.`
                );

                // The put method is used to fully refresh all commands in the guild with the current set
                const globalData = await rest.put(
                    Routes.applicationCommands(process.env.CLIENT_ID),
                    { body: globalCommands }
                );

                console.log(
                    `Successfully reloaded ${globalData.length} global commands`
                );
            } else if (commandType === "guild") {
                console.log(
                    `Started refreshing ${devCommands.length} guild commands.`
                );

                const devData = await rest.put(
                    Routes.applicationGuildCommands(
                        process.env.CLIENT_ID,
                        process.env.DEV_GUILD_ID
                    ),
                    { body: devCommands }
                );

                console.log(
                    `Successfully reloaded ${devData.length} guild commands.`
                );
            } else {
                console.error(
                    "Please provide a valid command type (either 'guiuld' or 'global')."
                );
            }
        } catch (error) {
            // And of course, make sure you catch and log any errors!
            console.error(error);
        }
    })();
} else {
    console.error(
        "Please provide a valid deployment type (either 'delete' or 'deploy')."
    );
}
