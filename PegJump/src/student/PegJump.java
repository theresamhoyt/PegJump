package student;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pegJump.*;

public class PegJump {
	private static  int filledPegs = -1;
	private static boolean[] pegs = null;
	private static Map<Integer,ArrayList<Jump>> jumpDictionary = new HashMap<Integer, ArrayList<Jump>>();
	private static int currDictionaryLocation = 0;	
	private static double jumpCnt = 0;
	

	// returns a monte-carlo estimate of the number of promising nodes in the
	// search tree.
	public static double estimatePegJump(PegJumpPuzzle puzzle) {
	
		return 100;     // a guess
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
		ArrayList<Jump> listOfJumps = new ArrayList<Jump>();
    	Iterator<Jump> jitr = puzzle.jumpIterator();
    	foundAjump = false;
    	while(jitr.hasNext()){
    		
    		if(filledPegs == 1){
    			foundAjump = false;
    			break;
    		}
    		Jump j = jitr.next();
			int from = j.getFrom();
			int over = j.getOver();
			int dest = j.getDest();
			
			int pegSize = pegs.length;
			
			if(from < pegSize && over < pegSize && dest < pegSize){
				if(pegs[from] && pegs[over] && !pegs[dest]){
					foundAjump = true;
					listOfJumps.add(j);
					
					int dictonaryIndex = jumpDictionary.size();
					jumpDictionary.put(dictonaryIndex, listOfJumps);
					currDictionaryLocation++;
				}
				ArrayList<Jump> currList = jumpDictionary.get(currDictionaryLocation - 1);
		    	if(foundAjump){
		    		
		    			Jump curr = currList.get(0);
		    			makeJump(curr);
		    			jumpList.add(curr);
		    			solvePegJump(puzzle, jumpList);
		    			foundAjump = false;
			}else if(!jumpList.isEmpty()) {
				//I thought this was where we should do the backtracking, but I cannot get it to work.
				//jumpList = backtrack(jumpList);
				
			}

    	}
		
    }

    return jumpCnt;
    
	}

	private static ArrayList<Jump> backtrack(ArrayList<Jump> jumpList){
		Jump j = jumpList.get(jumpList.size() - 1);
		undoMove(j);
		jumpList.remove(j);
		
		return jumpList;
	}
	private static void undoMove (Jump jump) {
		int from = jump.getFrom();
		int over = jump.getOver();
		int dest = jump.getDest();
		pegs[from] = true;
		pegs[over] = true;
		pegs[dest] = false;
		
		
	}
	private static Jump makeJump(Jump jump){
		int from = jump.getFrom();
		int over = jump.getOver();
		int dest = jump.getDest();
	
		pegs[from] = false;
		pegs[over] = false;
		pegs[dest] = true;
		jumpCnt++;
		filledPegs--;
		return jump;
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