package backend.app;

public class BasicStats {
    private int attack;
    private double hp;
    private int accuracy;
    private int evasion;
    private int crit;
    private int cridamage;
    private int critdef;
    private String origin;

    public String getOrigin(){
        return origin;
    }

    public int getAttack() {
        return attack;
    }

    public double getHp() {
        return hp;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getCrit() {
        return crit;
    }

    public int getCridamage() {
        return cridamage;
    }

    public int getCritdef() {
        return critdef;
    }

    public BasicStats(int attack, double hp, int accuracy, int evasion, int crit, int cridamage, int critdef, String origin) {
        this.attack = attack;
        this.hp = hp;
        this.accuracy = accuracy;
        this.evasion = evasion;
        this.crit = crit;
        this.cridamage = cridamage;
        this.critdef = critdef;
        this.origin = origin;
    }
}
