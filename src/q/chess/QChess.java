/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q.chess;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import java.util.ArrayList;
import java.io.FileNotFoundException;
public class QChess extends StateBasedGame{
     public static void main(String[] args) throws SlickException {
      AppGameContainer app = new AppGameContainer(new QChess("Visual Chess: Undercarriage Syndicate"));
      app.setShowFPS(false);
      app.setDisplayMode(640, 640, false);
      app.setTargetFrameRate(60);
      app.start();
    }

    public QChess(String title) {
        super(title);
    }

@Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new Menu(0));
        this.addState(new visualchess.Play(1));
        this.addState(new visualchess.PawnPromoter(2));
        this.addState(new Play(3));        
        this.addState(new BetzaPromo(4));
        this.addState(new WOTN(5));
        this.addState(new WOTNPromo(6));
        
               
 }
}