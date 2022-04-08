package backend.app.Heroes;

import backend.app.Buffs.Statbuff;
import backend.app.DamageEffect;
import backend.app.Hero;
import backend.app.Skill;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class TestHero extends Hero{
    public TestHero(JSONObject heroJSON) {
        super(heroJSON);
        this.init();
    }

    private void init(){
        this.skill = new Skill(30,0,"TestHero Skill");
    }
    @Override
    public void useSkill(){
        Statbuff def = new Statbuff(this,this,this.getName()+" Skill",true,false,false,210,"", Statbuff.Bufftype.HP,200);
        def.apply();
        Statbuff def1 = new Statbuff(this,this,this.getName()+" Skill",true,false,true,210,"", Statbuff.Bufftype.HP,50);
        def1.apply();
        Statbuff def2 = new Statbuff(this,this,this.getName()+" Skill",false,false,false,210,"", Statbuff.Bufftype.HP,50);
        def2.apply();
    }
}
