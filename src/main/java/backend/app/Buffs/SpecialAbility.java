package backend.app.Buffs;

import backend.app.Hero;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;

public abstract class SpecialAbility {

    @Getter @Setter protected int cooldownTimer = 0;
    @Getter @Setter protected boolean silencable;
    @Getter @Setter protected String name;
    @JsonIgnore
    @Getter @Setter protected Hero origin;
    @JsonIgnore
    @Getter @Setter protected Hero owner;
    @Getter @Setter protected String preciseOrigin;
    @Getter @Setter protected boolean removable;
    @Getter @Setter protected int timer;
    @Getter @Setter protected int cooldown;

    public SpecialAbility(int cooldown,int timer, boolean silencable, String name, Hero origin,Hero owner, String preciseOrigin, boolean removable) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.silencable = silencable;
        this.name = name;
        this.origin = origin;
        this.owner = owner;
        this.preciseOrigin = preciseOrigin;
        this.removable = removable;
    }

    public SpecialAbility(int cooldown,int timer, boolean silencable, String name, String preciseOrigin, boolean removable) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.silencable = silencable;
        this.name = name;
        this.preciseOrigin = preciseOrigin;
        this.removable = removable;
    }
    public SpecialAbility(){

    }

    public SpecialAbility(JSONObject specialJSON){
        this( (int)(long) specialJSON.get("cooldown"),
                (int)(long) specialJSON.get("timer"),
                (boolean) specialJSON.get("silencable"),
                (String)specialJSON.get("name"),
                (String)specialJSON.get("preciseOrigin"),
                (boolean) specialJSON.get("removable"));
    }

    public static SpecialAbility fromJSON(JSONObject data) {
        try{
            String typename = (String) data.get("typename");
            Class<SpecialAbility> specialAbilityClass = (Class<SpecialAbility>) Class.forName(typename);
            return specialAbilityClass.getConstructor(JSONObject.class).newInstance(data);
        }
        catch (Exception e){
            return null;
        }
    }

    public abstract void applySkill();

    public void update() {
        this.progressTimer();
        if(this.timer == 0){
            this.removeSpecialAbility();
        }
    }

    public void progressTimer(){
        if(this.timer > 0){
            this.timer--;
        }
        if(this.cooldownTimer > 0){
            this.cooldownTimer--;
        }
    }

    public abstract void removeSpecialAbility();
}
