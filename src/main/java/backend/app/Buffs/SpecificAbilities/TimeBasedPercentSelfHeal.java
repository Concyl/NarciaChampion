package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.TimeBasedSpecialAbility;
import backend.app.Hero;
import org.json.simple.JSONObject;

public class TimeBasedPercentSelfHeal extends TimeBasedSpecialAbility {

    double percentage;

    public TimeBasedPercentSelfHeal(int cooldown, int timer, boolean silencable, String name, Hero origin, Hero owner, String preciseOrigin, boolean removable,double percentage) {
        super(cooldown, timer, silencable, name, origin, owner, preciseOrigin, removable);
        this.percentage = percentage;
    }

    public TimeBasedPercentSelfHeal(JSONObject specialJSON){
        super(specialJSON);
        this.percentage = (int)(long) specialJSON.get("percentage");
    }

    @Override
    public void applySkill() {
        double percentageamount =this.owner.getMaxHp()*(percentage/100);
        this.owner.heal(percentageamount,this.preciseOrigin);
    }
}
