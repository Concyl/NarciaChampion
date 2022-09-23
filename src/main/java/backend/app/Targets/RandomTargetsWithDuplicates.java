package backend.app.Targets;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class RandomTargetsWithDuplicates extends Target{
    private int amount;
    private TargetType team;

    public RandomTargetsWithDuplicates(JSONObject specialJSON) {
        this.setIgnoresStealth((boolean) specialJSON.get("ignoresStealth"));
        this.amount = (int)(long) specialJSON.get("amount");
        this.team = Target.TargetType.valueOf((String)specialJSON.get("team"));
    }

    public ArrayList<Hero> setTargets(ArrayList<Hero> heroes){
        ArrayList<Hero> targetableHeroes = this.targetableHeroes(heroes);
        ArrayList<Hero> targets = new ArrayList<>();
        for(int i = 0;i<this.amount;i++){
            int randomNumber = (int)(Math.random()*targetableHeroes.size());
            if(!targets.contains(targetableHeroes.get(randomNumber))){
                targets.add(targetableHeroes.get(randomNumber));
            }
        }
        return targets;
    }



    @Override
    public ArrayList<Hero> getTarget() {
        if(this.team == TargetType.ALLY){
            return this.setTargets(this.getAllies());
        }
        else {
            return this.setTargets(this.getEnemies());
        }
    }
}
