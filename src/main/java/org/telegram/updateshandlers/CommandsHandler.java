package org.telegram.updateshandlers;

import org.telegram.BotConfig;
import org.telegram.commands.HelpCommand;
import org.telegram.services.Emoji;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    public static final String LOGTAG = "COMMANDSHANDLER";

    public CommandsHandler(BotCommand... commands) {
        for (BotCommand cmd : commands) {
            register(cmd);
        }
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((sender, message) -> {
            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId().toString());
            answer.setText("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE);
            try {
                sender.sendMessage(answer);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            helpCommand.execute(sender, message.getFrom(), message.getChat(), new String[] {});
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId().toString());
                echoMessage.setText("Hey heres your message:\n" + message.getText());

                try {
                    sendMessage(echoMessage);
                } catch (TelegramApiException e) {
                    BotLogger.error(LOGTAG, e);
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BotConfig.COMMANDS_USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.COMMANDS_TOKEN;
    }
}