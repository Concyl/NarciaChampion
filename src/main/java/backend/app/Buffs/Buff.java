package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public class Buff {
    @Getter @Setter protected String preciseOrigin;
    @Getter @Setter protected boolean isBuff;
    @Getter @Setter protected boolean isRemovable;
    @Getter @Setter protected int timer;
    @Getter @Setter protected int id;
    @Getter @Setter protected String name;
    @Getter @Setter protected Hero target;
    @Getter @Setter protected Hero origin;

    public Buff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name) {
        this.preciseOrigin = preciseOrigin;
        this.isBuff = isBuff;
        this.isRemovable = isRemovable;
        this.timer = timer;
        this.name = name;
        this.origin = origin;
        this.target = target;
    }

    public Buff(String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name, int id) {
        this.preciseOrigin = preciseOrigin;
        this.isBuff = isBuff;
        this.isRemovable = isRemovable;
        this.timer = timer;
        this.name = name;
        this.id= id;
    }

    public static Buff fromJSON(JSONObject data){
        try{
            String typename = (String) data.get("typename");
            Class<Buff> buffClass = (Class<Buff>) Class.forName(typename);
            return buffClass.getConstructor(JSONObject.class).newInstance(data);
        }
        catch (Exception e){
            return null;
        }
    }

    public void apply(){}

    public Buff(JSONObject json){
       this((String)json.get("preciseOrigin"),
               (boolean) json.get("isRemovable"),
               (boolean) json.get("isBuff"),
               (int)(long) json.get("timer"),
               (String)json.get("name"),
               (int)(long)json.get("id"));
    }

    public void update() {
        if(this.timer > 0){
            this.timer--;
        }
    }
}