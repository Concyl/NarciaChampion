package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import backend.app.Buffs.SpecialBuff;
import backend.app.DamageEffect;
import backend.app.Hero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateSpecialIgnore extends Ability {

    private ArrayList<SpecialBuff.SpecialIgnores> ignores = new ArrayList<>();
    public CreateSpecialIgnore(JSONObject specialJSON){
        JSONArray jsonArray = (JSONArray) specialJSON.get("ignores");
        for(int i = 0;i<jsonArray.size();i++){
            this.ignores.add(SpecialBuff.SpecialIgnores.valueOf(jsonArray.get(i).toString()));
        }
    }

    @Override
    public void applySkill(SpecialAbility specialAbility) {
        for(SpecialBuff.SpecialIgnores ignore:this.ignores){
            //SpecialBuff buff = new SpecialBuff()
        }
    }
}
