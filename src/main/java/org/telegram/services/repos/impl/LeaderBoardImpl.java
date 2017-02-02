package org.telegram.services.repos.impl;

import org.telegram.games.game2048.Game2048;
import org.telegram.services.repos.LeaderBoard;
import org.telegram.services.repos.LeaderBoardRepo;

import java.util.List;
import java.util.stream.Collectors;

public class LeaderBoardImpl implements LeaderBoard {
    private final LeaderBoardRepo boardRepo;

    public LeaderBoardImpl(LeaderBoardRepo boardRepo) {
        this.boardRepo = boardRepo;
    }

    @Override
    public boolean update(String username, Game2048 game) {
        boolean res = false;
        List<LeaderBoardRepo.Record> records = boardRepo.selectAll();
        if (records.size() < BOARD_SIZE) {
            boardRepo.insert(username, game.getScore());
            res = true;
        } else {
            LeaderBoardRepo.Record lastOne = records.stream().min((r1, r2) -> {
                if (r1.score() > r2.score()) return 1;
                if (r2.score() > r1.score()) return -1;
                return 0;
            }).get();

            if (game.getScore() > lastOne.score()) {
                boardRepo.replace(lastOne.id(), username, game.getScore());
                res = true;
            }
        }
        return res;
    }

    @Override
    public List<LeaderBoardRepo.Record> getAll() {
        return boardRepo.selectAll().stream().sorted((r1, r2) -> {
            if (r1.score() > r2.score()) return 1;
            if (r2.score() > r1.score()) return -1;
            return 0;
        }).collect(Collectors.toList());
    }

}
