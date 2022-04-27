package backend.app;

import lombok.Getter;

import java.util.ArrayList;

public class BonusStats {
    public enum BonusStat{
        ATTACK,HP,CRIT,CRITDAMAGE,CRITDEF,ACCURACY,Dodge
    }
    public enum Gear{
        CONQUEST,OATH
    }
    public enum Accessory{
        MAGIC,COLD,FANG,PUREBLOOD,FROST,CALM
    }

    public BonusStats(Hero hero, ArrayList<BonusStats> relicroll ,Gear gear,Accessory accessory ){

    }


    private void getAccessoryStats(Accessory accessory){

        switch (accessory){
            case MAGIC:
                // Magische Halskette  Magischer Ring
                // Attack 8949         Attack 8949
                // Dodge 2680          CRITDMG 4008


                // Kalte Halskette     Kalter Ring
                // Attack 10906        Attack 10906
                // Dodge 1434          CRITDMG 1956


                // Reißzahn Halskette  Reißzahn Ring
                // Attack 14100        HP 269696
                // CRITDMG 3087        CRITDMG 3503


                // Reinblut Halkette   Reinblut Ring
                // Attack 14100        HP 269696
                // Dodge 2064          Dodge 2469


                // Ruhige Halskette    Ruhiger Ring
                // Attack 18415        HP 296647
                // CRITDMG 2783        CRITDEF 1034
                

                // Frost Halskette     Frost Ring
                // Attack 18415        HP 296647
                // Crit 1136           Dodge 2676
        }
    }

    private void getGearStats(Gear gear){

    }
}
