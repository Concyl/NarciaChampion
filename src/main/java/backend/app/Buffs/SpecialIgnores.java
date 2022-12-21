package backend.app.Buffs;

import backend.app.Hero;

public enum SpecialIgnores {
    DAMAGECAP{
        @Override
        public void updateSpecialIgnore(Hero hero){
            hero.updateDamageCap();
        }

    },
    DAMAGETOLP{
        @Override
        public void updateSpecialIgnore(Hero hero){
            hero.updateDamageToLp();
        }

    },
    CANTMISS{
        @Override
        public void updateSpecialIgnore(Hero hero){
            // Never used
        }

    },
    DAMAGEREDUCTION{
        @Override
        public void updateSpecialIgnore(Hero hero){
            // Never used
        }

    },
    NOREFLECTEDDAMAGE{
        @Override
        public void updateSpecialIgnore(Hero hero){
            // Never used
        }

    },
    IGNOREREFLECT{
        @Override
        public void updateSpecialIgnore(Hero hero){
            // Never used
        }

    },
    STEALTH{
        @Override
        public void updateSpecialIgnore(Hero hero){

        }

    },
    REFLECTTALENT{
        @Override
        public void updateSpecialIgnore(Hero hero){
            hero.updateReflectTalent();
        }

    },
    REFLECT{
        @Override
        public void updateSpecialIgnore(Hero hero){
            hero.updateReflect();
        }

    },
    REFLECTCAP{
        @Override
        public void updateSpecialIgnore(Hero hero){
            hero.updateReflectCap();
        }

    },
    CONFUSION{
        @Override
        public void updateSpecialIgnore(Hero hero){

        }

    },
    ALL{
        @Override
        public void updateSpecialIgnore(Hero hero){
            hero.updateImmuneAll();
        }

    };
    public abstract void updateSpecialIgnore(Hero hero);
}
