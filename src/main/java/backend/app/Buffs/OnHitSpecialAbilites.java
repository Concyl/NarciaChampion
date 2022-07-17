package backend.app.Buffs;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class OnHitSpecialAbilites extends SpecialAbility{
    @Getter @Setter private int chanceToAcitvate;
    @Getter @Setter private int stacks;

    public OnHitSpecialAbilites(JSONObject specialJSON){
        super(specialJSON);
        this.chanceToAcitvate = (int)(long) specialJSON.get("chanceToAcitvate");
        this.stacks = (int)(long) specialJSON.get("stacks");
    }

    public OnHitSpecialAbilites(){

    }

    public void trigger(){
        if(this.rollChanceToActivate() && this.ability.canActivate(this) && this.cooldownTimer == 0 && (!this.owner.isSilenced() || !this.silencable) && this.stacks != 0){
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
        if(this.chanceToAcitvate < 100){
            double maxRoll = 100;
            double minRoll = 1;
            double roll = (int)(Math.random()*(maxRoll-minRoll+1))+minRoll;
            if(roll<=this.chanceToAcitvate){
                return true;
            }
            return false;
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
