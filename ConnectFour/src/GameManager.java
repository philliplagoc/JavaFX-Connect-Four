import java.util.Scanner;

/* Handles the basic game logic of Connect Four*/
public class GameManager {
	private Board board; // Game board
	
	/* no-arg ctor creates an empty board */
	public GameManager() {
		this.board = new Board();
	}
	
	/* Handles actual play logic of the game*/
	public void play() {
		Scanner in = new Scanner(System.in);
		boolean gameIsOver = false;
		while(!gameIsOver) {
			this.printState();
			if(this.board.isGameOver()) {
				System.out.println("Game Over!\nPlayer " + board.getCurrToken() + " won!");
				gameIsOver = true;
			} else {
				int columnIndex = in.nextInt();
				if(this.board.drop(columnIndex-1))
					this.board.switchPlayer();
			}
		}
	}
	
	/* Prints state of the game */
	public void printState() {
		System.out.println("	Controls:");
		System.out.println("Press a number to indicate which" + 
					" column you would like to drop your piece in.");
		System.out.println(this.board.toString());
	}
	
	// TODO: Remove after testing
	public static void main(String[] args) {
		GameManager gm = new GameManager();
		gm.play();
	}
}
