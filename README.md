# linkedinbot

Local run:

1) Download and confgiure postgres in **Docker** by folowwing manual **https://hub.docker.com/_/postgres**
2) Create your own chat-bot for testing. To perform this u need to use **https://t.me/BotFather**
3) Create application-dev.yml in /resources directory. Copy all data from **application.yml** and replace envs **($ENV_NAME)** with your values 
   - **${BOT_NAME}** - bot name
   - **${BOT_TOKEN}** - token from  https://t.me/BotFather
   - **${ADMIN_NICKNAME}** - "" 
   - **${ADMIN_PASS}** - ""
   - **${BOT_RANDOM_LIMIT}** - any number (how many random account to get on button click) 
   - **${PORT}** - any port where your app will run locally, 5500 for examaple 
   - **${DATABASE_HOST}** - localhost
   - **${DATABASE_PORT}** - 5432 (default postgres port)
   - **${DATABASE_NAME}** - database name (default postgres) 
   - **${DATABASE_SCHEMA}** - public (default postgres schema)
   - **${DATABASE_USERNAME}** - postgres username u assign
   - **${DATABASE_PASSWORD}** - postgres passowrd u assign 
   
   
Run Application with **"dev"** profile. 
