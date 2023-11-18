import java.util.*;

public class Modified_IP implements RoShamBot {
    private static final int MY_HIST = 0;
    private static final int OPP_HIST = 1;
    private static final int BOTH_HIST = 2;
    public ArrayList<Integer> plays;
    public ArrayList<Integer> ownPlays;
    public ArrayList<Integer> opponentPlays;
    int ages[] = { 1000, 100, 10, 5, 2, 1 };
    int numAges = 6;

    public Modified_IP() {
        this.plays = new ArrayList<Integer>();
        this.ownPlays = new ArrayList<Integer>();
        this.opponentPlays = new ArrayList<Integer>();
        // initStats();
    }

    public class Stats {
        public ArrayList<ArrayList<Integer>> sum;
        public int age;
    }

    public class Predict {
        public Stats stats;
        int last;
    }

    public class Iocaine {
        Predict pr_history[][][], pr_freq[][];
        Predict pr_fixed, pr_random, pr_meta[];
        Stats stats[];
    }

    // public void initStats() {
    // ArrayList<Integer> initList = new ArrayList<Integer>();
    // for (int i = 0; i <= 2; i++) {
    // initList.add(0);
    // }
    // stats.sum.add(initList);
    // }

    public int willBeat(int play) {
        double coinFlip = Math.random();
        switch (play) {
            case 0:
                if (coinFlip <= 0.5) {
                    return 2;
                } else {
                    return 3;
                }
            case 1:
                if (coinFlip <= 0.5) {
                    return 0;
                } else {
                    return 4;
                }
            case 2:
                if (coinFlip <= 0.5) {
                    return 1;
                } else {
                    return 3;
                }
            case 3:
                if (coinFlip <= 0.5) {
                    return 1;
                } else {
                    return 4;
                }
            default:
                if (coinFlip <= 0.5) {
                    return 0;
                } else {
                    return 2;
                }
        }
    }

    public int willLoseTo(int play) {
        double coinFlip = Math.random();
        switch (play) {
            case 0:
                if (coinFlip <= 0.5) {
                    return 1;
                } else {
                    return 4;
                }
            case 1:
                if (coinFlip <= 0.5) {
                    return 2;
                } else {
                    return 3;
                }
            case 2:
                if (coinFlip <= 0.5) {
                    return 0;
                } else {
                    return 4;
                }
            case 3:
                if (coinFlip <= 0.5) {
                    return 0;
                } else {
                    return 2;
                }
            default:
                if (coinFlip <= 0.5) {
                    return 1;
                } else {
                    return 3;
                }
        }
    }

    /**
     * Returns an action based on the most recently-played actions of the opponent
     *
     * @param lastOpponentMove the action that was played by the opponent on
     *                         the last round
     * @return the next action to play.
     */
    public Action getNextMove(Action lastOpponentMove) {
        updatePlays(lastOpponentMove, this.opponentPlays);
        // ArrayList<Double> movePercentages = new ArrayList<Double>();
        // double coinFlip = Math.random();
        // if (opponentPlays.size() < 5) {
        // movePercentages.clear();
        // for (int i = 0; i < 5; i++) {
        // movePercentages.add(0.2 * (i + 1));
        // }
        // } else {
        // movePercentages = movePercentages(5);
        // }

        // if (coinFlip <= movePercentages.get(0))
        // return addOwnMove(Action.ROCK);
        // else if (coinFlip <= movePercentages.get(1))
        // return addOwnMove(Action.PAPER);
        // else if (coinFlip <= movePercentages.get(2))
        // return addOwnMove(Action.SCISSORS);
        // else if (coinFlip <= movePercentages.get(3))
        // return addOwnMove(Action.LIZARD);
        // else
        // return addOwnMove(Action.SPOCK);
        updatePlays(Action.ROCK, this.ownPlays);
        return Action.ROCK;
    }

    public void updatePlays(Action move, ArrayList<Integer> moveArray) {
        switch (move) {
            case ROCK:
                this.plays.add(0);
                moveArray.add(0);
                break;
            case PAPER:
                this.plays.add(1);
                moveArray.add(1);
                break;
            case SCISSORS:
                this.plays.add(2);
                moveArray.add(2);
                break;
            case LIZARD:
                this.plays.add(3);
                moveArray.add(3);
                break;
            default:
                this.plays.add(4);
                moveArray.add(4);
                break;
        }
    }

    public ArrayList<Double> movePercentages(int numMoves) {
        ArrayList<Double> percentages = new ArrayList<Double>();
        for (int i = 0; i < numMoves; i++) {
            percentages.add(0.0);
        }
        for (int i = 0; i < numMoves; i++) {
            switch (this.opponentPlays.get(this.opponentPlays.size() - i - 1)) {
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
        int end = this.opponentPlays.size() - 1;
        for (j = 0; j <= index
                && this.opponentPlays.get(end - j) == this.opponentPlays.get(index - j)
                && this.ownPlays.get(end - j) == this.ownPlays.get(index - j); j++) {
        }
        ;
        return j;
    }

    public void doHistory(int age) {
        int size = this.ownPlays.size();
        ArrayList<Integer> best = new ArrayList<Integer>();
        ArrayList<Integer> bestLength = new ArrayList<Integer>();
        int i, j;
        ;
        for (int w = 0; w < 3; w++) {
            best.add(0);
            bestLength.add(0);
        }
        for (i = size - 2; i > size - age && i > bestLength.get(MY_HIST); i--) {
            j = matchSingle(i, this.ownPlays);
            if (j > bestLength.get(MY_HIST)) {
                bestLength.set(MY_HIST, j);
                best.set(MY_HIST, i);
                if (j > size / 2)
                    break;
            }
        }

        for (i = size - 2; i > size - age && i > bestLength.get(OPP_HIST); i--) {
            j = matchSingle(i, this.opponentPlays);
            if (j > bestLength.get(OPP_HIST)) {
                bestLength.set(OPP_HIST, j);
                best.set(OPP_HIST, i);
                if (j > size / 2)
                    break;
            }
        }

        for (i = size - 2; i > size - age && i > bestLength.get(BOTH_HIST); i--) {
            j = matchBoth(i);
            if (j > bestLength.get(BOTH_HIST)) {
                bestLength.set(BOTH_HIST, j);
                best.set(BOTH_HIST, i);
                if (j > size / 2)
                    break;
            }
        }
    }

    public void resetStats(Stats stats) {
        stats.age = 0;
        for (int i = 0; i <= 2; i++) {
            stats.sum.get(stats.age)
                    .set(stats.sum.get(stats.age).get(i), 0);
        }
    }

    public void addStats(Stats stats, int i, int delta) {
        int loc = stats.sum.get(stats.age).get(i);
        stats.sum.get(stats.age).set(loc, loc += delta);
    }

    public void nextStats(Stats stats) {
        int i;
        stats.age++;
        for (i = 0; i <= 2; i++) {
            stats.sum.get(stats.age).set(
                    stats.sum.get(stats.age).get(i),
                    stats.sum.get(stats.age - 1).get(i));
        }
    }

    public int maxStats(Stats stats, int age, int score) {
        int i;
        int which = -1;
        for (i = 0; i <= 2; i++) {
            int diff;
            if (age > stats.age) {
                diff = stats.sum.get(stats.age).get(i);
            } else {
                diff = stats.sum.get(stats.age).get(i)
                        - stats.sum.get(stats.age - age).get(i);
            }
            if (diff > score) {
                score = diff;
                which = i;
            }
        }
        if (-1 != which) {
            return which;
        } else {
            return 0;
        }
    }

    public void resetPredict(Predict predict) {
        resetStats(predict.stats);
        predict.last = -1;
    }

    public void doPredict(Predict predict, int last, int guess) {
        if (-1 != last) {
            int diff = (5 + last - predict.last) % 5;
            addStats(predict.stats, willBeat(diff), 1);
            addStats(predict.stats, willLoseTo(diff), -1);
            nextStats(predict.stats);
        }
        predict.last = guess;
    }

    public void scanPredict(Predict predict, int age, int move, int score) {
        int i = maxStats(predict.stats, age, score);
        if (i > 0) {
            move = ((predict.last + i) % 5);
        }
    }

    public int iocaine(Iocaine i) {
        int num = this.ownPlays.get(0);
        int last = (num > 0) ? this.opponentPlays.get(num) : -1;
        int guess = 

    }

    public static void main(String[] args) {
        Modified_IP testBot = new Modified_IP();
        for (int i = 0; i < 5; i++) {
            testBot.getNextMove(Action.ROCK);
            testBot.getNextMove(Action.SCISSORS);
            testBot.getNextMove(Action.SPOCK);
            testBot.getNextMove(Action.SCISSORS);
            testBot.getNextMove(Action.ROCK);
            testBot.getNextMove(Action.PAPER);
        }
        testBot.doHistory(30);
    }
}