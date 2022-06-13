package com.telegrambot.botoldhouse.Service;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Repository.SeanseRepository;
import com.telegrambot.botoldhouse.Telegram.Keybords.InlineKeybords;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SeanseService {
    private LocalDate ld = LocalDate.now();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MMMM", new Locale("ru"));
    private DateTimeFormatter dtfWeekDay = DateTimeFormatter.ofPattern("EEEE", new Locale("ru"));

    @Autowired
    private SeanseRepository seanseRepository;

    @Autowired
    private InlineKeybords inlineKeybords;


    public List<SendMessage> getByMontPageble(int mont, String chatId, int page){
        int messageInPage = 3;
        List<SendMessage> messageList = new ArrayList<>();
        List<Seanse> seanses = seanseRepository.findSeanseByMontPageble(mont, PageRequest.of(page-1, messageInPage));

        for (int i=0; i < seanses.size(); i++) {

            SendMessage sendMessage = seanseToMessage(seanses.get(i));
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(inlineKeybords.getPayLinkButtons(seanses.get(i)));
            messageList.add(sendMessage);

            if (i == seanses.size() - 1 & seanses.size() >= messageInPage & !seanseRepository.findSeanseByMontPageble(mont, PageRequest.of(page, messageInPage)).isEmpty()) {

                SendMessage nextMessage = new SendMessage(chatId, "нажмите Далее");
                nextMessage.setReplyMarkup(inlineKeybords.getNextButton(mont, page));
                messageList.add(nextMessage);
            }
        }
            return messageList;
    }

    /*
    Нужен чтобы выводить стартовую клавиатуру - только те месяцы, в которых есть сеансы.
     */
    public Boolean seanseExistInMonth(int index){
        if (seanseRepository.findSeanseByDate(ld.plusMonths(index).getMonthValue()).size() != 0){
            return true;
        } else return false;
    }

    /*
    Нужен для получения Продолжительности в формате HH:MM
    На сайте продолжительность могут написать как угодно
     */
    private String getTimeString(String str){
        if (!(str == null || str.equals(""))){
            String[] arr = str.trim().split("[^\\d]+");
            if (arr.length != 0) {
                String result = (arr[0] + ":" + arr[1]);
                return result;
            } else return "";
        } else return "";

    }

    private String getEndTime( String duration, LocalTime startTime){
        String emoji = EmojiParser.parseToUnicode(":frowning:");
        if (duration == null || duration.equals("")) {
            return emoji;
        } else {
            LocalTime lc = LocalTime.parse(duration, DateTimeFormatter.ofPattern("H:mm"));
            LocalTime endTime = startTime.plusHours(Long.valueOf(lc.getHour())).plusMinutes(Long.valueOf(lc.getMinute()));
            return endTime.toString();
        }
    }

    private SendMessage seanseToMessage(Seanse seanse){
        String dur = getTimeString(seanse.getDurattion());
        String clock = EmojiParser.parseToUnicode("\uD83D\uDD70");
        String calendar = EmojiParser.parseToUnicode("\uD83D\uDDD3");
        String text = (
                calendar + " " + seanse.getDate().format(dtf)
                + "  " + seanse.getDate().format(dtfWeekDay) + "\n"
                +clock +" <i> начало </i> "+ seanse.getTime().toString() +"  <i>окончание</i> "+getEndTime(dur,seanse.getTime())
                + "\n\n" + "<b>"+seanse.getName() +"</b>" + "\n\n"
                + "Продолжительность: " + dur
        );
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setText(text);
        return sendMessage;
    }

}
