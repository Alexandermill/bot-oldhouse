package com.telegrambot.botoldhouse.Telegram.Keybords;

import com.telegrambot.botoldhouse.Service.CacheSeanse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ReplyKeyboardMaker {

    @Autowired
    CacheSeanse cacheSeanse;

    private LocalDate ld = LocalDate.now();
    private String[] months = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd ММММ yyyy", new Locale("ru"));

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        for (int i=0; i < 6; i++ ){
            if (cacheSeanse.ifExistByMonth(ld.plusMonths(i).getMonth().getValue())){
                if (i <= 2){
                    row1.add(new KeyboardButton(months[ld.plusMonths(i).getMonth().getValue()]));
                } else row2.add(new KeyboardButton(months[ld.plusMonths(i).getMonth().getValue()]));
            }
        }

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getSeanseByMonthKeybord() {
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();

        for (int i=0; i < 6; i++ ){
            if (cacheSeanse.ifExistByMonth(i)){
                if (i <= 2){
                    row1.add(new KeyboardButton(months[ld.plusMonths(i).getMonth().getValue()]+"_"));
                } else row2.add(new KeyboardButton(months[ld.plusMonths(i).getMonth().getValue()]+"_"));
            }
        }

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }
}