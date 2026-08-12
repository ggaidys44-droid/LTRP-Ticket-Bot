LTRP TICKET BOT — NUO NULIO

1. GitHub
- Sukurk naują repository.
- Įkelk VISUS šio aplanko failus.
- NEKELK .env ir tikro Discord tokeno.

2. Discord Developer Portal
- Sukurk Discord Application/Bot.
- Nukopijuok bot tokeną.
- Bot -> Privileged Gateway Intents:
  įjunk MESSAGE CONTENT INTENT.
  įjunk SERVER MEMBERS INTENT.
- OAuth2 -> URL Generator:
  pasirink bot ir applications.commands.
  Suteik botui bent:
  View Channels
  Send Messages
  Manage Messages
  Manage Channels
  Read Message History
  Embed Links
  Attach Files

3. Discord serveris
- Sukurk kategoriją, kurioje bus ticket kanalai.
- Sukurk Support/Administracijos rolę.
- Sukurk log kanalą transkriptams.
- Nukopijuok jų ID:
  Developer Mode -> dešinys pelės -> Copy ID.

4. Railway
- New Project -> Deploy from GitHub Repo.
- Pasirink savo GitHub repository.
- Railway paims railway.json pats.
- Variables pridėk:
  DISCORD_TOKEN = tavo tikras bot tokenas
  TICKET_CATEGORY_ID = kategorijos ID
  SUPPORT_ROLE_ID = support rolės ID
  LOG_CHANNEL_ID = log kanalo ID

5. Paleidimas
- Railway turi pats paleisti Maven build.
- Start command jau įrašytas railway.json.
- NIEKO papildomai Start Command laukelyje rašyti nereikia.

6. Discord
- Boto serveryje parašyk:
  !setup_ticket
- Atsiras pagalbos-ticket panelė su kategorijų pasirinkimu.
- Pasirinkus kategoriją sukuriamas ticket kanalas.
- Uždarius ticket siunčiamas HTML transkriptas į LOG_CHANNEL_ID.

SVARBU:
- Tikras DISCORD_TOKEN turi būti tik Railway Variables.
- Jei tokeną buvai viešai įkėlęs ar rodęs kitiems, Discord Developer Portal iškart sugeneruok naują tokeną.
