package backend.app;

import backend.app.Buffs.SpecialIgnores;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Random;

public class DamageEffect {
    public enum DamageType {
        NORMAL, TRUE, FLAT ,FLATPERCENT
    }


    @Getter private boolean hit = false;
    @Getter private boolean damageHealed = false;
    private DamageType damageType;
    private double multiplier;
    private Hero attacker;
    private Hero receiver;
    private String origin;
    public final ArrayList<SpecialIgnores> specialIgnores = new ArrayList<>();
    private double narciaMultiplicator=1;

    public DamageEffect(Hero attacker, Hero receiver, DamageType type, double multiplier, String origin) {
        this.damageType = type;
        this.attacker = attacker;
        this.receiver = receiver;
        this.multiplier = multiplier;
        this.origin = origin;
    }

    public DamageEffect(Hero attacker, Hero receiver, DamageType type, double multiplier, String origin,ArrayList<SpecialIgnores> ignores) {
        this.damageType = type;
        this.attacker = attacker;
        this.receiver = receiver;
        this.multiplier = multiplier;
        this.origin = origin;
        for(int i =0;i<ignores.size();i++){
            this.specialIgnores.add(ignores.get(i));
        }
    }

    public DamageEffect(Hero attacker, Hero receiver, DamageType type, double multiplier, String origin, SpecialIgnores ignores) {
        this.damageType = type;
        this.attacker = attacker;
        this.receiver = receiver;
        this.multiplier = multiplier;
        this.origin = origin;
        this.specialIgnores.add(ignores);
    }

    public void applyDamage() {
        if(!hitCheck()){
            String s =this.receiver.getFullname()+" dodged "+this.origin+ " from "+this.attacker.getFullname();
            this.attacker.getBattlefield().getCombatText().addCombatText(s);
            return;
        }
        switch (damageType) {
            case NORMAL:
                this.receiveDamage(this.dealNormaldamage());
                break;
            case TRUE:
                this.receiveDamage(this.dealTrueDamage());
                break;
            case FLAT:
                this.receiveDamage(this.dealFlatDamage());
                break;
            case FLATPERCENT:
                this.receiveDamage(this.dealFlatPercentDamage());
            default:
                break;
        }
    }

    private int dealNormaldamage(){
        if(!this.attacker.isAlive()){
            return 0;
        }
        double narciaAttackmult = this.narciaMultiplicator;
        double maxRoll = 1;
        double minRoll = 0.8;
        double damageVariance = (Math.random()*(maxRoll-minRoll))+minRoll;
        int damage = (int)Math.floor(this.attacker.getAttack()*damageVariance*narciaAttackmult*this.multiplier);
        damage = this.wardenAdvantage(damage);
        damage = critcheck(damage);
        String s =(this.attacker.getFullname()+" deals "+ damage+ " "+this.damageType.name()+" Damage to "+this.receiver.getFullname() +" from "+this.origin);
        this.attacker.getBattlefield().getCombatText().addCombatText(s);
        return damage;
    }

    private boolean damageToLpCheck(){
        int damageToLpactive = this.receiver.getDamageToLp();
        if(damageToLpactive == -1){
            return false;
        }
        if(damageToLpactive < 100){
            Random random = new Random();
            if(random.nextInt(100)> damageToLpactive){
                return false;
            }
        }
        return !this.specialIgnores.contains(SpecialIgnores.DAMAGETOLP) && !this.attacker.getPassiveIgnore(SpecialIgnores.DAMAGETOLP);
    }

    private int dealTrueDamage(){
        if(!this.attacker.isAlive()){
            return 0;
        }
        double narciaAttackmult = this.narciaMultiplicator;
        int skilldamage = (int)Math.floor((this.attacker.getCoreStats().getAttack()*this.multiplier*narciaAttackmult));
        int damageAfterCrit = critcheck(skilldamage);
        String s =(this.attacker.getFullname()+" deals "+ damageAfterCrit+ " "+this.damageType.name()+" Damage to "+this.receiver.getFullname() +" from "+this.origin);
        this.attacker.getBattlefield().getCombatText().addCombatText(s);
        return damageAfterCrit;
    }

    private int dealFlatPercentDamage(){
        if(!this.attacker.isAlive()){
            return 0;
        }
        double narciaAttackmult = this.narciaMultiplicator;
        int skilldamage = (int)Math.floor((this.receiver.getMaxHp()*(this.multiplier/100)*narciaAttackmult));
        String s =(this.attacker.getFullname()+" deals "+ skilldamage+ " "+this.damageType.name()+" Damage to "+this.receiver.getFullname() +" from "+this.origin);
        this.attacker.getBattlefield().getCombatText().addCombatText(s);
        return skilldamage;
    }

    private int dealFlatDamage(){
        if(!this.attacker.isAlive()){
            return 0;
        }
        double narciaAttackmult = this.narciaMultiplicator;
        int skilldamage = (int)Math.floor(this.multiplier*narciaAttackmult);
        String s =(this.attacker.getFullname()+" deals "+ skilldamage+ " "+this.damageType.name()+" Damage to "+this.receiver.getFullname() +" from "+this.origin);
        this.attacker.getBattlefield().getCombatText().addCombatText(s);
        return skilldamage;
    }

    private void receiveDamage(int damage){
        if(!this.attacker.isAlive() || !this.receiver.isAlive()){
            return;
        }
        if(this.damageToLpCheck()){
            this.receiver.heal(damage,"Damage To Lp");
            this.damageHealed = true;
            return;
        }
        if(this.receiver.isNoDamageweak() && (!this.specialIgnores.contains(SpecialIgnores.NODAMAGEWEAK) || !this.attacker.getPassiveIgnore(SpecialIgnores.NODAMAGEWEAK))){
            damage = 0;
        }
        if(this.receiver.isNoDamagestrong()){
            damage = 0;
        }
        if(this.receiver.isStealth()){
            return;
        }
        int oldHp = (int)this.receiver.getCurrentHp();
        if( !this.specialIgnores.contains(SpecialIgnores.DAMAGEREDUCTION) && !this.attacker.getPassiveIgnore(SpecialIgnores.DAMAGEREDUCTION) && this.damageType==DamageType.NORMAL){
            damage = damageReduction(damage);
        }
        if(this.receiver.getDamageCap() > 0){
            damage = this.damageCap(damage,this.attacker,this.receiver);
        }
        damage = this.reflect(damage);
        this.receiver.setCurrentHp(oldHp-damage);
        int percentHpBefore = (int)Math.floor((oldHp/this.receiver.getMaxHp())*100);
        int percentNow = (int)Math.floor((this.receiver.getCurrentHp()/this.receiver.getMaxHp())*100);
        String combattext = this.receiver.getFullname() + " receives " + damage +" "+this.damageType.name()+" Damage from "+ this.origin+", HP reduced from "+ oldHp+ " ("+percentHpBefore+"%) to "+(int)receiver.getCurrentHp()+ " ("+percentNow+"%)";
        this.attacker.getBattlefield().getCombatText().addCombatText(combattext);
        this.receiver.postDamage();
    }

    private void receiveReflectDamage(int damage){
        if(!this.attacker.isAlive()){
            return;
        }
        int oldHp = (int)attacker.getCurrentHp();
        int damageAfterCaps = this.damageCap(damage,this.receiver,this.attacker);
        int damageAfterReflectCap = this.damageReflectCap(damageAfterCaps);
        this.attacker.setCurrentHp(oldHp-damageAfterReflectCap);
        int percentHpBefore = (int)Math.floor((oldHp/this.attacker.getMaxHp())*100);
        int percentNow = (int)Math.floor((this.attacker.getCurrentHp()/this.attacker.getMaxHp())*100);
        String combattext = this.attacker.getFullname() + " receives " + damageAfterReflectCap +" Reflected Damage from "+ this.origin+", HP reduced from "+ oldHp+ " ("+percentHpBefore+"%) to "+(int)attacker.getCurrentHp()+ " ("+percentNow+"%)";
        this.attacker.getBattlefield().getCombatText().addCombatText(combattext);
        this.attacker.deathCheck();
    }

    private int reflect(int damage){
        int reflectTalent = 0;
        if(!this.receiver.isSilenced()){
            reflectTalent = this.receiver.getReflectTalent();
        }
        int reflectOther = this.receiver.getReflect();
        int reflect = Math.max(reflectOther, reflectTalent);
        double reflectDamage = damage;
        if(reflect != 0 && !this.specialIgnores.contains(SpecialIgnores.IGNOREREFLECT) && !this.attacker.getPassiveIgnore(SpecialIgnores.IGNOREREFLECT)) {
            reflectDamage = (double)damage * (100 - reflect) / 100;
            if (!this.specialIgnores.contains(SpecialIgnores.NOREFLECTEDDAMAGE) && !this.attacker.getPassiveIgnore(SpecialIgnores.NOREFLECTEDDAMAGE)) {
                int reflectedDamage = (int) (damage - reflectDamage);
                this.receiveReflectDamage(reflectedDamage);
            }
        }
        else if(reflect == 0 && this.receiver.getPassiveIgnore(SpecialIgnores.REFLECTCAP)){
            double random = Math.random();
            if(random < 0.6){
                reflectDamage = 0;
                String combattext = "60% Chance Spikeshield triggert";
                this.attacker.getBattlefield().getCombatText().addCombatText(combattext);
                if (!this.specialIgnores.contains(SpecialIgnores.NOREFLECTEDDAMAGE) && !this.attacker.getPassiveIgnore(SpecialIgnores.NOREFLECTEDDAMAGE)) {
                    this.receiveReflectDamage(damage);
                }
            }
        }
        return (int) reflectDamage;
    }

    private int damageReflectCap(int damage){
        int reflectCap = this.attacker.getDamageReflectCap();
        if(reflectCap>0 && reflectCap<damage && !this.receiver.getPassiveIgnore(SpecialIgnores.REFLECTCAP)){
            damage = reflectCap;
        }
        return damage;
    }

    private int damageCap(int damage,Hero attacker, Hero defender){
        if(!this.specialIgnores.contains(SpecialIgnores.DAMAGECAP) && !attacker.getPassiveIgnore(SpecialIgnores.DAMAGECAP)){
            int damagecap = defender.getDamageCap();
            if(damagecap<damage && damagecap>0){
                damage = damagecap;
            }
        }
        return damage;
    }

    private int wardenAdvantage(int damage){
        Hero.Fraction attackfraction = this.attacker.getFraction();
        Hero.Fraction deffraction = this.receiver.getFraction();
        if(attackfraction == deffraction){
            return damage;
        }
        else if(attackfraction == Hero.Fraction.ORDERBOUND || attackfraction == Hero.Fraction.VOIDWALKER){
           return getFractionAttackMult(damage);
        }
        else if(attackfraction == Hero.Fraction.SAINT && deffraction == Hero.Fraction.BRAWLER
                || attackfraction == Hero.Fraction.ORACLE && deffraction == Hero.Fraction.SAINT
                || attackfraction == Hero.Fraction.BRAWLER && deffraction == Hero.Fraction.ORACLE){
            return getFractionAttackMult(damage);
        }
        return damage;
    }

    private int wardenDef(int damage){
        Hero.Fraction attackfraction = this.attacker.getFraction();
        Hero.Fraction deffraction = this.receiver.getFraction();
        if(attackfraction == deffraction){
            return damage;
        }
        else if(deffraction == Hero.Fraction.ORDERBOUND || deffraction == Hero.Fraction.VOIDWALKER){
            return getFractionDefMult(damage);
        }
        else if(attackfraction == Hero.Fraction.SAINT && deffraction == Hero.Fraction.ORACLE
                || attackfraction == Hero.Fraction.ORACLE && deffraction == Hero.Fraction.BRAWLER
                || attackfraction == Hero.Fraction.BRAWLER && deffraction == Hero.Fraction.SAINT){
            return getFractionDefMult(damage);
        }
        return damage;
    }

    private int getFractionDefMult(int damage){
        damage = (int)(damage*0.15);
        return damage;
    }

    private int getFractionAttackMult(int damage){
        damage = (int)(damage*2.96);
        return damage;
    }

    private int damageReduction(int damage){
        int damageAfterDef = (int) (damage*receiver.getDef());
        int damageAfterWarden = this.wardenDef(damageAfterDef);
        return damageAfterWarden ;
    }

    private int critcheck(int damage){
        double critchance = this.attacker.getCritchance();
        double critdef = this.receiver.getCritdef();
        if(critdef>critchance){
            return damage;
        }
        double critical = (((critchance/(critchance+1000))*10000)-((critdef/(critdef+1000))*10000))*0.1;
        double rnd = Math.random()*1000;
        if(critical>=rnd){
            double critdamage = this.attacker.getCritdamage();
            damage *= 1.5+(critdamage/10000);
            double critpercentage = Math.floor(critical)/10;
            String combattext = this.attacker.getFullname()+" crits, with "+ critpercentage+"% critchance";
            this.attacker.getBattlefield().getCombatText().addCombatText(combattext);
        }
        return damage;
    }

    private boolean hitCheck(){
        if(this.specialIgnores.contains(SpecialIgnores.CANTMISS) || attacker.getPassiveIgnore(SpecialIgnores.CANTMISS)){
            this.hit = true;
            return true;
        }
        int hitcap = 10000;
        if(this.receiver.getEvasion()< this.attacker.getAccuracy()){
            this.hit = true;
            return true;
        }
        int hitpluscap = this.attacker.getAccuracy()+hitcap;
        if(this.receiver.getEvasion()>hitpluscap){
            return false;
        }
        int randlower = this.receiver.getEvasion()- this.attacker.getAccuracy();
        double rand = Math.floor(Math.random()*hitcap);
        if(rand>=randlower){
            this.hit = true;
            return true;
        }
        return false;
    }
}
