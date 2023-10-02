package backend.app.SpecialAbilities;

import backend.app.Conditions.Condition;
import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import backend.app.Targets.Target;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public abstract class SpecialAbility {

    @Getter @Setter protected int cooldownTimer = 0;
    @Getter @Setter protected boolean silencable;
    @Getter @Setter protected String name;
    @Getter @Setter protected Hero origin;
    @Getter @Setter protected Hero owner;
    @Getter @Setter protected String preciseOrigin;
    @Getter @Setter protected boolean removable;
    @Getter @Setter protected int timer;
    @Getter @Setter protected int cooldown;
    @Getter @Setter protected Ability ability;
    @Getter @Setter protected Target target;
    @Getter @Setter protected int id;
    @Getter @Setter protected ArrayList<Condition> conditions = new ArrayList<>();
    @Getter @Setter protected boolean fromTalent = false;

    JSONObject jsontarget;

    public SpecialAbility(int cooldown,int timer, boolean silencable, String name, Hero origin,Hero owner, String preciseOrigin, boolean removable) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.silencable = silencable;
        this.name = name;
        this.origin = origin;
        this.owner = owner;
        this.preciseOrigin = preciseOrigin;
        this.removable = removable;
    }

    public SpecialAbility(int cooldown,int timer, boolean silencable, String name, String preciseOrigin, boolean removable, int id) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.silencable = silencable;
        this.name = name;
        this.preciseOrigin = preciseOrigin;
        this.removable = removable;
        this.id = id;
    }
    public SpecialAbility(){

    }

    public SpecialAbility(JSONObject specialJSON){
        this( (int)(long) specialJSON.get("cooldown"),
                (int)(long) specialJSON.get("timer"),
                (boolean) specialJSON.get("silencable"),
                (String)specialJSON.get("name"),
                (String)specialJSON.get("preciseOrigin"),
                (boolean) specialJSON.get("removable"),
                (int)(long) specialJSON.get("id"));
       this.ability = Ability.fromJSON((JSONObject) specialJSON.get("ability"));
       this.jsontarget = (JSONObject) specialJSON.get("target");
       this.initConditionsFromIdArray((JSONArray) specialJSON.get("conditions"));
    }

    public static SpecialAbility fromJSON(JSONObject data) {
        try{
            String typename = (String) data.get("typename");
            Class<SpecialAbility> specialAbilityClass = (Class<SpecialAbility>) Class.forName(typename);
            return specialAbilityClass.getConstructor(JSONObject.class).newInstance(data);
        }
        catch (Exception e){
            return null;
        }
    }

    public void applySkill(){
        this.ability.applySkill(this);
    }

    public void setCooldown(){
        this.cooldownTimer = this.cooldown;
    }

    public void update() {
        this.progressTimer();
        if(this.timer == 0){
            this.removeSpecialAbility();
        }
    }

    public void progressTimer(){
        if(this.timer > 0){
            this.timer--;
        }
        if(this.cooldownTimer > 0){
            this.cooldownTimer--;
        }
    }

    public void init(Hero origin,Hero owner){
        this.origin = origin;
        this.owner = owner;
        this.target = Target.fromJSON(owner,this.jsontarget);
    }

    public void initConditionsFromIdArray(JSONArray data){
        for (Object datum : data) {
            int id = Integer.parseInt(datum.toString());
            this.conditions.add(getConditionFromID(id));
        }
    }

    private Condition getConditionFromID(int id){
        JSONObject jsonObject =DataLoader.getIDObjectfromJSON(id, HeroInitController.conditionJSON,"id");
        return Condition.fromJSON(jsonObject);
    }

    public boolean checkConditions(){
        for(Condition condition : this.conditions){
            if(!condition.checkCondition(this)){
                return false;
            }
        }
        return true;
    }

    public boolean canActivate(){
        if(this.checkConditions() && this.cooldownTimer == 0 && (!this.owner.isSilenced() || !this.silencable)){
            return true;
        }
        return false;
    }

    public abstract void removeSpecialAbility();

}
