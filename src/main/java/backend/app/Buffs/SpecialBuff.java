package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONObject;

public class SpecialBuff extends Buff{

    @Getter private SpecialIgnores type;
    @Getter private int value;
    @Getter private boolean ignore;
    public SpecialBuff(Hero origin, Hero target, String preciseOrigin, boolean isRemovable, int timer, String name, SpecialIgnores type,int value, boolean ignore) {
        super(origin, target,preciseOrigin,isRemovable,timer,name);
        this.type = type;
        this.value = value;
        this.ignore = ignore;
    }

    public SpecialBuff(JSONObject json){
        super(json);
        this.type = SpecialIgnores.valueOf((String)json.get("type"));
        this.value = (int)(long)json.get("value");
        this.ignore = (boolean)json.get("ignore");
    }

    @Override
    public void apply(){
        if(this.isIgnore()){
            this.target.addPassiveIgnore(this);
        }
        else{
            this.type.addSpecialIgnore(this);
        }
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            this.target.removeSpecialBuff(this);
        }
    }
}
