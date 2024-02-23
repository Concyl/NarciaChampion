package backend.app.Pets;

import backend.app.BasicStats;
import backend.app.Hero;
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
}
