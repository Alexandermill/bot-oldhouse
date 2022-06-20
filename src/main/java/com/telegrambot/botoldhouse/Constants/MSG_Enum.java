package com.telegrambot.botoldhouse.Constants;

public enum MSG_Enum {
    START_MSG("Привет! Здесь можно получить афишу театра \"Старый дом\" г.Новосибирск \n" +
            "Просто выберете удобный для Вас способ!\n" +
            "Режим \"По названию\" выдает список спектаклей в ввыбранном месяце.\n" +
            "Выбрав спектакль, Вы получите список всех его показов в месце с подробностями.\n\n" +
            "Режим \"Листать все\" сразу выдаст Вам список всех спектаклей.\n\n" +
            "Для выбора месяца - используйте клавиатуру!"  ),
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
