import java.io.*;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {

		String gameBoardPath = args[0];
		 
		boolean newGame = true;
		Scanner userInput = new Scanner(System.in);
		
		while(newGame){
			game g = new game(gameBoardPath);
//			System.out.println("Would you like to play again? [Y/n]");
//			String ans = userInput.next();
//			if(ans.charAt(0) == 'n'){
//				newGame = false;
//				System.out.println("Daisy, daiiiisy giiive me your aaannswer, DOOOoooo...");
//				
//				break;
//			}
			break;

		}
		userInput.close();
		
		
		
//		gameBoard g = new gameBoard();
//		g.populateBoard("C:\\Users\\User\\Documents\\slither3.txt");
//		
//		g.makeMove(1, 1, 'b');
//		g.makeMove(1, 1, 'r');
//		g.makeMove(1, 2, 't');
//		g.makeMove(1, 2, 'r');
//		g.makeMove(1, 3, 'b');
//		g.makeMove(1, 3, 'r');
//		g.makeMove(1, 4, 't');
//		g.makeMove(1, 4, 'r');
//		g.makeMove(1, 5, 'b');
//		
//		g.makeMove(2, 1, 'l');
//		g.makeMove(2, 5, 'r');
//		
//		g.makeMove(3, 1, 't');
//		g.makeMove(3, 1, 'r');
//		g.makeMove(3, 2, 'b');
//		g.makeMove(3, 2, 'r');
//		g.makeMove(3, 3, 't');
//		g.makeMove(3, 3, 'r');
//		g.makeMove(3, 4, 'r');
//		g.makeMove(3, 5, 't');
//		
//		g.makeMove(4, 4, 'r');
//		g.makeMove(4, 4, 'l');
//		
//		g.makeMove(5, 1, 'l');
//		g.makeMove(5, 1, 't');
//		g.makeMove(5, 1, 'b');
//		g.makeMove(5, 2, 't');
//		g.makeMove(5, 2, 'b');
//		g.makeMove(5, 3, 'b');
//		g.makeMove(5, 3, 't');
//		g.makeMove(5, 4, 'b');
//		g.makeMove(5, 5, 'b');
//		g.makeMove(5, 5, 't');
//		g.makeMove(5, 5, 'r');
//		
//		g.isComplete();
		
		
//		Matrix daMat = new Matrix(3,3);
//		
//		daMat.setVal(0, 0, 'a');
//		daMat.setVal(0, 1, 'b');
//		daMat.setVal(0, 2, 'c');
//		daMat.setVal(1, 0, 'd');
//		daMat.setVal(1, 2, 'a');
//		daMat.setVal(2, 0, 'b');
//		daMat.setVal(2, 1, 'c');
//		daMat.setVal(2, 2, 'd');
//		
//		daMat.printMat();
		//System.out.println(daMat.getVal(0, 1));

	}

}
