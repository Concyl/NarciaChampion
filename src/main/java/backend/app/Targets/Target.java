package backend.app.Targets;

import backend.app.Hero;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public abstract class Target {
    public enum TargetType{
        SELF,ALLY,ENEMY,TARGET;
    }
    @Setter @Getter private Hero caster;
    @Setter @Getter private boolean ignoresStealth;

    public Target(){}

    public static Target fromJSON(Hero caster,JSONObject data){
        try{
            String typename = (String) data.get("typename");
            Class<Target> targetClass = (Class<Target>) Class.forName(typename);
            Target target = targetClass.getConstructor(JSONObject.class).newInstance(data);
            target.setCaster(caster);
            return target;
        }
        catch (Exception e){
            return null;
        }
    }

    public abstract ArrayList<Hero> getTarget();
}
