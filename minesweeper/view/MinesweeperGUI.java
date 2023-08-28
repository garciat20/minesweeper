package minesweeper.view;

import backtracker.Backtracker;
import backtracker.Configuration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import minesweeper.model.Location;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperConfiguration;
import minesweeper.model.MinesweeperException;
//this comment is for testing new branches
public class MinesweeperGUI extends Application{
    private Minesweeper minesweeper;
    private Label moves;
    private Label mineNum;
    private Label gameStateLabel;
    private Button[][] buttons;//It's stored in the format buttons[colums][rows]
    public static  boolean gameSolved = false;
    private final int ROWS=6;
    private final int COLS=6;
    private final int MINES=6;

    @Override
    public void start(Stage stage) throws Exception {
        setUp(stage);
    }
    
    public void setUp(Stage stage){
        minesweeper = new Minesweeper(ROWS, COLS, MINES);

        GridPane grid = makeGridPane();
        // System.out.println("hi");
        HBox hbox = new HBox();
        VBox vbox = new VBox();
        BorderPane pain = new BorderPane();

        moves = new Label("0"); // must be updated
        moves.setAlignment(Pos.CENTER);
        moves.setMaxWidth(Double.MAX_VALUE);
        mineNum = new Label(Integer.toString(minesweeper.getMineCount())); // must be updated
        mineNum.setAlignment(Pos.CENTER);
        mineNum.setMaxWidth(Double.MAX_VALUE);
        gameStateLabel = new Label("new game"); // temp
        gameStateLabel.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
        gameStateLabel.setMaxWidth(Double.POSITIVE_INFINITY);
        gameStateLabel.setAlignment(Pos.CENTER);

        Button hint = new Button("Hint");   // event handler    // lights up a box
        hint.setOnAction(new HintEventHandler(minesweeper, buttons, gameStateLabel));

        Button reset = new Button("Reset"); // event handler
        reset.setOnAction(new ResetEventHandler(this, stage));
        
        //boolean gameSolved = false;
        Button solve = new Button("Solve");

        solve.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                Backtracker backtracker = new Backtracker(false);
                MinesweeperConfiguration config = new MinesweeperConfiguration(minesweeper);
                Configuration solution = backtracker.solve(config);
                gameStateLabel.setText("Solving Game");
                MinesweeperGUI.gameSolved = true;
                new Thread(()->{
                    for(Location c : config.getPath()){
                        Platform.runLater(()->{
                            int row = c.getRow();
                            int col = c.getCol();
                            buttons[row][col].setText(Integer.toString(minesweeper.board[row][col]));
                            buttons[row][col].setDisable(true);
                        });
                        try{
                            Thread.sleep(250);
                        }catch(InterruptedException e){}
                    }
                    for(String key : minesweeper.getMineTracker()){
                        Platform.runLater(()->{
                            String[] tokens = key.strip().split(",");
                            int row = Integer.parseInt(tokens[0]);
                            int col = Integer.parseInt(tokens[1]);
                            buttons[row][col].setGraphic(new ImageView("file:media/images/mine24.png")); 
                            buttons[row][col].setDisable(true);
                            gameStateLabel.setText("Done!");
                        });
                    }   
                    
                           
                    //disableButtons();
                }).start();
                
                // for(Location c : config.getPath()){
                //     int row = c.getRow();
                //     int col = c.getCol();
                //     buttons[row][col].setText(Integer.toString(minesweeper.board[row][col]));
                // }

                // for(String key : minesweeper.getMineTracker()){
                //     String[] tokens = key.strip().split(",");
                //     int row = Integer.parseInt(tokens[0]);
                //     int col = Integer.parseInt(tokens[1]);
                //     buttons[row][col].setGraphic(new ImageView("file:media/images/mine24.png")); 
                // }
            }
        });
    
        if (MinesweeperGUI.gameSolved){
            gameStateLabel.setText("Game Solved");
        }
             
        vbox.getChildren().addAll(moves, mineNum,solve, hint, reset);
        hbox.getChildren().addAll(vbox,grid);
        pain.setTop(hbox);
        pain.setBottom(gameStateLabel);

        //observer pattern
        minesweeper.register((location)->{
            //update viewGUI
            gameStateLabel.setText("Game in progress");
            Button button = buttons[location.getRow()][location.getCol()];
            //If the button is a mine it will display the mine image
            int val = minesweeper.board[location.getRow()][location.getCol()];
            if(val==9){
                button.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                button.setGraphic(new ImageView("file:media/images/mine24.png")); 
                disableButtons();
                gameStateLabel.setText("Boom! Better luck next time!");
            }

            else if(val == 0){
                // do nothing
                button.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                gameStateLabel.setText("Game in progress");  
            }
            else{
                button.setText(Integer.toString(val));
                button.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                if(minesweeper.getPossibleSelections().size()==0){
                     
                    disableButtons();   
                  gameStateLabel.setText("You won!!");
                }
                
                
            
            }
            // String str = minesweeper.getGameState().toString();
            // gameStateLabel.setText(str);
            //else we will display the value of the grid
            //button.setText("2"); //image of number if not mine
            //button.setGraphic(new ImageView("file:media/images/mines24.png"));//Image of mine
        });//lambda

        stage.setScene(new Scene(pain));
        stage.setTitle("Minesweeper");
        stage.show();
    }

    // helper to disable buttons
    private void disableButtons(){
        for (int i = 0 ; i < minesweeper.cols ; i ++ ){
            for (int j = 0; j < minesweeper.rows; j ++){
                // board is covered
                buttons[i][j].setDisable(true);
                if (minesweeper.isCovered[i][j]){
                    if (minesweeper.board[i][j] > 8){
                        // col then row bc buttons is backwards, if something goes wrong do the opposite
                        buttons[i][j].setGraphic(new ImageView("file:media/images/mine24.png")); 
                    }
                    else if (minesweeper.board[i][j] > 0){
                        // buttons[i][j].setTextFill(Color.ALICEBLUE);
                        buttons[i][j].setText(Integer.toString(minesweeper.board[i][j]));
                        
                    }
                }        
            }
        }
    }

    private GridPane makeGridPane(){
        buttons = new Button[COLS][ROWS];
        GridPane gridpane = new GridPane();
        for(int i =0; i<COLS; i++){
            for(int j = 0; j<ROWS; j++){
                System.out.println();
                Button button = makeButtons(i, j);
                buttons[i][j] = button;
                // button.setOnAction(new )
                gridpane.add(button,i,j);
            }
        }
        return gridpane;
    }

    // event handler for button
    // Location c =minesweeper.locationMap.get(Integer.toString(rows)+Integer.toString(cols));
    // char character = minesweeper.getSymbol(c);
    // if(!character == '0'){
    //     button.setText(Character.toString(character));
    // }
    

    private Button makeButtons(int rows, int cols){


        Button button = new Button();
        button.setPrefSize(50,50);
        // button.setBackground(new ImageView("file:media/images/cover.png")); 
        button.setBackground(
            new Background(
                new BackgroundImage(new Image("file:media/images/cover.png"), 
                    BackgroundRepeat.NO_REPEAT, 
                    BackgroundRepeat.NO_REPEAT, 
                    BackgroundPosition.CENTER, 
                    BackgroundSize.DEFAULT)));

        button.setOnAction((event)->{
            try {
                minesweeper.makeSelection(new Location(rows, cols));
                int currMoves = Integer.parseInt(moves.getText());
                currMoves += 1;
                moves.setText(Integer.toString(currMoves));
            } catch (MinesweeperException e) {
                e.printStackTrace();
            }
        }); //pass in an lambda expression 1.update backend to chane the state of minesweeper
        return button;

    }

    // part 3
    private void solve2(){
        
    }
    public Label getResetLabel() {
        return this.gameStateLabel;
    }
    public static void main(String[] args) {
        launch();
    }
    
}
