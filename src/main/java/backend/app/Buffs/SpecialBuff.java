package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONObject;

public class SpecialBuff extends Buff{

    @Getter private SpecialIgnores type;
    @Getter private int value;
    @Getter private boolean ignore;
    @Getter private int chance;
    @Getter private int stacks;
    public SpecialBuff(Hero origin, Hero target, String preciseOrigin, boolean isRemovable, int timer, String name, SpecialIgnores type,int value, boolean ignore, int chance,int stacks) {
        super(origin, target,preciseOrigin,isRemovable,timer,name);
        this.type = type;
        this.value = value;
        this.ignore = ignore;
        this.chance = chance;
        this.stacks = stacks;
    }

    public SpecialBuff(JSONObject json){
        super(json);
        this.type = SpecialIgnores.valueOf((String)json.get("type"));
        this.value = (int)(long)json.getOrDefault("value",-1L);
        this.chance = (int)(long)json.getOrDefault("chance",100L);
        this.stacks = (int)(long)json.getOrDefault("stacks",-1L);
        this.ignore = (boolean)json.get("ignore");
    }

    @Override
    public void apply(){
        this.target.addBuff(this);
        if(this.isIgnore()){
            this.target.addPassiveIgnore(this);
        }
        else{
            this.type.updateSpecialIgnore(this.target);
        }
        String s = this.target.getFullname()+" receives "+this.type.toString()+" from "+this.origin.getFullname()+" by "+ this.name;
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            String s = this.target.getFullname()+" loses "+this.type.toString()+" from "+this.origin.getFullname()+" by "+ this.name;
            this.origin.getBattlefield().getCombatText().addCombatText(s);
        }
    }

    public void decreaseStacks(){
        if(this.stacks >0){
            this.stacks --;
        }
        if(this.stacks == 0){
            String s = this.getTarget().getFullname()+" lost all stacks from "+this.name ;
            this.getTarget().getBattlefield().getCombatText().addCombatText(s);
        }
    }
}
