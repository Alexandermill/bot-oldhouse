package com.telegrambot.botoldhouse.Telegram;

import com.telegrambot.botoldhouse.SeanseService;
import com.telegrambot.botoldhouse.Telegram.Keybords.ReplyKeyboardMaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


public class OldHouseBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;
    private String botToken;
    private LocalDate ld = LocalDate.now();
    private String[] monts = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd ММММ yyyy", new Locale("ru"));
    int numCurrentMonth = ld.getMonth().getValue();
    int numNextMonth = ld.plusMonths(1).getMonth().getValue();
    int numNextMonth2 = ld.plusMonths(2).getMonth().getValue();
    String currentMonth = monts[ld.getMonth().getValue()];
    String nextMonth = monts[ld.plusMonths(1).getMonth().getValue()];
    String nextMonth2 = monts[ld.plusMonths(2).getMonth().getValue()];

    @Autowired
    SeanseService seanseService;

    @Autowired
    ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public OldHouseBot(SetWebhook setWebhook, MessageHandler messageHandler,CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public String getBotPath() {
        return botPath;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery()) {
                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                String[] data = update.getCallbackQuery().getData().split(",");
                int page = Integer.parseInt(data[0]);
                int month = Integer.parseInt(data[1]);

                List<SendMessage> messageList2 = seanseService.getByMontPageble(month, chatId, (page+1));
                for (SendMessage s: messageList2){
                    execute(s);
                }

            } else if (update.getMessage() != null && update.getMessage().getText() != null) {

                for (SendMessage sendMessage : messageHandler.answerMessage(update.getMessage())){
                        execute(sendMessage);
                    }
            }


        } catch (IllegalArgumentException e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Попробуйте воспользоваться клавиатурой");
        } catch (Exception e) {
            System.out.println(e);
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Что то пошло не так");
        }
        return null;
    }


}
