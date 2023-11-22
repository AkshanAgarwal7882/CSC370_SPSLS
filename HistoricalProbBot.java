import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.*;

public class HistoricalProbBot implements RoShamBot {
    private Map<Action, Double> outcomeProbabilities;
    private Action lastOpponentMove;

    public HistoricalProbBot() {
        this.outcomeProbabilities = initializeProbabilities();
        this.lastOpponentMove = null;
    }

    private Map<Action, Double> initializeProbabilities() {
        Map<Action, Double> probabilities = new HashMap<>();
        for (Action action : Action.values()) {
            probabilities.put(action, 0.2);
        }
        return probabilities;
    }

    private static Action[] getFavorableOutcomes(Action opponentMove) {
        switch (opponentMove) {
            case ROCK:
                return new Action[]{Action.SCISSORS, Action.LIZARD};
            case PAPER:
                return new Action[]{Action.ROCK, Action.SPOCK};
            case SCISSORS:
                return new Action[]{Action.PAPER, Action.LIZARD};
            case LIZARD:
                return new Action[]{Action.SPOCK, Action.PAPER};
            case SPOCK:
                return new Action[]{Action.SCISSORS, Action.ROCK};
            default:
                throw new IllegalArgumentException("Invalid action: " + opponentMove);
        }
    }

    private void updateProbabilities(Action opponentMove) {
        if (lastOpponentMove != null) {
            // Identify the two outcomes that beat the opponent's move
            Action[] favorableOutcomes = getFavorableOutcomes(lastOpponentMove);

            // Update probabilities
            for (Action action : Action.values()) {
                if (action == lastOpponentMove) {
                    outcomeProbabilities.put(action, outcomeProbabilities.get(action) + 0.1);
                } else if (action == favorableOutcomes[0] || action == favorableOutcomes[1]) {
                    outcomeProbabilities.put(action, outcomeProbabilities.get(action) + 0.05);
                } else {
                    outcomeProbabilities.put(action, outcomeProbabilities.get(action) - 0.05);
                }

                // Ensure probabilities are within the valid range [0, 1]
                outcomeProbabilities.put(action, Math.max(0.0, Math.min(1.0, outcomeProbabilities.get(action))));
            }
        }
        lastOpponentMove = opponentMove;
    }

    
    private Action chooseNextAction() {
        List<Action> weightedActions = new ArrayList<>();

        for (Action action : Action.values()) {
            Action[] defeats = getDefeats(action);
            double weight = 0.2
                    - (outcomeProbabilities.get(defeats[0]) / 10.0)
                    + (outcomeProbabilities.get(defeats[1]) / 10.0);

            for (int i = 0; i < Math.round(weight * 10); i++) {
                weightedActions.add(action);
            }
        }

        return weightedActions.get(new Random().nextInt(weightedActions.size()));
    }

    private Action[] getDefeats(Action action) {
        switch (action) {
            case ROCK:
                return new Action[]{Action.PAPER, Action.SPOCK};
            case PAPER:
                return new Action[]{Action.SCISSORS, Action.LIZARD};
            case SCISSORS:
                return new Action[]{Action.ROCK, Action.SPOCK};
            case LIZARD:
                return new Action[]{Action.ROCK, Action.SCISSORS};
            case SPOCK:
                return new Action[]{Action.PAPER, Action.LIZARD};
            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }
    }
    @Override
    public Action getNextMove(Action lastOpponentMove) {
        if (lastOpponentMove != null) {
            updateProbabilities(lastOpponentMove);
        }

        return chooseNextAction();
    }
}
