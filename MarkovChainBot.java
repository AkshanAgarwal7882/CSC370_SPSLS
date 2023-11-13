import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MarkovChainBot implements RoShamBot {
    private double decay;
    private Map<Action, Map<Action, Observation>> matrix;
    private Action lastMove;

    public MarkovChainBot(int order, double decay) {
        this.decay = decay;
        this.matrix = createMatrix(order);
        this.lastMove = null;
    }

    // creates the intiial probablity matrix for the Markov Chain

    private Map<Action, Map<Action, Observation>> createMatrix(int order) {
        Map<Action, Map<Action, Observation>> matrix = new HashMap<>();

        for (Action outerAction : Action.values()) {
            Map<Action, Observation> innerMap = new HashMap<>();
            for (Action innerAction : Action.values()) {
                innerMap.put(innerAction, new Observation(1.0 / Action.values().length, 0));
            }
            matrix.put(outerAction, innerMap);
        }

        return matrix;
    }

    public void updateMatrix(Action pair, Action input) {
        if (lastMove != null) {
            for (Map.Entry<Action, Observation> entry : matrix.get(lastMove).entrySet()) {
                entry.getValue().nObs = decay * entry.getValue().nObs;
            }

            matrix.get(lastMove).get(input).nObs++;
            double nTotal = matrix.get(lastMove).values().stream().mapToDouble(o -> o.nObs).sum();

            for (Map.Entry<Action, Observation> entry : matrix.get(lastMove).entrySet()) {
                entry.getValue().prob = entry.getValue().nObs / nTotal;
            }
        }
        lastMove = pair;
    }

    public Action predict() {
        if (lastMove == null) {
            return Action.values()[new Random().nextInt(Action.values().length)];
        }

        Map<Action, Observation> probs = matrix.get(lastMove);

        if (probs.values().stream().map(Observation::getProb).max(Double::compareTo)
                .orElse(0.0) == probs.values().stream().map(Observation::getProb).min(Double::compareTo).orElse(0.0)) {
            return Action.values()[new Random().nextInt(Action.values().length)];
        } else {
            return probs.entrySet().stream()
                    .max(Map.Entry.comparingByValue((o1, o2) -> Double.compare(o1.prob, o2.prob)))
                    .map(Map.Entry::getKey)
                    .orElse(Action.ROCK); // Default to Rock if there is a tie
        }
    }

    public Action getNextMove(Action lastOpponentMove) {
        if (lastOpponentMove != null) {
            updateMatrix(lastMove, lastOpponentMove);
        }

        return predict();
    }

    private static class Observation {
        private double prob;
        private double nObs;

        public Observation(double prob, double nObs) {
            this.prob = prob;
            this.nObs = nObs;
        }

        public double getProb() {
            return prob;
        }
    }
}
