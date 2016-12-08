package org.telegram;

import org.telegram.commands.*;
import org.telegram.fluent.Answer;
import org.telegram.mamot.services.DAO;
import org.telegram.services.Events;
import org.telegram.services.LocalizationService;
import org.telegram.services.SimpleSkin;
import org.telegram.services.Weather;
import org.telegram.services.impl.*;
import org.telegram.sokoban.view.GameFieldPrinter;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;
import org.telegram.timertasks.CustomTimerTask;
import org.telegram.timertasks.DailyTask;
import org.telegram.updateshandlers.CommandsHandler;
import org.telegram.updateshandlers.WebHookExampleHandlers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

import static java.time.LocalDateTime.now;
import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.telegram.services.Emoji.ANCHOR;
import static org.telegram.services.Emoji.SAILBOAT;

public class Main {
    private static final String LOGTAG = "MAIN";
    private static final String SBT_TEAM_CHAT_ID = "-145229307";//"229669496";;

    public static void main(String[] args) {
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());
        try {
            BotLogger.registerLogger(new BotsFileHandler());
        } catch (IOException e) {
            BotLogger.severe(LOGTAG, e);
        }

        try {
            TelegramBotsApi telegramBotsApi = createTelegramBotsApi();
            try {
                final LocalizationService localizationService = new LocalizationService();
                final DAO dao = new DAO();
                WeatherPrinter weatherPrinter = new WeatherPrinter(localizationService, dao);
                WeatherResource weatherResource = new WeatherResource();
                Weather weather = new WeatherLoggingDecorator(
                        new SimpleWeather(weatherPrinter, weatherResource), localizationService);

                QuoteCommand quote = new QuoteCommand(new MessageFromURL(new QuoteResource(), new QuotePrinter()));
                AdviceCommand advice = new AdviceCommand(new MessageFromURL(new AdviceResource(), new AdvicePrinter()));
                JokeCommand joke = new JokeCommand(new MessageFromURL(new JokeResource(), new JokePrinter()));

                telegramBotsApi.registerBot(new CommandsHandler(getTimerTasks(),
                        quote, joke, advice,
                        new WeatherCommand(weather),
                        new WhoCommand(),
                        new PollCommand(),
                        new SupCommand(dao),
                        new ITQuoteCommand(dao),
                        new DieCommand(),
                        new DebugCommand(),
                        new Game2048Command(dao),
                        getGameCommand(),
                        new BardakCommand(dao)));
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private static List<CustomTimerTask> getTimerTasks() {
        List<CustomTimerTask> tasks = newArrayList();
        for (Events e : Events.values()) {
           tasks.add(new DailyTask(e.name(), -1) {
               @Override
               protected LocalDateTime startAt() {
                   return now().with(e.time());
               }

               @Override
               public void execute() {
                   new Answer(sender).to(SBT_TEAM_CHAT_ID)
                           .sticker(e.sticker())
                           .message(e.msg()).disableWebPagePreview()
                           .send();
               }
           });
        }
        return tasks;
    }

    private static GameCommand getGameCommand() {
        return new GameCommand(newArrayList(
                new GameFieldPrinter(new SimpleSkin("\uD83D\uDDB1", "\uD83C\uDF84", "\uD83D\uDE00", "\uD83D\uDCA9", "\uD83D\uDEBD")),
                new GameFieldPrinter(new SimpleSkin("\uD83D\uDDB1", "\uD83C\uDF0A", "\uD83C\uDF2A", SAILBOAT.toString(), ANCHOR.toString())),
                new GameFieldPrinter(new SimpleSkin("\uD83D\uDDB1", "\uD83D\uDC8B", "\uD83C\uDFC2", "\uD83D\uDC6F", "\uD83D\uDEC1"))));
    }

    private static TelegramBotsApi createTelegramBotsApi() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi;
        if (!BuildVars.useWebHook) {
            // Default (long polling only)
            telegramBotsApi = createLongPollingTelegramBotsApi();
        } else if (!BuildVars.pathToCertificatePublicKey.isEmpty()) {
            // Filled a path to a pem file ? looks like you're going for the self signed option then, invoke with store and pem file to supply.
            telegramBotsApi = createSelfSignedTelegramBotsApi();
            telegramBotsApi.registerBot(new WebHookExampleHandlers());
        } else {
            // Non self signed, make sure you've added private/public and if needed intermediate to your cert-store.
            telegramBotsApi = createNoSelfSignedTelegramBotsApi();
            telegramBotsApi.registerBot(new WebHookExampleHandlers());
        }
        return telegramBotsApi;
    }

    /**
     * @brief Creates a Telegram Bots Api to use Long Polling (getUpdates) bots.
     * @return TelegramBotsApi to register the bots.
     */
    private static TelegramBotsApi createLongPollingTelegramBotsApi() {
        return new TelegramBotsApi();
    }

    /**
     * @brief Creates a Telegram Bots Api to use Long Polling bots and webhooks bots with self-signed certificates.
     * @return TelegramBotsApi to register the bots.
     *
     * @note https://core.telegram.org/bots/self-signed#java-keystore for generating a keypair in store and exporting the pem.
    *  @note Don't forget to split the pem bundle (begin/end), use only the public key as input!
     */
    private static TelegramBotsApi createSelfSignedTelegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(BuildVars.pathToCertificateStore, BuildVars.certificateStorePassword, BuildVars.EXTERNALWEBHOOKURL, BuildVars.INTERNALWEBHOOKURL, BuildVars.pathToCertificatePublicKey);
    }

    /**
     * @brief Creates a Telegram Bots Api to use Long Polling bots and webhooks bots with no-self-signed certificates.
     * @return TelegramBotsApi to register the bots.
     *
     * @note Coming from a set of pem files here's one way to do it:
     * @code{.sh}
     * openssl pkcs12 -export -in public.pem -inkey private.pem > keypair.p12
     * keytool -importkeystore -srckeystore keypair.p12 -destkeystore server.jks -srcstoretype pkcs12
     * #have (an) intermediate(s) to supply? first:
     * cat public.pem intermediate.pem > set.pem (use set.pem as -in)
     * @endcode
     */
    private static TelegramBotsApi createNoSelfSignedTelegramBotsApi() throws TelegramApiException {
        return new TelegramBotsApi(BuildVars.pathToCertificateStore, BuildVars.certificateStorePassword, BuildVars.EXTERNALWEBHOOKURL, BuildVars.INTERNALWEBHOOKURL);
    }
}
