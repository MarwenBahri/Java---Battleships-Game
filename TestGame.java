

public class TestGame {
    public static void main(String[] args){
        /*Board firsBoard = new Board("testGame",10);
        firsBoard.print();
        List<AbstractShip> shipList = Arrays.asList(new AbstractShip[]{new Destroyer(), new Submarine(), new Submarine(), new BattleShip(), new Carrier()});
        BattleShipsAI battleShipsAI = new BattleShipsAI(firsBoard,firsBoard);
        battleShipsAI.putShips(shipList.toArray(new AbstractShip[shipList.size()]));
        int destructedShips=0;
        int[] Coord=new int[2];
        Hit hit;
        while(destructedShips<5){
            hit=battleShipsAI.sendHit(Coord);
            System.out.println(makeHitMessage(true, Coord, hit));
            firsBoard.print();
            if(hit!=Hit.MISS && hit!=Hit.STRIKE)destructedShips++;
            sleep(1000);
        }*/
        Game game = new Game();
        game.init();
        game.run();
    }
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static String makeHitMessage(boolean incoming, int[] coords, Hit hit) {
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

}
