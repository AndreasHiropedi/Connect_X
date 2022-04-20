import java.util.Arrays;
import java.util.Random;

/**
 * This file is to be completed by you.
 *
 * @author <s2015345>
 */

public final class Controller {
    private final Model model;
    private final TextView view;
    private int counter;
    private int[] potentialAIMoves;

    public Controller(Model model, TextView view) {
        this.model = model;
        this.view = view;
    }
    // =========================================================================================
    // ================================ GAME METHODS START HERE ================================
    // =========================================================================================
    public void startSession() {
        String level = view.setUpGame(model);
        // once the level is selected, the user can then choose which player they want to be in the game
        // and the game is set up based on the level chosen by the user
        if (level.equals("E")) {
            int player = selectPlayer();
            view.displayBoard(model);
            view.displayNewGameMessage();
            playEasyAI(player);
        } else if (level.equals("H")){
            int player = selectPlayer();
            view.displayBoard(model);
            view.displayNewGameMessage();
            playHardAI(player);
        }
        // if the user doesn't wish to play the AI, they can play human vs human
        else {
            view.displayBoard(model);
            view.displayNewGameMessage();
            play2Players();
        }
    }

    // checks for X in a row
    public boolean checkHorizontal(String[][] board) {
        // stores the values of how many consecutive R's there are in each row
        int[] valRowsP1 = new int[model.getNrRows() + 1];
        Arrays.fill(valRowsP1, 1);
        // stores the values of how many consecutive R's there are in each row
        int[] valRowsP2 = new int[model.getNrRows() + 1];
        Arrays.fill(valRowsP2, 1);
        int row = 0;
        // this loop starts from the top row and ends on the bottom row
        // and adds up occurrences of R's and Y's to the respective arrays at the respective indexes
        while (row < (model.getNrRows() + 1)) {
            for (int j = 1; j <= 2 * model.getNrCols() - 3; j += 2) {
                if (board[row][j].equals(board[row][j + 2]) && board[row][j].equals("R")) {
                    valRowsP1[row] = valRowsP1[row] + 1;
                } else if (board[row][j].equals(board[row][j + 2]) && board[row][j].equals("Y")) {
                    valRowsP2[row] = valRowsP2[row] + 1;
                }
            }
            row++;
        }
        // once the loop is finished, check if each array holds a value of X or more
        // and if so, return true to signal the game has been won horizontally
        for (int k = 0; k < valRowsP1.length; k++) {
            if (valRowsP1[k] >= model.getNeededToWin() || valRowsP2[k] >= model.getNeededToWin()) {
                return true;
            }
        }
        return false;
    }

    // checks for X consecutive inputs vertically
    public boolean checkVertical(String[][] board) {
        // stores the values of how many consecutive R's there are in each column
        int[] valColsP1 = new int[2 * model.getNrCols() + 1];
        Arrays.fill(valColsP1, 1);
        // stores the values of how many consecutive Y's there are in each column
        int[] valColsP2 = new int[2 * model.getNrCols() + 1];
        Arrays.fill(valColsP2, 1);
        int col = 1;
        // this loop starts from the top row and ends on the bottom row
        // and adds up occurrences of R's and Y's to the respective arrays at the respective indexes
        while (col < (2 * model.getNrCols() + 1)) {
            for (int i = 0; i < model.getNrRows(); i++) {
                if (board[i][col].equals(board[i + 1][col]) && board[i][col].equals("R")) {
                    valColsP1[col] = valColsP1[col] + 1;
                } else if (board[i][col].equals(board[i + 1][col]) && board[i][col].equals("Y")) {
                    valColsP2[col] = valColsP2[col] + 1;
                }
            }
            col = col + 2;
        }
        // once the loop is finished, check if each array holds a value of X or more
        // and if so, return true to signal the game has been won vertically
        for (int k = 0; k < valColsP1.length; k++) {
            if (valColsP1[k] >= model.getNeededToWin() || valColsP2[k] >= model.getNeededToWin()) {
                return true;
            }
        }
        return false;
    }

    // checks upward (/) diagonal
    public boolean checkDiagonalUp(String[][] board) {
        int rows = model.getNrRows();
        int cols = model.getNrCols();
        int winningTotal = model.getNeededToWin();
        // this 2D array only pulls the contents of board
        // and stores them in a rows x cols array, so that a normal nested for-loop can be used
        String[][] boardContent = new String[rows][cols];
        for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
                boardContent[a][b] = board[a][2 * (b + 1) - 1];
            }
        }
        // this loop iterates through every point on the board
        // and checks for X consecutive R's or Y's on the upward diagonal
        for (int row = rows - 1; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                // the two counters are reset for every point
                // so that the checks are relative to a fixed point on the grid
                int countP1 = 1;
                int countP2 = 1;
                for (int n = 1; n < winningTotal; n++) {
                    // the try-catch is used to avoid any index out of bounds errors
                    try {
                        // if a consecutive match is found, the corresponding counter is incremented
                        if (boardContent[row][col].equals("R") && boardContent[row - n][col + n].equals("R")) {
                            countP1++;
                            // if the counter is greater than or equal to X
                            // return true to signal the game was won on the upward diagonal
                            if (countP1 >= winningTotal) {
                                return true;
                            }
                        } else if (boardContent[row][col].equals("Y") && boardContent[row - n][col + n].equals("Y")) {
                            countP2++;
                            if (countP2 >= winningTotal) {
                                return true;
                            }
                        }
                    }
                    // if an error is found, catch it and continue the loop
                    catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return false;
    }

    // checks downward (\) diagonal
    public boolean checkDiagonalDown(String[][] board) {
        int rows = model.getNrRows();
        int cols = model.getNrCols();
        int winningTotal = model.getNeededToWin();
        // once again, this 2D array only pulls the contents of board
        // and stores them in a rows x cols array, so that a normal nested for-loop can be used
        String[][] boardContent = new String[rows][cols];
        for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
                boardContent[a][b] = board[a][2 * (b + 1) - 1];
            }
        }
        // this loop iterates through every point on the board
        // and checks for X consecutive R's or Y's on the downward diagonal
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // the two counters are reset for every point
                // so that the checks are relative to a fixed point on the grid
                int countP1 = 1;
                int countP2 = 1;
                for (int n = 1; n < winningTotal; n++) {
                    // the try-catch is used to avoid any index out of bounds errors
                    try {
                        // if a consecutive match is found, the corresponding counter is incremented
                        if (boardContent[row][col].equals("R") && boardContent[row + n][col + n].equals("R")) {
                            countP1++;
                            // if the counter is greater than or equal to X
                            // return true to signal the game was won on the downward diagonal
                            if (countP1 >= winningTotal) {
                                return true;
                            }
                        } else if (boardContent[row][col].equals("Y") && boardContent[row + n][col + n].equals("Y")) {
                            countP2++;
                            if (countP2 >= winningTotal) {
                                return true;
                            }
                        }
                    }
                    // if an error is found, catch it and continue the loop
                    catch (Exception e) {
                        continue;
                    }
                }
            }
        }
        return false;
    }

    // this method takes care of facing the AI at an easy level
    // where the AI randomises numbers
    public void playEasyAI(int player) {
        while (true) {
            // deals with the player chosen by the user
            if (player == 1) {
                // if it's the player's turn, it allows them to input a move
                if (model.getCounter() % 2 == 0) {
                    int move = makePlayerMove();
                    if (!onceMoveIsMade(move)) {
                        break;
                    }
                }
                // if it's not the player's turn, then the AI randomises the input
                else {
                    int moveAI = makeEasyAIMove(player);
                    if (!onceMoveIsMade(moveAI)) {
                        break;
                    }
                }
            }
            else {
                // once again, the counter allows the user to input their move at the correct time
                if (model.getCounter() % 2 == 1) {
                    int move = makePlayerMove();
                    if (!onceMoveIsMade(move)) {
                        break;
                    }
                } else {
                    // once again, the AI randomises the input
                    int moveAI = makeEasyAIMove(player);
                    if (!onceMoveIsMade(moveAI)) {
                        break;
                    }
                }
            }
        }
    }

    // this method takes care of facing the AI at a hard level
    // where the AI makes more strategic, logical moves in order to win
    public void playHardAI(int player) {
        while (true) {
            // deals with the player chosen by the user
            if (player == 1) {
                // if it's the player's turn, it allows them to input a move
                if (model.getCounter() % 2 == 0) {
                    int move = makePlayerMove();
                    if (!onceMoveIsMade(move)) {
                        break;
                    }
                }
                // if it's not the player's turn, then the AI makes a more logical move
                else {
                    int moveAI = makeHardAIMove(player);
                    if (!onceMoveIsMade(moveAI)) {
                        break;
                    }
                }
            }
            else {
                // once again, the counter allows the user to input their move at the correct time
                if (model.getCounter() % 2 == 1) {
                    int move = makePlayerMove();
                    if (!onceMoveIsMade(move)) {
                        break;
                    }
                } else {
                    // once again, the AI makes a more logical move when it's its turn
                    int moveAI = makeHardAIMove(player);
                    if (!onceMoveIsMade(moveAI)) {
                        break;
                    }
                }
            }
        }
    }

    // this method allows for the 2 player game
    public void play2Players() {
        while (true) {
            int move = makePlayerMove();
            if (!onceMoveIsMade(move)){
                break;
            }
        }
    }

    // this method deals with the user making a move
    // also displays the move associated with the correct player
    public int makePlayerMove() {
        if (model.getCounter() % 2 == 0) {
            System.out.print("Player 1 make a move: ");
        } else {
            System.out.print("Player 2 make a move: ");
        }
        int move = InputUtil.readIntFromUser();
        return move;
    }

    // this method deals with the aftermath of a move
    public boolean onceMoveIsMade(int move) {
        // handles the user input for conceding the game
        if (move == 0) {
            concededGame();
            // if the user doesn't wish to play again, the loop is broken
            String answer = reset();
            if (answer.equalsIgnoreCase("yes")) {
                counter = 0;
                model.setCounter(0);
                startSession();
                return false;
            } else {
                return false;
            }
        }
        else if (model.isMoveValid(move) && move != 0){
            // once the game is valid, the move is added to the board
            // and the board updated
            model.makeMove(move);
            view.displayBoard(model);
            // then the board is checked for a draw
            if (isDraw()) {
                String answer = reset();
                if (answer.equalsIgnoreCase("yes")) {
                    counter = 0;
                    model.setCounter(0);
                    startSession();
                    return false;
                } else {
                    return false;
                }
            }
            // if it's not a draw, then check for a win
            else if (checkForWin()){
                String answer = reset();
                if (answer.equalsIgnoreCase("yes")) {
                    counter = 0;
                    model.setCounter(0);
                    startSession();
                    return false;
                } else {
                    return false;
                }
            }
            model.incrementCounter();
        }
        return true;
    }

    // this method is in charge of resetting the game if the user wishes to play again
    // returns false in order to break the game cycle if the answer is no
    public String reset(){
        System.out.print("Would you like to play again? ");
        String answer = InputUtil.readStringFromUser();
        while (!answer.equalsIgnoreCase("no") && !answer.equalsIgnoreCase("yes")) {
            System.out.print("Invalid answer, please try again: ");
            answer = InputUtil.readStringFromUser();
        }
        return answer;
    }

    // this method allows the user to choose which player they wish to be
    // only for playing against the AI (easy or hard)
    public int selectPlayer() {
        System.out.print("Which player would you like to be? (type 1 for player 1 and 2 for player 2) ");
        int player = InputUtil.readIntFromUser();
        // this validates user input to avoid any odd inputs, such as large numbers or negative numbers
        while (player != 1 && player != 2) {
            System.out.print("Invalid player number, please try again: ");
            player = InputUtil.readIntFromUser();
        }
        return player;
    }

    // this method takes care of displaying the correct winner for a conceded game
    public void concededGame() {
        counter = 0;
        model.incrementCounter();
        if (model.getCounter() % 2 == 0) {
            System.out.println("Player 1 won!");
        } else {
            System.out.println("Player 2 won!");
        }
    }

    // this method checks for a win in Connect X
    // return true if the game has been won, otherwise false
    public boolean checkForWin() {
        // checks horizontal, vertical, and both diagonals, then displays the correct winner
        if (checkHorizontal(model.getBoard()) || checkVertical(model.getBoard()) || checkDiagonalDown(model.getBoard()) || checkDiagonalUp(model.getBoard())) {
            counter = 0;
            if (model.getCounter() % 2 == 0) {
                System.out.println("Player 1 won!");
            } else {
                System.out.println("Player 2 won!");
            }
            return true;
        }
        return false;
    }

    // this method checks if the game ends as a draw and handles that situation
    public boolean isDraw() {
        if (model.getCounter() == (long) model.getNrCols() * model.getNrRows() - 1) {
            System.out.println("It's a draw!");
            counter = 0;
            return true;
        }
        return false;
    }

    // this method deals with the easy AI making a move
    // used in the playEasyAI method, for randomising numbers
    public int makeEasyAIMove(int player) {
        Random rand = new Random();
        int moveAI = rand.nextInt(model.getNrCols() + 1);
        // ensures the move is associated with the correct player based on the user's choice
        if (player == 1) {
            System.out.println("Player 2 make a move: " + moveAI);
        }
        else {
            System.out.println("Player 1 make a move: " + moveAI);
        }
        return moveAI;
    }

    // this method deals with the hard AI making a move
    // used in the playHardAI method
    // also ensures the move is associated with the correct player based on the user's choice
    public int makeHardAIMove(int player) {
        String[][] board = model.getBoard();
        int rows = model.getNrRows();
        int cols = model.getNrCols();
        // the boardContent 2D array only pulls the content of the board
        // and ignores the display features used in the original 2D array
        String[][] boardContent = new String[rows][cols];
        for (int a = 0; a < rows; a++) {
            for (int b = 0; b < cols; b++) {
                boardContent[a][b] = board[a][2 * (b + 1) - 1];
            }
        }
        // this array is used to store the set of potential moves for the AI
        potentialAIMoves = new int[model.getNeededToWin()];
        int columnCounter = 0;
        // the following implementation checks for empty spaces horizontally as potential moves for the AI
        // however, if the player interferes with the original plan, the AI adapts
        // by constantly checking the state of the board
        for (int i = model.getNrRows() - 1; i >= 0; i--) {
            int emptyInARow = 0;
            for (int j = 0; j < model.getNrCols(); j++) {
                if (boardContent[i][j].equals(" ")) {
                    emptyInARow++;
                } else {
                    emptyInARow = 0;
                }
                if (emptyInARow >= model.getNeededToWin()) {
                    columnCounter = j;
                    break;
                }
            }
            if (columnCounter != 0 ) {
                break;
            }
        }
        // once the moves are decided, they are added to the potentialAIMoves array
        int index = 0;
        for (int i = columnCounter; i > columnCounter - model.getNeededToWin(); i--) {
            potentialAIMoves[index] = i + 1;
            index++;
        }
        int moveAI = potentialAIMoves[counter % model.getNeededToWin()];
        counter++;
        // ensures the move is associated with the correct player based on the user's choice
        if (player == 1) {
            System.out.println("Player 2 make a move: " + moveAI);
        }
        else {
            System.out.println("Player 1 make a move: " + moveAI);
        }
        return moveAI;
    }
}

