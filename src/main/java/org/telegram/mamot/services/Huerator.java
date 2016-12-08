package org.telegram.mamot.services;

import java.util.*;

public class Huerator {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList(new Character[]{'�', '�', '�', '�', '�', '�', '�', '�', '�', '�'}));
    private static final Map<Character, Character> VOWEL_PAIRS = new HashMap<>();
    private static final String COMMAND_PREFIX = "//";

    static {
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
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
                        word = "��" + word;
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