package backend.app.SpecialAbilities.Abilities;

import backend.app.Hero;
import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ReviveTalent extends Ability {
    double percentage;
    public ReviveTalent(JSONObject specialJSON){
        this.percentage = (int)(long) specialJSON.get("percentage");
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        ArrayList<Hero> targets = specialAbility.getTarget().getTarget();
        for (Hero target : targets) {
            target.reviveTalent(this.percentage, specialAbility.getName());
        }
    }
}
