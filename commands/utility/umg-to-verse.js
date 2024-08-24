const { SlashCommandBuilder, EmbedBuilder, AttachmentBuilder } = require("discord.js");
const axios = require("axios");
const fs = require("fs");
const path = require("path");
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
                .addAttachmentOption((option) => option.setName("input").setDescription("The UMG object as a file.").setRequired(true))
        ),
    async execute(interaction) {
        await interaction.deferReply({ ephemeral: true });

        // ============================ Interaction Variables ============================
        const { user, member, client } = interaction;

        // ============================ Axios Instance ============================
        const http = axios.create({
            baseURL: process.env.API_BASE_URL,
            headers: {
                "X-Api-Key": process.env.API_KEY,
                "Content-Type": "application/json",
            },
        });

        // ============================ Ensure User Exists ============================
        async function ensureUserExists() {
            try {
                await http.get(`users/${user.id}`);
            } catch (error) {
                if (error.response && error.response.status === 404) {
                    // User doesn't exist, create a new user
                    await http.post("users", {
                        username: user.username,
                        discordId: user.id,
                        email: user.id, // For now
                    });
                } else {
                    throw error; // Re-throw if it's not a 404 error
                }
            }
        }

        try {
            await ensureUserExists();
        } catch (error) {
            console.error("Error ensuring user exists:", error);
            const errorEmbed = new EmbedBuilder()
                .setColor(0xff0000)
                .setTitle(`<:exit:1276830233214976000> ERROR: User Registration Failed`)
                .setDescription("An error occurred while registering your user. Please try again later.")
                .setTimestamp()
                .setFooter({
                    text: "If this error persists, open a ticket.",
                    iconURL: client.user.avatarURL(),
                });
            return interaction.editReply({ embeds: [errorEmbed] });
        }

        let content;

        // ============================ File Input ============================
        if (interaction.options.getSubcommand() === "file") {
            const attachment = interaction.options.getAttachment("input");
            if (!attachment.contentType.startsWith("text/plain")) {
                const errorEmbed = new EmbedBuilder()
                    .setColor(0xff0000)
                    .setTitle(`<:exit:1276830233214976000> ERROR: Wrong File Type`)
                    .setDescription("```The file must be a text (.txt) file.```")
                    .setTimestamp()
                    .setFooter({
                        text: "If this error persists, open a ticket.",
                        iconURL: client.user.avatarURL(),
                    });

                return interaction.editReply({
                    embeds: [errorEmbed],
                });
            } else {
                content = await axios.get(attachment.url).then((response) => response.data);
            }
        }

        // ============================ Public Embed ============================
        async function sendPublicEmbed() {
            try {
                const countResponse = await http.request({
                    url: `users/${user.id}/conversions`,
                    method: "get",
                });

                const conversionCount = countResponse.data.length;
                const userRank = calculateRank(conversionCount);

                const publicEmbed = new EmbedBuilder()
                    .setColor(0x0466c8)
                    .setTitle("<:convert:1276935670526509160> UMG to Verse Conversion")
                    .setDescription(`${member.displayName} just converted a UMG to Verse!\nConvert yours now: </umg-to-verse file:1276920045594742902>`)
                    .addFields({ name: "Total Conversions", value: `${conversionCount}`, inline: true }, { name: "Rank", value: userRank, inline: true })
                    .setTimestamp()
                    .setThumbnail(user.avatarURL())
                    .setFooter({
                        text: `Convert more UMG to Verse to rank up!`,
                        iconURL: client.user.avatarURL(),
                    });

                await interaction.channel.send({
                    embeds: [publicEmbed],
                });
            } catch (error) {
                console.error(error);
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

                await interaction.deleteReply();
                return await interaction.followUp({
                    embeds: [errorEmbed],
                    ephemeral: true,
                });
            }
        }

        function calculateRank(conversionCount) {
            if (conversionCount >= 1000) return "<:purple:1276944781943246899> Legendary Converter";
            if (conversionCount >= 500) return "<:diamond:1276944623603945492> Diamond Converter";
            if (conversionCount >= 250) return "<:gold:1276944780265259199> Gold Converter";
            if (conversionCount >= 100) return "<:silver:1276944776934985812> Silver Converter";
            if (conversionCount >= 50) return "<:bronze:1276944778843525222> Bronze Converter";
            if (conversionCount >= 25) return "<:gold_star:1276945526759358564> Rising Star";
            if (conversionCount >= 10) return "<:apprentice:1276945330826641493> Apprentice";
            return "🌱 Novice";
        }

        // ============================ Ephemeral Message ============================
        try {
            // -------------- Send the request to the API and get the response --------------
            const response = await http.post("umg-to-verse", content, {
                params: { userId: user.id },
                headers: { "Content-Type": "text/plain" },
            });

            if (!response.data || !response.data.output) {
                throw new Error("Unexpected API response structure");
            }

            const conversionEmbed = new EmbedBuilder().setColor(0x0466c8).setTimestamp().setTitle(`Conversion #${response.data.id}`).setFooter({
                text: "Tip: Use the 'file' subcommand for larger UMG objects.",
                iconURL: client.user.avatarURL(),
            });

            if (response.data.output.length < 4090) {
                conversionEmbed.setDescription(`\`\`\`${response.data.output}\`\`\``);

                await sendPublicEmbed();
                await interaction.followUp({
                    embeds: [conversionEmbed],
                    ephemeral: true,
                });
            } else {
                conversionEmbed.setDescription(`\`\`\`Converted code was too long to include in the embed. See the attached file instead.\`\`\``);
                const assetsDir = path.join(__dirname, "..", "..", "assets");
                if (!fs.existsSync(assetsDir)) {
                    fs.mkdirSync(assetsDir, { recursive: true });
                }
                const filePath = path.join(assetsDir, `${response.data.id}.txt`);

                fs.writeFileSync(filePath, response.data.output);
                const file = new AttachmentBuilder(filePath, { name: `${response.data.id}.txt` });

                await sendPublicEmbed();
                await interaction.followUp({
                    embeds: [conversionEmbed],
                    files: [file],
                    ephemeral: true,
                });

                fs.unlink(filePath, (err) => {
                    if (err) {
                        console.error(`Error deleting file ${filePath}:`, err);
                    } else {
                        console.log(`Successfully deleted file ${filePath}`);
                    }
                });
            }
        } catch (error) {
            console.error(error);

            let errorEmbed;
            if (error.response && error.response.data) {
                const { status, exception, message } = error.response.data;
                errorEmbed = new EmbedBuilder()
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
                    );
            } else {
                errorEmbed = new EmbedBuilder()
                    .setColor(0xff0000)
                    .setTitle(`<:exit:1276830233214976000> ERROR`)
                    .setDescription(`An unexpected error occurred:\n\`\`\`${error.message}\`\`\``);
            }

            errorEmbed.setTimestamp().setFooter({
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
