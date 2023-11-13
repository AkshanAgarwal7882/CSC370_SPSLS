import java.util.*;

public class Bot_B implements RoShamBot {
    public ArrayList<Integer> plays;

    public Bot_B() {
        this.plays = new ArrayList<Integer>();
    }

    /**
     * Returns an action based on the most recently-played actions of the opponent
     * 
     * @param lastOpponentMove the action that was played by the opponent on
     *                         the last round
     * @return the next action to play.
     */
    public Action getNextMove(Action lastOpponentMove) {
        addOpponentMove(lastOpponentMove);
        ArrayList<Double> movePercentages = new ArrayList<Double>();
        double coinFlip = Math.random();
        if (plays.size() < 5) {
            movePercentages.clear();
            for (int i = 0; i < 5; i++) {
                movePercentages.add(0.2 * (i + 1));
            }
        } else {
            movePercentages = movePercentages(5);

        }

        if (coinFlip <= movePercentages.get(0))
            return Action.ROCK;
        else if (coinFlip <= movePercentages.get(1))
            return Action.PAPER;
        else if (coinFlip <= movePercentages.get(2))
            return Action.SCISSORS;
        else if (coinFlip <= movePercentages.get(3))
            return Action.LIZARD;
        else
            return Action.SPOCK;
    }

    public void addOpponentMove(Action lastOpponentMove) {
        switch (lastOpponentMove) {
            case ROCK:
                this.plays.add(0);
                break;
            case PAPER:
                this.plays.add(1);
                break;
            case SCISSORS:
                this.plays.add(2);
                break;
            case LIZARD:
                this.plays.add(3);
                break;
            default:
                this.plays.add(4);
                break;
        }
    }

    public ArrayList<Double> movePercentages(int numMoves) {
        ArrayList<Double> percentages = new ArrayList<Double>();
        for (int i = 0; i < numMoves; i++) {
            percentages.add(0.0);
        }
        for (int i = 0; i < numMoves; i++) {
            switch (this.plays.get(this.plays.size() - i - 1)) {
                case 0:
                    percentages.set(1, percentages.get(1) + 1);
                    percentages.set(4, percentages.get(4) + 1);
                    break;
                case 1:
                    percentages.set(2, percentages.get(2) + 1);
                    percentages.set(3, percentages.get(3) + 1);
                    break;
                case 2:
                    percentages.set(0, percentages.get(0) + 1);
                    percentages.set(4, percentages.get(4) + 1);
                    break;
                case 3:
                    percentages.set(0, percentages.get(0) + 1);
                    percentages.set(2, percentages.get(2) + 1);
                    break;
                case 4:
                    percentages.set(1, percentages.get(1) + 1);
                    percentages.set(3, percentages.get(3) + 1);
                    break;
                default:
                    break;
            }
        }
        double total = 0;
        for (int i = 0; i < percentages.size(); i++) {
            total += (percentages.get(i) / (percentages.size() * 2));
            percentages.set(i, total);
        }
        return percentages;
    }

    // public static void main(String[] args) {
    // Bot_B testBot = new Bot_B();
    // Action[] arr = new Action[100];
    // for (int i = 0; i < 100; i++) {
    // arr[i] = testBot.getNextMove(Action.ROCK);
    // }
    // }
}