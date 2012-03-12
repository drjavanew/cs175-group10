

public class Player {
	
	private String name;
	private boolean turn;
	private boolean movingBeans;
	private GUI gui;
	public boolean MouseMove;
	public int valMouseMove;
	
	
	public Player(GUI aGUI) {
		this.gui = aGUI;
		movingBeans = false;
		MouseMove = false;
	
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isTurn() {
		return turn;
	}
	
	public void startTurn() {
		this.turn = true;
	}
	
	public void endTurn() {
		this.turn = false;
	}
	
	public boolean isMovingBeans() {
		return movingBeans;
	}
	
	public void startMovingBeans() {
		this.movingBeans = true;
	}
	
	public void endMovingBeans() {
		this.movingBeans = false;
	}
		
	

	public void moveCounters(final int i, final int j, final int totalBeans) {
		
		if (j == -1) {

			movingBeans = true;
		          for (int c = 0; c < 6; ++c) {
		              int num = gui.pots[0][c].getBeans();
		              gui.pots[0][c].removeBeans();
		              gui.pots[0][c].addBeans(gui.pots[1][5-c].getBeans());
		              gui.pots[1][5-c].removeBeans();
		              gui.pots[1][5-c].addBeans(num);
		          }
		      
		      
		      int score = gui.bigPots[0].getBeans(); 
		      gui.bigPots[0].removeBeans();
		      gui.bigPots[0].addBeans(gui.bigPots[1].getBeans());
		      gui.bigPots[1].removeBeans();
		      gui.bigPots[1].addBeans(score);
		      
		      ((KalahPieGameState)(gui.game.gs)).secondPlayerFirstMoveMade = true;
		      endMovingBeans();
		      endTurn();
			  
		      gui.setCurrentPlayer(0);
		      return;
		  }
					
					movingBeans = true;
					

					
					
					int amount = totalBeans;
					// clean the beans
					int curRow = gui.getCurrentPlayer();
					int curDirection = i == 0 ? 1 : -1;
					int curCol = j;
					boolean bonusTurn = false;
					while (amount-- > 0) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						 curCol += curDirection; 
						 if (curCol >= GUI.PIT_COUNT) {
						        curRow = 1;
						        if (gui.getCurrentPlayer() == 0) {
						            gui.bigPots[0].addBeans(1);
						            if (amount == 0) bonusTurn = true;
						        }
						        else {
						          gui.pots[curRow][--curCol].addBeans(1);
						        }
						        curDirection = -1;
						 }
						 else if (curCol < 0) {
						        curRow = 0;
						        if (gui.getCurrentPlayer() == 1) {
						        	gui.bigPots[1].addBeans(1);
						            if (amount == 0) bonusTurn = true;
						        }
						        else {
						            gui.pots[curRow][++curCol].addBeans(1);
						        }
						        curDirection = 1;
						    }
						    else {
						       gui.pots[curRow][curCol].addBeans(1);
						    }
					   
//						  grab conditions?
						    if (curCol >=0 && curCol < GUI.PIT_COUNT && 
						        gui.pots[curRow][curCol].getBeans()== 1 && amount == 0) {
						        if (curRow == gui.getCurrentPlayer() && gui.pots[1 - curRow][curCol].getBeans() > 0) {
						            int grabAmt = gui.pots[1- curRow][curCol].getBeans() + 1;
						            gui.bigPots[gui.getCurrentPlayer()].addBeans(grabAmt);
						            gui.pots[1][curCol].removeBeans();
						            gui.pots[0][curCol].removeBeans();
						        }
						    }
						    }
						    endMovingBeans();
						    
						  if (!gui.pieMoveMade && gui.getCurrentPlayer() == 1) {
						      gui.pieMoveMade = true;
						  }
					
						  if (!bonusTurn) {
							  endTurn();
							  
						      gui.setCurrentPlayer(1- gui.getCurrentPlayer());                     // swap players
						  }
						
						
						
					
						
				
					
			
		
		
	}

	public GUI getGUI () {
		return gui;
	}

	
	
	public int getMove() {
		
		while (!MouseMove) {
			
			if (gui.ready >= 4) return 100;
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					}
				
		}
      
		MouseMove = false;
		return valMouseMove;

	}
}
