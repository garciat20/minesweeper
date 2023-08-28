package minesweeper.model;

public class Location{
    private int row, col;

    public Location(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }
    public int getCol() {
        return col;
    }
    public Location getLocation(){
        return this;
    }
    
    @Override
    public String toString() {
        return "["+row+", "+col+"]";
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof Location){
            Location o = (Location)(other);
            return this.getRow() == o.getRow() && this.getCol() == o.getCol();
        }
        return false;
    }
}