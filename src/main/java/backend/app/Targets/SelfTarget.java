package backend.app.Targets;

import backend.app.Hero;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class SelfTarget extends Target {

    public SelfTarget(JSONObject specialJSON) {
        this.setIgnoresStealth((boolean) specialJSON.get("ignoresStealth"));
    }

    @Override
    public  ArrayList<Hero> getTarget() {
        ArrayList<Hero> targets = new ArrayList<>();
        targets.add(this.getCaster());
        return targets;
    }
}
