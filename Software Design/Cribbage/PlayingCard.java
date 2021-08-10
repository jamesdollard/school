
public class PlayingCard implements Comparable<PlayingCard> {
	
	public static final int MIN = 1;
	public static final int MAX = 13; 
	public static final int INVALID = -1;
	
	public static final int ACE = 1;
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	
	public static final String HEARTS = "Hearts";
	public static final String CLUBS = "Clubs";
	public static final String DIAMONDS = "Diamonds";
	public static final String SPADES = "Spades";
	
	private static final String types[] = {null, "Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
		"Nine", "Ten", "Jack", "Queen", "King"};
	private static final int value[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};
	
	private String suit;
	private int type;

	/**
	 * Creates playing card giving it a suit and a type
	 * @param suit
	 * @param type
	 */
	public PlayingCard(String suit, int type) {
		super();
		this.suit = suit;
		this.type = type;
	}
	
	/**
	 * Returns card values
	 * @param value
	 * @return card values
	 */
	public static String getName(int value) {
		return types[value];
	}
	
	/**
	 * Determines card type
	 * @param name
	 * @return card type
	 */
	public static int determineType(String name)
	{
		for (int i=1; i < types.length; i ++)
		{
			if (name.toLowerCase().contains(types[i].toLowerCase()))
			{
				return i;
			}	
		}
		return INVALID;
	}
	
	/**
	 * Gets card suit
	 * @return card suit
	 */
	public String getSuit() {
		return suit;
	}
	
	/**
	 * Sets card suit
	 * @param card suit
	 */
	public void setSuit(String suit) {
		this.suit = suit;
	}
	
	/**
	 * Gets card type
	 * @return card type
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Sets card type
	 * @param card type
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * This assumes that an Ace will have a value of 1
	 * @return
	 */
	public int value()
	{
		return value[type];
	}
	
	/**
	 * Checks if card equals a type
	 * @param type
	 * @return boolean
	 */
	public boolean equalsType(int type) {
		return this.type == type;
	}
	
	/**
	 * Checks if card equals another card
	 */
	public boolean equals(Object o) {
		if (o == null)
		{
			return false;
		}
		if (o.getClass() == this.getClass())
		{
			PlayingCard card = (PlayingCard) o;
			if (card.suit.equals(this.suit) && (card.type == this.type))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Prints String representation of card
	 */
	public String toString()
	{
		return types[type] + " of " + suit;
	}

	/**
	 * Takes advantage of comparable by implementing compareTo method
	 */
	@Override
	public int compareTo(PlayingCard o) {
		if ((o == null) || (!(o instanceof PlayingCard)))
		{
			return -100; //kludge
		}
		PlayingCard card = (PlayingCard) o;
		if (this.getType() < card.getType())
		{
			return -1;
		}
		else if (this.getType() == card.getType())
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
}
