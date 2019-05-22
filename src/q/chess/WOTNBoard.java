/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q.chess;

/**
 *
 * @author Student
 */
import java.util.ArrayList;
public class WOTNBoard extends ChessBoard{
    public WOTNBoard(boolean advanced){
        WOTNChessPiece.modeSet(advanced);
        pieces=new int[8][8];
        board=new String[][]{
            {"RookB","KnightB","BishopB","QueenB","KingB","BishopB","KnightB","RookB"},
            {"PawnB","PawnB","PawnB","PawnB","PawnB","PawnB","PawnB","PawnB"},
            {"-","-","-","-","-","-","-","-"},
            {"-","-","-","-","-","-","-","-"},
            {"-","-","-","-","-","-","-","-"},
            {"-","-","-","-","-","-","-","-"},
            {"PawnW","PawnW","PawnW","PawnW","PawnW","PawnW","PawnW","PawnW"},
            {"RookW","KnightW","BishopW","QueenW","KingW","BishopW","KnightW","RookW"}
        };
        boolean player=false;
        int mod=-1,count=0;
        for(int c=0;c<8;c++){
            String s=board[0][c];
            String[] data=WOTNChessPiece.find(s.substring(0,s.length()-1)).split("-");
            black.add(new WOTNChessPiece(Integer.parseInt(data[0]),Integer.parseInt(data[1]),false));
            count++;
            pieces[0][c]=count*-1;
        }
        for(int c=0;c<8;c++){
            String s=board[1][c];
            String[] data=WOTNChessPiece.find(s.substring(0,s.length()-1)).split("-");
            black.add(new WOTNChessPiece(Integer.parseInt(data[0]),Integer.parseInt(data[1]),false));
            count++;
            pieces[1][c]=count*-1;
        }
        count=0;
        for(int c=0;c<8;c++){
            String s=board[6][c];
            String[] data=WOTNChessPiece.find(s.substring(0,s.length()-1)).split("-");
            white.add(new WOTNChessPiece(Integer.parseInt(data[0]),Integer.parseInt(data[1]),true));
            count++;
            pieces[6][c]=count;
        }
        for(int c=0;c<8;c++){
            String s=board[7][c];
            String[] data=WOTNChessPiece.find(s.substring(0,s.length()-1)).split("-");
            white.add(new WOTNChessPiece(Integer.parseInt(data[0]),Integer.parseInt(data[1]),true));
            count++;
            pieces[7][c]=count;
        }
        WOTNChessPiece.updateBoard(pieces);
        royalRowB=0;
        royalColB=4;
        royalRowW=7;
        royalColW=4;
        whiteSize=16;
        blackSize=16;
        royalCount();
    }
    public void promotion(int lastPiece,String betza,String name,int alignment){
        super.promotion(lastPiece,betza,name);
        if(lastPiece>0)((WOTNChessPiece)white.get(lastPiece-1)).levelUp(alignment);
        else if(lastPiece<0)((WOTNChessPiece)black.get(-lastPiece-1)).levelUp(alignment);
        
    }
    public boolean coronation(boolean p1){
        ArrayList<BetzaChessPiece> pieceList,enemyList;
        int bWMod;
        if(p1){
            pieceList=white;
            enemyList=black;
            bWMod=1;
        }
        else {
            pieceList=black;
            enemyList=white;
            bWMod=-1;
        }
        for(int c=0;c<pieceList.size();c++){
            if(pieceList.get(c).toString().split("@")[1].substring(6).equals("Unicorn")){
                findPosition(!p1,c);
                WOTNChessPiece.updateBoard(pieces);
                if(nightriderCheck(true,bWMod,enemyList))return true;
            }
        }
        return false;
    }
    @Override
    public boolean goodMove(boolean p1, String move){
        int newRow=8-Integer.parseInt(move.substring(1)),newCol=(int)move.charAt(0)-97,bWMod;
        int current=pieces[newRow][newCol];
        boolean regular=super.goodMove(p1,move);
        ArrayList<BetzaChessPiece> pieceList,enemyList;
        if((current>0&&!p1)||(current<0&&p1)||current==0){
            //System.out.println("No Sir");
            return false;
        }
        if(p1){
            pieceList=white;
            enemyList=black;
            bWMod=1;
        }
        else {
            pieceList=black;
            enemyList=white;
            bWMod=-1;
        }
        if(((WOTNChessPiece)pieceList.get(Math.abs(current)-1)).getLevel()!=9)return regular;
        else{
            return nightriderCheck(false,bWMod,enemyList);
        }
    }
    public boolean nightriderCheck(boolean check,int bWMod,ArrayList<BetzaChessPiece> enemies){
        
            int[][] tempboard=new int[8][8];
            for(int c=0;c<8;c++)for(int d=0;d<8;d++)tempboard[c][d]=pieces[c][d];
            int[] dirRow={1,1,2,2,-1,-1,-2,-2,1,-1,0,0};
            int[] dirCol={2,-2,1,-1,2,-2,1,-1,0,0,1,-1};
            
            boolean checkReturn=false,uX=startRow-2>=0,dX=startRow+2<=7,lX=startCol-2>=0,rX=startCol+2<=7,uY=startRow-1>=0,dY=startRow+1<=7,lY=startCol-1>=0,rY=startCol+1<=7;
            for(int c=0;c<12;c++){
                int rowMod=dirRow[c];
                int colMod=dirCol[c];
                if(startRow+rowMod>=0&&startRow+rowMod<=7&&startCol+colMod>=0&&startCol+colMod<=7){
                if(nightriderCheck(check,bWMod,enemies,1,rowMod,colMod,tempboard)){
                    if(check)return true;
                    else checkReturn=true;
                }
                }
            }
            return checkReturn;
    }
    public boolean nightriderCheck(boolean check,int bWMod,ArrayList<BetzaChessPiece> enemies,int amount, int rowMod, int colMod,int[][]tempboard){
     int position=tempboard[startRow+amount*rowMod][startCol+amount*colMod]*bWMod;
        if(position>0)return false;
        else if(position==0){
            if(startRow+(amount+1)*rowMod>=0&&startRow+(amount+1)*rowMod<=7&&startCol+(amount+1)*colMod>=0&&startCol+(amount+1)*colMod<=7)return nightriderCheck(check,bWMod,enemies,amount+1,rowMod,colMod,tempboard);
            else return false;
        }
        else {
            if(((WOTNChessPiece)enemies.get((-position)-1)).getLevel()>=4){
                if(!check)validMoves.add(Character.toString((char)(startCol+amount*colMod+97))+Integer.toString(8-(startRow+amount*rowMod)));
                return true;
            }
            return false;
        }   
    }
    public void setPromo(int index){
        WOTNChessPiece piece;
        if(index>0)piece=(WOTNChessPiece)white.get(index-1);
        else if(index<0)piece=(WOTNChessPiece)black.get(-index-1);
        else return;
        WOTNVars.level=piece.getLevel();
        WOTNVars.alignment=piece.getAlignment();
        
    }
    @Override
    public int playMove(String move, boolean p1){
        int newRow=8-Integer.parseInt(move.substring(1)),newCol=(int)move.charAt(0)-97,bWMod,position=Math.abs(pieces[startRow][startCol])-1,targetLevel,pieceLevel;
        ArrayList<BetzaChessPiece> pieceList;
        boolean wimpyPromo=false,aggroPromo=false;
        if(p1){
            pieceList=white;
            bWMod=1;
        }
        else {
            pieceList=black;
            bWMod=-1;
        }
        pieceLevel=((WOTNChessPiece)pieceList.get(position)).getLevel();
        
        String target=board[newRow][newCol];
        super.playMove(move,p1);
        if(pieceLevel==10)return 0;
            if(!p1)wimpyPromo=newRow>=5+pieceLevel;
            else wimpyPromo=newRow<=2-pieceLevel;
            
            if(!target.equals("-")){
                targetLevel=Integer.parseInt(WOTNChessPiece.find(target.substring(0,target.length()-1)).split("-")[0]);
                aggroPromo=(targetLevel+1)*2>=pieceLevel+1;
            }
            if(aggroPromo||wimpyPromo)return (position+1)*bWMod;
            else return 0;
        
    }
}
