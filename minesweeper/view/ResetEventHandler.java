package minesweeper.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ResetEventHandler implements EventHandler<ActionEvent>{
    private MinesweeperGUI gui;
    private Stage stage;

    public ResetEventHandler(MinesweeperGUI gui, Stage stage){
        this.gui = gui;
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent arg0) {
        gui.setUp(stage);
        gui.getResetLabel().setText("new game");
    }


    
}
