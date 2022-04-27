package backend.app;

import backend.app.Buffs.OnHitSpecialAbilites;
import backend.app.Buffs.SpecialAbility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
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

    public static ArrayList<JSONObject> loadSpecialAbilites(){
        JSONParser parser = new JSONParser();
        try {
            String filePath = new File("").getAbsolutePath();
            JSONArray obj = (JSONArray) parser.parse(new FileReader(filePath + "/src/main/resources/specialAbilities.json"));
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
}
