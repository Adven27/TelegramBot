package org.telegram.services.repos.impl;

import org.telegram.games.game2048.Game2048;
import org.telegram.services.repos.LeaderBoard;
import org.telegram.services.repos.LeaderBoardRepo;

import java.util.List;

public class LeaderBoardImpl implements LeaderBoard {
    private final LeaderBoardRepo boardRepo;

    public LeaderBoardImpl(LeaderBoardRepo boardRepo) {
        this.boardRepo = boardRepo;
    }

    @Override
    public void update(String username, Game2048 game) {
        List<LeaderBoardRepo.Record> records = boardRepo.selectAll();
        if (records.size() < BOARD_SIZE) {
            boardRepo.insert(username, game.getScore());
        } else {
            LeaderBoardRepo.Record lastOne = records.stream().min((r1, r2) -> {
                if (r1.score() > r2.score()) return 1;
                if (r2.score() > r1.score()) return -1;
                return 0;
            }).get();

            if (game.getScore() > lastOne.score()) {
                boardRepo.replace(lastOne.id(), username, game.getScore());
            }
        }
    }

}
