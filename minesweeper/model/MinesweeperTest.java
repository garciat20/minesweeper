package minesweeper.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class MinesweeperTest {
    
    // @Test
    // public void setAdjacentMineSquaresTest(){// this did not work cause the locations I made have different memory location

    //     //setup
    //     List<Location> list = new ArrayList<>();
    //     list.add(new Location(0, 0));
    //     list.add(new Location(0, 1));
    //     list.add(new Location(0, 2));
    //     list.add(new Location(0, 3));
    //     list.add(new Location(1, 0));
    //     list.add(new Location(1, 2));
    //     list.add(new Location(1, 3));
    //     list.add(new Location(2, 1));
    //     list.add(new Location(2, 2));
    //     list.add(new Location(2, 3));
    //     list.add(new Location(3, 0));
    //     list.add(new Location(3, 1));
    //     list.add(new Location(3, 2));
    //     list.add(new Location(3, 3));

    //     List<Location> expected = list;

        
    //     //invoke
    //     Minesweeper minesweeper = new Minesweeper(4, 4, 2, GameState.NOT_STARTED);
    //     List<Location> actual = minesweeper.getPossibleSelections();

    //     //analyze
    //     assertEquals(expected, actual);
        
    // }

    @Test
    public void getLocationFromMapTest(){

            //setup
            Location expected = new Location(0, 0);
    
            
            //invoke
            Minesweeper minesweeper = new Minesweeper(4, 4, 2);
            Location actual = minesweeper.getLocationFromLMap(0, 0);
    
            //analyze
            assertEquals(expected, actual);
    }

    @Test
    public void getMoveCountTest(){

            //setup
            int expected = 0;
    
            
            //invoke
            Minesweeper minesweeper = new Minesweeper(4, 4, 2);
            int actual = minesweeper.getMoveCount();
    
            //analyze
            assertEquals(expected, actual);
    }

    @Test
    public void getGameStateTest(){

            //setup
            GameState expected = GameState.NOT_STARTED;
    
            
            //invoke
            Minesweeper minesweeper = new Minesweeper(4, 4, 2);
            GameState actual = minesweeper.getGameState();
    
            //analyze
            assertEquals(expected, actual);
    }

    // @Test
    // public void toStringTest(){

    //         //setup
    //         String expected = "Moves: 0\n1 1 1 0 \n2 M 1 0 \nM 2 1 0 \n1 1 0 0\n";
    
            
    //         //invoke
    //         Minesweeper minesweeper = new Minesweeper(4, 4, 2, GameState.NOT_STARTED);
    //         String actual = minesweeper.toString();
    
    //         //analyze
    //         assertEquals(expected, actual);
    // }
}
