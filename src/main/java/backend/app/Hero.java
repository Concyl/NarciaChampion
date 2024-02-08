package backend.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

import backend.app.Buffs.*;
import backend.app.Pets.Pet;
import backend.app.SpecialAbilities.*;
import backend.app.SpecialAbilities.Abilities.Revive;
import backend.app.Targets.AutoAttackTarget;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

public abstract class Hero {



    public enum Team{
        BLUE,RED
    }

    public enum Fraction{
        ORACLE,SAINT,BRAWLER,ORDERBOUND,VOIDWALKER,NONE
    }

    private Warden warden;
    protected Skill skill;
    @Getter @Setter private boolean revived = false;
    @Getter @Setter private boolean inProcessOfRevive = false;
    @Getter @Setter private int reviveCount = 0;
    @Getter @Setter private AutoAttackTarget autoAttackTarget = new AutoAttackTarget(this);
    @Getter @Setter private Hero target = null;
    private boolean inAttackRange = false;
    private boolean isFlying;
    @Getter @Setter private String name;
    @Getter @Setter private String fullname;
    @Getter @Setter private Fraction fraction;
    @Getter @Setter private Team team;
    @Getter @Setter private boolean alive = true;
    private int id;
    private boolean isLegendary;
    @Getter @Setter private Battlefield battlefield;
    @Getter @Setter private int xCoordinate;
    @Getter @Setter private int yCoordinate;
    @Getter @Setter private int movementspeed;
    private boolean canMove = true;
    private boolean canActivateSkill = true;
    @Getter private boolean silenced = false;
    // Stats
    @Getter @Setter private double currentHp;
    @Getter @Setter private double maxHp;
    @Getter @Setter private int attack;
    @Getter @Setter private int accuracy;
    @Getter @Setter private int evasion;
    @Getter @Setter private int critchance;
    @Getter @Setter private int critdamage;
    @Getter @Setter private int critdef;
    @Getter @Setter private double def=1;
    @Getter @Setter private double healing=1;

    @Getter @Setter private int damageCap=-1;
    @Getter @Setter private int damageReflectCap=-1;
    @Setter private int reflect =0;
    @Getter @Setter private int reflectTalent=0;
    @Getter @Setter private boolean stealth = false;
    @Getter @Setter private boolean confusion = false;
    @Getter @Setter private boolean isBlind=false;
    @Getter @Setter private boolean immuneAll=false;
    @Getter @Setter private int damageToLp = -1;
    @Getter @Setter private boolean noDamageweak = false;
    @Getter @Setter private boolean noDamagestrong = false;

    @Getter @Setter private Pet pet;

    @Getter @Setter private int autoattackrange;
    @Getter @Setter private double attackspeed;
    @Getter @Setter private double realattackspeed;
    private boolean canAutoAttack = true;
    private double autoattackcooldown;

    @Getter private final ArrayList<Statbuff> buffs = new ArrayList<>();
    @Getter private final ArrayList<Immunity> immunities = new ArrayList<>();
    @Getter private final ArrayList<Impairment> impairments = new ArrayList<>();
    @Getter private final ArrayList<SpecialBuff> specialBuffs = new ArrayList<>();
    @Getter private final ArrayList<SpecialBuff> passiveIgnores = new ArrayList<>();


    @Getter private final ArrayList<Talent> talents = new ArrayList<>();
    @Getter private final ArrayList<SpecialAbility> specialAbilities = new ArrayList<>();
    @Getter private final ArrayList<TimeBasedSpecialAbility> timespecialAbilities = new ArrayList<>();


    // Skill and Energy
    @Getter @Setter private int energy=0;
    private int maxEnergy = 100;
    @Getter @Setter private int energyrecoveryrate;
    private boolean canReceiveEnergy= true;
    private boolean isAutoproc;
    @Getter @Setter private BasicStats coreStats;
    
    public Hero(JSONObject heroJSON) {

        this.id = Math.toIntExact((Long) heroJSON.get("id"));
        this.name = (String) heroJSON.get("name");
        this.fraction = (Fraction.valueOf( (String) heroJSON.get("fraction")));

        this.attack = Math.toIntExact((Long) heroJSON.get("attack"));
        this.maxHp = Math.toIntExact((Long) heroJSON.get("hp"));
        this.currentHp = maxHp;
        this.accuracy = Math.toIntExact((Long) heroJSON.get("accuracy"));
        this.evasion = Math.toIntExact((Long) heroJSON.get("evasion"));
        this.critchance = Math.toIntExact((Long) heroJSON.get("critchance"));
        this.critdamage = Math.toIntExact((Long) heroJSON.get("critdamage"));
        this.critdef = Math.toIntExact((Long) heroJSON.get("critdef"));
        this.critdamage = Math.toIntExact((Long) heroJSON.get("critdamage"));

        this.autoattackrange = Math.toIntExact((Long) heroJSON.get("autoattackrange"));
        this.movementspeed = Math.toIntExact((Long) heroJSON.get("movementspeed"));
        this.attackspeed = Math.toIntExact((Long) heroJSON.get("attackspeed"));
        this.energyrecoveryrate = Math.toIntExact((Long) heroJSON.get("energyrecoveryrate"));

        this.isAutoproc = (Boolean) heroJSON.get("isAutoproc");
        this.isFlying = (Boolean) heroJSON.get("isFlying");
        this.isLegendary = (Boolean) heroJSON.get("isLegendary");

        this.coreStats = new BasicStats(this.attack,this.maxHp,this.accuracy,this.evasion,this.critchance,this.critdamage,this.critdef,this.energyrecoveryrate,this.movementspeed,this.attackspeed,"Core Stats");
        this.calculateRealAttackspeed();
    }

    public int getReflect(){
        boolean flag = false;
        int curReflect = this.reflect;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.REFLECT && buff.getStacks() > 0){
                buff.decreaseStacks();
                flag = true;
            }
        }
        if(flag){
            updateReflect();
        }
        return curReflect;
    }

    public void decreaseRevives(){
        this.reviveCount--;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.REVIVE && buff.getStacks() > 0){
                buff.decreaseStacks();
            }
        }
    }
    //TODO
    public void removeAllDebuffs(){
        this.buffs.removeIf(x -> (!x.isBuff() && x.isRemovable() && !x.isFromTalent()));
        this.impairments.removeIf(x -> (x.isRemovable() && !x.isFromTalent()));
    }

    public void addSpecialAbility(SpecialAbility specialAbility){
        this.getSpecialAbilities().add(specialAbility);
        if(specialAbility instanceof TimeBasedSpecialAbility){
            this.getTimespecialAbilities().add((TimeBasedSpecialAbility) specialAbility);
        }
    }

    public void updateRevive(){
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.REVIVE && buff.getStacks()> this.reviveCount){
                this.reviveCount = buff.getStacks();
            }
        }
    }

    public void updateNoDamageWeak() {
            this.noDamageweak = this.specialBuffs.stream().anyMatch(x -> x.getType() == SpecialIgnores.NODAMAGEWEAK);
    }

    public void updateNoDamageStrong(){
        this.noDamagestrong = this.specialBuffs.stream().anyMatch(x -> x.getType() == SpecialIgnores.NODAMAGESTRONG);
    }

    public void updateImmuneAll(){
        if(this.specialBuffs.stream().noneMatch(x -> x.getType() == SpecialIgnores.ALL)){
            this.immuneAll = false;
        }
        else {
            this.immuneAll = true;
            removeAllDebuffs();
        }
    }

    public void updateStealth(){
        if(this.specialBuffs.stream().noneMatch(x -> x.getType() == SpecialIgnores.STEALTH)){
            this.stealth = false;
        }
        else {
            this.stealth = true;
        }
    }

    public void updateReflect(){
        int reflect = 0;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.REFLECT && buff.getStacks()!= 0){
                reflect = reflect + buff.getValue();
            }
        }
        if(reflect > 100){
            reflect = 100;
        }
        this.reflect = reflect;
    }

    public void updateReflectCap(){
        int lowestReflectCap = Integer.MAX_VALUE;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType()==SpecialIgnores.REFLECTCAP && buff.getValue() < lowestReflectCap){
                lowestReflectCap = buff.getValue();
            }
        }
        this.damageReflectCap = lowestReflectCap == Integer.MAX_VALUE ? -1 : lowestReflectCap;
    }

    public void updateReflectTalent(){
        int reflect = 0;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.REFLECTTALENT && buff.getStacks() != 0){
                reflect = reflect + buff.getValue();
            }
        }
        this.reflectTalent = reflect;
    }

    public void updateDamageToLp(){
        int chance = -1;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.DAMAGETOLP && buff.getChance() > chance){
                chance = buff.getChance();
            }
        }
        this.damageToLp = chance;
    }

    public void updateDamageCap(){
        int lowestDamageCap = Integer.MAX_VALUE;
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType()==SpecialIgnores.DAMAGECAP && buff.getValue() < lowestDamageCap){
                lowestDamageCap = buff.getValue();
            }
        }
       this.damageCap = lowestDamageCap == Integer.MAX_VALUE ? -1 : lowestDamageCap;
    }

    public void addBuff(Buff buff){
        if(buff instanceof Statbuff){
            this.buffs.add((Statbuff) buff);
        }
        else if(buff instanceof Immunity){
            this.immunities.add((Immunity) buff);
        }
        else if(buff instanceof Impairment){
            this.impairments.add((Impairment)buff);
        }
        else if(buff instanceof SpecialBuff){
            if(((SpecialBuff) buff).isIgnore()){
                this.passiveIgnores.add((SpecialBuff)buff);
            }
            else{
                this.specialBuffs.add((SpecialBuff)buff);
            }
        }
    }

    public void calculateNegativeEffects(){
        this.resetNegativeEffets();
        for(Impairment imp : this.getImpairments()){
            switch(imp.getType()){
                case FEAR:
                case STUN:
                case FROST:
                case ENTANGLE:
                case PETRIFY:
                    this.applyStuns();
                    break;
                case SILENCE:
                case INHIBIT:
                    this.applySilence();
                    this.cantReceiveEnergy();
                    break;
                case DISARM:
                    this.cantAutoAttack();
                    break;
                case BLIND:
                    this.isBlinded();
                    break;
                case PARALYZE:
                    this.applyStuns();
                    this.cantReceiveEnergy();
                    break;
                    // TODO
                    // KEINE HEILUNG
                case CONFUSION:
                    this.applyConfusion();
                    break;
            }
        }
    }

    public void addPassiveIgnore(SpecialBuff buff){
        this.passiveIgnores.add(buff);
    }

    private int distanceToTarget(){
        if(this.target != null){
            int xdistance = Math.abs(this.target.getXCoordinate()-this.xCoordinate);
            int ydistance = Math.abs(this.target.getYCoordinate()-this.yCoordinate);
            int distance = (int) Math.ceil(Math.sqrt(xdistance*xdistance+ydistance*ydistance));
            return distance;
        }
        else {
            return -1;
        }
    }

    public void move(){
        if(!isAlive()){
            return;
        }
        if(this.target == null || this.target.isStealth()){
            this.searchForTarget();
        }
        if(this.target == null){
            return;
        }
        int distance = this.distanceToTarget();
        if(distance == -1){
            this.inAttackRange = false;
            return;
        }
        if(this.autoattackrange < distance){
            this.inAttackRange = false;
            if(!this.canMove){
                return;
            }
            int oldX = this.xCoordinate;
            int oldY = this.yCoordinate;
            int distanceHerocanRunInFrame = (int)Math.floor(this.movementspeed/30);
            if(distanceHerocanRunInFrame > distance*2){
                distanceHerocanRunInFrame = distance;
            }
            double xdistance = Math.abs(this.target.getXCoordinate() - this.xCoordinate);
            double ydistance = Math.abs(this.target.getYCoordinate() - this.yCoordinate);
            double xpercent = xdistance/(xdistance+ydistance);
            if(xpercent >= 0.5){
                if(this.xCoordinate>this.target.getXCoordinate()){
                    this.xCoordinate -= Math.ceil(distanceHerocanRunInFrame*xpercent);
                }
                else{
                    this.xCoordinate += Math.ceil(distanceHerocanRunInFrame*xpercent);
                }
                if(this.yCoordinate>this.target.getYCoordinate()){
                    this.yCoordinate -= Math.floor(distanceHerocanRunInFrame*(1-xpercent));
                }
                else{
                    this.yCoordinate += Math.floor(distanceHerocanRunInFrame*(1-xpercent));
                }
            }
            else{
                if(this.xCoordinate>this.target.getXCoordinate()){
                    this.xCoordinate -= Math.floor(distanceHerocanRunInFrame*xpercent);
                }
                else{
                    this.xCoordinate += Math.floor(distanceHerocanRunInFrame*xpercent);
                }
                if(this.yCoordinate>this.target.getYCoordinate()){
                    this.yCoordinate -= Math.ceil(distanceHerocanRunInFrame*(1-xpercent));
                }
                else{
                    this.yCoordinate += Math.ceil(distanceHerocanRunInFrame*(1-xpercent));
                }
            }
            String s =(this.fullname+" moved from ("+oldX+"|"+oldY+") to ("+this.xCoordinate+"|"+this.yCoordinate+")");
            this.getBattlefield().getCombatText().addCombatText(s);
        }
        else{
            this.inAttackRange = true;
        }
    }

    private void searchForTarget(){
        if(!this.isAlive()){
            return;
        }
        ArrayList<Hero> target = this.autoAttackTarget.getTarget();
        if(target.size() != 0){
            this.target = target.get(0);
        }
        else{
            this.target = null;
        }
    }

    public ArrayList<Hero> getEnemyTeam(){
        if(this.team == Team.BLUE){
            return battlefield.activeredHeroes;
        }
        else {
            return battlefield.activeblueHeroes;
        }
    }

    public ArrayList<Hero> getAlliesTeam(){
        if(this.team == Team.BLUE){
            return battlefield.activeblueHeroes;
        }
        else {
            return battlefield.activeredHeroes;
        }
    }

    public void reduceCooldowns(){
        reduceAutoAttackCooldown();
        reduceBuffCooldowns();
        reduceImmunityCooldowns();
        reduceImpairmentCooldowns();
        reduceSpecialBuffCooldowns();
        for(int i = 0;i<this.getSpecialAbilities().size();i++){
            this.getSpecialAbilities().get(i).update();
        }
        this.skill.update();
    }

    private void reduceSpecialBuffCooldowns(){
        ArrayList<SpecialIgnores> removedTypes = new ArrayList<>();
        for(SpecialBuff buff : this.specialBuffs){
            buff.update();
            if(buff.isExpired() || buff.getStacks() == 0){
                removedTypes.add(buff.getType());
            }
        }
        if(removedTypes.size() == 0){
            return;
        }
        this.getSpecialBuffs().removeIf(Buff::isExpired);
        for(SpecialIgnores ignore : removedTypes){
            ignore.updateSpecialIgnore(this);
        }
    }

    private void reduceAutoAttackCooldown(){
        if(this.autoattackcooldown > 0){
            this.autoattackcooldown--;
        }
    }

    private void reduceImpairmentCooldowns(){
        boolean changed = false;
        for(Impairment buff : this.getImpairments()){
            buff.update();
            if(buff.isExpired()){
                changed = true;
            }
        }
        if(changed){
            this.getImpairments().removeIf(Buff::isExpired);
            this.calculateNegativeEffects();
        }
    }

    private void reduceImmunityCooldowns(){
        for(Immunity buff : this.getImmunities()){
            buff.update();
        }
        this.getImmunities().removeIf(Buff::isExpired);
    }

    private void reduceBuffCooldowns(){
        ArrayList<Bufftype> removedTypes = new ArrayList<>();
        for (Statbuff buff : this.getBuffs()) {
            buff.update();
            if(buff.isExpired()){
                removedTypes.add(buff.getType());
            }
        }
        if(removedTypes.size() == 0){
            return;
        }
        this.getBuffs().removeIf(Buff::isExpired);
        removedTypes = new ArrayList<>(new LinkedHashSet<>(removedTypes));
        for( Bufftype type : removedTypes){
            type.recalculateStat(this);
        }
    }

    public void autoattack(){
        if(this.inAttackRange && this.isAlive() && this.autoattackcooldown == 0 && this.canAutoAttack && this.target != null){
            if(!this.isAutoproc){
                this.activateSkill();
            }
            if(this.target == null || !this.target.isAlive() || !this.isAlive()){
                return;
            }
            this.setAttackSpeedCd();
            if(!isBlind){
                DamageEffect damage = new DamageEffect(this,this.target, DamageEffect.DamageType.NORMAL,1, "Auto Attack");
                damage.applyDamage();
                if(damage.isHit() && !damage.isDamageHealed()){
                    this.addAutoEnergy();
                }
            }
        }
    }

    public void postDamage(){
        this.deathCheck();
        for(int i = 0 ; i<this.getSpecialAbilities().size();i++){
            if(this.getSpecialAbilities().get(i) instanceof OnHitSpecialAbility){
                ((OnHitSpecialAbility) this.getSpecialAbilities().get(i)).trigger();
            }
        }
        this.addAutoEnergy();
    }

    public void triggerSpecialAbility(TriggerBasedSpecialAbility.Trigger trigger){
        for(int i = 0 ; i<this.getSpecialAbilities().size();i++){
            SpecialAbility ability = this.getSpecialAbilities().get(i);
            if(ability instanceof TriggerBasedSpecialAbility){
                if(((TriggerBasedSpecialAbility) ability).getTrigger() == trigger){
                    ((TriggerBasedSpecialAbility) ability).trigger();
                }
            }
        }
    }

    public void addAutoEnergy(){
        if(this.energy<this.maxEnergy && this.isAlive() && this.canReceiveEnergy){
            int oldenergy = this.energy;
            this.energy += energyrecoveryrate;
            if(this.energy>this.maxEnergy){
                this.energy = this.maxEnergy;
            }
            String s =(this.getFullname()+" receives "+this.energyrecoveryrate+" energy, previous energy = "+oldenergy+", current Energy = " + this.energy);
            this.getBattlefield().getCombatText().addCombatText(s);
        }
       // this.warden.addEnergy();
    }

    public void deathCheck(){
        if(this.currentHp <= 0){
            this.death();
        }
    }

    private void death(){
        String death = this.getFullname()+ " dies";
        this.getBattlefield().getCombatText().addCombatText(death);
        for(int i = 0 ; i<this.getSpecialAbilities().size();i++){
            if(this.getSpecialAbilities().get(i) instanceof OnDeathSpecialAbility){
                ((OnDeathSpecialAbility) this.getSpecialAbilities().get(i)).trigger();
            }
        }
        this.resetHero();
        if(this.reviveCount > 0){
            this.decreaseRevives();
            this.inProcessOfRevive = true;
            this.createReviveObject();
        }
    }

    public void createReviveObject(){
        int amount = 0;
        String buffname = "";
        for(SpecialBuff buff: this.specialBuffs){
            if(buff.getType() == SpecialIgnores.REVIVE){
                if( buff.getValue() > amount){
                    amount = buff.getValue();
                    buffname = buff.getName();
                }
            }
        }
        int timedead = (int) (this.autoattackcooldown+6);
        TimeBasedSpecialAbility rev = new TimeBasedSpecialAbility(timedead,timedead,false,"Revive",this,this,buffname,false);
        rev.setFromTalent(true);
        Ability revAbility = new Revive(amount);
        rev.setAbility(revAbility);
        rev.setCooldownTimer(timedead-1);
        this.addSpecialAbility(rev);
    }

    public void revive(int amount,String name){
        this.currentHp = this.maxHp*(100-(100-amount))/100;
        this.inProcessOfRevive = false;
        this.setAlive(true);
        String death = this.getFullname()+ " revives by "+name +" with "+amount+"% HP";
        this.getBattlefield().getCombatText().addCombatText(death);
        triggerSpecialAbility(TriggerBasedSpecialAbility.Trigger.ONREVIVE);
    }

    private void resetHero(){
        this.setAlive(false);
        this.target = null;
        this.currentHp = 0;
        this.energy = 0;
        this.hardResetBuffs();
    }

    private void hardResetBuffs(){
        this.buffs.removeIf(x -> !x.isFromTalent());
        this.impairments.removeIf((x -> !x.isFromTalent()));
        this.immunities.removeIf((x -> !x.isFromTalent()));
        this.passiveIgnores.removeIf((x -> !x.isFromTalent()));
        this.specialBuffs.removeIf((x -> !x.isFromTalent()));
        this.specialAbilities.removeIf(x -> !x.isFromTalent());
        this.timespecialAbilities.removeIf(x -> !x.isFromTalent());
        this.calculateNegativeEffects();
        Bufftype [] bufftypes = Bufftype.class.getEnumConstants();
        for(Bufftype buff: bufftypes){
            buff.recalculateStat(this);
        }
    }
    private void setAttackSpeedCd(){
         this.autoattackcooldown = this.realattackspeed/200*6;
    }

    public void calculateRealAttackspeed(){
        double atsmodulu = this.attackspeed%200;
        if(atsmodulu != 0){
            atsmodulu = 200-atsmodulu;
        }
       this.realattackspeed = this.attackspeed+atsmodulu;
    }

    private void activateSkill(){
        if(this.energy != this.maxEnergy || !this.isAlive() || !this.canActivateSkill){
            return;
        }
        this.skill.activateSkill(this);
    }

    public void activateAutoProcSkill(){
        if(this.isAutoproc && isAlive()){
            activateSkill();
        }
    }

    protected abstract void useSkill();

    protected abstract void init();

    public void initTalents(){
        for(int i = 0; i<this.getTalents().size();i++){
            Talent talent = this.getTalents().get(i);
            for(int j=0;j<talent.getSpecialAbilities().size();j++){
                SpecialAbility specialAbility = talent.getSpecialAbilities().get(j);
                specialAbility.init(this,this);
                this.specialAbilities.add(specialAbility);
            }
        }
    }

    public ArrayList<Hero> alliesInRange(int radius, int xpos, int ypos){
        return HeroesinRange(radius,xpos,ypos,this.getAlliesTeam());
    }
    public ArrayList<Hero> enemiesInRange(int radius, int xpos, int ypos){
        return HeroesinRange(radius,xpos,ypos,this.getEnemyTeam());
    }

    public boolean getPassiveIgnore(SpecialIgnores ignore){
        return this.passiveIgnores.stream().anyMatch(x -> x.getType() == ignore);
    }

    public void resetNegativeEffets(){
        this.canMove = true;
        this.canAutoAttack = true;
        this.canActivateSkill = true;
        this.silenced = false;
        this.canReceiveEnergy = true;
        this.confusion = false;
    }

    public void applySilence(){
        this.canActivateSkill = false;
        this.silenced = true;
    }

    public void applyStuns(){
        this.canMove = false;
        this.canAutoAttack = false;
        this.canActivateSkill = false;
    }

    public void applyConfusion(){
        this.confusion = true;
    }

    public void cantReceiveEnergy(){
        this.canReceiveEnergy = false;
    }

    public void cantAutoAttack(){
        this.canAutoAttack = false;
    }

    public void isBlinded(){
        this.isBlind = true;
    }

    public void heal(double amount,String text){
        double oldHp = this.currentHp;
        double realHeal = amount*this.healing;
        if(this.currentHp+realHeal>this.maxHp){
            this.currentHp = this.maxHp;
        }
        else {
            this.currentHp += realHeal;
        }
        int oldHppercent = (int) ((oldHp/this.maxHp)*100);
        int newHppercent = (int) ((this.currentHp/this.maxHp)*100);
        String combattext = this.fullname+" heals by "+(int)amount+", Hp increased from "+(int)oldHp+" ("+oldHppercent+"%) to "+(int)this.currentHp+" ("+newHppercent+"%) by "+text;
        this.getBattlefield().getCombatText().addCombatText(combattext);
    }

    private ArrayList<Hero>HeroesinRange(int radius, int xpos, int ypos,ArrayList<Hero> heroes){
        ArrayList<Hero> aliveheroes = filterDeadHeroes(heroes);
        ArrayList<Hero> inRangeHeroes = new ArrayList<>();
        for (Hero alivehero : aliveheroes) {
            double xdistance = Math.abs(xpos - alivehero.getXCoordinate());
            double ydistance = Math.abs(ypos - alivehero.getYCoordinate());
            double distance = Math.sqrt((xdistance * xdistance) + (ydistance * ydistance));
            if (distance <= radius) {
                inRangeHeroes.add(alivehero);
            }
        }
        return shuffle(aliveheroes);
    }

    private ArrayList<Hero> filterDeadHeroes(ArrayList<Hero> heroes){
        ArrayList<Hero> aliveheroes = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                aliveheroes.add(hero);
            }
        }
        return aliveheroes;
    }
    private ArrayList<Hero> shuffle(ArrayList<Hero> heroes){
        ArrayList<Hero> shuffleheroes = new ArrayList<>();
        shuffleheroes.addAll(heroes);
        Collections.shuffle(heroes);
        return shuffleheroes;
    }

    public ArrayList<Hero> getAmountofAliveHeroes(int count, ArrayList<Hero> heroes){
        ArrayList<Hero> aliveHeroes = shuffle(filterDeadHeroes(heroes));
        if(aliveHeroes.size()<count){
            count = aliveHeroes.size();
        }
        ArrayList<Hero> filtered = new ArrayList<>();
        for(int i = 0;i<count;i++){
            filtered.add(aliveHeroes.get(i));
        }
        return filtered;
    }


    public double applyHp(double basestat,Bufftype type){
        double mult=1;
        ArrayList<Statbuff> buffs = getBuffs();
        for (Statbuff buff : buffs) {
            if (buff.getType() == type) {
                if(buff.isBuff()){
                    mult = mult + (buff.getAmount() / 100);
                }
                else{
                    mult = mult-(buff.getAmount()/100);
                }
            }
        }
        return basestat *mult;
    }

    public double applyreverse(double basestat,Bufftype type){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getBuffs();
        for (Statbuff buff : buffs) {
            if (buff.getType() == type) {
                if (buff.isBuff()) {
                    stat = stat * ((100 - buff.getAmount()) / 100);
                } else {
                    stat = stat * ((buff.getAmount() / 100) + 1);
                }
            }
        }
        return stat;
    }

    public double applymult(double basestat,Bufftype type){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getBuffs();
        for (Statbuff buff : buffs) {
            if (buff.getType() == type) {
                if (buff.isBuff()) {
                    stat = stat * ((buff.getAmount() / 100) + 1);
                } else {
                    stat = stat * ((100 - buff.getAmount()) / 100);
                }
            }
        }
        return stat;
    }

    public double applyadd(double basestat,Bufftype type){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getBuffs();
        for (Statbuff buff : buffs) {
            if (buff.getType() == type) {
                if (buff.isBuff()) {
                    stat = stat + buff.getAmount();
                } else {
                    stat = stat - buff.getAmount();
                }
            }
        }
        return stat;
    }

    public double applyAttackspeed(double basestat,Bufftype type){
        double stat = basestat;
        ArrayList<Statbuff> buffs = getBuffs();
        for (Statbuff buff : buffs) {
            if (buff.getType() == type) {
                if (buff.isBuff()) {
                    stat = stat / ((buff.getAmount() / 100) + 1);
                } else {
                    stat = stat / ((100 - buff.getAmount()) / 100);
                }
            }
        }
        return stat;
    }
}