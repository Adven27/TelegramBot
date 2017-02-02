package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.fluent.EditedMessage;
import org.telegram.fluent.InlineKeyboard;
import org.telegram.games.game2048.Game2048;
import org.telegram.services.Emoji;
import org.telegram.services.repos.GameRepo;
import org.telegram.services.repos.LeaderBoard;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.updateshandlers.CommandsHandler;

import java.util.*;

import static org.telegram.services.Emoji.LEFT_ARROW;
import static org.telegram.services.Emoji.RIGHT_ARROW;

public class Game2048Command extends CallbackCommand {
    private static final String DOWN = "\uD83D\uDD3D";
    private static final String UP = "\uD83D\uDD3C";
    private static final String LEFT = LEFT_ARROW.toString();
    private static final String RIGHT = RIGHT_ARROW.toString();
    private static final String RESTART = "\uD83D\uDD04";
    private static final String LOSE_MSG = " \uD83D\uDC80";
    private static final String WON_MSG = " \uD83C\uDF89\uD83C\uDF89\uD83C\uDF89";
    private static final String BORDER = "\uD83D\uDDB1";
    private static Map<String, Game2048> userGames = new HashMap<>();
    private final GameRepo gameRepo;
    private final LeaderBoard leaderBoard;

    public Game2048Command(GameRepo gameRepo, LeaderBoard leaderBoard) {
        super("game2048","Game 2048");
        this.gameRepo = gameRepo;
        this.leaderBoard = leaderBoard;

        gameRepo.selectAll().forEach((name, game) -> userGames.put(name, new Game2048(game)));

        System.out.println("userGames = " + userGames);
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Answer answer = new Answer(sender).to(chat);
        String userName = getUserName(user);
        if (userGames.get(userName) != null && strings.length == 0) {
            sendAndWaitForCallback(answer.message(screen()).keyboard(getInlineKeyboard()));
            return;
        }
        Game2048 g = new Game2048();
        userGames.put(userName, g);
        gameRepo.insert(userName, g.toJSON());
        sendAndWaitForCallback(answer.message(screen()).keyboard(getInlineKeyboard()));
    }

    private String getUserName(User user) {
        return user.getLastName();
    }

    private InlineKeyboardMarkup getInlineKeyboard() {
        return new InlineKeyboard().row(UP, "up", DOWN, "down", LEFT, "left", RIGHT, "right")
                            .row(RESTART, "restart")
                            .build();
    }

    private String drawTile(Game2048.Tile tile) {
        switch (tile.value){
            case 2: return Emoji.ZERO.toString();
            case 4: return Emoji.ONE.toString();
            case 8: return Emoji.TWO.toString();
            case 16: return Emoji.THREE.toString();
            case 32: return Emoji.FOUR.toString();
            case 64: return Emoji.FIVE.toString();
            case 128: return Emoji.SIX.toString();
            case 256: return Emoji.SEVEN.toString();
            case 512: return Emoji.EIGHT.toString();
            case 1024: return Emoji.NINE.toString();
            case 2048: return Emoji.TEN.toString();
            default: return Emoji.EMPTY_CELL.toString();
        }
    }

    private String screen() {
        String msg = "";
        Set<Map.Entry<String, Game2048>> games = userGames.entrySet();
        LinkedList<Game2048> gs = new LinkedList<>();
        for (Map.Entry<String, Game2048> game : games) {
            Game2048 g = game.getValue();
            gs.add(g);
            msg += game.getKey() + " " + g.getScore();

            gameRepo.update(game.getKey(), g.toJSON());

            if (g.isLose()) {
                msg += LOSE_MSG;
                leaderBoard.update(game.getKey(), g);
            } else if (g.isWin()) {
                msg += WON_MSG;
            }
            msg += "\n";
        }

        final int maxInRow = 4;
        int cur = 0;

        List<List<Game2048>> rows = new ArrayList<>();
        List<Game2048> row = new ArrayList<>();

        int size = gs.size();
        for (int i = 0; i < size; i++) {
            if(cur < maxInRow) {
                cur++;
                row.add(gs.remove());
            } else {
                rows.add(row);
                row = new ArrayList<>();
                row.add(gs.remove());
                cur = 1;
            }
        }
        rows.add(row);

        for (List<Game2048> r : rows) {
            for (int y = 0; y < 4; y++) {
                for (Game2048 game : r) {
                    for (int x = 0; x < 4; x++) {
                        msg += drawTile(game.getTiles()[x + y * 4]);
                    }
                    msg += BORDER;
                }
                msg += "\n";
            }
            msg += "\n";
        }

        return msg;
    }

    @Override
    protected void handleCallback(CallbackQuery cb, AnswerCallbackQuery acb, CommandsHandler sender) throws TelegramApiException {
        Message message = cb.getMessage();
        if (userGames.isEmpty()) {
            gameRepo.selectAll().forEach((name, game) -> userGames.put(name, new Game2048(game)));
        }
        String data = cb.getData();
        User from = cb.getFrom();

        doAction(data, from);

        new EditedMessage(sender, message).newText(screen()).keyboard(getInlineKeyboard()).send();
    }

    private void doAction(String action, User from) {
        String userName = getUserName(from);
        Game2048 g = userGames.get(userName);
        if (g == null) {
            userGames.put(userName, new Game2048());
        } else {
            switch (action) {
                case "left":    g.left();  break;
                case "right":   g.right(); break;
                case "up":      g.up();    break;
                case "down":    g.down();  break;
                case "restart": leaderBoard.update(userName, g); g.resetGame(); break;
            }
        }
    }
}