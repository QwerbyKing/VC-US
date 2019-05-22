/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q.chess;

/**
 *
 * @author alinger2442
 */
public class WOTNChessPiece extends BetzaChessPiece{
    int level,alignment;
    protected static final String[][] CLASSLIST={{"","fcF1fmW1fmiW2",""}, {"W1fbD1","","A1D1"},   {"N1","","F0"},         {"N1W1","","D1F0"},     {"","W0",""},   {"N0","","F1L1D1"},         {"","F0N1",""},     {"W0N1","","W0F0"},         {"","N0F0",""},     {"","N0W0",""},     {"","W1F1k",""}};
    protected static final String[][] NAMELIST ={{"","Pawn",""},          {"Squire","","Deacon"}, {"Knight","","Bishop"}, {"Duke","","Warpriest"},{"","Rook",""}, {"Nightrider","","Magus"},  {"","Princess",""}, {"Chancellor","","Queen"},  {"","Champion",""}, {"","Unicorn",""},  {"","King",""}};
    public WOTNChessPiece(int level,int alignment, boolean p1){
        super(CLASSLIST[level][alignment], p1,NAMELIST[level][alignment]);
        this.level=level;
        this.alignment=alignment;
    }
    public void levelUp(int alignment){
        this.alignment=alignment;
        level++;
    }
    public static String find(String name){
        for(int c=0;c<NAMELIST.length;c++){
            for(int d=0;d<NAMELIST[c].length;d++){
                if(name.equals(NAMELIST[c][d]))return c+"-"+d;
            }
        }
        return "";
    }
    public static void modeSet(boolean advanced){
        if(advanced){
            CLASSLIST[3][1]="W1D1F1";
            NAMELIST[3][1]="Elephant";
            CLASSLIST[4][0]="N1W1fbD1";
            NAMELIST[4][0]="Cavalier";
            CLASSLIST[4][2]="F1A1D1";
            NAMELIST[4][2]="Cleric";
            CLASSLIST[5][1]="W1F1A1D1";
            NAMELIST[5][1]="Blacksmith";
            CLASSLIST[6][0]="N1A1D1";
            NAMELIST[6][0]="Archduke";
            CLASSLIST[6][2]="F0L1";
            NAMELIST[6][2]="Cardinal";
        }
        else{
            CLASSLIST[3][1]="";
            NAMELIST[3][1]="";
            CLASSLIST[4][0]="";
            NAMELIST[4][0]="";
            CLASSLIST[4][2]="";
            NAMELIST[4][2]="";
            CLASSLIST[5][1]="";
            NAMELIST[5][1]="";
            CLASSLIST[6][0]="";
            NAMELIST[6][0]="";
            CLASSLIST[6][2]="";
            NAMELIST[6][2]="";
        }
    }
    public int getAlignment(){
        return alignment;
    }
    public int getLevel(){
        return level;
    }
    
}
