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
import java.util.HashSet;
import java.util.Iterator;
import pegJump.*;

public class PegJump {

	// initialize variables
	private static boolean[] pegs = null;
	private static int pegCount = 0;
	private static double jumpCount = 0;

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

		// hashset to put all the seen boards in
		HashSet<PegJumpPuzzle> seenBoards = new HashSet<PegJumpPuzzle>();

		// initialize the peg array
		if(pegs == null)
			pegs = initalizePegArray(puzzle);

		// iterate through all the nodes and adds valid jumps to the validJumps array
		Iterator<Jump> jitr = puzzle.jumpIterator();
		while(jitr.hasNext()){
			Jump j = jitr.next();			

			// if you find a valid solution
			// do a jump and add it to the jump list
			// recursively call solvePeg to get next jump
			if(validJump(j)){
				jumpList.add(makeJump(j));
				solvePegJump(puzzle, jumpList);
			}
		}
		// checks to see if the puzzle is solved
		// this is when there is only one peg left
		if(solved(pegCount, puzzle)){
			return 0;
		}

		// puzzle not complete
		// need to backtrack
		backtrack(jumpList);

		return 0;

	}


	private static ArrayList<Jump> backtrack(ArrayList<Jump> jumpList){

		undoJump(jumpList.get(jumpList.size()-1));
		jumpList.remove(jumpList.size()-1);

		printJumpList(jumpList);
		System.out.println();
		return jumpList;
	}

	// print contents of the jumpList
	static void printJumpList(ArrayList<Jump> jumpList){
		for(Jump j: jumpList)
			System.out.println(j);
	}

	// checks to see if a jump is valid
	// from and over have to be true
	// dest have to be false
	static boolean validJump(Jump jump){

		int from = jump.getFrom();
		int over = jump.getOver();
		int dest = jump.getDest();

		if( pegs[from] && 
				pegs[over] && 
				!pegs[dest] )
			return true;
		else
			return false;

	}

	// initialize the puzzle
	// make an array to keep tracks of where the pegs are
	// and put pegs in all holes except the starting hole
	private static boolean[] initalizePegArray(PegJumpPuzzle puzzle){
		pegs = new boolean[puzzle.numHoles()];  // hole numbers start at 0
		pegCount = puzzle.numHoles()-1;
		for (int i = 0; i < puzzle.numHoles(); i++)  
			pegs[i] = true;                      // fill all holes
		pegs[puzzle.getStartHole()] = false;     // clear starting hole

		return pegs;
	}

	// checks to see if you have solved the puzzle
	static boolean solved(int pegCount, PegJumpPuzzle puzzle){
		return (pegCount==1 && pegs[puzzle.getStartHole()]);
	}

	// makes a jump by changing the boolean values
	// or from, over, and dest
	// increases jumpCount
	static Jump makeJump(Jump jump){
		int from = jump.getFrom();
		int over = jump.getOver();
		int dest = jump.getDest();

		pegs[from] = false;
		pegs[over] = false;
		pegs[dest] = true;

		pegCount--;
		jumpCount++;


		return jump;
	}

	// undoes the last move by changing the boolean values
	// or from, over, and dest
	// increases jumpCount 
	static Jump undoJump (Jump jump) {
		int from = jump.getFrom();
		int over = jump.getOver();
		int dest = jump.getDest();

		pegs[from] = true;
		pegs[over] = true;
		pegs[dest] = false;

		pegCount++;
		jumpCount--;

		return jump;

	}


}