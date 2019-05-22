/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualchess;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class PawnPromoter extends BasicGameState{
 private static StateBasedGame game;
 private boolean end = false;
 private boolean p1;
 private char type;
 private int stateID = -1,timer;
 private String name,use;
 private char[] piece={'q','r','n','b'};
 private Image[] pieces=new Image[4];
public PawnPromoter(int stateID){
    
     this.stateID = stateID;
     
}
    @Override
    public int getID() {
        return stateID;
      }
    @Override
    public void enter(GameContainer gc, StateBasedGame sg) throws SlickException {
       this.init(gc, sg);
    }
    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        timer=5;
        p1=Play.p1;
     if(p1)type='l';
     else type='d';
     for(int c=0;c<4;c++){
         name="immutable\\Chess"+piece[c]+type+".png";
         pieces[c]=new Image(name);
     }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
       grphcs.setBackground(Color.white);
       grphcs.drawImage(pieces[0], 0, 0);
       grphcs.drawImage(pieces[1], 320, 0);
       grphcs.drawImage(pieces[2], 0, 320);
       grphcs.drawImage(pieces[3], 320, 320);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
        game=sbg;
        if(timer>0)timer--;
        else if(timer<0)timer++;
        int posX=Mouse.getX();
        int posY=Mouse.getY();
        if(timer==0){
            if(Mouse.isButtonDown(0)){
            if(posX<320){
                if(posY>=320){
                    use="q";
                }
                else {
                   use="n"; 
                }
            }
            else{
                if(posY>=320){
                    use="r";
                }
                else {
                    use="b";
                }
            }
            if(p1)use=use.toUpperCase();
            Vars.pawn=use.charAt(0);
            Vars.choice=use.toLowerCase();
            timer=-6;
        }
        }
        else if(timer==-1)game.enterState(1, new FadeOutTransition(Color.white), new FadeInTransition(Color.black));
      if(end)gc.exit();
    }
}
