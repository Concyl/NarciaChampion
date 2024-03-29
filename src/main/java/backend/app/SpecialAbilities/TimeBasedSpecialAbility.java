package backend.app.SpecialAbilities;

import backend.app.Hero;
import org.json.simple.JSONObject;

public class TimeBasedSpecialAbility extends SpecialAbility{

    public TimeBasedSpecialAbility(int cooldown, int timer,boolean silencable,
                                   String name, Hero origin, Hero owner, String preciseOrigin, boolean removable){
        super(cooldown,timer,silencable,name,origin,owner,preciseOrigin,removable);
    }

    public TimeBasedSpecialAbility(JSONObject specialJSON){
        super(specialJSON);
    }

    public TimeBasedSpecialAbility(){

    }

    @Override
    public void update() {
        if (this.cooldownTimer == 0 && (!this.owner.isSilenced() || !this.silencable) && this.checkConditions()) {
            this.setCooldown();
            this.applySkill();
        }
        super.update();
    }

    @Override
    public void removeSpecialAbility(){
        String s =(this.owner.getFullname()+" loses "+this.name + " TimeBasedSpecialAbility");
        this.owner.getBattlefield().getCombatText().addCombatText(s);
        this.owner.getSpecialAbilities().remove(this);
        this.owner.getTimespecialAbilities().remove(this);
    }
}
