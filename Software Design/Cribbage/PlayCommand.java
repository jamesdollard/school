import java.util.ArrayList;

/**
 * 'Play' cribbage command implementation of Command interface
 * @author jamesdollard
 *
 */
public class PlayCommand implements Command {
	
	public boolean isAGo = false;
	
	Cribbage cribbageGame;
	
	Player currentPlayer;
	Player otherPlayer;
	PlayingCard cardPlayed;
	
	PlayingCard humanCard;
	PlayingCard computerCard;
	
	Pile currentPlayerPile;
	Pile otherPlayerPile;
	
	ArrayList<Score> scores = new ArrayList<Score>();
	int pointsAwarded;
	
	boolean isComputer;
	
	public PlayCommand(Cribbage cribbageGame, Player currentPlayer, Player otherPlayer, PlayingCard cardPlayed, boolean isComputer) {
		
		this.cribbageGame = cribbageGame;
		this.currentPlayer = currentPlayer;
		this.otherPlayer = otherPlayer;
		this.cardPlayed = cardPlayed;
		this.isComputer = isComputer;

	}
	
	/**
	 * Executes command to play
	 * @return list of scores
	 */
	public ArrayList<Score> doCommand() {
		
		currentPlayer.playCard(cardPlayed);
		
		scores = Score.scorePile(currentPlayer.getActivePile(), otherPlayer.getActivePile());
		if (scores == null)
		{
			scores = new ArrayList<Score>();
		}
		//check to see if opponent passed last time
		if (otherPlayer.hasPassed())
		{
			scores.add(new Score(2, "two for the go"));
		}
		
		//add up all the points and award them to the player; also save them!
		pointsAwarded = Score.addScores(scores);
		currentPlayer.addPoints(pointsAwarded);
		cribbageGame.addToPileTotal(cardPlayed.value());
		
		return scores;
		
	}
	
	/**
	 * Undoes the command to play
	 */
	public void undoCommand() {

		currentPlayer.replaceCard(cardPlayed);
		currentPlayer.subtractPoints(pointsAwarded);;
		cribbageGame.subtractFromPileTotal(cardPlayed.value());
		
		if(isComputer) {
			System.out.println("Computer returning " + cardPlayed.toString() + " and " + pointsAwarded + " points.");
		}else {
			System.out.println("Human returning " + cardPlayed.toString() + " and " + pointsAwarded + " points.");
		}
		
		
	}

}
