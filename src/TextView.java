import java.util.Locale;

/**
 * This file is to be completed by you.
 *
 * @author <s2015345>
 */
public final class TextView
{
	public TextView()
	{

	}
	
	public final void displayNewGameMessage()
	{
	    System.out.println("---- NEW GAME STARTED ----");
        System.out.println("---- TO CONCEDE THE GAME, TYPE IN 0 ----");
	}


	// this method gets all the necessary input from the user to set up the game mode
	public String setUpGame(Model model) {
        System.out.print("Please input the number of rows: ");
        int rows = InputUtil.readIntFromUser();
        model.setNrRows(rows);
        System.out.print("Please input the number of columns: ");
        int cols = InputUtil.readIntFromUser();
        model.setNrCols(cols);
        System.out.print("Please input the number of inputs in a row needed to win this game: ");
        int total = InputUtil.readIntFromUser();
        model.setNeededToWin(total);
        // this validates the game, so as to ensure it can actually be won
        // and doesn't violate any of the valid game criteria
        while (!valid_game(model)) {
            System.out.println("Invalid game, please try again!");
            System.out.print("Please input the number of rows: ");
            rows = InputUtil.readIntFromUser();
            model.setNrRows(rows);
            System.out.print("Please input the number of columns: ");
            cols = InputUtil.readIntFromUser();
            model.setNrCols(cols);
            System.out.print("Please input the number of inputs in a row needed to win this game: ");
            total = InputUtil.readIntFromUser();
            model.setNeededToWin(total);
        }
        model.setBoard(rows, cols);
        System.out.print("Would you like to play against the AI? ");
        String answer = InputUtil.readStringFromUser();
        if (answer.toLowerCase(Locale.ROOT).equals("yes")) {
            // if the player chooses the AI, they can then choose the level they want to play at:
            // easy, which randomises numbers, or hard, which makes more logical moves and is more of a challenge
            System.out.print("Would you like to play the easy level or the hard level? (type in E for easy and H for hard) ");
            String level = InputUtil.readStringFromUser();
            // validates the user input for the difficulty level
            while (!level.equals("E") && !level.equals("H")) {
                System.out.println("Invalid level input, please try again: ");
                level = InputUtil.readStringFromUser();
            }
            return level;
        }
        return "";
    }
	
	public final void displayBoard(Model model)
	{
        int nrRows = model.getNrRows();
        int nrCols = model.getNrCols();
		String[][] board = model.getBoard();
		// Get your board representation.
        for (int i = 0; i < (nrRows + 1) ; i++)
        {
            for (int j = 0; j < (2 * nrCols + 1); j++)
            {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
	}

    // this method ensures the game is valid
    // it makes sure the board is large enough to allow for vertical, horizontal and diagonal wins
    // it also ensures the user has to have at least 3 in a row
    // since 1 or 2 in a row are impossible to prevent in one move
    public boolean valid_game(Model model) {
        if (model.getNeededToWin() > model.getNrRows() || model.getNeededToWin() > model.getNrCols() || model.getNeededToWin() <= 2) {
            return false;
        }
        return true;
    }
}
