/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualchess;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class VisualChess extends StateBasedGame{
     public static void main(String[] args) throws SlickException {
          
      AppGameContainer app = new AppGameContainer(new VisualChess("Chess"));
      app.setShowFPS(false);
      app.setDisplayMode(640, 640, false);
      app.setTargetFrameRate(60);
      app.start();
    }

    public VisualChess(String title) {
        super(title);
    }

@Override
    public void initStatesList(GameContainer container) throws SlickException {
        this.addState(new Play(0));
        this.addState(new PawnPromoter(1));
     
 }
}