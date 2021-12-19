import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainApplication {
	public static void main(String[] args) {
		System.out.println("Let's Play Cards!");
		TheTable.PlayCards();
	}
}

class TheTable {

	public static ArrayList<Card> _discardedCards = new ArrayList<>();

	public static void PlayCards() {
		CardDeck.GenerateCardDeck();
		// I now have a card deck

		GamePlayers p = new GamePlayers();
		GamePlayers.GeneratePlayers();
		// I now have Card Players with Hands of Cards: use the Debugger to verify that you can see this

		// my Players have a calcuatel card value Method

		// your To Do: For Assignment 8: Is to out WHO the winner player is, based on the cards
		// that the Dealer gave them
		// We will look at additional code formulations later to see how to take and give new cards
		// in additional rounds of Play
		Round round_1 = new Round(p);
		round_1.EvaluateHand();
		System.out.println("VIEW CARDS");
		round_1.CardExchange();
//		System.out.println("\n\nCards left in the deck: " + CardDeck._cardDeck.size() + "\t " + CardDeck._cardDeck );;

		System.out.println("\n\nLET'S FIND OUT WHO WON\n");
		DeclareWinner(p);

	}

	public static void DeclareWinner(GamePlayers p) {
		int highestValue = 0;
		Player winner = null;
		for (Player playerName : GamePlayers.players) {
			int HandValue = playerName.CalculateHandValue();
			//			System.out.println(playerName.PlayerName + " and their hand value: " +  HandValue);
			if (highestValue == 0 || highestValue < HandValue ) {
				highestValue = HandValue;
				winner = playerName;
			}
		}
		System.out.println("\n\nThe winner is " + winner.PlayerName + " with a winning hand value of " + highestValue );
	}
}

class CardDeck {
	public static ArrayList<Card> _cardDeck = new ArrayList<>();
	static String[] suites = { "Clubs", "Diamonds", "Hearts", "Spades" };

	public static void GenerateCardDeck() {
		String[] suites;
		int[] cardValue;
		suites = new String[4];
		suites[0] = "Clubs";
		suites[1] = "Diamonds";
		suites[2] = "Hearts";
		suites[3] = "Spades";

		cardValue = new int[13];
		cardValue[0] = 2;
		cardValue[1] = 3;
		cardValue[2] = 4;
		cardValue[3] = 5;
		cardValue[4] = 6;
		cardValue[5] = 7;
		cardValue[6] = 8;
		cardValue[7] = 9;
		cardValue[8] = 10;
		cardValue[9] = 11;
		cardValue[10] = 12;
		cardValue[11] = 13;
		cardValue[12] = 14;

		for (String suite : suites) {
			for (int element : cardValue) {
				CardDeck._cardDeck.add(new Card(suite, element));
			}
		}

		// shuffle the card deck
		Collections.shuffle(CardDeck._cardDeck);
	}
}

class Card {

	private String suite;
	private int suitevalue; // total points based on suite and card value.
							// Example Diamonds with 2 as multiplier. Diamond 3 is worth 6 points
	private int cardvalue; // or just the card numbers 2-14
	// #TODO: Add to the this Class a VALUE Data Attribute
	// set the VALUE of this Card based on Suite and Card Value

	public Card(String _suite, int _cardvalue) {
		this.suite = _suite;
		this.cardvalue = _cardvalue;
		calculateSuiteValue(this.suite);
	}

	private void calculateSuiteValue(String _suite) {
		// this sets the overall value of each card based on suite
		if (_suite.equals("Clubs")) {
			this.suitevalue = this.cardvalue  * 1; // multiplier based on suite
		} else if (_suite.equals("Diamonds")) {
			this.suitevalue  = this.cardvalue * 2;
		} else if (_suite.equals("Hearts")) {
			this.suitevalue  = this.cardvalue * 3;
		} else {
			this.suitevalue  = this.cardvalue * 4;
		}
	}

	public int getCardValue() {
		return this.cardvalue;
	}

	public int getSuiteValue() {
		return this.suitevalue;
	}

	public String getCardSuite() {
		return this.suite;
	}

	@Override
	public String toString() {
		return this.suite + " " + this.cardvalue;
	}
}

class GamePlayers {
	// contains our list of players
	public GamePlayers() {

	}

	public static ArrayList<Player> players = new ArrayList<>();

	public static void GeneratePlayers() {
		// for (int m = 0; m < 4; m++) {
			players.add(new Player("Bill"));
			players.add(new Player("Mary"));
			players.add(new Player("Steve"));
			players.add(new Player("Susan"));
		//} // removed, otherwise, each player is present 4 times resulting in 16 number of players
	}

}

class Player {
	public Player(String playerName) {
		PlayerName = playerName;
		setOfCards = new Hand();
//		playersCardSet = setOfCards.IssueHand();
	}

	String PlayerName;
	Hand setOfCards; // Hand object
	ArrayList<Card> playersCardSet = new ArrayList<Card>();

	public int CalculateHandValue() {
		int HandValue = 0; // always reset to 0
		System.out.println("Calculate card value of " + this.PlayerName);
		for (Card element : playersCardSet) {
			int suiteValue = element.getSuiteValue();
			HandValue +=	suiteValue;
			System.out.println(element.toString() + ", with suiteValue: " + suiteValue );
		}

		System.out.println(this.PlayerName + " has " + HandValue + " points.\n\n");
		return HandValue;
	}

	public void setHand() {  // issueHand() method separately during each round instead of during player creation
		playersCardSet = setOfCards.IssueHand(); // copies reference to the list PlayersHand cards from Hand object
	}

	public void ViewCards() {
		for (Card element : playersCardSet) {
			System.out.print(element + " \t ");
		}
		System.out.println();
	}

	public void returnCards() {

	}
}

class Hand {
	// the Hand is the cards the player holds

	ArrayList<Card> PlayersHand = new ArrayList<Card>(); // will be copied by player object

	public Hand() {

	}

	public ArrayList<Card> IssueHand() {
		for (int j = 0; j < 5; j++) {
			PlayersHand.add((CardDeck._cardDeck).remove(j));
			// used .remove() instead of .get() so that the cards in the player's hand are not in the deck
			// and no duplicates will occur
		}
		System.out.println("Cards issued to player");
		return PlayersHand; // returned to player object
	}

}

class Round{
	int roundNumber=0;
	private int highestScore ;
	ArrayList<Player> RoundWinners; //
	Scanner keyboard = new Scanner(System.in);

	public Round(GamePlayers p) {
		this.roundNumber++;
	}

	public void EvaluateHand() {
		System.out.println("\nRound #" + this.roundNumber);
		for (Player element : GamePlayers.players) {
			String playerName =  element.PlayerName;
			System.out.println("Issue cards to player " + playerName);
			element.setHand(); // Cards issued to player; pre-requisite to calculating hand value
//			int playerHandValue =
			element.CalculateHandValue();
		}
//		System.out.println();
	}

	public void CardExchange() {
		/* In each round: Player may give 1, 2, or 3 cards  back to the Table (dealer) : and receive back an equivalent
		 * number of cards
		*/
		System.out.println("\nGive cards and get the same number of cards in return");

		for (var i = 0 ; i < GamePlayers.players.size(); i++ ) {
			System.out.println("\n\nView cards of " + GamePlayers.players.get(i).PlayerName );

			GamePlayers.players.get(i).ViewCards();
			System.out.println("Return cards, " + i);
			this.ReturnCardsConfirmation(GamePlayers.players.get(i));
		}
	}

	public void ReturnCardsConfirmation(Player p) {
		String response; // this.cardsToSurrender.nextLine();
		ArrayList<Card> player_cards = p.playersCardSet;
		System.out.println("ReturnCardsConfirmation");
		
		for (var i = player_cards.size()-1 ; 3 <= player_cards.size();  i-- ) {
			if (i < 0 ) { break;}
			System.out.println("player_cards.size(): " + player_cards.size() + " i is " + i);
			System.out.println("Do you want to return this card? "+ player_cards.get(i));
			System.out.print("Type 'yes' to confirm, any other key to keep card: ");
			response = this.keyboard.nextLine();
			if (response.equals("yes")) {
				TheTable._discardedCards.add(player_cards.remove(i)); // removed from hand, returned to table
			} else {
				System.out.println("Keeping card "+ player_cards.get(i));
				continue;
			}
		}
	}
}
