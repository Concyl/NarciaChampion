package backend.app.Targets;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AllHeroesInArea extends Target{

    private int radius;
    private TargetType team;
    private int amount;

    public AllHeroesInArea(JSONObject specialJSON) {
        this.setIgnoresStealth((boolean) specialJSON.get("ignoresStealth"));
        this.radius = (int)(long) specialJSON.get("radius");
        this.team = Target.TargetType.valueOf((String)specialJSON.get("team"));
    }
    @Override
    public ArrayList<Hero> getTarget() {
        if(this.team == TargetType.ALLY){
            return this.heroesinRange(this.radius,this.getCaster().getXCoordinate(),this.getCaster().getYCoordinate(),this.getAllies());
        }
        else {
            return this.heroesinRange(this.radius,this.getCaster().getXCoordinate(),this.getCaster().getYCoordinate(),this.getEnemies());
        }
    }
}
