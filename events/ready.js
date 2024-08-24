const { Events } = require("discord.js");
const wait = require("node:timers/promises").setTimeout;

module.exports = {
    name: Events.ClientReady,
    once: true,
    async execute(client) {
        console.log(`Ready! Logged in as ${client.user.tag}`);
        while (true) {
            // wait 10 minutes
            await wait(10 * 60 * 1000);
            console.log("I'm still here!");
        }
    },
};
