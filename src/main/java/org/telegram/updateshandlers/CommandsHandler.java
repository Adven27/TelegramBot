package org.telegram.updateshandlers;

import org.telegram.commands.GameCommand;
import org.telegram.commands.HelpCommand;
import org.telegram.fluent.Answer;
import org.telegram.mamot.services.DAO;
import org.telegram.mamot.services.Mamorator;
import org.telegram.services.*;
import org.telegram.sokoban.model.Direction;
import org.telegram.sokoban.view.GameFieldPrinter;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.List;
import java.util.Map;

import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.telegram.BotConfig.COMMANDS_TOKEN;
import static org.telegram.BotConfig.COMMANDS_USER;
import static org.telegram.commands.PollCommand.getInlineKeyboard;
import static org.telegram.commands.PollCommand.polls;
import static org.telegram.services.Emoji.AMBULANCE;
import static org.telegram.services.Emoji.ANCHOR;
import static org.telegram.services.Emoji.SAILBOAT;
import static org.telegram.services.Stickers.THINK;
import static org.telegram.sokoban.model.Direction.*;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    private static final String LOGTAG = "COMMANDSHANDLER";
    private static final String CHAT_ID = "-145229307";//"229669496";;
    private final InlineQueryHandler inlineQueryHandler = new InlineQueryHandler(this);
    private final GameCommand gameCommand;

    public CommandsHandler(BotCommand... commands) {
        registerAlerts();

        for (BotCommand cmd : commands) {
            register(cmd);
        }

        gameCommand = new GameCommand(newArrayList(
                new GameFieldPrinter(new SimpleSkin("\uD83D\uDDB1", "\uD83C\uDF84", "\uD83D\uDE00", "\uD83D\uDCA9", "\uD83D\uDEBD")),
                new GameFieldPrinter(new SimpleSkin("\uD83D\uDDB1", "\uD83C\uDF0A", "\uD83C\uDF2A", SAILBOAT.toString(), ANCHOR.toString())),
                new GameFieldPrinter(new SimpleSkin("\uD83D\uDDB1", "\uD83D\uDC8B", "\uD83C\uDFC2", "\uD83D\uDC6F", "\uD83D\uDEC1"))));
        register(gameCommand);

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((sender, message) -> {
            new Answer(sender).to(message.getChat())
                .message("The command '" + message.getText() + "' is not known by this bot. Here comes some help " + AMBULANCE)
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
            Answer answer = new Answer(this).to(msg.getChatId().toString());

            if (msg.getEntities() == null || msg.getEntities().isEmpty()) {
                if (msg.getSticker() != null) {
                    answer.message(msg.getSticker().getFileId()).send();
                } else {
                    //TODO get rid of hard code
                    answer.message(new Mamorator(new DAO()).mamate(msg.getText())).send();
                }
            } else {
                msg.getEntities().stream().filter(e -> e.getType().equals("url")).forEach(e -> {
                    answer.message("Link").send();
                });
            }
        } else if (update.hasInlineQuery()) {
            inlineQueryHandler.handleIncomingInlineQuery(update.getInlineQuery());
        } else if (update.hasCallbackQuery()) {
            CallbackQuery cb = update.getCallbackQuery();
            if (!cb.getData().contains("#")) {
                handleGameCallback(cb);
            } else {
                handlePollCallback(cb);
            }
        }
    }

    private void handlePollCallback(CallbackQuery cb) {
        String[] answer = cb.getData().split("#");
        String pollId = answer[0];
        String chosen = answer[1];
        Map<String, List<String>> poll = polls.get(pollId);
        Message message = cb.getMessage();

        if (poll == null) {
            removeInlineKeyboard(message);
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
            acb.setText("����������");
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

    private void handleGameCallback(CallbackQuery cb) {
        try {
            Message message = cb.getMessage();
            if (GameCommand.controller == null) {
                removeInlineKeyboard(message);
                return;
            }
            String data = cb.getData();

            String from = cb.getFrom().getLastName() + " " + cb.getFrom().getFirstName();
            switch (data) {
                case "left":
                    GameCommand.move(LEFT);
                    stat(from, LEFT);
                    break;
                case "left_turbo":
                    GameCommand.turboMove(LEFT);
                    stat(from, LEFT);
                    break;
                case "right":
                    GameCommand.move(RIGHT);
                    stat(from, RIGHT);
                    break;
                case "right_turbo":
                    GameCommand.turboMove(RIGHT);
                    stat(from, RIGHT);
                    break;
                case "up":
                    GameCommand.move(UP);
                    stat(from, UP);
                    break;
                case "up_turbo":
                    GameCommand.turboMove(UP);
                    stat(from, UP);
                    break;
                case "down":
                    GameCommand.move(DOWN);
                    stat(from, DOWN);
                    break;
                case "down_turbo":
                    GameCommand.turboMove(DOWN);
                    stat(from, DOWN);
                    break;
                case "right_down":
                    GameCommand.moveRightAnd(DOWN);
                    stat(from, DOWN);
                    break;
                case "right_up":
                    GameCommand.moveRightAnd(UP);
                    stat(from, UP);
                    break;
                case "restart":
                    GameCommand.restart();
                    break;
                case "next_skin":
                    gameCommand.nextSkin();
                    break;
            }

            String screen = gameCommand.screen();
            String stat = "";
            for (Map.Entry<String, List<Direction>> playerTurns : GameCommand.turns.entrySet()) {
                stat += playerTurns.getKey() + " : " + playerTurns.getValue().size() + "\n";
            }
            EditMessageText newTxt = new EditMessageText();
            newTxt.setMessageId(message.getMessageId());
            newTxt.setReplyMarkup(GameCommand.getInlineKeyboard());
            newTxt.setText(stat + screen);
            newTxt.setChatId(message.getChatId().toString());
            editMessageText(newTxt);

            AnswerCallbackQuery acb = new AnswerCallbackQuery();
            acb.setText(data);
            acb.setCallbackQueryId(cb.getId());
            answerCallbackQuery(acb);
        } catch (TelegramApiRequestException e) {
            if (e.getErrorCode().equals(429)) {
                AnswerCallbackQuery acb = new AnswerCallbackQuery();
                acb.setText("��� �������...\n" + e.getApiResponse());
                acb.setCallbackQueryId(cb.getId());
                try {
                    answerCallbackQuery(acb);
                } catch (TelegramApiException e1) {
                    BotLogger.error(LOGTAG, e1);
                }
                GameCommand.stepsInRow = 0;
            }
            BotLogger.error(LOGTAG, e);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private void stat(String from, Direction direction) {
        List<Direction> directions;
        directions = GameCommand.turns.get(from);
        if (directions == null) {
            directions = newArrayList(direction);
        } else {
            directions.add(direction);
        }
        GameCommand.turns.put(from, directions);
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