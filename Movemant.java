public class Movemant extends Move {

	private boolean eat;
    private boolean hazraha;
    private boolean firstMove;
    private boolean change;

    Movemant(Pos s, Pos t, boolean e, Boolean h, Boolean fm) {
		super(s, t);
		//TODO Auto-generated constructor stub
		eat=e; 
		hazraha=h; 
		firstMove=fm; 
		change=false; 
	}
    // Movemant(Movemant m)
    // {
    //     super(m.getSource(),m.getTarget());
    //     eat=m.eat; 
	// 	hazraha=m.hazraha; 
	// 	firstMove=m.firstMove; 
	// 	change=m.change; 
    // }
//  movemant(pos s, pos t):source(s),target(t),eat(false),hazraha(false),firstMove(false),change(false){}
//    pos getSource(){return source;}
//    pos getTarget(){return target;}
    boolean getEat(){return eat;}
    Boolean getHazraha(){return hazraha;}
    boolean getFM(){return firstMove;}
    Boolean getChange(){return change;}
    void setChange(Boolean c){change=c;}

}