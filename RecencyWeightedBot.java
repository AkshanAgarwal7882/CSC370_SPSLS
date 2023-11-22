import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RecencyWeightedBot implements RoShamBot {
    private double alpha;
    private Map<Action, Double> actionValues;
    private Action lastMove;

    public RecencyWeightedBot(double alpha) {
        this.alpha = alpha;
        this.actionValues = initializeActionValues();
        this.lastMove = null;
    }

    private Map<Action, Double> initializeActionValues() {
        Map<Action, Double> values = new HashMap<>();
        for (Action action : Action.values()) {
            values.put(action, 1.0 / Action.values().length);
        }
        return values;
    }

    public void updateValues(Action opponentMove) {
        if (lastMove != null) {
            for (Action action : Action.values()) {
                actionValues.put(action, (1 - alpha) * actionValues.get(action));
            }
            actionValues.put(opponentMove, actionValues.get(opponentMove) + alpha);
        }
        lastMove = opponentMove;
    }

    public Action predict() {
        if (lastMove == null) {
            return Action.values()[new Random().nextInt(Action.values().length)];
        }

        if (Math.random() < alpha) {
            // Explore: Randomly choose an action
            return Action.values()[new Random().nextInt(Action.values().length)];
        } else {
            // Exploit: Choose the action with the highest value
            return actionValues.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(Action.ROCK); // Default to Rock if there is a tie
        }
    }

    @Override
    public Action getNextMove(Action lastOpponentMove) {
        if (lastOpponentMove != null) {
            updateValues(lastOpponentMove);
        }

        return predict();
    }
}
