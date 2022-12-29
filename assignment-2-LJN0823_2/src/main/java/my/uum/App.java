package my.uum;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * This class is for connecting this app to database class with own telegram bot.
 *
 * @author Li Jia Ni
 */
public class App {
    public static void main(String[] args) {
        try {
            Database db = new Database();
            //Instantiate Telegram Bots API
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            //Register our bot
            botsApi.registerBot(new Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
