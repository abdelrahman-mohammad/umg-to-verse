const { SlashCommandBuilder, EmbedBuilder } = require("discord.js");
const axios = require("axios");
const fs = require("fs");
require("dotenv").config();

module.exports = {
    cooldown: 5,
    data: new SlashCommandBuilder()
        .setName("get-conversion")
        .setDescription("Get a specific conversion by ID.")
        .addStringOption((option) =>
            option
                .setName("id")
                .setDescription("The ID of the conversion.")
                .setRequired(true)
                .setMinLength(36)
                .setMaxLength(36)
        ),
    async execute(interaction) {
        await interaction.deferReply({ ephemeral: true });

        // ============================ Interaction Variables ============================
        const { user, member, id, client } = interaction;

        // ============================ Axios Instance ============================
        const http = axios.create({
            baseURL: process.env.API_BASE_URL,
        });

        // ============================ Ephemeral Message ============================
        try {
            // Send the request to the API and get the response
            const conversionResponse = await http.request({
                url: `/umg-to-verse/${interaction.options.getString("id")}`,
                method: "get",
                headers: {
                    "X-Api-Key": process.env.API_KEY,
                },
            });

            // Set the conversions embed
            const conversionEmbed = new EmbedBuilder()
                .setColor(0x0466c8)
                .setTimestamp()
                .setTitle(`Conversion #${conversionResponse.data.id}`)
                .setDescription(`\`\`\`${conversionResponse.data.output}\`\`\``)
                .setFooter({
                    text: "Tip: Use the 'file' subcommand for larger UMG objects.",
                    iconURL: "../../assets/verse-tools-square.png",
                });

            // Edit the reply with the conversions embed
            await interaction.editReply({
                embeds: [conversionEmbed],
                ephemeral: true,
            });
        } catch (error) {
            // ============================ Error Handling ============================
            // -------------- Error Variables --------------
            const { status, exception, message } = error.response.data;

            // -------------- Error Embed --------------
            const errorEmbed = new EmbedBuilder()
                .setColor(0xff0000)
                .setTitle(`<:exit:1276830233214976000> ERROR: ${exception}`)
                .setDescription(`Message:\n\`\`\`${message}\`\`\``)
                .addFields(
                    { name: "\u200B", value: "\u200B" },
                    {
                        name: "Status",
                        value: `\`\`\`${status}\`\`\``,
                        inline: true,
                    },
                    {
                        name: "Error",
                        value: `\`\`\`${error.response.data.error}\`\`\``,
                        inline: true,
                    },
                    {
                        name: "Exception",
                        value: `\`\`\`${exception}\`\`\``,
                        inline: true,
                    }
                )
                .setTimestamp()
                .setFooter({
                    text: "If this error persists, open a ticket.",
                    iconURL: "../../assets/verse-tools-square.png",
                });

            // -------------- Edit the reply with the error embed --------------
            await interaction.deleteReply();
            return await interaction.followUp({
                embeds: [errorEmbed],
                ephemeral: true,
            });
        }
    },
};
