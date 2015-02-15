import java.util.Scanner;
import java.io.*;

public class game {

	public game(String boardPath){
		boolean ai = false;
		//Scanner userInput2 = new Scanner(System.in);
		String path=boardPath;
		gameBoard daBoard = new gameBoard();
		
		//get path
		//System.out.println("Please supply the file path to your game board file:");
		//path = userInput2.nextLine();
		
		//ai branch
		//System.out.println("Would you like to use AI? [Y/n]");
		/*if(userInput2.nextLine().equals("Y"))*/ ai = true;
		
		//populateBoard
		daBoard.populateBoard(path);
		
		if(ai){
			Algorithm algo = new Algorithm(daBoard);
			algo.runAlgorithm();
			System.out.println("Puny, human. The computer wins.");
		}else{
			//loop makeMove
			while(!daBoard.isComplete()){
				System.out.println("what is your next move?");
			
				String uInput ="";// userInput2.nextLine();
				String[] in = uInput.split(" ");

				if(in.length == 3){				
					Move move = new Move(Integer.parseInt(in[0]) , Integer.parseInt(in[1]), in[2].charAt(0), false);
					daBoard.makeMovePrint(move);
				} else if(in.length == 4 && in[3].equals("x")){
					Move move = new Move(Integer.parseInt(in[0]) , Integer.parseInt(in[1]), in[2].charAt(0), true);
					daBoard.makeMovePrint(move);
				} else {
					System.out.println("Improper Input!");
				}
			

			}
			System.out.println("YOU WIN!");
		}
//		userInput2.close();
	}
	
	
}
