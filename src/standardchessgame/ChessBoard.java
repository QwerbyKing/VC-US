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
import java.util.Scanner;
import visualchess.Vars;
public class ChessBoard {
    public char[][] board= new char[8][8];
    int[][] pieces = new int[8][8];
    int startRow=0,startCol=0,royalRowW,royalRowB,royalColB,royalColW;
    private boolean enPassant=false;
    public ArrayList<String> validMoves = new ArrayList<String>();
    private ChessPieces[] white = new ChessPieces[16];
    private ChessPieces[] black = new ChessPieces[16];
    private static final int[][] DEFAULTPIECES={{-8,-6,-4,-2,-1,-3,-5,-7},{-9,-10,-11,-12,-13,-14,-15,-16},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{9,10,11,12,13,14,15,16},{8,6,4,2,1,3,5,7}};
    private static final String HOMERANK="rnbqkbnr";
    public ChessBoard(){
        //sets reference board
        for(int row =0;row<8;row++)for(int col=0;col<8;col++)pieces[row][col]=DEFAULTPIECES[row][col];
        //forwards to pieces
        ChessPieces.updateBoard(pieces);
        //sets pieces
        for(int c=1;c<=16;c++){
            switch(c){
                case 1: white[c-1]=new ChessPieces(true).new King(true);
                        black[c-1]=new ChessPieces(false).new King(false);
                        findPosition(true,c-1);
                        white[c-1].displayMoves();
                        findPosition(false,c-1);
                        black[c-1].displayMoves();
                        break;
                case 2: white[c-1]=new ChessPieces(true).new Queen(true);
                        black[c-1]=new ChessPieces(false).new Queen(false);
                        findPosition(true,c-1);
                        white[c-1].displayMoves();
                        findPosition(false,c-1);
                        black[c-1].displayMoves();
                        break;
                case 3: case 4: white[c-1]=new ChessPieces(true).new Bishop(true);
                                black[c-1]=new ChessPieces(false).new Bishop(false);
                                findPosition(true,c-1);
                                white[c-1].displayMoves();
                                findPosition(false,c-1);
                                black[c-1].displayMoves();
                                break;
                case 5: case 6: white[c-1]=new ChessPieces(true).new Knight(true);
                                black[c-1]=new ChessPieces(false).new Knight(false);
                                findPosition(true,c-1);
                                white[c-1].displayMoves();
                                findPosition(false,c-1);
                                black[c-1].displayMoves();
                                break;
                case 7: case 8: white[c-1]=new ChessPieces(true).new Rook(true);
                                black[c-1]=new ChessPieces(false).new Rook(false);
                                findPosition(true,c-1);
                                white[c-1].displayMoves();
                                findPosition(false,c-1);
                                black[c-1].displayMoves();
                                break;
                default:    white[c-1]=new ChessPieces(true).new Pawn(true);
                            black[c-1]=new ChessPieces(false).new Pawn(false);
                            findPosition(true,c-1);
                            white[c-1].displayMoves();
                            findPosition(false,c-1);
                            black[c-1].displayMoves();
                            break;
            }
        }
        //initial king positions
        royalColB=4;
        royalColW=4;
        royalRowB=0;
        royalRowW=7;
        //sets displayed board
    for(int col=0;col<8;col++){
        board[0][col]=HOMERANK.toLowerCase().charAt(col);
        board[1][col]='p';
        for(int row=2;row<6;row++)board[row][col]='-';
        board[6][col]='P';
        board[7][col]=HOMERANK.toUpperCase().charAt(col);
    }
    }
    public void initBoard(){
        //see constructor
        for(int row =0;row<8;row++)for(int col=0;col<8;col++)pieces[row][col]=DEFAULTPIECES[row][col];
        ChessPieces.updateBoard(pieces);
        for(int c=1;c<=16;c++){
            switch(c){
                case 1: white[c-1]=new ChessPieces(true).new King(true);
                        black[c-1]=new ChessPieces(false).new King(false);
                        break;
                case 2: white[c-1]=new ChessPieces(true).new Queen(true);
                        black[c-1]=new ChessPieces(false).new Queen(false);
                        break;
                case 3: case 4: white[c-1]=new ChessPieces(true).new Bishop(true);
                                black[c-1]=new ChessPieces(false).new Bishop(false);
                                break;
                case 5: case 6: white[c-1]=new ChessPieces(true).new Knight(true);
                                black[c-1]=new ChessPieces(false).new Knight(false);
                                break;
                case 7: case 8: white[c-1]=new ChessPieces(true).new Rook(true);
                                black[c-1]=new ChessPieces(false).new Rook(false);
                                break;
                default:    white[c-1]=new ChessPieces(true).new Pawn(true);
                            black[c-1]=new ChessPieces(false).new Pawn(false);
                            break;
            }
        }
        for(int col=0;col<8;col++){
        board[0][col]=HOMERANK.toLowerCase().charAt(col);
        board[1][col]='p';
        for(int row=2;row<6;row++)board[row][col]='-';
        board[6][col]='P';
        board[7][col]=HOMERANK.toUpperCase().charAt(col);
    }
        royalColB=4;
        royalColW=4;
        royalRowB=0;
        royalRowW=7;
        getPiece(true,true);
        getPiece(false,true);
    }
    public void updateBoard(){
        //forwards board to pieces
        ChessPieces.updateBoard(pieces);
    }
    public void displayBoard(){
        //displays board along with move syntax markers
        for(int row=0;row<8;row++){
            System.out.print(8-row + " ");
            for(int col=0;col<8;col++)System.out.print(board[row][col]);
            System.out.print("\n");
        }
        System.out.println("  abcdefgh");
        //updates board, so it is never outdated
        updateBoard();
    }
    private void checkCheck(boolean p1){
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
            //if a move would lead to the king being under fire by any piece, that move is no longer valid
            if(p1){
                if(!(white[pieces[startRow][startCol]-1] instanceof ChessPieces.King)){
                    row=royalRowW;
                    col=royalColW;
                }
            }
            else {
                if(!(black[Math.abs(pieces[startRow][startCol])-1] instanceof ChessPieces.King)){
                    row=royalRowB;
                    col=royalColB;
                }
            }
            if(positionCheck(p1,row,col,tempboard))validMoves.remove(counter);
            else counter++;
        }
    }
    private boolean positionCheck(boolean p1,int row,int col, int[][]tempboard){
        return checkDiagonals(p1,row,col,tempboard)||checkOrthogonals(p1,row,col,tempboard)||checkKnights(p1,row,col,tempboard)||checkPawns(p1,row,col,tempboard)||checkKings(p1,row,col);
    }
    private boolean checkDirection(boolean p1,int row,int col,int amount,int rowMod,int colMod, boolean dia,int[][] pieces){
        /*p1 is the active player (true-white, false-black) 
    row and col are the coordinates of the starting position
    amount is a counter, incrementing it checks the next space in the given direction
    rowMod and colMod decide direction, -1 is up/left, 0 indicates no movement, 1 is down/right
    dia is true if coming from checkDiagonals(), false if coming from checkOrthogonals
    pieces is the board*/
        
        boolean bishop=false,rook=false,queen=false;
        int position=Math.abs(pieces[row+amount*rowMod][col+amount*colMod]);
        if(p1){
            //if active player is white, checks if the given square contains a black queen, or a black rook (if checking orthogonal) or a black bishop (if checking diagonals)
            if(position>0&&position<17){
                        queen=black[Math.abs(pieces[row+amount*rowMod][col+amount*colMod])-1] instanceof ChessPieces.Queen;
                if(dia)bishop=black[Math.abs(pieces[row+amount*rowMod][col+amount*colMod])-1] instanceof ChessPieces.Bishop;
                else     rook=black[Math.abs(pieces[row+amount*rowMod][col+amount*colMod])-1] instanceof ChessPieces.Rook;
            }
            //returns false if an allied unit is identified, 
            if(pieces[row+amount*rowMod][col+amount*colMod]>0)return false;
            //checks if next space is empty and is not the edge of the board, and recursively calls the next spot
            else if(pieces[row+amount*rowMod][col+amount*colMod]==0&&((row+amount*rowMod>0&&row+amount*rowMod<7)||rowMod==0)&&((col+amount*colMod>0&&col+amount*colMod<7)||colMod==0))return checkDirection(p1,row,col,amount+1,rowMod,colMod,dia,pieces);
            //returns true if next space contains a threatening unit, false otherwise
            else return pieces[row+amount*rowMod][col+amount*colMod]<0&&(queen||(rook||bishop));
        }
        else {
            //if active player is white, checks if the given square contains a black queen, or a black rook (if checking orthogonal) or a black bishop (if checking diagonals)
            if(position>0&&position<17){
                queen=white[Math.abs(pieces[row+amount*rowMod][col+amount*colMod])-1] instanceof ChessPieces.Queen;
                if(dia)bishop=white[Math.abs(pieces[row+amount*rowMod][col+amount*colMod])-1] instanceof ChessPieces.Bishop;
                else rook=white[Math.abs(pieces[row+amount*rowMod][col+amount*colMod])-1] instanceof ChessPieces.Rook;
            }
            //returns false if an allied unit is identified
            if(pieces[row+amount*rowMod][col+amount*colMod]<0)return false;
            //checks if next space is empty and is not the edge of the board, and recursively calls the next spot
            else if(pieces[row+amount*rowMod][col+amount*colMod]==0&&((row+amount*rowMod>0&&row+amount*rowMod<7)||rowMod==0)&&((col+amount*colMod>0&&col+amount*colMod<7)||colMod==0))return checkDirection(p1,row,col,amount+1,rowMod,colMod,dia,pieces);
            //returns true if next space contains a threatening unit, false otherwise
            else return pieces[row+amount*rowMod][col+amount*colMod]>0&&(rook||queen||bishop);
        }
    }
    private boolean checkOrthogonals(boolean p1,int row,int col,int[][] tempboard){
      //boolean variables prevent Array outOfBounds exceptions from being thrown
        boolean left=col>0,up=row>0,right=col<7,down=row<7;
      
            if(up)if(checkDirection(p1,row,col,1,-1,0,false,tempboard))return true;
            if(down)if(checkDirection(p1,row,col,1,1,0,false,tempboard))return true;
            if(left)if(checkDirection(p1,row,col,1,0,-1,false,tempboard))return true;
            if(right)if(checkDirection(p1,row,col,1,0,1,false,tempboard))return true;
            return false;
    }
    private boolean checkDiagonals(boolean p1,int row,int col,int[][] tempboard){
        //boolean variables prevent Array outOfBounds exceptions from being thrown
        boolean left=col>0,up=row>0,right=col<7,down=row<7;
      
            if(up&&left)if(checkDirection(p1,row,col,1,-1,-1,true,tempboard))return true;
            if(down&&right)if(checkDirection(p1,row,col,1,1,1,true,tempboard))return true;
            if(left&&down)if(checkDirection(p1,row,col,1,1,-1,true,tempboard))return true;
            if(right&&up)if(checkDirection(p1,row,col,1,-1,1,true,tempboard))return true;
            return false;
    }
    private boolean checkKnights(boolean p1,int row,int col,int[][] tempboard){
        //boolean variables prevent Array outOfBounds exceptions
        boolean left1=col>0,left2=col>1,up1=row>0,up2=row>1,right1=col<7,right2=col<6,down1=row<7,down2=row<6;
        //checks if any of the pieces in the 8 knight accessible spots contain enemy knights, returns true if so
        if(p1){
            if(left2&&up1)if(tempboard[row-1][col-2]>-17&&tempboard[row-1][col-2]<0)if(black[Math.abs(tempboard[row-1][col-2])-1]instanceof ChessPieces.Knight)return true;
            if(left1&&up2)if(tempboard[row-2][col-1]>-17&&tempboard[row-2][col-1]<0)if(black[Math.abs(tempboard[row-2][col-1])-1]instanceof ChessPieces.Knight)return true;
            if(right1&&up2)if(tempboard[row-2][col+1]>-17&&tempboard[row-2][col+1]<0)if(black[Math.abs(tempboard[row-2][col+1])-1]instanceof ChessPieces.Knight)return true;
            if(right2&&up1)if(tempboard[row-1][col+2]>-17&&tempboard[row-1][col+2]<0)if(black[Math.abs(tempboard[row-1][col+2])-1]instanceof ChessPieces.Knight)return true;
            if(left2&&down1)if(tempboard[row+1][col-2]>-17&&tempboard[row+1][col-2]<0)if(black[Math.abs(tempboard[row+1][col-2])-1]instanceof ChessPieces.Knight)return true;
            if(left1&&down2)if(tempboard[row+2][col-1]>-17&&tempboard[row+2][col-1]<0)if(black[Math.abs(tempboard[row+2][col-1])-1]instanceof ChessPieces.Knight)return true;
            if(right1&&down2)if(tempboard[row+2][col+1]>-17&&tempboard[row+2][col+1]<0)if(black[Math.abs(tempboard[row+2][col+1])-1]instanceof ChessPieces.Knight)return true;
            if(right2&&down1)if(tempboard[row+1][col+2]>-17&&tempboard[row+1][col+2]<0)if(black[Math.abs(tempboard[row+1][col+2])-1]instanceof ChessPieces.Knight)return true;
        }
        else {
            if(left2&&up1)if(tempboard[row-1][col-2]<17&&tempboard[row-1][col-2]>0)if(white[tempboard[row-1][col-2]-1]instanceof ChessPieces.Knight)return true;
            if(left1&&up2)if(tempboard[row-2][col-1]<17&&tempboard[row-2][col-1]>0)if(white[tempboard[row-2][col-1]-1]instanceof ChessPieces.Knight)return true;
            if(right1&&up2)if(tempboard[row-2][col+1]<17&&tempboard[row-2][col+1]>0)if(white[tempboard[row-2][col+1]-1]instanceof ChessPieces.Knight)return true;
            if(right2&&up1)if(tempboard[row-1][col+2]<17&&tempboard[row-1][col+2]>0)if(white[tempboard[row-1][col+2]-1]instanceof ChessPieces.Knight)return true;
            if(left2&&down1)if(tempboard[row+1][col-2]<17&&tempboard[row+1][col-2]>0)if(white[tempboard[row+1][col-2]-1]instanceof ChessPieces.Knight)return true;
            if(left1&&down2)if(tempboard[row+2][col-1]<17&&tempboard[row+2][col-1]>0)if(white[tempboard[row+2][col-1]-1]instanceof ChessPieces.Knight)return true;
            if(right1&&down2)if(tempboard[row+2][col+1]<17&&tempboard[row+2][col+1]>0)if(white[tempboard[row+2][col+1]-1]instanceof ChessPieces.Knight)return true;
            if(right2&&down1)if(tempboard[row+1][col+2]<17&&tempboard[row+1][col+2]>0)if(white[tempboard[row+1][col+2]-1]instanceof ChessPieces.Knight)return true;
        }
        return false;
    }
    private boolean checkPawns(boolean p1,int row,int col, int[][] tempboard){
        //boolean values prevent Array outOfBounds exceptions from being thrown
        boolean left=col>0,right=col<7;
        //if statements check if king is on enemy's home rank, then if the piece in that position is a pawn of the enemy's colour
        if(p1&&row!=0){
            if(left )if(tempboard[row-1][col-1]>-17&&tempboard[row-1][col-1]<0)if(black[Math.abs(tempboard[row-1][col-1])-1]instanceof ChessPieces.Pawn)return true;
            if(right)if(tempboard[row-1][col+1]>-17&&tempboard[row-1][col+1]<0)if(black[Math.abs(tempboard[row-1][col+1])-1]instanceof ChessPieces.Pawn)return true;
        }
        else if((!p1)&&row!=7){
            if(left )if(tempboard[row+1][col-1]<17&&tempboard[row+1][col-1]>0)if(white[tempboard[row+1][col-1]-1]instanceof ChessPieces.Pawn)return true;
            if(right)if(tempboard[row+1][col+1]<17&&tempboard[row+1][col+1]>0)if(white[tempboard[row+1][col+1]-1]instanceof ChessPieces.Pawn)return true;
        }
        return false;
    }
    private boolean checkKings(boolean p1,int row,int col){
        boolean rows,cols;
        /*rows is true if opposing king is in the same or an adjacent row
        cols is true if opposing king is in the same or an adjacent column*/
        if(p1){
            rows=Math.abs(royalRowB-row)==1||royalRowB==row;
            cols=Math.abs(royalColB-col)==1||royalColB==col;
        }
        else {
            rows=Math.abs(royalRowW-row)==1||royalRowW==row;
            cols=Math.abs(royalColW-col)==1||royalColW==col;
        }
        //if opposing king is in the same or an adjacent row and column, it is in one of the positions surrounding the king
        return rows&&cols;
    }
    public boolean pieceSelect(String move,boolean p1){
        //assures proper syntax
        if(move.length()!=2||move.charAt(0)<'a'||move.charAt(0)>'h'||move.charAt(1)<'1'||move.charAt(1)>'8'){
            System.out.println("That's not a valid position!");
            return false;
        }
        else {
            //sets position for pieces
            startRow=8-Integer.parseInt(move.substring(1));
            startCol=(int)move.charAt(0)-97;
            ChessPieces.updatePosition(startRow,startCol);
            //assures that position chosen contains piece belonging to player
            if(!((p1&&pieces[startRow][startCol]>0&&pieces[startRow][startCol]<17)||(!p1&&pieces[startRow][startCol]<0&&pieces[startRow][startCol]>-17))){
                /*if(pieces[startRow][startCol]==0||pieces[startRow][startCol]>16||pieces[startRow][startCol]<-16)System.out.println("There's nothing there!");
                else System.out.println("That's not yours!");*/
                return false;
            }
            else {
                //sets list of moves to that of piece on chosen position
                getPiece(p1,false);
                //CASTLE WHITE
                int[][]tempboard = new int[8][8];
                for(int d=0;d<8;d++)for(int e=0;e<8;e++)tempboard[d][e]=pieces[d][e];
                
                    if(p1){
                        if(!positionCheck(p1,startRow,startCol,tempboard)){
                    if(white[pieces[startRow][startCol]-1] instanceof ChessPieces.King&&!white[pieces[startRow][startCol]-1].haveMoved()){
                        for(int c=startCol;c<7;c++){
                            if(pieces[startRow][c+1]>0){
                                if(white[pieces[startRow][c+1]-1] instanceof ChessPieces.Rook&&!white[pieces[startRow][c+1]-1].haveMoved()){
                                    if(validMoves.contains(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow))))){
                                        tempboard[startRow][startCol+2]=tempboard[startRow][startCol];
                                        tempboard[startRow][startCol]=0;
                                        if(!positionCheck(p1,startRow,startCol+2,tempboard)){
                                            validMoves.add("kc");
                                            validMoves.add(Character.toString((char)(startCol+2+97))+Integer.toString((8-(startRow))));
                                        }
                                        
                                    }
                                }
                                break;
                            }
                            else if(pieces[startRow][c+1]<0)break;
                        }
                        for(int d=0;d<8;d++)for(int e=0;e<8;e++)tempboard[d][e]=pieces[d][e];
                        for(int c=startCol;c>0;c--){
                            if(pieces[startRow][c-1]>0){
                                if(white[pieces[startRow][c-1]-1] instanceof ChessPieces.Rook&&!white[pieces[startRow][c-1]-1].haveMoved()){
                                    if(validMoves.contains(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow))))){
                                        tempboard[startRow][startCol-2]=tempboard[startRow][startCol];
                                        tempboard[startRow][startCol]=0;
                                        if(!positionCheck(p1,startRow,startCol-2,tempboard)){
                                            validMoves.add("qc");
                                            validMoves.add(Character.toString((char)(startCol-2+97))+Integer.toString((8-(startRow))));
                                        }
                                    }
                                }
                                break;
                            }
                            else if(pieces[startRow][c-1]<0)break;
                        }
                    }
                    }
                    }
                //CASTLE BLACK
                else {
                    if(!positionCheck(p1,startRow,startCol,tempboard)){
                    if(black[Math.abs(pieces[startRow][startCol])-1] instanceof ChessPieces.King&&!black[Math.abs(pieces[startRow][startCol])-1].haveMoved()){
                        for(int c=startCol;c<7;c++){
                            if(pieces[startRow][c+1]<0){
                                if(black[Math.abs(pieces[startRow][c+1])-1] instanceof ChessPieces.Rook&&!black[Math.abs(pieces[startRow][c+1])-1].haveMoved()){
                                    if(validMoves.contains(Character.toString((char)(startCol+1+97))+Integer.toString((8-(startRow))))){
                                        tempboard[startRow][startCol+2]=tempboard[startRow][startCol];
                                        tempboard[startRow][startCol]=0;
                                        if(!positionCheck(p1,startRow,startCol+2,tempboard)){
                                            validMoves.add("kc");
                                            validMoves.add(Character.toString((char)(startCol+2+97))+Integer.toString((8-(startRow))));
                                        }
                                    }
                                }
                                break;
                            }
                            else if(pieces[startRow][c+1]>0)break;
                        }
                        for(int d=0;d<8;d++)for(int e=0;e<8;e++)tempboard[d][e]=pieces[d][e];
                        for(int c=startCol;c>0;c--){
                            if(pieces[startRow][c-1]<0){
                                if(black[Math.abs(pieces[startRow][c-1])-1] instanceof ChessPieces.Rook&&!black[Math.abs(pieces[startRow][c-1])-1].haveMoved()){
                                    if(validMoves.contains(Character.toString((char)(startCol-1+97))+Integer.toString((8-(startRow))))){
                                        tempboard[startRow][startCol-2]=tempboard[startRow][startCol];
                                        tempboard[startRow][startCol]=0;
                                        if(!positionCheck(p1,startRow,startCol-2,tempboard)){
                                            validMoves.add("qc");
                                            validMoves.add(Character.toString((char)(startCol-2+97))+Integer.toString((8-(startRow))));
                                        }
                                    }
                                }
                                break;
                            }
                            else if(pieces[startRow][c-1]>0)break;
                        }
                    }
                }
                }
                //if no moves are available returns to piece selection
                if(validMoves.isEmpty()){
                    //System.out.println("That piece cannot legally move!");
                    return false;
                }
                else {
                    //outputs list of moves
                    /*for(int c=0;c<validMoves.size();c++){
                        System.out.print(validMoves.get(c));
                        if(c%4==3)System.out.println();
                        else System.out.print(" ");
                    }
                    if(validMoves.size()%4!=0)System.out.println();
                    System.out.println("End of movelist");*/
                    return true;
                }
            }
        }
    }
    private void getPiece(boolean p1,boolean check){
        //sets list of moves of piece on chosen position so board can output to user
        ArrayList<String> totalMoves= new ArrayList<String>();
        
        for(int c=0;c<16;c++){
            validMoves.clear();
            if(!check)c=Math.abs(pieces[startRow][startCol])-1;
            else findPosition(p1,c);
            if(p1&&!white[c].isDead())validMoves.addAll(white[c].displayMoves());
            else if(!p1&&!black[c].isDead())validMoves.addAll(black[c].displayMoves());
            //removes moves that violate check
            checkCheck(p1);
            if(!check)return;
            totalMoves.addAll(validMoves);
        }
        validMoves.clear();
        validMoves.addAll(totalMoves);
        
    }
    private void findPosition(boolean p1,int num){
        int bWMod=1;
        
        if(!p1)bWMod=-1;
        for(int c=0;c<8;c++)for(int d=0;d<8;d++){
            if(pieces[c][d]==(num+1)*bWMod){
                ChessPieces.updatePosition(c, d);
                startRow=c;
                startCol=d;
            }
        }
}
    public boolean moveSelect(String move){
        //checks of inputted move is in the list of valid moves
        return validMoves.indexOf(move)!=-1;
    }
    public boolean checkVictory(boolean p1){
        int[][] tempboard = new int[8][8];
        int row,col;
        String playnum;
        
        if(p1){
            row=royalRowB;
            col=royalColB;
            playnum="White";
        }
        else {
            row=royalRowW;
            col=royalColW;
            playnum="Black";
        }
        
        for(int c=0;c<8;c++)for(int d=0;d<8;d++)tempboard[c][d]=pieces[c][d];
        validMoves.clear();
        getPiece(!p1,true);
        if(validMoves.isEmpty()){
          if(checkDiagonals(!p1,row,col,tempboard)||
            checkOrthogonals(!p1,row,col,tempboard)||
            checkKnights(!p1,row,col,tempboard)||
            checkPawns(!p1,row,col,tempboard)||
            checkKings(!p1,row,col)){
              //System.out.println("Game Over! " + playnum + " wins!");
              visualchess.Play.victor=false;
              return true;
          }
          else {
              //System.out.println("Game Over! Nobody wins!");
              return true;
          }
        }
        else return false;
    }
    public void playMove(String move,boolean p1){
        int newRow,newCol;
        switch(move){
            case "kc":
                        enPassant=false;
                        for(int c=0;c<8;c++)for(int d=0;d<8;d++)if(Math.abs(pieces[c][d])>16)pieces[c][d]=0;
                        if(p1){
                            white[pieces[royalRowW][royalColW]-1].move();
                            white[pieces[7][7]-1].move();
                            pieces[7][5]=pieces[7][7];
                            pieces[7][6]=pieces[7][4];
                            pieces[7][7]=0;
                            pieces[7][4]=0;
                            board[7][5]=board[7][7];
                            board[7][6]=board[7][4];
                            board[7][7]='-';
                            board[7][4]='-';
                            royalColW=6;
                        }
                        else {
                            black[Math.abs(pieces[royalRowB][royalColB])-1].move();
                            black[Math.abs(pieces[0][7])-1].move();
                            pieces[0][5]=pieces[0][7];
                            pieces[0][6]=pieces[0][4];
                            pieces[0][7]=0;
                            pieces[0][4]=0;
                            board[0][5]=board[0][7];
                            board[0][6]=board[0][4];
                            board[0][7]='-';
                            board[0][4]='-';
                            royalColB=6;
                        }
                        break;
            case "qc": 
                        enPassant=false;
                        for(int c=0;c<8;c++)for(int d=0;d<8;d++)if(Math.abs(pieces[c][d])>16)pieces[c][d]=0;
                        if(p1){
                            white[pieces[royalRowW][royalColW]-1].move();
                            white[pieces[7][0]-1].move();
                            pieces[7][3]=pieces[7][0];
                            pieces[7][2]=pieces[7][4];
                            pieces[7][0]=0;
                            pieces[7][4]=0;
                            board[7][3]=board[7][0];
                            board[7][2]=board[7][4];
                            board[7][0]='-';
                            board[7][4]='-';
                            royalColW=2;
                        }
                        else {
                            black[Math.abs(pieces[royalRowB][royalColB])-1].move();
                            black[Math.abs(pieces[0][0])-1].move();
                           pieces[0][3]=pieces[0][0];
                            pieces[0][2]=pieces[0][4];
                            pieces[0][0]=0;
                            pieces[0][4]=0;
                            board[0][3]=board[0][0];
                            board[0][2]=board[0][4];
                            board[0][0]='-';
                            board[0][4]='-';
                            royalColB=2;
                        }
                        break;
            default: 
        
        newRow=8-Integer.parseInt(move.substring(1));
        newCol=(int)move.charAt(0)-97;
        if(pieces[newRow][newCol]>0){
            if(pieces[newRow][newCol]>16){
                if(!p1&&black[Math.abs(pieces[startRow][startCol]-1)] instanceof ChessPieces.Pawn)white[pieces[newRow][newCol]-17].kill();
                else enPassant=false;
            }
            else {
                white[pieces[newRow][newCol]-1].kill();
                enPassant=false;
            }
        }
        else if(pieces[newRow][newCol]<0){
            if(pieces[newRow][newCol]<-16){
                if(p1&&white[Math.abs(pieces[startRow][startCol]-1)] instanceof ChessPieces.Pawn)black[Math.abs(pieces[newRow][newCol])-17].kill();
                else enPassant=false;
            }
            else {
                black[Math.abs(pieces[newRow][newCol])-1].kill();
                enPassant=false;
            }
        }
        else enPassant=false;
        if(enPassant){
            if(p1){
                pieces[newRow+1][newCol]=0;
                board[newRow+1][newCol]='-';
            }
            else {
                pieces[newRow-1][newCol]=0;
                board[newRow-1][newCol]='-';
            }
            enPassant=false;
        }
        for(int c=0;c<8;c++)for(int d=0;d<8;d++)if(Math.abs(pieces[c][d])>16)pieces[c][d]=0;
        //moves piece to new position
        board[newRow][newCol]=board[startRow][startCol];
        pieces[newRow][newCol]=pieces[startRow][startCol];
        //sets piece to have moved (castling/double pawn move)
        if(p1)white[pieces[startRow][startCol]-1].move();
        else black[Math.abs(pieces[startRow][startCol])-1].move();
        
        if(p1&&white[pieces[startRow][startCol]-1] instanceof ChessPieces.King){
            royalRowW=newRow;
            royalColW=newCol;
        }
        else if(!p1&&black[Math.abs(pieces[startRow][startCol])-1] instanceof ChessPieces.King){
            royalRowB=newRow;
            royalColB=newCol;
        }
        //en passant possibility
        if(p1&&newRow==4&&white[pieces[startRow][startCol]-1] instanceof ChessPieces.Pawn){
            pieces[5][startCol]=pieces[startRow][startCol]+16;
            enPassant=true;
        }
        else if(!p1&&newRow==3&&black[Math.abs(pieces[startRow][startCol])-1] instanceof ChessPieces.Pawn){
            pieces[2][startCol]=pieces[startRow][startCol]-16;
            enPassant=true;
        }
        //deletes piece from original position
        board[startRow][startCol]='-';
        pieces[startRow][startCol]=0;
    }
    }
    public boolean isPawn(String move,boolean p1){
        char pawn;
        int newRow,newCol;
        newRow=8-Integer.parseInt(move.substring(1));
        newCol=(int)move.charAt(0)-97;
        if(p1)pawn='P';
        else pawn ='p';
        return board[newRow][newCol]==pawn;
    }
    public void pawnPromotion(String move, boolean p1){
        int newRow,newCol;
        newRow=8-Integer.parseInt(move.substring(1));
        newCol=(int)move.charAt(0)-97;
        if(p1)board[newRow][newCol]=pawnPromotion(pieces[newRow][newCol]-1,p1);
        else board[newRow][newCol]=pawnPromotion(Math.abs(pieces[newRow][newCol])-1,p1);
    }
    private char pawnPromotion(int id,boolean p1){
        Scanner input= new Scanner(System.in);
        String choice;
        //sets piece to relevant type, returns character referring to displayed board icon (uppercase-->white, lowercase-->black)
        choice=Vars.choice;
        if(p1){
            if(choice.equals("q")){
                white[id]=new ChessPieces(p1).new Queen(p1);
            }
            else if(choice.equals("r")){
                white[id]=new ChessPieces(p1).new Rook(p1);
            }
            else if(choice.equals("b")){
                white[id]=new ChessPieces(p1).new Bishop(p1);
            }
            else {
                white[id]=new ChessPieces(p1).new Knight(p1);
            }
        }
        else {
            if(choice.equals("q")){
                black[id]=new ChessPieces(p1).new Queen(p1);
            }
            else if(choice.equals("r")){
                black[id]=new ChessPieces(p1).new Rook(p1);
            }
            else if(choice.equals("b")){
                black[id]=new ChessPieces(p1).new Bishop(p1);
            }
            else {
                black[id]=new ChessPieces(p1).new Knight(p1);
            }
        }
        return Vars.pawn;
    }
    }
