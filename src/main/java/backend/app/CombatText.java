package backend.app;

import java.util.ArrayList;

public class CombatText {
    ArrayList<String> combatlog = new ArrayList<>();
    int timer=0;
    public CombatText(){

    }
    public void update(){
        this.timer++;
    }
    public void addCombatText(String text){
        String currentFrameTime = "["+timer+"] "+text;
        this.combatlog.add(currentFrameTime);
    }
    public void printCombatText(){
        for(int i = 0;i<combatlog.size();i++){
            System.out.println(combatlog.get(i));
        }
    }
}
