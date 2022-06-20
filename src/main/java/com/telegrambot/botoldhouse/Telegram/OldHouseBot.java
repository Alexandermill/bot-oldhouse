package com.telegrambot.botoldhouse.Telegram;


import com.telegrambot.botoldhouse.Service.SeanseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.telegrambot.botoldhouse.Constants.CalbackDataEnum;


public class OldHouseBot extends SpringWebhookBot {

    static Logger userLogger = LoggerFactory.getLogger("telegram_bot");
    public static Logger logger = LoggerFactory.getLogger(OldHouseBot.class);

    private String botPath;
    private String botUsername;
    private String botToken;

    @Autowired
    SeanseService seanseService;

    @Autowired
    MessageHandler messageHandler;
    @Autowired
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
            if (update.hasCallbackQuery() && !update.getCallbackQuery().getData().equals("null")) {

//                userLogger.debug("User {}, {}, нажал кнопку {}", update.getCallbackQuery().getMessage().getChatId(),
//                        update.getCallbackQuery().getMessage().getChat().getFirstName(),
//                        update.getCallbackQuery().getMessage().getReplyMarkup().getKeyboard().get(0).get(0).getText());

                return callbackQueryHandler.CallbackQueryAnswer(update.getCallbackQuery());


//                String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
//                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
//                String[] arrData = update.getCallbackQuery().getData().split("#>");
//                if (arrData[0].equals(CalbackDataEnum.ALL_SEANSES.name())) {
//                    int page = Integer.parseInt(arrData[1]);
//                    int month = Integer.parseInt(arrData[2]);
//
//                    execute(seanseService.getEditMessage(month, page, chatId, messageId));
//                } else if (arrData[0].equals(CalbackDataEnum.GET_MSG.name())) {
//                    int page = Integer.parseInt(arrData[1]);
//                    int month = Integer.parseInt(arrData[2]);
//                    String name = arrData[3];
//                    SendMessage sendMessage = seanseService.getMsgByMontAndNamePageble(month, chatId, name, page);
//                    execute(sendMessage);
//                } else if (arrData[0].equals(CalbackDataEnum.ONE_SEANSE.name())) {
//                    int page = Integer.parseInt(arrData[1]);
//                    int month = Integer.parseInt(arrData[2]);
//                    String name = arrData[3];
//                    execute(seanseService.getEditMessage(month, page, chatId, name, messageId));
//
//                } else if (update.getCallbackQuery().getData().equals(CalbackDataEnum.BY_NAME.name())){
//                    SendMessage sendMessage = new SendMessage("Выберете месяц чтобы посмотреть афишу");
//                    sendMessage.setReplyMarkup();
//                }
            } else if (update.getMessage() != null && update.getMessage().getText() != null) {

                    execute(messageHandler.answerMessage(update.getMessage()));
                }





        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Попробуйте воспользоваться клавиатурой");
        } catch (Exception e) {
            logger.debug(e.toString());
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Что то пошло не так");
        }
        return null;
    }


}
