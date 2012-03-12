

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GUI {
	Pot[][] pots;
	Pot[] bigPots;
	public final static int PIT_COUNT = 6;
	private static final int START_BEANS = 4;
	private Player[] players;

	TermGame game;
	 
	private String  nameOne="";
	private String nameTwo ="";
	private String  newNameOne="";
	private String newNameTwo ="";
	private int CurrentPlayer;
	public boolean pieMoveMade;
	public boolean newInfoLearnt;
	 int ready = 0; // 0 unknow  1 ready to play, 2 playing, 3 ended, 4 interrupted
	
	
	public static void main(String[] args) {
		GUI me = new GUI("interactive","random");
		while (true) {
		if (me.ready == 1)
			me.play();
		}
		
		
	}
	
	public int getCurrentPlayer () {
		return CurrentPlayer;
	}
	
	public void setCurrentPlayer(int player) {
		CurrentPlayer = player;
		players[player].startTurn();
	}
	
	
		
	private void flashingPots(int player) {
		for (int i = 0; i< PIT_COUNT; i++) {
			pots[player][i].mouseOver = true;
			pots[player][i].refresh();
			try {
				Thread.sleep(100);
			} catch (InterruptedException ie2) {
				
				ie2.printStackTrace();
			}
			pots[player][i].mouseOver = false;
			pots[player][i].refresh();
		}
		try {
			Thread.sleep(300);
		} catch (InterruptedException ie2) {
			
			ie2.printStackTrace();
		}
	}
	
	private void play() {
		
		 while (!game.gs.checkEndGame() && ready <4) {
			    
			    
	            CurrentPlayer = game.gs.CurrentPlayer();
	            flashingPots(CurrentPlayer);
	            System.out.print("Player " + game.gs.CurrentPlayer() + "'s move: ");
	            int move =100;
				try {
					if (!players[getCurrentPlayer()].getName().equalsIgnoreCase("interactive") )
					move = game.player[game.gs.CurrentPlayer()].getMove(game.gs);
					else {
						move = players[getCurrentPlayer()].getMove();
					}
					switch (ready){
					case 4:
						reset(nameOne, nameTwo);
						break;
					case 5:
						reset(newNameOne,newNameTwo);
						return;
					case 6:
						reset(nameTwo,nameOne);
						return;
						
					default:
						break;
				}		
				
				}
				catch (Exception e1) {
				
					e1.printStackTrace();
				}
	            while (!game.gs.validMove(move) ) {
	                System.out.println("Invalid move!");
	                System.out.println(CurrentPlayer);
	                System.out.println("Player " + game.gs.CurrentPlayer() + "'s move: ");
	                try {
	                	if (!players[getCurrentPlayer()].getName().equalsIgnoreCase("interactive") )
	    					move = game.player[game.gs.CurrentPlayer()].getMove(game.gs);
	    					else {
	    						move = players[getCurrentPlayer()].getMove();}
	                	switch (ready){
						case 4:
							reset(nameOne, nameTwo);
							break;
						case 5:
							reset(newNameOne,newNameTwo);
							return;
						case 6:
							reset(nameTwo,nameOne);
							return;
							
						default:
							break;
					}		
					} catch (Exception e2) {
						
						e2.printStackTrace();
					}
	            }
	            
	            		
	            try {
	            	
					game.gs.play(move);
					if (ready ==1) ready =2;
					System.out.println("MOVE  " +move);
					int num =0;
					if (move != -1) {
						num = pots[CurrentPlayer][move].getBeans();
//						System.out.println(num);
						pots[CurrentPlayer][move].removeBeans();
					}
					players[CurrentPlayer].moveCounters(CurrentPlayer, move, num);
					try {
						Thread.sleep(500);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				} catch (Exception e) {
					
					e.printStackTrace();
				}
	            
	        }
		 
//	        System.out.println("Performing player 0 post game actions...");
	        game.player[0].postGameActions(game.gs);
//	        System.out.println("Performing player 1 post game actions...");
	        game.player[1].postGameActions(game.gs);
	        
	        game.gs.computeFinalScore();
	        finalCollections();
	        ready =3;
	        newInfoLearnt = true;
//	        System.out.println("After moving in remaining pieces: ");
//	        gs.printState();

	        System.out.println("Player 0 score: " + game.gs.getScore(0));
	        System.out.println("Player 1 score: " + game.gs.getScore(1));
	        System.out.println();
//	        if (gs.getScore(0) > gs.getScore(1)) win++;
//	        else if (gs.getScore(0) < gs.getScore(1)) loose++;
//	        else draw++;
	        // ask for new game or not
	        //gs.reset();
		
	}
	
	private void finalCollections() {
		for (int i = 0; i<=1; i++) {
			for (int j = 0; j < PIT_COUNT; j++) {
				int num = pots[i][j].getBeans();
				if (num >0) {
					pots[i][j].removeBeans();
					bigPots[i].addBeans(num);
				}
				
			}
			bigPots[i].refresh();
		}
		int finalscore = bigPots[0].getBeans() - bigPots[1].getBeans();
		if(finalscore >0) {
			JOptionPane.showMessageDialog(null, players[0].getName() + " won with " + bigPots[0].getBeans() + " points.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		} else if(finalscore < 0) {
			JOptionPane.showMessageDialog(null, players[1].getName() + " won with " + bigPots[1].getBeans() + " points.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "It was a tie. " + players[0].getName() + " and " + players[1].getName() + " both scored 24 points.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
		}

	}
	//constructor 
	public GUI(String name1, String name2) {
		createGUI (name1, name2); 
		game = new TermGame("KalahPie",START_BEANS,name1, name2);
	}
	
	private  void createGUI (String name1, String name2) {
		if (name1 =="" && name2 == "") {
			nameOne = "interactive";
				nameTwo =  "random";
		}
		else {
			nameOne = name1;
			nameTwo = name2;
		}
		
		
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		JFrame frame = new JFrame("Mancala");
		frame.setSize(860, 320);
		frame.setLocation((screen.width - 860) / 2, (screen.height - 320) / 2);
		
		createMenu(frame);
		
		
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		
		initPlayers(nameOne, nameTwo);
		createPots();
		
		JPanel center = new JPanel(new GridLayout(2, 6));
		center.setOpaque(false);
		MyPanel panel = new MyPanel();
		panel.setVisible(true);
		
		panel.setLayout(new BorderLayout());
		panel.add(center, BorderLayout.CENTER);
		
		
		addPots(center, panel);
		
		
		content.add(panel, BorderLayout.CENTER);
		panel.repaint();
		
		frame.setVisible(true);
		frame.repaint();
		
		
		
		CurrentPlayer = 0;
		pieMoveMade = false;
		ready = 0;
		newInfoLearnt = false;
		
	}
	
	private void initPlayers(String name1,String name2) {
		players = new Player[2];
		players[0] = new Player(this);
		players[1] = new Player(this);
		players[0].setName(name1);
		players[1].setName(name2);
		players[0].startTurn();
		
	}

	private void createMenu(JFrame frame) {
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("Game");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);
		
		JMenuItem newGame = new JMenuItem("New Game");
		menu.add(newGame);
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!players[0].isMovingBeans() && !players[1].isMovingBeans()) {
					if (ready == 0)  { ready = 1; return;}
					if (ready <3) ready = 4;
					else if (ready == 3) {
						reset(nameOne,nameTwo);
				
					}
					
				}
			}

			
		});
		
		JMenuItem changePlayers = new JMenuItem("Change Players");
		menu.add(changePlayers);
		changePlayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String value1 = JOptionPane.showInputDialog(null, "Enter name for player one", nameOne);
				
					if(value1 != null && value1.length() <= 40)
						newNameOne = value1.replaceAll("\t", " ");
				String value2 = JOptionPane.showInputDialog(null, "Enter name for player two", nameTwo);
					if(value2 != null && value2.length() <= 40)
						newNameTwo = value2.replaceAll("\t", " ");
				if (ready == 3 || ready == 0) {
						reset(newNameOne,newNameTwo);
						return;
					}
				if (ready <3) ready = 5;
			}
		});
		
		JMenuItem switchPlayers = new JMenuItem("Switch Players");
		menu.add(switchPlayers);
		switchPlayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ready == 3 || ready == 0) {
					reset(nameTwo,nameOne);
					return;
				}
				if (ready <3) ready = 6;
				 
			}
		});
		
		JMenuItem exit = new JMenuItem("Exit");
		menu.add(exit);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (newInfoLearnt) {
					game.player[0].actionsBeforeDeletion();
					game.player[1].actionsBeforeDeletion();
						
				}
				System.exit(0);
			}
		});
		
	}

	


	private void addPots(JPanel center, MyPanel panel) {
		panel.add(bigPots[0], BorderLayout.EAST);
		panel.add(bigPots[1], BorderLayout.WEST);
		for (int i = 1; i >=0 ; i--) 
			for (int j = 0; j<6; j++ )
			center.add(pots[i][j]);
			
	}

	private void createPots() {
		bigPots = new BigPot[2];
		for (int i = 0; i<=1; i++) {
			bigPots[i] = new BigPot();
			bigPots[i].setOwner(players[i]);
		}
		
		
		pots = new Pot[2][6];
		for (int i = 0; i < pots.length; i++) 
			for (int j =0; j< pots[0].length; j++) {
				pots[i][j] = new Pot(i,j);
				pots[i][j].setOwner(players[i]);
			} 
		
	}
	
	
	private void reset(String name1, String name2) {
		
		if (ready ==1) return;
		
		players[0].endTurn();
		players[1].endTurn();
		setCurrentPlayer(0);
		
		
		
		
		
		if (nameOne == name1 && nameTwo == name2) {  // old setting, new iteration
			for (int i = 0; i<=1; i++) {
				bigPots[i].initBeans();
				bigPots[i].refresh();
			}
			for (int i = 0; i < pots.length; i++) 
				for (int j =0; j< pots[0].length; j++) {
					pots[i][j].initBeans();
					pots[i][j].refresh();
				}
			
			if (ready != 3) {
				game.player[0].reset();
				game.player[1].reset();
			}
			
			ready =1;

			game.gs.reset();
			

		}
		else {  // newgame
			nameOne = name1;
			nameTwo = name2;
			
			
			players[0] = new Player(this);
			players[1] = new Player(this);
			players[0].setName(nameOne);
			players[1].setName(nameTwo);
			players[0].startTurn();
			
			if (newInfoLearnt) {
			game.player[0].actionsBeforeDeletion();
			game.player[1].actionsBeforeDeletion();
			}
	
			game = new TermGame("KalahPie",START_BEANS,nameOne, nameTwo);
			for (int i = 0; i<=1; i++) {
				bigPots[i].setOwner(players[i]);
				bigPots[i].initBeans();
				bigPots[i].refresh();
			}
			for (int i = 0; i < pots.length; i++) 
				for (int j =0; j< pots[0].length; j++) {
					pots[i][j].setOwner(players[i]);
					pots[i][j].initBeans();
					pots[i][j].refresh();
				}
			setCurrentPlayer(0);
			ready = 1;
			
		}
	}

	
	
	
}
