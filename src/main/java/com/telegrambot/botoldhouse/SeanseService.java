package com.telegrambot.botoldhouse;

import com.telegrambot.botoldhouse.Entity.Seanse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SeanseService {
    private LocalDate ld = LocalDate.now();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM", new Locale("ru"));
    private DateTimeFormatter dtfWeekDay = DateTimeFormatter.ofPattern("EEEE", new Locale("ru"));

    @Autowired
    private SeanseRepository seanseRepository;

//
//    public Iterable<Seanse> addSeansesDB() throws IOException {
//
//        return seanseRepository.saveAll(parseToSeanse.getFullAfisha());
//    }

    public Iterable<Seanse> getAll() throws IOException {

        return seanseRepository.findAll();
    }

    public List<SendMessage> getByMont(int mont, String chatId){
        List<SendMessage> messageList = new ArrayList<>();
        List<Seanse> seanses = seanseRepository.findSeanseByDate(mont);

        for (Seanse s : seanses){

            String text = (s.getDate().format(dtf)+" "+s.getDate().format(dtfWeekDay)+"\n"
                    + s.getTime().toString()+"\n"+s.getName()+"\n"+"Продолжительность: "+s.getDurattion());
            SendMessage sendMessage = new SendMessage(chatId, text);
            InlineKeyboardMarkup inlineKeybord = new InlineKeyboardMarkup();
            InlineKeyboardButton buttonPay = new InlineKeyboardButton("купить билет");

            if (s.getPayLink().isBlank()){
                buttonPay.setText("нет онлайн билетов");
                buttonPay.setUrl("https://old-house.ru");
            } else buttonPay.setUrl(s.getPayLink());

            InlineKeyboardButton buttonLink = new InlineKeyboardButton("на сайт");

            if (s.getWebLink().isBlank()){
                buttonPay.setUrl("https://old-house.ru");
            } else buttonLink.setUrl(s.getWebLink());

            buttonLink.setUrl(s.getWebLink());
            List<InlineKeyboardButton> buttonList = new ArrayList<>();
            buttonList.add(buttonPay);
            buttonList.add(buttonLink);
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(buttonList);
            inlineKeybord.setKeyboard(rowList);
            sendMessage.setReplyMarkup(inlineKeybord);
            messageList.add(sendMessage);
        }

        return messageList;
    }


}
