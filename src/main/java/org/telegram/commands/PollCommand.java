package org.telegram.commands;

import jersey.repackaged.com.google.common.collect.Lists;
import org.telegram.fluent.Answer;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.updateshandlers.CommandsHandler;

import java.util.*;

import static org.telegram.services.Stickers.ASK;

public class PollCommand extends CallbackCommand {

    private static final String HELP_MSG = "Пример: Чей крым? наш не_наш";
    private static Map<String, Map<String, List<String>>> polls = new HashMap();

    public PollCommand() {
        super("poll","..?");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Answer answer = new Answer(sender).to(chat);
        ArrayList<String> list = Lists.newArrayList(strings);

        if (list.isEmpty()) {
            say(answer.message(HELP_MSG));
        } else {
            String question = "";
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

            say(answer.message(question).keyboard(getInlineKeyboard(pollId, vars.keySet()))
                  .sticker(ASK));
        }
    }

    private InlineKeyboardMarkup getInlineKeyboard(String pollId, Set<String> vars) {
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

    @Override
    public void handleCallback(CallbackQuery cb, CommandsHandler sender) {
        String[] answer = cb.getData().split("#");
        String pollId = answer[0];
        String chosen = answer[1];
        Map<String, List<String>> poll = polls.get(pollId);
        Message message = cb.getMessage();

        if (poll == null) {
            sender.removeInlineKeyboard(message);
            return;
        }

        for (String v : poll.keySet()) {
            List<String> votes = poll.get(v);
            String voter = cb.getFrom().getLastName() + " " + cb.getFrom().getFirstName();
            if (chosen.equals(v)) {
                if (!votes.contains(voter)) {
                    votes.add(voter);
                }
            } else {
                if (votes.contains(voter)) {
                    votes.remove(voter);
                }
            }
            poll.put(v, votes);
        }
        polls.put(pollId, poll);

        try {
            AnswerCallbackQuery acb = new AnswerCallbackQuery();
            String msg = message.getText();
            String question = !msg.contains("\n\n") ? msg : msg.substring(0, msg.indexOf("\n\n"));
            String text = question + "\n\n";
            for (Map.Entry<String, List<String>> e : poll.entrySet()) {
                text += e.getKey() + " - " + (e.getValue().size() == 0 ? "" : e.getValue()) + "\n";
            }
            acb.setText("Спасибушки");
            acb.setCallbackQueryId(cb.getId());
            sender.answerCallbackQuery(acb);

            EditMessageText newTxt = new EditMessageText();
            newTxt.setMessageId(message.getMessageId());
            newTxt.setReplyMarkup(getInlineKeyboard(pollId, poll.keySet()));
            newTxt.setText(text);
            newTxt.setChatId(message.getChatId().toString());
            sender.editMessageText(newTxt);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}