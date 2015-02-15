import java.io.*;

/* A class for easily dealing with matrices*/
public class Matrix {
	
	private char[][] theMat;
	public int numRows;
	public int numCols;
	
	Matrix (int numRows, int numCols){
		theMat = new char[numRows][numCols];
		for(int i = 0; i<numRows; i++){
			for(int j = 0; j<numCols; j++){
				theMat[i][j] = ' ';
			}
		}
		this.numRows = numRows;
		this.numCols = numCols;
	}
	
	Matrix (Matrix another){
		this.numRows = another.numRows;
		this.numCols = another.numCols;
		this.theMat = new char[numRows][numCols];
		for(int i = 0; i<numRows; i++){
			for(int j = 0; j<numCols; j++){
				theMat[i][j] = another.theMat[i][j];
			}
		}
	}
	
	public void setVal(int row, int col, char val){
		theMat[row][col] = val;
	}

	public char getVal(int row, int col){
		return theMat[row][col];
	}
	
	public void printMat(){
		for(int i = 0; i<numRows; i++){
			for(int j = 0; j<numCols; j++){
				System.out.print(getVal(i,j));
			}
			System.out.println();
		}
	}
}
