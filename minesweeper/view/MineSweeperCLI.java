package minesweeper.view;

import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import backtracker.Backtracker;
import backtracker.Configuration;
import minesweeper.model.Location;

import minesweeper.model.GameState;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperConfiguration;
import minesweeper.model.MinesweeperException;

public class MineSweeperCLI {
    private static Minesweeper minesweeper;
    // make private final rows and cols

    public MineSweeperCLI(){
        // This constructor is called game is reset, so make game state not NOT_STARTED?
        minesweeper = new Minesweeper(4, 4, 2);
        
    }

    public void printBoard(){
        System.out.println(minesweeper);
    }

    public static void pick(int row, int col){ 
        // String key = Integer.toString(row)+Integer.toString(col);
        try{
            minesweeper.model.Location location = minesweeper.getLocationFromLMap(row, col);    // need to make a check if the entry is invalid
            minesweeper.makeSelection(location);  // need to catch exception or try catch
        }
        catch(MinesweeperException e){
            System.out.println("invalid row and col input");
        }

    }
    public static void commandPrintOut(){
        System.out.println("Commands:\n"+
        "\thelp - this help message\n"+
        "\tpick <row> <col> - uncovers cell a <row> <col>\n"+
        "\thint - displays a safe selection\n"+
        "\treset - resets to a new game\n"+
        "\tsolve - solves the game" +
        "\tquit - quits the game\n\n");
    }

    public static void main(String[] args) {
        // user interaction
        // while loop until quit is typed
        Scanner scanner = new Scanner(System.in);
        MineSweeperCLI cli = new MineSweeperCLI();

        System.out.println("Mines: " +minesweeper.getMineCount());
        commandPrintOut();
        // System.out.println("\n");
        cli.printBoard();
        //System.out.println("\nMoves: " + minesweeper.getMoveCount() + "\n");
        System.out.print("Enter command: ");

        String input = scanner.nextLine();

        while(!(input.equals("quit"))){
            if(input.equals("quit")){
                break;
            }
            else if(input.charAt(0)=='p'){
                String[] tokens = input.strip().split(" ");
                pick(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                // game state never being updated -- need to fix
                if(minesweeper.getGameState()== GameState.LOST){
                    // when user has lost loop breaks
                    System.out.println("BOOM! Better luck next time!");
                   
                    cli.printBoard();
                    // cli.minesweeper.finalPrintOut();
                    break  ;
                }
                // game state never being updated -- need to fix
                else if (minesweeper.getGameState()== GameState.WON){
                    System.out.println("Congratz u won :3");
                    cli.printBoard(); // toString will change based on GameState so this is fine
                    // cli.minesweeper.finalPrintOut();
                    break ;
                }
                cli.printBoard();
                
            }
            else if(input.equals("help")){
                commandPrintOut();
            }
            else if(input.equals("hint")){
                List<Location> list = minesweeper.getPossibleSelections();
                Location location = list.remove(list.size()-1); //grab the last location from the arrayList
                System.out.println("try (" + location.getRow()+", " +location.getCol()+")");
                // need to figure out how to implement hint
                // for hints we need to use getPossibleSelections
            }
            else if(input.equals("reset")){
                cli = new MineSweeperCLI();
                // new board is printed out based on toString so this is fine
                cli.printBoard();//need to print out the new board
                // by default, when reseting board gameState is NOT_STARTED, this is fine
            }
            else if(input.equals("solve")){
                Backtracker backtracker = new Backtracker(false);
                MinesweeperConfiguration config = new MinesweeperConfiguration(minesweeper);
                Configuration solution = backtracker.solve(config);
                if(solution == null){
                    System.out.println("no sol?");
                }
                else{
                    for(Location e : config.getPath()){
                        int row = e.getRow();
                        int col = e.getCol();
                        pick(row, col);
                        // cli.printBoard();
                    }
                    cli.printBoard();
                }
                break;
            }
            else{
                System.out.println("invalid entry, try again");
            }
            // we dont need moves below since its already implemented/
            // updates properly in toString im p sure
            //System.out.println("\nMoves: "+ minesweeper.getMoveCount() + "\n");
            System.out.print("Enter command: ");
            input = scanner.nextLine();
        }
        
        scanner.close();
        if (minesweeper.getGameState() == GameState.LOST ||
        minesweeper.getGameState() == GameState.WON){
            minesweeper.toString();
        }
    }
}
