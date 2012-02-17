import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Random;

public class GeneticTermGame {
    public MancalaPlayer player[];
    public MancalaGameState gs;

    // Using reflection to instantiate player classes from command line
    public GeneticTermGame (String gameType, int startingStones, MancalaPlayer[] player) {
        try {
            Class<?> cls = Class.forName(gameType+"GameState");
            Class partypes[] = new Class[1];
            partypes[0] = int.class;
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = new Integer(startingStones);
            gs = (MancalaGameState) ct.newInstance(arglist);
        } catch (Throwable e) {
       //     System.err.println(e);
        }
        this.player = player;
       

    }

    public int play() throws Exception {
        while (!gs.checkEndGame()) {
       //     gs.printState();
      //      System.out.print("Player " + gs.CurrentPlayer() + "'s move: ");
            int move = player[gs.CurrentPlayer()].getMove(gs);
            while (!gs.validMove(move)) {
            //    System.out.println("Invalid move!");
            //    System.out.println("Player " + gs.CurrentPlayer() + "'s move: ");
                move = player[gs.CurrentPlayer()].getMove(gs);
            }
       //     System.out.println(move);
            gs.play(move);
        }
      //  gs.printState();
      //  System.out.println("\nPerforming player 0 post game actions...");
        player[0].postGameActions(gs);
      //  System.out.println("Performing player 1 post game actions...");
        player[1].postGameActions(gs);

        gs.computeFinalScore();
    //    System.out.println("After moving in remaining pieces: ");
   //     gs.printState();

     //   System.out.println("Player 0 score: " + gs.getScore(0));
      //  System.out.println("Player 1 score: " + gs.getScore(1));
     //   System.out.println();
       
        
        
        if (gs.getScore(0) < gs.getScore(1))
        {gs.reset();
        	return -1;}
        if (gs.getScore(0) > gs.getScore(1))
        {gs.reset();
        	return 1;}
        gs.reset();
        return 0;
        

    }

    public static void main(String[] args) 
        throws Exception {

        String gameType = "KalahPie";
        int ss = 4;
        int ELITE_COUNT = 2;	// Top "X" player will be selected

        int win;
        int maxCount = 0;
        int maxPlayer = 0;
        
        int POPULATION_SIZE = 100;
        ArrayList<MancalaPlayer> population = new ArrayList<MancalaPlayer>();
        double[] weight = new double[GeneticRegressionLearning.getTotalFeature()];
        
        Random ran = new Random();
        GeneticTermGame game;
        
        //init
        for (int i = 0; i < POPULATION_SIZE;i++)
        {
        	weight  = new double[GeneticRegressionLearning.getTotalFeature()];
    		try {
				String sCurrentLine;
			 
				BufferedReader br = new BufferedReader(new FileReader("data.txt"));
				//System.out.println("Reading theta from " + filename );
				int j=0;
				while ((sCurrentLine = br.readLine()) != null) {
					weight[j] =	Double.parseDouble(sCurrentLine);
					j++;
				}			
				br.close();	    
			} 
    		catch (Exception e)
    		{}
    		
   //     	for (int j = 0; j < weight.length;j++)
   //     	{
   //     		weight[j] = ran.nextInt() % 100;
   //     	}
        	population.add(new GeneticAILearning_Player(weight));
        }
        GeneticAILearning_Player tempAI;

        for (int a = 0; a < 1000; a++)
        {
        	if (a > 0)
        	{
        		try {
    				String sCurrentLine;
    			 
    				BufferedReader br = new BufferedReader(new FileReader("data.txt"));
    				//System.out.println("Reading theta from " + filename );
    				int j=0;
    				while ((sCurrentLine = br.readLine()) != null) {
    					weight[j] =	Double.parseDouble(sCurrentLine);
    					j++;
    				}			
    				br.close();	    
    			} 
        		catch (Exception e)
        		{}
        		// reproduction
        		population = new ArrayList<MancalaPlayer>();
        		
        		population.add(new GeneticAILearning_Player(weight));
        		for (int i= 1; i < POPULATION_SIZE;i++)
        		{
        			tempAI = new GeneticAILearning_Player(weight.clone());
        			tempAI.mutate();
        			population.add(tempAI);
        		}
        		
        	}
        	
        	
        	
        	
	        //fitness
	        MancalaPlayer[] player = new MancalaPlayer[2];
	        int[] count = new int[POPULATION_SIZE];
	        for (int i = 0; i < count.length;i++)
	        {
	        	count[i] = 0;
	        }
	        
	
	        int least = 0;
	        for (int i = 0; i < POPULATION_SIZE;i++)
	        {
	        	
	        	for (int j = 0; j < POPULATION_SIZE;j++)
	        	{
	        	//	System.err.println(i + "vs" + j);
	        		((GeneticAILearning_Player) population.get(i)).setPlayer(0);
	        		((GeneticAILearning_Player) population.get(j)).setPlayer(1);
	        		player[0] = population.get(i);
	        		player[1] = population.get(j);
	        		
	        		game = new GeneticTermGame(gameType, ss, player);
	        		win = game.play();
	        		if (win == 1)
	        		{
	        			count[i]++;
	        			if (count[i] >	maxCount)
	        			{
	        				maxCount = count[i];
	        				maxPlayer = i;
	        			}
	        		}
	        		else if (win == -1)
	        		{
	        			count[j]++;
	        			if (count[j] > maxCount)
	        			{	maxCount = count[j];
	        				maxPlayer = j;
	        			}
	        		}
	        	}   
	        }
	        weight = ((GeneticAILearning_Player) population.get(maxPlayer)).getWeight();
			try {
				
				String s;
				BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
				for (int j=0; j < weight.length; j++) {
						s = String.valueOf(weight[j])+ "\n";
						bw.write(s);
				}
				
				bw.close();
				    
			}   
				 
			catch (IOException e) {
				    
			}
	        
	        System.out.println("Best Gene for generation " + a + " :");
	        for (int i = 0; i < weight.length;i++)
	        {
	        	System.out.println(weight[i]);
	        }
	        
	         
        }
        
        

    }
}
