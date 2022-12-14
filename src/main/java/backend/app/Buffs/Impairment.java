package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Impairment extends Buff{
    @Getter private Bufftype type;
    @Getter private ArrayList<SpecialIgnores> ignores = new ArrayList<>();
    public Impairment(Hero origin, Hero target, String preciseOrigin, boolean isRemovable, int timer, String name, ArrayList<SpecialIgnores> ignore) {
        super(origin, target, preciseOrigin, isRemovable, timer, name);
        this.type = type;
        this.ignores.addAll(ignore);
    }

    public Impairment(String preciseOrigin, boolean isRemovable, int timer, String name, int id, ArrayList<SpecialIgnores> ignore) {
        super(preciseOrigin, isRemovable, timer, name, id);
        this.type = type;
        this.ignores.addAll(ignore);
    }

    public Impairment(JSONObject json) {
        super(json);
        this.type = Bufftype.valueOf((String)json.get("type"));
    }

    @Override
    public void apply(){
        this.target.getI().removeIf(x -> x instanceof Statbuff && !((Statbuff) x).isImmunity && ((Statbuff) x).type == this.type);
        this.target.addBuff(this);
        calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.type.toString()+" Immunity from "+this.origin.getFullname();
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }
}
