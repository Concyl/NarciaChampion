package backend.app.Buffs;

import backend.app.Hero;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Statbuff extends Buff {

    @Getter private Bufftype type;
    @Getter private double amount;
    private boolean isImmunity;
    @Getter private boolean isBuff;
    public Statbuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, boolean isImmunity, int timer, String name,
                    Bufftype type,double amount) {
        super(origin,target,preciseOrigin,isRemovable,timer,name);
        this.isBuff = isBuff;
        this.type = type;
        this.amount = amount;
        this.isImmunity = isImmunity;
    }

    public Statbuff(JSONObject json){
        super(json);
        this.isBuff= (boolean) json.get("isBuff");
        this.type = Bufftype.valueOf((String)json.get("type"));
        this.amount = (int)(long) json.get("amount");
        this.isImmunity = (boolean) json.get("isImmunity");
    }

    @Override
    public void apply(){
        if(this.isImmunity){
           this.immunity();
        }
        else{
           this.buff();
        }
    }

    private void immunity(){
        if(this.isBuff){
            this.applyImmunityBuff();
        }
        else {
            this.applyImmunityDeBuff();
        }
    }

    private void applyImmunityBuff(){
        this.target.getBuffs().removeIf(x -> x instanceof Statbuff && !((Statbuff) x).isImmunity && ((Statbuff) x).type == this.type);
        this.target.addBuff(this);
        calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.type.toString()+" Immunity from "+this.origin.getFullname();
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    private void applyImmunityDeBuff(){
        if(canApplyDebuff()){
            String s = this.target.getFullname()+" blocks "+this.type.toString()+" from "+this.origin.getFullname();
            this.origin.getBattlefield().getCombatText().addCombatText(s);
            return;
        }
        this.target.addBuff(this);
        calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.type.toString()+" from "+this.origin.getFullname();
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    private void buff(){
        String s;
        if(this.isBuff){
            s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" Buff from "+this.origin.getFullname();
        }
        else{
            if(canApplyDebuff()){
                return;
            }
            s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" DeBuff from "+this.origin.getFullname();
        }
        this.origin.getBattlefield().getCombatText().addCombatText(s);
        this.target.addBuff(this);
        this.type.recalculateStat(this.target);
    }

    private boolean canApplyDebuff(){
        return this.target.getBuffs().stream().anyMatch(x -> x instanceof Statbuff && ((Statbuff) x).isImmunity && ((Statbuff) x).type == this.type);
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            if(this.isImmunity){
                if(this.isBuff){
                    this.target.getBuffs().remove(this);
                    String s =this.target.getFullname() + " loses "+ this.type.toString() + " Immunity from " + this.origin.getFullname();
                    this.origin.getBattlefield().getCombatText().addCombatText(s);
                }
                else{
                    this.target.getBuffs().remove(this);
                    calculateNegativeEffects();
                    String s=this.target.getFullname() + " loses "+ this.type.toString() + " from " + this.origin.getFullname();
                    this.origin.getBattlefield().getCombatText().addCombatText(s);
                }
            }
            else {
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

    private void calculateNegativeEffects(){
        this.target.resetNegativeEffets();
        for(Buff buff : this.target.getBuffs()){
            if(buff instanceof Statbuff && ((Statbuff) buff).isImmunity && !((Statbuff) buff).isBuff)
            switch(((Statbuff) buff).type){
                case FEAR:
                case STUN:
                case FROST:
                case ENTANGLE:
                case PETRIFY:
                    this.target.applyStuns();
                    break;
                case SILENCE:
                case INHIBIT:
                    this.target.applySilence();
                    this.target.cantReceiveEnergy();
                    break;
                case DISARM:
                    this.target.cantAutoAttack();
                case BLIND:
                    this.target.isBlinded();
                case PARALYZE:
                    this.target.applyStuns();
                    this.target.cantReceiveEnergy();
                    // TODO
                    // KEINE HEILUNG
            }
        }
    }
}
