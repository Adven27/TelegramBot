package org.telegram.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

import static org.telegram.services.Stickers.HELP;

public class HelpCommand extends BotCommand {

    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super("help", "Get all the commands this bot provides");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
                .sticker(HELP)
                .message(getHelpMsg()).enableMarkdown()
                .send();
    }

    private String getHelpMsg() {
        StringBuilder msg = new StringBuilder("");
        for (BotCommand c : commandRegistry.getRegisteredCommands()) {
            msg.append("/" + c.getCommandIdentifier() + " " + c.getDescription()).append("\n");
        }
        return msg.toString();
    }

}