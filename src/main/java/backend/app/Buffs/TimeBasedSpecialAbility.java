package backend.app.Buffs;

import backend.app.Hero;

public class TimeBasedSpecialAbility extends SpecialAbility{

    protected int tickrate;
    protected int tickcooldown =0;

    public TimeBasedSpecialAbility(int cooldown, int timer, int tickrate, boolean silencable, int stacks,
                                   String name, Hero origin, Hero owner, String preciseOrigin, boolean removable){
        super(cooldown,timer,silencable,stacks,name,origin,owner,preciseOrigin,removable);
        this.tickrate = tickrate;
    }

    @Override
    public void applySkill() {
        this.setCooldown();
    }

    @Override
    public void update() {
        super.update();
        if (this.tickcooldown == 0 && !this.owner.isSilenced()) {
            this.setCooldown();
            this.applySkill();
        }
    }

    public void progressTimer(){
        super.progressTimer();
        if(this.tickcooldown > 0){
            this.tickcooldown--;
        }
    }

    public void setCooldown(){
        this.tickcooldown = this.tickrate;
    }
}
