/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standardchessgame;

/**
 *
 * @author alinger2442
 */
import java.util.ArrayList;
public class ChessPieces {
    public static int[][] pieces = new int [8][8];
    public static int startRow,startCol;
    public ArrayList<String> validMoves;
    public final boolean p1;
    public boolean haveMoved,dead;
    
    public ChessPieces(boolean play1){
        p1=play1;
        validMoves=new ArrayList<String>();
        dead=false;
        haveMoved=false;
    }
    public void kill(){
        dead=true;
    }
    public void move(){
        haveMoved=true;
    }
    public boolean haveMoved(){
        return haveMoved;
    }
    public boolean isDead(){
        return dead;
    }
    public static void updatePosition(int row, int col){
        startRow=row;
        startCol=col;
    }
    public static void updateBoard(int[][] board){
        for(int c=0;c<8;c++)for(int d=0;d<8;d++)pieces[c][d]=board[c][d];
    }
    public ArrayList<String> displayMoves(){
     return validMoves;   
    }
    public ArrayList<String> checkDirection(int amount,int rowMod,int colMod, ArrayList<String> validMoves){
        int bWMod;
        if(p1)bWMod=1;
        else bWMod=-1;
        if(pieces[startRow+amount*rowMod][startCol+amount*colMod]*bWMod>0)return validMoves;
        else if((pieces[startRow+amount*rowMod][startCol+amount*colMod]==0||Math.abs(pieces[startRow+amount*rowMod][startCol+amount*colMod])>16)&&((startRow+amount*rowMod>0&&startRow+amount*rowMod<7)||rowMod==0)&&((startCol+amount*colMod>0&&startCol+amount*colMod<7)||colMod==0)){
            validMoves.add(Character.toString((char)(startCol+amount*colMod+97))+Integer.toString(8-(startRow+amount*rowMod)));
            return checkDirection(amount+1,rowMod,colMod,validMoves);
        }
        else {
            validMoves.add(Character.toString((char)(startCol+amount*colMod+97))+Integer.toString(8-(startRow+amount*rowMod)));
            return validMoves;
        }
        }
    public class King extends ChessPieces{
 
        public King(boolean play1){
            super(play1);
        }
        public ArrayList<String> displayMoves(){
            boolean left=startCol>0,up=startRow>0,right=startCol<7,down=startRow<7;
            int bWMod=-1;
            if(p1)bWMod=1;
            validMoves.clear();
            if(up)if(pieces[startRow-1][startCol]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+97))+Integer.toString((8-(startRow-1))));
            if(down)if(pieces[startRow+1][startCol]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+97))+Integer.toString((8-(startRow+1))));
            if(left)if(pieces[startRow][startCol-1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow))));
            if(right)if(pieces[startRow][startCol+1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow))));
            if(up&&left)if(pieces[startRow-1][startCol-1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow-1))));
            if(up&&right)if(pieces[startRow-1][startCol+1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow-1))));
            if(down&&left)if(pieces[startRow+1][startCol-1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow+1))));
            if(down&&right)if(pieces[startRow+1][startCol+1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow+1))));
            return validMoves;
        }
    }
    public class Queen extends ChessPieces{
        public Queen(boolean play1){
            super(play1);
        }
         public ArrayList<String> displayMoves(){
            boolean left=startCol>0,up=startRow>0,right=startCol<7,down=startRow<7;
            validMoves.clear();
            if(up)validMoves=checkDirection(1,-1,0,validMoves);
            if(down)validMoves=checkDirection(1,1,0,validMoves);
            if(left)validMoves=checkDirection(1,0,-1,validMoves);
            if(right)validMoves=checkDirection(1,0,1,validMoves);
            if(up&&left)validMoves=checkDirection(1,-1,-1,validMoves);
            if(up&&right)validMoves=checkDirection(1,-1,1,validMoves);
            if(down&&left)validMoves=checkDirection(1,1,-1,validMoves);
            if(down&&right)validMoves=checkDirection(1,1,1,validMoves);
            return validMoves;
        }
    }
    public class Rook extends ChessPieces{
        public Rook(boolean play1){
            super(play1);
        }
         public ArrayList<String> displayMoves(){
            boolean left=startCol>0,up=startRow>0,right=startCol<7,down=startRow<7;
            validMoves.clear();
            if(up)validMoves=checkDirection(1,-1,0,validMoves);
            if(down)validMoves=checkDirection(1,1,0,validMoves);
            if(left)validMoves=checkDirection(1,0,-1,validMoves);
            if(right)validMoves=checkDirection(1,0,1,validMoves);
            return validMoves;
        }
    }
    public class Bishop extends ChessPieces{
        public Bishop(boolean play1){
            super(play1);
        }
         public ArrayList<String> displayMoves(){
            boolean left=startCol>0,up=startRow>0,right=startCol<7,down=startRow<7;
            validMoves.clear();            
            if(up&&left)validMoves=checkDirection(1,-1,-1,validMoves);
            if(up&&right)validMoves=checkDirection(1,-1,1,validMoves);
            if(down&&left)validMoves=checkDirection(1,1,-1,validMoves);
            if(down&&right)validMoves=checkDirection(1,1,1,validMoves);
            return validMoves;
        }
    }
    public class Knight extends ChessPieces{
       public Knight(boolean play1){
            super(play1);
        } 
        public ArrayList<String> displayMoves(){
            boolean left1=startCol>0,left2=startCol>1,up1=startRow>0,up2=startRow>1,right1=startCol<7,right2=startCol<6,down1=startRow<7,down2=startRow<6;
            int bWMod;
            if(p1)bWMod=1;
            else bWMod=-1;
            validMoves.clear();
            if(left2&&up1)if(pieces[startRow-1][startCol-2]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-2+97))+Integer.toString((8-(startRow-1))));
            if(left1&&up2)if(pieces[startRow-2][startCol-1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow-2))));
            if(right1&&up2)if(pieces[startRow-2][startCol+1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow-2))));
            if(right2&&up1)if(pieces[startRow-1][startCol+2]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+2+97))+Integer.toString((8-(startRow-1))));
            if(left2&&down1)if(pieces[startRow+1][startCol-2]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-2+97))+Integer.toString((8-(startRow+1))));
            if(left1&&down2)if(pieces[startRow+2][startCol-1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow+2))));
            if(right1&&down2)if(pieces[startRow+2][startCol+1]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow+2))));
            if(right2&&down1)if(pieces[startRow+1][startCol+2]*bWMod<=0)validMoves.add(Character.toString((char)(startCol+2+97))+Integer.toString((8-(startRow+1))));  
            return validMoves;
        }
    }
    public class Pawn extends ChessPieces{
        
       public Pawn(boolean play1){
            super(play1);
        }
        public ArrayList<String> displayMoves(){
            int bWMod;
            boolean left=startCol>0,right=startCol<7;
            validMoves.clear();
            if(p1){
                bWMod=1;
            }
            else {
                bWMod=-1;
            }
            if(left)if(pieces[startRow-1*bWMod][startCol-1]*bWMod<0)validMoves.add(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow-1*bWMod))));
            if(right)if(pieces[startRow-1*bWMod][startCol+1]*bWMod<0)validMoves.add(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow-1*bWMod))));
            if(pieces[startRow-1*bWMod][startCol]==0){
                validMoves.add(Character.toString((char)(startCol+97))+Integer.toString((8-(startRow-1*bWMod))));
                if((!haveMoved)&&pieces[startRow-2*bWMod][startCol]==0)validMoves.add(Character.toString((char)(startCol+97))+Integer.toString((8-(startRow-2*bWMod))));    
            }
            return validMoves;
        }
    }
}
