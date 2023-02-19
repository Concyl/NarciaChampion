package backend.app.SpecialAbilities;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class OnDeathSpecialAbility extends SpecialAbility{
    @Getter @Setter private int stacks;
    public OnDeathSpecialAbility(JSONObject specialJSON){
        super(specialJSON);
        this.stacks = (int)(long) specialJSON.get("stacks");
    }

    public OnDeathSpecialAbility(){

    }

    public void trigger(){
        if(this.canActivate() && this.stacks != 0){
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
    @Override
    public void removeSpecialAbility() {

    }
}
