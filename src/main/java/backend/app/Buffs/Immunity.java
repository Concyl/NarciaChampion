package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONObject;

public class Immunity extends Buff{
    @Getter
    private Bufftype type;

    public Immunity(Hero origin, Hero target, String preciseOrigin, boolean isRemovable, int timer, String name, Bufftype type) {
        super(origin, target, preciseOrigin, isRemovable, timer, name);
        this.type = type;
    }

    public Immunity(String preciseOrigin, boolean isRemovable, int timer, String name, int id, Bufftype type) {
        super(preciseOrigin, isRemovable, timer, name, id);
        this.type = type;
    }

    public Immunity(JSONObject json) {
        super(json);
        this.type = Bufftype.valueOf((String)json.get("type"));
    }

    @Override
    public void apply(){
        this.target.getImpairments().removeIf(x -> x.getType() == this.type);
        this.target.addBuff(this);
        this.target.calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.type.toString()+" Immunity from "+this.origin.getFullname()+" by "+ this.name;
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            String s=this.target.getFullname() + " loses "+ this.type.toString() + " Immunity from " + this.origin.getFullname()+" by "+ this.name;
            this.origin.getBattlefield().getCombatText().addCombatText(s);
        }
    }
}
