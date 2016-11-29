package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.fluent.InlineKeyboard;
import org.telegram.sokoban.controller.Controller;
import org.telegram.sokoban.model.Direction;
import org.telegram.sokoban.view.GameFieldPrinter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.telegram.services.Emoji.*;

public class GameCommand extends BotCommand {
    public static final String DOWN = "\uD83D\uDD3D";
    public static final String UP = "\uD83D\uDD3C";
    public static final String LEFT = LEFT_ARROW.toString();
    public static final String RIGHT = RIGHT_ARROW.toString();
    public static final String RESTART = "\uD83D\uDD04";
    public static int x = 0;
    public static int y = 0;
    public static Controller controller;

    public static int stepsInRow = 0;

    public static Map<String, List<Direction>> turns = new HashMap();
    private List<GameFieldPrinter> printers;
    private int curSkin = 0;

    public GameCommand(List<GameFieldPrinter> printers) {
        super("game","..?");
        this.printers = printers;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        turns.clear();
        controller = strings.length == 0 ? new Controller() : new Controller(Integer.parseInt(strings[0]));
        stepsInRow = 0;
        new Answer(sender).to(chat).message(screen()).replyKeyboard(getInlineKeyboard()).send();
    }

    public static InlineKeyboardMarkup getInlineKeyboard() {
        return new InlineKeyboard()
                .row(UP, "up", DOWN, "down", LEFT, "left", RIGHT, "right")
                .row(UP_DOUBLE_ARROW.toString(), "up_turbo", DOWN_DOUBLE_ARROW.toString(), "down_turbo",
                     LEFT_DOUBLE_ARROW.toString(), "left_turbo", RIGHT_DOUBLE_ARROW.toString(), "right_turbo")
                .row(RIGHT_UP_ARROW.toString(), "right_up", RIGHT_DOWN_ARROW.toString(), "right_down", RESTART, "restart", "\uD83C\uDFA6", "next_skin")
                .build();
    }

    public String screen() {
        return printers.get(curSkin).print(controller.getGameField(), controller.getGameObjects(), controller.getCurLevel());
    }

    public static boolean move(Direction down) {
        stepsInRow++;
        return controller.move(down);
    }

    public static void turboMove(Direction d) {
        stepsInRow++;
        while (true) {
            if (!controller.move(d)) {
                break;
            }
        }
    }

    public static void moveRightAnd(Direction direction) {
        stepsInRow++;
        if(controller.move(Direction.RIGHT)) {
            controller.move(direction);
        }
    }

    public static void restart() {
        turns.clear();
        controller.restart();
    }

    public void nextSkin() {
        curSkin = (curSkin + 1) % printers.size();
    }
}