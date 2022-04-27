package backend.app;

import backend.app.Buffs.OnHitSpecialAbilites;
import backend.app.Buffs.SpecialAbility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mainclass {

    public static void main(String[] args) throws IOException, ParseException {
        //HeroInitController hero = new HeroInitController();
        //hero.createHeroObjectswithIds();


        List<JSONObject> specialAbilites = DataLoader.loadSpecialAbilites();
        List<SpecialAbility> list = specialAbilites.stream().map(data -> SpecialAbility.fromJSON(data)).collect(Collectors.toList());

    }
}

