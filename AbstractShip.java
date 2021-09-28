import java.io.Serializable;

public abstract class AbstractShip implements Serializable {
    public enum Orientation{NORTH,SOUTH,EAST,WEST}
    private char label;
    private String name;
    private int taille;
    private int strikeCount;
    Orientation orientation;
    public char getLabel(){return label;}
    public String getName(){return name;}
    public int getLength(){return taille;}
    public Orientation getOrientation(){return orientation;}
    public void setOrientation(Orientation o){orientation=o;}
    public AbstractShip(String nom,char l,int t,Orientation o){
        name=nom;
        taille=t;
        label=l;
        orientation=o;
    }
    public void addStrike(){strikeCount++;}
    public boolean isSunk(){return strikeCount>=taille;}
}
