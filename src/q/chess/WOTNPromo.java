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
import java.util.ArrayList;
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
public class WOTNPromo extends BasicGameState{
    private static StateBasedGame game;
    private int stateID=-1,timer=0;
    char player;
    int level,alignment;
    String[] promoBoardB;
    String[] promoBoardN;
    ArrayList<Image> pieces;
    public WOTNPromo(int stateID){
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
        this.level=WOTNVars.level;
        this.alignment=WOTNVars.alignment;
        this.promoBoardB=WOTNChessPiece.CLASSLIST[level+1];
        this.promoBoardN=WOTNChessPiece.NAMELIST[level+1];
        this.player=WOTNVars.player;
        this.pieces=new ArrayList<>();
        
        for(int c=0;c<3;c++){
                if(!promoBoardN[c].isEmpty()&&Math.abs(c-alignment)<=1){
                    pieces.add(new Image("WOTN\\"+promoBoardN[c]+player+".png"));
                    pieces.get(pieces.size()-1).setName(Integer.toString(c));
                }
            }
        
        
        
        game=sbg;
        }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics grphcs) throws SlickException {
       grphcs.setBackground(Color.black);
       
        for(int c=0;c<pieces.size();c++){
           grphcs.setColor(Color.white);
           grphcs.fillRect(0,(640/pieces.size())*c,640,(640/pieces.size())-1);
           grphcs.drawImage(pieces.get(c),0,(640/pieces.size())*c);
           String output;
           switch(pieces.get(c).getName()){
               case"0":output="Way of the Knight: "+promoBoardN[0]+" ("+promoBoardB[0]+")";
               break;
               case"1":output="Way of the Middle: "+promoBoardN[1]+" ("+promoBoardB[1]+")";
               break;
               case"2":output="Way of the Bishop: "+promoBoardN[2]+" ("+promoBoardB[2]+")";
               break;
               default: output="";
               break;
               
           }
               grphcs.setColor(Color.black);
           grphcs.drawString(output,(640-grphcs.getFont().getWidth(output))/2,(640/pieces.size())*c);
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
            for(int c=pieces.size()-1;c>=0;c--){
            if(640-posY>(640/pieces.size())*c){
                WOTNVars.betza=promoBoardB[Integer.parseInt(pieces.get(c).getName())];
                WOTNVars.name=promoBoardN[Integer.parseInt(pieces.get(c).getName())]+player;
                c=-1;
                timer=-6;
            }
        }
    }
    }
        else if(timer==-1)game.enterState(5, new FadeOutTransition(Color.black), new FadeInTransition(Color.white));
    }
}
