package org.telegram.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class HelpCommand extends BotCommand {

    private static final String LOGTAG = "HELPCOMMAND";

    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super("help", "Get all the commands this bot provides");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        StringBuilder helpMessageBuilder = new StringBuilder("Try:\n\n");

        for (BotCommand c : commandRegistry.getRegisteredCommands()) {
            helpMessageBuilder.append("/" + c.getCommandIdentifier() + " " + c.getDescription()).append("\n");
        }

        SendMessage m = new SendMessage();
        m.setChatId(chat.getId().toString());
        m.enableMarkdown(true);
        m.setText(helpMessageBuilder.toString());

        try {
            absSender.sendMessage(m);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
