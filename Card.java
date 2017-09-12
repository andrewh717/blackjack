//Author: Andrew Hernandez
public class Card{
  //Card suits (provided for your convenience - use is optional)
  public static final int SPADES   = 1;
  public static final int HEARTS   = 2;
  public static final int CLUBS    = 3;
  public static final int DIAMONDS = 4;

  //Card faces (provided for your convenience - use is optional)
  public static final int ACE      = 1;
  public static final int TWO      = 2;
  public static final int THREE    = 3;
  public static final int FOUR     = 4;
  public static final int FIVE     = 5;
  public static final int SIX      = 6;
  public static final int SEVEN    = 7;
  public static final int EIGHT    = 8;
  public static final int NINE     = 9;
  public static final int TEN      = 10;
  public static final int JACK     = 11;
  public static final int QUEEN    = 12;
  public static final int KING     = 13;


  //define fields here
  private int suitNum;
  private int faceNum;

  //This constructor builds a card with the given suit and face, turned face down.
  public Card(int cardSuit, int cardFace){
    this.suitNum = cardSuit;
    if(cardFace >= 1 && cardFace <= 13){
      this.faceNum = cardFace;
    }
    else{
      IO.reportBadInput();
    }
  }

  //This method retrieves the suit (spades, hearts, etc.) of this card.
  public int getSuit(){
    return suitNum;
  }
  public String getSuitName(){
    String word;
    switch(suitNum){
      case 1:  word = "Spades";
               break;
      case 2:  word = "Hearts";
               break;
      case 3:  word = "Clubs";
               break;
      case 4:  word = "Diamonds";
               break;
      default: word = "Invalid card";
               break;
    }
    return word;
  }

  //This method retrieves the face (ace through king) of this card.
  public int getFace(){
    return faceNum;
  }
  public String getFaceName(){
    String word;
    switch(faceNum){
      case 1:  word = "Ace";
               break;
      case 2:  word = "Two";
               break;
      case 3:  word = "Three";
               break;
      case 4:  word = "Four";
               break;
      case 5:  word = "Five";
               break;
      case 6:  word = "Six";
               break;
      case 7:  word = "Seven";
               break;
      case 8:  word = "Eight";
               break;
      case 9:  word = "Nine";
               break;
      case 10: word = "Ten";
               break;
      case 11: word = "Jack";
               break;
      case 12: word = "Queen";
               break;
      case 13: word = "King";
               break;
      default: word = "Invalid card";
               break;
    }
    return word;
  }
  public String toString(){
     return this.getFaceName() + " of " + this.getSuitName();
  }

  //This method retrieves the numerical value of this card
  //(usually same as card face, except 1 for ace and 10 for jack/queen/king)
  public int getValue(){
    if((faceNum != 1) && (faceNum <= 10)){
      return faceNum;
    }
    else if(faceNum == 1){
      return 11;
    }
    else{
      return 10;
    }
  }
}
