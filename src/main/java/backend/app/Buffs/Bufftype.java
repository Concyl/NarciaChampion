package backend.app.Buffs;

import backend.app.Hero;

public enum Bufftype {
    ATTACK{
        @Override
        public void recalculateStat(Hero hero){
            hero.setAttack((int) hero.applymult(hero.getCoreStats().getAttack(),Bufftype.ATTACK));
        }
    },
    HP{
        @Override
        public void recalculateStat(Hero hero){
            hero.setMaxHp((int) hero.applyHp(hero.getCoreStats().getHp(),Bufftype.HP));
        }
    },
    DEF{
        @Override
        public void recalculateStat(Hero hero){
            hero.setDef(hero.applyreverse(1,Bufftype.DEF));
        }
    },
    CRIT{
        @Override
        public void recalculateStat(Hero hero){
            hero.setCritchance((int) hero.applyadd(hero.getCoreStats().getCrit(),Bufftype.CRIT));
        }
    },
    CRITDAMAGE{
        @Override
        public void recalculateStat(Hero hero){
            hero.setCritdamage((int) hero.applyadd(hero.getCoreStats().getCridamage(),Bufftype.CRITDAMAGE));
        }
    },
    CRITDEF{
        @Override
        public void recalculateStat(Hero hero){
            hero.setCritdef((int) hero.applyadd(hero.getCoreStats().getCritdef(),Bufftype.CRITDEF));
        }
    },
    ACCURACY{
        @Override
        public void recalculateStat(Hero hero){
            hero.setAccuracy((int) hero.applyadd(hero.getCoreStats().getAccuracy(),Bufftype.ACCURACY));
        }
    },
    EVASION{
        @Override
        public void recalculateStat(Hero hero){
            hero.setEvasion((int) hero.applyadd(hero.getCoreStats().getEvasion(),Bufftype.EVASION));
        }
    },
    MOVEMENTSPEED{
        @Override
        public void recalculateStat(Hero hero){
            hero.setMovementspeed((int) hero.applymult(hero.getCoreStats().getMovementspeed(),Bufftype.MOVEMENTSPEED));
        }
    },
    HEALING{
        @Override
        public void recalculateStat(Hero hero){
            hero.setHealing((int) hero.applymult(1,Bufftype.HEALING));
        }
    },
    ATTACKSPEED{
        @Override
        public void recalculateStat(Hero hero){
            hero.setAttackspeed((int) hero.applyAttackspeed(hero.getCoreStats().getAttackspeed(),Bufftype.ATTACKSPEED));
            hero.calculateRealAttackspeed();
        }
    },
    ENERGY {
        @Override
        public void recalculateStat(Hero hero) {
            hero.setEnergyrecoveryrate((int) hero.applymult(hero.getCoreStats().getMovementspeed(),Bufftype.ENERGY));
        }
    },
    STUN{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    FEAR{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    FROST{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    SILENCE{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    DISARM{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    PETRIFY{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    ENTANGLE{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    BLIND{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    INHIBIT{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },
    PARALYZE{
        @Override
        public void recalculateStat(Hero hero) {

        }
    },;
    public abstract void recalculateStat(Hero hero);
}
