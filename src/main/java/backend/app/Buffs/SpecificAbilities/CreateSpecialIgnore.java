package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import backend.app.Buffs.SpecialIgnores;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateSpecialIgnore extends Ability {

    private ArrayList<SpecialIgnores> ignores = new ArrayList<>();
    public CreateSpecialIgnore(JSONObject specialJSON){
        JSONArray jsonArray = (JSONArray) specialJSON.get("ignores");
        for(int i = 0;i<jsonArray.size();i++){
            this.ignores.add(SpecialIgnores.valueOf(jsonArray.get(i).toString()));
        }
    }

    @Override
    public void applySkill(SpecialAbility specialAbility) {
        for(SpecialIgnores ignore:this.ignores){
            //SpecialBuff buff = new SpecialBuff()
        }
    }
}
