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

    @Override
    public void removeSpecialAbility() {
        this.owner.getTimespecialAbilities().remove(this);
    }
}
