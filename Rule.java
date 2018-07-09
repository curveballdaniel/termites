import java.awt.Color;
import java.util.Random;

/*
 * created as a helper class so that all rules can live happily ever after.
 */
public class Rule {
	
	private int[][] movementarray;
	
	// constructor that takes in two arguments, the amount of states this specific rule should have, and the array of colors attached to it
	// then creates an internal array called the movement array, which is capped by the amount of states the 'rule' has. it is a two D array
	// because the second dimension is locked to hold three more options for every single state of the 'rule', the turning of the ant, the 
	// color that it should write to next, and the next state it should move to. This is built in randomly for every single state,
	// so while the states will go up from 0 - states-1, each state will have a completely random next state, color, and direction to move toward.
	public Rule(int states, Color[] colors){
		Random r = new Random();
		
		movementarray = new int[states][3];
		
		for (int i = 0; i < states; i++){
			
			if (r.nextInt(2) == 0){
				movementarray[i][0] = 1;
				
			} else {
				movementarray[i][0] = -1;
			}
			movementarray[i][1] = r.nextInt(colors.length);
			movementarray[i][2] = r.nextInt(states);
		}
	}
	
	// returns the ++ or --, movement of ant. a 1 will mean move the ant right, and a -1 will mean move the ant left. This is used to control movement of the ant.
	public int getMovement(int currentstate){
		return movementarray[currentstate][0];	
	}

	// returns the number representation of the color to draw when given the current state. this number is then plugged into the color array to obtain the color.
	public int getColorToDraw(int currentstate){
		return movementarray[currentstate][1];
	}
	
	// returns the int next state when given a current state
	public int getNextState(int currentstate){
		return movementarray[currentstate][2];
	}
	
	// this method is used for printing the ruleset that is created through the program so that it is somewhat understandable to the user.
	public static void printRuleSet(Rule[] ruleset, Color[] colorarray, int totalstates){
		System.out.println("Generated ruleset: ");
		for (int i = 0; i < ruleset.length; i++){
			System.out.println("Rule " + (i+1) + ", if the color is " + colorarray[i] + ":");
			for (int j = 0; j < totalstates; j++){
				String turndirection;
				if (ruleset[i].getMovement(j) == 1){
					turndirection = "right";
				} else {
					turndirection = "left";
				}
				System.out.println("Turn " + turndirection + ", and draw the color " + colorarray[ruleset[i].getColorToDraw(j)] + ". The next state will be " + ruleset[i].getNextState(j) + ".");
				
			}
			
		}
	}
}
