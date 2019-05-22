package q.chess;
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
import java.util.Scanner;
import java.io.*;

public class Menu extends BasicGameState{
    private int select[][];
    private Input input;
    private StateBasedGame game;
    private Image menu;
    Menu(int i){
    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sg) throws SlickException {
       this.init(gc, sg);
    }
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        menu=new Image("immutable\\Menu.png");
        input=gc.getInput();
        game=sbg;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
        grphcs.setBackground(Color.white);
        grphcs.drawImage(menu,0,0);
        /*if(select[0][0]==1){
            grphcs.setColor(Color.black);
            grphcs.drawString("Play", 100, 100);
        }else{
            grphcs.setColor(Color.lightGray);
            grphcs.drawString("Play", 100, 100);
        }
        if(select[1]==1){
            grphcs.setColor(Color.black);
            grphcs.drawString("Options", 100, 150);
        }else{
            grphcs.setColor(Color.lightGray);
            grphcs.drawString("Options", 100, 150);
        }
        if(select[2]==1){
            grphcs.setColor(Color.black);
            grphcs.drawString("Quit", 100, 200);
        }else{
            grphcs.setColor(Color.lightGray);
            grphcs.drawString("Quit", 100, 200);
        }*/
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        int posX=Mouse.getX();
        int posY=Mouse.getY();
        Input input=gc.getInput();
        if(Mouse.isButtonDown(0)){
            if(posX<320){
                //top-left
                if(posY>=320){
                    game.enterState(3, new FadeOutTransition(Color.white), new FadeInTransition(Color.black));;
                }
                //bottom-left
                else {
                   game.enterState(1, new FadeOutTransition(Color.white), new FadeInTransition(Color.black)); 
                }
            }
            else{
                //top-right
                if(posY>=320){
                    game.enterState(5, new FadeOutTransition(Color.white), new FadeInTransition(Color.black));
                }
                //bottom-right
                else {
                    gc.exit();
                }
            }
            
        }
        else if(input.isKeyPressed(Input.KEY_ESCAPE)){
            gc.exit();
        }
    }
}
