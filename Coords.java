public class Coords {
    private final int row;
    private final int col;
    public Coords(int row , int col){
        this.row = row;
        this.col = col; 
    }

    public int getCoords(int dim){
        if (dim == 0){
            return this.row;
        } else if (dim == 1){
            return this.col;
        }
        throw new IllegalArgumentException("dim must be 0 or 1");
    }
}