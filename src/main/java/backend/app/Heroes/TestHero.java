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
        Statbuff attackbuff = new Statbuff(this,this,this.getName()+" Skill",false,false,false,210,"", Statbuff.Bufftype.ATTACK,50);
        attackbuff.apply();
    }
}
