package com.telegrambot.botoldhouse;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.io.IOException;
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
    private ParseToSeanse parseToSeanse;


    public Iterable<Seanse> getAll() throws IOException {
        return seanseRepository.findAll();
    }


    public void updateDB() throws IOException {
        List<Seanse> seanses = parseToSeanse.getFullAfisha();
        seanseRepository.deleteAll();
        seanseRepository.saveAll(seanses);
    }



    public List<SendMessage> getByMontPageble(int mont, String chatId, int page){
        int messageInPage = 3;
        List<SendMessage> messageList = new ArrayList<>();
        List<Seanse> seanses = seanseRepository.findSeanseByMontPageble(mont, PageRequest.of(page-1, messageInPage));
        System.out.println(seanses.size());
        System.out.println(seanses.get(0).getName());

        System.out.println(seanseToMessage(seanses.get(0)).getText());

        for (int i=0; i < seanses.size(); i++) {

            SendMessage sendMessage = seanseToMessage(seanses.get(i));
            sendMessage.setChatId(chatId);
            System.out.println(seanses.get(i).getDate());

            InlineKeyboardMarkup inlineKeybord = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            rowList.add(getPayLinkButtons(seanses.get(i)));

            inlineKeybord.setKeyboard(rowList);
            sendMessage.setReplyMarkup(inlineKeybord);
            messageList.add(sendMessage);

            if (i == seanses.size() - 1 & seanses.size() >= messageInPage & !seanseRepository.findSeanseByMontPageble(mont, PageRequest.of(page, messageInPage)).isEmpty()) {

                SendMessage nextMessage = new SendMessage(chatId, "нажмите Далее");
                InlineKeyboardButton buttonNext = new InlineKeyboardButton("Далее >");
                buttonNext.setCallbackData(String.valueOf(page)+","+String.valueOf(mont));
                List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
                buttonList2.add(buttonNext);
                List<List<InlineKeyboardButton>> rowList2 = new ArrayList<>();
                rowList2.add(buttonList2);
                nextMessage.setReplyMarkup(new InlineKeyboardMarkup(rowList2));
                messageList.add(nextMessage);
            }

        }
            return messageList;
    }

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

    private List<InlineKeyboardButton> getPayLinkButtons (Seanse seanse){

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

        return buttonList;
    }

    public Boolean seanseExistInMonth(int index){
        if (seanseRepository.findSeanseByDate(ld.plusMonths(index).getMonthValue()).size() != 0){
            return true;
        } else return false;
    }



}
