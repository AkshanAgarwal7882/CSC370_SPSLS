import java.util.*;

public class Bot_B implements RoShamBot {
    public ArrayList<Integer> oppPlays;
    public ArrayList<Integer> history;
    public ArrayList<Integer> ownPlays;
    public ArrayList<Integer> best;
    public ArrayList<Integer> bestLength;

    public Bot_B() {
        this.oppPlays = new ArrayList<Integer>();
        this.history = new ArrayList<Integer>();
        this.ownPlays = new ArrayList<Integer>();
        this.best = new ArrayList<Integer>();
        this.bestLength = new ArrayList<Integer>();
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
        if (oppPlays.size() < 5) {
            movePercentages.clear();
            for (int i = 0; i < 5; i++) {
                movePercentages.add(0.2 * (i + 1));
            }
        } else {
            movePercentages = movePercentages(5);
        }
        if (oppPlays.size() >= 5 && this.bestLength.get(0) > 0) {
            if (1 - 1 / (Math.pow((double) 5, (double) this.bestLength.get(0))) < 0.2) {
                return willLoseTo(this.ownPlays.get(this.best.get(0) + 1));
            }
        }
        if (oppPlays.size() >= 5 && this.bestLength.get(1) > 0) {
            if (1 - 1 / (Math.pow((double) 5, (double) this.bestLength.get(1))) < 0.2) {
                return returnMove(this.oppPlays.get(this.best.get(1) + 1));
            }
        }
        if (oppPlays.size() >= 5 && this.bestLength.get(2) > 0 && oppPlays.size() < 5) {
            if (1 - 1 / (Math.pow((double) 5, (double) this.bestLength.get(2))) < 0.35) {
                return returnMove(this.oppPlays.get(this.best.get(2) + 1));
            }
        }
        if (coinFlip <= movePercentages.get(0)) {
            this.ownPlays.add(0);
            return Action.ROCK;
        } else if (coinFlip <= movePercentages.get(1)) {
            this.ownPlays.add(1);
            return Action.PAPER;
        } else if (coinFlip <= movePercentages.get(2)) {
            this.ownPlays.add(2);
            return Action.SCISSORS;
        } else if (coinFlip <= movePercentages.get(3)) {
            this.ownPlays.add(3);
            return Action.LIZARD;
        } else {
            this.ownPlays.add(4);
            return Action.SPOCK;
        }
    }

    public Action returnMove(int play) {
        switch (play) {
            case 0:
                this.ownPlays.add(0);
                return Action.ROCK;
            case 1:
                this.ownPlays.add(1);
                return Action.PAPER;
            case 2:
                this.ownPlays.add(2);
                return Action.SCISSORS;
            case 3:
                this.ownPlays.add(3);
                return Action.LIZARD;
            default:
                this.ownPlays.add(4);
                return Action.SPOCK;
        }
    }

    public Action willLoseTo(int play) {
        switch (play) {
            case 0:
                this.ownPlays.add(3);
                return Action.LIZARD;
            case 1:
                this.ownPlays.add(0);
                return Action.ROCK;
            case 2:
                this.ownPlays.add(1);
                return Action.PAPER;
            case 3:
                this.ownPlays.add(4);
                return Action.SPOCK;
            default:
                this.ownPlays.add(2);
                return Action.SCISSORS;
        }
    }

    public void addOpponentMove(Action lastOpponentMove) {
        switch (lastOpponentMove) {
            case ROCK:
                this.oppPlays.add(0);
                break;
            case PAPER:
                this.oppPlays.add(1);
                break;
            case SCISSORS:
                this.oppPlays.add(2);
                break;
            case LIZARD:
                this.oppPlays.add(3);
                break;
            default:
                this.oppPlays.add(4);
                break;
        }
    }

    public ArrayList<Double> movePercentages(int numMoves) {
        ArrayList<Double> percentages = new ArrayList<Double>();
        if (losingBad()) {
            for (int i = 0; i < 5; i++) {
                percentages.add(0.2 * (i + 1));
            }
            return percentages;
        }
        for (int i = 0; i < numMoves; i++) {
            percentages.add(0.0);
        }
        for (int i = 0; i < numMoves; i++) {
            switch (this.oppPlays.get(this.oppPlays.size() - i - 1)) {
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

    public int matchSingle(int index, ArrayList<Integer> arr) {
        int lowptr = index;
        int highptr = arr.size() - 1;
        while (lowptr >= 0 && arr.get(lowptr) == arr.get(highptr)) {
            lowptr--;
            highptr--;
        }
        return arr.size() - highptr - 1;
    }

    public int matchBoth(int index) {
        int j = 0;
        // int end = this.oppPlays.size() - 2;
        // for (j = 0; j <= index
        // && this.oppPlays.get(end - j - 1) == this.oppPlays.get(index - j - 1)
        // && this.ownPlays.get(end - j) == this.ownPlays.get(index - j); j++) {
        // }
        // ;
        return j;
    }

    public void doHistory(int age) {
        int size = this.ownPlays.size();
        int i, j;
        ;
        for (int w = 0; w < 3; w++) {
            this.best.add(0);
            this.bestLength.add(0);
        }
        for (i = size - 2; i > size - age && i > this.bestLength.get(0); i--) {
            j = matchSingle(i, this.ownPlays);
            if (j > this.bestLength.get(0)) {
                this.bestLength.set(0, j);
                this.best.set(0, i);
                if (j > size / 2)
                    break;
            }
        }

        for (i = size - 1; i > size - age - 1 && i > this.bestLength.get(1); i--) {
            j = matchSingle(i, this.oppPlays);
            if (j > this.bestLength.get(1)) {
                this.bestLength.set(1, j);
                this.best.set(1, i);
                if (j > size / 2)
                    break;
            }
        }

        for (i = size - 2; i > size - age && i > bestLength.get(2); i--) {
            j = matchBoth(i);
            if (j > bestLength.get(2)) {
                bestLength.set(2, j);
                this.best.set(2, i);
                if (j > size / 2)
                    break;
            }
        }
    }

    public boolean losingBad() {
        this.best.clear();
        this.bestLength.clear();
        doHistory(this.ownPlays.size());
        if (this.ownPlays.size() >= 1)

        {
            this.history.add(
                    play(this.ownPlays.get(this.ownPlays.size() - 1), this.oppPlays.get(this.oppPlays.size() - 1)));
        }
        int score = 0;
        for (int i = 1; i <= this.history.size(); i++) {
            score += this.history.get(this.history.size() - i);
        }
        if (score < -2) {
            return true;
        }
        return false;
    }

    public int play(int ownPlay, int oppPlay) {
        switch (oppPlay) {
            case 0:
                if (ownPlay == 1 | ownPlay == 4) {
                    return 1;
                } else if (ownPlay == 0) {
                    return 0;
                } else {
                    return -1;
                }
            case 1:
                if (ownPlay == 2 | ownPlay == 3) {
                    return 1;
                } else if (ownPlay == 1) {
                    return 0;
                } else {
                    return -1;
                }
            case 2:
                if (ownPlay == 0 | ownPlay == 4) {
                    return 1;
                } else if (ownPlay == 2) {
                    return 0;
                } else {
                    return -1;
                }
            case 3:
                if (ownPlay == 0 | ownPlay == 2) {
                    return 1;
                } else if (ownPlay == 3) {
                    return 0;
                } else {
                    return -1;
                }
            default:
                if (ownPlay == 1 | ownPlay == 3) {
                    return 1;
                } else if (ownPlay == 4) {
                    return 0;
                } else {
                    return -1;
                }

        }
    }

    // public static void main(String[] args) {
    // Bot_B testBot = new Bot_B();
    // Action[] arr = new Action[100];
    // for (int i = 0; i < 100; i++) {
    // arr[i] = testBot.getNextMove(Action.ROCK);
    // }
    // }
}