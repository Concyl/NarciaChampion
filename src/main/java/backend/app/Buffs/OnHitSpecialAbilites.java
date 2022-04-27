package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class OnHitSpecialAbilites extends SpecialAbility{
    @Getter @Setter private int chanceToAcitvate;
    private int stacks;

    public OnHitSpecialAbilites(int cooldown, int timer, boolean silencable, String name, String preciseOrigin, boolean removable,int chanceToAcitvate ) {
        super(cooldown, timer, silencable, name, preciseOrigin, removable);
        this.chanceToAcitvate = chanceToAcitvate;
    }

    @Override
    public void applySkill() {

    }

    public OnHitSpecialAbilites(JSONObject specialJSON){
        super(specialJSON);
        this.chanceToAcitvate = (int)(long) specialJSON.get("chanceToAcitvate");
    }

    public OnHitSpecialAbilites(){

    }

    @Override
    public void removeSpecialAbility() {

    }
}
