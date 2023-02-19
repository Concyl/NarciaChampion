package backend.app.Conditions;

import backend.app.SpecialAbilities.SpecialAbility;
import org.json.simple.JSONObject;

public class HpUnderThresholdCondition extends Condition{
    double percentage;
    public HpUnderThresholdCondition(JSONObject specialJSON){
        this.percentage = (int)(long) specialJSON.get("percentage");
    }

    @Override
    public boolean checkCondition(SpecialAbility specialAbility) {
        return (specialAbility.getOwner().getCurrentHp()>=(specialAbility.getOwner().getMaxHp()*this.percentage/100));
    }
}
