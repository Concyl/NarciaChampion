package backend.app;

import java.util.ArrayList;
import java.util.stream.Collectors;

import backend.app.Buffs.SpecialAbility;
import backend.app.Buffs.Talent;
import backend.app.Heroes.DummyNoobHeld;
import backend.app.Heroes.Dynamica;
import backend.app.Heroes.TestHero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HeroInitController {
    private ArrayList<JSONObject> heroJSONList;
    private ArrayList<JSONObject> specialAbilitiesJSON;
    private ArrayList<JSONObject> talentJSON;

    public HeroInitController() {
        this.heroJSONList = DataLoader.loadHeroData();
        this.specialAbilitiesJSON = DataLoader.loadSpecialAbilites("/src/main/resources/specialAbilities.json");
        this.talentJSON = DataLoader.loadSpecialAbilites("/src/main/resources/talent.json");
    }

    public void createHeroObjectswithIds() {
        ArrayList<Integer> idListBlueTeam = new ArrayList<>();
        idListBlueTeam.add(0);

        ArrayList<Integer> idListRedTeam = new ArrayList<>();
        idListRedTeam.add(1000);
        ArrayList<Hero> blueHeroes = initHeroFromIds(idListBlueTeam);
        ArrayList<Hero> redHeroes = initHeroFromIds(idListRedTeam);
        battlefieldMainLoop(blueHeroes, redHeroes);
    }

    private void battlefieldMainLoop(ArrayList<Hero> blueHeroes,ArrayList<Hero> redHeroes){
        CombatText combatText = new CombatText();
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
            JSONObject heroJSON = DataLoader.getIDObjectfromJSON(heroIds.get(i),this.heroJSONList,"id");
            ArrayList<Integer> specialIDJson = DataLoader.getJSONListfromJSON(heroJSON,"specialAbilities");
            ArrayList<SpecialAbility> abilities = initSpecialAbilitiesFromIds(specialIDJson);
            Hero initHero = mapIdstoHeroes(heroIds.get(i),heroJSON);
            for(int j = 0; j<abilities.size();j++){
                initHero.addSpecialAbility(abilities.get(j));
            }
            initalizedHeroes.add(initHero);
        }
        return initalizedHeroes;
    }

    private ArrayList<SpecialAbility> initSpecialAbilitiesFromIds(ArrayList<Integer> specialIds) {
        ArrayList<SpecialAbility> abilities = new ArrayList<>();
        for (int i = 0; i < specialIds.size(); i++) {
            SpecialAbility specialAbility = getSpecialAbilityfromId(specialIds.get(i));
            abilities.add(specialAbility);
        }
        return abilities;
    }

    private Hero mapIdstoHeroes (int id, JSONObject jsonObject){
        switch (id){
            case 0:
                return new TestHero(jsonObject);
            case 1:
                return new Dynamica(jsonObject);
            case 1000:
                return new DummyNoobHeld(jsonObject);
            default:
                return new Dynamica(jsonObject);
        }
    }

    public SpecialAbility getSpecialAbilityfromId(int id){
        JSONObject special = DataLoader.getIDObjectfromJSON(id,this.specialAbilitiesJSON,"id");
        SpecialAbility ability =  SpecialAbility.fromJSON(special);
        return ability;
    }

    public ArrayList<Talent> getTalentsFromIds(ArrayList<Integer> ids){
        ArrayList<Talent> talents = new ArrayList<>();
        for(int i=0;i<ids.size();i++){
            talents.add(this.getTalentFromId(ids.get(i)));
        }
        return talents;
    }

    public Talent getTalentFromId(int id){
        JSONObject jsonTalent = DataLoader.getIDObjectfromJSON(id,this.talentJSON,"id");
        Talent talent = new Talent(jsonTalent);
        for(int i = 0;i<talent.getSpecialAbilityIds().size();i++){
            talent.getSpecialAbilities().add(getSpecialAbilityfromId(talent.getSpecialAbilityIds().get(i)));
        }
        return talent;
    }
}
