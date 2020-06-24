# Steam Player's notifications app

It consists of a simple app which, depending on your list of Steam friends, check their status to find out if they are
online playing any specified Steam's game.

In this case, the program sends a message to a specified Telegram group to notify that your friend is currently online. Also, it will notify whether your friend goes offline.

The app acts as an intermediary to consult information from the Steam API and send it through the Telegram API.
## Running the bot

### Setup

In order to run the app, you will need to set up two different config files as follows:
* [`conf/steam.conf`] you will have to get a Steam API key, and you will need to know which is your Steam ID to get your list of friends.

    ```
    config {
        apiKey = "**************************"
        myId = "*****************"
        gameId = "730" ;CS:GO
    }
    ```
  
* [`conf/telegram.conf`] you will have to create a telegram bot and get its key and to know the chatId
where you want to send the notifications.

    ```
    config {
        botKey = "bot1*******:A******************"
            chatId {
                prod = "-3********"
                dev = "-4********"
            }
    }
    ```
  
  #### Local environment
  Add a .env file with the following variable: `APP_ENV=local`. If you are using IntelliJ you can also add
 the environment variable in Run > Edit Configurations > Environment variables`
  


### TODOs:

- Add logger :warning:
- Add Cache :white_check_mark:
- Tests :x:
- Implement args to set the scheduler :x:
- Dockerize the app :white_check_mark:
- Implement Telegram bot commands :x:
