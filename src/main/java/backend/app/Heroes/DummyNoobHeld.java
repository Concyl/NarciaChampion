package backend.app.Heroes;

import backend.app.Buffs.SpecificAbilities.TimeBasedPercentSelfHeal;
import backend.app.Hero;
import backend.app.Skill;
import org.json.simple.JSONObject;

public class DummyNoobHeld extends Hero {

    public DummyNoobHeld(JSONObject heroJSON) {
        super(heroJSON);
        this.init();
    }

    private void init(){
        this.skill = new Skill(30,0,"DummyNoobHero Skill");
    }
    @Override
    public void useSkill(){
        TimeBasedPercentSelfHeal heal = new TimeBasedPercentSelfHeal(30,150,false,"Dyna Self Heal",this,this,"Dyna Self Heal",false,15);
    }
}
