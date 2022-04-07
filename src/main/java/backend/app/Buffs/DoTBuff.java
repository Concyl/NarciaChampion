package backend.app.Buffs;

import backend.app.DamageEffect;
import backend.app.Hero;

public class DoTBuff extends SpecialBuff {
    private DamageEffect damage;

    public DoTBuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name, int stacks,
                   int tickrate, DamageEffect damage) {
        super(origin,target,preciseOrigin, isBuff, isRemovable, timer, name, stacks,tickrate);
        this.damage = damage;
    }

    @Override
    public void applySkill() {
        // TODO Auto-generated method stub

    }
}
