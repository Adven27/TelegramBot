package org.telegram.commands;

import jersey.repackaged.com.google.common.collect.Lists;
import org.telegram.sokoban.controller.Controller;
import org.telegram.sokoban.model.*;
import org.telegram.sokoban.view.Point;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;
import static org.telegram.services.Emoji.*;

public class GameCommand extends BotCommand {
    public static final String BORDER = "\uD83C\uDF84";
    public static final String EMPTY_CELL = CLOUD.toString();
    //public static final String PLAYER = "\uD83E\uDD16";
    public static final List<String> PLAYER_AVA = Lists.newArrayList("\uD83D\uDE00", "\uD83D\uDE01", "\uD83D\uDE10",
                                                                     "\uD83D\uDE25", "\uD83D\uDE2A", "\uD83D\uDE1E",
                                                                     "\uD83E\uDD15", "\uD83D\uDE07");
    public static final String BOX = "\uD83D\uDCA9";
    public static final String HOME = "\uD83D\uDEBD";

/*
    public static final String BORDER = "X";
    public static final String EMPTY_CELL = "   ";
    public static final String PLAYER = "@";
    public static final String BOX = "*";
    public static final String HOME = "0";
*/

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

    public GameCommand() {
        super("game","..?");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        turns.clear();
        controller = new Controller();
        stepsInRow = 0;
        new Answer(sender).to(chat).message(screen()).replyKeyboard(getInlineKeyboard()).send();
    }

    public static InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup ikb = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> kb = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton b = new InlineKeyboardButton();
        b.setCallbackData("up");
        b.setText(UP);
        row.add(b);
        b = new InlineKeyboardButton();
        b.setCallbackData("down");
        b.setText(DOWN);
        row.add(b);
        b = new InlineKeyboardButton();
        b.setCallbackData("left");
        b.setText(LEFT);
        row.add(b);
        b = new InlineKeyboardButton();
        b.setCallbackData("right");
        b.setText(RIGHT);
        row.add(b);

        kb.add(row);

        row = new ArrayList<>();
        b = new InlineKeyboardButton();
        b.setCallbackData("up_turbo");
        b.setText(UP_DOUBLE_ARROW.toString());
        row.add(b);
        b = new InlineKeyboardButton();
        b.setCallbackData("down_turbo");
        b.setText(DOWN_DOUBLE_ARROW.toString());
        row.add(b);
        b = new InlineKeyboardButton();
        b.setCallbackData("left_turbo");
        b.setText(LEFT_DOUBLE_ARROW.toString());
        row.add(b);
        b = new InlineKeyboardButton();
        b.setCallbackData("right_turbo");
        b.setText(RIGHT_DOUBLE_ARROW.toString());
        row.add(b);

        kb.add(row);

        row = new ArrayList<>();
        b = new InlineKeyboardButton();
        b.setCallbackData("right_up");
        b.setText(RIGHT_UP_ARROW.toString());
        row.add(b);

        b = new InlineKeyboardButton();
        b.setCallbackData("right_down");
        b.setText(RIGHT_DOWN_ARROW.toString());
        row.add(b);

        b = new InlineKeyboardButton();
        b.setCallbackData("restart");
        b.setText(RESTART);
        row.add(b);


        kb.add(row);
        ikb.setKeyboard(kb);
        return ikb;
    }

    public static String screen() {
        Map<Point, GameObject> gameField = controller.getGameField();

        String msg = "";
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 14; x++) {
                GameObject gameObject = gameField.get(new Point(x, y));
                if (gameObject == null) {
                    msg += EMPTY_CELL;
                } else if (gameObject instanceof Player) {
                    msg +=  PLAYER_AVA.get(min(stepsInRow/3, 7));
                } else if (gameObject instanceof Wall) {
                    msg += BORDER;
                } else if (gameObject instanceof Box) {
                    msg += BOX;
                } else if (gameObject instanceof Home) {
                    msg += HOME;
                }
            }
            msg += "\n";
        }
        return msg;
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
}