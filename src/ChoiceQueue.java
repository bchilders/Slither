import java.util.ArrayList;


public class ChoiceQueue {
	private ArrayList<Choice> Q;
	
	ChoiceQueue(){
		Q = new ArrayList<Choice>();
	}
	
	public int size(){
		return Q.size();
	}
	
	public void add(Choice choice){
		Q.add(choice);
	}
	
	public Choice pop(){
		Choice tempChoice = Q.get(0);
		Q.remove(0);
		return tempChoice;
	}
	
	public void sort(){//sorts by blockpoints+num of moves in moveList, smallest to largest
		boolean didChange=true;
		while(didChange){
			didChange=false;
			for(int i = 0; i<this.Q.size()-1; i++){
				if(Q.get(i).priority>Q.get(i+1).priority){
					Q.add(i+2, Q.get(i));
					Q.remove(i);
					didChange = true;
				}
				
			}
		}
	}
	
}
