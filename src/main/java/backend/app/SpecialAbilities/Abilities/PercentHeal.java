package backend.app.SpecialAbilities.Abilities;

import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
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
}
