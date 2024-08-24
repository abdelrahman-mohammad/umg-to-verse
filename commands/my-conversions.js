const { SlashCommandBuilder, EmbedBuilder, ActionRowBuilder, ButtonBuilder, ButtonStyle } = require("discord.js");
const axios = require("axios");
require("dotenv").config();

// Store active collectors for each user
const activeCollectors = new Map();

module.exports = {
    cooldown: 5,
    data: new SlashCommandBuilder()
        .setName("my-conversions")
        .setDescription("A list of your UMG to Verse conversions.")
        .addIntegerOption((option) => option.setName("page").setDescription("The page number to view").setMinValue(1)),
    async execute(interaction) {
        await interaction.deferReply({ ephemeral: true });

        const { user, client } = interaction;
        let page = interaction.options.getInteger("page") || 1;
        const itemsPerPage = 10;

        // Stop any existing collector for this user
        if (activeCollectors.has(user.id)) {
            activeCollectors.get(user.id).stop();
        }

        const http = axios.create({
            baseURL: process.env.API_BASE_URL,
            headers: {
                "X-Api-Key": process.env.API_KEY,
            },
        });

        let allConversions = [];

        async function fetchConversions() {
            try {
                const response = await http.get(`/users/${user.id}/conversions`);
                allConversions = response.data;
            } catch (error) {
                console.error("Error fetching conversions:", error);
                allConversions = [];
            }
        }

        await fetchConversions();

        function getPageEmbed(currentPage) {
            if (allConversions.length === 0) {
                return new EmbedBuilder()
                    .setColor(0x0466c8)
                    .setTitle("🔄 Your UMG to Verse Conversions")
                    .setDescription("You don't have any conversions yet.")
                    .setTimestamp();
            }

            const totalPages = Math.ceil(allConversions.length / itemsPerPage);
            currentPage = Math.max(1, Math.min(currentPage, totalPages));
            const startIndex = (currentPage - 1) * itemsPerPage;
            const endIndex = Math.min(startIndex + itemsPerPage, allConversions.length);
            const currentPageConversions = allConversions.slice(startIndex, endIndex);

            const conversionList = currentPageConversions
                .map((conversion, index) => {
                    const date = conversion.createdAt ? new Date(conversion.createdAt).toLocaleString() : "Unknown Date";
                    return `**${startIndex + index + 1}.** \`${conversion.id}\` - ${date}`;
                })
                .join("\n");

            return new EmbedBuilder()
                .setColor(0x0466c8)
                .setTitle(`🔄 Your UMG to Verse Conversions`)
                .setDescription(conversionList || "No conversions on this page.")
                .setFooter({
                    text: `Page ${currentPage}/${totalPages} • Total Conversions: ${allConversions.length}`,
                    iconURL: client.user.avatarURL(),
                })
                .setTimestamp();
        }

        function getActionRow(currentPage) {
            const totalPages = Math.ceil(allConversions.length / itemsPerPage);
            return new ActionRowBuilder().addComponents(
                new ButtonBuilder()
                    .setCustomId("prev_page")
                    .setLabel("Previous")
                    .setStyle(ButtonStyle.Primary)
                    .setDisabled(currentPage <= 1),
                new ButtonBuilder()
                    .setCustomId("next_page")
                    .setLabel("Next")
                    .setStyle(ButtonStyle.Primary)
                    .setDisabled(currentPage >= totalPages)
            );
        }

        async function updateMessage(interactionToUpdate, newPage) {
            const embed = getPageEmbed(newPage);
            const row = getActionRow(newPage);

            const messageContent = {
                embeds: [embed],
                components: [row],
            };

            try {
                await interactionToUpdate.update(messageContent);
            } catch (error) {
                console.error("Error updating message:", error);
                if (error.code === 10062) {
                    // Unknown interaction
                    await interaction.followUp({
                        content: "The previous interaction has expired. Here's the updated list:",
                        ...messageContent,
                        ephemeral: true,
                    });
                } else {
                    throw error;
                }
            }
        }

        await interaction.editReply({
            embeds: [getPageEmbed(page)],
            components: [getActionRow(page)],
        });

        const filter = (i) => i.user.id === user.id;
        const collector = interaction.channel.createMessageComponentCollector({ filter, time: 300000 }); // 5 minutes

        // Store the new collector
        activeCollectors.set(user.id, collector);

        collector.on("collect", async (i) => {
            const totalPages = Math.ceil(allConversions.length / itemsPerPage);
            if (i.customId === "prev_page") {
                page = Math.max(1, page - 1);
            } else if (i.customId === "next_page") {
                page = Math.min(totalPages, page + 1);
            }

            try {
                await updateMessage(i, page);
            } catch (error) {
                console.error("Error in collector:", error);
                await i.reply({ content: "An error occurred. Please try the command again.", ephemeral: true }).catch(console.error);
            }
        });

        collector.on("end", () => {
            activeCollectors.delete(user.id);
            interaction.editReply({ components: [] }).catch(console.error);
        });
    },
};
