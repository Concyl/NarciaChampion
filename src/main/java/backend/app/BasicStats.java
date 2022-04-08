package backend.app;

import lombok.Getter;

public class BasicStats {
    @Getter private int attack;
    @Getter private double hp;
    @Getter private int accuracy;
    @Getter private int evasion;
    @Getter private int crit;
    @Getter private int cridamage;
    @Getter private int critdef;
    @Getter private int energyrecoveryrate;
    @Getter private int movementspeed;
    @Getter private double attackspeed;
    @Getter private String origin;

    public BasicStats(int attack, double hp, int accuracy, int evasion, int crit, int cridamage, int critdef,int energyrecoveryrate,int movementspeed,double attackspeed, String origin) {
        this.attack = attack;
        this.hp = hp;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.crit = crit;
        this.cridamage = cridamage;
        this.critdef = critdef;
        this.energyrecoveryrate = energyrecoveryrate;
        this.movementspeed = movementspeed;
        this.attackspeed=attackspeed;
        this.origin = origin;
    }
}
