package org.telegram.mamot.services;

import java.util.*;

public class Huerator {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList(new Character[]{'а', 'я', 'о', 'ё', 'у', 'ю', 'ы', 'и', 'э', 'е'}));
    private static final Map<Character, Character> VOWEL_PAIRS = new HashMap<>();
    private static final String COMMAND_PREFIX = "//";

    static {
        VOWEL_PAIRS.put('а', 'я');
        VOWEL_PAIRS.put('о', 'ё');
        VOWEL_PAIRS.put('у', 'ю');
        VOWEL_PAIRS.put('ы', 'и');
        VOWEL_PAIRS.put('э', 'е');
    }

    public String huate(String message) {
        String res = "";
        for (String word : removePrefix(message).toLowerCase().trim().replaceAll(" +", " ").split(" ")) {
            for (int i = 0; i < word.length(); i++) {
                if (hasVowels(word)) {
                    Character ch = word.charAt(i);
                    if (isVowel(ch)) {
                        word = word.substring(i);
                        if (isSubstitutable(ch)) {
                            Character pair = VOWEL_PAIRS.get(ch);
                            word = word.replaceFirst(ch.toString(), pair.toString());
                        }
                        word = "ху" + word;
                        break;
                    }
                }
            }
            res += word + " ";
        }
        return res;
    }

    private boolean hasVowels(String word) {
        return word.chars().filter(p -> VOWELS.contains(Character.valueOf((char) p))).findFirst().isPresent();
    }

    private String removePrefix(String message) {
        return message.substring(COMMAND_PREFIX.length());
    }

    private static boolean isSubstitutable(Character ch) {
        return VOWEL_PAIRS.containsKey(ch);
    }

    private static boolean isVowel(Character ch) {
        return VOWELS.contains(ch);
    }

}