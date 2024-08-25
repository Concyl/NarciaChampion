package backend.app;

import backend.app.Buffs.Buff;
import backend.app.Pets.Pet;
import backend.app.Pets.SuperPet;
import backend.app.SpecialAbilities.SpecialAbility;
import backend.app.Buffs.Talent;
import lombok.Getter;

import java.util.ArrayList;

public class InitContainer {
    @Getter private ArrayList<Talent> talents;
    @Getter private ArrayList<Buff> buffs;
    @Getter private ArrayList<SpecialAbility> specialAbilities;
    @Getter private Hero hero;

    @Getter private Pet pet;

    public InitContainer(ArrayList<Talent> talents, ArrayList<Buff> buffs, ArrayList<SpecialAbility> specialAbilities, Hero hero, Pet pet) {
        this.talents = talents;
        this.buffs = buffs;
        this.specialAbilities = specialAbilities;
        this.hero = hero;
        this.pet = pet;
    }

    public void init(){
        initBuffs();
        initSpecialAbilites();
        initPet();

    }

    private void initPet(){
        Pet pet = this.pet.init(this.hero);
        if (pet instanceof SuperPet){

        }
    }
    private void initSpecialAbilites(){
        for(SpecialAbility ability: this.specialAbilities){
            hero.addSpecialAbility(ability);
        }
    }

    private void initBuffs(){
        for(Buff buff: this.buffs){
            buff.apply();
        }
    }
}
