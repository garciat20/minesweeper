package minesweeper.view;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import minesweeper.model.Location;
import minesweeper.model.Minesweeper;

public class HintEventHandler implements EventHandler<ActionEvent>{
    private Minesweeper minesweeper;
    private Button[][] buttons;
    private Label gameStateLabel;

    public HintEventHandler(Minesweeper minesweeper, Button[][] buttons, Label gameStateLabel){
        this.minesweeper = minesweeper;
        this.buttons = buttons;
        this.gameStateLabel= gameStateLabel;
    }

    @Override
    public void handle(ActionEvent arg0) {
        List<Location> list = minesweeper.getPossibleSelections();
        if(list.size()>0){
            Location location = list.get(list.size()-1);
            Button button = buttons[location.getRow()][location.getCol()];
            button.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        else{
            gameStateLabel.setText("No more available hints left");
        }
        
        
        // need to figure out to get the button at that location and change its color
    }   
}
