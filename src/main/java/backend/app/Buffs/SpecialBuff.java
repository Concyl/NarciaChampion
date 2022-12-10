package backend.app.Buffs;

import backend.app.DamageEffect;
import backend.app.Hero;
import lombok.Getter;

public class SpecialBuff extends Buff{
    public enum SpecialIgnores{
        DAMAGECAP, DAMAGETOLP, CANTMISS, DAMAGEREDUCTION, NOREFLECTEDDAMAGE, IGNOREREFLECT, STEALTH, CONFUSION
    }

    @Getter private SpecialIgnores type;
    public SpecialBuff(Hero origin, Hero target, String preciseOrigin, boolean isBuff, boolean isRemovable, int timer, String name, SpecialIgnores type) {
        super(origin, target, preciseOrigin, isBuff, isRemovable, timer, name);
        this.type = type;
    }

    @Override
    public void apply(){
        this.target.addSpecialBuff(this);
    }

    @Override
    public void update(){
        super.update();
        if(this.timer == 0){
            this.target.removeSpecialBuff(this);
        }
    }
}
