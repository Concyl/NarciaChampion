package backend.app.Buffs;

public enum SpecialIgnores {
    DAMAGECAP{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){
                buff.target.addDamageCap(buff);
        }
    },
    DAMAGETOLP{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    },
    CANTMISS{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    },
    DAMAGEREDUCTION{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    },
    NOREFLECTEDDAMAGE{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    },
    IGNOREREFLECT{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    },
    STEALTH{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    },
    CONFUSION{
        @Override
        public void addSpecialIgnore(SpecialBuff buff){

        }
    };
    public abstract void addSpecialIgnore(SpecialBuff buff);
}
