package backend.app.SpecialAbilities.Abilities;

import backend.app.Buffs.Buff;
import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import backend.app.SpecialAbilities.Ability;
import backend.app.SpecialAbilities.SpecialAbility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class CreateBuff extends Ability {
    ArrayList <Integer> buffID = new ArrayList<>();
    public CreateBuff (JSONObject specialJSON){
        JSONArray arr = (JSONArray) specialJSON.get("id");
        for (Object buff : arr) {
            int id = Integer.parseInt(buff.toString());
            this.buffID.add(id);
        }
    }
    @Override
    public void applySkill(SpecialAbility specialAbility) {
        for(int i : this.buffID) {
            JSONObject special = DataLoader.getIDObjectfromJSON(i, HeroInitController.buffJSON, "id");
            ArrayList<Hero> targets = specialAbility.getTarget().getTarget();
            for (Hero target : targets) {
                Buff buff = Buff.fromJSON(special);
                buff.setPreciseOrigin(specialAbility.getName());
                buff.setOrigin(specialAbility.getOwner());
                buff.setTarget(target);
                buff.apply();
            }
        }
    }
}
