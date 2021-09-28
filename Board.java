import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Board implements IBoard, Serializable {
    private String boardName;
    private ShipState[][] navires;
    private Boolean[][] frappes;
    public Board(String name,int taille)
    {
        boardName=name;
        navires=new ShipState[taille][taille];
        frappes=new Boolean[taille][taille];
    }
    public Board(String name)
    {
        boardName=name;
        navires=new ShipState[10][10];
        frappes=new Boolean[10][10];
    }
    public void setBoardName(String name){boardName=name;}

    public String getBoardName() {
        return boardName;
    }
    public int getSize(){return navires.length;}
    public void putShip(AbstractShip ship, int x, int y) {
        if(x<0 || y<0 || x>=navires.length || y>=navires.length){
            System.out.println(ColorUtil.colorize("Invalid position -putship", ColorUtil.Color.RED));
            return ;
        }
        AbstractShip.Orientation orient = ship.orientation;

        int i=y,t= ship.getLength(),j=x,xDirection=0,yDirection=0 ;
        String msg;

        switch (orient) {
            case SOUTH:
            case NORTH:
                xDirection = orient == AbstractShip.Orientation.SOUTH ? 1 : -1;
                break;
            case EAST:
            default:
                yDirection = orient == AbstractShip.Orientation.EAST ? 1 : -1;
                break;
        }
        while(i>=0 && i< navires.length && j>=0 && j< navires.length && Math.abs(y-i)<t && Math.abs(x-j)<t) {
            if(navires[i][j]!=null){
                msg = "Can't place ship "+ship.getName()+" with orientation "
                        +orient+" in position "+(char)('A'+x)+(y + 1);
                System.out.println(ColorUtil.colorize(msg, ColorUtil.Color.RED));
                return;
            }
            i+=yDirection;
            j+=xDirection;
        }
        if((Math.abs(y-i)!=t && yDirection!=0) || (Math.abs(x-j)!=t && xDirection!=0)){
            msg = "Can't place ship "+ship.getName()+" with orientation "
                    +orient+" in position "+(char)('A'+x)+(y + 1);
            System.out.println(ColorUtil.colorize(msg, ColorUtil.Color.RED));

            return ;
        }

        i=y;
        j=x;
        while(Math.abs(y-i)<t && Math.abs(x-j)<t){
            navires[i][j] = new ShipState(ship);
            i+=yDirection;
            j+=xDirection;
        }

    }
    public boolean hasShip(int x, int y){
        if(x<0 || y<0 || x==navires.length || y>=navires.length) {
            System.out.println("Invalid position -hasShip");
            return false;
        }
        return navires[y][x]!=null;
    }
    public void setHit(boolean hit, int x, int y) {
        if(x<0 || y<0 || x>=navires.length || y>=navires.length) {
            System.out.println("Invalid position -setHit");
            return ;
        }
        frappes[y][x]=hit;
    }
    public Hit sendHit(int x,int y){
        if(x<0 || x>=navires.length || y<0 || y> navires.length)return null;
        if(!hasShip(x,y)|| navires[y][x]==null){
            return Hit.MISS;
        }
        if(navires[y][x].isStruck() || navires[y][x].isSunk()){
            return Hit.STRIKE;
        }
        navires[y][x].addStrike();
        if(navires[y][x].getShip().isSunk()){
            String s=navires[y][x].getShip().getName();
            System.out.println(s+" coule");
            s=s.toUpperCase();
            switch(s){
                case "BattleShip":
                    return Hit.BATTLESHIP;
                case "CARRIER":
                    return Hit.CARRIER;
                case "DESTROYER":
                    return Hit.DESTROYER;
                default:
                    return Hit.SUBMARINE;
            }
        }else return Hit.STRIKE;
    }
    public Boolean getHit(int x, int y){
        if(x<0 || y<0 || x>=navires.length || y>=navires.length) {
            System.out.println("Invalid position -gethit");
            return false;
        }
        return frappes[y][x];
    }
    public void print()
    {
        List<String> linesOfShips= new ArrayList<String>(navires.length+2);
        List<String> linesOfHits = new ArrayList<String>(navires.length+2);
        linesOfShips.add(0,"Navires :");
        linesOfHits.add(0,"Frappes :");
        int n= navires.length;
        linesOfHits.add(1,"  ");
        linesOfShips.add(1,"  ");
        n/=10;
        while(n>0)
        {
            n/=10;
            linesOfHits.set(1,linesOfHits.get(1)+" ");
            linesOfShips.set(1,linesOfShips.get(1)+" ");
        }
        int c = (int)'A';
        for(int i=0;i< navires.length;i++) {
            String s=Character.toString(c+(i%26))+" ";
            linesOfShips.set(1,linesOfShips.get(1)+s);
            linesOfHits.set(1,linesOfHits.get(1)+s);
        }
        int maxLength=Integer.toString(navires.length+2).length()+1;
        int longestString=Integer.MIN_VALUE;
        List<Integer> unvisbStrings = new ArrayList<Integer>();
        unvisbStrings.add(0);
        unvisbStrings.add(0);
        for(int i=0;i<navires.length;i++){
            String currLine=Integer.toString(i+1);
            unvisbStrings.add(0);
            int currLength = currLine.length();
            for(int j=0;j<maxLength-currLength;j++){
                currLine+=" ";
            }
            linesOfShips.add(i+2,currLine);
            linesOfHits.add(i+2,currLine);
            for(int j=0;j<navires.length;j++) {
                if(navires[i][j]==null)linesOfShips.set(i+2,linesOfShips.get(i+2)+". ");
                else if(!navires[i][j].isStruck())
                    linesOfShips.set(i+2,linesOfShips.get(i+2)+navires[i][j]+" ");
                else {
                    linesOfShips.set(i+2,linesOfShips.get(i+2)+ColorUtil.colorize(navires[i][j].toString(), ColorUtil.Color.RED)+" ");
                    unvisbStrings.set(i+2,unvisbStrings.get(i+2)+9);
                }

                if(frappes[i][j]==null)linesOfHits.set(i+2,linesOfHits.get(i+2)+". ");
                else if(!frappes[i][j])linesOfHits.set(i+2,linesOfHits.get(i+2)+"X ");
                else {
                    linesOfHits.set(i+2,linesOfHits.get(i+2)+ColorUtil.colorize("X ", ColorUtil.Color.RED));
                }
            }
            longestString=Math.max(longestString,linesOfShips.get(i+2).length()-unvisbStrings.get(i+2));
        }
        for(int i=0;i<navires.length+2;i++) {
            System.out.print(linesOfShips.get(i));
            int currLength=linesOfShips.get(i).length();
            for(int j=0;j<longestString-currLength-unvisbStrings.get(i);j++)System.out.print(" ");
            System.out.println("  "+linesOfHits.get(i));
        }
    }
}
