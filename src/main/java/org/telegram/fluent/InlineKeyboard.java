package org.telegram.fluent;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
public class InlineKeyboard {
    InlineKeyboardMarkup ikb = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> kb = new ArrayList<>();

    public InlineKeyboard row(String... textDataPairs) {
        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i =0 ; i < textDataPairs.length; i = i + 2) {
            InlineKeyboardButton b = new InlineKeyboardButton();
            b.setText(textDataPairs[i]);
            b.setCallbackData(textDataPairs[i + 1]);
            row.add(b);
        }
        kb.add(row);
        return this;
    }

    public InlineKeyboardMarkup build() {
        ikb.setKeyboard(kb);
        return ikb;
    }
}
