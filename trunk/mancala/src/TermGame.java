import java.io.Console;
import java.io.IOException;
import java.lang.reflect.*;

public class TermGame {
    public MancalaPlayer player[];
    public MancalaGameState gs;

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
            int move = player[gs.CurrentPlayer()].getMove(gs);
            while (!gs.validMove(move)) {
//                System.out.println("Invalid move!");
//                System.out.println("Player " + gs.CurrentPlayer() + "'s move: ");
                move = player[gs.CurrentPlayer()].getMove(gs);
            }
//            System.out.println(move);
            gs.play(move);
        }
//        gs.printState();
//        System.out.println("\nPerforming player 0 post game actions...");
        player[0].postGameActions(gs);
//        System.out.println("Performing player 1 post game actions...");
        player[1].postGameActions(gs);

        gs.computeFinalScore();
//        System.out.println("After moving in remaining pieces: ");
//        gs.printState();

//        System.out.println("Player 0 score: " + gs.getScore(0));
//        System.out.println("Player 1 score: " + gs.getScore(1));
//        System.out.println();
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
        
            
        TermGame game = new TermGame(gameType, ss, players[0], players[1]);
        for (int i =1; i<= 100000; i++){
//        	System.out.println("Iteration" + i + ": " );
        game.play();
        
        }
    }
}
