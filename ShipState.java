import java.io.Serializable;

public class ShipState implements Serializable {
    private AbstractShip ship;
    private boolean struck;
    public ShipState(){
        struck=false;
    }
    public ShipState(AbstractShip s){
        ship=s;
        struck=false;
    }
    public void addStrike(){
        if(struck)return ;
        struck=true;
        ship.addStrike();
    }
    public boolean isStruck(){
        return struck;
    }
    public String toString(){
        if(ship==null) return ".";
        else if(struck)return ""+ship.getLabel();

        else return ""+ship.getLabel();
    }
    public boolean isSunk(){
        return ship.isSunk();
    }
    public AbstractShip getShip() {
        return ship;
    }
}
