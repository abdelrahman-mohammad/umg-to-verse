const { SlashCommandBuilder, EmbedBuilder, AttachmentBuilder } = require("discord.js");
const axios = require("axios");
const fs = require("fs").promises;
const path = require("path");
require("dotenv").config();

module.exports = {
    cooldown: 5,
    data: new SlashCommandBuilder()
        .setName("get-conversion")
        .setDescription("Get a specific conversion by ID.")
        .addStringOption((option) => option.setName("id").setDescription("The ID of the conversion.").setRequired(true).setMinLength(36).setMaxLength(36)),
    async execute(interaction) {
        await interaction.deferReply({ ephemeral: true });

        const { user, client } = interaction;
        const conversionId = interaction.options.getString("id");

        const http = axios.create({
            baseURL: process.env.API_BASE_URL,
            headers: {
                "X-Api-Key": process.env.API_KEY,
            },
        });

        try {
            const response = await http.get(`/umg-to-verse/${conversionId}`);
            const conversion = response.data;

            // Parse the timestamp or use current time if invalid
            let timestamp;
            try {
                timestamp = conversion.createdAt ? new Date(conversion.createdAt) : new Date();
                if (isNaN(timestamp.getTime())) throw new Error("Invalid date");
            } catch (error) {
                console.warn(`Invalid createdAt value: ${conversion.createdAt}. Using current time.`);
                timestamp = new Date();
            }

            const conversionEmbed = new EmbedBuilder()
                .setColor(0x0466c8)
                .setTitle(`Conversion #${conversion.id}`)
                .setTimestamp(timestamp)
                .setFooter({
                    text: `Requested by ${user.tag}`,
                    iconURL: user.avatarURL(),
                });

            if (conversion.output.length < 4090) {
                conversionEmbed.setDescription(`\`\`\`${conversion.output}\`\`\``);
                await interaction.editReply({ embeds: [conversionEmbed] });
            } else {
                const tempDir = path.join(__dirname, "..", "..", "temp");
                await fs.mkdir(tempDir, { recursive: true });
                const filePath = path.join(tempDir, `conversion-${conversion.id}.txt`);

                await fs.writeFile(filePath, conversion.output);
                const attachment = new AttachmentBuilder(filePath, { name: `conversion-${conversion.id}.txt` });

                conversionEmbed.setDescription("The conversion output is too large to display here. Please see the attached file.");

                await interaction.editReply({
                    embeds: [conversionEmbed],
                    files: [attachment],
                });

                // Clean up the temporary file
                setTimeout(() => fs.unlink(filePath).catch(console.error), 5000);
            }
        } catch (error) {
            console.error("Error fetching conversion:", error);

            let status, exception, message, errorDetails;

            if (error.response) {
                status = error.response.status;
                exception = error.response.data.exception || "UnknownException";
                message = error.response.data.message || "An unknown error occurred";
                errorDetails = error.response.data.error || "No additional error details";
            } else {
                status = 500;
                exception = "InternalError";
                message = error.message || "An unexpected error occurred";
                errorDetails = "No additional error details";
            }

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
                        value: `\`\`\`${errorDetails}\`\`\``,
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

            await interaction.editReply({
                embeds: [errorEmbed],
                ephemeral: true,
            });
        }
    },
};
