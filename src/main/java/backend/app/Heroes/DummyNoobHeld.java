package backend.app.Heroes;

import backend.app.Buffs.Bufftype;
import backend.app.Buffs.Statbuff;
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
                //Statbuff immun = new Statbuff(this,hero,"DummyNoobSkill",false,false,true,100,"Para Debuff", Bufftype.PARALYZE,95);
                //immun.apply();
                //DamageEffect damage = new DamageEffect(this, enemies.get(i), DamageEffect.DamageType.FLATPERCENT, 70, "Dynamica Skill", DamageEffect.SpecialIgnores.IGNOREREFLECT);
               // damage.applyDamage();
            }
        }
    }
}
