package backend.app.Buffs.SpecificAbilities;

import backend.app.Buffs.Ability;
import backend.app.Buffs.SpecialAbility;
import backend.app.DataLoader;
import backend.app.HeroInitController;
import org.json.simple.JSONObject;

public class CreateSpecialAbility extends Ability {
    int specialAbilityId;
    public CreateSpecialAbility(JSONObject specialJSON){
        this.specialAbilityId = (int)(long)specialJSON.get("id");
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        JSONObject special = DataLoader.getIDObjectfromJSON(this.specialAbilityId, HeroInitController.specialAbilitiesJSON,"id");
        SpecialAbility ability =  SpecialAbility.fromJSON(special);
    }
}
