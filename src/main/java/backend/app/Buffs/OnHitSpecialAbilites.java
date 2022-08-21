package backend.app.Buffs;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class OnHitSpecialAbilites extends SpecialAbility{
    @Getter @Setter private int chanceToActivate;
    @Getter @Setter private int stacks;
    @Getter @Setter private int hpThreshold;

    public OnHitSpecialAbilites(JSONObject specialJSON){
        super(specialJSON);
        this.chanceToActivate = (int)(long) specialJSON.get("chanceToActivate");
        this.hpThreshold = (int)(long) specialJSON.get("hpThreshold");
        this.stacks = (int)(long) specialJSON.get("stacks");
    }

    public OnHitSpecialAbilites(){

    }

    public void trigger(){
        if(this.canActivate() && this.rollChanceToActivate() && this.checkConditions() && this.cooldownTimer == 0 && (!this.owner.isSilenced() || !this.silencable) && this.stacks != 0){
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
            if(roll<=this.chanceToActivate){
                return true;
            }
            return false;
        }
        else {
            return true;
        }
    }

    boolean canActivate(){
        return (this.getOwner().getCurrentHp() >= (this.getOwner().getMaxHp()*this.hpThreshold/100) || this.hpThreshold < 0);
    }
    @Override
    public void removeSpecialAbility() {
        this.owner.getSpecialAbilities().remove(this);
    }
}
