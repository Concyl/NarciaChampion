package backend.app.SpecialAbilities;

import backend.app.Hero;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class TriggerBasedSpecialAbility extends SpecialAbility{

    public TriggerBasedSpecialAbility(JSONObject specialJSON){
        super(specialJSON);
        this.chanceToActivate = (int)(long) specialJSON.get("chanceToActivate");
        this.stacks = (int)(long) specialJSON.get("stacks");
        this.trigger = (Trigger.valueOf( (String) specialJSON.get("trigger")));
    }
    public enum Trigger {
        ONDEATH,ONHIT,ONREVIVE
    }
    @Getter @Setter private int stacks;
    @Getter @Setter Trigger trigger;
    @Getter @Setter private int chanceToActivate;

    @Override
    public void removeSpecialAbility() {
        this.owner.getSpecialAbilities().remove(this);
    }


    public void trigger(){
        if(this.canActivate() && this.rollChanceToActivate() && this.stacks != 0){
            this.setCooldown();
            if(this.stacks > 0){
                this.stacks--;
            }
            this.applySkill();
            if(this.stacks == 0){
                this.removeSpecialAbility();
            }
        }
    }

    private boolean rollChanceToActivate(){
        if(this.chanceToActivate < 100){
            double maxRoll = 100;
            double minRoll = 1;
            double roll = (int)(Math.random()*(maxRoll-minRoll+1))+minRoll;
            return roll <= this.chanceToActivate;
        }
        else {
            return true;
        }
    }
}
