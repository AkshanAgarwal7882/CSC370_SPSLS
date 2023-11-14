import java.util.HashMap;
import java.util.Map;
import java.util.AbstractMap;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MarkovChainBot implements RoShamBot {
    private double decay;
    private Map<List<Action>, Map<Action, Observation>> matrix;
    private List<Action> lastMove;

    public MarkovChainBot(int order, double decay) {
        this.decay = decay;
        this.matrix = createMatrix(order);
        this.lastMove = null;
    }

    private Map<List<Action>, Map<Action, Observation>> createMatrix(int order) {
        Map<List<Action>, Map<Action, Observation>> matrix = new HashMap<>();

        List<List<Action>> keys = createKeys(order);

        for (List<Action> key : keys) {
            Map<Action, Observation> innerMap = new HashMap<>();
            for (Action innerAction : Action.values()) {
                innerMap.put(innerAction, new Observation(1.0 / Action.values().length, 0));
            }
            matrix.put(key, innerMap);
        }

        return matrix;
    }

    private List<List<Action>> createKeys(int order) {
        List<List<Action>> keys = new ArrayList<>();
    
        if (order > 0) {
            generateKeys(order, new ArrayList<>(), keys);
        }
    
        return keys;
    }
    
    private void generateKeys(int remaining, List<Action> currentKey, List<List<Action>> keys) {
        if (remaining == 0) {
            keys.add(new ArrayList<>(currentKey));
            return;
        }
    
        for (Action action : Action.values()) {
            currentKey.add(action);
            generateKeys(remaining - 1, currentKey, keys);
            currentKey.remove(currentKey.size() - 1);
        }
    }

    public void updateMatrix(List<Action> pair, Action input) {
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

    @Override
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
