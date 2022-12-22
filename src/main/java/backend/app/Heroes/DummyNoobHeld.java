package backend.app.Heroes;

import backend.app.Buffs.*;
import backend.app.Hero;
import backend.app.Skill;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DummyNoobHeld extends Hero {

    public DummyNoobHeld(JSONObject heroJSON) {
        super(heroJSON);
    }

    public void init(){
        this.skill = new Skill(30,0,"DummyNoobHero Skill");

       // this.getPassiveIgnores().add(DamageEffect.SpecialIgnores.IGNOREREFLECT);
        //this.setDamageCap(11);
    }
    @Override
    public void useSkill(){
        ArrayList<Hero> enemies = getAmountofAliveHeroes(1,this.getEnemyTeam());
        for(Hero hero : enemies){
            if(this.isAlive()) {
                ArrayList<SpecialIgnores> specialIgnores = new ArrayList<>();
                specialIgnores.add(SpecialIgnores.ALL);
                Impairment immun = new Impairment(this,hero,"DummyNoobSkill",false,250,"Stun Debuff",specialIgnores, Bufftype.STUN);
                //immun.apply();
                //DamageEffect damage = new DamageEffect(this, enemies.get(i), DamageEffect.DamageType.FLATPERCENT, 70, "Dynamica Skill", DamageEffect.SpecialIgnores.IGNOREREFLECT);
               // damage.applyDamage();
            }
        }
    }
}
