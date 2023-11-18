import java.util.*;

public class Bot_A implements RoShamBot {
    public ArrayList<Integer> plays;

    public Bot_A() {
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
            return Action.PAPER;
        else if (coinFlip <= movePercentages.get(1))
            return Action.SCISSORS;
        else if (coinFlip <= movePercentages.get(2))
            return Action.SPOCK;
        else if (coinFlip <= movePercentages.get(3))
            return Action.LIZARD;
        else
            return Action.ROCK;
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
            percentages.set(this.plays.get(this.plays.size() - i - 1),
                    percentages.get(this.plays.get(this.plays.size() - i - 1)) + 1);
        }
        double total = 0;
        for (int i = 0; i < percentages.size(); i++) {
            total += (percentages.get(i) / percentages.size());
            percentages.set(i, total);
        }
        return percentages;
    }

    // public static void main(String[] args) {
    // Bot_A testBot = new Bot_A();
    // Action[] arr = new Action[100];
    // for (int i = 0; i < 100; i++) {
    // arr[i] = testBot.getNextMove(Action.ROCK);
    // }
    // }
}