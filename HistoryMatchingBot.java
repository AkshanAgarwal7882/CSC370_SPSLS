import java.util.*;
import java.math.*;

public class HistoryMatchingBot implements RoShamBot {
    public ArrayList<Integer> oppPlays;
    public ArrayList<Integer> history;
    public ArrayList<Integer> ownPlays;
    public ArrayList<Integer> best;
    public ArrayList<Integer> bestLength;
    public Action bestPlay;
    public int AGE = 500;
    public boolean losing = false;
    public int[] strategy;

    public HistoryMatchingBot() {
        this.oppPlays = new ArrayList<Integer>();
        this.history = new ArrayList<Integer>();
        this.ownPlays = new ArrayList<Integer>();
        this.best = new ArrayList<Integer>();
        this.bestLength = new ArrayList<Integer>();
        this.bestPlay = getRandomMove();
        this.strategy = new int[3];
    }

    /**
     * Returns an action based on the most recently-played actions of the opponent
     * 
     * @param lastOpponentMove the action that was played by the opponent on
     *                         the last round
     * @return the next action to play.
     */
    public Action getNextMove(Action lastOpponentMove) {
        checkIfLosing();
        this.bestPlay = getRandomMove();
        addOpponentMove(lastOpponentMove);
        // if (this.ownPlays.size() > 60) {
        // this.ownPlays.remove(0);
        // this.oppPlays.remove(0);
        // }
        if (!this.losing) {
            doHistory(this.AGE);
        }
        // ArrayList<Double> movePercentages = new ArrayList<Double>();
        // if (oppPlays.size() < 5) {
        // movePercentages.clear();
        // for (int i = 0; i < 5; i++) {
        // movePercentages.add(0.2 * (i + 1));
        // }
        // }
        if (this.bestPlay == Action.ROCK) {
            this.ownPlays.add(0);
        } else if (this.bestPlay == Action.PAPER) {
            this.ownPlays.add(1);
        } else if (this.bestPlay == Action.SCISSORS) {
            this.ownPlays.add(2);
        } else if (this.bestPlay == Action.LIZARD) {
            this.ownPlays.add(3);
        } else {
            this.ownPlays.add(4);
        }
        return this.bestPlay;
        // if (coinFlip <= movePercentages.get(0)) {
        // this.ownPlays.add(0);
        // return Action.ROCK;
        // } else if (coinFlip <= movePercentages.get(1)) {
        // this.ownPlays.add(1);
        // return Action.PAPER;
        // } else if (coinFlip <= movePercentages.get(2)) {
        // this.ownPlays.add(2);
        // return Action.SCISSORS;
        // } else if (coinFlip <= movePercentages.get(3)) {
        // this.ownPlays.add(3);
        // return Action.LIZARD;
        // } else {
        // this.ownPlays.add(4);
        // return Action.SPOCK;
        // }
    }

    public Action returnMove(int play) {
        switch (play) {
            case 0:
                return Action.ROCK;
            case 1:
                return Action.PAPER;
            case 2:
                return Action.SCISSORS;
            case 3:
                return Action.LIZARD;
            default:
                return Action.SPOCK;
        }
    }

    public Action getRandomMove() {
        double coinFlip = Math.random();
        switch ((int) (coinFlip * 5)) {
            case 0:
                return Action.ROCK;
            case 1:
                return Action.PAPER;
            case 2:
                return Action.SCISSORS;
            case 3:
                return Action.LIZARD;
            default:
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

    public Action willBeat(int play) {
        double coinFlip = Math.random();
        switch (play) {
            case 0:
                if (coinFlip < 0.5) {
                    return Action.PAPER;
                }
                return Action.SPOCK;
            case 1:
                if (coinFlip < 0.5) {
                    return Action.SCISSORS;
                }
                return Action.LIZARD;
            case 2:
                if (coinFlip < 0.5) {
                    return Action.ROCK;
                }
                return Action.SPOCK;
            case 3:
                if (coinFlip < 0.5) {
                    return Action.ROCK;
                }
                return Action.SCISSORS;
            default:
                if (coinFlip < 0.5) {
                    return Action.PAPER;
                }
                return Action.LIZARD;
        }
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
        int end = this.oppPlays.size() - 2;
        for (j = 0; j <= index
                && this.oppPlays.get(end - j - 1) == this.oppPlays.get(index - j - 1)
                && this.ownPlays.get(end - j) == this.ownPlays.get(index - j); j++) {
        }
        ;
        return j;
    }

    public void doHistory(int AGE) {
        int size = this.ownPlays.size();
        int i, j;
        for (int w = 0; w < 3; w++) {
            this.best.add(0);
            this.bestLength.add(0);
        }
        for (i = size - 2; i > size - AGE; i--) {
            j = matchSingle(i, this.ownPlays);
            if (j > this.bestLength.get(0)) {
                this.bestLength.set(0, j);
                this.best.set(0, i);
                if (j > size / 2)
                    break;
            }
        }
        size = this.oppPlays.size();
        ArrayList<ArrayList<Integer>> oppResults = new ArrayList<ArrayList<Integer>>();
        for (int k = 0; k < size; k++) {
            oppResults.add(new ArrayList<>(
                    List.of()));
        }
        for (i = size - 2; i > size - AGE - 1 && i >= 0; i--) {
            j = matchSingle(i, this.oppPlays);
            oppResults.get(j).add(i);
            // if (j > this.bestLength.get(1)) {
            // this.bestLength.set(1, j);
            // this.best.set(1, i);
            // if (j > size / 2)
            // break;
            // }
        }
        if (oppResults.size() > this.AGE + 1) {
            int check = 1;
        }
        Action bestPlay = findBestPlay(oppResults, this.oppPlays.size());
        this.bestPlay = bestPlay;

        // for (i = size - 2; i > size - age && i > bestLength.get(2); i--) {
        // j = matchBoth(i);
        // if (j > bestLength.get(2)) {
        // bestLength.set(2, j);
        // this.best.set(2, i);
        // if (j > size / 2)
        // break;
        // }
        // }
    }

    public Action findBestPlay(ArrayList<ArrayList<Integer>> results, int size) {
        size = size > this.AGE ? this.AGE : size;
        int bestIndex = 0;
        double lowestP = 1.0;
        ArrayList<Integer> bestIndices = new ArrayList<Integer>();
        // ignoring sequences of length 0 for now - use later to determine what not to
        // play
        for (int j = 1; j < results.size(); j++) {
            if (results.get(j).isEmpty())
                continue;
            if (results.get(j).size() == 1) {
                double p = 1 - Math.pow((1 - 1 / Math.pow((double) 5, (double) j)), (double) size - j);
                if (p < lowestP) {
                    lowestP = p;
                    bestIndex = j;
                }
            } else {
                double p = 0.0;
                ArrayList<Integer> indices = new ArrayList<Integer>();
                for (int i = j; i < results.size(); i++) {
                    if (results.get(i).isEmpty()) {
                        continue;
                    }
                    for (int k = 0; k < results.get(i).size(); k++) {
                        indices.add(results.get(i).get(k));
                    }
                }
                // x = indices.size()
                // m = size
                // n = j
                for (int i = indices.size(); i <= size - j; i++) {
                    p += binomialCoeff(size - j, i)
                            * Math.pow((1 / (Math.pow(5, j))), i)
                            * Math.pow((1 - 1 / Math.pow(5, j)), size - j - i);
                }
                if (p < lowestP) {
                    lowestP = p;
                    bestIndex = j;
                    bestIndices = indices;
                }
            }
        }
        if (results.get(bestIndex).size() == 1 && lowestP < 0.05) {
            this.strategy[1]++;
            return willBeat(this.oppPlays.get(results.get(bestIndex).get(0) + 1));
        } else if (lowestP < 0.05) {
            this.strategy[2]++;
            return bestNext(bestIndices);
        }
        this.strategy[0]++;
        return this.bestPlay;
    }

    public Action bestNext(ArrayList<Integer> indices) {
        // create array to track the numbers of subsequent moves which a) equal a given
        // action b) lose to a given action
        double lowestP = 1.0;
        Action bestAction = getRandomMove();
        int[][] actions = new int[5][2];
        for (int i = 0; i < indices.size(); i++) {
            switch (this.oppPlays.get(indices.get(i) + 1)) {
                case 0:
                    actions[0][0]++;
                    actions[1][1]++;
                    actions[4][1]++;
                    break;
                case 1:
                    actions[1][0]++;
                    actions[2][1]++;
                    actions[3][1]++;
                    break;
                case 2:
                    actions[2][0]++;
                    actions[0][1]++;
                    actions[4][1]++;
                    break;
                case 3:
                    actions[3][0]++;
                    actions[0][1]++;
                    actions[2][1]++;
                    break;
                default:
                    actions[4][0]++;
                    actions[1][1]++;
                    actions[3][1]++;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (actions[i][0] > indices.size() / (double) 2) {
                double p = 0.0;
                for (int k = actions[i][0]; k <= indices.size(); k++) {
                    p += binomialCoeff(indices.size(), k) * Math.pow(0.2, k) * Math.pow(0.8, indices.size() - k);
                }
                p = 5 * p;
                if (p < lowestP) {
                    lowestP = p;
                    bestAction = willBeat(i);
                }
            }
            if (actions[i][1] > indices.size() / (double) 2) {
                double p = 0.0;
                for (int k = actions[i][1]; k <= indices.size(); k++) {
                    p += binomialCoeff(indices.size(), k) * Math.pow(0.4, k) * Math.pow(0.6, indices.size() - k);
                }
                p = 2 * p;
                if (p < lowestP) {
                    lowestP = p;
                    bestAction = returnMove(i);
                }
            }
            if (actions[i][0] + actions[i][1] > indices.size() / (double) 2) {
                double p = 0.0;
                for (int k = actions[i][0] + actions[i][1]; k <= indices.size(); k++) {
                    p += binomialCoeff(indices.size(), k) * Math.pow(0.6, k) * Math.pow(0.4, indices.size() - k);
                }
                if (p < lowestP) {
                    lowestP = p;
                    bestAction = returnMove(i);
                }
            }
        }

        return bestAction;
    }

    public double calculate(ArrayList<ArrayList<Integer>> results, int size, int startingIndex) {
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (int i = startingIndex; i < results.size(); i++) {
            if (results.get(i).isEmpty()) {
                continue;
            }
            for (int k = 0; k < results.get(i).size(); k++) {
                indices.add(results.get(i).get(k));
            }
        }
        // x = indices.size()
        // m = size
        // n = startingIndex
        double p = 0.0;
        for (int i = indices.size(); i <= size - startingIndex; i++) {
            p += binomialCoeff(size - startingIndex, i)
                    * Math.pow((1 / (Math.pow(5, startingIndex))), i)
                    * Math.pow((1 - 1 / Math.pow(5, startingIndex)), size - startingIndex - i);
        }
        return p;
    }

    public void checkIfLosing() {
        if (this.ownPlays.size() > 0) {
            this.history
                    .add(outcome(this.ownPlays.get(this.ownPlays.size() - 1),
                            this.oppPlays.get(this.oppPlays.size() - 1)));
        }
        if (this.ownPlays.size() < 100) {
            return;
        }
        int numLosses = 0;
        for (int i = 0; i < this.history.size(); i++) {
            if (this.history.get(i) == -1) {
                numLosses++;
            }
        }
        if (numLosses > this.history.size() / 2) {
            this.losing = true;
        }
    }

    public int outcome(int ownPlay, int oppPlay) {
        if (oppPlay == ownPlay) {
            return 0;
        }
        switch (ownPlay) {
            case 0:
                if (oppPlay == 1 || oppPlay == 4) {
                    return -1;
                }
                return 1;
            case 1:
                if (oppPlay == 2 || oppPlay == 3) {
                    return -1;
                }
                return 1;
            case 2:
                if (oppPlay == 0 || oppPlay == 4) {
                    return -1;
                }
                return 1;
            case 3:
                if (oppPlay == 0 || oppPlay == 2) {
                    return -1;
                }
                return 1;
            default:
                if (oppPlay == 1 || oppPlay == 3) {
                    return -1;
                }
                return 1;
        }
    }

    // public boolean losingBad() {
    // this.best.clear();
    // this.bestLength.clear();
    // {
    // this.history.add(
    // play(this.ownPlays.get(this.ownPlays.size() - 1),
    // this.oppPlays.get(this.oppPlays.size() - 1)));
    // }
    // int score = 0;
    // for (int i = 1; i <= this.history.size(); i++) {
    // score += this.history.get(this.history.size() - i);
    // }
    // if (score < -2) {
    // return true;
    // }
    // return false;
    // }

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

    // public static BigInteger binomialCoeff(int n, int k) {
    // long check = (fact(k) * fact(n - k));
    // if (check > Long.MAX_VALUE || check < Long.MIN_VALUE) {
    // System.out.println("A");
    // }
    // if (check <= 0) {
    // System.out.println("B");
    // ;
    // }
    // long b = fact(n) / (fact(k) * fact(n - k));
    // return b;
    // }

    public long binomialCoeff(int n, int k) {
        if (k > n - k)
            k = n - k;

        long b = 1;
        for (int i = 1, m = n; i <= k; i++, m--)
            b = b * m / i;
        return b;
    }

    public static void main(String[] args) {
        // long check = binomialCoeff(60, 30);
        // HistoryMatchingBot testBot = new HistoryMatchingBot();
        // ArrayList<Integer> testPlays = new ArrayList<>(
        // List.of(4, 1, 2, 0, 1, 2, 3, 1, 2, 3, 1, 2, 3, 4, 1, 2));
        // testBot.oppPlays = testPlays;
        // testBot.ownPlays = new ArrayList<>(
        // List.of(1, 2, 3, 0, 1, 2, 4, 1, 2, 3, 4, 4, 4, 0, 1));
        // testBot.doHistory(16);

        // Action[] arr = new Action[100];
        // for (int i = 0; i < 100; i++) {
        // arr[i] = testBot.getNextMove(Action.ROCK);
        // }
        // Random rand = new Random();

        // int[] arr = new int[37];
        // for (int i = 0; i < 1000000; i++) {
        // ArrayList<Integer> testPlays = new ArrayList<>();
        // for (int j = 0; j < 37; j++) {
        // int rand_int = rand.nextInt(5);
        // testPlays.add(rand_int);
        // }

        // int numOnes = 0;
        // for (int k = 0; k < testPlays.size() - 1; k++) {
        // int test = testBot.matchSingle(k, testPlays);
        // // if (test > longestSeq) {
        // // longestSeq = test;
        // // }
        // if (test >= 1) {
        // numOnes++;
        // }
        // }
        // arr[numOnes]++;
        // }
        // double chanceOfTwo = arr[2] / 1000000.0;
        // ArrayList<Integer> testPlays = new ArrayList<>(
        // List.of(1, 2, 0, 2, 2, 3, 4, 5, 1, 0));
    }
}