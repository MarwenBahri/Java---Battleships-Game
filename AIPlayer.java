import java.util.List;
//testtesttest
public class AIPlayer extends Player {
    /* **
     * Attribut
     */
    private BattleShipsAI ai;

    /* **
     * Constructeur
     */
    public AIPlayer(Board ownBoard, Board opponentBoard, List<AbstractShip> ships) {
        super(ownBoard, opponentBoard, ships);
        ai = new BattleShipsAI(ownBoard, opponentBoard);
    }

    // TODO AIPlayer must not inherit "keyboard behavior" from player. Call ai instead.
    @Override
    public Hit sendHit(int[] coords){
        Hit hit = ai.sendHit(coords);
        return hit;
    }
    @Override
    public void putShips() {
        ai.putShips(super.ships);
    }
}
