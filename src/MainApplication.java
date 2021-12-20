

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.HashMap;

public class MainApplication {
	public static void main(String[] args) {
		System.out.println("Let's Play Cards!");
		TheTable.PlayCards();
	}
}

class TheTable {

	public static ArrayList<Card> _discardedCards = new ArrayList<>();
	public static Round[] rounds = new Round[3];
	
	public static void createRounds() {
		Round temp = null;
		for(int i=0; i<3; i++) {
			temp = new Round();
			rounds[i] = temp;
		}
	}

	public static void PlayCards() {

		System.out.println("Decide whether to keep on return cards. Get the highest hand value to WIN!\n");
		
		CardDeck.GenerateCardDeck();
		// I now have a card deck

		GamePlayers p = new GamePlayers();
		GamePlayers.GeneratePlayers();
		// I now have Card Players with Hands of Cards: use the Debugger to verify that you can see this
		// my Players have a calcuatel card value Method
		
		TheTable.createRounds();

		for(int i=0; i<TheTable.rounds.length; i++) {
			System.out.println("\nROUND " + (i+1) );
			rounds[i].cardExchange();
			System.out.println("ROUND " + (i+1) + " FINISHED");
		}


		System.out.println("\n\nLET'S FIND OUT WHO WON\n");
		DeclareWinner(p);

	}

	public static void DeclareWinner(GamePlayers p) {
		int highestValue = 0;
		Player winner = null;
		for (Player playerName : GamePlayers.players) {
			int playerScore = playerName.getTotalHandValue();
			if (highestValue == 0 || highestValue < playerScore ) {
				highestValue = playerScore;
				winner = playerName;
			}
		}
		System.out.println("\n\nThe winner is " + winner.PlayerName + " with a TOTAL winning hand value of " + highestValue );
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
		players.add(new Player("Bill"));
		players.add(new Player("Mary"));
		players.add(new Player("Steve"));
		players.add(new Player("Susan"));
	}

}

class Player {
	Scanner keyboard = new Scanner(System.in);
	int score;
	public Player(String playerName) {
		PlayerName = playerName;
		setOfCards = new Hand();
		this.setHand();
		this.score = 0;
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
		this.score += HandValue; // for each round, update player score
		return HandValue;
	}

	public void setHand() {  // issueHand() method separately during each round instead of during player creation
		playersCardSet = setOfCards.IssueHand(); // copies reference to the list PlayersHand cards from Hand object
		System.out.println("Cards issued to player " + this.PlayerName);
	}

	public int getTotalHandValue() {
		return this.score;
	}
	
	public void ViewCards() {
		for (Card element : playersCardSet) {
			System.out.print(element + " \t ");
		}
		System.out.println();
	}


	public void returnCardsConfirmation() {
		String response; // this.cardsToSurrender.nextLine();

		System.out.println("ReturnCardsConfirmation");
		
		for (int i = this.playersCardSet.size()-1 ; 3 <= this.playersCardSet.size();  i-- ) {
			if (i < 0 ) { break;}
			System.out.println("Do you want to return this card? "+ this.playersCardSet.get(i));
			System.out.print("Type 'yes' to RETURN a card, or any other key to keep card: ");
			response = this.keyboard.nextLine();
			if (response.equals("yes")) {
				Card card_to_remove = this.playersCardSet.remove(i);
				TheTable._discardedCards.add(card_to_remove); // removed from hand, returned to table
				Round.returnedCards ++ ;
				System.out.println("Card " + card_to_remove +  " removed. Returned cards: " + Round.returnedCards);
			} else {
				System.out.println("Keeping card "+ this.playersCardSet.get(i));
				continue;
			}
		}
	}
	
	public void recieveNewCards() {
		System.out.println("recieveNewCards equivalent to Returned cards: " + Round.returnedCards);
		for (int i = 0; i < Round.returnedCards ; i++ ) {
			this.playersCardSet.add(CardDeck._cardDeck.remove(0));
		}
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
		return PlayersHand; // returned to player object
	}

}

class Round{
	public static int roundNumber=0;
	private int highestScore ;
	ArrayList<Player> RoundWinners; //
	public static int returnedCards = 0;

	public Round() {
		roundNumber++;
	}

	

	public void cardExchange() {
		/* In each round: Player may give 1, 2, or 3 cards  back to the Table (dealer) : and receive back an equivalent
		 * number of cards
		*/
		System.out.println("\nGive cards and get the same number of cards in return");

		for (var i = 0 ; i < GamePlayers.players.size(); i++ ) {
			returnedCards = 0;
			System.out.println("\n\nView cards of " + GamePlayers.players.get(i).PlayerName );

			System.out.println("Return cards, " + i);
			GamePlayers.players.get(i).ViewCards();
			GamePlayers.players.get(i).returnCardsConfirmation();
			System.out.println("\n\nEvaluate AFTER returnCardsConfirmation:");
			GamePlayers.players.get(i).ViewCards();
			GamePlayers.players.get(i).recieveNewCards();
			System.out.println("\n\nEvaluate AFTER recieveNewCards");
			GamePlayers.players.get(i).ViewCards();
			GamePlayers.players.get(i).CalculateHandValue(); // calculate at the very end of the round;
		}
	}

}
