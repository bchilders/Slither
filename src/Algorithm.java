import java.util.ArrayList;
import java.io.*;

public class Algorithm {
	gameBoard daBoard;
	
	int totalBlockPoints;
	
	ChoiceQueue Q;
	ChoiceQueue seedQ;
	
	Algorithm(gameBoard gb){
		daBoard = gb;
	}
	
	ArrayList<ArrayList<Move>> moveListList;
	
	public void runAlgorithm(){
		setup();
		
		Choice workingChoice;
		
		while(true){//runs until isComplete breaks the loop
			Q.sort();//sort the choice q
			workingChoice = Q.pop();//pop off the next in line
			workingChoice.showPrint();//put it on the board
			if(daBoard.isComplete()){//check if it solves the puzzle
				break;
			}else if(!isFail(workingChoice)){//if it hasn't broken a rule
				ArrayList<Choice> newChoices = workingChoice.eval();//find its children
				for(int i = 0;i<newChoices.size();i++){//add them to the choice q
					Q.add(newChoices.get(i));
				}
			}
			workingChoice.show();//remove choice from the board
			
		}
		
	}
	
	//checks the board to see that there are no failure conditions
	private boolean isFail(Choice choice){
		if(isInMoveListList(choice.moveList)){return true;}
		
		for(int row = 1; row<=daBoard.getNumRows(); row++){
			for(int col = 1; col<=daBoard.getNumCols(); col++){
				
				if(daBoard.getBlockVal(row, col)=='0'){
					if(daBoard.getSurrValCount(row, col)[1]>0){//if there are any lines
						return true;
					}
				}else if(daBoard.getBlockVal(row, col)=='1'){
					if(daBoard.getSurrValCount(row, col)[2]>3){//if it is surrunded by xs
						return true;
					}else if(daBoard.getSurrValCount(row, col)[1]>1){//if it has more than 1 line
						return true;
					}
				}else if(daBoard.getBlockVal(row, col)=='2'){
					if(daBoard.getSurrValCount(row, col)[1]>2){//if it has more than 2 lines
						return true;
					}else if(daBoard.getSurrValCount(row, col)[2]>2){//if it has more than 2 xs
						return true;
					}
				}else if(daBoard.getBlockVal(row, col)=='3'){
					if(daBoard.getSurrValCount(row, col)[1]>3){//if it has more than 3 lines
						return true;
					}else if(daBoard.getSurrValCount(row, col)[2]>1){//if it has more than 1 x
						return true;
					}
				}
			}
		}
		
		for(int row = 1; row<=daBoard.getNumRows()+1; row++){
			for(int col = 1; col<=daBoard.getNumCols()+1; col++){
				if(daBoard.getPointSurrValCount(row, col)[1]==1 &&daBoard.getPointSurrValCount(row, col)[2]==3){
					return true;
				}else if(daBoard.getPointSurrValCount(row, col)[1]>2){
					return true;
				}
			}
		}
		
		moveListList.add(choice.moveList);
		return false;
	}
	
	private void setup(){
		moveListList = new ArrayList<ArrayList<Move>>();
		Q= new ChoiceQueue();
		ArrayList<Move> tempRemList = new ArrayList<Move>();
		ArrayList<Move> tempMoveList = new ArrayList<Move>();
		this.totalBlockPoints=daBoard.getTotalBlockPoints();
		
		for(int row = 1; row<=daBoard.getNumRows(); row++){//x 0s
			for(int col = 1; col<=daBoard.getNumCols(); col++){
					if(daBoard.getBlockVal(row, col)=='0'){
					tempMoveList.add(daBoard.makeMove(new Move(row,col,'t',true)));
					tempMoveList.add(daBoard.makeMove(new Move(row,col,'b',true)));
					tempMoveList.add(daBoard.makeMove(new Move(row,col,'l',true)));
					tempMoveList.add(daBoard.makeMove(new Move(row,col,'r',true)));
					tempRemList.add(new Move(row,col,'t',true));
					tempRemList.add(new Move(row,col,'b',true));
					tempRemList.add(new Move(row,col,'l',true));
					tempRemList.add(new Move(row,col,'r',true));
				}
			}
		}
		
		for(int row = 1; row<=daBoard.getNumRows(); row++){//look for 3 corners and xs on 3s
			for(int col = 1; col<=daBoard.getNumCols(); col++){
				if(daBoard.getBlockVal(row, col)=='3'){
					
					//xs on 3s
					if(daBoard.getSpaceVal(row, col, 't')=='x'){
						tempMoveList.add(new Move(row, col, 'r', false));
						tempMoveList.add(new Move(row, col, 'b', false));
						Q.add(new Choice(
								new SnakeHead(row,col,'l',SnakeHead.NORTH, daBoard),
								tempMoveList,
								daBoard
								));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
					if(daBoard.getSpaceVal(row, col, 'r')=='x'){
						tempMoveList.add(new Move(row, col, 'b', false));
						tempMoveList.add(new Move(row, col, 'l', false));
						Q.add(new Choice(
								new SnakeHead(row,col,'t',SnakeHead.EAST, daBoard),
								tempMoveList,
								daBoard
								));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
					if(daBoard.getSpaceVal(row, col, 'b')=='x'){
						tempMoveList.add(new Move(row, col, 'l', false));
						tempMoveList.add(new Move(row, col, 't', false));
						Q.add(new Choice(
								new SnakeHead(row,col,'r',SnakeHead.SOUTH, daBoard),
								tempMoveList,
								daBoard
								));
						daBoard.makeCollMove(tempRemList);
						return;
						
						
					}
					if(daBoard.getSpaceVal(row, col, 'l')=='x'){
						tempMoveList.add(new Move(row, col, 't', false));
						tempMoveList.add(new Move(row, col, 'r', false));
						Q.add(new Choice(
								new SnakeHead(row,col,'b',SnakeHead.WEST, daBoard),
								tempMoveList,
								daBoard
								));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
					
					//corners
					SnakeHead snake = new SnakeHead(row, col, 't', SnakeHead.EAST, daBoard);
					if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//top right
						Q.add(new Choice(snake, new ArrayList<Move>(),daBoard));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
					snake = snake.getCWCoord();
					if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//bottom right
						Q.add(new Choice(snake, new ArrayList<Move>(),daBoard));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
					snake = snake.getCWCoord();
					if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//bottom left
						Q.add(new Choice(snake, new ArrayList<Move>(),daBoard));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
					snake = snake.getCWCoord();
					if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//top left
						Q.add(new Choice(snake, new ArrayList<Move>(),daBoard));
						daBoard.makeCollMove(tempRemList);
						return;
						
					}
				}
			}
		}
		
		for(int row = 1; row<=daBoard.getNumRows(); row++){//just grab something and start chugging
			for(int col = 1; col<=daBoard.getNumCols(); col++){
				if(daBoard.getBlockVal(row, col)=='3'||daBoard.getBlockVal(row, col)=='2'||daBoard.getBlockVal(row, col)=='1'){
					Q.add(new Choice(
							new SnakeHead(row,col,'b',SnakeHead.WEST, daBoard),
							new ArrayList<Move>(),
							daBoard
							));
					Q.add(new Choice(
							new SnakeHead(row,col,'l',SnakeHead.NORTH, daBoard),
							new ArrayList<Move>(),
							daBoard
							));
					Q.add(new Choice(
							new SnakeHead(row,col,'t',SnakeHead.EAST, daBoard),
							new ArrayList<Move>(),
							daBoard
							));
					Q.add(new Choice(
							new SnakeHead(row,col,'r',SnakeHead.SOUTH, daBoard),
							new ArrayList<Move>(),
							daBoard
							));
				}
			}
		}
		daBoard.makeCollMove(tempRemList);
		return;
	}
	
//	private class SnakeHeadMoveListPackage{
//		public ArrayList<SnakeHead> snakeHeadList;
//		public ArrayList<Move> moveList;
//		SnakeHeadMoveListPackage(){
//			this.snakeHeadList= new ArrayList<SnakeHead>();
//			this.moveList= new ArrayList<Move>();
//		}
//	}
	
	public boolean isInMoveListList(ArrayList<Move> m){
		for(ArrayList<Move> ml : moveListList){
			if(isMoveListEqual(m,ml)) return true;
		}
		return false;
	}
	
	public boolean isMoveListEqual(ArrayList<Move> m1, ArrayList<Move> m2){
		if(m1.size()!=m2.size()) return false;
		for(Move m : m1){
			boolean isThere = false;
			for(Move n : m2){
				if(m.equals(n)){
					isThere = true;
					//m1.remove(m);
					//m2.remove(n);
				}
			}
			if(!isThere){
				return false;
			}
		}
		return true;
	}
	
	/*attempt at adding more rules in encapsulated form
	private snakeHeadMoveListPackage rule3xBlock(int row, int col){
		SnakeHeadMoveListPackage shmlp = new SnakeHeadMoveListPackage();
		if(daBoard.getSurrValCount(row, col)[2]==1){
			SnakeHead snake = new SnakeHead(row, col, t, SnakeHead.EAST, daBoard);
			for(int i = 0; 1<4; i++){
				if(snake.getCurrentVal()=='x'){
					shmlp.snakeHeadList.add(new SnakeHead(row, col, r, ))
				}
			}
		}
	}
	
	/*
	public void runrules(){
		boolean isBoardDiff = true;
		
		while(isBoardDiff){
			isBoardDiff = false;//must flip before the end of the loop, or loop ends
			for(int row = 1; row<=daBoard.getNumRows(); row++){//for each block
				for(int col = 1; col<=daBoard.getNumCols(); col++){
					
					//3x
					if(rule3x(row,col)){isBoardDiff=true;}
					
				}
			}
			
		}
	}
	
	//rules
	//point rules
	public boolean rule3x(int row, int col){
		boolean isChanged = false;
		if(daBoard.getSpaceVal(row, col, 't') == 'x'){//if an x is on top
			if(daBoard.getSpaceVal(row-1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row-1, col-1, 'b')=='x' && daBoard.getSpaceVal(row, col, 'l')==' '){//on the left
				daBoard.makeMove(new Move(row, col, 'l', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row-1, col-1, 'b')==' ' && daBoard.getSpaceVal(row, col, 'l')=='x'){
				daBoard.makeMove(new Move(row-1, col-1, 'b', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col-1, 'r') == ' ' && daBoard.getSpaceVal(row-1, col-1, 'b')=='x' && daBoard.getSpaceVal(row, col, 'l')=='x'){
				daBoard.makeMove(new Move(row-1, col-1, 'r', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row-1, col+1, 'b')=='x' && daBoard.getSpaceVal(row, col, 'r')==' '){//on the right
				daBoard.makeMove(new Move(row, col, 'r', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row-1, col+1, 'b')==' ' && daBoard.getSpaceVal(row, col, 'r')=='x'){
				daBoard.makeMove(new Move(row-1, col+1, 'b', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col+1, 'l') == ' ' && daBoard.getSpaceVal(row-1, col+1, 'b')=='x' && daBoard.getSpaceVal(row, col, 'r')=='x'){
				daBoard.makeMove(new Move(row-1, col+1, 'l', true));
				isChanged = true;
			}
		} else if(daBoard.getSpaceVal(row, col, 'b') == 'x'){//if an x is on bottom
			if      (daBoard.getSpaceVal(row+1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row+1, col-1, 't')=='x' && daBoard.getSpaceVal(row, col, 'l')==' '){//on the left
				daBoard.makeMove(new Move(row, col, 'l', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row+1, col-1, 't')==' ' && daBoard.getSpaceVal(row, col, 'l')=='x'){
				daBoard.makeMove(new Move(row+1, col-1, 't', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col-1, 'r') == ' ' && daBoard.getSpaceVal(row+1, col-1, 't')=='x' && daBoard.getSpaceVal(row, col, 'l')=='x'){
				daBoard.makeMove(new Move(row+1, col-1, 'r', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row+1, col+1, 't')=='x' && daBoard.getSpaceVal(row, col, 'r')==' '){//on the right
				daBoard.makeMove(new Move(row, col, 'r', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row+1, col+1, 't')==' ' && daBoard.getSpaceVal(row, col, 'r')=='x'){
				daBoard.makeMove(new Move(row+1, col+1, 't', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col+1, 'l') == ' ' && daBoard.getSpaceVal(row+1, col+1, 't')=='x' && daBoard.getSpaceVal(row, col, 'r')=='x'){
				daBoard.makeMove(new Move(row+1, col+1, 'l', true));
				isChanged = true;
			}
		}else if(daBoard.getSpaceVal(row, col, 'r') == 'x'){//if an x is right
			if      (daBoard.getSpaceVal(row+1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row+1, col+1, 't')=='x' && daBoard.getSpaceVal(row, col, 'b')==' '){//on the bottom
				daBoard.makeMove(new Move(row, col, 'b', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row+1, col+1, 't')==' ' && daBoard.getSpaceVal(row, col, 'b')=='x'){
				daBoard.makeMove(new Move(row+1, col+1, 't', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col+1, 'l') == ' ' && daBoard.getSpaceVal(row+1, col+1, 't')=='x' && daBoard.getSpaceVal(row, col, 'b')=='x'){
				daBoard.makeMove(new Move(row+1, col+1, 'l', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row-1, col+1, 'b')=='x' && daBoard.getSpaceVal(row, col, 't')==' '){//on the top
				daBoard.makeMove(new Move(row, col, 't', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col+1, 'l') == 'x' && daBoard.getSpaceVal(row-1, col+1, 'b')==' ' && daBoard.getSpaceVal(row, col, 't')=='x'){
				daBoard.makeMove(new Move(row-1, col+1, 'b', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col+1, 'l') == ' ' && daBoard.getSpaceVal(row-1, col+1, 'b')=='x' && daBoard.getSpaceVal(row, col, 't')=='x'){
				daBoard.makeMove(new Move(row-1, col+1, 'l', true));
				isChanged = true;
			}
		}else if(daBoard.getSpaceVal(row, col, 'b') == 'x'){//if an x is on left
			if      (daBoard.getSpaceVal(row+1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row+1, col-1, 't')=='x' && daBoard.getSpaceVal(row, col, 'b')==' '){//on the bottom
				daBoard.makeMove(new Move(row, col, 'b', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row+1, col-1, 't')==' ' && daBoard.getSpaceVal(row, col, 'b')=='x'){
				daBoard.makeMove(new Move(row+1, col-1, 't', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row+1, col-1, 'r') == ' ' && daBoard.getSpaceVal(row+1, col-1, 't')=='x' && daBoard.getSpaceVal(row, col, 'b')=='x'){
				daBoard.makeMove(new Move(row+1, col-1, 'r', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row-1, col-1, 'b')=='x' && daBoard.getSpaceVal(row, col, 't')==' '){//on the top
				daBoard.makeMove(new Move(row, col, 't', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col-1, 'r') == 'x' && daBoard.getSpaceVal(row-1, col-1, 'b')==' ' && daBoard.getSpaceVal(row, col, 't')=='x'){
				daBoard.makeMove(new Move(row-1, col-1, 'b', true));
				isChanged = true;
			}else if(daBoard.getSpaceVal(row-1, col-1, 'r') == ' ' && daBoard.getSpaceVal(row-1, col-1, 'b')=='x' && daBoard.getSpaceVal(row, col, 't')=='x'){
				daBoard.makeMove(new Move(row-1, col-1, 'r', true));
				isChanged = true;
			}
		}
		
		return isChanged;
	}*/
	
}
