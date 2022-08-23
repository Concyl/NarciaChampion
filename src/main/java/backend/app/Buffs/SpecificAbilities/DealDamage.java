package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import backend.app.DamageEffect;
import backend.app.Hero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DealDamage extends Ability {
    private double percentage;
    private DamageEffect.DamageType type;
    private ArrayList<DamageEffect.SpecialIgnores> ignores = new ArrayList<>();
    public DealDamage(JSONObject specialJSON){
        this.percentage = (double)(long) specialJSON.get("percentage");
        this.type = DamageEffect.DamageType.valueOf((String)specialJSON.get("type"));
        JSONArray jsonArray = (JSONArray) specialJSON.get("ignores");
        for(int i = 0;i<jsonArray.size();i++){
            this.ignores.add(DamageEffect.SpecialIgnores.valueOf(jsonArray.get(i).toString()));
        }
    }

    @Override
    public void applySkill(SpecialAbility specialAbility) {
        Hero attacker = specialAbility.getOwner();
        for(Hero target : specialAbility.getTarget().getTarget()){
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
