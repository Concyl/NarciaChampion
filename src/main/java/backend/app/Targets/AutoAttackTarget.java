package backend.app.Targets;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AutoAttackTarget extends Target{

    public AutoAttackTarget(Hero caster) {
       this.setIgnoresStealth(false);
       this.setCaster(caster);
    }

    public ArrayList<Hero> setTargets(ArrayList<Hero> heroes){
        ArrayList<Hero> targetableHeroes = this.targetableHeroes(heroes);
        ArrayList<Hero> targets = new ArrayList<>();
        Hero closest = null;
        double max = Double.MAX_VALUE;
        int min = Math.min(1,targetableHeroes.size());
        for(int i = 0;i<min;i++){
            double distance = Math.sqrt(Math.pow(Math.abs(targetableHeroes.get(i).getXCoordinate()-this.getCaster().getXCoordinate()),2)+ Math.pow(Math.abs(targetableHeroes.get(i).getYCoordinate()-this.getCaster().getYCoordinate()),2));
            if(distance<max){
                max = distance;
                closest = targetableHeroes.get(i);
            }
        }
        if(closest != null){
            targets.add(closest);
        }
        return targets;
    }

    @Override
    public ArrayList<Hero> getTarget() {
        return this.setTargets(this.getCaster().getEnemyTeam());
    }
}
