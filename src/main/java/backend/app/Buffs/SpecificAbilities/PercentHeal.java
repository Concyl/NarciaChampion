package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import org.json.simple.JSONObject;

public class PercentHeal extends Ability {
    double percentage;
    public PercentHeal(JSONObject specialJSON){
        this.percentage = (int)(long) specialJSON.get("percentage");
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        double percentageamount =specialAbility.getOwner().getMaxHp()*(percentage/100);
        specialAbility.getOwner().heal(percentageamount,specialAbility.getPreciseOrigin());
    }
}
