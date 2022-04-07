package backend.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DataLoader {
    public static ArrayList<JSONObject> loadHeroData() {
        JSONParser parser = new JSONParser();
        try {
            String filePath = new File("").getAbsolutePath();
            JSONObject obj = (JSONObject) parser.parse(new FileReader(filePath + "/src/main/resources/herodata.json"));
            JSONArray heroes = (JSONArray) (obj.get("Heroes"));
            ArrayList<JSONObject> herolist = new ArrayList<>();
            for (int i = 0; i < heroes.size(); i++) {
                JSONObject hero = (JSONObject) heroes.get(i);
                herolist.add(hero);
            }
            return herolist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
