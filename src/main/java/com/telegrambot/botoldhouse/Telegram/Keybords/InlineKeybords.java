package com.telegrambot.botoldhouse.Telegram.Keybords;

import com.telegrambot.botoldhouse.Entity.Seanse;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import com.telegrambot.botoldhouse.Constants.CalbackDataEnum;

import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeybords {

    public InlineKeyboardMarkup getInlineButtons(Seanse seanse, int page, int month, boolean next, String prefix){

        InlineKeyboardButton buttonNext = new InlineKeyboardButton();
        InlineKeyboardButton buttonPrevios = new InlineKeyboardButton();
        InlineKeyboardButton buttonNull = new InlineKeyboardButton("   ");
        buttonNull.setCallbackData("null");

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        if (page != 0) {
            buttonPrevios.setText(" < " + page);
            buttonPrevios.setCallbackData(prefix +"#>"+ String.valueOf(page -1 )+"#>"+String.valueOf(month));
            buttonList.add(buttonPrevios);
        } else buttonList.add(buttonNull);

        if (next) {
            buttonNext.setText((page+2) + " > ");
            buttonNext.setCallbackData(prefix +"#>"+ String.valueOf(page +1 )+"#>"+String.valueOf(month));
            buttonList.add(buttonNext);
        } else buttonList.add(buttonNull);

        InlineKeyboardMarkup inlineKeybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeybord.setKeyboard(rowList);

        return inlineKeybord;
    }

    public InlineKeyboardMarkup getInlineButtons(Seanse seanse, int page, int month, boolean next, String prefix, String name){

        InlineKeyboardButton buttonNext = new InlineKeyboardButton();
        InlineKeyboardButton buttonPrevios = new InlineKeyboardButton();
        InlineKeyboardButton buttonNull = new InlineKeyboardButton("   ");
        buttonNull.setCallbackData("null");

        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        if (page != 0) {
            buttonPrevios.setText(" < " + page);
            buttonPrevios.setCallbackData(prefix +"#>"+ String.valueOf(page -1 )+"#>"+String.valueOf(month)+"#>"+name);
            buttonList.add(buttonPrevios);
        } else buttonList.add(buttonNull);

        if (next) {
            buttonNext.setText((page+2) + " > ");
            buttonNext.setCallbackData(prefix +"#>"+ String.valueOf(page +1 )+"#>"+String.valueOf(month)+"#>"+name);
            buttonList.add(buttonNext);
        } else buttonList.add(buttonNull);

        InlineKeyboardMarkup inlineKeybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        inlineKeybord.setKeyboard(rowList);

        return inlineKeybord;
    }

    public InlineKeyboardMarkup getInlineMonnthKeybord(List<String> seansesName, int month){
        InlineKeyboardMarkup monthKeybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int page = 0;

        for (int i = 0; i < seansesName.size(); i++) {
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            InlineKeyboardButton buttonSeanse = new InlineKeyboardButton(seansesName.get(i));
            buttonSeanse.setCallbackData(CalbackDataEnum.GET_MSG.name() + "#>" + page + "#>" + month +"#>" + seansesName.get(i).toString());
            buttonList.add(buttonSeanse);
            rowList.add(buttonList);
        }

        monthKeybord.setKeyboard(rowList);
        return monthKeybord;

    }

    public InlineKeyboardMarkup getStartInlineButtons(){
        InlineKeyboardButton byNameButton = new InlineKeyboardButton("По названию");
        InlineKeyboardButton listAllButton = new InlineKeyboardButton("Листать все");
        byNameButton.setCallbackData(CalbackDataEnum.BY_NAME.name());
        listAllButton.setCallbackData(CalbackDataEnum.LIST_ALL.name());
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(byNameButton);
        buttonList.add(listAllButton);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(buttonList);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(rowList);

        return inlineKeyboardMarkup;
    }


}
