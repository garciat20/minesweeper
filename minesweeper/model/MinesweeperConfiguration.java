package minesweeper.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import backtracker.Configuration;

public class MinesweeperConfiguration implements Configuration{

    private Minesweeper minesweeper;
    private List<Location> path = new ArrayList<>(); 

    public MinesweeperConfiguration(Minesweeper minesweeper){
        this.minesweeper = minesweeper;

    }

    public MinesweeperConfiguration(Minesweeper minesweeper, List<Location> path){
        this.minesweeper = minesweeper;

        for(Location elm: path){
            this.path.add(elm);
        }
        
    }
    public List<Location> getPath(){
        return path;
    }

    @Override
    public Collection<Configuration> getSuccessors(){
        // TODO Auto-generated method stub
        //make two for loops for the rows and cols to make sure it goes through all the possible solutions
        //check if location is not equal to 9(mine) then we will make a deep copy of the board
        //Configuration successor = new minesweeper(this);
        //add the successor //successors.add(successor)
        //return successor
        List<Configuration> successors = new LinkedList<>();

      
        for(int i=0; i<minesweeper.rows; i++){
            for(int j=0; j<minesweeper.cols; j++){

                // not a bomb and is covered
                if(!(minesweeper.board[i][j]== 9) && minesweeper.isCovered[i][j]){
                    // make deepy copy whenever a move is made
                    Minesweeper deepCopy = new Minesweeper(minesweeper); 

                    Location location = new Location(i, j);

                    try {
                        deepCopy.makeSelection(location);
                        path.add(location);
                    } catch (MinesweeperException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    Configuration s = new MinesweeperConfiguration(deepCopy,path);
                    successors.add(s);
                }
            }
        }
        
        // Minesweeper deepCopy = new Minesweeper(this);

        return successors;
    }
    @Override
    public boolean isValid() {
        //if the location is not a mine
        for(String key : minesweeper.locationMap.keySet()){
            Location temp = minesweeper.locationMap.get(key);
            return (!(minesweeper.board[temp.getRow()][temp.getCol()] == 9)) && (minesweeper.isCovered[temp.getRow()][temp.getCol()]==false);
            //return when the location is covered and is a mine(9)
        }
        return false;
    }
    @Override
    public boolean isGoal() {
        // TODO Auto-generated method stub
        // return minesweeper.rows * minesweeper.cols - minesweeper.mineCount == minesweeper.isCovered.length;
        return minesweeper.getGameState() == GameState.WON;
    }

    @Override
    public String toString() {
        return minesweeper.toString();
    }
}
