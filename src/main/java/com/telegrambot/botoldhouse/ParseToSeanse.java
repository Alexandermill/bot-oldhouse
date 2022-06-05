package com.telegrambot.botoldhouse;

import com.telegrambot.botoldhouse.Entity.Seanse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParseToSeanse {

    Clock clock = Clock.system(ZoneId.of("Asia/Novosibirsk"));
    LocalDate localDate = LocalDate.now(clock);
    LocalTime localTime = LocalTime.now(clock);
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru"));
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm", new Locale("ru"));
    DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MMMM", new Locale("RU"));
    String root = "https://old-house.ru";
    String jsonStr;
    List<Seanse> seanses = new ArrayList<>();



    public ParseToSeanse() {
    }


    URL url = null;

    public List<Seanse> getFullAfisha() throws IOException {

            String month = "";

            for (int i = 0; i < 3; i++) {
                LocalDate ld = localDate.plusMonths(i);
                String[] montsForUrl = new String[]{"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
                        "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};
                String year = String.valueOf(ld.getYear());
                String monthUrl = montsForUrl[ld.getMonth().getValue()];

            try {
                    URL urlNew = new URL("https://old-house.ru/assets/modules/calendar/ajax/rep_return.php?z=" + monthUrl + "<sub>" + String.valueOf(ld.getYear()) + "</sub>&now_lang=ru");


                    month = ld.format(dtfMonth);
                    url = urlNew;
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("GET");
                    http.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
                    http.setRequestProperty("Accept-Language", "ru,en;q=0.9");
                    http.setRequestProperty("Connection", "keep-alive");
                    http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    http.setRequestProperty("Host", "old-house.ru");
                    http.setRequestProperty("Sec-Fetch-Site", "same-origin");
                    http.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                    StringBuilder result = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                    for (String line; (line = reader.readLine()) != null; ) {
                        result.append(line);
                    }
                    jsonStr = StringEscapeUtils.unescapeJava(result.toString());
                    http.disconnect();

            } catch (Exception e) {
                System.out.println("ошибко: ==== \n"+e.getMessage() + "\n конец ошибко");
                e.printStackTrace();
            }

                Document afishaDocument = Jsoup.parseBodyFragment(jsonStr);
                Elements elements = afishaDocument.select("div > h2, li");
                String dayOfMonth = "";
                String duration = "";
                String description = "";
                for (Element e : elements) {
                    if (e.select("h4").size() == 0) {
                        dayOfMonth = "";
                        dayOfMonth = e.select("span").text();
                    } else {
                        Document aboutSeanse = Jsoup.connect(root + "/" + e.select("h4 > a").attr("href").substring(1)).get();
                        duration = aboutSeanse.select("div.right-box > ul").first().select("span").get(1).text();
                        description = aboutSeanse.select("p[style]").text();

                        String datestr = (dayOfMonth + " "+ month+" "+ ld.getYear());
                        LocalDate date = LocalDate.parse(datestr, DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru")));
                        LocalTime time = LocalTime.parse(e.select("h3").text(), DateTimeFormatter.ofPattern("HH:mm"));
                        String name = (e.select("h4 > a, p").text());
                        String payLink = (e.select("a").attr("data-src"));
                        String webLink = (root + "/" + e.select("h4 > a").attr("href").substring(1));
                        String desc = description.substring(0, description.indexOf('.', 200) + 1);

                        Seanse seanse = new Seanse(date, time, name, desc, duration, payLink, webLink);
                        seanses.add(seanse);

                    }
                }
            }

        seanses.forEach(s -> System.out.println(s.getName() +", "+ s.getDate()));
            return seanses;
            // сделать валидацию: на входе всех парсов (jsoup, localdate & time) = notnull + try catch?
        // сделать валидацию на входе bufferedreader = notnull, trow ParseExeption
        // try catch на весь метод ?




    }

}
