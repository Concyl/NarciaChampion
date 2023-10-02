package backend.app.SpecialAbilities.Abilities;

import backend.app.Hero;
import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Revive extends Ability {
    double percentage;
    public Revive(JSONObject specialJSON){
        this.percentage = (int)(long) specialJSON.get("percentage");
    }
    public Revive(double percentage){
        this.percentage = percentage;
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        specialAbility.getOwner().revive((int) this.percentage,specialAbility.getPreciseOrigin());
    }
}