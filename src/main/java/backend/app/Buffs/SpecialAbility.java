package backend.app.Buffs;

import backend.app.Hero;

public abstract class SpecialAbility {

    protected int cooldownTimer = 0;
    protected boolean silencable;
    protected int stacks;
    protected String name;
    protected Hero origin;
    protected Hero owner;
    protected String preciseOrigin;
    protected boolean removable;
    protected int timer;
    protected int cooldown;

    public SpecialAbility(int cooldown,int timer, boolean silencable, int stacks, String name, Hero origin,Hero owner, String preciseOrigin, boolean removable) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.silencable = silencable;
        this.stacks = stacks;
        this.name = name;
        this.origin = origin;
        this.owner = owner;
        this.preciseOrigin = preciseOrigin;
        this.removable = removable;
    }

    public abstract void applySkill();

    public void update() {
        this.progressTimer();
        if(this.timer == 0){
            this.removeSpecialAbility();
        }
    }

    public void progressTimer(){
        if(this.timer > 0){
            this.timer--;
        }
        if(this.cooldownTimer > 0){
            this.cooldownTimer--;
        }
    }

    public void removeSpecialAbility(){
        this.origin.getSpecialAbilities().remove(this);
    }
}
