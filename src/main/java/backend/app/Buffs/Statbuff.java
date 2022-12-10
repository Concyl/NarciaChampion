package backend.app.Buffs;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Statbuff extends Buff {
    public enum Bufftype{
        ATTACK,HP,DEF,CRIT,CRITDAMAGE,CRITDEF,ACCURACY,EVASION,MOVEMENTSPEED,HEALING,ATTACKSPEED,ENERGY,STUN,FEAR,FROST,SILENCE,DISARM,PETRIFY,ENTANGLE,BLIND,INHIBIT,PARALYZE
    }

    public Bufftype getType() {
        return type;
    }

    private Bufftype type;
    private double amount;
    private boolean isImmunity;
    public Statbuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, boolean isImmunity, int timer, String name,
                    Bufftype type,double amount) {
        super(origin,target,preciseOrigin, isBuff, isRemovable, timer, name);
        this.type = type;
        this.amount = amount;
        this.isImmunity = isImmunity;
    }

    public Statbuff(JSONObject json){
        super(json);
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
        this.target.getImmunities().add(this);
        if(!this.target.getNegativestatus().stream().anyMatch( stat -> stat.type == this.type)){
            return;
        }
        for(int i = 0;i<this.target.getNegativestatus().size();i++){
            if(this.target.getNegativestatus().get(i).type == this.type){
                this.target.getNegativestatus().remove(i);
            }
        }
        this.target.calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" Immunity from "+this.origin.getFullname();
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    private void applyImmunityDeBuff(){
        if(this.target.getImmunities().stream().anyMatch( stat -> stat.type == this.type)){
            return;
        }
        this.target.getNegativestatus().add(this);
        this.target.calculateNegativeEffects();
        String s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" Negative Status from "+this.origin.getFullname();
        this.origin.getBattlefield().getCombatText().addCombatText(s);
    }

    private void buff(){
        if(this.isBuff){
            String s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" Buff from "+this.origin.getFullname();
            this.origin.getBattlefield().getCombatText().addCombatText(s);
            this.target.getBuffs().add(this);
        }
        else{
            if(this.target.getImmunities().stream().anyMatch( stat -> stat.type == this.type)){
                return;
            }
            String s = this.target.getFullname()+" receives "+this.amount+"% "+this.type.toString()+" DeBuff from "+this.origin.getFullname();
            this.origin.getBattlefield().getCombatText().addCombatText(s);
            this.target.getDebuffs().add(this);
        }
        applyBuff();
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            if(this.isImmunity){
                if(this.isBuff){
                    this.target.getImmunities().remove(this);
                    String s =this.target.getFullname() + " loses " + this.amount + "% " + this.type.toString() + " Immunity from " + this.origin.getFullname();
                    this.origin.getBattlefield().getCombatText().addCombatText(s);
                }
                else{
                    this.target.getNegativestatus().remove(this);
                    this.target.calculateNegativeEffects();
                    String s=this.target.getFullname() + " loses " + this.amount + "% " + this.type.toString() + " Negative Status from " + this.origin.getFullname();
                    this.origin.getBattlefield().getCombatText().addCombatText(s);
                }
            }
            else {
                this.target.getBuffs().remove(this);
                this.applyBuff();
                if (this.isBuff) {
                    String s =this.target.getFullname() + " loses " + this.amount + "% " + this.type.toString() + " Buff from " + this.origin.getFullname();
                    this.origin.getBattlefield().getCombatText().addCombatText(s);
                } else {
                    String s =this.target.getFullname() + " loses " + this.amount + "% " + this.type.toString() + " DeBuff from " + this.origin.getFullname();
                    this.origin.getBattlefield().getCombatText().addCombatText(s);
                }
            }
        }
    }

    public void applyBuff() {
        switch(this.type){
            case HP:
                this.target.setMaxHp(this.applyHp(Bufftype.HP,this.target.getCoreStats().getHp()));
                break;
            case DEF:
                this.target.setDef(this.applyreverse(Bufftype.DEF,1));
                break;
            case ATTACK:
                this.target.setAttack((int) this.applymult(Bufftype.ATTACK,this.target.getCoreStats().getAttack()));
                break;
            case CRIT:
                this.target.setCritchance((int) this.applyadd(Bufftype.CRIT,this.target.getCoreStats().getCrit()));
                break;
            case CRITDEF:
                this.target.setCritdef((int) this.applyadd(Bufftype.CRITDEF,this.target.getCoreStats().getCritdef()));
                break;
            case CRITDAMAGE:
                this.target.setCritdamage((int) this.applyadd(Bufftype.CRITDAMAGE,this.target.getCoreStats().getCridamage()));
                break;
            case EVASION:
                this.target.setEvasion((int) this.applyadd(Bufftype.EVASION,this.target.getCoreStats().getEvasion()));
                break;
            case ACCURACY:
                this.target.setAccuracy((int) this.applyadd(Bufftype.ACCURACY,this.target.getCoreStats().getAccuracy()));
                break;
            case ENERGY:
                this.target.setEnergyrecoveryrate((int) this.applymult(Bufftype.ENERGY,this.target.getCoreStats().getEnergyrecoveryrate()));
                break;
            case HEALING:
                this.target.setHealing(this.applymult(Bufftype.HEALING,1));
                break;
            case MOVEMENTSPEED:
                this.target.setMovementspeed((int) this.applymult(Bufftype.MOVEMENTSPEED,this.target.getCoreStats().getMovementspeed()));
                break;
            case ATTACKSPEED:
                this.target.setAttackspeed(this.applyAttackspeed(Bufftype.ATTACKSPEED,this.target.getCoreStats().getAttackspeed()));
                this.target.calculateRealAttackspeed();
                break;
            default:
                return;
        }
    }

    private double applyHp(Bufftype bufftype,double basestat){
        double stat = basestat;
        double mult=1;
        ArrayList<Statbuff> buffs =this.target.getBuffs();
        for(int i = 0;i<buffs.size();i++){
            Statbuff buff = buffs.get(i);
            if(buff.type == type){
                mult = mult+(buff.amount/100);
            }
        }
        ArrayList<Statbuff> debuffs =this.target.getDebuffs();
        for(int i = 0;i<debuffs.size();i++){
            Statbuff buff = debuffs.get(i);
            if(buff.type == type){
                mult = mult-(buff.amount/100);
            }
        }
        return stat*mult;
    }

    private double applyreverse(Bufftype bufftype,double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs =this.target.getBuffs();
        for(int i = 0;i<buffs.size();i++){
            Statbuff buff = buffs.get(i);
            if(buff.type == type){
                stat = stat*((100-buff.amount)/100);
            }
        }
        ArrayList<Statbuff> debuffs =this.target.getDebuffs();
        for(int i = 0;i<debuffs.size();i++){
            Statbuff buff = debuffs.get(i);
            if(buff.type == type){
                stat=stat*((buff.amount/100)+1);
            }
        }
        return stat;
    }

    private double applymult(Bufftype bufftype,double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs =this.target.getBuffs();
        for(int i = 0;i<buffs.size();i++){
            Statbuff buff = buffs.get(i);
            if(buff.type == bufftype){
                stat = stat*((buff.amount/100)+1);
            }
        }
        ArrayList<Statbuff> debuffs =this.target.getDebuffs();
        for(int i = 0;i<debuffs.size();i++){
            Statbuff buff = debuffs.get(i);
            if(buff.type == bufftype){
                stat = stat*((100-buff.amount)/100);
            }
        }
        return stat;
    }

    private double applyadd(Bufftype bufftype,double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs =this.target.getBuffs();
        for(int i = 0;i<buffs.size();i++){
            Statbuff buff = buffs.get(i);
            if(buff.type == bufftype){
                stat = stat+buff.amount;
            }
        }
        ArrayList<Statbuff> debuffs =this.target.getDebuffs();
        for(int i = 0;i<debuffs.size();i++){
            Statbuff buff = debuffs.get(i);
            if(buff.type == bufftype){
                stat = stat-buff.amount;
            }
        }
        return stat;
    }

    private double applyAttackspeed(Bufftype bufftype,double basestat){
        double stat = basestat;
        ArrayList<Statbuff> buffs =this.target.getBuffs();
        for(int i = 0;i<buffs.size();i++){
            Statbuff buff = buffs.get(i);
            if(buff.type == bufftype){
                stat = stat/((buff.amount/100)+1);
            }
        }
        ArrayList<Statbuff> debuffs =this.target.getDebuffs();
        for(int i = 0;i<debuffs.size();i++){
            Statbuff buff = debuffs.get(i);
            if(buff.type == bufftype){
                stat = stat/((100-buff.amount)/100);
            }
        }
        return stat;
    }
}
