package org.telegram.mamot.services;

import java.util.*;

public class Huerator {

    private static final Set<Character> VOWELS = new HashSet<>(Arrays.asList(new Character[]{'�','�','�','�','�','�','�','�','�','�'}));
    private static final Map<Character, Character> VOWEL_PAIRS = new HashMap<>();

    static {
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
        VOWEL_PAIRS.put('�', '�');
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
        return "��" + message;
    }

    private static boolean isSubstitutable(Character ch) {
        return VOWEL_PAIRS.containsKey(ch);
    }

    private static boolean isVowel(Character ch) {
        return VOWELS.contains(ch);
    }

}