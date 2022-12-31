package backend.app;

import java.util.ArrayList;
import java.util.stream.Collectors;

import backend.app.Buffs.Buff;
import backend.app.Buffs.SpecialAbility;
import backend.app.Buffs.Talent;
import backend.app.Heroes.DummyNoobHeld;
import backend.app.Heroes.Dynamica;
import backend.app.Heroes.TestHero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class HeroInitController {
    private ArrayList<JSONObject> heroJSONList;
    public static ArrayList<JSONObject> specialAbilitiesJSON;
    private ArrayList<JSONObject> talentJSON;
    public static ArrayList<JSONObject> conditionJSON;
    public static ArrayList<JSONObject> buffJSON;

    public HeroInitController() {
        this.heroJSONList = DataLoader.loadHeroData();
        this.specialAbilitiesJSON = DataLoader.loadSpecialAbilites("/src/main/resources/specialAbilities.json");
        this.talentJSON = DataLoader.loadSpecialAbilites("/src/main/resources/talent.json");
        this.conditionJSON = DataLoader.loadSpecialAbilites("/src/main/resources/conditions.json");
        this.buffJSON = DataLoader.loadSpecialAbilites("/src/main/resources/buff.json");
    }

    public void createHeroObjectswithIds() {
        ArrayList<Integer> idListBlueTeam = new ArrayList<>();
        idListBlueTeam.add(0);

        ArrayList<Integer> idListRedTeam = new ArrayList<>();
        idListRedTeam.add(1000);
        ArrayList<InitContainer> blue = initHeroFromIds(idListBlueTeam);
        ArrayList<InitContainer> red = initHeroFromIds(idListRedTeam);
        battlefieldMainLoop(blue, red);
    }

    private void battlefieldMainLoop(ArrayList<InitContainer> blue,ArrayList<InitContainer> red){
        CombatText combatText = new CombatText();
        Battlefield battlefield = new Battlefield(blue,red,combatText);
        battlefield.init();
        while(battlefield.winner== Battlefield.GameState.UNDECIDED){
            battlefield.update();
        }
        combatText.addCombatText(battlefield.winner.toString());
        combatText.printCombatText();
    }

    private ArrayList<InitContainer> initHeroFromIds(ArrayList<Integer> heroIds) {
        ArrayList<InitContainer> containerArr = new ArrayList<>();
        for (int i = 0; i < heroIds.size(); i++) {
            JSONObject heroJSON = DataLoader.getIDObjectfromJSON(heroIds.get(i),this.heroJSONList,"id");
            Hero initHero = mapIdstoHeroes(heroIds.get(i),heroJSON);
            ArrayList<SpecialAbility> specialAbilities = addSpecialAbilitesToHero(initHero,heroJSON);
            ArrayList<Buff> buffs = addBuffsToHero(initHero,heroJSON);
            ArrayList<Talent> talents = new ArrayList<>();
            InitContainer container = new InitContainer(talents,buffs,specialAbilities,initHero);
            containerArr.add(container);
        }
        return containerArr;
    }

    private ArrayList<SpecialAbility> addSpecialAbilitesToHero(Hero hero,JSONObject heroJSON){
        ArrayList<Integer> specialIDJson = DataLoader.getJSONListfromJSON(heroJSON,"specialAbilities");
        ArrayList<SpecialAbility> abilities = initSpecialAbilitiesFromIds(specialIDJson);
        for(int j = 0; j<abilities.size();j++){
            abilities.get(j).init(hero,hero);
        }
        return abilities;
    }

    private ArrayList<Buff> addBuffsToHero(Hero hero,JSONObject heroJSON){
        ArrayList<Integer> idjson = DataLoader.getJSONListfromJSON(heroJSON,"buffs");
        ArrayList<Buff> buffs = getBuffsFromIds(idjson);
        for(Buff buff : buffs){
            buff.setTarget(hero);
            buff.setOrigin(hero);
        }
        return buffs;
    }

    private ArrayList<Buff> getBuffsFromIds(ArrayList<Integer> list){
        ArrayList<Buff> buffs = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            Buff buff = getBuffFromJSON(list.get(i));
            buff.setFromTalent(true);
            buffs.add(buff);
        }
        return buffs;
    }

    public Buff getBuffFromJSON(int id){
        JSONObject special = DataLoader.getIDObjectfromJSON(id,buffJSON,"id");
        Buff buff =  Buff.fromJSON(special);
        return buff;
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
                return new DummyNoobHeld(jsonObject);
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
