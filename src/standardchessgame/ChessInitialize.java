/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package standardchessgame;

import java.util.Scanner;

/**
 *
 * @author alinger2442
 */
public class ChessInitialize {
    public ChessInitialize(){
        
    }
    public void startGame(){
        ChessBoard board = new ChessBoard();
    Scanner input=new Scanner(System.in);
    boolean p1,cont=true,win,pieceThere,goodMove;
    String playnum,move;
    
    while(cont){ 
        p1=true;
        board.initBoard();
        board.displayBoard();
        win=false;
        while(!win){
           if(p1)playnum="White";
           else playnum="Black";
           //repeats until a position with a valid move belonging to player is chosen
           do{
               System.out.print(playnum + " enter a piece: ");
               move=input.nextLine().toLowerCase();
               pieceThere=board.pieceSelect(move,p1);  
           }while(!pieceThere);
           //repeats until a move is chosen that is on the list
           do{
             System.out.print(playnum + " enter a move: ");
             move=input.nextLine().toLowerCase();
             goodMove=board.moveSelect(move);
             if(!goodMove)System.out.println("Input a valid move!");
           }while(!goodMove);
           //changes board to reflect move
           board.playMove(move,p1);
           //displays board again
           board.displayBoard();
           //checks for check(mate)
           win=board.checkVictory(p1);
           p1=!p1;
        }
        System.out.println("Say \"Stop\" if you're done playing, otherwise you can play again.");
        if(input.nextLine().equals("Stop"))cont=false;
    }  
    }
}
