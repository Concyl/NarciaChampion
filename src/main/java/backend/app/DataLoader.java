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

    public static ArrayList<JSONObject> loadSpecialAbilites(String path){
        JSONParser parser = new JSONParser();
        try {
            String filePath = new File("").getAbsolutePath();
            JSONArray obj = (JSONArray) parser.parse(new FileReader(filePath + path));
            ArrayList<JSONObject> result = new ArrayList<>();
            for (int i = 0; i < obj.size(); i++) {
                JSONObject hero = (JSONObject) obj.get(i);
                result.add(hero);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getIDObjectfromJSON(int id, ArrayList<JSONObject> list,String search) {
        for (int i = 0; i < list.size(); i++) {
            long curid = (long) (list.get(i).get(search));
            if (id == Math.toIntExact(curid)) {
                return list.get(i);
            }
        }
        return null;
    }

    public static ArrayList<Integer> getJSONListfromJSON(JSONObject json,String search) {
        JSONArray jsonObject = (JSONArray) json.get(search);
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i = 0;i<jsonObject.size();i++){
            Long obj = (Long) jsonObject.get(i);
            ids.add((int) obj.doubleValue());
        }
        return ids;
    }

    public static ArrayList<String> getJSONListfromJSONString(JSONObject json,String search) {
        JSONArray jsonObject = (JSONArray) json.get(search);
        ArrayList<String> ids = new ArrayList<>();
        for(int i = 0;i<jsonObject.size();i++){
            String obj = (String) jsonObject.get(i);
            ids.add(obj);
        }
        return ids;
    }
}
