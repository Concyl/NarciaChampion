package backend.app;

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
    Warden blueWarden;
    Warden redWarden;
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

    public Battlefield(ArrayList<Hero> blueHeroes, ArrayList<Hero> redHeroes) {
        this.blueHeroes = blueHeroes;
        this.redHeroes = redHeroes;
        this.activeblueHeroes = blueHeroes;
        this.activeredHeroes = redHeroes;
    }

    public void init() {
        setStartingPostions();
        initWarden();
        setBattlefield();
    }

    private void setBattlefield(){
        for(int i = 0; i<this.blueHeroes.size();i++){
            this.blueHeroes.get(i).setBattlefield(this);
            this.blueHeroes.get(i).setTeam(Hero.Team.BLUE);
        }
        for(int i = 0; i<this.redHeroes.size();i++){
            this.redHeroes.get(i).setBattlefield(this);
            this.redHeroes.get(i).setTeam(Hero.Team.RED);
        }
    }

    public void update(){
        GameState gamestatus = this.checkGameState();
        if(gamestatus != GameState.UNDECIDED){
            this.winner = gamestatus;
        }
        else {
            this.heroActionLoop();
        }
        this.timer++;
    }

    private void heroActionLoop(){
        for(int i = 0;i<this.activeblueHeroes.size();i++){
            this.activeblueHeroes.get(i).reduceCooldowns();
        }
        for(int i = 0;i<this.activeredHeroes.size();i++){
            this.activeredHeroes.get(i).reduceCooldowns();
        }
        for(int i = 0;i<this.activeblueHeroes.size();i++){
            this.activeblueHeroes.get(i).move();
        }
        for(int i = 0;i<this.activeredHeroes.size();i++){
            this.activeredHeroes.get(i).move();
        }
        for(int i = 0;i<this.activeblueHeroes.size();i++){
            this.activeblueHeroes.get(i).autoattack();
        }
        for(int i = 0;i<this.activeredHeroes.size();i++){
            this.activeredHeroes.get(i).autoattack();
        }
        for(int i = 0;i<this.activeblueHeroes.size();i++){
            this.activeblueHeroes.get(i).activateAutoProcSkill();
        }
        for(int i = 0;i<this.activeredHeroes.size();i++){
            this.activeredHeroes.get(i).activateAutoProcSkill();
        }
    }

    private GameState checkGameState(){
        if(this.timer >= this.maxtimer){
            return GameState.REDWINTIMEOUT;
        }
        boolean check = true;
        for(int i=0; i<this.activeredHeroes.size();i++){
            if(this.activeredHeroes.get(i).isAlive()){
                check = false;
                break;
            }
        }
        if(check){
            return GameState.BLUEWIN;
        }
        check = true;
        for(int i=0; i<this.activeblueHeroes.size();i++){
            if(this.activeblueHeroes.get(i).isAlive()){
                check = false;
                break;
            }
        }
        if(check){
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
        String bleu = getWardenBonus(this.blueHeroes);
        String red = getWardenBonus(this.redHeroes);
    }

    private String getWardenBonus(ArrayList<Hero> heroes) {
        ArrayList<String> fractions = new ArrayList<>();
        for (int i = 0; i < heroes.size(); i++) {
            fractions.add(heroes.get(i).getFraction());
        }
        return getHighestOccurences(fractions);
    }

    private String getHighestOccurences(ArrayList<String> list) {
        int max = 0;
        int equalmax = 0;
        int curr = 0;
        String currKey = null;
        Set<String> unique = new HashSet<String>(list);

        for (String key : unique) {
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
            return "";
        } else {
            return currKey;
        }
    }
}
