package backend.app.Buffs;

import backend.app.BasicStats;

import java.util.ArrayList;

public class Talent {
    private ArrayList<Buff> buffs;
    private String name;
    private BasicStats stats;
    private int id;
    private TalentType type;

    public enum TalentType{
        NORMAL,INSIGNIA,LUXURYINSIGNIA,ENCHANTMENT
    }
}
