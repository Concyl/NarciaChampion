package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import backend.app.DamageEffect;
import backend.app.Hero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateSpecialIgnore extends Ability {

    private ArrayList<DamageEffect.SpecialIgnores> ignores = new ArrayList<>();
    public CreateSpecialIgnore(JSONObject specialJSON){
        JSONArray jsonArray = (JSONArray) specialJSON.get("ignores");
        for(int i = 0;i<jsonArray.size();i++){
            this.ignores.add(DamageEffect.SpecialIgnores.valueOf(jsonArray.get(i).toString()));
        }
    }

    @Override
    public void applySkill(SpecialAbility specialAbility) {
        Hero attacker = specialAbility.getOrigin();
        ArrayList<Hero> targets = specialAbility.getTarget().getTarget();
        for(Hero target : targets){
            if(attacker.getCurrentHp()<=0 || !attacker.isAlive()){
                continue;
            }
            else {
                DamageEffect damageEffect = new DamageEffect(attacker,target,type,percentage,specialAbility.getName(),this.ignores);
                damageEffect.applyDamage();
            }
        }
    }
}
