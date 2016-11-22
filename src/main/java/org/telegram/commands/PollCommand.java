package org.telegram.commands;

import jersey.repackaged.com.google.common.collect.Lists;
import org.telegram.services.Emoji;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.*;

import static org.telegram.services.Stickers.ASK;

public class PollCommand extends BotCommand {

    private String question;
    public static Map<String, Map<String, List<String>>> polls = new HashMap();

    public PollCommand() {
        super("poll","..?");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Answer answer = new Answer(sender).to(chat);
        ArrayList<String> list = Lists.newArrayList(strings);

        if (list.isEmpty()) {
            answer.message("Пример: Чей крым? наш не_наш").send();
        } else {
            question = "";
            int i = 0;
            for (; i < list.size(); i++) {
                if (list.get(i).contains("?")) {
                    for (int j = 0; j <= i; j++) {
                        question += list.get(j) + " ";
                    }
                    break;
                }
            }
            LinkedHashMap<String, List<String>> vars = new LinkedHashMap<>();
            for (String var : list.subList(i + 1, list.size())) {
                vars.put(var, new ArrayList<>());
            }
            String pollId = question.trim();
            polls.put(pollId, vars);

            answer.message(question).replyKeyboard(getInlineKeyboard(pollId, vars.keySet()))
                  .sticker(ASK)
                  .send();
        }
    }

    public static InlineKeyboardMarkup getInlineKeyboard(String pollId, Set<String> vars) {
        InlineKeyboardMarkup ikb = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> kb = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (String v : vars) {
            InlineKeyboardButton b = new InlineKeyboardButton();
            b.setCallbackData(pollId + "#" + v);
            b.setText(v);
            row.add(b);
        }
        kb.add(row);
        ikb.setKeyboard(kb);
        return ikb;
    }

    private static ReplyKeyboardMarkup getAlertsKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(" " + Emoji.HEAVY_PLUS_SIGN);
        row.add(" " + Emoji.HEAVY_CHECK_MARK);
        keyboard.add(row);

        row = new KeyboardRow();
        row.add(" " + Emoji.AIRPLANE);
        row.add(" " + Emoji.ANGRY_FACE);
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}