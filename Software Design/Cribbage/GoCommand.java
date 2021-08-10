import java.util.ArrayList;

/**
 * 'Go' cribbage command implementation of Command interface
 * @author jamesdollard
 *
 */
public class GoCommand implements Command{
	
	Player currentPlayer;
	Player otherPlayer;
	
	Cribbage cribbageGame;
	int lastPileTotal;
	Pile currentPlayerPile;
	Pile otherPlayerPile;
	
	boolean otherPlayerPassed = false;
	
	ArrayList<Score> scores = new ArrayList<Score>();
	
	public GoCommand(Cribbage cribbageGame, Player currentPlayer, Player otherPlayer) {
		this.cribbageGame = cribbageGame;
		this.currentPlayer = currentPlayer;
		this.otherPlayer = otherPlayer;
		
		lastPileTotal = cribbageGame.getPileTotal();
		currentPlayerPile = currentPlayer.getPile();
		otherPlayerPile = otherPlayer.getPile();
		
		Pile currentPileCopy = new Pile();
		ArrayList<PlayingCard> currentPileCards = currentPlayerPile.getActivePile();
		
		while(currentPileCards.size() > 0) {
			currentPileCopy.addCard(currentPileCards.remove(currentPileCards.size()-1));
		}
		currentPlayerPile = currentPileCopy;
		
		Pile otherPileCopy = new Pile();
		ArrayList<PlayingCard> otherPileCards = otherPlayerPile.getActivePile();
		
		while(otherPileCards.size() > 0) {
			otherPileCopy.addCard(otherPileCards.remove(otherPileCards.size()-1));
		}
		otherPlayerPile = otherPileCopy;

	}
	
	/**
	 * Executes command to go
	 * @return list of scores
	 */
	public ArrayList<Score> doCommand() {
		
		if (otherPlayer.hasPassed())
		{
			scores.add(new Score(2, "two for the go"));
			currentPlayer.addPoints(2);
			
			otherPlayerPassed = true;
			
		}
		currentPlayer.setPassed(true);
		if (!scores.isEmpty())
		{
			System.out.println("Starting a new pile");
			currentPlayer.setPassed(false);
			otherPlayer.setPassed(false);
			currentPlayer.newPile();
			otherPlayer.newPile();	
			cribbageGame.setPileTotal(0); //starting a new pile
		}
		
		return scores;
		

	}
	
	/**
	 * Undoes the command to go
	 */
	public void undoCommand() {
		
		if(otherPlayerPassed) {
			currentPlayer.subtractPoints(2);
		}
		currentPlayer.setPassed(false);
		if(!scores.isEmpty()) {
			otherPlayer.setPassed(otherPlayerPassed);
		}
		
		cribbageGame.setPileTotal(lastPileTotal);
		
		currentPlayer.setPile(currentPlayerPile);
		otherPlayer.setPile(otherPlayerPile);
		
		
	}

}
