package minesweeper.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import backtracker.Backtracker;
import backtracker.Configuration;

/**
 * Minesweeper part 1
 * 
 * @author Dinson Chen, Madisyn DeLozier, Thomas Gracia
 * 
 */
public class Minesweeper{
    public static final int MINE = 9 ; // check 2nd for loop as to why its 9
    public static final char COVERED = '-' ;
    public int[][] board; // changed board to int to make it easier to implement squares w/ adjacent Mines
    public int moves, rows, cols;
    public GameState gameState; 
    public boolean[][] isCovered; // update when user makes a selection?   //need to be deepcopyed
    public int mineCount;
    public String[] mineTracker;
    public Map<String, Location> locationMap; // added field to use for getters below, update based off user interaction // making a dictionary because allows easier for
                                      // accessor and using in MineSweeperCLI
    public char[][] mineSweeperBoardFinal;  // final version of the board which will be created off of all the board is calculated with ints
    
    private MinesweeperObserver observer;

    public Minesweeper(int rows, int cols, int mineCount){
        this.board = new int[rows][cols];
        this.mineSweeperBoardFinal = new char[rows][cols];
        this.moves = 0;
        this.gameState = GameState.NOT_STARTED ;
        this.mineCount = mineCount;
        this.isCovered = new boolean[rows][cols];
        this.rows = rows;
        this.cols = cols;
        this.locationMap = new HashMap<>();
        this.mineTracker = new String[mineCount];
    
        for(int r =0; r< rows; r++){
            for(int c=0; c<cols; c++){
                board[r][c] = 0; // by default no mines are adjacent 
                // to a square, its updated below 
                isCovered[r][c] = true;
            }
        }

        Random random = new Random();
        int count =0;
        // random.setSeed(1);//add see to not have different mines everytime
        for(int i = 0; i< mineCount; i++){
            int rowRand = random.nextInt(rows);
            int colRand = random.nextInt(cols);
            /**
             * Making mine's == 9 since a square
             * can't be surrounded by more than 8
             * other bombs (worst scenario)
             */
            if(!(board[rowRand][colRand]==9)){
                board[rowRand][colRand] = 9;
                mineTracker[count]= Integer.toString(rowRand) +","+ Integer.toString(colRand);
                count += 1;
                // && rowRand-1 >=0 && rowRand +1 <= row && colRand-1>=0 && colRand ?
            }
            else{
                i--;
            }
        }

        /**
         * For loop below creates board with 
         * squares containing numbers adjacent
         * to the mines.
         */
       
        for(int j = 0; j < this.rows; j++){
            for(int k = 0; k < this.cols; k++){
                if(board[j][k] == 9){
                    /// find a way to find all the values that have M and then make all
                    // board spots surrounding them have an increase 0-->1 1---> 2 etc
                    // j == row && k == col 
                    // helper creates board spots with values 0-->1-->2 etc
                    // by default board spot contains 0
                    setAdjacentMineSquares(j, k);
                }
            }
        }

        /** 
         * Initializing the list of locations (maybe delete)
         * updates the char array for final game board; implements 'M' for mines
         */
        for(int j = 0; j < this.rows; j++){
            for(int k = 0; k < this.cols; k++){
                Location location = new Location(j, k) ;
                String keyHolder = Integer.toString(j) + Integer.toString(k);   // makes key
                locationMap.put(keyHolder, location) ;
                int placeHolder = board[j][k];
                if(placeHolder == 9){
                    mineSweeperBoardFinal[j][k] = 'M';
                }
                else{
                    mineSweeperBoardFinal[j][k] = (char) placeHolder;
                }
            }
        }
    }

    public Minesweeper(Minesweeper minesweeper){
        // this.board = minesweeper.board;
        this.moves = minesweeper.moves;
        this.gameState = minesweeper.gameState ;
        this.mineCount = minesweeper.mineCount;
        this.rows = minesweeper.rows;
        this.cols = minesweeper.cols;
        this.observer = null;

        int[][] copyBoard = new int[rows][cols];
        for(int row = 0; row<rows; row++){
            for(int col = 0; col<cols; col++){
                copyBoard[row][col] = minesweeper.board[row][col];
            }
        }

        this.board = copyBoard;

        boolean[][] copyisCovered = new boolean[rows][cols];
        for(int row = 0; row<rows; row++){
            for(int col = 0; col<cols; col++){
                copyisCovered[row][col] = minesweeper.isCovered[row][col];
            }
        }

        this.isCovered = copyisCovered;
       // this.board = Arrays.copyOf(this.board, this.board.length);
       
        // this.isCovered = minesweeper.isCovered; //not allowed needed to be deep copyed
        // boolean[][] deepCopyCovered = Arrays.copyOf(this.isCovered, this.isCovered.length); //this might be another way of deep coping 2d array

        // Map<String, Location> copyLocationMap = this.locationMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e ->List.copyOf(e.getValue()))); //scuffed way of deep coping 
        Map<String, Location> copyLocationMap = new HashMap<>();
        for(String elm: minesweeper.locationMap.keySet()){
            copyLocationMap.put(elm,minesweeper.locationMap.get(elm));
        }

        this.locationMap =copyLocationMap;
        
        // this.locationMap = minesweeper.locationMap; //also need to deep copy

    }

    /**
     * helper function to fill in squares with number(s) relating to adjacent mines
     */
    public void setAdjacentMineSquares(int row, int col){

         /** 1st condition: takes care of left hand side of Mine */
         if (col - 1 >= 0){  // 1 space to the left simply (no change in row)

            if(board[row][col-1]!=9){
               board[row][col-1] ++ ; 
            }
            
            
            // if's below takes care of diagonals left hand side
            if (col-1 >= 0 && row - 1 >= 0){ // 1 space to left 
            // and 1 space up due to starting at 0,0 top left
            // incrementing the row (j) causes us to go down
                if(board[row-1][col-1]!=9){
                    board[row-1][col-1] ++ ; 
                }
            }
            if (col-1 >= 0 && row + 1 < this.rows){ // 1 space to left 
                // and 1 space down
                if(board[row+1][col-1]!=9){
                    board[row+1][col-1] ++ ; 
                }
            }
        }
        /** 2nd condition: takes care of right hand side of Mine */
        if (col + 1 < this.cols){ 
            if(board[row][col+1]!=9){
                board[row][col+1] ++ ; 
            }
            // 1 space to right (no change in row)
            
            // if's below takes care of diagonals right hand side
            if (col+1 < this.cols && row + 1 < this.rows){ // 1 space to right
                // and 1 space down due to starting at 0,0 top left
                // hence incrememnting row (j) causes us to go down
                if(board[row+1][col+1]!=9){
                    board[row+1][col+1] ++ ; 
                }
            }
            if (col+1 < this.cols && row - 1 >= 0){ // 1 space to right
                // and 1 space up 
                if(board[row-1][col+1]!=9){
                    board[row-1][col+1] ++ ; 
                }
            }
                
        }
        /** 3rd condition, takes care of verticals */
        if (row - 1 >= 0){ // takes care of going up 1 (no change in col)
            if(board[row-1][col]!=9){
                board[row-1][col] ++ ; 
            }
        }
        if (row + 1 < this.rows){ // takes care of going down 1 (no change in col)
            if(board[row+1][col]!=9){
                board[row+1][col] ++ ; 
            }
        }
    }
    
    public void makeSelection(Location location)throws MinesweeperException{
        /// basis
        
        // 1). Valid if in board    //because implementing location possibly checked in CLI
        // 2. Location ([row][col] == '-')
        // 3. update field (LinkedList) so it can also
        // update getPossibleSelections at the same time

        // update isCovered
        int row = location.getRow() ;
        int col = location.getCol() ;
        // checks that board is covered to make valid move
        // valid move as long as the board is NOT covered 
        this.gameState = GameState.IN_PROGRESS ;  
        if (this.isCovered[row][col]){
            this.isCovered[row][col] = false; // whenever we update this.board we also have to update other boolean board
            // if the location selected is a Mine, game is lost, print board with answers
            if(this.board[row][col] > 8){   
                this.gameState = GameState.LOST ; // user lost
            }
            
        }
        notifyObserver(location); // notifies observer
        gameStatus() ; // checks to see if user has won game
        moves++;
    }

    /**
     * Helper checks status of Game, whether or not
     * the user has won/ if game is still in progress
     */
    public void gameStatus(){
        int checkSquareStatus = 0 ; // checks to see if board was properly filled
        int boardFilled = (this.rows * this.cols) - this.mineCount ; // amount of squares needing to be filled to win 
        for (int row = 0; row < this.rows ; row++){
            for (int col = 0; col < this.cols; col++){
                // checking that board isnt covered, and excluding mines on board
                if (!this.isCovered[row][col] && this.board[row][col] <= 8){    
                    checkSquareStatus ++ ;
                }
                if (!this.isCovered[row][col] && this.board[row][col] > 8){  
                    this.gameState = GameState.LOST ;
                    break ;
                }
            }
        }
        if(checkSquareStatus == boardFilled){
            this.gameState = GameState.WON ;
        }
        //this.gameState = GameState.IN_PROGRESS ;
    }
    
    /*
    * takes row and column and returns the location from the map
    */
    public Location getLocationFromLMap(int row, int col){
        Location location = locationMap.get(Integer.toString(row)+Integer.toString(col));
        return location;
    }

    public int getMoveCount(){
        return moves;
    }
    public GameState getGameState(){
        // placeholder for now
        return this.gameState;
    }
    public int getMineCount(){
        return mineCount;
    }
    public String[] getMineTracker(){
        return mineTracker;
    }

    public List<Location> getPossibleSelections(){//changed Collection to List
        // 1. If Location on board is covered.
        // 2. if location isnt a Mine
    
        List<Location> possbleSelections = new ArrayList<>();

        for (String keys : locationMap.keySet()){
            // checking if location isnt a mine/ is covered
            Location location = locationMap.get(keys);
            int row = location.getRow() ;
            int col = location.getCol() ;
            if (this.board[row][col] <= 8 && isCovered[row][col]){
                possbleSelections.add(location) ; // return location that 
                // isnt a Mine/ is covered
            }
        }
 
        return possbleSelections;
    }
    @Override
    public String toString() {  // return current board
        String str ="Moves: " + getMoveCount() + "\n"; // added a moves counter

        /**
         * Current board in this toString showcases the answers
         * Replace this current board to showcase to user the board WITHOUT the answers
         * MAYBE: use GameState to select certain boards to print.
         * Ex: if GameState == WON/LOST ==> Showcase board with all answers?
         * If GameState == IN_PROGRESS ==> Showcase board showing NO answers
         * and markers on whether or not a location is covered or not 
         */
        char[][] emptyBoard = new char[this.rows][this.cols];
        if (this.gameState == GameState.NOT_STARTED){
            // print covered board, and if gameState is IN_PROGRESS
            // that board will change in respect to user interaction
            for(int r =0; r< this.rows; r++){
                for(int c=0; c< this.cols; c++){
                   // board[r][c] = COVERED; // greater than 8 since
                        // a square can't be surrounded by more 
                        // than 8 mines
                        emptyBoard[r][c] = COVERED ;
                        str += emptyBoard[r][c] + " ";
                    
                   // currentBoard[r][c] = isCovered[r][c] ? COVERED: (char)board[r][c];
                    //else{
                   //     str += board[r][c] + " ";
                    } 
                    str+= "\n";
                }
                
        }


        if (this.gameState == GameState.IN_PROGRESS){
            // print board with covered mines/ updates based off of user interaction
            // Only print specific locations if its not covered
            for(int r =0; r< rows; r++){
                for(int c=0; c<cols; c++){
                    if (isCovered[r][c]){ // greater than 8 since
                        // a square can't be surrounded by more 
                        // than 8 mines
                        str += COVERED + " "; // printing square is covered
                    }
                   // currentBoard[r][c] = isCovered[r][c] ? COVERED: (char)board[r][c];
                    else if (!isCovered[r][c]){
                        if (board[r][c] <= 8){
                            str += board[r][c] + " "; // printing actual value since its not covered
                        }
                        else{
                            str += "M" + " ";
                            // this.gameState = GameState.LOST ;
                        }
                    } 
                }
                str+= "\n";
        }
    }



        // If game is WON or LOST, then display board with all the answers
        // Maybe add a special message if the User wins/loses?
        if (this.gameState == GameState.LOST || this.gameState == GameState.WON){
            for(int r =0; r< rows; r++){
                for(int c=0; c<cols; c++){
                    
                    if (board[r][c] > 8){ // greater than 8 since
                        // a square can't be surrounded by more 
                        // than 8 mines
                       // mineSweeperBoardFinal[r][c] = 'M' + ' ';
                        str += "M" + " "; 
                    }
                // currentBoard[r][c] = isCovered[r][c] ? COVERED: (char)board[r][c];
                    else{
                        str += board[r][c] + " ";
                    } 
                }
                str+= "\n";
            }
        }   
    
        return str;
        
    }
    public void finalPrintOut(){
        for(int i=0; i<rows;i++){
            for(int j=0; j<cols;j++){
                char temp = mineSweeperBoardFinal[i][j];
                System.out.print(temp);
            }
            System.out.println();
        }
    }

    // register(observer: MinesweeperObserver)
    public void register(MinesweeperObserver observer){
        this.observer = observer;
    }

    // notifyObserver(location: Location)
    private void notifyObserver(Location location){
        if(observer != null){
            observer.cellUpdated(location);
        }
    }
    // getSymbol(location: Location): char

    public char getSymbol(Location locations){
        int row = locations.getRow();
        int col = locations.getCol();
        if (board[row][col] > 8){
            return 'M';
        }
        else if (board[row][col] > 0){
            int convertToChar = board[row][col];
            return (char)convertToChar; 
        }
        return ' '; 
    } 

    public boolean isCovered(Location locations){
        int row = locations.getRow();
        int col = locations.getCol(); 
        if (isCovered[row][col]){
            return true; 
        }
        return false;

    }

    public static void main(String[] args) {
        // simply testing board
        // Minesweeper testingBoard = new Minesweeper(6, 6, 4);
        // System.out.println(testingBoard);
       
       
        Backtracker backTracker = new Backtracker(true) ;   
        Minesweeper minesweeper = new Minesweeper(6, 6, 4);
        MinesweeperConfiguration config = new MinesweeperConfiguration(minesweeper);
        

        Configuration solution = backTracker.solve(config) ;
        System.out.println(solution);
        System.out.println(((MinesweeperConfiguration) solution).getPath());

    }
}
