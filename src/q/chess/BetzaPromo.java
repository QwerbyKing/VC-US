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
import java.io.*;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
public class BetzaPromo extends BasicGameState{
    private static StateBasedGame game;
    private int stateID=-1,piece,timer;
    private boolean p1;
    private char player;
    private Image[] pieces;
    private String[][] promo;
    public BetzaPromo(int stateID){
        this.stateID=stateID;
    }
    public int getID(){
        return stateID;
    }
    @Override
    public void enter(GameContainer gc, StateBasedGame sg) throws SlickException {
       this.init(gc, sg);
    }
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        this.piece=Vars.piece;
        this.promo=Vars.promo;
        this.p1=Vars.p1;
        if(p1)player='W';
        else player='B';
        pieces=new Image[promo.length];    
        for(int c=0;c<promo.length;c++){
            if(new File("game\\"+promo[c][1]+player+".png").exists())pieces[c]=new Image("game\\"+promo[c][1]+player+".png");
            else pieces[c]=new Image("immutable\\DEFAULT.png");
        }
        game=sbg;
        timer=0;
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
       grphcs.setBackground(Color.black);
       
        for(int c=0;c<promo.length;c++){
           grphcs.setColor(Color.white);
           grphcs.fillRect(0,(640/promo.length)*c,640,(640/promo.length)-1);
           grphcs.drawImage(pieces[c],0,(640/promo.length)*c);
           String output=promo[c][1]+" ("+promo[c][0]+")";
           grphcs.setColor(Color.black);
           grphcs.drawString(output,(640-grphcs.getFont().getWidth(output))/2,(640/promo.length)*c);
       }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        int posX=Mouse.getX();
        int posY=Mouse.getY();
        if(timer>0)timer--;
        else if(timer<0)timer++;
        if(timer==0){
        if(Mouse.isButtonDown(0)){
            for(int c=promo.length-1;c>=0;c--){
            if(640-posY>(640/promo.length)*c){
                Vars.betza=promo[c][0];
                Vars.name=promo[c][1];
                c=-1;
                timer=-6;
            }
        }
        }
    }
        else if(timer==-1)game.enterState(3, new FadeOutTransition(Color.black), new FadeInTransition(Color.white));
    }
}
