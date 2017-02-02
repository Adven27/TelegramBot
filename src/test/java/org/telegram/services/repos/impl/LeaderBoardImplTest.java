package org.telegram.services.repos.impl;

import org.junit.Test;
import org.telegram.games.game2048.Game2048;
import org.telegram.services.repos.LeaderBoard;
import org.telegram.services.repos.LeaderBoardInMemRepo;
import org.telegram.services.repos.LeaderBoardRepo;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.telegram.services.repos.LeaderBoard.BOARD_SIZE;

public class LeaderBoardImplTest {
    final LeaderBoardRepo boardRepo = new LeaderBoardInMemRepo();
    final LeaderBoard sut = new LeaderBoardImpl(boardRepo);

    @Test
    public void addNewRecordIfBoardNotFull() throws Exception {
        boardRepo.insert("user", 100);
        final int score = 0;

        sut.update("user", new Game2048(score));

        assertThat(boardRepo.selectAll().size(), equalTo(2));
    }

    @Test
    public void removeLastAndAddNew_IfBoardFullAndNewGreaterThanOld() throws Exception {
        for (int i = 0; i < BOARD_SIZE; i++) {
            boardRepo.insert("user", i);
        }
        final long newMaxScore = 100;

        sut.update("user", new Game2048(newMaxScore));

        List<LeaderBoardRepo.Record> actual = boardRepo.selectAll();

        assertEquals(BOARD_SIZE, actual.size());
        assertEquals(asList(1L,2L,3L,4L, newMaxScore), actual.stream().map(LeaderBoardRepo.Record::score).collect(toList()));
    }

    @Test
    public void doNothing_IfBoardFullAndNewLesserThanOld() throws Exception {
        for (int i = 0; i < BOARD_SIZE; i++) {
            boardRepo.insert("user", i + 100);
        }
        final long newNotEnoughScore = 0;

        sut.update("user", new Game2048(newNotEnoughScore));

        List<LeaderBoardRepo.Record> actual = boardRepo.selectAll();

        assertEquals(BOARD_SIZE, actual.size());
        assertEquals(asList(100L,101L,102L,103L,104L), actual.stream().map(LeaderBoardRepo.Record::score).collect(toList()));
    }
}