import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.io.*;

public class gameBoard {

	private Matrix cleanBoard;
	private Matrix board;
	
	String[] data;//string from the data file describing the board
	
	public gameBoard(){}
	
	public gameBoard(gameBoard gb){
		this.board=new Matrix(gb.cleanBoard);
		this.cleanBoard = new Matrix(gb.cleanBoard);
	}
	
	

	
	
	
	public boolean isComplete(){
		if (isCircuit() && fitsNums()){
			return true;
		}
		//System.out.println("YOU SUCK!");
		return false;
	
	}
	
	/*
	 * this checks if the current board has a complete circuit of lines. 
	 * It checks around every + to make sure it has either 0 or 2 lines around it.
	 */
	private boolean isCircuit(){
		String[] lineSplit = data[0].split(" ");
		int numRows = Integer.parseInt(lineSplit[0]);
		int numCols = Integer.parseInt(lineSplit[1]);
		ArrayList<Integer> rows = new ArrayList<Integer>();//used for isAllOne()
		ArrayList<Integer> cols = new ArrayList<Integer>();//used for isAllOne()
		
		for(int i = 0; i<=numRows; i++){
			for(int j = 0; j<=numCols; j++){
				int counter = 0;
				if (board.getVal(2+2*i -1, 2+2*j) != ' ' && board.getVal(2+2*i -1, 2+2*j) != 'x') counter++;//top
				if (board.getVal(2+2*i +1, 2+2*j) != ' ' && board.getVal(2+2*i +1, 2+2*j) != 'x'){
					counter++;//bottom
					rows.add(2+2*i +1);
					cols.add(2+2*j);
				}
				if (board.getVal(2+2*i, 2+2*j -1) != ' ' && board.getVal(2+2*i, 2+2*j -1) != 'x') counter++;//left
				if (board.getVal(2+2*i, 2+2*j +1) != ' ' && board.getVal(2+2*i, 2+2*j +1) != 'x'){
					counter++;//right
					rows.add(2+2*i);
					cols.add(2+2*j +1);
				}
				
				if (counter == 1 || counter == 3 || counter == 4) return false;
			}
		}
		
		if (rows.size()<4) return false;
		else if(!isAllOne(rows, cols)) return false;
		
		return true;
	}
	
	private boolean isAllOne(ArrayList<Integer> rows,ArrayList<Integer> cols){
		int index= 0;
		int curRow = rows.get(0);
		int curCol = cols.get(0);
		rows.remove(0);
		cols.remove(0);
		do{
			if(curRow%2 == 0){
				
				if((index = arrayRunThrough(rows,cols,curRow+1,curCol-1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow,curCol-2)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow-1,curCol-1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow+1,curCol+1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow,curCol+2)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow-1,curCol+1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else{
					return false;
				}
			}else{
				if((index = arrayRunThrough(rows,cols,curRow+1,curCol-1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow-2,curCol)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow-1,curCol-1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow+1,curCol+1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow+2,curCol)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else if((index = arrayRunThrough(rows,cols,curRow-1,curCol+1)) != -1){
					curRow=rows.get(index);
					curCol=cols.get(index);
					rows.remove(index);
					cols.remove(index);
				}else{
					return false;
				}
			}
		}while(rows.size() > 0);
		
		return true;
	}
	
	private int arrayRunThrough(ArrayList<Integer> rows, ArrayList<Integer>cols,int targRow,int targCol){
		for(int index = 0; index<rows.size(); index++){
			if(rows.get(index) == targRow && cols.get(index) == targCol){
				return index;
			}
		}
		
		return -1;
	}
	
	/*
	 * this checks that each number has the correct number of lines around it
	 */
	private boolean fitsNums(){

		for(int row = 1; row<=this.getNumRows(); row++){
			for(int col = 1; col<=this.getNumCols(); col++){
				if(this.getBlockVal(row, col)!=' '){
					int blockNum=Character.getNumericValue(this.getBlockVal(row, col));
					int numLines = this.getSurrValCount(row, col)[1];
					if( blockNum != numLines )//if the block number does not match the number of lines around it
						return false;
				}
			}
		}
		return true;
	}
	
	//Allows AI access data on the gameboard the way it makes moves


	
	
	
	
	public char getSpaceVal(int row, int col, char side){
		
		//convert from gameboard coords to matrix coords
		row = 1+2*row;
		col = 1+2*col;
		
		if(side=='t' || side=='T'){
			row-=1;
		} else if(side=='b'||side=='B'){
			row+=1;
		} else if(side=='l'||side=='L'){
			col-=1;
		} else if(side=='r'||side=='R'){
			col+=1;
		}
		
		return board.getVal(row, col);
	}
	
	public char getBlockVal(int row, int col){
		if(row<1 || col<1){
			return ' ';
		}
		
		row = 1+2*row;
		col = 1+2*col;
		return board.getVal(row, col);
	}
	
	public int getNumRows(){
		return (board.numRows-4)/2;
	}
	public int getNumCols(){
		return (board.numCols-4)/2;
	}

	
	
	
	
	
	
	public int getTotalBlockPoints(){
		int ans=0;
		for(int row=1;row<=this.getNumRows();row++){
			for(int col=1;col<=this.getNumCols();col++){
				ans+=Character.getNumericValue(this.getBlockVal(row, col));
			}
		}
		return ans;
	}
	
	/*
	 * returns a 3 item int array representing the state of the spaces surrounding a block
	 * index 0 is num of blank spaces
	 * index 1 is num of lines
	 * index 2 is num of xs
	 */
	public int[] getSurrValCount(int row, int col){
		int[] arr = {0,0,0};
		//top
		if(this.getSpaceVal(row, col, 't') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col, 't') == '-' || this.getSpaceVal(row, col, 't') == '|') arr[1]++;
		if(this.getSpaceVal(row, col, 't') == 'x') arr[2]++;
		//bottom
		if(this.getSpaceVal(row, col, 'b') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col, 'b') == '-' || this.getSpaceVal(row, col, 'b') == '|') arr[1]++;
		if(this.getSpaceVal(row, col, 'b') == 'x') arr[2]++;
		//left
		if(this.getSpaceVal(row, col, 'l') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col, 'l') == '-' || this.getSpaceVal(row, col, 'l') == '|') arr[1]++;
		if(this.getSpaceVal(row, col, 'l') == 'x') arr[2]++;
		//right
		if(this.getSpaceVal(row, col, 'r') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col, 'r') == '-' || this.getSpaceVal(row, col, 'r') == '|') arr[1]++;
		if(this.getSpaceVal(row, col, 'r') == 'x') arr[2]++;
		
		return arr;
	}
	
	/*
	 * returns a 4 item array representing the state of each space around a block
	 * index:0=t,1=r,2=b,3=l
	 * 
	 */
	public char[] getSurrVals(int row, int col){
		char[]arr={'e','e','e','e'};
		arr[0]=this.getSpaceVal(row,col,'t');
		arr[1]=this.getSpaceVal(row,col,'r');
		arr[2]=this.getSpaceVal(row,col,'b');
		arr[3]=this.getSpaceVal(row,col,'l');
		return arr;
	}
	
	//fills all empty spaces around a block with the value val
	//returns what it did an array of the moves it made.
	public ArrayList<Move> fillSurrBlockVals(int row, int col, boolean isX){
		ArrayList<Move> arr = new ArrayList<Move>();
		Move m;
		if(this.getSpaceVal(row, col, 't')==' '){
			m = new Move(row, col, 't', isX);
			arr.add(m);
			this.makeMove(m);
		}
		if(this.getSpaceVal(row, col, 'r')==' '){
			m = new Move(row, col, 'r', isX);
			arr.add(m);
			this.makeMove(m);
		}
		if(this.getSpaceVal(row, col, 'b')==' '){
			m = new Move(row, col, 'b', isX);
			arr.add(m);
			this.makeMove(m);
		}
		if(this.getSpaceVal(row, col, 'l')==' '){
			m = new Move(row, col, 'l', isX);
			arr.add(m);
			this.makeMove(m);
		}
		return arr;
	}
	
	//-1 indicates blank
	//-2 indicates no no
	public int[] getBlockValsFromPoint(int row, int col){
		int[] arr={-2,-2,-2,-2};
		char temp;
		//nw
		if(!(row==1 || col==1)){
			temp=getBlockVal(row-1, col-1);
			if(temp!=' '){
				arr[0]=Character.getNumericValue(temp);
			}
		}
		//ne
		if(!(row==1 || col==getNumCols()+1)){
			temp=getBlockVal(row-1, col);
			if(temp!=' '){
				arr[0]=Character.getNumericValue(temp);
			}
		}
		//se
		if(!(row==getNumRows()+1 || col==getNumCols()+1)){
			temp=getBlockVal(row, col);
			if(temp!=' '){
				arr[0]=Character.getNumericValue(temp);
			}
		}
		//sw
		if(!(row==getNumRows()+1 || col==1)){
			temp=getBlockVal(row, col-1);
			if(temp!=' '){
				arr[0]=Character.getNumericValue(temp);
			}
		}
		return arr;
	}
	
	//checks if there are xs surrounding any corners of a block
	//returns a 4 element array. 0=nw,1=ne,2=se,3=sw
	public boolean[] isBlockInCorner(int row, int col){
		boolean[] arr= {false, false, false, false};
		
		SnakeHead snake = new SnakeHead(row, col, 'l', SnakeHead.NORTH, this);
		if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//nw
			arr[0]=true;
		}
		snake = snake.getCWCoord();
		if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//ne
			arr[1]=true;
		}
		snake = snake.getCWCoord();
		if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//se
			arr[2]=true;
		}
		snake = snake.getCWCoord();
		if(snake.getCCWVal()=='x' && snake.getForwardVal()=='x'){//sw
			arr[3]=true;
		}
		
		return arr;
	}
	
	//finds all places on board with unconnected linepoints
	public ArrayList<SnakeHead> getSnakeHeads(){
		int [] count;
		char[] vals;
		ArrayList<SnakeHead> shl = new ArrayList<SnakeHead>();
		//go to each point
		for(int row = 1;row<=getNumRows()+1; row++){
			for(int col = 1; col<=getNumCols()+1; col++){
				count=getPointSurrValCount(row, col);
				if(count[1]==1 && count[2]<=2){//if there is one line and less than 2 xs
					vals = getPointSurrVals(row,col);
					if(vals[0]=='|' || vals[0]=='-'){//top
						shl.add(new SnakeHead(row-1,col,'l',SnakeHead.SOUTH, this));
					}else if(vals[1]=='|' || vals[1]=='-'){//right
						shl.add(new SnakeHead(row,col,'t',SnakeHead.WEST, this));
					}else if(vals[2]=='|' || vals[2]=='-'){//bottom
						shl.add(new SnakeHead(row,col,'l',SnakeHead.NORTH, this));
					}else{//left
						shl.add(new SnakeHead(row,col-1,'t',SnakeHead.EAST, this));
					}
					
				}
			}
		}
		
		return shl;
	}
	
	//returns count of the elements surrounding the point in the upper left corner of the chosen block
	public int[] getPointSurrValCount(int row, int col){
		int[] arr = {0,0,0};
		//top
		if(this.getSpaceVal(row-1, col, 'l') == ' ') arr[0]++;
		if(this.getSpaceVal(row-1, col, 'l') == '-' || this.getSpaceVal(row-1, col, 'l') == '|') arr[1]++;
		if(this.getSpaceVal(row-1, col, 'l') == 'x') arr[2]++;
		//bottom
		if(this.getSpaceVal(row, col, 'l') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col, 'l') == '-' || this.getSpaceVal(row, col, 'l') == '|') arr[1]++;
		if(this.getSpaceVal(row, col, 'l') == 'x') arr[2]++;
		//left
		if(this.getSpaceVal(row, col-1, 't') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col-1, 't') == '-' || this.getSpaceVal(row, col-1, 't') == '|') arr[1]++;
		if(this.getSpaceVal(row, col-1, 't') == 'x') arr[2]++;
		//right
		if(this.getSpaceVal(row, col, 't') == ' ') arr[0]++;
		if(this.getSpaceVal(row, col, 't') == '-' || this.getSpaceVal(row, col, 't') == '|') arr[1]++;
		if(this.getSpaceVal(row, col, 't') == 'x') arr[2]++;
		
		return arr;
		
		
		
	}
	
	/*
	 * returns a 4 item array representing the state of each space around a point
	 * index:0=t,1=r,2=b,3=l
	 * 
	 */
	public char[] getPointSurrVals(int row, int col){
		char[]arr={'e','e','e','e'};
		arr[0]=this.getSpaceVal(row-1,col,'l');
		arr[1]=this.getSpaceVal(row,col,'t');
		arr[2]=this.getSpaceVal(row,col,'l');
		arr[3]=this.getSpaceVal(row,col-1,'t');
		return arr;
	}
	
	//fills all empty spaces around a point with the value val
		//
	public ArrayList<Move> fillSurrPointVals(int row, int col, boolean isX){
		ArrayList<Move> arr = new ArrayList<Move>();
		Move m;
		char[] vals = getPointSurrVals(row,col);
		if(vals[0]==' '){
			m=new Move(row-1, col, 'l', isX);
			arr.add(m);
			this.makeMove(m);
		}
		if(vals[1]==' '){
			m=new Move(row, col, 't', isX);
			arr.add(m);
			this.makeMove(m);
		}
		if(vals[2]==' '){
			m=new Move(row, col, 'l', isX);
			arr.add(m);
			this.makeMove(m);
		}
		if(vals[3]==' '){
			m=new Move(row, col-1, 't', isX);
			arr.add(m);
			this.makeMove(m);
		}
		return arr;
	}
	
	

	
	

	public void makeCollMovePrint(ArrayList<Move> moveList){
		makeCollMove(moveList);
		board.printMat();
	}
	
	public void makeCollMove(ArrayList<Move> moveList){
		for(int i =0; i<moveList.size(); i++){
			this.makeMove(moveList.get(i));
		}
	}
	
	public void makeMovePrint(Move move){
		this.makeMove(move);
		board.printMat();
	}
	
	public boolean makeMoveIfAvailable(Move move){
		if(this.getSpaceVal((move.row-1)/2, (move.col-1)/2, move.side)==' '){ 
			makeMove(move);
			return true;
		}
		return false;
	}
	
	//takes rows and cols of the literal matrix, not the game board
	public Move makeMove(Move move){
		boolean isVert=false;
		int row = move.row;
		int col = move.col;
		
		if(move.side=='t' || move.side=='T'){
			row-=1;
			isVert = false;
		} else if(move.side=='b'||move.side=='B'){
			row+=1;
			isVert = false;
		} else if(move.side=='l'||move.side=='L'){
			col-=1;
			isVert = true;
		} else if(move.side=='r'||move.side=='R'){
			col+=1;
			isVert = true;
		}
		
		if(!move.isX){
			if (board.getVal(row, col) == ' ' || board.getVal(row, col) == 'x'){
				if(isVert){
					board.setVal(row, col, '|');
				} else if(!isVert){
					board.setVal(row, col, '-');
				}
			} else {
				board.setVal(row, col, ' ');
			}
		}else{
			if (board.getVal(row, col) == ' ' || board.getVal(row, col) == '|' || board.getVal(row, col) == '-'){
				board.setVal(row, col, 'x');
				
			} else {
				board.setVal(row, col, ' ');
			}
		}
		return move;
	}
	

	
	
	

	
	public void populateBoard(String path){
		try{
			data = OpenFile(path);
			
			/*getting the dimensions of the board*/
			String[] rowcol = data[0].split(" ");
			int numRows = Integer.parseInt(rowcol[0]);
			int numCols = Integer.parseInt(rowcol[1]);
			
			/* 2 rows for edge numbers,
			 * 1 row for the first set of +s
			 * 2 rows for each row of #s on board
			 * 1 row for final xs
			 */
			
			board = new Matrix(4+2*numRows, 4+2*numCols);
			
			//the column coordinates. Note: cannot accept values over 99
			for(int i = 1; i<=numCols; i++){
				int ones = i%10;
				int tens = (i - ones)/10;
				if(tens>0) board.setVal(0, 1+2*i, Character.forDigit(tens, 10));
				board.setVal(1, 1+2*i, Character.forDigit(ones, 10));
			}
			
			//the row coordinates. Note: cannot accept values over 99
			for(int i = 1; i<=numRows; i++){
				int ones = i%10;
				int tens = (i - ones)/10;
				if(tens>0) board.setVal(1+2*i, 0, Character.forDigit(tens, 10));
				board.setVal(1+2*i, 1, Character.forDigit(ones, 10));
			}
			
			//add +s
			for(int i = 0; i<=numRows; i++){
				for(int j = 0; j<=numCols; j++){
					board.setVal(2+2*i, 2+2*j, '+');
				}
			}
			
			//add xs
			for(int i =0;i<=numRows;i++){
				board.setVal(2+2*i, 1, 'x');
				board.setVal(2+2*i, 3+2*numCols, 'x');
			}
			for(int i = 0;i<=numCols;i++){
				board.setVal(1, 2+2*i, 'x');
				board.setVal(3+2*numRows, 2+2*i, 'x');
			}
			
			//add #s
			for(int i = 1; i<data.length; i++){
				String[] lineSplit = data[i].split(" ");
				
				board.setVal(1+2*Integer.parseInt(lineSplit[0]), 
						1+2*Integer.parseInt(lineSplit[1]), 
						lineSplit[2].charAt(0));
			}
			
			cleanBoard = new Matrix(board);
			board.printMat();
			
		}
		catch (IOException e){
			System.out.println(e.getMessage());
		}	
	}
	
	/*this function does the work of making the text file into 
	 * an array of strings.
	 * One element for each line in the file.
	 */
	public String[] OpenFile(String path) throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		
		int numOfLines = readLines(path);
		
		
		String[] textData = new String[numOfLines];
		
		for(int i = 0; i< numOfLines; i++){
			textData[i] = textReader.readLine();
		}
		
		textReader.close();
		return textData;
	}
	
	private int readLines(String path) throws IOException{
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		String aLine;
		int numOfLines = 0;
		
		while ((aLine = bf.readLine()) != null){
			numOfLines++;
		}
		bf.close();
		
		return numOfLines;
	}
}
