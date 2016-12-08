package org.telegram.mamot.services;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class DAO {

    public static final String QUOTES_FILE = "quotes/quotes";

    public String getEndWord(int index) {
        return endWords().get(index % endWords().size());
    }

    private static List<String> endWords() {
        ArrayList words = new ArrayList();
        words.add("наверное");
        words.add("может быть");
        words.add("мне кажетс€");
        words.add("уверен!");
        return words;
    }

    public Map<Integer, String> getWeekMenu() {
        Map<Integer, String> words = new HashMap();
        words.put(1, "30% на салаты и 2 коктейл€ по цене одного");
        words.put(2, "30% на роллы и 2 коктейл€ по цене одного");
        words.put(3, "30% на пасты и бургеры и 2 коктейл€ по цене одного");
        words.put(4, "30% на гор€чие закуски и 2 коктейл€ по цене одного");
        words.put(5, "с 3 часов ночи скидка на все алкогольные коктейли 30%");
        words.put(6, "с 3 часов ночи скидка на все алкогольные коктейли 30%");
        words.put(7, "30% на все гор€чие блюда и 2 коктейл€ по цене одного");
        return words;
    }

    private List<String> getQuotes() {
        List<String> list = new ArrayList<>();

        String path = getClass().getClassLoader().getResource(QUOTES_FILE).getPath().substring(1);
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
           list = br.lines().collect(toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void saveLevel(int lvl) {
        String file = "data.txt";
        System.out.println("Writing to file: " + file);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getClass().getResource("/data.txt").getPath()))) {
            writer.write("level:" + lvl);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getLevel() {
        try (BufferedReader writer = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/data.txt")))) {
            return Integer.valueOf(writer.readLine().split(":")[1]);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public String getQuote() {
        int i = new Random().nextInt(200) + 1;
        List<String> quotes = getQuotes();

        int qi = i % 2 == 0 ? i - 2 : i - 1;
        int ai = qi + 1;
        return quotes.get(qi) + "\n\n" + quotes.get(ai);
    }

    public String getComplement() {
        Map<Integer, String> words1 = new HashMap();
        words1.put(0, "так");
        words1.put(1, "очень");
        words1.put(2, "офигенски");
        words1.put(3, "просто");

        Map<Integer, String> words2 = new HashMap();
        words2.put(0, "круто");
        words2.put(1, "потр€сно");
        words2.put(2, "вкусно");
        words2.put(3, "улетно");

        Map<Integer, String> words3 = new HashMap();
        words3.put(0, "выгл€дишь");
        words3.put(1, "пахнешь");
        words3.put(2, "кодишь");
        words3.put(3, "говнокодишь");
        words3.put(4, "делаешь вид что работаешь");

        Map<Integer, String> words4 = new HashMap();
        words4.put(0, "снова");
        words4.put(1, "сегодн€");
        words4.put(2, "всегда");
        words4.put(3, "пупсик");

        Random random = new Random();
        return "ты " + words1.get(random.nextInt(4)) + " " +
               words2.get(random.nextInt(4)) + " " +
               words3.get(random.nextInt(5)) + " " +
               words4.get(random.nextInt(4));
    }

    public void updateScore(int score) {

    }
}