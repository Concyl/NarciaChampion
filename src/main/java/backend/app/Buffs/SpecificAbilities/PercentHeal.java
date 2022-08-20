package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PercentHeal extends Ability {
    double percentage;
    public PercentHeal(JSONObject specialJSON){
        this.percentage = (int)(long) specialJSON.get("percentage");
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        double percentageamount =specialAbility.getOwner().getMaxHp()*(percentage/100);
        ArrayList<Hero> targets = specialAbility.getTarget().getTarget();
        for (Hero target : targets) {
            if (target.getCurrentHp() > 0) {
                target.heal(percentageamount, specialAbility.getName());
            }
        }
    }

    @Override
    public boolean canActivate(SpecialAbility specialAbility){
        return true;
    }
}
