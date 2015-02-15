
public class SnakeHead {

	//cardinals
	public static final String NORTH = "north";
	public static final String EAST = "east";
	public static final String SOUTH = "south";
	public static final String WEST = "west";
	
	public int row, col;
	public char side;
	public String dir;
	private gameBoard gb;
	
	SnakeHead(int row, int col, char side, String dir, gameBoard gb){
		if(side == 'b'){//snakehead is always on top of the block
			this.row = row+1;	
		} else{
			this.row = row;
		}
		if(side=='r'){//or on the left of the block
			this.col = col+1;
		}else{
			this.col = col;	
		}
		this.dir = dir;
		if(dir == NORTH || dir == SOUTH){
			this.side = 'l';
		}else{
			this.side = 't';
		}
		
		this.gb = gb;
		
	}

	public char getCurrentVal(){
		switch(this.dir){
		case NORTH:
			return this.gb.getSpaceVal(this.row, this.col, 'l');
		case EAST:
			return this.gb.getSpaceVal(this.row, this.col, 't');
		case SOUTH:
			return this.gb.getSpaceVal(this.row, this.col, 'l');
		case WEST:
			return this.gb.getSpaceVal(this.row, this.col, 't');
		default:
			return 'e';
	}					
	}
	
	public char getCCWVal(){
		switch(this.dir){
			case NORTH:
				return this.gb.getSpaceVal(this.row, this.col - 1, 't');
			case EAST:
				return this.gb.getSpaceVal(this.row-1, this.col+1, 'l');
			case SOUTH:
				return this.gb.getSpaceVal(this.row+1, this.col, 't');
			case WEST:
				return this.gb.getSpaceVal(this.row, this.col, 'l');
			default:
				return 'e';
		}			
	}
	
	public char getForwardVal(){
		switch(this.dir){
			case NORTH:
				return this.gb.getSpaceVal(this.row-1, this.col, 'l');
			case EAST:
				return this.gb.getSpaceVal(this.row, this.col+1, 't');
			case SOUTH:
				return this.gb.getSpaceVal(this.row+1, this.col, 'l');
			case WEST:
				return this.gb.getSpaceVal(this.row, this.col-1, 't');
			default:
				return 'e';
		}			
	}
	
	public char getCWVal(){
		switch(this.dir){
			case NORTH:
				return this.gb.getSpaceVal(this.row, this.col, 't');
			case EAST:
				return this.gb.getSpaceVal(this.row, this.col+1, 'l');
			case SOUTH:
				return this.gb.getSpaceVal(this.row+1, this.col-1, 't');
			case WEST:
				return this.gb.getSpaceVal(this.row-1, this.col, 'l');
			default:
				return 'e';
		}			
	}
	
	
	public SnakeHead getCCWCoord(){
			switch(this.dir){
			case NORTH:
				return new SnakeHead(this.row, this.col - 1, 't', WEST, this.gb);
			case EAST:
				return new SnakeHead(this.row-1, this.col+1, 'l', NORTH, this.gb);
			case SOUTH:
				return new SnakeHead(this.row+1, this.col, 't', EAST, this.gb);
			case WEST:
				return new SnakeHead(this.row, this.col, 'l', SOUTH, this.gb);
			default:
				return new SnakeHead(this.row, this.col, this.side, this.dir, this.gb);
			}
	}

	public SnakeHead getForwardCoord(){
		switch(this.dir){
		case NORTH:
			return new SnakeHead(this.row-1, this.col, 'l', NORTH, this.gb);
		case EAST:
			return new SnakeHead(this.row, this.col+1, 't', EAST, this.gb);
		case SOUTH:
			return new SnakeHead(this.row+1, this.col, 'l', SOUTH, this.gb);
		case WEST:
			return new SnakeHead(this.row, this.col-1, 't', WEST, this.gb);
		default:
			return new SnakeHead(this.row, this.col, this.side, this.dir, this.gb);
		}
	}
	
	public SnakeHead getCWCoord(){
		switch(this.dir){
		case NORTH:
			return new SnakeHead(this.row, this.col, 't', EAST, this.gb);
		case EAST:
			return new SnakeHead(this.row, this.col+1, 'l', SOUTH, this.gb);
		case SOUTH:
			return new SnakeHead(this.row+1, this.col-1, 't', WEST, this.gb);
		case WEST:
			return new SnakeHead(this.row-1, this.col, 'l', NORTH, this.gb);
		default:
			return new SnakeHead(this.row, this.col, this.side, this.dir, this.gb);
		}
	}
	
	public SnakeHead getBackwardCoord(){
		switch(this.dir){
		case NORTH:
			return new SnakeHead(this.row, this.col, 'l', SOUTH, this.gb);
		case EAST:
			return new SnakeHead(this.row, this.col, 't', WEST, this.gb);
		case SOUTH:
			return new SnakeHead(this.row, this.col, 'l', NORTH, this.gb);
		case WEST:
			return new SnakeHead(this.row, this.col, 't', EAST, this.gb);
		default:
			return new SnakeHead(this.row, this.col, this.side, this.dir, this.gb);
		}
	}
	
	
	public int[] getRightSideBlockCoord(){
		int[]arr=new int[2];
		switch(this.dir){
		case NORTH:
			arr[0]=this.row;
			arr[1]=this.col;
			break;
		case EAST:
			arr[0]=this.row;
			arr[1]=this.col;
			break;
		case SOUTH:
			arr[0]=this.row;
			arr[1]=this.col-1;
			break;
		case WEST:
			arr[0]=this.row-1;
			arr[1]=this.col;
			break;
		default:
			arr[0]=-1;
			arr[1]=-1;
			break;
		}
		return arr;
	}
	
	public int[] getLeftSideBlockCoord(){
		int[]arr=new int[2];
		switch(this.dir){
		case NORTH:
			arr[0]=this.row;
			arr[1]=this.col-1;
			break;
		case EAST:
			arr[0]=this.row-1;
			arr[1]=this.col;
			break;
		case SOUTH:
			arr[0]=this.row;
			arr[1]=this.col;
			break;
		case WEST:
			arr[0]=this.row;
			arr[1]=this.col;
			break;
		default:
			arr[0]=-1;
			arr[1]=-1;
			break;
		}
		return arr;
	}
}
