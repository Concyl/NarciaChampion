package backend.app.Buffs;

import backend.app.Hero;

public class Buff {
    protected String preciseOrigin;
    protected boolean isBuff;
    protected boolean isRemovable;
    protected int timer;
    protected String name;
    protected Hero target;
    protected Hero origin;

    public Buff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name) {
        this.preciseOrigin = preciseOrigin;
        this.isBuff = isBuff;
        this.isRemovable = isRemovable;
        this.timer = timer;
        this.name = name;
        this.origin = origin;
        this.target = target;
    }

    public void update() {
        if(this.timer > 0){
            this.timer--;
        }
    }


}