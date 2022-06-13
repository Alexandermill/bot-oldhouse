package com.telegrambot.botoldhouse.Telegram.Keybords;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeybords {

    public InlineKeyboardMarkup getPayLinkButtons (Seanse seanse){

        String payemoji = EmojiParser.parseToUnicode("\uD83D\uDCB3");
        InlineKeyboardButton buttonPay = new InlineKeyboardButton(payemoji+ " купить билет");

        if (seanse.getPayLink().isBlank()) {
            buttonPay.setText("нет онлайн билетов");
            buttonPay.setUrl("https://old-house.ru");
        } else buttonPay.setUrl(seanse.getPayLink());

        InlineKeyboardButton buttonLink = new InlineKeyboardButton("на сайт");

        if (seanse.getWebLink().isBlank()) {
            buttonPay.setUrl("https://old-house.ru");
        } else buttonLink.setUrl(seanse.getWebLink());

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(buttonPay);
        buttonList.add(buttonLink);

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
