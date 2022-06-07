import java.util.List;
import java.util.ArrayList;
import java.util.*;


public class Game {
    int value;
    Board b = new Board();
    List<Piece> piecesW = new ArrayList<Piece>();
    List<Piece> piecesB = new ArrayList<Piece>();
    List<Piece> eaten = new ArrayList<Piece>();
    List<Movemant> moves = new ArrayList<Movemant>();
    List<Move> validMovesList = new ArrayList<Move>();
    boolean col=false;
    int index = 0;
    Scanner myObj = new Scanner(System.in);
    
    boolean getCol(){return col;}
    Game(){
        for(int i=0;i<8;i++)
            piecesW.add(new Piece(new Pos(i+1,2), Player.PAWN, false));
        piecesW.add(new Piece(new Pos(1,1), Player.ROOK, false));
        piecesW.add(new Piece(new Pos(8,1), Player.ROOK, false));
        piecesW.add(new Piece(new Pos(2,1), Player.KNIGHT, false));
        piecesW.add(new Piece(new Pos(7,1), Player.KNIGHT, false));
        piecesW.add(new Piece(new Pos(3,1), Player.BISHOF, false));
        piecesW.add(new Piece(new Pos(6,1), Player.BISHOF, false));
        piecesW.add(new Piece(new Pos(4,1), Player.QUEEN, false));
        piecesW.add(new Piece(new Pos(5,1), Player.KING, false));
        for(int i=0;i<8;i++)
            piecesB.add(new Piece(new Pos(i+1,7), Player.PAWN, true));
        piecesB.add(new Piece(new Pos(1,8), Player.ROOK, true));
        piecesB.add(new Piece(new Pos(8,8), Player.ROOK, true));
        piecesB.add(new Piece(new Pos(2,8), Player.KNIGHT, true));
        piecesB.add(new Piece(new Pos(7,8), Player.KNIGHT, true));
        piecesB.add(new Piece(new Pos(3,8), Player.BISHOF, true));
        piecesB.add(new Piece(new Pos(6,8), Player.BISHOF, true));
        piecesB.add(new Piece(new Pos(4,8), Player.QUEEN, true));
        piecesB.add(new Piece(new Pos(5,8), Player.KING, true));
        value=0;
        //allValidMoves();
        int n=1;
        while(n!=0)
        {
            System.out.println("enter 1 to play against friend or 2 to play against the computer, or 0 to exit");
            n = myObj.nextInt();
            if(n==1)
                playTwoPlayers();
            if(n==2){
                System.out.println("chosse difficulty: [1-5]");
                n = myObj.nextInt();
                while(n<1 || n>5) n = myObj.nextInt();;
                Random rand = new Random();
                int random_number = rand.nextInt(2);
                boolean pcColor=random_number!=0?true:false;
                System.out.println("you are " + (pcColor?"white":"black") + " Player");
                if(pcColor)
                    play();
                playAgainstPc(n+1);
            }
        }
        
    }

    void playAgainstPc(int n)
    {
        if(checkMat()) return;
        doBestMove(n);
        if(checkMat()) return;
        play(true);
        playAgainstPc(n);
    }

    Move doRandomMove()
    {
        Random rand = new Random();
        int random_number = rand.nextInt(validMovesList.size());
        Move m = validMovesList.get(random_number);
        move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY,true);
        return new Move(m.getSource(),m.getTarget());
    }
    
    void doBestMove(int n)
    {
        print();
        Move p = getBestMove(n);
        move(p.getTarget().posX,p.getTarget().posY,p.getSource().posX,p.getSource().posY,true);
        System.out.println("pc move:" + (char)(p.getSource().posX+'a'-1) + p.getSource().posY + " -> " + (char)(p.getTarget().posX+'a'-1) + p.getTarget().posY);
    }
    
//    Move getBestMove(int n=1)
//    {
//        index=0;
//        Move p=doRandomMove();
//        int v=BM(n-1),current=value;
//        undo(col?WHITE:BLACK);
//        if(n==0) return p;
//        allValidMoves();
//        List<Move> updateValidMovesList(validMovesList);
//        for(List<Move>::iterator it=updateValidMovesList.begin(); it!=updateValidMovesList.end();it++)
//        {
//            move(it->getTarget().posX, it->getTarget().posY, it->getSource().posX, it->getSource().posY,true);
//            int val = BM(n-1);
//            if((col && val<v) || (!col && val>v))
//            {
//                current=value;
//                v=val;
//                p=Move(it->getSource(),it->getTarget());
//            }
//            undo(col?WHITE:BLACK);
//        }
//        cout<<index<<endl;
//        return p;
//    }
//
//    int BM(int n)
//    {
//        index++;
//        if (n==0) return value;
//        int v=value;
//        col=col?WHITE:BLACK;
//        allValidMoves();
//        if(validMovesList.empty() && check()) v= col? 1000:-1000;
//        List<Move> updateValidMovesList(validMovesList);
//        for(List<Move>::iterator it=updateValidMovesList.begin(); it!=updateValidMovesList.end();it++)
//        {
//            move(it->getTarget().posX, it->getTarget().posY, it->getSource().posX, it->getSource().posY,true);
//            int val=BM(n-1);
//            if((col && val<v) || (!col && val>v))
//                v=val;
//            undo(col?WHITE:BLACK);
//        }
//        col=col?WHITE:BLACK;
//        return v;
//    }
    Move getBestMove(){return getBestMove(1);}
    Move getBestMove(int n)
    {
        index=0;
        Move p = doRandomMove();
        if(n==0) return p;
        int a=-5000,b=5000,v=BM(n-1,a,b),current=value;
        undo();
        allValidMoves();
        List<Move> updateValidMovesList = new ArrayList<Move>(validMovesList);
        for(Move m : updateValidMovesList)
        {
            move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY,true);//add move, col dont change
            int val = BM(n-1,a,b);
            undo();
            if((col && val<v) || (!col && val>v))
            {
                current = value;
                v = val;
                p = new Move(m.getSource(),m.getTarget());
            }
            a = (!col && v>a)? v:a;
            b = (col && v<b)? v:b;
            if(a>b) break;
        }
        System.out.println(index);;
        return p;
    }
    
    int BM(int n,int a,int b)
    {
        index++;
        if (n==0) return value;
        int v=value,i=0;
        //col=col?false:true;
        allValidMoves();
        if(validMovesList.isEmpty())
            v = check()? (col? 1000:-1000): 0;
        List<Move> updateValidMovesList = new ArrayList<Move>(validMovesList);
        for(Move m : updateValidMovesList)
        {
            move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY,true);//not change col
            int val=BM(n-1,a,b);
            undo();  // col return back the right
            if((col && val<v) || (!col && val>v))
                v=val;
            a = (!col && v>a)? v:a;
            b = (col && v<b)? v:b;
            if(a>b) break;
        }
        //col=col?false:true;
        return v;
    }
    
    void playTwoPlayers()
    {
        if(checkMat()) return;
        play();
        playTwoPlayers();
    }

    void play(){play(false);}
    void play(boolean pc){
        updateCol();
        print();
        String mov, turn=col?"BLACK":"WHITE",chec=check()?" [check!!]":"";
        boolean flag=false;
        System.out.print(turn+" move:"+chec+"\n");
        mov = myObj.next();
        if(mov.compareTo("print")==0)
        {   
            allValidMoves();
            for(Move m: validMovesList)
                System.out.println(m.getSource().posX+","+m.getSource().posY+" to "+m.getTarget().posX+","+m.getTarget().posY);
        }
        else if(mov.compareTo("help")==0){
            Move p=getBestMove(4);
            System.out.println((char)(p.getSource().posX+'a'-1) + p.getSource().posY + " -> " + (char)(p.getTarget().posX+'a'-1) + p.getTarget().posY);}
        else if(mov.length()==2 && validTarget(mov.charAt(0), mov.charAt(1)))//PAWN move [example 'd4']
            flag = move(mov.charAt(0)-'a'+1, mov.charAt(1)-'1'+1, Player.PAWN, 0, 0);
        else if(mov.length()==3 && validTarget(mov.charAt(1), mov.charAt(2)) && what(mov.charAt(0))!=Player.PAWN)//other piece move [example 'Nf3']
            flag = move(mov.charAt(1)-'a'+1, mov.charAt(2)-'1'+1, what(mov.charAt(0)), 0, 0);
        else if(mov.length()==4 && mov.charAt(1)=='x' && validTarget(mov.charAt(0),'2') && validTarget(mov.charAt(2), mov.charAt(3))) //PAWN attack [example 'dxe5']
            flag = move(mov.charAt(2)-'a'+1, mov.charAt(3)-'1'+1, Player.PAWN, mov.charAt(0)-'a'+1, 0);
        else if(mov.length()==5 && what(mov.charAt(0))!=Player.PAWN && mov.charAt(0)!='K' &&validTarget(mov.charAt(1), mov.charAt(2)) && validTarget(mov.charAt(3), mov.charAt(4)))//when there is several options to move [example 'Nf3d4']
            flag = move(mov.charAt(3)-'a'+1, mov.charAt(4)-'1'+1, what(mov.charAt(0)), mov.charAt(1)-'a'+1, mov.charAt(2)-'1'+1);
        else if(mov.compareTo("undo")==0)
            {if(!pc) flag = undo();
                else if(moves.size()>1) {undo();undo();}}
        if(!flag || (pc && mov.compareTo("undo")==0)) {
            allValidMoves(); 
            play(pc);
        }
        //if(flag) play();
    }
    
    boolean validTarget(char letter, char num)
    { return letter>='a' && letter<='h' && num>='1' && num<='8'; }
    
    Player what(char c)
    {
        if(c=='N') return Player.KNIGHT;
        else if(c=='B') return Player.BISHOF;
        else if(c=='R') return Player.ROOK;
        else if(c=='Q') return Player.QUEEN;
        else if(c=='K') return Player.KING;
        return Player.PAWN;
    }

    boolean move(int targetX, int targetY, int sourceX, int sourceY)
    { return move(targetX, targetY, sourceX, sourceY, false);}
    boolean move(int targetX, int targetY, int sourceX, int sourceY, boolean pcTurn)//get players name
    {
        updateCol();
        for(Piece p : col?piecesB:piecesW)
            if(p.getPos().equal(new Pos(sourceX,sourceY)))
                return move(targetX, targetY, p.getName(), sourceX, sourceY, pcTurn);
        return false;
    }

    boolean move(int targetX, int targetY, Player name, int sourceX, int sourceY) // regular move
    { return move(targetX, targetY, name, sourceX, sourceY, false);}
    boolean move(int targetX, int targetY, Player name, int sourceX, int sourceY, boolean pcTurn)
    {
        updateCol();
        for(Piece p : col?piecesB:piecesW)
        {
            if(p.getName()==name && (sourceX==0 || p.getPos().posX==sourceX) && (sourceY==0 || p.getPos().posY==sourceY))
                if(validMove(p, new Pos(targetX,targetY)))
                {
                    if(Math.abs(p.getPos().posX - targetX) == 2 && name == Player.KING){//hazraha..
                        if(!(underthreat(p.getPos().posX, targetY, col) || underthreat(targetX,targetY, col) || underthreat((targetX+p.getPos().posX)/2,targetY, col)))
                        for(Piece r : col?piecesB:piecesW)
                            if(r.getName()==Player.ROOK && r.getInit() && (r.getPos().posX==targetX+1 || r.getPos().posX==targetX-2))
                                {
                                    moves.add(0, new Movemant(new Pos(r.getPos().posX,targetY), new Pos(r.getPos().posX==8?6:4,targetY),false,false,r.getInit()));
                                    b.move(r.getPos().posX,targetY,r.getPos().posX==8?6:4, targetY);
                                    value += r.move(r.getPos().posX==8?6:4, targetY);
                                    moves.add(0, new Movemant(new Pos(p.getPos().posX,targetY),new Pos(targetX, targetY),false, true, p.getInit()));
                                    b.move(p.getPos().posX,targetY,targetX, targetY);
                                    value += p.move(targetX, targetY);
                                    return true;
                                }
                        return false;
                    }
                    boolean flagP = false, flag = false;  //flagP = Pawn goes to kill
                    if(p.getName()==Player.PAWN && p.getPos().posX!=targetX && b.empty(targetX, targetY))
                        flagP = true;
                    if(!b.empty(targetX, targetY) || flagP)
                        for(Piece die : col?piecesW:piecesB)
                            if((die.getPos().posX==targetX && die.getPos().posY==targetY) || (flagP && die.getPos().equal(moves.get(0).getTarget())))
                                {kill(die); flag=true; break;}
                    moves.add(0, new Movemant(p.getPos(), new Pos(targetX, targetY), flag, false, p.getInit()));
                    b.move(p.getPos().posX,p.getPos().posY,targetX, targetY);
                    value += p.move(targetX, targetY);
                    if(((!col && targetY==8) || (col && targetY==1)) && name==Player.PAWN){
                        char c='K';
                        if(!pcTurn){
                            System.out.println("choose change\n");
                            while(c!='B' && c!='N' && c!='Q' && c!='R') c = myObj.next().charAt(0);  }
                        value += p.become(what(pcTurn?'Q':c));
                        moves.get(0).setChange(true);
                    }
                    return true;
                }
        }
        return false;
    }

    boolean underthreat(int targetX, int targetY, boolean col) {
        int n = b.getVal(targetX,targetY);
        b.setVal(targetX,targetY, col?2:1);
        for(Piece p : col?piecesW:piecesB)
            if(p.validMove(b, new Pos(targetX,targetY)))
                {b.setVal(targetX,targetY, n); return true;}
        b.setVal(targetX,targetY, n);
        return false;
    }

    void kill(Piece die) {
        value-=die.getValue();
        eaten.add(0, new Piece(die.getPos(),die.getName(),die.getCol(),die.getInit()));
        b.setVal(die.getPos().posX, die.getPos().posY, 0);
        if(die.getCol()) piecesB.remove(die);
        else piecesW.remove(die);
    }

    void updateCol(){
        if(moves.isEmpty())
            col = false;
        else
            col = b.getVal(moves.get(0).getTarget().posX,moves.get(0).getTarget().posY)==1 ? true : false;
    }

    boolean validMove(Piece p, Pos target) {
        boolean flagP=false, flag=false;
        if(!moves.isEmpty())
            if(p.getName()==Player.PAWN && moves.get(0).getFM() && moves.get(0).getTarget().posY==(p.getCol()?4:5) && moves.get(0).getSource().posY==(p.getCol()?2:7) && new Pos(moves.get(0).getTarget().posX,(p.getCol()?3:6))==target && Math.abs(p.getPos().posX-target.posX)==1 && p.getPos().posY==(p.getCol()?4:5))
                {b.setVal(target.posX, target.posY, col?1:2); flagP=true;}
        if(p.validMove(b, target))
        {
            if(p.getName()==Player.KING && Math.abs(p.getPos().posX-target.posX) == 2){//hatzraha
                if(!(underthreat(p.getPos().posX,p.getPos().posY, col) || underthreat(target.posX,target.posY, col) || underthreat((target.posX+p.getPos().posX)/2,target.posY, col)))
                    for(Piece r : col?piecesB:piecesW)
                        if(r.getName()==Player.ROOK && r.getInit() && (r.getPos().posX==target.posX+1 || r.getPos().posX==target.posX-2))
                            return true;
                return false;}
            if(!b.empty(target.posX, target.posY))//eating
                for(Piece die : col?piecesW:piecesB)
                    if(die.getPos().posX==target.posX && (die.getPos().posY==target.posY || (flagP && die.getPos().posY==(getCol()?4:5))))
                    {kill(die); flag=true; break;}
            b.move(p.getPos().posX,p.getPos().posY,target.posX, target.posY);
            moves.add(0, new Movemant(p.getPos(),target,flag,false,p.getInit()));
            value += p.move(target.posX, target.posY);
            flag=check(p.getCol());
            undo();
            return !flag;
        }
        return false;
    }

    boolean undo() {
        updateCol();
        if(moves.isEmpty()) return false;
        Movemant m = moves.get(0);
        moves.remove(0);
        for(Piece p : col?piecesW:piecesB)
            if(p.getPos().posX==m.getTarget().posX && p.getPos().posY==m.getTarget().posY){
                value += p.move(m.getSource().posX, m.getSource().posY,m.getFM());
                if(m.getChange()) value += p.become(Player.PAWN);
                break;
            }
        b.move(m.getTarget().posX, m.getTarget().posY, m.getSource().posX, m.getSource().posY);
        if(m.getEat()){
            if(col)piecesB.add(eaten.get(0));
            else piecesW.add(eaten.get(0));
            b.getBack(eaten.get(0).getPos().posX,eaten.get(0).getPos().posY, col?2:1);
            value += eaten.get(0).getValue();
            eaten.remove(0);
        }
        updateCol();
        return m.getHazraha()? undo():true;
    }

    boolean check() {return check(col);}
    boolean check(boolean col) {
        for(Piece p : col?piecesB:piecesW)
            if(p.getName()==Player.KING)
                return underthreat(p.getPos().posX,p.getPos().posY, col);
        return false;
    }
    
    String printPieceByPosition(int x,int y, boolean co)
    {
        String s="";
        for (Piece i : co?piecesB:piecesW)
            if(i.getPos().posX==x && i.getPos().posY==y)
                switch (i.getName()) {
                    case PAWN:
                        s = "p";
                        break;
                    case KNIGHT:
                        s = "n";
                        break;
                    case BISHOF:
                        s = "b";
                        break;
                    case ROOK:
                        s = "r";
                        break;
                    case QUEEN:
                        s = "q";
                        break;
                    case KING:
                        s = "k";
                        break;
                }
        if(s=="")
            System.out.println("error\n");
        s+=co?"d":"l";
        return s;
    }
    
    void print()
    {
        //cout<<"    | A  | B  | C  | D  | E  | F  | G  | H  |\n";
        System.out.print(col?"1:  | ":"8:  | ");
        for (int i=(col?1:8),z=0; z<8; z++, System.out.print("\n" + (char)(z!=8?(col?z+'1':'8'-z):' ')), System.out.print(z!=8?":  | ":"")){
            for (int j=1; j<9; j++, System.out.print(" | "))
                {System.out.print((b.getVal(j,(col?(i+z):(i-z)))!=0)?printPieceByPosition(j,(col?(i+z):(i-z)),b.getVal(j,(col?(i+z):(i-z)))==1?false:true):"  ");}
        }
        System.out.print("   | A  | B  | C  | D  | E  | F  | G  | H  |\n");
    }
    
    void printB()
    {
        System.out.print("\n| ");
        for (int i=8; i>0; i--,System.out.print("\n| "))
            for (int j=1; j<9; j++,System.out.print(" | "))
                System.out.print(b.getVal(j,i));
    }

    boolean checkMat()
    {
        allValidMoves();
        if(!check() && checkPat()) return true;
        if(!(validMovesList.isEmpty())) return false;
        //print();
        System.out.print(col?"\nWHITE":"\nBLACK" + " Player win!!!\n");//
        //System.out.print(" Player win!!!\n");//        
        return true;
    }
    
    boolean checkPat()
    {
        if(validMovesList.isEmpty()){
            //print();
            System.out.println("Pat!! game over\n");//
            return true;
        }
        //if()//if its repeat himself
        return false;
    }

    void allValidMoves()
    {
        updateCol();
        validMovesList.clear();
        for(Piece p : col?piecesB:piecesW)
            for(int i=1;i<9;i++)
                for(int j=1;j<9;j++){
                    if(validMove(p, new Pos(i,j)))
                        validMovesList.add(new Move(p.getPos(),new Pos(i,j)));}
    }

}
