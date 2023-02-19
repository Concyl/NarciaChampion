package backend.app.SpecialAbilities.Abilities;

import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
import backend.app.Buffs.SpecialIgnores;
import backend.app.DamageEffect;
import backend.app.Hero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DealDamage extends Ability {
    private double percentage;
    private DamageEffect.DamageType type;
    private ArrayList<SpecialIgnores> ignores = new ArrayList<>();
    public DealDamage(JSONObject specialJSON){
        this.percentage = (double)(long) specialJSON.get("percentage");
        this.type = DamageEffect.DamageType.valueOf((String)specialJSON.get("type"));
        JSONArray jsonArray = (JSONArray) specialJSON.get("ignores");
        for(int i = 0;i<jsonArray.size();i++){
            this.ignores.add(SpecialIgnores.valueOf(jsonArray.get(i).toString()));
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
