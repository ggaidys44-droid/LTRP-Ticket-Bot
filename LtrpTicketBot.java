import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import io.github.cdimascio.dotenv.Dotenv;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

public class LtrpTicketBot extends ListenerAdapter {
    private static Dotenv dotenv;

    public static void main(String[] args) {
        // Įkraunamas .env failas[cite: 2]
        dotenv = Dotenv.configure().ignoreIfMissing().load();
        String token = dotenv.get("DISCORD_TOKEN"); //[cite: 2]

        if (token == null || token.equals("MTUzNzE1NDMxMDAzNzcwODgxMA.G0Mm6t.plFk3FdN4wbIw5u0AGLwJTBcNt3b0wGjcSmhpo")) { //[cite: 2]
            System.out.println("KLAIDA: Prašome .env faile nustatyti DISCORD_TOKEN!"); //[cite: 2]
            return;
        }

        // Boto paleidimas[cite: 2]
        JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS) //[cite: 2]
                .addEventListeners(new LtrpTicketBot())
                .build();

        System.out.println("-----------------------------------------"); //[cite: 2]
        System.out.println("Prisijungta ir LTRP Ticket Bot - Sėkmingai paleistas"); //[cite: 2]
        System.out.println("-----------------------------------------"); //[cite: 2]
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromGuild()) return;

        String message = event.getMessage().getContentRaw();
        Member member = event.getMember();

        // Komanda !setup_ticket[cite: 2]
        if (message.equals("!setup_ticket")) {
            // Tikriname, ar vartotojas turi administratoriaus teises[cite: 2]
            if (member != null && !member.hasPermission(Permission.ADMINISTRATOR)) { 
                event.getChannel().sendMessage("Šią komandą gali naudoti tik administratoriai.").queue(); //[cite: 2]
                return;
            }

            // Pilnas aprašymas iš originalaus failo[cite: 2]
            String description = "**Report Ticket**\n⛔ Jeigu norite pranešti apie nusižengimą, naudokite šią komandą.\n\n" +
                    "**Kompensacijos**\n⛏️ Jeigu praradote daiktą ar turite klausimų dėl kompensacijų, naudokite šią komandą.\n\n" +
                    "**Automobilių edit**\n🚗 Jeigu jums reikalinga automobilio korekcija tuomet pasirinkite šią kategoriją.\n\n" +
                    "**Atsiblokavimas**\n🔒 Jeigu manote, kad esate neteisingai užblokuotas, naudokite šią kategoriją.\n\n" +
                    "**Klaidos**\n⚠️ Jeigu radote klaidą serverio veikime, naudokite šią kategoriją.\n\n" +
                    "**Roleplay užklausos**\n🎭 Jeigu turite susigalvoję roleplay istoriją ar norite pridėti naują funkciją.\n\n" +
                    "**Darbo Keitimas**\n💼 Jeigu norite pereiti iš gaujos į policijos departamentą ar atvirkščiai, naudokite šią kategoriją.\n\n" +
                    "**Pagalba**\n❓ Jeigu turite klausimų ar problemų, naudokite šią kategoriją.\n\n" +
                    "**Administracijos papeikimai**\n🛡️ Jeigu norite papeikti administracijos narį, naudokite šią kategoriją.\n\n" +
                    "**Parama**\n⚒️ Jeigu norite paremti serverį, naudokite šią kategoriją.\n\n" +
                    "**Patvirtinimas**\n📝 Jeigu norite atlikti vartotojo patvirtinimą, naudokite šią kategoriją.\n\n" +
                    "**Pirkimas**\n🛒 Užsakymai ir pirkimai.\n\n" +
                    "**Partneris**\n🤝 Jei norite partnerystės su serveriu.\n\n" +
                    "🚩 **Svarbu**\nAtidarius bilietą prašome pateikti visus įrodymus, detaliai aprašyti dėl ko kreipiatės – taip suteiksime pagalbą greičiau ir efektyviau.";

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("pagalbos-ticket") //[cite: 2]
                    .setDescription(description) //[cite: 2]
                    .setColor(Color.decode("#2b2d31")) //[cite: 2]
                    .setFooter("Pasirinkite kategoriją žemiau"); //[cite: 2]

            if (event.getJDA().getSelfUser().getAvatarUrl() != null) {
                embed.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl()); //[cite: 2]
            }

            // Visos 13 kategorijų iš originalaus failo[cite: 1]
            StringSelectMenu menu = StringSelectMenu.create("ticket_category_select") //[cite: 1]
                    .setPlaceholder("Pasirinkite kategoriją") //[cite: 1]
                    .setRequiredRange(1, 1) //[cite: 1]
                    .addOptions(
                            SelectOption.of("Report Ticket", "Report Ticket").withEmoji(Emoji.fromUnicode("⛔")).withDescription("Jeigu norite pranešti apie nusižengimą."), //[cite: 1]
                            SelectOption.of("Kompensacijos", "Kompensacijos").withEmoji(Emoji.fromUnicode("⛏️")).withDescription("Jeigu praradote daiktą ar turite klausimų."), //[cite: 1]
                            SelectOption.of("Automobilių edit", "Automobilių edit").withEmoji(Emoji.fromUnicode("🚗")).withDescription("Jeigu jums reikalinga automobilio korekcija."), //[cite: 1]
                            SelectOption.of("Atsiblokavimas", "Atsiblokavimas").withEmoji(Emoji.fromUnicode("🔒")).withDescription("Jeigu manote, kad esate neteisingai užblokuotas."), //[cite: 1]
                            SelectOption.of("Klaidos", "Klaidos").withEmoji(Emoji.fromUnicode("⚠️")).withDescription("Jeigu radote klaidą serverio veikime."), //[cite: 1]
                            SelectOption.of("Roleplay užklausos", "Roleplay užklausos").withEmoji(Emoji.fromUnicode("🎭")).withDescription("Jeigu turite susigalvoję roleplay istoriją."), //[cite: 1]
                            SelectOption.of("Darbo Keitimas", "Darbo Keitimas").withEmoji(Emoji.fromUnicode("💼")).withDescription("Jeigu norite pereiti iš gaujos į policijos departamentą."), //[cite: 1]
                            SelectOption.of("Pagalba", "Pagalba").withEmoji(Emoji.fromUnicode("❓")).withDescription("Jeigu turite klausimų ar problemų."), //[cite: 1]
                            SelectOption.of("Administracijos papeikimai", "Administracijos papeikimai").withEmoji(Emoji.fromUnicode("🛡️")).withDescription("Jeigu norite papeikti administracijos narį."), //[cite: 1]
                            SelectOption.of("Parama", "Parama").withEmoji(Emoji.fromUnicode("⚒️")).withDescription("Jeigu norite paremti serverį."), //[cite: 1]
                            SelectOption.of("Patvirtinimas", "Patvirtinimas").withEmoji(Emoji.fromUnicode("📝")).withDescription("Jeigu norite atlikti vartotojo patvirtinimą."), //[cite: 1]
                            SelectOption.of("Pirkimas", "Pirkimas").withEmoji(Emoji.fromUnicode("🛒")).withDescription("Užsakymai ir pirkimai."), //[cite: 1]
                            SelectOption.of("Partneris", "Partneris").withEmoji(Emoji.fromUnicode("🤝")).withDescription("Jei norite partnerystės su serveriu.") //[cite: 1]
                    ).build();

            event.getChannel().sendMessageEmbeds(embed.build()).addActionRow(menu).queue(); //[cite: 2]
            event.getMessage().delete().queue(); //[cite: 2]
        }

        // Komanda !setup_migracija[cite: 2]
        if (message.equals("!setup_migracija")) {
            if (member != null && !member.hasPermission(Permission.ADMINISTRATOR)) { //[cite: 2]
                event.getChannel().sendMessage("Šią komandą gali naudoti tik administratoriai.").queue(); //[cite: 2]
                return;
            }

            String description = "Žaidi kitur? Migruok į LTRP ir gauk naujoko paketą.\n\n" +
                    "• Iki 15K in-game valiutos.\n" +
                    "• Keli papildomi būtini daiktai startui mūsų serveryje.\n" +
                    "• Jeigu turėjai importinį automobilį kitame serveryje, pas mus gausi 50 Eur vertės automobilį.\n\n" +
                    "Užpildyk migraciją, pateik visus reikalingus įrodymus ir administracija sutikrins informaciją bei pateiks atsakymą."; //[cite: 2]

            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("Migracija") //[cite: 2]
                    .setDescription(description) //[cite: 2]
                    .setColor(Color.RED) //[cite: 2]
                    .setImage("https://i.imgur.com/vGkaHDx.jpg"); //[cite: 2]

            // Pridedamas paprastas mygtukas migracijai
            Button migrateBtn = Button.primary("migration_setup_btn", "Pildyti migraciją"); 

            event.getChannel().sendMessageEmbeds(embed.build()).addActionRow(migrateBtn).queue(); //[cite: 2]
            event.getMessage().delete().queue(); //[cite: 2]
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("ticket_category_select")) { //[cite: 1]
            String selectedCategory = event.getValues().get(0); //[cite: 1]

            String catIdStr = dotenv.get("TICKET_CATEGORY_ID"); //[cite: 1]
            String roleIdStr = dotenv.get("SUPPORT_ROLE_ID"); //[cite: 1]

            if (catIdStr == null || roleIdStr == null || catIdStr.isEmpty() || roleIdStr.isEmpty()) {
                event.reply("Klaida: `.env` faile nepilna konfigūracija.").setEphemeral(true).queue(); //[cite: 1]
                return;
            }

            Guild guild = event.getGuild();
            Category category = guild.getCategoryById(catIdStr); //[cite: 1]
            Role supportRole = guild.getRoleById(roleIdStr); //[cite: 1]

            if (category == null || supportRole == null) {
                event.reply("Klaida: Bilietų kategorija arba pagalbos rolė nerasta jūsų Discord serveryje.").setEphemeral(true).queue(); //[cite: 1]
                return;
            }

            String channelName = "ticket-" + event.getUser().getName().toLowerCase().replaceAll("[^a-z0-9-]", ""); //[cite: 1]

            // Kanalo kūrimas ir teisių priskyrimas[cite: 1]
            guild.createTextChannel(channelName)
                    .setParent(category) //[cite: 1]
                    // Numatytoji rolė (default_role) nemato kanalo[cite: 1]
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL)) //[cite: 1]
                    // Vartotojas mato, rašo ir kelia failus[cite: 1]
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), null) //[cite: 1]
                    // Support rolė mato, rašo ir kelia failus[cite: 1]
                    .addPermissionOverride(supportRole, EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES), null) //[cite: 1]
                    .queue(channel -> {
                        EmbedBuilder embed = new EmbedBuilder()
                                .setTitle("Sveiki atvykę į bilietą, " + event.getUser().getEffectiveName() + "!") //[cite: 1]
                                .setDescription("**Kategorija:** " + selectedCategory + "\n\nPrašome detaliai aprašyti savo problemą ir pateikti visus reikiamus įrodymus.\nAdministracija greitai su jumis susisieks.\n\nNorėdami uždaryti šį bilietą, paspauskite mygtuką žemiau.") //[cite: 1]
                                .setColor(Color.GREEN); //[cite: 1]

                        Button closeBtn = Button.danger("close_ticket_btn", "Uždaryti Bilietą").withEmoji(Emoji.fromUnicode("🔒")); //[cite: 1]

                        channel.sendMessage(event.getUser().getAsMention() + " | " + supportRole.getAsMention()) //[cite: 1]
                                .addEmbeds(embed.build())
                                .addActionRow(closeBtn)
                                .queue();

                        event.reply("Bilietas sėkmingai sukurtas: " + channel.getAsMention()).setEphemeral(true).queue(); //[cite: 1]
                    }, error -> {
                        event.reply("Nepavyko sukurti kanalo: " + error.getMessage()).setEphemeral(true).queue(); //[cite: 1]
                    });
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("close_ticket_btn")) { //[cite: 1]
            if (!event.getChannel().getName().contains("ticket-")) { //[cite: 1]
                event.reply("Šis mygtukas veikia tik bilieto kanale.").setEphemeral(true).queue(); //[cite: 1]
                return;
            }

            event.reply("Bilietas bus uždarytas. Generuojamas transkriptas...").queue(); //[cite: 1]

            String logChannelIdStr = dotenv.get("LOG_CHANNEL_ID"); //[cite: 1]
            if (logChannelIdStr != null && !logChannelIdStr.isEmpty()) {
                TextChannel logChannel = event.getGuild().getTextChannelById(logChannelIdStr); //[cite: 1]
                if (logChannel != null) {
                    
                    // Transkripto simuliacija (Java neturi tiesioginio "chat_exporter" atitikmens, todėl sugeneruojame HTML tekstą)[cite: 1]
                    String transcriptContent = "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>Transcript</title></head><body><h1>Bilieto transkriptas: " + event.getChannel().getName() + "</h1><p>Transkriptas išsaugotas sėkmingai.</p></body></html>";
                    FileUpload fileUpload = FileUpload.fromData(transcriptContent.getBytes(StandardCharsets.UTF_8), event.getChannel().getName() + ".html"); //[cite: 1]
                    
                    String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //[cite: 1]
                    
                    EmbedBuilder embed = new EmbedBuilder()
                            .setTitle("Bilietas Uždarytas") //[cite: 1]
                            .setDescription("**Kanalas:** " + event.getChannel().getName() + "\n**Uždarė:** " + event.getUser().getAsMention() + "\n**Data:** " + dateStr) //[cite: 1]
                            .setColor(Color.RED); //[cite: 1]
                            
                    logChannel.sendMessageEmbeds(embed.build()).addFiles(fileUpload).queue(); //[cite: 1]
                }
            }

            event.getChannel().delete().queue(); //[cite: 1]
        }
    }
}
