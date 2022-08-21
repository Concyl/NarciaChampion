package backend.app.Conditions;

import backend.app.Buffs.SpecialAbility;
import org.json.simple.JSONObject;

public abstract class Condition {
    public Condition(){

    }
    public static Condition fromJSON(JSONObject data) {
        try{
            String typename = (String) data.get("typename");
            Class<Condition> specialAbilityClass = (Class<Condition>) Class.forName(typename);
            return specialAbilityClass.getConstructor(JSONObject.class).newInstance(data);
        }
        catch (Exception e){
            return null;
        }
    }
    public abstract boolean checkCondition(SpecialAbility specialAbility);
}
