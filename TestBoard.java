//package ensta;
public class TestBoard {
    public static void main(String[] args)
    {
        Board b = new Board("test",10);
        b.print();
        Destroyer destroyer = new Destroyer();
        Destroyer d2 = new Destroyer();
        BattleShip bb = new BattleShip(AbstractShip.Orientation.NORTH);
        b.putShip(destroyer,2 , 2);
        b.putShip(d2,2 , 2);
        b.putShip(d2,4 , 5);
        b.putShip(bb,10 , 10);
        b.print();
        System.out.println(destroyer.isSunk());
        System.out.println(b.sendHit(2 ,2));
        System.out.println(destroyer.isSunk());
        System.out.println(b.sendHit(2 ,3));
        System.out.println(b.getHit(2,2));
        System.out.println(b.getHit(2,3));
        System.out.println("sunk: "+ destroyer.isSunk());
    }
}
