public class Submarine extends AbstractShip {
    public Submarine(AbstractShip.Orientation orientation)
    {
        super("Submarine",'S', 3, orientation);
    }
    public Submarine() {
        super("Submarine",'S', 3, Orientation.EAST);
    }
}
