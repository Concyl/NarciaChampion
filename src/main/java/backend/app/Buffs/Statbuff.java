package backend.app.Buffs;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Statbuff extends Buff {
    public enum Bufftype{
        ATTACK,
        HP,
        DEF,
        CRIT,
        CRITDAMAGE,
        CRITDEF,
        ACCURACY,
        EVASION,
        MOVEMENTSPEED,
        HEALING,
        ATTACKSPEED,
        ENERGY,
        STUN,
        FEAR,
        FROST,
        SILENCE,
        DISARM,
        PETRIFY,
        ENTANGLE,
        BLIND,
        INHIBIT,
        PARALYZE
    }

    public Bufftype getType() {
        return type;
    }

    private Bufftype type;
    private double amount;
    private boolean isImmunity;
    private boolean isBuff;
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
        applyBuff();
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
                this.target.getBuffs().remove(this);
                this.applyBuff();
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

    public void applyBuff() {
        switch(this.type){
            case HP:
                this.target.setMaxHp(this.applyHp(this.target.getCoreStats().getHp()));
                break;
            case DEF:
                this.target.setDef(this.applyreverse(1));
                break;
            case ATTACK:
                this.target.setAttack((int) this.applymult(this.target.getCoreStats().getAttack()));
                break;
            case CRIT:
                this.target.setCritchance((int) this.applyadd(this.target.getCoreStats().getCrit()));
                break;
            case CRITDEF:
                this.target.setCritdef((int) this.applyadd(this.target.getCoreStats().getCritdef()));
                break;
            case CRITDAMAGE:
                this.target.setCritdamage((int) this.applyadd(this.target.getCoreStats().getCridamage()));
                break;
            case EVASION:
                this.target.setEvasion((int) this.applyadd(this.target.getCoreStats().getEvasion()));
                break;
            case ACCURACY:
                this.target.setAccuracy((int) this.applyadd(this.target.getCoreStats().getAccuracy()));
                break;
            case ENERGY:
                this.target.setEnergyrecoveryrate((int) this.applymult(this.target.getCoreStats().getEnergyrecoveryrate()));
                break;
            case HEALING:
                this.target.setHealing(this.applymult(1));
                break;
            case MOVEMENTSPEED:
                this.target.setMovementspeed((int) this.applymult(this.target.getCoreStats().getMovementspeed()));
                break;
            case ATTACKSPEED:
                this.target.setAttackspeed(this.applyAttackspeed(this.target.getCoreStats().getAttackspeed()));
                this.target.calculateRealAttackspeed();
                break;
            default:
                return;
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

    private double applyHp(double basestat){
        double stat = basestat;
        double mult=1;
        ArrayList<Statbuff> buffs = getAllStatBuffs();
        for (Statbuff buff : buffs) {
            if (buff.type == type) {
                if(isBuff){
                    mult = mult + (buff.amount / 100);
                }
                else{
                    mult = mult-(buff.amount/100);
                }
            }
        }
        double newHP = stat*mult;
        return newHP;
    }

    private double applyreverse(double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getAllStatBuffs();
        for (Statbuff buff : buffs) {
            if (buff.type == type) {
                if (isBuff) {
                    stat = stat * ((100 - buff.amount) / 100);
                } else {
                    stat = stat * ((buff.amount / 100) + 1);
                }
            }
        }
        return stat;
    }

    private ArrayList<Statbuff> getAllStatBuffs(){
        ArrayList<Statbuff> buffs = new ArrayList<>();
        for(Buff buff: this.target.getBuffs()){
            if(buff instanceof Statbuff){
                buffs.add((Statbuff) buff);
            }
        }
        return buffs;
    }

    private double applymult(double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getAllStatBuffs();
        for (Statbuff buff : buffs) {
            if (buff.type == type) {
                if (isBuff) {
                    stat = stat * ((buff.amount / 100) + 1);
                } else {
                    stat = stat * ((100 - buff.amount) / 100);
                }
            }
        }
        return stat;
    }

    private double applyadd(double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getAllStatBuffs();
        for (Statbuff buff : buffs) {
            if (buff.type == type) {
                if (isBuff) {
                    stat = stat + buff.amount;
                } else {
                    stat = stat - buff.amount;
                }
            }
        }
        return stat;
    }

    private double applyAttackspeed(double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getAllStatBuffs();
        for (Statbuff buff : buffs) {
            if (buff.type == type) {
                if (isBuff) {
                    stat = stat / ((buff.amount / 100) + 1);
                } else {
                    stat = stat / ((100 - buff.amount) / 100);
                }
            }
        }
        return stat;
    }
}
