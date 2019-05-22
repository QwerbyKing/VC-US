package visualchess;
import standardchessgame.ChessBoard;
import java.awt.Rectangle;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
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
    public static StateBasedGame game;
    public ChessBoard cboard;
    private boolean end = true,tempo=false,ender=false,promo=false;
    public static boolean p1,victor;
    private int stateID = -1,timer,flasher;
    public Image[] chessPieces= new Image[12];
    public Image gameBoard,gameover,goodmove;
    char[][]board;
    ArrayList<String> moves=new ArrayList<String>();
    String move="";
    private static final String HOMERANK="rnbqkbnr";
public Play(int stateID){
    
     this.stateID = stateID;
     
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
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
    String name;
    char type,colour;
    flasher=0;
    cboard = new ChessBoard();
    board=cboard.board;
    game = sbg;
    p1=true;
    victor=true;
    gameBoard=new Image("immutable\\ChessBoard.jpg");
    gameover=new Image("immutable\\gameover.png");
    goodmove=new Image("immutable\\Accessible.jpg");
    for(int col=0;col<8;col++){
        board[0][col]=HOMERANK.toLowerCase().charAt(col);
        board[1][col]='p';
        for(int row=2;row<6;row++)board[row][col]='-';
        board[6][col]='P';
        board[7][col]=HOMERANK.toUpperCase().charAt(col);
    }
    for(int c=0;c<12;c++){
        switch(c/2){
            case 0: type='b';
                    break;
            case 1: type='k';
                    break;
            case 2: type='n';
                    break;
            case 3: type='p';
                    break;
            case 4: type='q';
                    break;
            case 5: type='r';
                    break;
            default:type='x'; 
                    break;
        }
        if(c%2==0)colour='d';
        else colour='l';
        name="immutable\\Chess_" + type + colour + "t60.png";
        chessPieces[c]=new Image(name);
        
    }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.drawImage(gameBoard, 0, 0);
        for(int c=0;c<moves.size();c++){
           if(flasher>=30&&!(moves.get(c).equals("kc")||moves.get(c).equals("qc")))grphcs.drawImage(goodmove,((int)(moves.get(c).charAt(0))-97)*80,(8-Integer.parseInt(moves.get(c).substring(1)))*80);
        }
        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                switch(board[row][col]){
                    case 'b':   grphcs.drawImage(chessPieces[0], 80*col+10,80*row+10);
                                break;
                    case 'B':   grphcs.drawImage(chessPieces[1], 80*col+10,80*row+10);
                                break;
                    case 'k':   grphcs.drawImage(chessPieces[2], 80*col+10,80*row+10);
                                break;
                    case 'K':   grphcs.drawImage(chessPieces[3], 80*col+10,80*row+10);
                                break;
                    case 'n':   grphcs.drawImage(chessPieces[4], 80*col+10,80*row+10);
                                break;
                    case 'N':   grphcs.drawImage(chessPieces[5], 80*col+10,80*row+10);
                                break;
                    case 'p':   grphcs.drawImage(chessPieces[6], 80*col+10,80*row+10);
                                break;
                    case 'P':   grphcs.drawImage(chessPieces[7], 80*col+10,80*row+10);
                                break;
                    case 'q':   grphcs.drawImage(chessPieces[8], 80*col+10,80*row+10);
                                break;
                    case 'Q':   grphcs.drawImage(chessPieces[9], 80*col+10,80*row+10);
                                break;
                    case 'r':   grphcs.drawImage(chessPieces[10], 80*col+10,80*row+10);
                                break;
                    case 'R':   grphcs.drawImage(chessPieces[11], 80*col+10,80*row+10);
                                break;
                    default:    break;
                        
                }
            }
        }
        if(end){
            grphcs.drawImage(gameover,0,0);
            grphcs.setColor(Color.white);
            grphcs.fillRect(275, 310, 90, 20);
            grphcs.setColor(Color.black);
            if(victor)grphcs.drawString("Stalemate!",275,310);
            else{
                if(p1)grphcs.drawString("Black Wins",275,310);
                else  grphcs.drawString("White Wins",275,310);
            }
        }
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
            cboard.pawnPromotion(move, p1);
            promo=false;
        }
        
        cboard.updateBoard();
        timer=30;
        move="";
        end=cboard.checkVictory(p1);
        if(end)timer=150;
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
        if(!cboard.pieceSelect(move,p1)){
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
               if(p1){
                   if(cboard.moveSelect("kc")&&move.equals("g1"))move="kc";
                   else if(cboard.moveSelect("qc")&&move.equals("c1"))move="qc";
               }
               else {
                   if(cboard.moveSelect("kc")&&move.equals("g8"))move="kc";
                   else if(cboard.moveSelect("qc")&&move.equals("c8"))move="qc";
               }
               cboard.playMove(move,p1);
               if(p1&&move.charAt(1)=='8'){
                   if(cboard.isPawn(move, p1)){
                       game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.white));
                       promo=true;
                   }
               }
               else if((!p1)&&move.charAt(1)=='1'){
                   if(cboard.isPawn(move, p1)){
                       game.enterState(2, new FadeOutTransition(Color.black), new FadeInTransition(Color.white));
                       promo=true;
                   }
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
