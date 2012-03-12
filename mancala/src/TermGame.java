import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;

public class TermGame {
    public MancalaPlayer player[];
    public MancalaGameState gs;
    static BufferedWriter bw;
    static int win;
    static int loose;
    static int draw;
    
    // Using reflection to instantiate player classes from command line
    public TermGame (String gameType, int startingStones, String player0Name, String player1Name) {
    	
    	
        try {
            Class<?> cls = Class.forName(gameType+"GameState");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(startingStones);
            gs = (MancalaGameState) ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }
        player = new MancalaPlayer[2];
        try {
            Class<?> cls = Class.forName(player0Name+"_Player");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(0);
            player[0] = (MancalaPlayer) ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }
        try {
            Class<?> cls = Class.forName(player1Name+"_Player");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(1);
            player[1] = (MancalaPlayer) ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }
    }

    public void play() throws Exception {
        while (!gs.checkEndGame()) {
//            gs.printState();
//            System.out.print("Player " + gs.CurrentPlayer() + "'s move: ");
        	long time = System.currentTimeMillis();
		int move = player[gs.CurrentPlayer()].getMove(gs.copy());
            while (!gs.validMove(move)) {
                System.out.println("Invalid move!");
                System.out.println("Player " + gs.CurrentPlayer() + "'s move: ");
                move = player[gs.CurrentPlayer()].getMove(gs.copy());
            }
//            System.out.println(move);
            time -= System.currentTimeMillis();
            if (time >= 5000) System.err.println("TAKES TOO LONG:" + time /1000);
            gs.play(move);
        }
//        gs.printState();
//        System.out.println("Performing player 0 post game actions...");
        player[0].postGameActions(gs);
//        System.out.println("Performing player 1 post game actions...");
        player[1].postGameActions(gs);

        gs.computeFinalScore();
//        System.out.println("After moving in remaining pieces: ");
//        gs.printState();

        System.out.println("Player 0 score: " + gs.getScore(0));
        System.out.println("Player 1 score: " + gs.getScore(1));
        System.out.println();
        if (gs.getScore(0) > gs.getScore(1)) win++;
        else if (gs.getScore(0) < gs.getScore(1)) loose++;
        else draw++;
        bw.write("Player 0 score: \t" + gs.getScore(0)+ "\n");
        bw.write("Player 1 score: \t" + gs.getScore(1)+ "\n");
        bw.write("\n");
        gs.reset();

    }

    public static void main(String[] args) 
        throws Exception {
        if (args.length < 1) {
            System.err.println("No game type specified!");
            return;
        }
        if (args.length < 2) {
            System.err.println("Starting stones in each bucket not specified!");
            return;
        }
        String gameType = args[0];
        int ss = Integer.parseInt(args[1]);
        if (ss < 1) {
            System.err.println("Starting stones must be a non-zero positive number");
            return;
        }
        String[] players = new String[2];
            if (args.length > 1) players[0]=args[2]; else players[0] = "interactive";
            if (args.length > 2) players[1]=args[3]; else players[1] = "random";
        
            
//        TermGame game = new TermGame(gameType, ss, players[0], players[1]);
//        for (int i =1; i<=100; i++){
//        	System.out.println();
//      	System.out.println("Iteration " + i + ": " );
//        game.play();
//        
//        }
            runTest();
    }
    
    
    public static void reset() {
    	win =0;
    	loose =0;
    	draw= 0;
    }
    
    public static void TestTemplate(String filename, String Player1, String Player2, int limit) {
    	try {
    	bw = new BufferedWriter(new FileWriter(filename));
    	
    	TermGame game = new TermGame("KalahPie",4,Player1, Player2);
    	bw.write(Player1 + " vs " + Player2 + "\n");
    	reset();
    	for( int i =1; i<= limit; i++) {
	  System.out.println("Iteration" + i);
    		bw.write("Iteration " + i + ": \n" );
	        try {
				game.play();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			bw.write("Win = " + win + "\n");
			bw.write("Loose = " + loose + "\n");
			bw.write("Draw = " + draw + "\n");
			bw.write("====================================="+ "\n\n");	
    	bw.close();
    	game.player[0].actionsBeforeDeletion();
    	game.player[1].actionsBeforeDeletion();
    	}
    	catch (IOException e) {
    		
    	}
    }
    public static void runTest () {
    	int games = 5000;
//    	TestTemplate("R1.txt", "MinMaxAB", "AIGraDescent", games);
//    	TestTemplate("R2.txt", "AIGraDescent","MinMaxAB", games);
//    	TestTemplate("R3.txt", "MinMaxNaive", "AIGraDescent", games);
//    	TestTemplate("R4.txt", "AIGraDescent","MinMaxNaive", games);
//    	TestTemplate("R5.txt", "Herlihy", "AIGraDescent", games);
//    	TestTemplate("R6.txt", "AIGraDescent","Herlihy", games);
	
    	

//    	TestTemplate("S1.txt", "MinMaxAB", "AILearning", games);
//    	TestTemplate("S2.txt", "AILearning","MinMaxAB", games);
//    	TestTemplate("S3.txt", "MinMaxNaive", "AILearning", games);
//    	TestTemplate("S4.txt", "AILearning","MinMaxNaive", games);
//    	TestTemplate("S5.txt", "Herlihy", "AILearning", games);
//    	TestTemplate("S6.txt", "AILearning","Herlihy", games);
    	
    	
//    	TestTemplate("T1.txt", "MinMaxAB", "AINormalEq", games);
//    	TestTemplate("T2.txt", "AINormalEq","MinMaxAB", games);
//    	TestTemplate("T3.txt", "MinMaxNaive", "AINormalEq", games);
//    	TestTemplate("T4.txt", "AINormalEq","MinMaxNaive", games);
//    	TestTemplate("T5.txt", "Herlihy", "AINormalEq", games);
//    	TestTemplate("T6.txt", "AINormalEq","Herlihy", games);

//    	      TestTemplate("SS1.txt", "AILearning", "Group9RL", games);
//	      TestTemplate("SS2.txt", "Group9RL","AILearning", games);
//	      TestTemplate("SS3.txt", "AILearning", "Team8", games);
//	      TestTemplate("SS4.txt", "Team8","AILearning", games);
//	      TestTemplate("SS5.txt", "AILearning", "group1", games);
//	      TestTemplate("SS6.txt", "group1","AILearning", games);

//	      TestTemplate("TT1.txt", "AINormalEq", "Group9RL", games);
//              TestTemplate("TT2.txt", "Group9RL","AINormalEq", games);
//              TestTemplate("TT3.txt", "AINormalEq", "Team8", games);
//              TestTemplate("TT4.txt", "Team8","AINormalEq", games);
//              TestTemplate("TT5.txt", "AINormalEq", "group1", games);
//              TestTemplate("TT6.txt", "group1","AINormalEq", games);

            TestTemplate("AI1.txt", "AINormalEq", "AILearning", games);
              TestTemplate("AI2.txt", "AILearning","AINormalEq", games);

    }
}
