package backend.app.Buffs;

import backend.app.BasicStats;
import backend.app.DataLoader;
import backend.app.SpecialAbilities.SpecialAbility;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Talent {

    @Getter @Setter private String name;
    @Getter @Setter private int attack;
    @Getter @Setter private int hp;
    @Getter @Setter private BasicStats stats;
    @Getter @Setter private int id;
    @Getter @Setter private TalentType type;
    @Getter private final ArrayList<Integer> specialAbilityIds = new ArrayList<>();
    @Getter private final ArrayList<SpecialAbility> specialAbilities = new ArrayList<>();
    @Getter private final ArrayList<TalentType> possibleTypes = new ArrayList<>();

    public enum TalentType{
        NORMAL,INSIGNIA,LUXURYINSIGNIA,ENCHANTMENT
    }

    public Talent(){}
    public Talent(JSONObject data){
        this( (int)(long) data.get("id"),
                (String)data.get("name"),
                (int)(long) data.get("hp"),
                (int)(long) data.get("attack"),
                DataLoader.getJSONListfromJSONString(data,"type"),
                DataLoader.getJSONListfromJSON(data,"specialAbilities"));
    }

    public Talent (int id,String name, int hp, int attack,ArrayList<String> type,ArrayList<Integer> specialAbilityIds){
        this.id = id;
        this.name = name;
        this.attack = attack;
        this.hp = hp;
        for(int i = 0;i<type.size();i++){
            this.possibleTypes.add(TalentType.valueOf(type.get(i)));
        }
        for(int i = 0;i<specialAbilityIds.size();i++){
            this.specialAbilityIds.add(specialAbilityIds.get(i));
        }
    }
}
