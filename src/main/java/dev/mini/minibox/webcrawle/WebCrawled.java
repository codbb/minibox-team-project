package dev.mini.minibox.webcrawle;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebCrawled {
    public void scheduleData() {
        String result = "";
        try {
            URL url = new URL("https://megabox.co.kr/on/oh/ohc/Brch/schedulePage.do?brchNo1=&crtDe=20241223&detailType=area&firstAt=N&masterType=brch&playDe=20241223");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            result = reader.readLine();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONArray movieFormList = (JSONArray) jsonObject.get("movieFormList");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}