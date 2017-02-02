package org.telegram.services.repos;

import java.util.List;
import java.util.Objects;

public interface LeaderBoardRepo {
    List<Record> selectAll();
    void replace(int id, String username, long score);
    void insert(String username, long score);

    class Record {
        final int id;
        final String user;
        final long score;

        public Record(int id, String user, long score) {
            this.id = id;
            this.user = user;
            this.score = score;
        }

        public int id() {
            return id;
        }

        public String user() {
            return user;
        }

        public Long score() {
            return score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Record record = (Record) o;
            return score == record.score &&
                    Objects.equals(user, record.user);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, score);
        }
    }
}
