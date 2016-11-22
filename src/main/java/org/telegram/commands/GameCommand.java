package org.telegram.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class GameCommand extends BotCommand {
    public static final String BORDER = "\uD83C\uDF84";
    public static final String EMPTY_CELL = "\uD83C\uDF3E";
    public static final String PLAYER = "\uD83E\uDD16";
    public static final String MAP =
                    "11111\n" +
                    "10001\n" +
                    "10001\n" +
                    "10001\n" +
                    "11111";
    public static int x = 0;
    public static int y = 0;

    public GameCommand() {
        super("game","..?");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Answer answer = new Answer(sender).to(chat);

        String msg = MAP;
        String[] rows = msg.split("\n");
        for (int i = 0; i < rows.length; i++) {
            if (i == y) {
                StringBuilder newRow = new StringBuilder(rows[i]);
                newRow.setCharAt(x, '@');
                rows[i] = newRow.toString();
            }
        }
        answer.message(msg).replyKeyboard(getInlineKeyboard()).send();

    }

    public static InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup ikb = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> kb = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton b = new InlineKeyboardButton();
        b.setCallbackData("up");
        b.setText("\uD83D\uDD3C");
        row.add(b);

        b = new InlineKeyboardButton();
        b.setCallbackData("down");
        b.setText("\uD83D\uDD3D");
        row.add(b);

        kb.add(row);
        ikb.setKeyboard(kb);
        return ikb;
    }

    public static void up() {
        if (y > 0) {
            y--;
        }
    }

    public static void down() {
        y++;
    }

    public static String screen() {
        String msg = "";
        String[] rows = MAP.split("\n");
        for (int i = 0; i < rows.length; i++) {
            if (i == y) {
                String newRow =  rows[i].substring(0, x + 1)+ "@" + rows[i].substring(x + 2);
                rows[i] = newRow;
            }
            msg += rows[i] + "\n";
        }
        return msg.replaceAll("1", BORDER).replaceAll("0", EMPTY_CELL).replaceFirst("@", PLAYER);
    }
}