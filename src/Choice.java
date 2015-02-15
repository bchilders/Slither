import java.util.ArrayList;
import java.io.*;

public class Choice {

	gameBoard gb;
	ArrayList<Move> moveList;
	int blockPoints;//how many lines remain to be drawn for all blocks to be satisfied
	int priority;//blockpoints+movelist.size
	SnakeHead snake;
	boolean failBit;
	
	public void show(){//makes all moves in the moveList. If run a second time, will remove all moves in the movelist from the board
		gb.makeCollMove(moveList);
	}
	
	public void showPrint(){//makes all moves in the moveList. If run a second time, will remove all moves in the movelist from the board
		gb.makeCollMovePrint(moveList);
	}
	
	public ArrayList<Choice> eval(){
		ArrayList<Choice> arr = new ArrayList<Choice>();
		ArrayList<SnakeHead> shl = this.gb.getSnakeHeads();
		for(SnakeHead s : shl){
			if(s.getCCWVal()==' '){
				arr.add(new Choice(
						s.getCCWCoord(),
						this.moveList,
						this.gb
						));
			}
			if(s.getForwardVal()==' '){
				arr.add(new Choice(
						s.getForwardCoord(),
						this.moveList,
						this.gb
						));
			}
			if(s.getCWVal()==' '){
				arr.add(new Choice(
						s.getCWCoord(),
						this.moveList,
						this.gb
						));
			}	
		}
		return arr;
	}
	
	Choice(SnakeHead snake, ArrayList<Move> parentMoveList, gameBoard gb/*, int parentBlockPts*/){
		this.snake = snake;
		this.gb = gb;
		gameBoard tempBoard = new gameBoard(gb);
		
		this.moveList = new ArrayList<Move>();
		for(int i=0;i<parentMoveList.size();i++){
			this.moveList.add(i,parentMoveList.get(i));
		}
		this.moveList.add(new Move(snake.row,snake.col,snake.side, false));//add the next line to moveList
		
		tempBoard.makeCollMove(this.moveList);//used for building up moves inside the choice
		int prevSize=-1;
		//apply as many rules as possible
		while(prevSize<moveList.size()){
			prevSize=moveList.size();
			for(int row = 1; row<=tempBoard.getNumRows();row++){
				for(int col =1; col<=tempBoard.getNumCols();col++){
					if( hasRequisiteLines(row,col,tempBoard)){
						
						for(Move m :tempBoard.fillSurrBlockVals(row, col, true)){
							moveList.add(m);
						}
					}
					if(hasRequisiteXs(row,col,tempBoard)){
						
						for(Move m :tempBoard.fillSurrBlockVals(row, col, false)){
							moveList.add(m);
						}
					}
					if(is3InCorner(row,col,tempBoard)){
						
						boolean [] cornerArr =tempBoard.isBlockInCorner(row, col);
						for(int i=0;i<4;i++){
							if(cornerArr[i]){
								int[] coord = getCornerCoord(row, col, i);
								for(Move m : tempBoard.fillSurrPointVals(coord[0], coord[1], false)){
									moveList.add(m);
								}
							}
						}
					}
					if(is1InCorner(row,col,tempBoard)){
						
						boolean [] cornerArr =tempBoard.isBlockInCorner(row, col);
						for(int i=0;i<4;i++){
							if(cornerArr[i]){
								int[] coord = getCornerCoord(row, col, i);
								for(Move m : tempBoard.fillSurrPointVals(coord[0], coord[1], true)){
									moveList.add(m);
								}
							}
						}
					}
					boolean [] arr;
					if(ors(arr = is3Adj3(row,col,tempBoard))){
						do3Adj3(row, col, arr, tempBoard);

					}
						
				}
			}
			for(int row = 1; row<=tempBoard.getNumRows()+1;row++){
				for(int col =1; col<=tempBoard.getNumCols()+1;col++){
					if(is3XPoint(row,col,tempBoard)){
						
						for(Move m :tempBoard.fillSurrPointVals(row, col, true)){
							moveList.add(m);
						}
					}
					if(is2LinePoint(row,col,tempBoard)){
						
						for(Move m :tempBoard.fillSurrPointVals(row, col, true)){
							moveList.add(m);
						}
					}
					if(isLineExtender(row,col,tempBoard)){
						
						for(Move m :tempBoard.fillSurrPointVals(row, col, false)){
							moveList.add(m);
						}
					}
				}
			}
			
		}
		
		//check for unconnected loops
//		this.failBit=checkLoops(this.moveList,tempBoard);
		
		
		//if the choice makes a connection, there is a good chance it is either a success or a fail
		//for this reason, we should break the algorithm a bit, and put it at the front of the queue
		//if it is a success, the algorithm finishes much quicker
		//if it is a fail, it is gotten out of the sorting algorithms way
		if(snake.getCCWVal()=='-'||snake.getCCWVal()=='|'||
				snake.getForwardVal()=='-'||snake.getForwardVal()=='|'||
				snake.getCWVal()=='-'||snake.getCWVal()=='|'){
			this.priority=0;
		}else{
			//calculating blockpoints
			this.blockPoints = 0;
			for(int row =1;row<=tempBoard.getNumRows();row++){
				for(int col=1;col<=tempBoard.getNumCols();col++){
					if(gb.getBlockVal(row, col)!=' '){
						int blkVal = Character.getNumericValue(gb.getBlockVal(row, col));
						int blkCnt = tempBoard.getSurrValCount(row, col)[1];
						if(blkVal>blkCnt){
							this.blockPoints+=(blkVal-blkCnt);
						}
					}
				}
			}
			
			//calc number of lines
			int numLines=0;
			for(Move m : moveList){
				if(!m.isX) numLines++;
			}
					
			this.priority = blockPoints+numLines;	
		}
		 
	}
	


	//rule checks
	private boolean hasRequisiteLines(int row, int col, gameBoard tempBoard){
		if(isFull(tempBoard.getSurrValCount(row, col))) return false;
		int numLines = tempBoard.getSurrValCount(row, col)[1];
		int blkVal=Character.getNumericValue(tempBoard.getBlockVal(row, col));
		if(numLines == blkVal) return true;
		return false;
	}
	
	private boolean hasRequisiteXs(int row, int col, gameBoard tempBoard){
		if(isFull(tempBoard.getSurrValCount(row, col))) return false;
		int numXs = tempBoard.getSurrValCount(row, col)[2];
		int blkVal=Character.getNumericValue(tempBoard.getBlockVal(row, col));
		if(numXs == (4-blkVal)) return true;
		return false;
	}
	
	private boolean is3XPoint(int row, int col, gameBoard tempBoard){
		if(isFull(tempBoard.getPointSurrValCount(row, col))) return false;
		int numXs=tempBoard.getPointSurrValCount(row, col)[2];
		if (numXs==3) return true;
		return false;
		
	}
	
	private boolean is2LinePoint(int row, int col, gameBoard tempBoard){
		if(isFull(tempBoard.getPointSurrValCount(row, col))) return false;
		int numLines=tempBoard.getPointSurrValCount(row, col)[1];
		if (numLines==2) return true;
		return false;
	}
	private boolean isLineExtender(int row, int col, gameBoard tempBoard){
		if(isFull(tempBoard.getPointSurrValCount(row, col))) return false;
		int numLines=tempBoard.getPointSurrValCount(row, col)[1];
		int numXs=tempBoard.getPointSurrValCount(row, col)[2];
		if (numLines==1 && numXs==2) return true;
		return false;
	}
	private boolean is3InCorner(int row, int col, gameBoard tempBoard){
		if(Character.getNumericValue(tempBoard.getBlockVal(row, col))!=3)return false;
		boolean[] cornerArr = tempBoard.isBlockInCorner(row, col);
		char[] sideArr = tempBoard.getSurrVals(row, col);
		if(cornerArr[0]){
			if(sideArr[0]==' '&& sideArr[3]==' '){
				return true;
			}
		}else if(cornerArr[1]){
			if((sideArr[0]==' '&&sideArr[1]==' ')){
				return true;
			}
		}else if(cornerArr[2]){
			if((sideArr[2]==' '&&sideArr[1]==' ')){
				return true;
			}
		}else if(cornerArr[3]){
			if((sideArr[2]==' '&&sideArr[3]==' ')){
				return true;
			}
		}
		return false;
	}
	
	private int[] getCornerCoord(int row, int col, int corner ){
		int[] arr=new int[2];
		if(corner==0){
			arr[0]=row;
			arr[1]=col;
		} else if(corner==1){
			arr[0]=row;
			arr[1]=col+1;
		} else if(corner==2){
			arr[0]=row+1;
			arr[1]=col+1;
		} else if(corner==3){
			arr[0]=row+1;
			arr[1]=col;
		} else{
			arr[0]=-1;
			arr[1]=-1;
		}
		return arr;
	}
	
	private boolean is1InCorner(int row, int col, gameBoard tempBoard){
		if(Character.getNumericValue(tempBoard.getBlockVal(row, col))!=1)return false;
		boolean[] cornerArr = tempBoard.isBlockInCorner(row, col);
		char[] sideArr = tempBoard.getSurrVals(row, col);
		if(cornerArr[0]){
			if((sideArr[0]==' '&&sideArr[3]==' ')){
				return true;
			}
		}else if(cornerArr[1]){
			if((sideArr[0]==' '&&sideArr[1]==' ')){
				return true;
			}
		}else if(cornerArr[2]){
			if((sideArr[2]==' '&&sideArr[1]==' ')){
				return true;
			}
		}else if(cornerArr[3]){
			if((sideArr[2]==' '&&sideArr[3]==' ')){
				return true;
			}
		}
		return false;
	}

	private boolean[] is3Adj3(int row, int col, gameBoard tempBoard){
		boolean[]arr={false, false, false, false};
		if(tempBoard.getBlockVal(row,col)==3){
			if(tempBoard.getBlockVal(row-1, col)=='3'){
				arr[0]=true;
			}
			if(tempBoard.getBlockVal(row, col+1)=='3'){
				arr[0]=true;
			}
			if(tempBoard.getBlockVal(row+1, col)=='3'){
				arr[0]=true;
			}
			if(tempBoard.getBlockVal(row, col-1)=='3'){
				arr[0]=true;
			}
		}
		return arr;
	}
	
	private boolean ors(boolean[] arr){
		for(int i =0; i<arr.length;i++){
			if(arr[i]) return true;
		}
		return false;
	}
	 private void do3Adj3(int row, int col, boolean[]arr,gameBoard tempBoard){
		Move m;	
		if(arr[0]){
		 	m=new Move(row, col, 'b', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row, col, 't', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row-1, col, 't', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
		}
		if(arr[1]){
			 m=new Move(row, col, 'l', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row, col, 'r', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row, col+1, 'r', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
		}
		if(arr[2]){
			 m=new Move(row, col, 'b', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row, col, 't', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row+1, col, 'b', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
		}
		if(arr[3]){
			 m=new Move(row, col, 'r', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row, col, 'l', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
			m=new Move(row, col-1, 'l', false);
			if(tempBoard.makeMoveIfAvailable(m)){
				moveList.add(m);
			}
		}
	 }
	
	private boolean isFull(int[] arr){

		if((arr[1]+arr[2])==4) return true;
		return false;
	}
}
