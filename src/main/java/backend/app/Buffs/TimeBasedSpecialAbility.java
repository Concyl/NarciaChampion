package backend.app.Buffs;

import backend.app.Hero;
import org.json.simple.JSONObject;

public abstract class TimeBasedSpecialAbility extends SpecialAbility{

    public TimeBasedSpecialAbility(int cooldown, int timer,boolean silencable,
                                   String name, Hero origin, Hero owner, String preciseOrigin, boolean removable){
        super(cooldown,timer,silencable,name,origin,owner,preciseOrigin,removable);
        this.owner.getTimespecialAbilities().add(this);
    }

    public TimeBasedSpecialAbility(JSONObject specialJSON){
        super(specialJSON);
    }

    @Override
    public void update() {
        super.update();
        if (this.cooldownTimer == 0 && (!this.owner.isSilenced() || !this.silencable)) {
            this.setCooldown();
            this.applySkill();
        }
    }

    public void setCooldown(){
        this.cooldownTimer = this.cooldown;
    }

    @Override
    public void removeSpecialAbility(){
        this.owner.getTimespecialAbilities().remove(this);
    }
}
