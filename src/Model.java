/**
 * This file is to be completed by you.
 *
 * @author <s2015345>
 */
public final class Model
{
	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================
	// The most common version of Connect Four has 6 rows and 7 columns.
	public static final int DEFAULT_NR_ROWS = 6;
	public static final int DEFAULT_NR_COLS = 7;
	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================
	// The size of the board.
	private int nrRows;
	private int nrCols;
	private int neededToWin;
	public static String[][] board;
	public long counter;
	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================
	public Model()
	{
	    setBoard(nrRows, nrCols);
	}
	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================
	public boolean isMoveValid(int move)
	{
	    // allows for a 0 input in order to concede the game
        if (move == 0) {
	        return true;
        }
        // this ensures the column requested by the user isn't full
        // otherwise, if it is full, it tells the user the move is invalid
        else if (1 <= move && move <= nrCols){
	        for (int i = nrRows; i >= 0; i--) {
	            if (board[i][2 * move - 1].equals(" ")) {
	                return true;
                }
            }
        }
        return false;
	}
	
	public void makeMove(int move)
	{
	    // ensures the move is valid before allowing it to be added to the board
	    while (!isMoveValid(move)) {
            System.out.print("Invalid move, please try again: ");
            move = InputUtil.readIntFromUser();
            makeMove(move);
        }
	    // loops through every row from the bottom upwards, and checks for empty spaces
	    int i = nrRows;
	    while (i >= 0) {
	        // if the user inputs 0, it treats it as a separate case in order to concede the game
	        if (move == 0) {
	            break;
            }
	        // if not, then it adds it in the appropriate space on the appropriate column on the board
	        else if (board[i][2 * move - 1].equals(" ")) {
	            if (counter % 2 == 0) {
                    board[i][2 * move - 1] = "R";
                    break;
                }
	            else {
	                board[i][2 * move - 1] = "Y";
                    break;
                }
            }
	        i--;
        }
	}

    // this method is used to keep track of each player's turn
    public void incrementCounter() {
        counter++;
    }
	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================
	public int getNrRows()
	{
		return nrRows;
	}
	
	public int getNrCols()
	{
		return nrCols;
	}

	public int getNeededToWin() {
	    return neededToWin;
    }

	public String[][] getBoard() {
	    return board;
    }

    public long getCounter() {
	    return counter;
    }
    // =========================================================================
    // ================================ SETTERS ================================
    // =========================================================================
    public void setNrRows(int rows) {
	    nrRows = rows;
    }

    public void setNrCols(int cols) {
        nrCols = cols;
    }

    public void setNeededToWin(int winningTotal) {
	    neededToWin = winningTotal;
    }

    public void setBoard(int rows, int cols) {
	    board = new String[rows+1][2*cols+1];
        for (int i = 0; i < (nrRows + 1); i++) {
            for (int j = 0; j < (2 * nrCols + 1); j++) {
                if (j % 2 == 0) {
                    board[i][j] = "|";
                }
                else board[i][j] = " ";
                if (i == nrRows) board[i][j] = "-";
            }
        }
    }

    public void setCounter(int n) {
	    counter = n;
    }
}

