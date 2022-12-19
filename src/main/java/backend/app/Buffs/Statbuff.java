package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Statbuff extends Buff {

    @Getter private Bufftype type;
    @Getter private double amount;
    @Getter private boolean isBuff;
    public Statbuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name,
                    Bufftype type,double amount) {
        super(origin,target,preciseOrigin,isRemovable,timer,name);
        this.isBuff = isBuff;
        this.type = type;
        this.amount = amount;
    }

    public Statbuff(JSONObject json){
        super(json);
        this.isBuff= (boolean) json.get("isBuff");
        this.type = Bufftype.valueOf((String)json.get("type"));
        this.amount = (int)(long) json.get("amount");
    }

    @Override
    public void apply(){
        String s;
        if(this.isBuff){
            s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" Buff from "+this.origin.getFullname();
        }
        else{
            if(!canApplyDebuff()){
                s = this.target.getFullname()+" blocked "+this.type.toString()+" Debuff from "+this.origin.getFullname();
                this.origin.getBattlefield().getCombatText().addCombatText(s);
                return;
            }
            s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" DeBuff from "+this.origin.getFullname();
        }
        this.origin.getBattlefield().getCombatText().addCombatText(s);
        this.target.addBuff(this);
        this.type.recalculateStat(this.target);
    }

    private boolean canApplyDebuff(){
        if(this.target.isImmuneAll()){
            return false;
        }
        return this.target.getImmunities().stream().noneMatch(x -> x.getType() == this.type);
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            String s;
            if (this.isBuff) {
                s = this.target.getFullname() + " loses " + this.amount + "% " + this.type.toString() + " Buff from " + this.origin.getFullname();
            }
            else {
                s = this.target.getFullname() + " loses " + this.amount + "% " + this.type.toString() + " DeBuff from " + this.origin.getFullname();
            }
            this.origin.getBattlefield().getCombatText().addCombatText(s);
        }
    }
}
