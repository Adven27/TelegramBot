package org.telegram.mamot.services;

import java.util.*;

public class Huerator {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList(new Character[]{'а','я','о','ё','у','ю','ы','и','э','е'}));
    private static final Map<Character, Character> VOWEL_PAIRS = new HashMap<>();

    static {
        VOWEL_PAIRS.put('а', 'я');
        VOWEL_PAIRS.put('о', 'ё');
        VOWEL_PAIRS.put('у', 'ю');
        VOWEL_PAIRS.put('ы', 'и');
        VOWEL_PAIRS.put('э', 'е');
    }

    public String huate(String message) {
        for (int i = 0; i < message.length(); i++) {
            Character ch = (char) message.codePointAt(i);
            if (isVowel(ch)) {
                message = message.substring(i);
                if (isSubstitutable(ch)) {
                    Character pair = VOWEL_PAIRS.get(ch);
                    message = message.replaceFirst(ch.toString(), pair.toString());
                }
                break;
            }
        }
        return "ху" + message;
    }

    private static boolean isSubstitutable(Character ch) {
        return VOWEL_PAIRS.containsKey(ch);
    }

    private static boolean isVowel(Character ch) {
        return VOWELS.contains(ch);
    }

}