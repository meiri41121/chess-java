public class Move {
    Pos source;
    Pos target;

    Move(Pos s, Pos t){
        source=new Pos(s.posX,s.posY);target=new Pos(t.posX,t.posY);
    }
    Pos getSource(){return source;}
    Pos getTarget(){return target;}
}
