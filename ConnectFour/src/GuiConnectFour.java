import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/* Class handles all of the GUI parts of the Connect Four
 * Application.
 * Players will be able to drop tiles into the board using the number pad.
 * This class also uses Board.java as a back-end for the GUI elements.
 */
public class GuiConnectFour extends Application {
	private Board board;
	private static final int TILE_WIDTH = 35;
	private static final Color PLAYER1COLOR = Color.rgb(229, 16, 48); // x
	private static final Color PLAYER2COLOR = Color.rgb(236, 247, 34); // o
	private static final Color EMPTY_COLOR = Color.rgb(242, 232, 213);
	private static final Color BG_COLOR = Color.rgb(211, 208, 203);

	private StackPane baseStackPane; // Base Pane
	private BorderPane borderPane; // Holds gridPane
	private GridPane gridPane; // Holds board
	private StackPane titlePane; // Holds title
	private StackPane playerPane; // Holds playerDisplay
	private StackPane gameOverPane; // Game over Pane
	private Text title; // Title
	private Text playerDisplay; // Shows currPlayer
	private Circle[][] guiTiles; // GUI grid
	private int boardRowSize;
	private int boardColSize;
	private char currToken;
	private Circle tokenIndicator; // Color represents whose turn it is
	private char[][] boardGrid; // Internal board

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Construct board and panes
		board = new Board();
		boardRowSize = Board.GRID_SIZE_ROWS;
		boardColSize = Board.GRID_SIZE_COLS;
		boardGrid = board.getGrid();
		currToken = board.getCurrToken();
		baseStackPane = new StackPane();
		gameOverPane = new StackPane();
		borderPane = new BorderPane();
		titlePane = new StackPane();
		playerPane = new StackPane();
		gridPane = new GridPane();

		// Set style of all Panes
		baseStackPane.setStyle("-fx-background-color: rgb(211, 208, 203)");
		gameOverPane.setStyle("-fx-background-color: transparent");
		borderPane.setStyle("-fx-background-color: transparent");
		titlePane.setStyle("-fx-background-color: rgb(211, 208, 203)");
		playerPane.setStyle("-fx-background-color: transparent");
		gridPane.setStyle("-fx-background-color: rgb(0, 70, 242)");

		// Add borderPane and gameOverPane to baseStackPane
		baseStackPane.getChildren().addAll(borderPane, gameOverPane);

		// Set alignment of gridPane and its attributes
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		gridPane.setHgap(15);
		gridPane.setVgap(15);

		// Create titlePane and properties
		title = new Text("Connect Four");
		title.setFont(Font.font("Impact", FontWeight.BOLD, FontPosture.ITALIC, 70));
		title.setFill(Color.BLACK);
		titlePane.getChildren().add(title);

		// Create playerPane and properties
		// playerDisplay = new Text("Player " + currToken + "'s turn");
		playerDisplay = new Text("'s turn");
		playerDisplay.setFont(Font.font("Monospaced", FontWeight.BOLD, 50));
		// playerDisplay.setFill(Color.BLACK);
		StackPane.setAlignment(playerDisplay, Pos.CENTER_RIGHT);
		tokenIndicator = new Circle(TILE_WIDTH);
		tokenIndicator.setFill(determineTileColor(currToken));
		StackPane.setAlignment(tokenIndicator, Pos.CENTER);
		playerPane.getChildren().addAll(tokenIndicator, playerDisplay);

		// Set borderPane properties and place player and title pane on it
		borderPane.setPadding(new Insets(15, 10, 10, 10));
		borderPane.setCenter(gridPane);
		BorderPane.setAlignment(gridPane, Pos.CENTER);
		borderPane.setBottom(playerPane);
		BorderPane.setMargin(playerPane, new Insets(15, 15, 15, 15));
		BorderPane.setAlignment(playerPane, Pos.CENTER);
		borderPane.setTop(titlePane);
		BorderPane.setAlignment(titlePane, Pos.CENTER);

		// Init tiles
		guiTiles = new Circle[boardRowSize][boardColSize];
		for (int row = 0; row < boardRowSize; row++) {
			for (int col = 0; col < boardColSize; col++) {
				guiTiles[row][col] = new Circle(TILE_WIDTH);
			}
		}
		this.constructTiles();

		// Add titles
		for (int row = 0; row < boardRowSize; row++) {
			for (int col = 0; col < boardColSize; col++) {
				Circle tile = guiTiles[row][col];
				gridPane.add(tile, col, row + 1, 1, 1);
				GridPane.setHalignment(tile, HPos.CENTER);
			}
		}

		// Add column indicators
		int columnNumber = 1;
		for (int col = 0; col < boardColSize; col++) {
			Text colText = new Text("" + columnNumber);
			colText.setFont(Font.font("Arial Black", FontWeight.BOLD, 20));
			colText.setFill(Color.WHITE);
			gridPane.add(colText, col, 0, 1, 1);
			GridPane.setHalignment(colText, HPos.CENTER);
			columnNumber++;
		}

		Scene scene = new Scene(baseStackPane);
		scene.setOnKeyPressed(new PlayHandler());
		primaryStage.setTitle("Connect Four");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	// Construct tiles and its colors
	private void constructTiles() {
		for (int i = 0; i < boardRowSize; i++) {
			for (int j = 0; j < boardColSize; j++) {
				Circle circle = guiTiles[i][j];
				char internalChar = this.boardGrid[i][j];
				Color tileColor = this.determineTileColor(internalChar);
				circle.setFill(tileColor);
			}
		}
	}

	// Determines tile color
	private Color determineTileColor(char c) {
		Color color;
		if (c == Board.PLAYER1TOKEN)
			color = PLAYER1COLOR;
		else if (c == Board.PLAYER2TOKEN)
			color = PLAYER2COLOR;
		else
			color = EMPTY_COLOR;
		return color;
	}

	// Constructs playerDisplay
	private void constructPlayerDisplay() {
		this.currToken = board.getCurrToken();
		playerDisplay.setText("'s turn");
		tokenIndicator.setFill(determineTileColor(currToken));
	}

	// Reset board
	private void resetBoard() {
		for (int i = 0; i < boardRowSize; i++) {
			for (int j = 0; j < boardColSize; j++) {
				guiTiles[i][j].setFill(EMPTY_COLOR);
			}
		}
	}

	// Refreshes GUI
	private void refresh() {
		resetBoard();
		boardGrid = board.getGrid();
		constructPlayerDisplay();
		constructTiles();
	}

	// Game over method
	private void gameOver() {
		Text gameOverText = new Text("Game Over!\n      Player");
		Text winner = new Text(" won!");
		tokenIndicator.setFill(determineTileColor(currToken));
		tokenIndicator.setRadius(TILE_WIDTH * 2);
		playerDisplay.setFill(BG_COLOR);

		VBox vBox = new VBox(30);
		vBox.setPadding(new Insets(15, 15, 15, 15));
		vBox.setAlignment(Pos.CENTER);

		gameOverText.setFont(Font.font("Impact", FontWeight.BOLD, 90));
		winner.setFont(Font.font("Impact", FontWeight.BOLD, 90));
		gameOverPane.setStyle("-fx-background-color: rgba(218, 225, 238, 0.73);");

		vBox.getChildren().addAll(gameOverText, tokenIndicator, winner);
		gameOverPane.getChildren().add(vBox);

		// Cover bottom of borderPane
		Rectangle cover = new Rectangle();
		cover.setFill(BG_COLOR);
		borderPane.setBottom(cover);

		baseStackPane.setStyle("-fx-background-color: rgba(218, 225, 238, 0.73);");
	}

	private class PlayHandler implements EventHandler<KeyEvent> {
		@Override
		public void handle(KeyEvent e) {
			if (!board.isGameOver()) {
				if (e.getCode() == KeyCode.NUMPAD1) {
					if (!board.isGameOver() && board.drop(0)) {
						if (board.isGameOver()) {
							gameOver();
							refresh();
							return;
						}
						board.switchPlayer();
						refresh();
					}
				} else if (e.getCode() == KeyCode.NUMPAD2) {
					if (!board.isGameOver() && board.drop(1)) {
						if (board.isGameOver()) {
							gameOver();
							refresh();
							return;
						}
						board.switchPlayer();
						refresh();
					}
				} else if (e.getCode() == KeyCode.NUMPAD3) {
					if (!board.isGameOver() && board.drop(2)) {
						if (board.isGameOver()) {
							gameOver();
							refresh();
							return;
						}
						board.switchPlayer();
						refresh();
					}
				} else if (e.getCode() == KeyCode.NUMPAD4) {
					if (!board.isGameOver() && board.drop(3)) {
						if (board.isGameOver()) {
							gameOver();
							refresh();
							return;
						}
						board.switchPlayer();
						refresh();
					}
				} else if (e.getCode() == KeyCode.NUMPAD5) {
					if (!board.isGameOver() && board.drop(4)) {
						if (board.isGameOver()) {
							gameOver();
							refresh();
							return;
						}
						board.switchPlayer();
						refresh();
					}
				} else if (e.getCode() == KeyCode.NUMPAD6) {
					if (!board.isGameOver() && board.drop(5)) {
						if (board.isGameOver()) {
							gameOver();
							refresh();
							return;
						}
						board.switchPlayer();
						refresh();
					}
				}
			}
		}
	}
}
