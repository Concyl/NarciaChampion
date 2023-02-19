package backend.app.SpecialAbilities;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class OnHitSpecialAbility extends SpecialAbility{
    @Getter @Setter private int chanceToActivate;
    @Getter @Setter private int stacks;

    public OnHitSpecialAbility(JSONObject specialJSON){
        super(specialJSON);
        this.chanceToActivate = (int)(long) specialJSON.get("chanceToActivate");
        this.stacks = (int)(long) specialJSON.get("stacks");
    }

    public OnHitSpecialAbility(){

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

    @Override
    public void removeSpecialAbility() {
        this.owner.getSpecialAbilities().remove(this);
    }
}
