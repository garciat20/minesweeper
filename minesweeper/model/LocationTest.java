package minesweeper.model;



import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class LocationTest {

    @Test
    public void getRowTest(){

            //setup
            int expected = 4;
    
            
            //invoke
            Location location = new Location(4, 5); 
            int actual = location.getRow();
    
            //analyze
            assertEquals(expected, actual);
    }

    @Test
    public void getColTest(){

            //setup
            int expected = 5;
    
            
            //invoke
            Location location = new Location(4, 5); 
            int actual = location.getCol();
    
            //analyze
            assertEquals(expected, actual);
    }

    @Test
    public void getLocationTest(){

            //setup
            Location expected = new Location(4,5);
    
            
            //invoke
            Location location = new Location(4, 5); 
            Location actual = location.getLocation();
    
            //analyze
            assertEquals(expected, actual);
    }

    @Test
    public void equalsTrueTest(){

            //setup
            boolean expected = true;
    
            
            //invoke
            Location location = new Location(4, 5); 
            Location location2 = new Location(4, 5);
            boolean actual = location.equals(location2);
    
            //analyze
            assertEquals(expected, actual);
    }

    @Test
    public void equalsFalseTest(){

            //setup
            boolean expected = false;
    
            
            //invoke
            Location location = new Location(4, 7); 
            Location location2 = new Location(4, 5);
            boolean actual = location.equals(location2);
    
            //analyze
            assertEquals(expected, actual);
    }
    
    
}
