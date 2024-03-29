package backend.app.SpecialAbilities.Abilities;

import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateSpecialAbility extends Ability {
    int specialAbilityId;
    public CreateSpecialAbility(JSONObject specialJSON){
        this.specialAbilityId = (int)(long)specialJSON.get("id");
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        JSONObject special = DataLoader.getIDObjectfromJSON(this.specialAbilityId, HeroInitController.specialAbilitiesJSON,"id");
        ArrayList<Hero> targets = specialAbility.getTarget().getTarget();
        for(Hero target : targets){
            SpecialAbility ability =  SpecialAbility.fromJSON(special);
            ability.setPreciseOrigin(specialAbility.getName());
            target.addSpecialAbility(ability);
            ability.init(specialAbility.getOwner(),target);
            String s = target.getFullname()+" receives "+ability.getName()+" from "+specialAbility.getOwner().getFullname() +" by "+specialAbility.getName();
            target.getBattlefield().getCombatText().addCombatText(s);
        }
    }
}
