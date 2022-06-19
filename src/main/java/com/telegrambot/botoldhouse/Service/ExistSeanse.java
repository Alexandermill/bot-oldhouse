package com.telegrambot.botoldhouse.Service;

import com.telegrambot.botoldhouse.Entity.Seanse;
import com.telegrambot.botoldhouse.Repository.SeanseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExistSeanse {

    @Autowired
    private SeanseRepository seanseRepository;

    private List<SeanseDateTimeName> seansDTN = new ArrayList<>();
    private boolean exist;
    private String[] months = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

/*
Конструктор заполняет поле seanseDTN данными из базы при старте и при обновлении базы. Дальше поле использыется для
проверки есть или нет сеанс в базе, без обращения к базе.
 */
    @PostConstruct
    public void getExistSeansesFromDB(){
        LocalDate ld = LocalDate.now();
        this.seansDTN.clear();
        for (int i = 0; i < 6; i++) {
            int month = ld.plusMonths(i).getMonth().getValue();
            List<Seanse> seanses = seanseRepository.findSeanseByMonth(month);
            seanses.forEach(s -> {
                SeanseDateTimeName seanseDateTimeName = new SeanseDateTimeName(s.getDate(), s.getTime(), s.getName());
                this.seansDTN.add(seanseDateTimeName);
            });
        }
    }

    public boolean ifExistByMonth(int month){
        exist = false;
        seansDTN.forEach(sDTN -> {
            if (sDTN.getDate().getMonth().getValue() == month){
                exist = true;
            }
        });
        return exist;
    }

    public boolean ifExistByMonth(String month){
        exist = false;
        int monthNum = monthTonum(month);
        seansDTN.forEach(sDTN -> {
            if (sDTN.getDate().getMonth().getValue() == monthNum){
                exist = true;
            }
        });
        return exist;
    }

    private int monthTonum(String monthText){
        return Arrays.asList(months).indexOf(monthText);
    }
}