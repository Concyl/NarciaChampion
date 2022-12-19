package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Impairment extends Buff{
    @Getter private Bufftype type;
    @Getter private ArrayList<SpecialIgnores> ignores = new ArrayList<>();
    public Impairment(Hero origin, Hero target, String preciseOrigin, boolean isRemovable, int timer, String name, ArrayList<SpecialIgnores> ignore, Bufftype type) {
        super(origin, target, preciseOrigin, isRemovable, timer, name);
        this.type = type;
        this.ignores.addAll(ignore);
    }

    public Impairment(String preciseOrigin, boolean isRemovable, int timer, String name, int id, ArrayList<SpecialIgnores> ignore, Bufftype type) {
        super(preciseOrigin, isRemovable, timer, name, id);
        this.type = type;
        this.ignores.addAll(ignore);
    }

    public Impairment(JSONObject json) {
        super(json);
        this.type = Bufftype.valueOf((String)json.get("type"));
        for(Object jsonObject : (JSONArray) json.get("ignore")){
            this.ignores.add(SpecialIgnores.valueOf((String) jsonObject));
        }
    }

    @Override
    public void apply(){
        if(!canApplyDebuff()){
            String s = this.target.getFullname()+" blocked "+this.type.toString()+" from "+this.origin.getFullname();
            this.origin.getBattlefield().getCombatText().addCombatText(s);
            return;
        }
        this.target.addBuff(this);
        this.target.calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.type.toString()+" from "+this.origin.getFullname();
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            String s=this.target.getFullname() + " loses "+ this.type.toString() + " from " + this.origin.getFullname();
            this.origin.getBattlefield().getCombatText().addCombatText(s);
        }
    }


    protected boolean canApplyDebuff(){
        if(this.target.isImmuneAll()){
            if(!this.ignores.contains(SpecialIgnores.ALL)){
                return false;
            }
        }
        return this.target.getImmunities().stream().noneMatch(x -> x.getType() == this.type);
    }
}
