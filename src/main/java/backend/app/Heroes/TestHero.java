package backend.app.Heroes;

import backend.app.Buffs.SpecificAbilities.TimeBasedPercentSelfHeal;
import backend.app.Buffs.Statbuff;
import backend.app.DamageEffect;
import backend.app.Hero;
import backend.app.Skill;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TestHero extends Hero{
    public TestHero(JSONObject heroJSON) {
        super(heroJSON);
    }

    public void init(){
        this.skill = new Skill(30,0,"TestHero Skill");
        this.getPassiveIgnores().add(DamageEffect.SpecialIgnores.DAMAGECAP);
        this.setReflectother(10);
        Statbuff def = new Statbuff(this,this,"BaseLineAbility",true,false,false,1500,"Def", Statbuff.Bufftype.DEF,95);
        def.apply();
    }
    @Override
    public void useSkill(){
        //Statbuff def = new Statbuff(this,this,"BaseLineAbility",true,false,false,1500,"Def", Statbuff.Bufftype.DEF,2);
        //def.apply();
       // TimeBasedPercentSelfHeal heal = new TimeBasedPercentSelfHeal(30,150,false,"Dyna Self Heal",this,this,"Dyna Self Heal",false,1);
    }
}
