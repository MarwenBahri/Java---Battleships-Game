import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

    /* ***
     * Constante
     */
    public static final File SAVE_FILE = new File("savegame.dat");

    /* ***
     * Attributs
     */
    private Player player1;
    private Player player2;
    private Scanner sin;
    public boolean modePvP;
    /* ***
     * Constructeurs
     */
    public Game() {}

    public Game init() {
        if (!loadSave()) {
            // init attributes
            System.out.println("appuyez sur 1 pour mode PlayerVsComputer");
            System.out.println("appuyez sur 2 pour mode PlayerVsPlayer");
            sin = new Scanner(System.in);
            String mode = sin.nextLine();
            while(!mode.equals("1") && !mode.equals("2")){
                System.out.println("appuyez sur 1 ou sur 2");
                mode = sin.nextLine();
            }
            modePvP = mode.equals("2");

            String playerName;
            String player2Name;

            if(!modePvP){
                System.out.println("entre ton nom:");
                playerName = sin.nextLine();
                player2Name="AIBoard";
            }else{
                System.out.println("entre le nom du joueur 1:");
                playerName = sin.nextLine();
                System.out.println("entre le nom du joueur 2:");
                player2Name = sin.nextLine();
            }
            // TODO use a scanner to read player name

            // TODO init boards
            Board b1=new Board(playerName), b2=new Board(player2Name);
            List<AbstractShip> player1Ships = Arrays.asList(new AbstractShip[]{new Destroyer(), new Submarine(), new Submarine(), new BattleShip(), new Carrier()});
            List<AbstractShip> player2Ships = Arrays.asList(new AbstractShip[]{new Destroyer(), new Submarine(), new Submarine(), new BattleShip(), new Carrier()});

            // TODO init this.player1 & this.player2
            this.player1 = new Player(b1,b2,player1Ships);
            this.player2 = modePvP ? new Player(b2,b1,player2Ships) : new AIPlayer(b2,b1,player2Ships);

            b1.print();
            // place player ships
            this.player1.putShips();
            this.player2.putShips();
        }
        return this;
    }

    /* ***
     * Méthodes
     */
    public void run() {
        int[] coords = new int[2];
        Board b1 = player1.board;
        Hit hit;

        // main loop
        boolean done,strike;
        do {
            System.out.println(player1.board.getBoardName());
            hit = this.player1.sendHit(coords); // TODO player1 send a hit
            strike = (hit != Hit.MISS && hit!=null); // TODO set this hit on his board (b1)
            this.player1.board.setHit(strike,coords[0],coords[1]);

            done = updateScore();
            b1.print();
            System.out.println(makeHitMessage(false /* outgoing hit */, coords, hit));

            save();

            if (!done && !strike) {
                do {
                    System.out.println(this.player2.board.getBoardName());
                    hit = this.player2.sendHit(coords); // TODO player2 send a hit.

                    strike = (hit != Hit.MISS && hit !=null);
                    this.player2.board.setHit(strike,coords[0],coords[1]);

                    if (modePvP)player2.board.print();
                    else if (strike) {
                        player1.board.print();
                    }

                    System.out.println(makeHitMessage(true /* incoming hit */, coords, hit));
                    done = updateScore();

                    if (!done) {
                        save();
                    }
                    sleep(500);
                } while(strike && !done);
            }
            sleep(500);
        } while (!done);

        SAVE_FILE.delete();
        System.out.println(String.format("joueur %d gagne", player1.lose ? 2 : 1));
        sin.close();
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void save() {
        try {
            // TODO bonus 2 : uncomment
            if (!SAVE_FILE.exists()) {
                  SAVE_FILE.getAbsoluteFile().getParentFile().mkdirs();
            }

            // TODO bonus 2 : serialize players
            FileOutputStream file = new FileOutputStream(SAVE_FILE.getName());
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(this.player1);
            out.writeObject(this.player2);

            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadSave() {
        if (SAVE_FILE.exists()) {
            System.out.println("vous voulez charger la sauvegarde ? o/n");
            Scanner sin = new Scanner(System.in);
            String c = sin.nextLine();
            while(!c.equals("o") && !c.equals("n")){
                System.out.println("o/n ?");
                c = sin.nextLine();
            }
            if(c.equals("n"))return false;

            try {
                // TODO bonus 2 : deserialize players
                FileInputStream file = new FileInputStream(SAVE_FILE.getName());
                ObjectInputStream out = new ObjectInputStream(file);

                this.player1 = (Player) out.readObject();
                this.player2 = (Player) out.readObject();

                out.close();
                file.close();

                return true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean updateScore() {
        for (Player player : new Player[]{player1, player2}) {
            int destroyed = 0;
            for (AbstractShip ship : player.getShips()) {
                if (ship.isSunk()) {
                    destroyed++;
                }
            }

            player.destroyedCount = destroyed;
            player.lose = destroyed == player.getShips().length;
            if (player.lose) {
                return true;
            }
        }
        return false;
    }

    private String makeHitMessage(boolean incoming, int[] coords, Hit hit) {
        String msg;
        ColorUtil.Color color = ColorUtil.Color.RESET;
        switch (hit) {
            case MISS:
                msg = hit.toString();
                break;
            case STRIKE:
                msg = hit.toString();
                color = ColorUtil.Color.RED;
                break;
            default:
                msg = hit.toString() + " coule";
                color = ColorUtil.Color.RED;
        }
        msg = String.format("%s Frappe en %c%d : %s", incoming ? "<=" : "=>",
                ((char) ('A' + coords[0])),
                (coords[1] + 1), msg);
        return ColorUtil.colorize(msg, color);
    }

    private static List<AbstractShip> createDefaultShips() {
        return Arrays.asList(new AbstractShip[]{new Destroyer(), new Submarine(), new Submarine(), new BattleShip(), new Carrier()});
    }

    public static void main(String args[]) {
        new Game().init().run();
    }
}
