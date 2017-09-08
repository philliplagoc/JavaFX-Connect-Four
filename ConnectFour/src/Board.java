import java.util.Arrays;

/* Responsible for managing the internal state of the ConnectFour board.
 * Will check if the game is over by checking for winners, and handle switching players.
 * Will be used in GameManager.java and ConnectFourGui.java
 */
public class Board {
	public char[][] grid; // Represents actual gaming board
	private char currToken; // Represents active player pieces (x and o)
	public static final char PLAYER1TOKEN = 'x';
	public static final char PLAYER2TOKEN = 'o';
	public static final int GRID_SIZE_ROWS = 6;
	public static final int GRID_SIZE_COLS = 7;

	/*
	 * Initializes grid to a 7 columns by 6 rows empty char matrix. currToken will
	 * be x by default
	 */
	public Board() {
		this.grid = new char[GRID_SIZE_ROWS][GRID_SIZE_COLS];
		for (int row = 0; row < GRID_SIZE_ROWS; row++) {
			for (int col = 0; col < GRID_SIZE_COLS; col++) {
				grid[row][col] = ' ';
			}
		}
		this.currToken = PLAYER1TOKEN;
	}

	// Getters and setters
	public char[][] getGrid() {
		return grid;
	}

	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public char getCurrToken() {
		return currToken;
	}

	public void setCurrToken(char token) {
		this.currToken = token;
	}

	@Override
	public String toString() {
		StringBuilder outputString = new StringBuilder();
		outputString.append("Connect Four: Player " + this.getCurrToken() + "'s turn\n");
		for (int row = 0; row < GRID_SIZE_ROWS; row++) {
			for (int column = 0; column < GRID_SIZE_COLS; column++) {
				outputString.append(grid[row][column] == ' ' ? "    -" : String.format("%5s", grid[row][column]));
			}

			outputString.append("\n");
		}
		return outputString.toString();
	}

	/*
	 * Checks for a winner if there are either four in a row horizontally,
	 * diagonally, or vertically
	 * 
	 * @return: boolean value determining if game is over
	 */
	public boolean isGameOver() {
		return this.checkHorizontal() || this.checkVertical() || this.checkDiagonal();
	}

	/*
	 * Switches currToken
	 */
	public void switchPlayer() {
		if (this.currToken == PLAYER1TOKEN)
			this.setCurrToken(PLAYER2TOKEN);
		else if (this.currToken == PLAYER2TOKEN)
			this.setCurrToken(PLAYER1TOKEN);
	}

	/*
	 * Drops the currToken into the specified column and returns whether or not a
	 * move has been made
	 * 
	 * @return: Whether or not a move has been made
	 */
	public boolean drop(int columnIndex) {
		boolean moveMade = false;
		if (this.canDrop(columnIndex)) {
			int rowIndex = 0;
			this.grid[rowIndex][columnIndex] = this.getCurrToken();
			while (rowIndex < 5 && this.grid[rowIndex + 1][columnIndex] == ' ') {
				this.grid[rowIndex + 1][columnIndex] = this.getCurrToken();
				this.grid[rowIndex][columnIndex] = ' ';
				rowIndex++;
			}
			moveMade = true;
		}
		return moveMade;
	}

	/*
	 * Checks whether or not the column to drop into is valid
	 * 
	 * @return: boolean determining if column has at least one empty space for token
	 * to fall into
	 */
	public boolean canDrop(int columnIndex) {
		for (int i = 0; i < GRID_SIZE_ROWS; i++) {
			if (this.grid[i][columnIndex] == ' ')
				return true;
		}
		return false;
	}

	/*****************************
	 * isGameOver() HELPER METHODS BELOW
	 *****************************/
	// Checks to see if the 4 passed in chars are the same
	private boolean allCharsSame(char c1, char c2, char c3, char c4) {
		return this.currToken == c1 && c1 == c2 && c2 == c3 && c3 == c4;
	}

	// Checks if there is a winner horizontally
	private boolean checkHorizontal() {
		for (int row = 0; row < GRID_SIZE_ROWS; row++) {
			for (int col = 0; col < GRID_SIZE_COLS - 3; col++) {
				char c1 = this.grid[row][col];
				char c2 = this.grid[row][col + 1];
				char c3 = this.grid[row][col + 2];
				char c4 = this.grid[row][col + 3];
				if (this.allCharsSame(c1, c2, c3, c4))
					return true;
			}
		}
		return false;
	}

	// Checks if there is a winner vertically
	private boolean checkVertical() {
		for (int col = 0; col < GRID_SIZE_COLS; col++) {
			for (int row = 0; row < GRID_SIZE_ROWS - 3; row++) {
				char c1 = this.grid[row][col];
				char c2 = this.grid[row + 1][col];
				char c3 = this.grid[row + 2][col];
				char c4 = this.grid[row + 3][col];
				if (this.allCharsSame(c1, c2, c3, c4))
					return true;
			}
		}
		return false;
	}

	// Checks if there is a winner diagonally
	private boolean checkDiagonal() {
		return this.checkDiagonal1() || this.checkDiagonal2();
	}

	// Checks for a winner along diagonal 1
	// Diagonal 1
	private boolean checkDiagonal1() {
		for (int row = 0; row < GRID_SIZE_ROWS - 3; row++) {
			for (int col = 0; col < GRID_SIZE_COLS - 3; col++) {
				char c1 = this.grid[row][col];
				char c2 = this.grid[row + 1][col + 1];
				char c3 = this.grid[row + 2][col + 2];
				char c4 = this.grid[row + 3][col + 3];
				if (this.allCharsSame(c1, c2, c3, c4))
					return true;
			}
		}
		return false;
	}

	// Checks for a winner along diagonal 2
	private boolean checkDiagonal2() {
		for (int row = 0; row < GRID_SIZE_ROWS - 3; row++) {
			for (int col = GRID_SIZE_COLS - 1; col >= GRID_SIZE_COLS - 4; col--) {
				char c1 = this.grid[row][col];
				char c2 = this.grid[row + 1][col - 1];
				char c3 = this.grid[row + 2][col - 2];
				char c4 = this.grid[row + 3][col - 3];
				if (this.allCharsSame(c1, c2, c3, c4))
					return true;
			}
		}
		return false;
	}

	// TODO: Remove after testing
	public static void main(String[] args) {
		Board b = new Board();

		// Testing dropping into empty board
		/*System.out.println(b);
		System.out.println("isGameOver val: " + b.isGameOver());
		System.out.println("Column 0 Empty: " + b.canDrop(0));
		System.out.println("Column 1 Empty: " + b.canDrop(1));
		System.out.println("Column 2 Empty: " + b.canDrop(2));
		System.out.println(b.drop(5));
		System.out.println(b);
		System.out.println(b.drop(0));
		System.out.println(b);
		*/
		
		// Testing horizontal win conditions
		char[][] horzWinBoard = {
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{'x', 'x', 'x', 'x', ' ', ' ', ' '},
		};
		/*
		b.setGrid(horzWinBoard);
		System.out.println(b);
		System.out.println("isGameOver val: " + b.isGameOver());
		System.out.println("Can drop in Col0: " + b.canDrop(0));
		System.out.println("Can drop in Col1: " + b.canDrop(1));
		System.out.println("Can drop in Col2: " + b.canDrop(2));
		System.out.println(b.drop(5));
		System.out.println(b);
		System.out.println(b.drop(0));
		System.out.println(b);
		*/
		
		// Testing vertical win conditions
		char[][] vertWinBoard = {
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{'x', ' ', ' ', ' ', ' ', ' ', ' '},
				{'x', ' ', ' ', ' ', ' ', ' ', ' '},
				{'x', ' ', ' ', ' ', ' ', ' ', ' '},
				{'x', ' ', ' ', ' ', ' ', ' ', ' '},
		};
		/*
		b.setGrid(vertWinBoard);
		System.out.println(b);
		System.out.println("isGameOver val: " + b.isGameOver());
		System.out.println("Can drop in Col0: " + b.canDrop(0));
		System.out.println("Can drop in Col1: " + b.canDrop(1));
		System.out.println("Can drop in Col2: " + b.canDrop(2));
		System.out.println(b.drop(5));
		System.out.println(b);
		System.out.println(b.drop(0));
		System.out.println(b);
		System.out.println(b.drop(0));
		System.out.println(b);
		System.out.println(b.drop(0));
		System.out.println(b);
		*/
		// Testing vertical win conditions
		char[][] diagWinBoard = {
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', 'x', ' ', ' ', ' ', ' '},
				{' ', 'x', ' ', ' ', ' ', ' ', ' '},
				{'x', ' ', ' ', ' ', ' ', ' ', ' '},
		};
		b.setGrid(diagWinBoard);
		System.out.println(b);
		System.out.println("isGameOver val: " + b.isGameOver());
		System.out.println("Can drop in Col0: " + b.canDrop(0));
		System.out.println("Can drop in Col1: " + b.canDrop(1));
		System.out.println("Can drop in Col2: " + b.canDrop(2));
		System.out.println(b.drop(5));
		System.out.println(b);
		System.out.println(b.drop(0));
		System.out.println(b);
	}
}
