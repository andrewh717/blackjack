//Author: Andrew Hernandez
import java.util.*;
public class Blackjack{
  public static void main(String[] args) {
    boolean gameLoop = false;
    //Main game loop
      System.out.println("Hello! Welcome to the game of Blackjack.\nHow many people will be playing?");
      int numPlayers = IO.readInt();
      while(numPlayers > 6){
        System.out.println("Sorry. You can have 6 players at most. Please re-enter the number of players: ");
        numPlayers = IO.readInt();
      }
      Player[] players = new Player[numPlayers];
      double[] wagers = new double[numPlayers];
      System.out.println("How much money would you like each player to start with?\nEnter the amount as a decimal with no dollar sign:");
      double initialBankroll = IO.readDouble();
      System.out.println("Ok. Now how many decks would you like to use?");
      int numDecks = IO.readInt();
      Deck myDeck = new Deck(numDecks);
      for(int i = 0; i < numPlayers; i++){
        players[i] = new Player(initialBankroll);
      }
      do{
      myDeck.shuffle();
      //Dealer's Turn
      System.out.println("\nDealer's Inital Turn:");
      ArrayList<Card> dealerHand = new ArrayList<Card>();
      int dealerTotalValue = 0;
      boolean dealerBust = false;
      dealerHand.add(myDeck.deal());
      dealerTotalValue += dealerHand.get(0).getValue();
      System.out.println("The dealer's first card is:\n" + dealerHand.get(0).toString());
      boolean[] isBusted = new boolean[numPlayers];
      double[] insurance = new double[numPlayers];
      //Player loop
      for(int i = 0; i < numPlayers; i++){
        isBusted[i] = false;
        insurance[i] = 0;
        System.out.println("Player " + (i+1) + "'s turn:");
        System.out.println("Please input an amount to wager (in decimal form with no dollar sign):");
        wagers[i] = IO.readDouble();
        while((players[i].bankroll - wagers[i]) < 0){
          System.out.println("Sorry, you can't bet more than you've got.\nEnter a lower wager:");
          wagers[i] = IO.readDouble();
        }
        if(players[i].bankrupt() == true){
          System.out.println("Sorry pal, you've got no money.\nYou can't play.");
          break;
        }
        //If the dealer's first card is an ace, allow the player to take insurance
        while(dealerHand.get(0).getFace() == 1){
          System.out.println("The dealer has drawn an ace. Would you like to take insurance before the dealer draws his next card? Enter yes or no:");
          String insuranceChoice = IO.readString();
          if(insuranceChoice.equalsIgnoreCase("yes")){
            System.out.println("How much would you like to bet as insurance? Your maximum is $" + wagers[i]/2.0);
            insurance[i] = IO.readDouble();
            break;
          }
          else if(insuranceChoice.equalsIgnoreCase("no")){
            break;
          }
          else{
            System.out.println("Error. Please enter yes or no:");
          }
        }

        //Dealing the initial 2 cards
        ArrayList<Card> hand = new ArrayList<Card>();
        System.out.println("Here are your first two cards:");
        hand.add(myDeck.deal());
        hand.add(myDeck.deal());
        int sum = 0;
        int aces = 0;
        //Each aces counts as 11 by default
        //To allow for aces = 1, count the number of aces that appear in the hand
        for(int c = 0; c < hand.size(); c++){
          System.out.println(hand.get(c).toString());
          if(hand.get(c).getValue() == 11){
            aces++;
          }
          sum += hand.get(c).getValue();
        }
        int timesAceSubtracted = 0;
        players[i].totalValue = sum;
        System.out.println("Total Value: " + players[i].totalValue);
        int nextHit = 2;

        //Player decisions
        boolean stay = false;
        while(players[i].totalValue <= 21 && stay == false){
          String decision;
          if(nextHit == 2){
            boolean canSplit = false;
            System.out.println("Would you like to hit, stay, or double down?");
            if(hand.get(0).getValue() == hand.get(1).getValue()){
              System.out.println("Since your first two cards have the same value, you may also choose to split.\nWhat is your decision?");
              canSplit = true;
            }
            //The following section offers hints based on static probablities of hand values
            if(players[i].totalValue <= 9){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue == 10 || players[i].totalValue == 11) && players[i].totalValue > dealerTotalValue){
              System.out.println("You should probably double down.");
            }
            else if((players[i].totalValue >= 12 && players[i].totalValue <= 16) && dealerTotalValue >= 7){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue >= 12 && players[i].totalValue <= 16) && dealerTotalValue <= 6){
              System.out.println("You should probably stay.");
            }
            else if(players[i].totalValue >= 17){
              System.out.println("You should probably stay.");
            }
            else if((players[i].totalValue >= 13 && players[i].totalValue <= 18) && (dealerTotalValue == 5 || dealerTotalValue == 6)){
              System.out.println("You should probably double down.");
            }
            else if(players[i].totalValue <= 17){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue == 18) && dealerTotalValue <= 6){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue == 18) && dealerTotalValue >= 7){
              System.out.println("You should probably stay.");
            }
            else if(players[i].totalValue >= 19){
              System.out.println("You should probably stay.");
            }
            else if((hand.get(0).getValue() == hand.get(1).getValue()) && (hand.get(0).getValue() == 1 || hand.get(0).getValue() == 8 || hand.get(0).getValue() == 11)){
              System.out.println("You should probably split.");
            }

            decision = IO.readString();
            if(decision.equalsIgnoreCase("double down")){
              wagers[i] *= 2;
              System.out.println("Doubling your initial wager. Current wager: $" + wagers[i] + "\nYou will automatically stay after receiving your next card.");
              decision = "hit";
              stay = true;
            }

            //The following section is a huge chunk dedicated to the split mechanism
            else if(decision.equalsIgnoreCase("split") && canSplit == true){
              System.out.println("You have chosen to split.\nNow playing the first hand:");
              ArrayList<Card> hand1 = new ArrayList<Card>();
              int sum1 = 0;
              int sum2 = 0;
              int nextHit1 = 2;
              int nextHit2 = 2;
              int aces1 = 0;
              int aces2 = 0;
              int timesAceSubtracted1 = 0;
              int timesAceSubtracted2 = 0;
              double wager1 = wagers[i];
              double wager2 = wagers[i];
              boolean isBusted1 = false;
              boolean isBusted2 = false;
              hand1.add(hand.get(0));
              hand1.add(myDeck.deal());
              for(int c = 0; c < nextHit1; c++){
                System.out.println(hand1.get(c).toString());
                sum1 += hand1.get(c).getValue();
                if(hand1.get(c).getValue() == 11){
                  aces1++;
                }
              }
              System.out.println("Total Value of Hand 1: " + sum1);
              while(sum1 <= 21 && stay == false){
                System.out.println("Would you like to hit or stay?");
                //The following section offers hints based on static probablities of hand values
                if(sum1 <= 9){
                  System.out.println("You should probably hit.");
                }
                else if((sum1 >= 12 && sum1 <= 16) && dealerTotalValue >= 7){
                  System.out.println("You should probably hit.");
                }
                else if((sum1 >= 12 && sum1 <= 16) && dealerTotalValue <= 6){
                  System.out.println("You should probably stay.");
                }
                else if(sum1 >= 17){
                  System.out.println("You should probably stay.");
                }
                else if(sum1 <= 17){
                  System.out.println("You should probably hit.");
                }
                else if((sum1 == 18) && dealerTotalValue <= 6){
                  System.out.println("You should probably hit.");
                }
                else if((sum1 == 18) && dealerTotalValue >= 7){
                  System.out.println("You should probably stay.");
                }
                else if(sum1 >= 19){
                  System.out.println("You should probably stay.");
                }
                String splitDecision = IO.readString();
                if(splitDecision.equalsIgnoreCase("hit")){
                  hand1.add(myDeck.deal());
                  //Reprint the first two cards
                  for(int c = 0; c < nextHit1; c++){
                    System.out.println(hand1.get(c).toString());
                  }
                  //Print the cards received from hitting and add their values to sum
                  System.out.println(hand1.get(nextHit1).toString());
                  sum1 += hand1.get(nextHit1).getValue();
                  if(hand1.get(nextHit1).getValue() == 11){
                    aces1++;
                  }
                  nextHit1++;
                  //Subtract 10 for each ace when total value of hand is over 21
                  //Ensures that 10 is not subtracted more than times than the number of aces
                  if(sum1 > 21){
                    if(aces1 >= 1){
                      if(aces1 > timesAceSubtracted1){
                        sum1 -= 10;
                        timesAceSubtracted1++;
                      }
                      else{
                        System.out.println("Player " + (i+1) + " has busted on hand 1!");
                        isBusted1 = true;
                      }
                    }
                    else{
                      System.out.println("Player " + (i+1) + " has busted on hand 1!");
                      isBusted1 = true;
                    }
                  }
                  players[i].totalValue = sum1;
                  System.out.println("Total Value: " + sum1);
                }
                else if(splitDecision.equalsIgnoreCase("stay")){
                  stay = true;
                }
                else{
                  System.out.println("Error. Please enter hit or stay.");
                }
              }
              System.out.println("Now playing the second hand:");
              ArrayList<Card> hand2 = new ArrayList<Card>();
              hand2.add(hand.get(1));
              hand2.add(myDeck.deal());
              for(int c = 0; c < nextHit2; c++){
                System.out.println(hand2.get(c).toString());
                sum2 += hand2.get(c).getValue();
                if(hand2.get(c).getValue() == 11){
                  aces2++;
                }
              }
              System.out.println("Total Value of Hand 2: " + sum2);
              while(sum2 <= 21 && stay == false){
                System.out.println("Would you like to hit or stay?");
                //The following section offers hints based on static probablities of hand values
                if(sum2 <= 9){
                  System.out.println("You should probably hit.");
                }
                else if((sum2 >= 12 && sum2 <= 16) && dealerTotalValue >= 7){
                  System.out.println("You should probably hit.");
                }
                else if((sum2 >= 12 && sum2 <= 16) && dealerTotalValue <= 6){
                  System.out.println("You should probably stay.");
                }
                else if(sum2 >= 17){
                  System.out.println("You should probably stay.");
                }
                else if(sum2 <= 17){
                  System.out.println("You should probably hit.");
                }
                else if((sum2 == 18) && dealerTotalValue <= 6){
                  System.out.println("You should probably hit.");
                }
                else if((sum2 == 18) && dealerTotalValue >= 7){
                  System.out.println("You should probably stay.");
                }
                else if(sum2 >= 19){
                  System.out.println("You should probably stay.");
                }
                String splitDecision = IO.readString();
                if(splitDecision.equalsIgnoreCase("hit")){
                  hand2.add(myDeck.deal());
                  //Reprint the first two cards
                  for(int c = 0; c < nextHit2; c++){
                    System.out.println(hand2.get(c).toString());
                  }
                  //Print the cards received from hitting and add their values to sum
                  System.out.println(hand2.get(nextHit2).toString());
                  sum1 += hand2.get(nextHit2).getValue();
                  if(hand2.get(nextHit2).getValue() == 11){
                    aces2++;
                  }
                  nextHit2++;
                  //Subtract 10 for each ace when total value of hand is over 21
                  //Ensures that 10 is not subtracted more than times than the number of aces
                  if(sum2 > 21){
                    if(aces2 >= 1){
                      if(aces2 > timesAceSubtracted2){
                        sum2 -= 10;
                        timesAceSubtracted2++;
                      }
                      else{
                        System.out.println("Player " + (i+1) + " has busted on hand 2!");
                        isBusted2 = true;
                      }
                    }
                    else{
                      System.out.println("Player " + (i+1) + " has busted on hand 2!");
                      isBusted2 = true;
                    }
                  }
                  players[i].totalValue = sum2;
                  System.out.println("Total Value: " + sum2);
                }
                else if(splitDecision.equalsIgnoreCase("stay")){
                  stay = true;
                }
                else{
                  System.out.println("Error. Please enter hit or stay.");
                }
              }
            }//End of the giant split section
          }
          else{
            System.out.println("Would you like to hit or stay?");
            if(players[i].totalValue <= 9){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue >= 12 && players[i].totalValue <= 16) && dealerTotalValue >= 7){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue >= 12 && players[i].totalValue <= 16) && dealerTotalValue <= 6){
              System.out.println("You should probably stay.");
            }
            else if(players[i].totalValue >= 17){
              System.out.println("You should probably stay.");
            }
            else if(players[i].totalValue <= 17){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue == 18) && dealerTotalValue <= 6){
              System.out.println("You should probably hit.");
            }
            else if((players[i].totalValue == 18) && dealerTotalValue >= 7){
              System.out.println("You should probably stay.");
            }
            else if(players[i].totalValue >= 19){
              System.out.println("You should probably stay.");
            }
            decision = IO.readString();
          }
          if(decision.equalsIgnoreCase("hit")){
            hand.add(myDeck.deal());
            //Reprint the first two cards
            for(int c = 0; c < nextHit; c++){
              System.out.println(hand.get(c).toString());
            }
            //Print the cards received from hitting and add their values to sum
            System.out.println(hand.get(nextHit).toString());
            sum += hand.get(nextHit).getValue();
            if(hand.get(nextHit).getValue() == 11){
              aces++;
            }
            nextHit++;
            //Subtract 10 for each ace when total value of hand is over 21
            //Ensures that 10 is not subtracted more than times than the number of aces
            if(sum > 21){
              if(aces >= 1){
                if(aces > timesAceSubtracted){
                  sum -= 10;
                  timesAceSubtracted++;
                }
                else{
                  System.out.println("Player " + (i+1) + " has busted!");
                  isBusted[i] = true;
                }
              }
              else{
                System.out.println("Player " + (i+1) + " has busted!");
                isBusted[i] = true;
              }
            }
            players[i].totalValue = sum;
            System.out.println("Total Value: " + sum);
          }
          else if(decision.equalsIgnoreCase("stay")){
            stay = true;
          }
          else{
            System.out.println("Error. Please enter hit or stay.");
          }
        }
      }

      int k = 1;//Keeps track of the hit number so that the value of the first card isn't added to the total value again
      //Dealer hit or stay
      while(dealerTotalValue < 17){
          System.out.println("The dealer has decided to hit.");
          dealerHand.add(myDeck.deal());
          dealerTotalValue += dealerHand.get(k).getValue();
          k++;
          //Reprint the first card
          System.out.println(dealerHand.get(0).toString());
          //Print the cards received from hitting
          for(int d = 1; d < dealerHand.size(); d++){
            System.out.println(dealerHand.get(d).toString());
          }
          for(int t = 0; t < dealerHand.size(); t++){
            if((dealerHand.get(t).getValue() == 11) && (dealerTotalValue > 21)){
              dealerTotalValue -= 10;
            }
          }
          System.out.println("Total Value: " + dealerTotalValue);
      }
      if((dealerTotalValue >= 17) && (dealerTotalValue < 21)){
        System.out.println("The dealer has decided to stay.");
      }
      else if(dealerTotalValue == 21){
        System.out.println("The dealer has Blackjack!");
        for(int i = 0; i < numPlayers; i++){
          players[i].bankroll += (insurance[i] * 2.0);
        }
      }
      else if(dealerTotalValue > 21){
        System.out.println("The dealer has busted!");
        dealerBust = true;
      }

      //Results of the game
      for(int i = 0; i < numPlayers; i++){
        if(isBusted[i] == true){
          System.out.println("Player " + (i+1) + " has busted and therefore lost.");
          players[i].bankroll -= wagers[i];
        }
        else if((isBusted[i] == false) && (dealerBust == false) && (players[i].totalValue < dealerTotalValue)){
          System.out.println("Player " + (i+1) + " has lost to the dealer.");
          players[i].bankroll -= wagers[i];
        }
        else if((isBusted[i] == false) && (dealerBust == false) && (players[i].totalValue == dealerTotalValue)){
          System.out.println("Player " + (i+1) + " has tied with the dealer.");
        }
        else if((isBusted[i] == false) && (dealerBust == false) && (players[i].totalValue > dealerTotalValue)){
          System.out.println("Player " + (i+1) + " has beat the dealer!");
          players[i].bankroll += wagers[i];
        }
        else if((isBusted[i] == false) && (dealerBust == true)){
          System.out.println("Player " + (i+1) + " has beat the dealer because the dealer busted!");
          players[i].bankroll += wagers[i];
        }
      }

      //Ending Player Balances
      System.out.println("\nEnding Balances:");
      for(int i = 0; i < numPlayers; i++){
        System.out.println("Player " + (i+1) + ": $" + players[i].bankroll);
      }

      //Play again?
      System.out.println("Do you want to play again? Enter yes or no:");
      String playAgain = IO.readString();
      if(playAgain.equalsIgnoreCase("yes")){
        gameLoop = true;
        System.out.println("Starting a new round...");
      }
      else if(playAgain.equalsIgnoreCase("no")){
        System.out.println("Exiting. Hope you enjoyed the game!");
        gameLoop = false;
      }
    }while(gameLoop == true);
  }
}
