package backend.app.Buffs;

import backend.app.Hero;

public abstract class SpecialAbility {

    protected int cooldown;
    protected int timer;
    protected int tickrate;
    protected boolean silencable;
    protected Trigger trigger;
    protected int stacks;
    protected String name;
    protected Hero origin;
    protected Hero owner;
    protected String preciseOrigin;
    protected boolean removable;
    protected int chanceToActivate;

    protected int tickcooldown=0;

    public SpecialAbility(int cooldown, int timer, int tickrate, boolean silencable, Trigger trigger, int stacks, String name, Hero origin,Hero owner, String preciseOrigin, boolean removable, int chanceToActivate) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.tickrate = tickrate;
        this.silencable = silencable;
        this.trigger = trigger;
        this.stacks = stacks;
        this.name = name;
        this.origin = origin;
        this.owner = owner;
        this.preciseOrigin = preciseOrigin;
        this.removable = removable;
        this.chanceToActivate = chanceToActivate;
    }


    public enum Trigger{
        TIME,AUTOATTACK,DAMAGETAKEN,ONDEATH
    }

    public abstract void applySkill();

    public void update() {
        this.progressTimer();
        if (this.tickcooldown == 0 && this.trigger == Trigger.TIME && !this.owner.isSilenced()) {
            this.setCooldown();
            this.applySkill();
        }
        if(this.timer == 0){
            this.removeSpecialAbility();
        }
    }

    public void progressTimer(){
        if(this.timer > 0){
            this.timer--;
        }
        if(this.tickcooldown > 0){
            this.tickcooldown--;
        }
    }

    public void removeSpecialAbility(){
        this.origin.getSpecialAbilities().remove(this);
    }

    public void setCooldown(){
        this.tickcooldown = this.tickrate;
    }
}
