import java.io.*;
public class Move {
	
	public int row;
	public int col;
	public char side;
	public boolean isX;
	
	public Move(int gameRow, int gameCol, char side, boolean isX){
//		String[] parts = in.split(" ");
		
		//int gRow = Integer.parseInt(gameRow);
		//int gCol = Integer.parseInt(gameCol);
		if(side=='r'){
			gameCol++;
			side='l';
		}
		if(side=='b'){
			gameRow++;
			side='t';
		}
		row = 1+2*gameRow;
		col = 1+2*gameCol;
		this.side = side;
		this.isX = isX;
	}
	
	public boolean equals(Move m){
		if(this.row==m.row &&
				this.col==m.col &&
				this.side==m.side &&
				this.isX==m.isX){
			return true;
		}
		return false;
	}

}
