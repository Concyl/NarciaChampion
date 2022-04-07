package backend.app;

public class DamageEffect {
    public enum DamageType {
        NORMAL, TRUEDAMAGE, FLAT, FLATPERCENT
    }
    public enum SpecialIgnores{
        DAMAGECAP, DAMAGETOLP, CANTMISS, DAMAGEREDUCTION, NOREFLECTEDDAMAGE, IGNOREREFLECT
    }

    private DamageType damageType;
    private double multiplier;
    private Hero attacker;
    private Hero receiver;
    private String origin;

    public DamageEffect(Hero attacker, Hero receiver, DamageType type, double multiplier, String origin) {
        this.damageType = type;
        this.attacker = attacker;
        this.receiver = receiver;
        this.multiplier = multiplier;
        this.origin = origin;
    }

    public void applyDamage() {
        if(!hitCheck()){
            System.out.println(this.attacker.getName()+" missed "+this.origin+ "against "+receiver.getName());
            return;
        }
        switch (damageType) {
            case NORMAL:
                this.receiveNormalDamage(this.dealNormaldamage());
                break;
            case TRUEDAMAGE:

                break;
            case FLAT:

                break;
            case FLATPERCENT:

                break;
            default:
                break;
        }
        attacker.addAutoEnergy();
    }

    private int dealNormaldamage(){
        if(!this.attacker.isAlive()){
            return 0;
        }
        double narciaAttackmult = 0.75;
        double maxRoll = 1;
        double minRoll = 0.8;
        double damageVariance = (Math.random()*(maxRoll-minRoll))+minRoll;
        int damage = (int)Math.floor(this.attacker.getAttack()*damageVariance*narciaAttackmult*this.multiplier);
        damage = critcheck(damage);
        System.out.println(this.attacker.getName()+" deals "+ damage+ " to "+this.receiver.getName() +" from "+this.origin);
        return damage;
    }

    private void receiveNormalDamage(int damage){
        if(damage == 0 || !this.attacker.isAlive() || !this.receiver.isAlive()){
            return;
        }
        int oldHp = (int)receiver.getCurrentHp();
        //TODO
        receiver.setCurrentHp(oldHp-damage);
        int percentHpBefore = (int)Math.floor((oldHp/receiver.getMaxHp())*100);
        int percentNow = (int)Math.floor((receiver.getCurrentHp()/receiver.getMaxHp())*100);
        String combattext = receiver.getName() + " receives " + damage +" Normal Damage from "+ this.origin+", HP reduced from "+ oldHp+ " ("+percentHpBefore+"%) to "+(int)receiver.getCurrentHp()+ " ("+percentNow+"%)";
        System.out.println(combattext);
        this.receiver.postDamage(this.attacker);
    }

    private int critcheck(int damage){
        double critchance = this.attacker.getCritchance();
        double critdef = this.receiver.getCritdef();
        if(critdef>critchance){
            return damage;
        }
        double critical = (critchance-critdef)/(critchance-critdef+10000);
        if(critical>=Math.random()){
            double critdamage = this.attacker.getCritdamage();
            damage *= 1.5+(critdamage/10000);
            double critpercentage = Math.floor(critical*10000)/100;
            String combattext = this.attacker.getName()+" crits, with "+ critpercentage+"% critchance";
            System.out.println(combattext);
        }
        return damage;
    }

    private boolean hitCheck(){
        int hitcap = 10000;
        if(this.receiver.getEvasion()< this.attacker.getAccuracy()){
            return true;
        }
        int hitpluscap = this.attacker.getAccuracy()+hitcap;
        if(this.receiver.getEvasion()>hitpluscap){
            return false;
        }
        int randlower = this.receiver.getEvasion()- this.attacker.getAccuracy();
        double rand = Math.floor(Math.random()*hitcap);
        if(rand>=randlower){
            return true;
        }
        return false;
    }
}
