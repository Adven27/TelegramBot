package org.telegram.services.repos.impl;

import org.junit.After;
import org.junit.Test;
import org.telegram.services.repos.LeaderBoardRepo;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class PGSQLGameLeaderBoardRepoTest {
    private PGSQLGameLeaderBoardRepo sut = new PGSQLGameLeaderBoardRepo("test_games_leaderboard");

    @After
    public void tearDown() throws Exception {
        sut.dropTable();
    }

    @Test
    public void selectAll() throws Exception {
        List<LeaderBoardRepo.Record> expected = new ArrayList<>();
        expected.add(new LeaderBoardRepo.Record(1, "kostya", 10));
        expected.add(new LeaderBoardRepo.Record(2, "mamay", 500));

        expected.forEach( record -> sut.insert(record.user(), record.score()));

        assertThat(sut.selectAll(), equalTo(expected));
    }

    @Test
    public void replaceShouldDeleteOneAndAddAnother() throws Exception {
        List<LeaderBoardRepo.Record> expected = new ArrayList<>();
        LeaderBoardRepo.Record toRemove = new LeaderBoardRepo.Record(1, "kostya", 10);
        expected.add(toRemove);
        expected.add(new LeaderBoardRepo.Record(2, "mamay", 500));
        expected.forEach( record -> sut.insert(record.user(), record.score()));
        expected.remove(toRemove);
        LeaderBoardRepo.Record toAdd = new LeaderBoardRepo.Record(3, "new", 1000);
        expected.add(toAdd);

        sut.replace(toRemove.id(), toAdd.user(), toAdd.score());

        assertThat(sut.selectAll(), equalTo(expected));
    }
}