const { SlashCommandBuilder, EmbedBuilder } = require("discord.js");
const axios = require("axios");
const fs = require("fs");
require("dotenv").config();

module.exports = {
    cooldown: 5,
    data: new SlashCommandBuilder()
        .setName("umg-to-verse")
        .setDescription("Converts your UMG to Verse code.")
        .addSubcommand((subcommand) =>
            subcommand
                .setName("file")
                .setDescription("The UMG object as a file.")
                .addAttachmentOption((option) =>
                    option
                        .setName("input")
                        .setDescription("The UMG object as a file.")
                        .setRequired(true)
                )
        ),
    async execute(interaction) {
        await interaction.deferReply({ ephemeral: true });

        // ============================ Interaction Variables ============================
        const { user, member, id, client } = interaction;

        // ============================ Axios Instance ============================
        const http = axios.create({
            baseURL: process.env.API_BASE_URL,
        });

        let content;

        // ============================ File Input ============================
        if (interaction.options.getSubcommand() === "file") {
            const attachment = interaction.options.getAttachment("input");
            if (!attachment.contentType.startsWith("text/plain")) {
                return interaction.editReply({
                    embeds: [
                        errorEmbed.setDescription(
                            "The file must be a text file."
                        ),
                    ],
                });
            } else {
                // -------------- Read the file content --------------
                content = await axios
                    .get(attachment.url)
                    .then((response) => response.data);
            }
        }

        // ============================ Public Message ============================
        try {
            const countResponse = await http.request({
                url: `users/${user.id}/conversions`,
                method: "get",
                headers: {
                    "X-Api-Key": process.env.API_KEY,
                },
            });

            // -------------- Public Embed --------------
            const publicEmbed = new EmbedBuilder()
                .setColor(0x0466c8)
                .setTitle(
                    `${member.displayName}'s UMG to Verse Conversion #${countResponse.data.length}`
                )
                .setThumbnail(user.avatarURL())
                .addFields({
                    name: "Widget Type",
                    value: `\`\`\`WIP\`\`\``,
                    inline: true,
                })
                .setTimestamp()
                .setFooter({
                    iconURL: "../../assets/verse-tools-square.png",
                });
            await interaction.deleteReply();
            await interaction.followUp({
                embeds: [publicEmbed],
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
                    iconURL: client.user.avatarURL(),
                });

            // -------------- Edit the reply with the error embed --------------
            await interaction.deleteReply();
            return await interaction.followUp({
                embeds: [errorEmbed],
                ephemeral: true,
            });
        }

        // ============================ Ephemeral Message ============================
        try {
            // -------------- Send the request to the API and get the response --------------
            const response = await http.request({
                url: "umg-to-verse",
                method: "post",
                params: {
                    userId: user.id,
                },
                headers: {
                    "Content-Type": "text/plain",
                    "X-Api-Key": process.env.API_KEY,
                },
                data: content,
            });

            // -------------- Conversion Embed --------------
            const conversionEmbed = new EmbedBuilder()
                .setColor(0x0466c8)
                .setTimestamp()
                .setTitle(`Conversion #${response.data.id}`)
                .setDescription(`\`\`\`${response.data.output}\`\`\``)
                .setFooter({
                    text: "Tip: Use the 'as-file' subcommand for larger UMG objects.",
                    iconURL: client.user.avatarURL(),
                });

            // -------------- Edit the reply with the conversion embed --------------
            await interaction.followUp({
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
                    iconURL: client.user.avatarURL(),
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
