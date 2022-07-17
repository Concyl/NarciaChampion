package backend.app;

import backend.app.Buffs.SpecialAbility;
import backend.app.Buffs.Talent;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Mainclass {

    public static void main(String[] args) throws IOException, ParseException {
        HeroInitController hero = new HeroInitController();
        hero.createHeroObjectswithIds();
        SpecialAbility specialAbility = hero.getSpecialAbilityfromId(1);
        Talent talent = hero.getTalentFromId(0);
      //  List<JSONObject> specialAbilites = DataLoader.loadSpecialAbilites("/src/main/resources/specialAbilities.json");
       // List<SpecialAbility> list = specialAbilites.stream().map(data -> SpecialAbility.fromJSON(data)).collect(Collectors.toList());

    }
}

