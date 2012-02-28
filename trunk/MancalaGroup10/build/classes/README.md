###Changelog
- 01/26/2012: Changed to feature added yesterday to have the method take in the game state instead. This should allow for more flexibility by providing the entire final state as information.
- 01/25/2012: Added a method to MancalaPlayer which allows it to do something after the game ends. This can be used to provide feedback to the player for learning purposes.
- 01/23/2012: Fixed a bug in KalahGameState where a grab was not occuring when it lands on the first bucket after the opponent's.
- 01/21/2012: Modified the interactive_Player class so it works in Eclipse.
- 01/18/2012: Added a version of Kalah using the pie rule (KalahPieGameState).
Removed "winner" fields from GameState classes.
- 01/14/2012: Initial release.

###About the Mancala Game Framework

This framework was adapted for Mancala by William Lam for the Winter 2012 
offering of CS 175, taught by Max Welling. The code was originally designed 
for Connect Four, provided by Alex Ihler.

###Classes/Interfaces
    MancalaGameState (abstract)
    |- KalahGameState
    |- OwareGameState
    |- KalahPieGameState

Use one of the subclasses to decide on the Mancala variant.


    MancalaPlayer (interface)
    |- random_Player
    |- interactive_Player

Implement the MancalaPlayer interface to create your AI routine. The 
name of your class should be formatted as YourClassName_Player.


    TermGame

TermGame provides an example of how to use MancalaGameState and 
MancalaPlayer. It uses reflection to instantiate the state and player classes 
based on command line parameters. We will use a similar main class to 
this to run the tournament.

    The command-line parameter format is: 
    java TermGame <GameType> <StartingStones> <Player0Class> <Player1Class>

    Ex: java TermGame Kalah 4 interactive random
    Set player 0 as the interactive player and player 1 as the random player, 
    playing the Kalah variant with 4 starting stones in each bucket.

Note that for Oware, repeated states are possible in the game tree. Since 
we cannot assume that two players will make a consensus to end the game, 
we will leave this decision up to the user interface running the game. (It 
should detect a sequence of repeated states and end the main execution loop, 
if so.)

###About the competition

For the competition, we will run a program using the *KalahPieGameState* class 
that uses your AI player classes (and any of their dependencies) only.  It 
will stop the execution of your player class without warning (and thus count 
as a forfeit) if it does not make a move within the time limit. You will be 
responsible of keeping track of the time within your implementation.

We will use a machine running Java 6 and your agent should use no more than 
1GB memory and a single thread.

Time limit: 5 seconds per move

Tournament format: Double round-robin 
(allowing players to take turns going first)

We will then run the tournament as many times as we have time/resources for. 
The hope is that reinforcement learning agents will play differently as we 
run more tournaments. The rankings will then be determined by the number of 
wins across all tournaments.

