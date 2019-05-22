package q.chess;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alinger2442
 */
import java.awt.Rectangle;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import java.io.File;
import java.io.FileNotFoundException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import java.util.ArrayList;
public class Play extends BasicGameState{
    private int stateID=-1,victor,timer,flasher,lastPiece;
    public static StateBasedGame game;
    public ChessBoard cboard;
    private boolean end = true,tempo=false,ender=false,promo=false;
    public static boolean p1;
    public ArrayList<Image> chessPieces;
    public ArrayList<String> chessPiecesIndexes;
    public Image gameBoard,gameover,goodmove;
    String[][]board;
    ArrayList<String> moves=new ArrayList<>();
    String move="";
    public Play(int stateID) throws SlickException{
        this.stateID=stateID;
        
    }
    @Override
    public int getID() {
        return stateID;
      }
    @Override
    public void enter(GameContainer gc, StateBasedGame sg) throws SlickException {
       if(end){
           end=false;
           this.init(gc, sg);
       }
    }
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
        chessPiecesIndexes=new ArrayList<>();
        chessPieces=new ArrayList<>();
        fileLoad();
        flasher=0;
        try{
            cboard=new ChessBoard(0);
        }catch(FileNotFoundException e){
            
        }
        board=cboard.board;
        game=sbg;
        p1=true;
        victor=-1;
        gameBoard=new Image("immutable\\ChessBoard.jpg");
        gameover=new Image("immutable\\gameover.png");
        goodmove=new Image("immutable\\Accessible.jpg");
    }
    private void fileLoad() throws SlickException{
        chessPieces.clear();
        chessPiecesIndexes.clear();
        chessPieces.add(new Image("immutable\\DEFAULT.png"));
        for(char c='A';c<='Z';c++){
            File testImage=new File("game\\"+c+"W.png");
            if(testImage.exists()){
                chessPieces.add(new Image("game\\"+c+"W.png"));
                chessPiecesIndexes.add(c+"W");
            }
            testImage=new File("game\\"+c+"B.png");
            if(testImage.exists()){
                chessPieces.add(new Image("game\\"+(char)((int)c+32)+"B.png"));
                chessPiecesIndexes.add((char)((int)c+32)+"B");
            }
        }
    }
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException{
        grphcs.drawImage(gameBoard,0,0);
        for(int c=0;c<moves.size();c++){
           if(flasher>=30)grphcs.drawImage(goodmove,((int)(moves.get(c).charAt(0))-97)*80,(8-Integer.parseInt(moves.get(c).substring(1)))*80);
        }
        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                if(!board[row][col].equals("-"))grphcs.drawImage(chessPieces.get(chessPiecesIndexes.indexOf(board[row][col])+1), 80*col+10,80*row+10);
            }
        }
        if(end){
            grphcs.drawImage(gameover,0,0);
            grphcs.setColor(Color.white);
            grphcs.fillRect(275, 310, 90, 20);
            grphcs.setColor(Color.black);
            if(victor==0)grphcs.drawString("Stalemate!",275,310);
            else{
                if(p1)grphcs.drawString("Black Wins",275,310);
                else  grphcs.drawString("White Wins",275,310);
            }
        }
    }
    private String addFile(String name,boolean p1){
        char player;
        if(p1)player='W';
        else player='B';
        File testImage=new File("game\\"+name+player+".png");
            if(testImage.exists()){
                try{
                    chessPieces.add(new Image("game\\"+name+player+".png"));
                }catch(SlickException e){
                    
                }
                chessPiecesIndexes.add(name+Character.toString(player));
                return name+player;
            }
            else return "";
    }
    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
    Input input= gc.getInput();
    int posX=Mouse.getX();
    int posY=Mouse.getY();
    moves=cboard.validMoves;
    if(!end){
        if(ender){
        if(promo){
            cboard.promotion(lastPiece, Vars.betza,addFile(Vars.name,p1));
            promo=false;
            
        }
        
        cboard.updateBoard();
        timer=30;
        move="";
        victor=cboard.checkVictory(p1);
        if(victor>=0){
            timer=150;
            end=true;
        }
        p1=!p1;
        ender=false;
    }
    }
    if(!end){
    if(Mouse.isButtonDown(0)&&!tempo){
        move=Character.toString((char)(posX/80+97))+Integer.toString((posY/80)+1);
        tempo=true;
        timer=-6;
        flasher++;
        if(!cboard.goodMove(p1,move)){
        tempo=false;
        timer=0;
        flasher--;
    }
    }
    if(timer==-1){
        flasher++;
        if(flasher>=60)flasher=flasher%60;
       if(Mouse.isButtonDown(0)){
           move=Character.toString((char)(posX/80+97))+Integer.toString((posY/80)+1);
           if(cboard.moveSelect(move)){
               flasher=0;
               lastPiece=cboard.playMove(move,p1);
               if(lastPiece!=0){
                   Vars.piece=lastPiece;
                   Vars.promo=cboard.getPromo(lastPiece);
                   Vars.p1=p1;
                   game.enterState(4, new FadeOutTransition(Color.black), new FadeInTransition(Color.white));
                   promo=true;
                   
               }             
           ender=true;
           }
           else move="";
       } 
       
    }
    if(timer==0)tempo=false;
    else if(timer>0)timer--;
    else if(timer<-1)timer++;
    }
    else {
        timer--;
        if(timer==0)game.enterState(0, new FadeOutTransition(Color.white), new FadeInTransition(Color.black));
    }
    if(input.isKeyPressed(Input.KEY_ESCAPE)){
       end=true;
       game.enterState(0, new FadeOutTransition(Color.white), new FadeInTransition(Color.black));
    }
    }

}
