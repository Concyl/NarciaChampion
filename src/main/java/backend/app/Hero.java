package backend.app;

import java.util.ArrayList;
import java.util.Collections;

import backend.app.Buffs.SpecialAbility;
import backend.app.Buffs.Statbuff;
import backend.app.Buffs.TimeBasedSpecialAbility;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import backend.app.Buffs.Buff;

public abstract class Hero {

    public enum Team{
        BLUE,RED
    }

    public enum Immunities{
        ALL,
    }

    private Warden warden;
    protected Skill skill;
    private boolean revived = false;

    @Getter @Setter private Hero target = null;
    private boolean inAttackRange = false;
    private boolean isFlying;
    @Getter @Setter private String name;
    @Getter @Setter private String fullname;
    @Getter @Setter private String fraction;
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
    @Getter @Setter private int reflecttalent=0;
    @Getter @Setter private int reflectother=0;

    @Getter @Setter private int autoattackrange;
    @Getter @Setter private double attackspeed;
    @Getter @Setter private double realattackspeed;
    private boolean canAutoAttack = true;
    private double autoattackcooldown;
    private boolean isBlind=false;
    @Getter private final ArrayList<Statbuff> buffs = new ArrayList<>();
    @Getter private final ArrayList<Statbuff> debuffs = new ArrayList<>();
    @Getter private final ArrayList<Statbuff> immunities = new ArrayList<>();
    @Getter private final ArrayList<Statbuff> negativestatus = new ArrayList<>();
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
        this.setFraction((String) heroJSON.get("fraction"));

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
        if(this.target == null){
            this.searchForTarget();
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
        if(this.target == null || !(this.target.isAlive())){
            Hero closestHeroTarget = null;
            int closestTarget = 0;
            ArrayList<Hero> enemies = this.getEnemyTeam();
            for(int i = 0;i<enemies.size();i++){
                if(!enemies.get(i).isAlive()){
                    continue;
                }
                double distance = Math.sqrt(Math.pow(Math.abs(enemies.get(i).getXCoordinate()-xCoordinate),2)+ Math.pow(Math.abs(enemies.get(i).getYCoordinate()-yCoordinate),2));
                if(distance< closestTarget || closestTarget == 0){
                    closestHeroTarget = enemies.get(i);
                }
            }
            this.target = closestHeroTarget;
        }
    }

    protected ArrayList<Hero> getEnemyTeam(){
        if(this.team == Team.BLUE){
            return battlefield.activeredHeroes;
        }
        else {
            return battlefield.activeblueHeroes;
        }
    }

    protected ArrayList<Hero> getAlliesTeam(){
        if(this.team == Team.BLUE){
            return battlefield.activeblueHeroes;
        }
        else {
            return battlefield.activeredHeroes;
        }
    }

    public void reduceCooldowns(){
        if(this.autoattackcooldown > 0){
            this.autoattackcooldown--;
        }
        for(int i = 0; i<this.buffs.size();i++){
            this.buffs.get(i).update();
        }
        for(int i = 0; i<this.debuffs.size();i++){
            this.debuffs.get(i).update();
        }
        for(int i = 0;i<this.getTimespecialAbilities().size();i++){
            this.getTimespecialAbilities().get(i).update();
        }
        this.skill.update();
    }

    public void autoattack(){
        this.searchForTarget();
        if(this.inAttackRange && this.isAlive() && this.autoattackcooldown == 0 && this.canAutoAttack){
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
            }
        }
    }

    public void postDamage(Hero attacker){
        this.deathCheck(attacker);
        this.addAutoEnergy();
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
    private void deathCheck(Hero attacker){
        if(this.currentHp <= 0){
            this.setAlive(false);
            attacker.setTarget(null);
            this.target = null;
            this.currentHp = 0;
            this.energy = 0;
            String death = this.getFullname()+ " dies";
            this.getBattlefield().getCombatText().addCombatText(death);
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
        if(this.isAutoproc){
            activateSkill();
        }
    }

    protected abstract void useSkill();

    public ArrayList<Hero> alliesInRange(int radius, int xpos, int ypos){
        return HeroesinRange(radius,xpos,ypos,this.getAlliesTeam());
    }
    public ArrayList<Hero> enemiesInRange(int radius, int xpos, int ypos){
        return HeroesinRange(radius,xpos,ypos,this.getEnemyTeam());
    }

    public void calculateNegativeEffects(){
        this.resetNegativeEffets();
        for(int i = 0;this.getNegativestatus().size()<i;i++){
           Statbuff.Bufftype buff = this.getNegativestatus().get(i).getType();
           switch(buff){
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
                   this.canReceiveEnergy = false;
                   break;
               case DISARM:
                   this.canAutoAttack = false;
               case BLIND:
                   this.isBlind = true;
               case PARALYZE:
                   this.applyStuns();
                   this.canReceiveEnergy = false;
                   // TODO
                   // KEINE HEILUNG
           }
       }
    }

    private void resetNegativeEffets(){
        this.canMove = true;
        this.canAutoAttack = true;
        this.canActivateSkill = true;
        this.silenced = false;
        this.canReceiveEnergy = true;
    }

    private void applySilence(){
        this.canActivateSkill = false;
        this.silenced = true;
    }

    private void applyStuns(){
        this.canMove = false;
        this.canAutoAttack = false;
        this.canActivateSkill = false;
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
        for(int i = 0;i<aliveheroes.size();i++) {
            double xdistance = Math.abs(xpos-aliveheroes.get(i).getXCoordinate());
            double ydistance = Math.abs(ypos-aliveheroes.get(i).getYCoordinate());
            double distance = Math.sqrt((xdistance*xdistance)+(ydistance*ydistance));
            if(distance<= radius){
                inRangeHeroes.add(aliveheroes.get(i));
            }
        }
        return shuffle(aliveheroes);
    }

    private ArrayList<Hero> filterDeadHeroes(ArrayList<Hero> heroes){
        ArrayList<Hero> aliveheroes = new ArrayList<>();
        for(int i = 0;i<heroes.size();i++){
            if(heroes.get(i).isAlive()){
                aliveheroes.add(heroes.get(i));
            }
        }
        return aliveheroes;
    }
    private ArrayList<Hero> shuffle(ArrayList<Hero> heroes){
        ArrayList<Hero> shuffleheroes = new ArrayList<>();
        for(int i = 0;i<heroes.size();i++){
            shuffleheroes.add(heroes.get(i));
        }
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
}