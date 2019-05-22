/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q.chess;
import standardchessgame.ChessPieces;

/**
 *
 * @author alinger2442
 */
import java.util.ArrayList;
public class BetzaChessPiece extends ChessPieces {
    String betza,name;
    ArrayList<Atom> atoms=new ArrayList<>();
    boolean royal;
    String[][] promoDB;
    int promotionRow;
    static final String[][] SHORTCUTS={{"K","B","R","Q","C","Z"},{"(WF)","F0","W0","(W0F0)","L","J"}};
    public static class BetzaException extends Exception{
        public BetzaException(){
        
        }
        public BetzaException(String message){
            super(message);
        }
    }
    public static class Atom {
        ArrayList<String>oper;
    char type;
    String mod;
    
    int leapX,leapY,stepCount,stepType;
    static final char[][] LEAPLIST = {{'O'}, {'W', 'F'}, {'D', 'N', 'A'}, {'H', 'L', 'J', 'G'}};

    public Atom(String atom) throws BetzaException{
        mod="";
        for(int c=0;c<atom.length();c++){
            if(Character.isUpperCase(atom.charAt(c))){
                type=atom.charAt(c);
                if(c!=atom.length()-1)stepCount=Integer.parseInt(atom.substring(c+1));
                else stepCount=1;
                c=atom.length();
            }
            else {
                mod+=atom.substring(c,c+1);
            }
        }
        if(mod.matches("[miuc]*"))mod+="a";
        
        //x >= y
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y <= x; y++) {
                if (LEAPLIST[x][y]==type) {
                    leapX=x;
                    leapY=y;
                }
            }
        }
        if(leapX==0&&leapY==0&&type!='O')throw new BetzaException("Atom \""+type+"\" unrecognized.");
        //steptype 1-ortho 2-diag, 3 oblique
    if(leapY==0)stepType=1;
        else if(leapX==leapY)stepType=2;
        else stepType=3;
    try{
        oper=parseOperator(mod,stepType);
        }catch(BetzaException e){
            System.err.println("Exception: "+e.getMessage()+"\nErroneous Atom: "+this.toString());
            oper=new ArrayList<>();
        }
    }
    public boolean verify(char atom,int count,boolean moved,String dir){
        boolean check;
        if(moved)check=!mod.contains("i");
        else check=!mod.contains("u");
        return(atom==type&&(count<=stepCount||stepCount==0)&&!mod.contains("m")&&check&&oper.contains(dir));
    }
    public static char convert(int x,int y){
        return LEAPLIST[Math.abs(x)][Math.abs(y)];
    }
    public String toString(){
        return mod+" "+leapX+" "+leapY+" "+stepCount+" "+stepType;
    } 
}
    public BetzaChessPiece(String betza, boolean p1,String name){
        super(p1);
        if(p1)promotionRow=0;
        else promotionRow=7;
        init(betza,name);
    }
    public boolean isRoyal(){
        return royal;
    }
    public String[][] getPromo(){
        return promoDB;
    }
    public boolean verify(char type,int count,String dir){
        for(int c=0;c<atoms.size();c++){
            if(atoms.get(c).verify(type,count,this.haveMoved(),dir))return true;
        }
        return false;
    }
    public void init(String betza,String name){
        this.name=name;
        this.betza=promo(betza);
        this.betza=shortcut(this.betza);
        atoms.clear();
        
        //royal check
        if(this.betza.contains("k")){
            royal=true;
            this.betza=this.betza.replaceAll("k","");
        }
        else royal=false;
        ArrayList<String> preAtoms=parseBetza(this.betza);
        for(int c=0;c<preAtoms.size();c++){
            try{
                atoms.add(new Atom(preAtoms.get(c)));
            }catch(BetzaException e){
                System.err.println("Exception: "+e.getMessage()+"\nErroneous Betza: "+preAtoms.get(c));
            }
        }
    }
    private String promo(String betza){
        if(betza.contains("*")){
            String[] items=betza.substring(betza.indexOf("*")+1).split(",");
            promoDB=new String[items.length][];
            for(int c=0;c<items.length;c++){
                promoDB[c]=items[c].split("/");
            }
            betza=betza.substring(0,betza.indexOf("*"));
        }
        else {
            promoDB=new String[1][1];
            promoDB[0][0]="";
            promotionRow=-1;
        }
        return betza;
    }
public ArrayList<String> checkDirection(int amount,int rowMod,int colMod, ArrayList<String> validMoves, int limit,int modality){
    int bWMod;
    if(limit==0)limit=-1;
        if(p1)bWMod=1;
        else bWMod=-1;
        if(pieces[startRow+amount*rowMod][startCol+amount*colMod]*bWMod>0||amount-1==limit)return validMoves;
        else if(pieces[startRow+amount*rowMod][startCol+amount*colMod]==0){
            if(modality!=-1){
                validMoves.add(Character.toString((char)(startCol+amount*colMod+97))+Integer.toString(8-(startRow+amount*rowMod)));
                if(((startRow+(amount+1)*rowMod>=0&&startRow+(amount+1)*rowMod<=7))&&((startCol+(amount+1)*colMod>=0&&startCol+(amount+1)*colMod<=7)))return checkDirection(amount+1,rowMod,colMod,validMoves, limit,modality);
                else return validMoves;
            }
            else return validMoves;
        }
        else {
            if(modality!=1)validMoves.add(Character.toString((char)(startCol+amount*colMod+97))+Integer.toString(8-(startRow+amount*rowMod)));
            return validMoves;
        }
}
public String toString(){
    return "Betza: "+betza+"@Name: "+name;
}
public static void output(ArrayList<String> oper,String mod){
    System.out.println("Mod:"+mod);
    for(String s:oper){
        System.out.println(s);
    }
}
public static void output(ArrayList<Atom> atoms,int f){
    for(Atom a:atoms){
        System.out.println(a);
    }
}
public ArrayList<String> displayMoves(){
    validMoves.clear();
    for(int c=0;c<atoms.size();c++){
        String[] data=atoms.get(c).toString().split(" ");
        String mod=data[0],stept=data[4];
        int x=Integer.parseInt(data[1]),y=Integer.parseInt(data[2]),step=Integer.parseInt(data[3]),modal=0;
        if(x!=0||y!=0){
            boolean uX=startRow-x>=0,dX=startRow+x<=7,lX=startCol-x>=0,rX=startCol+x<=7,uY=startRow-y>=0,dY=startRow+y<=7,lY=startCol-y>=0,rY=startCol+y<=7;
        //reverse direction for black (important for directional mod)
        //u becomes forward, d backward, l ccw, r cw
        if(!p1){
            boolean hold;
            hold=uX;
            uX=dX;
            dX=hold;
            hold=lX;
            lX=rX;
            rX=hold;
            hold=uY;
            uY=dY;
            dY=hold;
            hold=lY;
            lY=rY;
            rY=hold;
            x=-x;
            y=-y;
        }
        ArrayList<String> oper;
        try{
            oper=parseOperator(mod,Integer.parseInt(stept));
        }catch(BetzaException e){
            System.err.println("Exception: "+e.getMessage()+"\nErroneous Atom: "+atoms.get(c).toString());
            oper=new ArrayList<>();
        }
        
        if(!(oper.contains("i")&&this.haveMoved())&&!(oper.contains("u")&&!this.haveMoved())){
            if(oper.contains("m"))modal=1;
            else if(oper.contains("c"))modal=-1;
            switch(stept){
                case("1"):
                    if(uX&&oper.contains("f"))validMoves=checkDirection(1,-x,0,validMoves,step,modal);
                    if(dX&&oper.contains("b"))validMoves=checkDirection(1,x,0,validMoves,step,modal);
                    if(lX&&oper.contains("l"))validMoves=checkDirection(1,0,-x,validMoves,step,modal);
                    if(rX&&oper.contains("r"))validMoves=checkDirection(1,0,x,validMoves,step,modal);
                    break;
                case("2"):
                    if(uX&&lX&&oper.contains("fl"))validMoves=checkDirection(1,-x,-x,validMoves,step,modal);
                    if(uX&&rX&&oper.contains("fr"))validMoves=checkDirection(1,-x,x,validMoves,step,modal);
                    if(dX&&lX&&oper.contains("bl"))validMoves=checkDirection(1,x,-x,validMoves,step,modal);
                    if(dX&&rX&&oper.contains("br"))validMoves=checkDirection(1,x,x,validMoves,step,modal);
                    break;
                case("3"):
                if(lX&&uY&&oper.contains("fl"))validMoves=checkDirection(1,-y,-x,validMoves,step,modal);
                if(lY&&uX&&oper.contains("lf"))validMoves=checkDirection(1,-x,-y,validMoves,step,modal);
                if(rY&&uX&&oper.contains("rf"))validMoves=checkDirection(1,-x,y,validMoves,step,modal);
                if(rX&&uY&&oper.contains("fr"))validMoves=checkDirection(1,-y,x,validMoves,step,modal);
                if(lX&&dY&&oper.contains("bl"))validMoves=checkDirection(1,y,-x,validMoves,step,modal);
                if(lY&&dX&&oper.contains("lb"))validMoves=checkDirection(1,x,-y,validMoves,step,modal);
                if(rY&&dX&&oper.contains("rb"))validMoves=checkDirection(1,x,y,validMoves,step,modal);
                if(rX&&dY&&oper.contains("br"))validMoves=checkDirection(1,y,x,validMoves,step,modal);
                break;
            default: 
                Exception e=new BetzaException("StepType \""+stept+"\" unrecognized.");
                System.err.println("Exception: "+e.getMessage()+"\nErroneous Atom: "+atoms.get(c).toString());
        }
        }
    }
        else{
        validMoves.add(Character.toString((char)(startCol+97))+Integer.toString(8-(startRow)));
        }
        
    }
    return validMoves;
}
public static ArrayList<String> parseOperator(String mod,int stepType)throws BetzaException{
    ArrayList<String> oper=new ArrayList<>();
    int capMode=0,virMode=0;
    char car,nexcar;
    for(int c=0;c<mod.length();c++){
        car=mod.charAt(c);
        switch(car){
        case'i':
            virMode++;
            break;
        case'u':
            virMode--;
            break;
        case'm':
            capMode++;
            break;
        case'c':
            capMode--;
            break;
        default:
            switch(stepType){
                case 1:
                    switch(car){
                        case 'a':oper.add("f");
                        oper.add("b");
                        oper.add("l");
                        oper.add("r");
                        break;
                        case 'v':oper.add("f");
                        oper.add("b");
                    break;
                        case 's':
                        oper.add("l");
                        oper.add("r");
                    break;
                        case'f':
                        case'b':
                        case'l':
                        case'r': 
                        oper.add(Character.toString(car));
                    break;
                        default:
                        throw new BetzaException("Modifier \""+car+"\" could not be parsed.");    
                    }
                    break;
                case 2:
                    if(c==mod.length()-1){
                        switch(car){
                            case 'a':oper.add("fl");
                        oper.add("br");
                        oper.add("bl");
                        oper.add("fr");
                        break;
                            case 'f':oper.add("fl");
                                oper.add("fr");
                                break;
                            case 'b':oper.add("bl");
                                oper.add("br");
                                break;
                            case 'l':oper.add("fl");
                                oper.add("bl");
                                break;
                            case 'r':oper.add("br");
                                oper.add("fr");
                                break;
                            default:
                                throw new BetzaException("Modifier \""+car+"\" could not be parsed.");
                        }
                    }
                    else{
                        nexcar=mod.charAt(c+1);
                        switch(car){
                            case 'a':oper.add("fl");
                        oper.add("br");
                        oper.add("bl");
                        oper.add("fr");
                        break;
                            case 'f':
                                c++;
                                switch(nexcar){
                                case'l': oper.add("fl");
                                break;
                                case 'r': oper.add("fr");
                                break;
                                default: 
                                oper.add("fl");
                                oper.add("fr");
                                c--;
                                break;
                            }
                            break;
                                case 'b':
                                c++;
                                switch(nexcar){
                                case'l': oper.add("bl");
                                break;
                                case 'r': oper.add("br");
                                break;
                                default: oper.add("bl");
                                oper.add("br");
                                c--;
                                break;
                            }
                            break;
                                case 'l': oper.add("fl");
                                oper.add("bl");
                                break;
                                case 'r':oper.add("fr");
                                oper.add("br");
                                break;
                                default:
                                    throw new BetzaException("Modifier \""+car+"\" could not be parsed.");
                        }
                    }
                    break;
                case 3:
                    if(c==mod.length()-1){
                        switch(car){
                            case 'a':oper.add("fl");
                        oper.add("br");
                        oper.add("bl");
                        oper.add("fr");
                        oper.add("lf");
                        oper.add("rb");
                        oper.add("lb");
                        oper.add("rf");
                        break;
                            case 'f':oper.add("fl");
                                oper.add("fr");
                                break;
                            case 'b':oper.add("bl");
                                oper.add("br");
                                break;
                            case 'l':oper.add("fl");
                                oper.add("bl");
                                break;
                            case 'r':oper.add("br");
                                oper.add("fr");
                                break;
                            case 'v':oper.add("lf");
                                oper.add("rf");
                            oper.add("lb");
                                oper.add("rb");
                                break;
                            case 's':oper.add("fl");
                                oper.add("fr");
                            oper.add("bl");
                                oper.add("br");
                                break;
                            default:
                                throw new BetzaException("Modifier \""+car+"\" could not be parsed.");
                        }
                    }
                    else{
                        nexcar=mod.charAt(c+1);
                        switch(car){
                            case 'a':oper.add("fl");
                        oper.add("br");
                        oper.add("bl");
                        oper.add("fr");
                        oper.add("lf");
                        oper.add("rb");
                        oper.add("lb");
                        oper.add("rf");
                        break;
                            case 'f':switch(nexcar){
                                case'h':
                                    oper.add("fl");
                                oper.add("fr");
                                oper.add("lf");
                                oper.add("rf");
                                c++;
                                break;
                            
                            case's':
                                oper.add("fl");
                                oper.add("fr");
                                c++;
                                break;
                            case'l':
                            case'r': 
                                oper.add(mod.substring(c,c+2));
                                c++;
                            break;
                            default:oper.add("lf");
                                    oper.add("rf");
                                    break;
                        }
                            break;
                            case 'b':switch(nexcar){
                                case'h':
                                    oper.add("bl");
                                oper.add("br");
                                oper.add("bf");
                                oper.add("bf");
                                c++;
                                break;
                            
                            case's':
                                oper.add("bl");
                                oper.add("br");
                                c++;
                                break;
                            case'l':
                            case'r': 
                                oper.add(mod.substring(c,c+2));
                                c++;
                            break;
                            default:oper.add("lb");
                                    oper.add("rb");
                                    break;
                        }
                            break;
                        case 'l':switch(nexcar){
                                case'h':
                                    oper.add("fl");
                                oper.add("bl");
                                oper.add("lf");
                                oper.add("lb");
                                c++;
                                break;
                            
                            case'v':
                                oper.add("lf");
                                oper.add("lb");
                                c++;
                                break;
                            case'f':
                            case'b': 
                                oper.add(mod.substring(c,c+2));
                                c++;
                            break;
                            default:oper.add("fl");
                                    oper.add("bl");
                                    break;
                        }
                            break;
                                case 'r':switch(nexcar){
                                case'h':
                                    oper.add("fr");
                                oper.add("br");
                                oper.add("rf");
                                oper.add("rb");
                                c++;
                                break;
                            
                            case'v':
                                oper.add("rf");
                                oper.add("rb");
                                c++;
                                break;
                            case'f':
                            case'b': 
                                oper.add(mod.substring(c,c+2));
                                c++;
                            break;
                            default:oper.add("fr");
                                    oper.add("br");
                                    break;
                        }
                            break;
                                default:
                                    throw new BetzaException("Modifier \""+car+"\" could not be parsed.");
                        }
                    }
                    break;
                default: 
                    throw new BetzaException("StepType \""+stepType+"\" unrecognized.");
            }
            break;
            
        }
    }
    if(virMode==1)oper.add("i");
    else if(virMode==-1)oper.add("u");
    if(capMode==1)oper.add("m");
    else if(capMode==-1)oper.add("c");
    
    return oper;
}
private static String shortcut(String betza){
        for(int c=0;c<SHORTCUTS[0].length;c++)betza=betza.replaceAll(SHORTCUTS[0][c],SHORTCUTS[1][c]);
        while(betza.contains("((")){
            betza=betza.substring(0,betza.indexOf("((")).concat(betza.substring(betza.indexOf("((")).replaceFirst("\\)",""));
            betza=betza.replaceFirst("\\(\\(","(");
        }
        return betza;
    }
public ArrayList<String> parseBetza(String betza){
        ArrayList<String> inner,output=new ArrayList<>();
        char car;
        String mod="",atom="";
        for(int c=0;c<betza.length();c++){
            car=betza.charAt(c);
            if(Character.isLowerCase(car)){
                if(!atom.isEmpty()){
                    output.add(mod.concat(atom));
                    mod="";
                    atom="";
                }
                mod+=Character.toString(car);
            }
            else if(car=='('){
                inner=parseBetza(betza.substring(c+1,betza.indexOf(")",c)));
                for(int d=0;d<inner.size();d++)output.add(mod.concat(inner.get(d)));
                mod="";
                c=betza.indexOf(")",c);
            }
            else if(Character.isUpperCase(car)){
                if(!atom.isEmpty()){
                    output.add(mod.concat(atom));
                    mod="";
                }
                atom=Character.toString(car);
            }
            else if(Character.isDigit(car))atom+=Character.toString(car);
        }
        if(!atom.isEmpty())output.add(mod.concat(atom));
        return output;
    }
public void atoms(){
    for(int c=0;c<atoms.size();c++){
        System.out.println(atoms.get(c));
    }
}
}