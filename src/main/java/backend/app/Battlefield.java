package backend.app;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Battlefield {
    int height = 2000;
    int width = 1000;
    int timer = 0;
    int maxtimer = 1350;
    GameState winner = GameState.UNDECIDED;
    int[][] blueTeamStartPositions = { { 250, 1250 }, { 300, 1250 }, { 350, 1250 }, { 250, 1300 }, { 300, 1300 },
            { 350, 1300 } };
    int[][] redTeamStartPositions = { { 250, 1150 }, { 300, 1150 }, { 350, 1150 }, { 250, 1100 }, { 300, 1100 },
            { 350, 1100 } };
    ArrayList<Hero> blueHeroes;
    ArrayList<Hero> redHeroes;
    ArrayList<Hero> activeblueHeroes;
    ArrayList<Hero> activeredHeroes;
    ArrayList<InitContainer> blue;
    ArrayList<InitContainer> red;
    Warden blueWarden;
    Warden redWarden;
    @Getter CombatText combatText;
    public enum GameState{
        UNDECIDED,BLUEWIN,REDWIN,REDWINTIMEOUT;

        public String toString() {
            switch (this){
                case UNDECIDED:
                    return "Undecided";
                case REDWIN:
                    return "RedWin";
                case BLUEWIN:
                    return "BlueWin";
                case REDWINTIMEOUT:
                    return "RedWinTimeout";
                default:
                    return "Undecided, something is wrong";
            }
        }
    }

    public Battlefield(ArrayList<InitContainer> blue, ArrayList<InitContainer> red,CombatText combatText) {
        this.blue = blue;
        this.red = red;
        this.combatText = combatText;
    }

    public void init() {
        fillTeams();
        setStartingPostions();
        initWarden();
        setBattlefield();
        initContainer(this.blue);
        initContainer(this.red);
    }

    private void initContainer(ArrayList<InitContainer> containerList){
        for(InitContainer container: containerList){
            container.init();
        }
    }

    private void fillTeams(){
        ArrayList<Hero> blueHeroes = new ArrayList<>();
        for(InitContainer container: this.blue){
            blueHeroes.add(container.getHero());
        }
        this.blueHeroes = blueHeroes;
        this.activeblueHeroes = blueHeroes;
        ArrayList<Hero> redHeroes = new ArrayList<>();
        for(InitContainer container: this.red){
            redHeroes.add(container.getHero());
        }
        this.redHeroes = redHeroes;
        this.activeredHeroes = redHeroes;
    }

    private void setBattlefield(){
        for (Hero blue : this.blueHeroes) {
            blue.setBattlefield(this);
            blue.setTeam(Hero.Team.BLUE);
            blue.setFullname(blue.getTeam() + " " + blue.getName());
            blue.init();
        }
        for (Hero red : this.redHeroes) {
            red.setBattlefield(this);
            red.setTeam(Hero.Team.RED);
            red.setFullname(red.getTeam() + " " + red.getName());
            red.init();
        }
    }

    public void update(){
        this.heroActionLoop();
        GameState gamestatus = this.checkGameState();
        if(gamestatus != GameState.UNDECIDED){
            this.winner = gamestatus;
            return;
        }
        this.timer++;
        this.combatText.update();
    }

    private void heroActionLoop(){
        for (Hero activeblueHero : this.activeblueHeroes) {
            activeblueHero.reduceCooldowns();
        }
        for (Hero activeredHero : this.activeredHeroes) {
            activeredHero.reduceCooldowns();
        }
        for (Hero activeblueHero : this.activeblueHeroes) {
            activeblueHero.move();
        }
        for (Hero activeredHero : this.activeredHeroes) {
            activeredHero.move();
        }
        for (Hero activeblueHero : this.activeblueHeroes) {
            activeblueHero.autoattack();
        }
        for (Hero activeredHero : this.activeredHeroes) {
            activeredHero.autoattack();
        }
        for (Hero activeblueHero : this.activeblueHeroes) {
            activeblueHero.activateAutoProcSkill();
        }
        for (Hero activeredHero : this.activeredHeroes) {
            activeredHero.activateAutoProcSkill();
        }
    }

    private GameState checkGameState(){
        if(this.timer >= this.maxtimer){
            return GameState.REDWINTIMEOUT;
        }
        activeredHeroes.removeIf(x -> !x.isAlive() && x.isRevived() && !x.isInProcessOfRevive());
        if(activeredHeroes.stream().noneMatch(x-> x.isAlive() && !x.isInProcessOfRevive())){
            return GameState.BLUEWIN;
        }
        activeblueHeroes.removeIf(x -> !x.isAlive() && x.isRevived() && !x.isInProcessOfRevive());
        if(activeblueHeroes.stream().noneMatch(x-> x.isAlive() && !x.isInProcessOfRevive())){
            return GameState.REDWIN;
        }
        return GameState.UNDECIDED;
    }

    private void setStartingPostions() {
        for (int i = 0; i < blueHeroes.size(); i++) {
            blueHeroes.get(i).setXCoordinate(blueTeamStartPositions[i][0]);
            blueHeroes.get(i).setYCoordinate(blueTeamStartPositions[i][1]);
        }
        for (int i = 0; i < redHeroes.size(); i++) {
            redHeroes.get(i).setXCoordinate(redTeamStartPositions[i][0]);
            redHeroes.get(i).setYCoordinate(redTeamStartPositions[i][1]);
        }
    }

    private void initWarden() {
        Hero.Fraction bleu = getWardenBonus(this.blueHeroes);
        Hero.Fraction red = getWardenBonus(this.redHeroes);
    }

    private Hero.Fraction getWardenBonus(ArrayList<Hero> heroes) {
        ArrayList<Hero.Fraction> fractions = new ArrayList<>();
        for (Hero hero : heroes) {
            fractions.add(hero.getFraction());
        }
        return getHighestOccurences(fractions);
    }

    private Hero.Fraction getHighestOccurences(ArrayList<Hero.Fraction> list) {
        int max = 0;
        int equalmax = 0;
        int curr = 0;
        Hero.Fraction currKey = null;
        Set<Hero.Fraction> unique = new HashSet<>(list);

        for (Hero.Fraction key : unique) {
            curr = Collections.frequency(list, key);

            if (max == curr) {
                equalmax = curr;
            }
            if (max < curr) {
                max = curr;
                currKey = key;
            }
        }
        if (equalmax == max) {
            return Hero.Fraction.NONE;
        } else {
            return currKey;
        }
    }
}
