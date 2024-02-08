package backend.app.Pets;

import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import backend.app.SpecialAbilities.SpecialAbility;
import lombok.Getter;
import org.json.simple.JSONObject;

public class Pet {
    @Getter private Hero hero;
    @Getter private String name;
    @Getter private SpecialAbility specialAbility;
    @Getter private int id;
    @Getter private int specialAbilityid;

    public Pet (Hero hero, String name, int specialAbilityid, int id){
        this.hero = hero;
        this.name = name;
        this.id = id;
        this.specialAbilityid = specialAbilityid;
        this.setPetSkill();
    }

    public Pet(JSONObject jsonObject){
        this.name = (String) jsonObject.get("name");
        this.id = Math.toIntExact((Long) jsonObject.get("id"));
        this.specialAbilityid = Math.toIntExact((Long) jsonObject.get("specialAbilityid"));
    }


    public void init(Hero hero){
        this.hero = hero;
        this.hero.setPet(this);
        this.setPetSkill();
    }
    private void setPetSkill (){
        JSONObject special = DataLoader.getIDObjectfromJSON(this.specialAbilityid, HeroInitController.specialAbilitiesJSON,"id");
        SpecialAbility ability =  SpecialAbility.fromJSON(special);
        ability.setPreciseOrigin(this.name+" "+this.hero.getTeam());
        ability.setFromTalent(true);
        this.hero.addSpecialAbility(ability);
        this.specialAbility = ability;
        ability.init(this.hero,this.hero);
    }
}
