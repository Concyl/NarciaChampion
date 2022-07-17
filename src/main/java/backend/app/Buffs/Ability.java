package backend.app.Buffs;

import org.json.simple.JSONObject;

public abstract class Ability {
    public Ability(){

    }
    public static Ability fromJSON(JSONObject data) {
        try{
            String typename = (String) data.get("typename");
            Class<Ability> specialAbilityClass = (Class<Ability>) Class.forName(typename);
            return specialAbilityClass.getConstructor(JSONObject.class).newInstance(data);
        }
        catch (Exception e){
            return null;
        }
    }
    public abstract void applySkill(SpecialAbility specialAbility);

    public boolean canActivate(SpecialAbility specialAbility){
        return true;
    }
}
