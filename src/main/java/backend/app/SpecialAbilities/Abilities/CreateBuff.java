package backend.app.SpecialAbilities.Abilities;

import backend.app.Buffs.Buff;
import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateBuff extends Ability {
    int buffID;
    public CreateBuff (JSONObject specialJSON){
        this.buffID = (int)(long)specialJSON.get("id");
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        JSONObject special = DataLoader.getIDObjectfromJSON(this.buffID, HeroInitController.buffJSON,"id");
        ArrayList<Hero> targets = specialAbility.getTarget().getTarget();
        for(Hero target : targets){
            Buff buff =  Buff.fromJSON(special);
            buff.setPreciseOrigin(specialAbility.getName());
            buff.setOrigin(specialAbility.getOwner());
            buff.setTarget(target);
            buff.apply();
        }
    }
}
