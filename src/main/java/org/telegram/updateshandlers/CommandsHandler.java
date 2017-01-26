package org.telegram.updateshandlers;

import org.telegram.commands.CallbackCommand;
import org.telegram.commands.HelpCommand;
import org.telegram.fluent.Answer;
import org.telegram.fluent.EditedMessage;
import org.telegram.mamot.services.DAO;
import org.telegram.mamot.services.Huerator;
import org.telegram.mamot.services.Mamorator;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.timertasks.CustomTimerTask;
import org.telegram.timertasks.TimerExecutor;

import java.util.List;

import static org.telegram.BotConfig.COMMANDS_USER;
import static org.telegram.services.Emoji.AMBULANCE;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    private final InlineQueryHandler inlineQueryHandler = new InlineQueryHandler(this);

    public CommandsHandler(List<CustomTimerTask> tasks, BotCommand... commands) {
        scheduleTasks(tasks);
        registerCommands(commands);

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((sender, message) -> {
            new Answer(sender).to(message.getChat())
                    .message("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + AMBULANCE)
                    .send();
            helpCommand.execute(sender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    private void registerCommands(BotCommand[] commands) {
        for (BotCommand cmd : commands) {
            register(cmd);
        }
    }

    private void scheduleTasks(List<CustomTimerTask> tasks) {
        for (CustomTimerTask t : tasks) {
            t.setSender(this);
            TimerExecutor.getInstance().schedule(t);
        }
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            Answer answer = new Answer(this).to(msg.getChatId().toString());

            if (msg.getEntities() == null || msg.getEntities().isEmpty()) {
                if (msg.getSticker() != null) {
                    answer.message(msg.getSticker().getFileId()).send();
                } else {
                    //TODO get rid of hard code
                    if (msg.getText().startsWith("//")) {
                        answer.message(new Huerator().huate(msg.getText())).send();
                    } else {
                        answer.message(new Mamorator(new DAO()).mamate(msg.getText())).send();
                    }
                }
            } else {
                msg.getEntities().stream().filter(e -> e.getType().equals("url")).forEach(e -> answer.message("Link").send());
            }
        } else if (update.hasInlineQuery()) {
            inlineQueryHandler.handleIncomingInlineQuery(update.getInlineQuery());
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        }
    }

    private void handleCallback(Update update) {
        CallbackQuery cb = update.getCallbackQuery();
        for (BotCommand cmd : getRegisteredCommands()) {
            if (cmd instanceof CallbackCommand && ((CallbackCommand) cmd).handle(cb, this)) {
                return;
            }
        }
        removeInlineKeyboard(cb.getMessage());
    }

    public void removeInlineKeyboard(Message m) {
        new EditedMessage(this, m).newText(m.getText()).send();
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_TOKEN");
    }

    @Override
    public String getBotUsername() {
        return COMMANDS_USER;
    }
}