package com.telegrambot.botoldhouse.Telegram.Keybords;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeybords {

    public InlineKeyboardMarkup getInlineButtons(Seanse seanse, int page, int month, boolean next){



        InlineKeyboardButton buttonNext = new InlineKeyboardButton();
        InlineKeyboardButton buttonPrevios = new InlineKeyboardButton();
        InlineKeyboardButton buttonNull = new InlineKeyboardButton("   ");
        buttonNull.setCallbackData("null");



        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        if (page != 0) {
            buttonPrevios.setText(" < " + page);
            buttonPrevios.setCallbackData(String.valueOf(page -1 )+","+String.valueOf(month));
            buttonList.add(buttonPrevios);
        } else buttonList.add(buttonNull);

        if (next) {
            buttonNext.setText((page+2) + " > ");
            buttonNext.setCallbackData(String.valueOf(page +1 )+","+String.valueOf(month));
            buttonList.add(buttonNext);
        } else buttonList.add(buttonNull);

        InlineKeyboardMarkup inlineKeybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeybord.setKeyboard(rowList);

        return inlineKeybord;
    }

    public InlineKeyboardMarkup getNextButton (int mont, int page){
        InlineKeyboardButton buttonNext = new InlineKeyboardButton("Далее >");
        buttonNext.setCallbackData(String.valueOf(page)+","+String.valueOf(mont));
        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        buttonList2.add(buttonNext);
        List<List<InlineKeyboardButton>> rowList2 = new ArrayList<>();
        rowList2.add(buttonList2);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        inlineKeyboard.setKeyboard(rowList2);
        return inlineKeyboard;
    }

}
