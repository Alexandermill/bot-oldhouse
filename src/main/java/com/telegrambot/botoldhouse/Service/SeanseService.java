package com.telegrambot.botoldhouse.Service;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Repository.SeanseRepository;
import com.telegrambot.botoldhouse.Telegram.Keybords.InlineKeybords;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import com.telegrambot.botoldhouse.Constants.CalbackDataEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class SeanseService {
    private LocalDate ld = LocalDate.now();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM", new Locale("ru"));
    private DateTimeFormatter dtfWeekDay = DateTimeFormatter.ofPattern("EEEE", new Locale("ru"));
    private DateTimeFormatter Hmm = DateTimeFormatter.ofPattern("H:mm", new Locale("ru"));
    private String[] months = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    @Autowired
    private SeanseRepository seanseRepository;

    @Autowired
    private InlineKeybords inlineKeybords;

    @Autowired
    private CacheSeanse cacheSeanse;


    public SendMessage getByMontPageble(int month, String chatId, int page){
        SendMessage sendMessage = new SendMessage();
        int messageInPage = 1;
        List<Seanse> seanses = seanseRepository.findSeansesByMontPageble(month, PageRequest.of(page, messageInPage));

        for (int i=0; i < seanses.size(); i++) {

            sendMessage = seanseToMessage(seanses.get(i), page);
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(inlineKeybords.getInlineButtons(seanses.get(i), page, month,
                                        ifNotfinalPage(month, page), CalbackDataEnum.ALL_SEANSES.name())
                                        );

        }
            return sendMessage;
    }

    public EditMessageText getEditMessage(int month, int page, String chatId, Integer messageId){
        Seanse seanse = seanseRepository.findSeanseByMontPageble(month, PageRequest.of(page, 1));
        EditMessageText editMessageText = new EditMessageText();
        SendMessage sendMessage = seanseToMessage(seanse, page);
        editMessageText.enableHtml(true);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(sendMessage.getText());
        editMessageText.setReplyMarkup(inlineKeybords.getInlineButtons(seanse, page, month,
                                        ifNotfinalPage(month, page), CalbackDataEnum.ALL_SEANSES.name())
                                        );
        editMessageText.disableWebPagePreview();

        return editMessageText;
    }

    public EditMessageText getEditMessage(int month, int page, String chatId, String name, Integer messageId){
        Seanse seanse = seanseRepository.findSeanseByMonthAdNamePageble(month, name, PageRequest.of(page, 1));
        EditMessageText editMessageText = new EditMessageText();
        SendMessage sendMessage = seanseToMessage(seanse, page);
        editMessageText.enableHtml(true);
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(sendMessage.getText());
        editMessageText.setReplyMarkup(inlineKeybords.getInlineButtons(seanse, page, month,
                                        ifNotfinalPage(month, page, name), CalbackDataEnum.ONE_SEANSE.name(), name)
                                        );
        editMessageText.disableWebPagePreview();

        return editMessageText;
    }

    public SendMessage getMsgByMontAndNamePageble(int month, String chatId, String name, int page){
        SendMessage sendMessage = new SendMessage();
        int messageInPage = 1;
        Seanse seanse = seanseRepository.findSeanseByMonthAdNamePageble(month, name, PageRequest.of(page, messageInPage));
        sendMessage = seanseToMessage(seanse, page);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeybords.getInlineButtons(seanse, page, month,
                                    ifNotfinalPage(month, page, name), CalbackDataEnum.ONE_SEANSE.name(), name)
                                    );


        return sendMessage;
    }

    public SendMessage getNameSensesInMonth (int month, String chatId){
        SendMessage sendMessage = new SendMessage(chatId, "Спектакли на "+ months[month]+" :\n нажмите для перехода к показам и описанию");
        List<String> seansesName = seanseRepository.findSeansesNameByMonth(month);
        seansesName.forEach(s -> System.out.println(s+"\n"));
        sendMessage.setReplyMarkup(inlineKeybords.getInlineMonnthKeybord(seansesName, month));
        return sendMessage;
    }




    /*
    Нужен для получения Продолжительности в формате HH:MM
    На сайте продолжительность могут написать как угодно
     */
    public String getTimeString(String str) {
        if (!(str == null || str.equals(""))) {

            String[] arr = str.trim().split("[^\\d]+");

            if (arr.length > 1) {
                String result = (arr[0] + ":" + arr[1]);
                return result;
            } else if (arr.length == 1 & str.contains("минут")) {
                if (Integer.parseInt(arr[0]) >= 60) {
                    int minutes = Integer.parseInt(arr[0]) % 60;
                    int hours = Integer.parseInt(arr[0]) / 60;
                    return (Integer.toString(hours) + ":" + Integer.toString(minutes));
                } else return ("0:" + arr[0]);
            } else if (arr.length == 1 & str.contains("час")) {
                return (arr[0] + ":00");
            }
        }
        return "";
    }

    private boolean ifNotfinalPage(int month, int page){

        boolean next = false;
        if (!seanseRepository.findSeansesByMontPageble(month, PageRequest.of(page+1, 1)).isEmpty()){
            next = true;
        }
        return next;
    }

    private boolean ifNotfinalPage(int month, int page, String name){

        boolean next = false;
        if (seanseRepository.findSeanseByMonthAdNamePageble(month, name, PageRequest.of(page+1, 1)) != null){
            next = true;
        }
        return next;
    }



    private String getEndTime( String duration, LocalTime startTime){
        String emoji = EmojiParser.parseToUnicode(":frowning:");
        if (duration == null || duration.equals("")) {
            return emoji;
        } else {
            LocalTime lt = LocalTime.parse(duration, DateTimeFormatter.ofPattern("H:m"));
            LocalTime endTime = startTime.plusHours(Long.valueOf(lt.getHour())).plusMinutes(Long.valueOf(lt.getMinute()));
            return endTime.toString();
        }
    }

    private SendMessage seanseToMessage(Seanse seanse, int page){
        String dur = getTimeString(seanse.getDurattion());
        String clock = EmojiParser.parseToUnicode("\uD83D\uDD70");
        String calendar = EmojiParser.parseToUnicode("\uD83D\uDDD3");
        String payemoji = EmojiParser.parseToUnicode("\uD83D\uDCB3");
        page = page+1;
        String webLink;
        String pay;
        String palLink;
        if (seanse.getPayLink().isBlank()) {
            pay = "нет онлайн билетов";
            palLink = "";
        } else {
            pay = "купить билет";
            palLink = seanse.getPayLink();
        }

        if (seanse.getPayLink().isBlank()) {
            webLink = "https://old-house.ru";
        } else {
            webLink = seanse.getWebLink();
        }

        String text = (
                "#"+page+"     "+calendar + " " + "<b>"+ seanse.getDate().format(dtf)+"</b>"
                + "  " + seanse.getDate().format(dtfWeekDay) + "\n\n"
                +clock +" <i> начало </i> "+ seanse.getTime().toString() +"  <i>окончание</i> "+getEndTime(dur,seanse.getTime())
                + "\n\n" + "Спектакль: " + "<b>"+seanse.getName() +"</b>"  + "\n"
                +seanse.getAdress()+ "\n"
                + "Продолжительность: " + dur+"\n\n" + payemoji
                +" <a href=\"" + palLink + "\">"+ pay + "</a>" + "       " + "<a href=\"" + webLink + "\">на сайт</a>"+"\n\n"
                + "Описание: \n"
                + seanse.getDescription()

        );
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setText(text);
        sendMessage.disableWebPagePreview();
        return sendMessage;
    }

}
