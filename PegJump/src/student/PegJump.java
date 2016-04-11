package student;
import java.util.AbstractList;
/* 
 * This Student class is meant to contain your algorithm.
 * You should implement the two static methods:
 *   estimatePegJump - does a monte carlo estimation of the number or promising
 *                     nodes in the search tree
 *   solvePegJump - finds a solution and the number of nodes examined in the search
 *                  it should fill in the jumpList argument with the jumps that form
 *                  your solution
 *
 * The input is a PegJumpPuzzle object, which has:
 *   a size, the number of holes numbered 0 .. size()-1
 *   the startHole that is initially empty
 *   an ArrayList of allowed jumps, which are triples (from, over, dest)
 *   a jump takes the peg 'from' over the peg in 'over' (removing it) and into 'dest'
 */
import java.util.ArrayList;
import java.util.Iterator;
import pegJump.*;

public class PegJump {
	private static  int filledPegs = -1;
	private static int jumpCnt = 0;
	private static boolean[] pegs = null;

	
	// returns a monte-carlo estimate of the number of promising nodes in the
	// search tree.
	public static double estimatePegJump(PegJumpPuzzle puzzle) {
		return 1000.0;     // a guess
	}
	
	// simple example routine that just repeatedly finds the first valid
	// jump until it fails
	// this returns:
	//   the number of jumps tried
	//   and as a modifiable argument, it fills in the jumpList
	public static double solvePegJump(PegJumpPuzzle puzzle, ArrayList<Jump> jumpList) 
	{		
		
		if(filledPegs == -1){
			filledPegs = puzzle.numHoles() - 1;
		}
		if(pegs == null){
			pegs = initalizePegArray(puzzle);
		}
		boolean foundAjump;

    	Iterator<Jump> jitr = puzzle.jumpIterator();
    	// start doing jumps
    	do {
    		
    		if(filledPegs == 1){
    			break;
    		}
			foundAjump = false;
			while (jitr.hasNext()) {

				Jump j = jitr.next();
				int from = j.getFrom();
				int over = j.getOver();
				int dest = j.getDest();
				
				if (pegs[from] && pegs[over] && !pegs[dest]) {
					// found a valid jump
				      // add to the result list
					
					if( !jumpList.isEmpty() && jumpList.get(jumpList.size() - 1) == j){
						jumpList.remove(j);
						foundAjump = false;
						filledPegs++;
						break;
					
					}
					jumpList.add(j); 
					
					pegs[from] = false;    // do the jump
					pegs[over] = false;
					pegs[dest] = true;
					jumpCnt++;
					foundAjump = true;
					filledPegs = filledPegs - 1;
		    		break;
						
				} 
			
			}
		} while (foundAjump);
    	
    	if(filledPegs != 1){
    		backtrack(jumpList);
    		solvePegJump(puzzle, jumpList);
    	}
    	filledPegs = -1;
    	pegs = null;
		return jumpCnt; 
	}

	private static void backtrack (ArrayList<Jump> jumpList) {
		Jump lastJump = jumpList.get(jumpList.size() - 1);
		int from = lastJump.getFrom();
		int over = lastJump.getOver();
		int dest = lastJump.getDest();
		pegs[from] = true;
		pegs[over] = true;
		pegs[dest] = false;
		
	}
	private static boolean[] initalizePegArray(PegJumpPuzzle puzzle){
	    // initialize the puzzle
		// make an array to keep tracks of where the pegs are
		// and put pegs in all holes except the starting hole
		pegs = new boolean[puzzle.numHoles()];  // hole numbers start at 0
		for (int i = 0; i < puzzle.numHoles(); i++)  
			pegs[i] = true;                      // fill all holes
    	pegs[puzzle.getStartHole()] = false;     // clear starting hole

    	return pegs;
	}
}