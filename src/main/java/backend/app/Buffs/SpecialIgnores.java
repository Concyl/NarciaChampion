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

        }

    },
    CANTMISS{
        @Override
        public void updateSpecialIgnore(Hero hero){

        }

    },
    DAMAGEREDUCTION{
        @Override
        public void updateSpecialIgnore(Hero hero){

        }

    },
    NOREFLECTEDDAMAGE{
        @Override
        public void updateSpecialIgnore(Hero hero){

        }

    },
    IGNOREREFLECT{
        @Override
        public void updateSpecialIgnore(Hero hero){

        }

    },
    STEALTH{
        @Override
        public void updateSpecialIgnore(Hero hero){

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
