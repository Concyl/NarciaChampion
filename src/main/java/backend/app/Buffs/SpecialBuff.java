package backend.app.Buffs;

import backend.app.Hero;

public abstract class SpecialBuff extends Buff {

    protected int tickrate;
    protected boolean isSilenceable;
    protected Trigger trigger;
    protected int stacks;
    public enum Trigger{
        TIME,AUTOATTACK,DAMAGETAKEN,ONDEATH,
    }

    public SpecialBuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name, int stacks,
                       int tickrate) {
        super(origin,target,preciseOrigin, isBuff, isRemovable, timer, name);
        this.stacks = stacks;
        this.tickrate = tickrate;
    }

    public abstract void applySkill();

    @Override
    public void update() {
        super.update();
       // if (result && this.timer % this.tickrate == 0) {
          //  this.applySkill();
        //}
    }
}
