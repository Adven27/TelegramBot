package org.telegram.services.impl;

import com.google.common.collect.Maps;
import org.junit.After;
import org.junit.Test;
import org.telegram.services.repos.impl.PGSQLGameRepo;

import java.util.Map;
import java.util.function.BiConsumer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PGSQLGameRepoTest {
    private PGSQLGameRepo sut = new PGSQLGameRepo("test_games");

    @After
    public void tearDown() throws Exception {
        sut.dropTable();
    }

    @Test
    public void insertAndSelectAll() throws Exception {
        Map expected = Maps.newHashMap();
        expected.put("kostya", "10");
        expected.put("mamay", "500");

        expected.forEach((BiConsumer<String, String>) (name, game) -> sut.insert(name, game));

        assertThat(sut.selectAll().entrySet(), equalTo(expected.entrySet()));
    }

    @Test
    public void update() throws Exception {
        sut.insert("mamay", "0");
        sut.update("mamay", "1");
        assertThat(sut.selectAll().get("mamay"), is("1"));
    }

    @Test
    public void delete() throws Exception {
        Map expected = Maps.newHashMap();
        expected.put("kostya", "10");
        expected.put("mamay", "500");
        expected.forEach((BiConsumer<String, String>) (name, game) -> sut.insert(name, game));
        expected.remove("kostya");

        sut.delete("kostya");

        assertThat(sut.selectAll().entrySet(), equalTo(expected.entrySet()));
    }
}