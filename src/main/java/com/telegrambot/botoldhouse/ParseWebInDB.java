package com.telegrambot.botoldhouse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ParseWebInDB {
    Clock clock = Clock.system(ZoneId.of("Asia/Novosibirsk"));
    LocalDate localDate = LocalDate.now(clock);
    LocalTime localTime = LocalTime.now(clock);
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM YYYY", new Locale("ru"));
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm", new Locale("ru"));
    DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MMMM", new Locale("RU"));
    DateTimeFormatter dtfDayWeek = DateTimeFormatter.ofPattern("EEEE", new Locale("RU"));
    String[] montsLocale = new String[] {"","Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август",
            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",};
    String year = String.valueOf(localDate.getYear());
    LocalDate nextMont1 = localDate.plusMonths(1);
    LocalDate nextMont2 = localDate.plusMonths(2);
    String monthNow = montsLocale[localDate.getMonth().getValue()];
    String monthNext1 = montsLocale[nextMont1.getMonth().getValue()];
    String monthNext2 = montsLocale[nextMont2.getMonth().getValue()];
    String dayAfisha;
    String duration = "";
    String description = "";
    List<String> textElements = new ArrayList<>();
    String root = "https://old-house.ru";

    URL url = null;

    public void dayAfisha() {
        try {
            URL url1 = new URL("https://old-house.ru/assets/modules/calendar/ajax/rep_return.php?z="+monthNow+"<sub>"+ String.valueOf(localDate.getYear()) + "</sub>&now_lang=ru");
            URL url2 = new URL("https://old-house.ru/assets/modules/calendar/ajax/rep_return.php?z="+monthNext1+"<sub>"+ String.valueOf(nextMont1.getYear()) + "</sub>&now_lang=ru");
            URL url3 = new URL("https://old-house.ru/assets/modules/calendar/ajax/rep_return.php?z="+monthNext2+"<sub>"+ String.valueOf(nextMont2.getYear()) + "</sub>&now_lang=ru");
            List<URL> urls = new ArrayList<>();
            urls.add(url1);
            urls.add(url2);
            urls.add(url3);
            String month = "";

            for (int i = 0; i < 3; i++) {
                LocalDate ld = localDate.plusMonths(i);
                month = ld.format(dtfMonth);
                url = urls.get(i);
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
                String jsonStr = StringEscapeUtils.unescapeJava(result.toString());
                http.disconnect();

                Document afisha = Jsoup.parseBodyFragment(jsonStr);
                Elements elements = afisha.select("div > h2, li");
                String cal = "";
                for (Element e : elements) {
                    if (e.select("h4").size() == 0) {
                        cal = "";
                        cal = e.select("span").text();
                    } else {
                        Document about = Jsoup.connect(root + "/" + e.select("h4 > a").attr("href").substring(1)).get();
                        duration = about.select("div.right-box > ul").first().select("span").get(1).text();
                        description = about.select("p[style]").text();

                        dayAfisha = (cal + " " + month + " " + ld.getYear() + " " + e.select("h3, h4 > a, p").text()
                                + " " + e.select("a").attr("data-src").toString() + " "
                                + e.select("h4 > a").attr("href").toString() + " " + duration + " "+ description.substring(0, description.indexOf('.', 200)+1));
                        textElements.add(dayAfisha);
                    }
                }
            }

            textElements.forEach(t -> System.out.println(t));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}