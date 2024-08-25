package backend.app.Pets;

import backend.app.BasicStats;
import backend.app.DataLoader;
import backend.app.Hero;
import backend.app.HeroInitController;
import backend.app.SpecialAbilities.SpecialAbility;
import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class SuperPet extends Pet{
    @Getter
    private BasicStats stats;

    @Getter
    private int petFlairId;

    public SuperPet(){
    }

    public SuperPet(JSONObject jsonObject) {
        super(jsonObject);
        this.petFlairId = (int)(long) jsonObject.get("stacks");
    }

    @Override
    protected void setPetSkill() {
        super.setPetSkill();
        setFPetlairSkill();
    }

    private void setFPetlairSkill(){
        JSONObject special = DataLoader.getIDObjectfromJSON(this.petFlairId, HeroInitController.specialAbilitiesJSON,"id");
        SpecialAbility ability =  SpecialAbility.fromJSON(special);
        ability.setPreciseOrigin(this.getName() +" "+ this.getHero().getTeam());
        ability.setFromTalent(true);
        this.getHero().addSpecialAbility(ability);
        this.getSpecialAbilities().add(ability);
        ability.init(this.getHero(), this.getHero());
    }
}
