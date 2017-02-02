package org.telegram.services.repos;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardInMemRepo implements LeaderBoardRepo {
    private List<Record> records = new ArrayList<>();

    @Override
    public List<Record> selectAll() {
        return records;
    }

    @Override
    public void replace(int id, String username, long score) {
        records.removeIf(record -> record.id() == id);
        insert(username, score);
    }

    public void insert(String user, long score) {
        records.add(new Record(records.size(), user, score));
    }
}
