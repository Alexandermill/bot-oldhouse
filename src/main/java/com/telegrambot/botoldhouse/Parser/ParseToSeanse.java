package com.telegrambot.botoldhouse.Parser;

    import com.google.common.base.Charsets;
import com.telegrambot.botoldhouse.Entity.Seanse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.telegrambot.botoldhouse.Telegram.OldHouseBot.logger;

@Component
public class ParseToSeanse {

    Clock clock = Clock.system(ZoneId.of("Asia/Novosibirsk"));
    LocalDate localDate = LocalDate.now(clock);
    DateTimeFormatter dtfMonth = DateTimeFormatter.ofPattern("MMMM", new Locale("RU"));
    String root = "https://old-house.ru";
    String jsonStr;



    URL url = null;

    public List<Seanse> getFullAfisha() throws IOException {
        List<Seanse> seanses = new ArrayList<>();
        Long id = 1L;

        String month = "";

        for (int i = 0; i < 6; i++) {
                LocalDate ld = localDate.plusMonths(i);
                String jsonStr = getJson(ld);
                month = ld.format(dtfMonth);

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
//                        if (aboutSeanse.select("div.right-box > ul").first().select("span").size() > 1){
//                            duration = aboutSeanse.select("div.right-box > ul").first().select("span").get(1).text();
//                        } else duration = "";

                        String parseDuration = aboutSeanse.getElementsByClass("right-box").select("li:contains(продолжительность) > span").text();
                        duration = getParsingTime(parseDuration);

                        description = aboutSeanse.select("p[style]").text();

                        String datestr = (dayOfMonth + " "+ month+" "+ ld.getYear());
                        LocalDate date = null;
                        LocalTime time = null;

                        if (datestr != null & datestr !="") {
                            date = LocalDate.parse(datestr, DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("ru")));
                        }

                        if (e.select("h3").text() != null & e.select("h3").text() != "") {
                            time = LocalTime.parse(e.select("h3").text(), DateTimeFormatter.ofPattern("HH:mm"));
                        }

                        String name = (e.select("h4 > a").text());
                        String adress = (e.select("p").text());
                        String payLink = (e.select("a").attr("data-src"));
                        String webLink = (root + "/" + e.select("h4 > a").attr("href").substring(1));
                        String desc = description.substring(0, description.indexOf('.', 200) + 1);  // краткое описание - несколько предложений

                        if (desc.length() > 400 & desc.contains("?")){
                            desc = description.substring(0, description.indexOf('?', 200) + 1);
                        }

                        byte[] byteDesc = desc.getBytes(StandardCharsets.UTF_8);
                        String descUTF8 = new String(byteDesc, Charsets.UTF_8);
                        id++;
                        Seanse seanse = new Seanse(id, date, time, name,  duration, payLink, webLink, descUTF8, adress);
                        logger.debug("Добавляем в базу: {} {} {}", seanse.getDate(), seanse.getTime(), seanse.getName());
                        seanses.add(seanse);

                    }
                }
        }

        id = 1L;
        return seanses;

    }

    private String getJson (LocalDate date){
        try {

            String month = date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
            month = month.replace(month.substring(0,1), month.substring(0,1).toUpperCase());

            URL url = new URL("https://old-house.ru/assets/modules/calendar/ajax/rep_return.php?z=" + month + "<sub>" + String.valueOf(date.getYear()) + "</sub>&now_lang=ru");


            url = url;
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
            logger.error(e.getMessage());
        }
        return jsonStr;
    }

    private String getParsingTime(String text){
        String str = text.toLowerCase();
        String[] arr = str.trim().split("[^\\d&^,)]+");

        for (int i = 0; i < arr.length; i++) {
        }

        if (str == null || str.equals("")){
            return "";
        }

        if(arr.length > 1 && str.contains(":")){
            if(arr[0].equals("")){
                arr[0] = "0";
            }
            return String.valueOf(arr[0])+":"+String.valueOf(arr[1]);
        }

        if(arr.length == 1 && str.contains("минут")){
            if(Integer.parseInt(arr[0]) >= 60){
                int minutes = Integer.parseInt(arr[0]) % 60;
                int hours = Integer.parseInt(arr[0]) / 60;
                return hours +":"+minutes;
            }
            return "0:" + arr[0];

        }

        if (arr.length == 1 && str.contains("час")){

            if(arr[0].contains(",")){
                String[] hourArray = arr[0].split(",");
                if (hourArray.length > 1){
                    Double minutes = Double.parseDouble("0."+hourArray[1])*60;
                    int min = minutes.intValue();
                    return hourArray[0] + ":"+min;
                }
            }

            if(arr[0].contains(".")){
                String[] hourArray = arr[0].split(".");
                if (hourArray.length > 1){
                    Double minutes = Double.parseDouble("0."+hourArray[1])*60;
                    int min = minutes.intValue();
                    return hourArray[0] + ":"+min;
                }
            }

            return arr[0] + ":00";
        }

        return "";

    }
}
