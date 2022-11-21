import java.util.*;

public class Blackjack { //program does not take into account cards currently in play
    static int[] card;
    public static void main(String[] args) { //dealer must stand on 17 and draw to 16
        Scanner in = new Scanner(System.in);
        card = new int[13];
        for(int i = 0; i < card.length; i++) {
            if(i < 9 && i != 0)
                card[i] = i+1;
            else if(i > 9)
                card[i] = 10;
            else
                card[i] = 11;
        }
        int p1 = card[(int)((Math.random() * 13))];
        int p2 = card[(int)((Math.random() * 13))];
        int d1 = card[(int)((Math.random() * 13))];
        int d2 = card[(int)((Math.random() * 13))]; //unknown to player

        p1 = 11;
        p2 = 11;
        d1 = 10;
        d2 = 11;
        boolean stand = false;
        ArrayList<Card> player = new ArrayList<>();
        ArrayList<Card> dealer = new ArrayList<>();
        player.add(new Card(p1)); player.add(new Card(p2));
        dealer.add(new Card(d1)); dealer.add(new Card(d2));

        int index = 2;
        int busted = 0;
        int sum = p1 + p2;
        int dSum = d1 + d2;

        if(sum > 21) {
            sum -= 10;
            busted++;
        }

        if(d1 != 11)
            System.out.println("Dealer Hand: " + d1 + " ?");
        else
            System.out.println("Dealer Hand: 1/11 ?");
        displayHand(player,"Player", sum, true);


        while(!stand && sum != 21) {
            System.out.println("H or S?");
            String move = in.next();
            int times = occur(player) - busted;
            if(move.equals("H")) {
                //hit(player);
                player.add(new Card(11));
                sum += player.get(index).value;
                index++;
                while(sum > 21) {
                    busted++;
                    if(times > 0) {
                        sum -= 10;
                    }
                    else {
                        displayHand(dealer, "Dealer", dSum, false);
                        displayHand(player,"Player", sum, true);
                        System.out.println();
                        break;
                    }
                }
            }
            else if(move.equals("S")) {
                stand = true;
                break;
            }
            else {
                System.out.println("Please enter H or S");
            }
            System.out.println("Dealer Hand: " + d1 + " ?");
            displayHand(player, "Player", sum, true);
        }
        System.out.println();
        busted = 0;
        index = 2;
        if(sum > 21) {
            System.out.println();
            System.out.println("You lose. Dealer wins.");
        }
        else {
            //if(sum < 21)
            displayHand(dealer, "Dealer", dSum, true);
            displayHand(player, "Player", sum, true);
            while(dSum < 17) {
                hit(dealer);
                dSum += dealer.get(index).value;
                index++;
                int times = occur(dealer) - busted;
                while(dSum > 21) {
                    busted++;
                    if (times > 0) {
                        dSum -= 10;
                    }
                    else {
                        break;
                    }
                }
                System.out.println();
                displayHand(dealer, "Dealer", dSum, true);
                displayHand(player, "Player", sum, true);
            }
            System.out.println();
            if(dSum > 21) {
                System.out.println("You win. Dealer loses.");
            }
            else if(dSum > sum) {
                System.out.println("You lose. Dealer wins.");
            }
            else if(dSum == sum) {
                System.out.println("Draw.");
            }
            else {
                System.out.println("You win. Dealer loses.");
            }
        }
        int pSum = p1 + p2;
        int pDiff = 21 - pSum;
        int dDiff = 21 - dSum;

        //double pP = pDiff / 13.0;
        //double dP = card[pSum - d1];
    }

    public static void hit(ArrayList<Card> cards) {
        cards.add(new Card(card[(int)((Math.random() * 13))]));
    }

    public static int sum(ArrayList<Card> cards) {
        int sum = 0;
        for(int i = 0; i < cards.size(); i++) {
            sum += cards.get(i).value;
        }
        return sum;
    }

    public static void displayHand(ArrayList<Card> cards, String user, int sum, boolean show) {
        System.out.print(user + " Hand: ");
        for(Card x: cards) {
            if(x.value != 11)
                System.out.print(x.value + " ");
            else {
                System.out.print("1/11 ");
            }
        }
        if(show)
            System.out.print("= " + sum);
        System.out.println();
    }

    public static int occur(ArrayList<Card> cards) {
        int c = 0;
        for(int i = 0; i < cards.size(); i++) {
            if(cards.get(i).value == 11) {
                c++;
            }
        }
        return c;
    }

    static class Card {
        int value;
        int value2;
        boolean ace = false;
        public Card(int val) {
            value = val;
            if(val == 11) {
                value2 = 1;
                ace = true;
            }
        }
    }
}
