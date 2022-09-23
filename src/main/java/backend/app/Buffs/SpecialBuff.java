package backend.app.Buffs;

import backend.app.DamageEffect;
import backend.app.Hero;
import lombok.Getter;

public class SpecialBuff extends Buff{
    @Getter private DamageEffect.SpecialIgnores type;
    public SpecialBuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name, DamageEffect.SpecialIgnores type) {
        super(origin, target, preciseOrigin, isBuff, isRemovable, timer, name);
        this.type = type;
    }
}
