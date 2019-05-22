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
import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;
public class ChessBoard {  
    public String[][] board;
    int[][] pieces;
    int startRow=0,startCol=0,royalRowW,royalRowB,royalColB,royalColW,whiteSize,blackSize,royalCountW,royalCountB;
    public ArrayList<String> validMoves = new ArrayList<>();
    protected ArrayList<BetzaChessPiece> white=new ArrayList<>();
    protected ArrayList<BetzaChessPiece> black=new ArrayList<>();
    private static final String[] BETZAS=new String[26];
    Scanner reader;
    public ChessBoard(){
        //fake constructor 
    }
    public ChessBoard(int no) throws FileNotFoundException{
        board=new String[8][];
        pieces=new int[8][8];
        for(int c=0;c<26;c++){
           File betzer=new File("game\\"+Character.toString((char)(c+65))+".txt");
           if(betzer.exists()){
               reader=new Scanner(betzer);
               BETZAS[c]=reader.nextLine();
           }
           else BETZAS[c]="";
        }
        reader=new Scanner(new File("game\\board.txt"));
        int whit=0,blac=0;
        for(int c=0;c<8;c++){
            board[c]=toStringArray(reader.nextLine().toCharArray());
            for(int d=0;d<8;d++){
                if(Character.isUpperCase(board[c][d].charAt(0))){
                    board[c][d]+="W";
                    whit++;
                    pieces[c][d]=whit;
                    white.add(new BetzaChessPiece(BETZAS[(int)board[c][d].charAt(0)-65],true,board[c][d]));
                    if(white.get(white.size()-1).isRoyal()){
                        royalRowW=c;
                        royalColW=d;
                        royalCountW++;
                    }
                }
                else if(Character.isLowerCase(board[c][d].charAt(0))){
                    board[c][d]+="B";
                    blac++;
                    pieces[c][d]=-blac;
                    black.add(new BetzaChessPiece(BETZAS[(int)board[c][d].charAt(0)-97],false,board[c][d]));
                    if(black.get(black.size()-1).isRoyal()){
                        royalRowB=c;
                        royalColB=d;
                        royalCountB++;
                    }
                }
            }
        }
        BetzaChessPiece.updateBoard(pieces);
    }
    private String[] toStringArray(char[] array){
        String[] newArray=new String[array.length];
        for(int c=0;c<array.length;c++){
            newArray[c]=Character.toString(array[c]);
        }
        return newArray;
    }
    public boolean goodMove(boolean p1,String move){
        int newRow=8-Integer.parseInt(move.substring(1));
        int newCol=(int)move.charAt(0)-97;
        int current=pieces[newRow][newCol];
        if((current>0&&!p1)||(current<0&&p1)||current==0)return false;
        else{
            startRow=newRow;
            startCol=newCol;
            BetzaChessPiece.updatePosition(newRow,newCol);
            getPiece(p1,false);
            return !validMoves.isEmpty();
        }
    }
    public boolean moveSelect(String move){
        return validMoves.contains(move);
    }
    public String[][] getPromo(int lastPiece){
        if(lastPiece>0)return white.get(lastPiece-1).getPromo();
        else if(lastPiece<0)return black.get(-lastPiece-1).getPromo();
        else return new String[1][1];    
    }
    public void promotion(int index,String betza,String name){
        if(index==0)return;
        ArrayList<BetzaChessPiece> pieceList;
        if(index>0)pieceList=white;
        else pieceList=black;
        int newIndex=Math.abs(index)-1;
        pieceList.get(newIndex).init(betza,name);
        setName(index,name);
    }
    private void setName(int index,String name){
        for(int c=0;c<8;c++){
            for(int d=0;d<8;d++){
                if(pieces[c][d]==index){
                    board[c][d]=name;
                }
            }
        }
    }
    public void royalCount(){
        royalCountB=0;
        for(BetzaChessPiece b:black){
            if(b.isRoyal())royalCountB++;
        }
        royalCountW=0;
        for(BetzaChessPiece w:white){
            if(w.isRoyal())royalCountW++;
        }
    }
    public void removeIllegalMoves(boolean p1){
        ArrayList<BetzaChessPiece> pieceList;
        if(p1)pieceList=white;
        else pieceList=black;
        int counter=0,row,col;
        int[][] tempboard=new int[8][8];
        while(counter<validMoves.size()){
        //sets alternate board to be changed, without altering main board
            for(int c=0;c<8;c++)for(int d=0;d<8;d++)tempboard[c][d]=pieces[c][d];
            row=8-Integer.parseInt(validMoves.get(counter).substring(1));
            col=(int)validMoves.get(counter).charAt(0)-97;
            //changes alternate board to what it would be if the move went through
            tempboard[row][col]=tempboard[startRow][startCol];
            tempboard[startRow][startCol]=0;
            if(!pieceList.get(Math.abs(pieces[startRow][startCol])-1).isRoyal()){
                if(p1){
                    row=royalRowW;
                    col=royalColW;
                }
                else{
                    row=royalRowB;
                    col=royalColB;
                }
            }
            if(positionCheck(p1,row,col,tempboard))validMoves.remove(counter);
            else counter++;
        }
    }
    
    private boolean positionCheck(boolean p1,int row, int col, int[][] tempboard){
        if(p1&&royalCountW>1||!p1&&royalCountB>1)return false;
        ArrayList<BetzaChessPiece> enemyPieceList;
        int bWMod;
        if(p1){
            enemyPieceList=black;
            bWMod=1;
        }
        else {
            enemyPieceList=white;
            bWMod=-1;
        }
        for (int x = 1; x < 4; x++) {
            for (int y = 0; y <= x; y++) {
                if(checkHub(bWMod,row,col,tempboard,x,y,enemyPieceList))return true;
            }
        }
        return false;
    }
            
    private boolean checkHub(int bWMod,int row, int col, int[][] tempboard, int x,int y,ArrayList<BetzaChessPiece> enemies){
        int[][] directions=new int[2][];
        String[]operList;
        char type=BetzaChessPiece.Atom.convert(x, y);
        if(y==0){
            if(bWMod==-1)operList=new String[]{"f","b","l","r"};
            else operList=new String[]{"b","f","r","l"};
            int[]rowDir={x,-x,0,0};
            int[]colDir={0,0,x,-x};
            directions[0]=rowDir;
            directions[1]=colDir;
        }
        else if(x==y){
            if(bWMod==-1)operList=new String[]{"fl","br","fr","bl"};
            else operList=new String[]{"br","fl","bl","fr"};
            int[]rowDir={x,-x,x,-x};
            int[]colDir={x,-x,-x,x};
            directions[0]=rowDir;
            directions[1]=colDir;
        }
        else{
            if(bWMod==-1)operList=new String[]{"lf","rb","rf","lb","fl","br","bl","fr"};
            else operList=new String[]{"rb","lf","lb","rf","br","fl","fr","bl"};
            int[]rowDir={x,-x,x,-x,y,-y,-y,y};
            int[]colDir={y,-y,-y,y,x,-x,x,-x};   
            directions[0]=rowDir;
            directions[1]=colDir;
        }
        for(int c=0;c<directions[0].length;c++){
            if(row+directions[0][c]>=0&&row+directions[0][c]<=7&&col+directions[1][c]>=0&&col+directions[1][c]<=7)if(checkDirection(bWMod,operList[c],enemies,row,col,1,directions[0][c],directions[1][c],type,tempboard))return true;
        }
        return false;
    }
     
    private boolean checkDirection(int bWMod,String dir,ArrayList<BetzaChessPiece> enemies,int row, int col, int amount, int rowMod, int colMod, char type,int[][]tempboard){
        int position=tempboard[row+amount*rowMod][col+amount*colMod]*bWMod;
        if(position>0)return false;
        else if(position==0){
            if(row+(amount+1)*rowMod>=0&&row+(amount+1)*rowMod<=7&&col+(amount+1)*colMod>=0&&col+(amount+1)*colMod<=7)return checkDirection(bWMod,dir,enemies,row,col,amount+1,rowMod,colMod,type,tempboard);
            else return false;
        }
        else return enemies.get((-position)-1).verify(type,amount,dir);
    }
    private void getPiece(boolean p1,boolean check){
        //sets list of moves of piece on chosen position so board can output to user
        ArrayList<String> totalMoves= new ArrayList<String>();
        int limit;
        if(p1)limit=white.size();
        else limit=black.size();
        for(int c=0;c<limit;c++){
            validMoves.clear();
            if(!check)c=Math.abs(pieces[startRow][startCol])-1;
            else findPosition(p1,c);
            if(p1&&!white.get(c).isDead()){
                validMoves.addAll(white.get(c).displayMoves());
            }
            else if(!p1&&!black.get(c).isDead())validMoves.addAll(black.get(c).displayMoves());
            //removes moves that violate check
            removeIllegalMoves(p1);
            
            if(!check)return;
            totalMoves.addAll(validMoves);
        }
        validMoves.clear();
        validMoves.addAll(totalMoves);
        
    }
    public int checkVictory(boolean p1){
        int[][] tempboard = new int[8][8];
        int row,col,bWMod;
        boolean inCheck=false;
        String playnum;
        ArrayList<BetzaChessPiece> enemyPieceList;
        if(p1){
            row=royalRowB;
            col=royalColB;
            enemyPieceList=white;
            bWMod=-1;
        }
        else {
            row=royalRowW;
            col=royalColW;
            enemyPieceList=black;
            bWMod=1;
        }
        
        for(int c=0;c<8;c++)for(int d=0;d<8;d++)tempboard[c][d]=pieces[c][d];
        validMoves.clear();
        getPiece(!p1,true);
        if(validMoves.isEmpty()){
            for (int x = 1; x < 4; x++) {
                for (int y = 0; y <= x; y++) {
                    inCheck=inCheck||checkHub(bWMod,row,col,tempboard,x,y,enemyPieceList);
                }
            }
              //System.out.println("Game Over! " + playnum + " wins!");
            if(inCheck)return 1;
            else return 0;
        }
        else return -1;
    }
    
    public int playMove(String move, boolean p1){
        int newRow=8-Integer.parseInt(move.substring(1)),newCol=(int)move.charAt(0)-97;
        ArrayList<BetzaChessPiece> enemyList,allyList;
        if(p1){
            enemyList=black;
            allyList=white;
        }
        else {
            enemyList=white;
            allyList=black;
        }
        if(pieces[newRow][newCol]!=0)enemyList.get(Math.abs(pieces[newRow][newCol])-1).kill();
        board[newRow][newCol]=board[startRow][startCol];
        pieces[newRow][newCol]=pieces[startRow][startCol];
        allyList.get(Math.abs(pieces[startRow][startCol])-1).move();
        if(allyList.get(Math.abs(pieces[startRow][startCol])-1).isRoyal()){
            if(p1){
                if(royalCountW==1){
                    royalRowW=newRow;
                    royalColW=newCol;
                }
            }
            else{
                if(royalCountB==1){
                    royalRowB=newRow;
                    royalColB=newCol;
                }
            }
        }
        
        if(!(startRow==newRow&&startCol==newCol)){
            board[startRow][startCol]="-";
            pieces[startRow][startCol]=0;
        }
        if(newRow==allyList.get(Math.abs(pieces[newRow][newCol])-1).promotionRow){
           return pieces[newRow][newCol];
        }
        else return 0;
    }
    public void updateBoard(){
        //forwards board to pieces
        BetzaChessPiece.updateBoard(pieces);
        royalCount();
    }
    protected void findPosition(boolean p1,int num){
        int bWMod=1;
        
        if(!p1)bWMod=-1;
        for(int c=0;c<8;c++)for(int d=0;d<8;d++){
            if(pieces[c][d]==(num+1)*bWMod){
                BetzaChessPiece.updatePosition(c, d);
                startRow=c;
                startCol=d;
            }
        }
}
}

