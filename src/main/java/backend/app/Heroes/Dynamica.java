package backend.app.Heroes;

import backend.app.DamageEffect;
import backend.app.Hero;
import backend.app.Skill;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Dynamica extends Hero {
    public Dynamica(JSONObject heroJSON) {
        super(heroJSON);
        this.init();
    }

    private void init(){
        this.skill = new Skill(210,0,"Dynamica Skill");
    }
    @Override
    public void useSkill(){
        ArrayList<Hero> enemies = getAmountofAliveHeroes(1,this.getEnemyTeam());
        for(int i = 0; i<enemies.size();i++){
            if(this.isAlive()) {
                DamageEffect damage = new DamageEffect(this, enemies.get(i), DamageEffect.DamageType.NORMAL, 5, "Dynamica Skill");
                damage.applyDamage();
            }
        }
    }
}
