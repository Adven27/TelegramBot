package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.fluent.InlineKeyboard;
import org.telegram.sokoban.controller.Controller;
import org.telegram.sokoban.model.Direction;
import org.telegram.sokoban.view.GameFieldPrinter;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.updateshandlers.CommandsHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jersey.repackaged.com.google.common.collect.Lists.newArrayList;
import static org.telegram.services.Emoji.*;

public class GameCommand extends CallbackCommand {
    private static final String DOWN = "\uD83D\uDD3D";
    private static final String UP = "\uD83D\uDD3C";
    private static final String LEFT = LEFT_ARROW.toString();
    private static final String RIGHT = RIGHT_ARROW.toString();
    private static final String RESTART = "\uD83D\uDD04";
    private static final String LOGTAG = "GAMECOMMAND";

    public static int x = 0;
    public static int y = 0;
    private static Controller controller;

    private static Map<String, List<Direction>> turns = new HashMap();
    private List<GameFieldPrinter> printers;
    private int curSkin = 0;

    public GameCommand(List<GameFieldPrinter> printers) {
        super("game","..?");
        this.printers = printers;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Answer answer = new Answer(sender).to(chat);
        if (controller != null && strings.length == 0) {
            say(answer.message(screen()).keyboard(getKeyboard()));
            return;
        }
        turns.clear();
        controller = strings.length == 0 ? new Controller() : new Controller(Integer.parseInt(strings[0]));
        say(answer.message(screen()).keyboard(getKeyboard()));
    }

    private InlineKeyboardMarkup getKeyboard() {
        return new InlineKeyboard()
                .row(UP, "up", DOWN, "down", LEFT, "left", RIGHT, "right")
                .row(UP_DOUBLE_ARROW.toString(), "up_turbo", DOWN_DOUBLE_ARROW.toString(), "down_turbo",
                     LEFT_DOUBLE_ARROW.toString(), "left_turbo", RIGHT_DOUBLE_ARROW.toString(), "right_turbo")
                .row(RIGHT_UP_ARROW.toString(), "right_up", RIGHT_DOWN_ARROW.toString(), "right_down", RESTART, "restart", "\uD83C\uDFA6", "next_skin")
                .build();
    }

    private String screen() {
        return printers.get(curSkin).print(controller.getGameField(), controller.getGameObjects(), controller.getCurLevel());
    }

    private boolean move(Direction down) {
        return controller.move(down);
    }

    private void turboMove(Direction d) {
        while (true) {
            if (!controller.move(d)) {
                break;
            }
        }
    }

    private void moveRightAnd(Direction direction) {
        if(controller.move(Direction.RIGHT)) {
            controller.move(direction);
        }
    }

    private void restart() {
        turns.clear();
        controller.restart();
    }

    private void nextSkin() {
        curSkin = (curSkin + 1) % printers.size();
    }

    private void stat(String from, Direction direction) {
        List<Direction> directions;
        directions = turns.get(from);
        if (directions == null) {
            directions = newArrayList(direction);
        } else {
            directions.add(direction);
        }
        turns.put(from, directions);
    }

    @Override
    public void handleCallback(CallbackQuery cb,  CommandsHandler sender) {
        try {
            Message message = cb.getMessage();
            if (controller == null) {
                sender.removeInlineKeyboard(message);
                return;
            }
            String data = cb.getData();

            String from = cb.getFrom().getLastName() + " " + cb.getFrom().getFirstName();
            switch (data) {
                case "left":
                    move(Direction.LEFT);
                    stat(from, Direction.LEFT);
                    break;
                case "left_turbo":
                    turboMove(Direction.LEFT);
                    stat(from, Direction.LEFT);
                    break;
                case "right":
                    move(Direction.RIGHT);
                    stat(from, Direction.RIGHT);
                    break;
                case "right_turbo":
                    turboMove(Direction.RIGHT);
                    stat(from, Direction.RIGHT);
                    break;
                case "up":
                    move(Direction.UP);
                    stat(from, Direction.UP);
                    break;
                case "up_turbo":
                    turboMove(Direction.UP);
                    stat(from, Direction.UP);
                    break;
                case "down":
                    move(Direction.DOWN);
                    stat(from, Direction.DOWN);
                    break;
                case "down_turbo":
                    turboMove(Direction.DOWN);
                    stat(from, Direction.DOWN);
                    break;
                case "right_down":
                    moveRightAnd(Direction.DOWN);
                    stat(from, Direction.DOWN);
                    break;
                case "right_up":
                    moveRightAnd(Direction.UP);
                    stat(from, Direction.UP);
                    break;
                case "restart":
                    restart();
                    break;
                case "next_skin":
                    nextSkin();
                    break;
            }

            String screen = screen();
            String stat = "";
            for (Map.Entry<String, List<Direction>> playerTurns : turns.entrySet()) {
                stat += playerTurns.getKey() + " : " + playerTurns.getValue().size() + "\n";
            }
            EditMessageText newTxt = new EditMessageText();
            newTxt.setMessageId(message.getMessageId());
            newTxt.setReplyMarkup(getKeyboard());
            newTxt.setText(stat + screen);
            newTxt.setChatId(message.getChatId().toString());
            sender.editMessageText(newTxt);

            AnswerCallbackQuery acb = new AnswerCallbackQuery();
            acb.setText(data);
            acb.setCallbackQueryId(cb.getId());
            sender.answerCallbackQuery(acb);
        } catch (TelegramApiRequestException e) {
            if (e.getErrorCode().equals(429)) {
                AnswerCallbackQuery acb = new AnswerCallbackQuery();
                acb.setText("Чот приуныл...\n" + e.getApiResponse());
                acb.setCallbackQueryId(cb.getId());
                try {
                    sender.answerCallbackQuery(acb);
                } catch (TelegramApiException e1) {
                    BotLogger.error(LOGTAG, e1);
                }
            }
            BotLogger.error(LOGTAG, e);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}