import java.util.ArrayList;
import java.util.Collections;

/**
 * Defines the player class. This includes the hand, any books placed on the table, and a name.
 * @author jburge
 *
 */
public class Player {

	private String name;
	private ArrayList<PlayingCard> myHand;
	private Pile myPile;
	private int points;
	private boolean passed;

	/**
	 * Creates a Player and gives them a name
	 * @param name
	 */
	public Player(String name)
	{
		this.name = name;
		myHand = new ArrayList<PlayingCard>();
		myPile = new Pile();
		points = 0;
		passed = false;
	}

	/**
	 * Resets the player for a new hand
	 */
	public void resetForNewHand()
	{
		myHand = new ArrayList<PlayingCard>();
		myPile = new Pile();
		passed = false;
	}

	/**
	 * Gets player name
	 * @return name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Gets player hand
	 * @return hand
	 */
	public ArrayList<PlayingCard> getHand()
	{
		return myHand;
	}
	
	/**
	 * Sets player hand
	 * @param newHand
	 */
	public void setHand( ArrayList<PlayingCard> newHand) {
		myHand = newHand;
	}

	/**
	 * Returns true if passed
	 * @return passed
	 */
	public boolean hasPassed() {
		return passed;
	}

	/**
	 * Sets passed
	 * @param passed
	 */
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	
	/**
	 * Gets passed
	 * @return passed
	 */
	public boolean getPassed() {
		return passed;
	}

	/**
	 * Gets active pile
	 * @return active pile
	 */
	public ArrayList<PlayingCard>getActivePile()
	{
		return myPile.getActivePile();
	}

	/**
	 * Returns the number of cards held in the player's hand
	 * @return number of cards
	 */
	public int numCards()
	{
		return myHand.size();
	}


	/**
	 * Add new points to the player's hand
	 */
	public void addPoints(int points)
	{
		this.points += points;
	}

	/**
	 * Subtracts points (for undo)
	 * @param points
	 */
	public void subtractPoints(int points)
	{
		this.points -= points;
	}

	/**
	 * Gets points
	 * @return points
	 */
	public int getPoints()
	{
		return this.points;
	}
	/**
	 * Draws a card from the deck and places it in the player's hand
	 * @param deck the deck of cards
	 * @return the card dealt
	 */
	public PlayingCard draw(Deck deck)
	{
		PlayingCard cardDealt = deck.draw();
		myHand.add(cardDealt);
		//Sort the hand
		Collections.sort(myHand);
		return cardDealt;
	}

	/**
	 * Puts a card back in the player's hand
	 * @param card the card being added
	 */
	public void replaceCard(PlayingCard card)
	{
		myHand.add(card);
		myPile.removeCard();
	}

	/**
	 * Returns the cards in the pile to the player's hand
	 */
	public void returnPile()
	{
		this.myPile.returnPile(this.myHand);
	}

	/**
	 * Starts a new pile
	 */
	public void newPile()
	{
		myPile.newPile();
	}

	/**
	 * Plays a card by adding it to the pile. This requires calculating pile values
	 * @param card
	 */
	public void playCard(PlayingCard card)
	{
		myHand.remove(card);
		myPile.addCard(card);
	}

	/**
	 * Removes a card from the hand - this is done so it can be added to the crib
	 */
	public void removeCard(PlayingCard card)
	{
		myHand.remove(card);
	}
	/**
	 * Used to create a string representation of the player so it can be printed.
	 */
	public String toString()
	{
		String player = "\n" + name + ":\n";
		player += "Points = " + points + "\n";
		if (myHand.isEmpty())
		{
			player += "No cards left!";
		}
		else
		{
			player += myHand.toString();
		}
		/*  Useful to look at pile for debugging but not so much for game play...
		player += "\nPile:\n";
		player += myPile.toString();
		*/
		return player;
	}
	
	/**
	 * Gets player pile
	 * @return pile
	 */
	public Pile getPile() {
		return myPile;
	}
	
	/**
	 * Sets player pile
	 * @param newPile
	 */
	public void setPile(Pile newPile) {
		myPile = newPile;
	}
	
	/**
	 * Gives the player a card (for testing)
	 * @param new playing card
	 */
	public void giveCard(PlayingCard playingCard) {
		myHand.add(playingCard);
	}



}
