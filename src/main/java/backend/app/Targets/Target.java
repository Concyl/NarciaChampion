package backend.app.Targets;

import backend.app.Buffs.SpecialIgnores;
import backend.app.Hero;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Target {
    public enum TargetType{
        ALLY,ENEMY,SELF;
    }
    @Setter @Getter private Hero caster;
    @Setter @Getter private boolean ignoresStealth;

    public Target(){}

    public static Target fromJSON(Hero caster,JSONObject data){
        try{
            String typename = (String) data.get("typename");
            Class<Target> targetClass = (Class<Target>) Class.forName(typename);
            Target target = targetClass.getConstructor(JSONObject.class).newInstance(data);
            target.setCaster(caster);
            return target;
        }
        catch (Exception e){
            return null;
        }
    }

    public abstract ArrayList<Hero> getTarget();

    public ArrayList<Hero> getAllies(){
        if(!this.caster.isConfusion()){
            return this.caster.getAlliesTeam();
        }
        else {
            ArrayList<Hero> confused = new ArrayList<>();
            confused.addAll(this.caster.getEnemyTeam());
            confused.add(this.caster);
            return confused;
        }
    }

    public ArrayList<Hero> getEnemies(){
        if(!this.caster.isConfusion()){
            return this.caster.getEnemyTeam();
        }
        else {
            return this.caster.getAlliesTeam();
        }
    }


    protected ArrayList<Hero> heroesinRange(int radius, int xpos, int ypos, ArrayList<Hero> heroes){
        ArrayList<Hero> aliveheroes = targetableHeroes(heroes);
        ArrayList<Hero> inRangeHeroes = new ArrayList<>();
        for (Hero alivehero : aliveheroes) {
            double xdistance = Math.abs(xpos - alivehero.getXCoordinate());
            double ydistance = Math.abs(ypos - alivehero.getYCoordinate());
            double distance = Math.sqrt((xdistance * xdistance) + (ydistance * ydistance));
            if (distance <= radius * 10) {
                inRangeHeroes.add(alivehero);
            }
        }
        return inRangeHeroes;
    }

    protected ArrayList<Hero> targetableHeroes(ArrayList<Hero> heroes){
        return this.shuffle(filterStealthHeroes(filterDeadHeroes(heroes)));
    }

    protected ArrayList<Hero> filterDeadHeroes(ArrayList<Hero> heroes){
        ArrayList<Hero> aliveheroes = new ArrayList<>();
        for(int i = 0;i<heroes.size();i++){
            if(heroes.get(i).isAlive()){
                aliveheroes.add(heroes.get(i));
            }
        }
        return aliveheroes;
    }

    protected ArrayList<Hero> filterStealthHeroes(ArrayList<Hero> heroes){
        if(this.ignoresStealth || caster.getPassiveIgnore(SpecialIgnores.STEALTH)){
            return heroes;
        }
        ArrayList<Hero> aliveheroes = new ArrayList<>();
        for (Hero hero : heroes) {
            if (!hero.isStealth()) {
                aliveheroes.add(hero);
            }
        }
        return aliveheroes;
    }


    protected ArrayList<Hero> shuffle(ArrayList<Hero> heroes){
        Collections.shuffle(heroes);
        return heroes;
    }

    protected ArrayList<Hero> getAmountofAliveHeroes(int count, ArrayList<Hero> heroes){
        ArrayList<Hero> aliveHeroes = this.targetableHeroes(heroes);
        if(aliveHeroes.size()<count){
            return aliveHeroes;
        }
        ArrayList<Hero> filtered = new ArrayList<>();
        for(int i = 0;i<count;i++){
            filtered.add(aliveHeroes.get(i));
        }
        return filtered;
    }
}
