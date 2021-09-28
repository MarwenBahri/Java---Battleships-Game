import java.io.Serializable;
import java.util.List;

public class Player implements Serializable {
    /* **
     * Attributs
     */
    protected Board board;
    protected Board opponentBoard;
    protected int destroyedCount;
    protected AbstractShip[] ships;
    protected boolean lose;

    /* **
     * Constructeur
     */
    public Player(Board board, Board opponentBoard, List<AbstractShip> ships) {
        this.board = board;
        this.ships = ships.toArray(new AbstractShip[0]);
        this.opponentBoard = opponentBoard;
    }

    /* **
     * Méthodes
     */

    /**
     * Read keyboard input to get ships coordinates. Place ships on given coodrinates.
     */
    public void putShips() {
        boolean done = false;
        int i = 0;
        do {
            AbstractShip s = ships[i];
            String msg = String.format("placer %d : %s(%d)", i + 1, s.getName(), s.getLength());
            System.out.println(msg);
            InputHelper.ShipInput res = InputHelper.readShipInput();
            // TODO set ship orientation
            if(res.orientation.equals("n"))s.setOrientation(AbstractShip.Orientation.NORTH);
            else if(res.orientation.equals("s"))s.setOrientation(AbstractShip.Orientation.SOUTH);
            else if(res.orientation.equals("e"))s.setOrientation(AbstractShip.Orientation.EAST);
            else s.setOrientation(AbstractShip.Orientation.WEST);
            // TODO put ship at given position
            boolean oldState = board.hasShip(res.x,res.y);
            board.putShip(s,res.x,res.y);
            // TODO when ship placement successful
            if(board.hasShip(res.x,res.y) && !oldState) {
                ++i;
                done = i == 5;
            }
            board.print();
        } while (!done);
    }

    public Hit sendHit(int[] coords) {
        boolean done;
        Hit hit;

        do {
            System.out.println("ou frapper?");
            InputHelper.CoordInput hitInput = InputHelper.readCoordInput();
            // TODO call sendHit on this.opponentBoard
            hit = this.opponentBoard.sendHit(hitInput.x, hitInput.y);

            // TODO : Game expects sendHit to return BOTH hit result & hit coords.
            // return hit is obvious. But how to return coords at the same time ?
            coords[0]= hitInput.x;
            coords[1]= hitInput.y;
            done=(hit!=null);
        } while (!done);

        return hit;
    }

    public AbstractShip[] getShips() {
        return ships;
    }

    public void setShips(AbstractShip[] ships) {
        this.ships = ships;
    }
}
