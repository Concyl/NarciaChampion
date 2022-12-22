package backend.app.Targets;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class RandomTargets extends Target{
    private int amount;
    private TargetType team;

    public RandomTargets(JSONObject specialJSON) {
        this.setIgnoresStealth((boolean) specialJSON.get("ignoresStealth"));
        this.amount = (int)(long) specialJSON.get("amount");
        this.team = Target.TargetType.valueOf((String)specialJSON.get("team"));
    }

    public ArrayList<Hero> setTargets(ArrayList<Hero> heroes){
        ArrayList<Hero> targetableHeroes = this.targetableHeroes(heroes);
        ArrayList<Hero> targets = new ArrayList<>();
        int min = Math.min(this.amount,targetableHeroes.size());
        for(int i = 0;i<min;i++){
            targets.add(targetableHeroes.get(i));
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
