package backend.app.Heroes;

import backend.app.Buffs.Bufftype;
import backend.app.Buffs.SpecialBuff;
import backend.app.Buffs.SpecialIgnores;
import backend.app.Buffs.Statbuff;
import backend.app.Hero;
import backend.app.Skill;
import org.json.simple.JSONObject;

public class TestHero extends Hero{
    public TestHero(JSONObject heroJSON) {
        super(heroJSON);
    }

    public void init(){
        this.skill = new Skill(30,0,"TestHero Skill");
        SpecialBuff immuna = new SpecialBuff(this,this,"DummyNoobSkill",false,250,"DamageCap", SpecialIgnores.NOREFLECTEDDAMAGE,100,true,100,-1);
        immuna.apply();
        //this.getPassiveIgnores().add(SpecialIgnores.DAMAGECAP);
        //this.setReflectother(10);
    }
    @Override
    public void useSkill(){
        //Statbuff def = new Statbuff(this,this,"BaseLineAbility",true,false,false,1500,"Def", Statbuff.Bufftype.DEF,2);
        //def.apply();
       // TimeBasedPercentSelfHeal heal = new TimeBasedPercentSelfHeal(30,150,false,"Dyna Self Heal",this,this,"Dyna Self Heal",false,1);
    }
}
