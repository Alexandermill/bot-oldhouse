package com.telegrambot.botoldhouse.Constants;

public enum MSG_Enum {
    START_MSG("Привет! Здесь можно получить афишу\n" +
            "театра \"Старый дом\" г.Новосибирск. \n\n" +

            "Режим <b>\"По названию:\"</b> \n" +
            "Нужно выбрать месяц, спектакль\n" +
            "и пролистать все показы спектакля\n" +
            "на месяц с подробностями.\n\n" +

            "Режим <b>\"Листать все:\"</b> \n" +
            "Нужно выбрать месяц\n" +
            "и пролистать все спектакли на месяц.\n\n"+

            "Для выбора месяца - используйте клавиатуру!\n" +
            "Для возврата в выбор режима нажмите /start"),
    MONTHLY_MSG(""),
    ALL_SEANSES_MSG("");

    private final String msg;

    MSG_Enum(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
