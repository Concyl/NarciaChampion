package backend.app;

import java.util.ArrayList;
import java.util.Collections;

public class Skill {
    private int activationrange = -1;
    private int cooldown;
    private int timer;
    private String name;

    public Skill(int activationrange, int cooldown, int timer, String name) {
        this.activationrange = activationrange;
        this.cooldown = cooldown;
        this.timer = timer;
        this.name = name;
    }
    public Skill(int cooldown, int timer, String name) {
        this.cooldown = cooldown;
        this.timer = timer;
        this.name = name;
    }

    public void activateSkill(Hero hero){
        boolean inrange = true;
        if(this.activationrange > 0){
            ArrayList<Hero> inRange= hero.enemiesInRange(this.activationrange,hero.getXCoordinate(),hero.getYCoordinate());
            if(inRange.size() == 0){
                inrange = false;
            }
        }
        if(this.timer == 0 && inrange){
            hero.setEnergy(0);
            System.out.println(hero.getName()+" uses their skill");
            if(this.cooldown >0){
                this.timer = this.cooldown;
            }
            hero.useSkill();
        }
    }

    public void update(){
        if(this.timer > 0){
            this.timer--;
        }
    }
    public int getActivationrange() {
        return activationrange;
    }

    public void setActivationrange(int activationrange) {
        this.activationrange = activationrange;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
