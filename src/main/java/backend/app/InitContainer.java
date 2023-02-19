package backend.app;

import backend.app.Buffs.Buff;
import backend.app.SpecialAbilities.SpecialAbility;
import backend.app.Buffs.Talent;
import lombok.Getter;

import java.util.ArrayList;

public class InitContainer {
    @Getter private ArrayList<Talent> talents;
    @Getter private ArrayList<Buff> buffs;
    @Getter private ArrayList<SpecialAbility> specialAbilities;
    @Getter private Hero hero;

    public InitContainer(ArrayList<Talent> talents, ArrayList<Buff> buffs, ArrayList<SpecialAbility> specialAbilities, Hero hero) {
        this.talents = talents;
        this.buffs = buffs;
        this.specialAbilities = specialAbilities;
        this.hero = hero;
    }

    public void init(){
        initBuffs();
        initSpecialAbilites();
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
