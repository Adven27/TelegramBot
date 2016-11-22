package org.telegram.updateshandlers;

import org.telegram.commands.Answer;
import org.telegram.commands.GameCommand;
import org.telegram.commands.HelpCommand;
import org.telegram.mamot.services.DAO;
import org.telegram.mamot.services.Mamorator;
import org.telegram.services.CustomTimerTask;
import org.telegram.services.Emoji;
import org.telegram.services.Events;
import org.telegram.services.TimerExecutor;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;

import static org.telegram.BotConfig.COMMANDS_TOKEN;
import static org.telegram.BotConfig.COMMANDS_USER;
import static org.telegram.commands.PollCommand.getInlineKeyboard;
import static org.telegram.commands.PollCommand.polls;
import static org.telegram.services.Stickers.THINK;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    public static final String LOGTAG = "COMMANDSHANDLER";
    public static final String CHAT_ID = "-145229307";//"229669496";;
    private final InlineQueryHandler inlineQueryHandler = new InlineQueryHandler(this);

    public CommandsHandler(BotCommand... commands) {
        registerAlerts();

        for (BotCommand cmd : commands) {
            register(cmd);
        }
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((sender, message) -> {
            new Answer(sender).to(message.getChat())
                .message("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE)
                .send();
            helpCommand.execute(sender, message.getFrom(), message.getChat(), new String[] {});
        });
    }

    private void registerAlerts() {
        for (Events e : Events.values()) {
            startAlertTimers(e);
        }
        TimerExecutor.getInstance().startExecutionOnRandomHourAt(new CustomTimerTask("Quotes", -1) {
            @Override
            public void execute() {
                //TODO get rid of this shit
                new Answer(CommandsHandler.this).to(CHAT_ID)
                        .sticker(THINK)
                        .message(new DAO().getQuote()).disableWebPagePreview()
                        .send();
            }
        }, 15 , 52, 0);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            Sticker recievedSticker = msg.getSticker();
            String recievedStickerFileId = recievedSticker != null ? recievedSticker.getFileId() : "";
            Answer answer = new Answer(this).to(msg.getChatId().toString());

            if (msg.getEntities() == null || msg.getEntities().isEmpty()) {
                if (!"".equals(recievedStickerFileId)) {
                    answer.message(recievedStickerFileId).send();
                } else {
                    //TODO get rid of hard code
                    answer.message(new Mamorator(new DAO()).mamate(msg.getText())).send();
                }
            } else {
                for (MessageEntity e : msg.getEntities()) {
                    if (e.getType().equals("url")) {
                        answer.message("Link " + e.getText()).send();
                    }
                }
            }
        } else if (update.hasInlineQuery()) {
            inlineQueryHandler.handleIncomingInlineQuery(update.getInlineQuery());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery cb = update.getCallbackQuery();
            if (cb.getData().indexOf("#") == -1) {
                try {
                    Message message = cb.getMessage();
                    String data = cb.getData();
                    AnswerCallbackQuery acb = new AnswerCallbackQuery();
                    acb.setText(data);
                    acb.setCallbackQueryId(cb.getId());
                    answerCallbackQuery(acb);
                    if ("up".equals(data)) {
                        GameCommand.up();
                    } else {
                        GameCommand.down();
                    }

                    EditMessageText newTxt = new EditMessageText();
                    newTxt.setMessageId(message.getMessageId());
                    newTxt.setReplyMarkup(GameCommand.getInlineKeyboard());
                    newTxt.setText(GameCommand.screen());
                    newTxt.setChatId(message.getChatId().toString());
                    editMessageText(newTxt);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            } else {
                String[] answer = cb.getData().split("#");
                String pollId = answer[0];
                String choosen = answer[1];
                Map<String, List<String>> poll = polls.get(pollId);
                Message message = cb.getMessage();

                if (poll == null) {
                    removeInlineKeyboard(message);
                    return;
                }

                for (String v : poll.keySet()) {
                    List<String> votes = poll.get(v);
                    String voter = cb.getFrom().getLastName() + " " + cb.getFrom().getFirstName();
                    if (choosen.equals(v)) {
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
                    String question = msg.indexOf("\n\n") == -1 ? msg : msg.substring(0, msg.indexOf("\n\n"));
                    String text = question + "\n\n";
                    for (Map.Entry<String, List<String>> e : poll.entrySet()) {
                        text += e.getKey() + " - " + (e.getValue().size() == 0 ? "" : e.getValue()) + "\n";
                    }
                    acb.setText("Спасибушки");
                    acb.setCallbackQueryId(cb.getId());
                    answerCallbackQuery(acb);

                    EditMessageText newTxt = new EditMessageText();
                    newTxt.setMessageId(message.getMessageId());
                    newTxt.setReplyMarkup(getInlineKeyboard(pollId, poll.keySet()));
                    newTxt.setText(text);
                    newTxt.setChatId(message.getChatId().toString());
                    editMessageText(newTxt);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void removeInlineKeyboard(Message message) {
        try {
            EditMessageText newTxt = new EditMessageText();
            newTxt.setMessageId(message.getMessageId());
            newTxt.setChatId(message.getChatId().toString());
            newTxt.setText(message.getText());
            editMessageText(newTxt);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void startAlertTimers(Events e) {
        TimerExecutor.getInstance().startExecutionEveryDayAt(new CustomTimerTask(e.name(), -1) {
            @Override
            public void execute() {
                new Answer(CommandsHandler.this).to(CHAT_ID)
                        .sticker(e.sticker())
                        .message(e.msg()).disableWebPagePreview()
                        .send();
            }
        }, e.time().getHour(), e.time().getMinute(), e.time().getSecond());
    }

    @Override
    public String getBotToken() {
        return COMMANDS_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return COMMANDS_USER;
    }
}