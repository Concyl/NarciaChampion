package backend.app.Buffs;

import backend.app.Hero;

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
        System.out.println(this.target.getName()+" receives "+this.amount+"% "+this.type.toString()+" Immunity from "+this.origin.getName());
    }

    private void applyImmunityDeBuff(){
        if(this.target.getImmunities().stream().anyMatch( stat -> stat.type == this.type)){
            return;
        }
        this.target.getNegativestatus().add(this);
        this.target.calculateNegativeEffects();
        System.out.println(this.target.getName()+" receives "+this.amount+"% "+this.type.toString()+" Negative Status from "+this.origin.getName());
    }

    private void buff(){
        if(this.isBuff){
            this.applyBuff();
            System.out.println(this.target.getName()+" receives "+this.amount+"% "+this.type.toString()+" Buff from "+this.origin.getName());
            this.target.getBuffs().add(this);
        }
        else{
            this.applyDeBuff();
            System.out.println(this.target.getName()+" receives "+this.amount+"% "+this.type.toString()+" DeBuff from "+this.origin.getName());
            this.target.getDebuffs().add(this);
        }
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            if(this.isImmunity){
                if(this.isBuff){
                    this.target.getImmunities().remove(this);
                    System.out.println(this.target.getName() + " loses " + this.amount + "% " + this.type.toString() + " Immunity from " + this.origin.getName());
                }
                else{
                    this.target.getNegativestatus().remove(this);
                    this.target.calculateNegativeEffects();
                    System.out.println(this.target.getName() + " loses " + this.amount + "% " + this.type.toString() + " Negative Status from " + this.origin.getName());
                }
            }
            else {
                if (this.isBuff) {
                    this.removeBuff();
                    System.out.println(this.target.getName() + " loses " + this.amount + "% " + this.type.toString() + " Buff from " + this.origin.getName());
                    this.target.getBuffs().remove(this);
                } else {
                    this.removeDeBuff();
                    System.out.println(this.target.getName() + " loses " + this.amount + "% " + this.type.toString() + " DeBuff from " + this.origin.getName());
                    this.target.getDebuffs().remove(this);
                }
            }
        }
    }

    public void removeDeBuff() {
        switch(this.type){
            case HP:
                this.removeDebuffHp();
                break;
            case DEF:
                this.removeDebuffDef();
                break;
            case ATTACK:
                this.removeDebuffAttack();
                break;
            case CRIT:
                this.applyCritchance();
                break;
            case CRITDEF:
                this.applyCritdef();
                break;
            case CRITDAMAGE:
                this.applyCritdamage();
                break;
            case EVASION:
                this.applyEvasion();
                break;
            case ACCURACY:
                this.applyAccuracy();
                break;
            case ENERGY:
                this.removeDebuffEnergy();
                break;
            case HEALING:
                this.removeDebuffHealing();
                break;
            case MOVEMENTSPEED:
                this.removeDebuffMovementSpeed();
                break;
            case ATTACKSPEED:
                this.removeDebuffAttackspeed();
                break;
            default:
                return;
        }
    }

    public void removeBuff(){
        switch(this.type){
            case HP:
                this.removeHp();
                break;
            case DEF:
                this.removeDef();
                break;
            case ATTACK:
                this.removeAttack();
                break;
            case CRIT:
                this.removeCritchance();
                break;
            case CRITDEF:
                this.removeCritdef();
                break;
            case CRITDAMAGE:
                this.removeCritdamage();
                break;
            case EVASION:
                this.removeEvasion();
                break;
            case ACCURACY:
                this.removeAccuracy();
                break;
            case ENERGY:
                this.removeEnergy();
                break;
            case HEALING:
                this.removeHealing();
                break;
            case MOVEMENTSPEED:
                this.removeMovementSpeed();
                break;
            case ATTACKSPEED:
                this.removeAttackspeed();
                break;
            default:
                return;
        }
    }

    public void applyDeBuff() {
        switch(this.type){
            case HP:
                this.applyDebuffHp();
                break;
            case DEF:
                this.applyDebuffDef();
                break;
            case ATTACK:
                this.applyDebuffAttack();
                break;
            case CRIT:
                this.removeCritchance();
                break;
            case CRITDEF:
                this.removeCritdef();
                break;
            case CRITDAMAGE:
                this.removeCritdamage();
                break;
            case EVASION:
                this.removeEvasion();
                break;
            case ACCURACY:
                this.removeAccuracy();
                break;
            case ENERGY:
                this.applyDebuffEnergy();
                break;
            case HEALING:
                this.applyDebuffHealing();
                break;
            case MOVEMENTSPEED:
                this.applyDebuffMovementSpeed();
                break;
            case ATTACKSPEED:
                this.applyDebuffAttackspeed();
                break;
            default:
                return;
        }
    }

    public void applyBuff() {
        switch(this.type){
            case HP:
                this.applyHp();
                break;
            case DEF:
                this.applyDef();
                break;
            case ATTACK:
                this.applyAttack();
                break;
            case CRIT:
                this.applyCritchance();
                break;
            case CRITDEF:
                this.applyCritdef();
                break;
            case CRITDAMAGE:
                this.applyCritdamage();
                break;
            case EVASION:
                this.applyEvasion();
                break;
            case ACCURACY:
                this.applyAccuracy();
                break;
            case ENERGY:
                this.applyEnergy();
                break;
            case HEALING:
                this.applyHealing();
                break;
            case MOVEMENTSPEED:
                this.applyMovementSpeed();
                break;
            case ATTACKSPEED:
                this.applyAttackspeed();
                break;
            default:
                return;
        }
    }

    // TODO
    private void removeHp(){

    }
    private void applyHp(){

    }
    private void applyDebuffHp(){

    }
    private void removeDebuffHp(){

    }

    private void removeDebuffDef(){
        double newdef = this.target.getDef()/((this.amount/100)+1);
        this.target.setDef(newdef);
    }
    private void applyDebuffDef(){
        double newdef = this.target.getDef()*((this.amount/100)+1);
        this.target.setDef(newdef);
    }

    private void removeDef(){
        double newdef = this.target.getDef()/((100-this.amount)/100);
        this.target.setDef(newdef);
    }
    private void applyDef(){
        double newdef = this.target.getDef()*((100-this.amount)/100);
        this.target.setDef(newdef);
    }

    private void applyDebuffAttack(){
        int attack = (int) (this.target.getAttack()*((100-this.amount)/100));
        this.target.setAttack(attack);
    }

    private void removeDebuffAttack(){
        int attack = (int) (this.target.getAttack()/((100-this.amount)/100));
        this.target.setAttack(attack);
    }

    private void removeAttack(){
        int attack = (int) (this.target.getAttack()/((this.amount/100)+1));
        this.target.setAttack(attack);
    }
    private void applyAttack(){
        int attack = (int) (this.target.getAttack()*((this.amount/100)+1));
        this.target.setAttack(attack);
    }
    private void removeCritchance(){
        int crit = (int) (this.target.getCritchance()-this.amount);
        this.target.setCritchance(crit);
    }
    private void applyCritchance(){
        int crit = (int) (this.target.getCritchance()+this.amount);
        this.target.setCritchance(crit);
    }
    private void removeCritdamage(){
        int critdamage = (int) (this.target.getCritdamage()-this.amount);
        this.target.setCritdamage(critdamage);
    }
    private void applyCritdamage(){
        int critdamage = (int) (this.target.getCritdamage()+this.amount);
        this.target.setCritdamage(critdamage);
    }
    private void removeCritdef(){
        int critdef = (int) (this.target.getCritdef()-this.amount);
        this.target.setCritdef(critdef);
    }
    private void applyCritdef(){
        int critdef = (int) (this.target.getCritdef()+this.amount);
        this.target.setCritdef(critdef);
    }
    private void removeAccuracy(){
        int acc = (int) (this.target.getAccuracy()-this.amount);
        this.target.setAccuracy(acc);
    }
    private void applyAccuracy(){
        int acc = (int) (this.target.getAccuracy()+this.amount);
        this.target.setAccuracy(acc);
    }
    private void removeEvasion(){
        int eva = (int) (this.target.getEvasion()-this.amount);
        this.target.setEvasion(eva);
    }
    private void applyEvasion(){
        int eva = (int) (this.target.getEvasion()+this.amount);
        this.target.setEvasion(eva);
    }

    private void applyDebuffHealing(){
        int heal = (int) (this.target.getHealing()*((100-this.amount)/100));
        this.target.setHealing(heal);
    }

    private void removeDebuffHealing(){
        int heal = (int) (this.target.getHealing()/((100-this.amount)/100));
        this.target.setHealing(heal);
    }
    private void removeHealing(){
        int heal = (int) (this.target.getHealing()/((this.amount/100)+1));
        this.target.setHealing(heal);
    }
    private void applyHealing(){
        int heal = (int) (this.target.getHealing()*((this.amount/100)+1));
        this.target.setHealing(heal);
    }
    private void applyDebuffMovementSpeed(){
        int move = (int) (this.target.getMovementspeed()*((100-this.amount)/100));
        this.target.setMovementspeed(move);
    }

    private void removeDebuffMovementSpeed(){
        int move = (int) (this.target.getMovementspeed()/((100-this.amount)/100));
        this.target.setMovementspeed(move);
    }
    private void removeMovementSpeed(){
        int move = (int) (this.target.getMovementspeed()/((this.amount/100)+1));
        this.target.setMovementspeed(move);
    }
    private void applyMovementSpeed(){
        int move = (int) (this.target.getMovementspeed()*((this.amount/100)+1));
        this.target.setMovementspeed(move);
    }
    private void applyDebuffEnergy(){
        int energy = (int) (this.target.getEnergyrecoveryrate()*((100-this.amount)/100));
        this.target.setEnergyrecoveryrate(energy);
    }

    private void removeDebuffEnergy(){
        int energy = (int) (this.target.getEnergyrecoveryrate()/((100-this.amount)/100));
        this.target.setEnergyrecoveryrate(energy);
    }
    private void removeEnergy(){
        int energy = (int) (this.target.getEnergyrecoveryrate()/((this.amount/100)+1));
        this.target.setEnergyrecoveryrate(energy);
    }
    private void applyEnergy(){
        int energy = (int) (this.target.getEnergyrecoveryrate()*((this.amount/100)+1));
        this.target.setEnergyrecoveryrate(energy);
    }
    private void applyDebuffAttackspeed(){
        int energy = (int) (this.target.getAttackspeed()*((100-this.amount)/100));
        this.target.setAttackspeed(energy);
        this.target.calculateRealAttackspeed();
    }

    private void removeDebuffAttackspeed(){
        int energy = (int) (this.target.getAttackspeed()/((100-this.amount)/100));
        this.target.setAttackspeed(energy);
        this.target.calculateRealAttackspeed();
    }
    private void removeAttackspeed(){
        double ats = this.target.getAttackspeed()*((this.amount/100)+1);
        this.target.setAttackspeed(ats);
        this.target.calculateRealAttackspeed();
    }
    private void applyAttackspeed(){
        double ats = this.target.getAttackspeed()/((this.amount/100)+1);
        this.target.setAttackspeed(ats);
        this.target.calculateRealAttackspeed();
    }
}
