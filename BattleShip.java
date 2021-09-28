public class BattleShip extends AbstractShip{
    public BattleShip(AbstractShip.Orientation orientation) {
        super("Battleship",'B', 4, orientation);
    }
    public BattleShip() {
        super("Battleship",'B', 4, Orientation.EAST);
    }
}
