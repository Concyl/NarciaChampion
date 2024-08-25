package backend.app.Pets;

import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import backend.app.SpecialAbilities.SpecialAbility;
import lombok.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Pet {
    @Getter private Hero hero;
    @Getter private String name;
    @Getter private ArrayList<SpecialAbility> specialAbilities = new ArrayList<>();
    @Getter private int id;
    @Getter private ArrayList<Integer> specialAbilityids = new ArrayList<>();

    public Pet (Hero hero, String name, ArrayList<Integer> specialAbilityid, int id){
        this.hero = hero;
        this.name = name;
        this.id = id;
        this.specialAbilityids = specialAbilityid;
        this.setPetSkill();
    }

    public Pet(JSONObject jsonObject){
        this.name = (String) jsonObject.get("name");
        this.id = Math.toIntExact((Long) jsonObject.get("id"));
        JSONArray arr = (JSONArray) jsonObject.get("specialAbilityid");
        for (Object buff : arr) {
            int id = Integer.parseInt(buff.toString());
            this.specialAbilityids.add(id);
        }
    }

    public Pet(){

    }

    public Pet init(Hero hero){
        this.hero = hero;
        this.hero.setPet(this);
        this.setPetSkill();
        return this;
    }
    protected void setPetSkill (){
        for(int i = 0;i<this.specialAbilityids.size();i++){
            JSONObject special = DataLoader.getIDObjectfromJSON(this.specialAbilityids.get(i), HeroInitController.specialAbilitiesJSON,"id");
            SpecialAbility ability =  SpecialAbility.fromJSON(special);
            ability.setPreciseOrigin(this.name+" "+this.hero.getTeam());
            ability.setFromTalent(true);
            this.hero.addSpecialAbility(ability);
            this.specialAbilities.add(ability);
            ability.init(this.hero,this.hero);
        }
    }
}
