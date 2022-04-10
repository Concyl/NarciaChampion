package backend.app;

import java.util.ArrayList;
import java.util.EventListener;

import backend.app.Heroes.Dynamica;
import backend.app.Heroes.TestHero;
import org.json.simple.JSONObject;

public class HeroInitController {
    private ArrayList<JSONObject> heroJSONList;

    public HeroInitController() {
        this.heroJSONList = DataLoader.loadHeroData();
    }

    public void createHeroObjectswithIds() {
        ArrayList<Integer> idListBlueTeam = new ArrayList<>();
        idListBlueTeam.add(0);

        ArrayList<Integer> idListRedTeam = new ArrayList<>();
        idListRedTeam.add(0);
        CombatText combatText = new CombatText();
        ArrayList<Hero> blueHeroes = initHeroFromIds(idListBlueTeam);
        ArrayList<Hero> redHeroes = initHeroFromIds(idListRedTeam);
        Battlefield battlefield = new Battlefield(blueHeroes, redHeroes,combatText);
        battlefield.init();
        while(battlefield.winner== Battlefield.GameState.UNDECIDED){
            battlefield.update();
        }
        combatText.addCombatText(battlefield.winner.toString());
        combatText.printCombatText();
    }

    private ArrayList<Hero> initHeroFromIds(ArrayList<Integer> heroIds) {
        ArrayList<Hero> initalizedHeroes = new ArrayList<>();
        for (int i = 0; i < heroIds.size(); i++) {
            JSONObject heroJSON = getHeroIdfromJSONList(heroIds.get(i));
            Hero initHero = mapIdstoHeroes(heroIds.get(i),heroJSON);
            initalizedHeroes.add(initHero);
        }
        return initalizedHeroes;
    }

    private JSONObject getHeroIdfromJSONList(int id) {
        for (int i = 0; i < this.heroJSONList.size(); i++) {
            long curid = (long) (this.heroJSONList.get(i).get("id"));
            if (id == Math.toIntExact(curid)) {
                return this.heroJSONList.get(i);
            }
        }
        return null;
    }

    private Hero mapIdstoHeroes (int id, JSONObject jsonObject){
        switch (id){
            case 0:
                return new TestHero(jsonObject);
            case 1:
                return new Dynamica(jsonObject);
            default:
                return new Dynamica(jsonObject);
        }
    }
}
