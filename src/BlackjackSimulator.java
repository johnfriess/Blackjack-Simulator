import java.util.*;

public class BlackjackSimulator { //program does not take into account cards currently in play
    static int[] card;
    public static void main(String[] args) { //dealer must stand on 17 and draw to 16
        Scanner in = new Scanner(System.in);
        System.out.println("What weight on player bust potential compared to dealer bust potential?");
        double val = in.nextDouble()/100.0; double val2 = 1-val;
        card = new int[13];
        for(int i = 0; i < card.length; i++) {
            if(i < 9 && i != 0)
                card[i] = i+1;
            else if(i > 9)
                card[i] = 10;
            else
                card[i] = 11;
        }

        int win = 0; int lose = 0;
        for(int j = 0; j < 10000; j++) {
            System.out.println(j);
            int p1 = card[(int) ((Math.random() * 13))];
            int p2 = card[(int) ((Math.random() * 13))];
            int d1 = card[(int) ((Math.random() * 13))];
            int d2 = card[(int) ((Math.random() * 13))]; //unknown to player

            ArrayList<Card> player = new ArrayList<>();
            ArrayList<Card> dealer = new ArrayList<>();
            player.add(new Card(p1)); player.add(new Card(p2));
            dealer.add(new Card(d1)); dealer.add(new Card(d2));

            int index = 2;
            int busted = 0;
            int sum = p1 + p2;
            int dSum = d1 + d2;
            double pP;
            double dP = dealerBust(d1);

            if (sum > 21) {
                sum -= 10;
                busted++;
            }

            if(sum < 12)
                pP = 0;
            else {
                pP = (sum - 8)/13.0;
            }

            if (d1 != 11)
                System.out.println("Dealer Hand: " + d1 + " ?");
            else
                System.out.println("Dealer Hand: 1/11 ?");
            displayHand(player, "Player", sum, true);

            while (sum != 21) {
                String move = whatMove(pP, dP, val, val2, sum, occur(player));
                int times;
                if (move.equals("H")) {
                    hit(player);
                    sum += player.get(index).value;
                    index++;
                    if(sum < 12)
                        pP = 0;
                    else {
                        pP = (sum - 8)/13.0;
                    }
                    times = occur(player) - busted;
                    while (sum > 21) {
                        busted++;
                        System.out.println("times: " + times);
                        if (times > 0) {
                            sum -= 10;
                        } else {
                            displayHand(dealer, "Dealer", dSum, false);
                            displayHand(player, "Player", sum, true);
                            System.out.println();
                            break;
                        }
                    }
                    if(sum > 21)
                        break;
                } else if (move.equals("S")) {
                    break;
                } else {
                    System.out.println("Please enter H or S");
                }
                System.out.println("Dealer Hand: " + d1 + " ?");
                displayHand(player, "Player", sum, true);
            }
            System.out.println();
            busted = 0;
            index = 2;

            if (sum > 21) {
                System.out.println();
                System.out.println("You lose. Dealer wins.");
                lose++;
            } else {
                displayHand(dealer, "Dealer", dSum, true);
                displayHand(player, "Player", sum, true);
                while (dSum < 17) {
                    hit(dealer);
                    dSum += dealer.get(index).value;
                    index++;
                    int times = occur(dealer) - busted;
                    while (dSum > 21) {
                        busted++;
                        if (times > 0) {
                            dSum -= 10;
                        } else {
                            break;
                        }
                    }
                    System.out.println();
                    displayHand(dealer, "Dealer", dSum, true);
                    displayHand(player, "Player", sum, true);
                }
                System.out.println();
                if (dSum > 21) {
                    System.out.println("You win. Dealer loses.");
                    win++;
                } else if (dSum > sum) {
                    System.out.println("You lose. Dealer wins.");
                    lose++;
                } else if (dSum == sum) {
                    System.out.println("Draw.");
                } else {
                    System.out.println("You win. Dealer loses.");
                    win++;
                }
            }
            System.out.println();
        }
        System.out.println("Win: " + win + "     Lose: " + lose);
        double winrate = ((double)win/(win+lose))*100;
        System.out.println("Win rate: " + winrate);
    }

    public static double dealerBust(int c) { //Use values from https://www.blackjackonline.com/strategy/dealer-probabilities/
        switch(c) {
            case 2:
                return .353;
            case 3:
                return .374;
            case 4:
                return .396;
            case 5:
                return .419;
            case 6:
                return .423;
            case 7:
                return .262;
            case 8:
                return .243;
            case 9:
                return .23;
            case 10:
                return .213;
            default:
                return .116;
        }
    }

    public static void hit(ArrayList<Card> cards) {
        cards.add(new Card(card[(int)((Math.random() * 13))]));
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
        public Card(int val) {
            value = val;
        }
    }

    public static String whatMove(double pP, double dP, double val, double val2, int sum, int occ) {
        if(occ > 0 && sum < 17) {
            return "H";
        }
        double v1 = pP*val;
        System.out.println("v1: " + v1);
        double v2 = dP*val2;
        System.out.println("v2: " + v2);
        if(v1 > v2) {
            return "S";
        }
        else {
            return "H";
        }
    }
}
