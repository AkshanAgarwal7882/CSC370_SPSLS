
    
import java.util.Random;
//Demolishes SolidAsaRock
//Looses/ties to NashBot
//Ties wih MixedBot
//looses to ApeBot

public class GreedyBot implements RoShamBot {
    private Action lastMove;
    private int rockCount;
    private int paperCount;
    private int scissorsCount;
    private int lizardCount;
    private int spockCount;
    private int Count;
    private Random random;

    public GreedyBot() {
        this.lastMove = null;
        this.rockCount = 0;
        this.paperCount = 0;
        this.scissorsCount = 0;
        this.lizardCount = 0;
        this.spockCount = 0;
        this.Count = 0;
        this.random = new Random();
    }

    public Action getNextMove(Action lastOpponentMove) {
        if (lastMove != null) {
            updateCounts(lastOpponentMove);
        }

        // Greedy strategy
        Action greedyAction = getGreedyAction();
        // if (greedyAction != null) {
        //     lastMove = greedyAction;
        //     return greedyAction;
        // }

        // ε-greedy strategy
        double epsilon = 0.1;  // Set your desired value for epsilon
        if (random.nextDouble() < epsilon) {
            lastMove = getRandomAction();
            return lastMove;
        } else {
            lastMove = getGreedyAction();
            return lastMove;
        }
    }

    private void updateCounts(Action lastOpponentMove) {
        Count++;

        switch (lastOpponentMove) {
            case ROCK:
                rockCount++;
                break;
            case PAPER:
                paperCount++;
                break;
            case SCISSORS:
                scissorsCount++;
                break;
            case LIZARD:
                lizardCount++;
                break;
            case SPOCK:
                spockCount++;
                break;
        }
    }

    private Action getGreedyAction() {
        int probRock = rockCount/ Count;
        int probScissor = scissorsCount/ Count;
        int probPaper = paperCount/ Count;
        int probSpock = spockCount/ Count;
        int probLizard = lizardCount/ Count;

        


        if (rockCount <= paperCount && rockCount <= scissorsCount
                && rockCount <= lizardCount && rockCount <= spockCount) {
            return Action.ROCK;
        } else if (paperCount <= rockCount && paperCount <= scissorsCount
                && paperCount <= lizardCount && paperCount <= spockCount) {
            return Action.PAPER;
        } else if (scissorsCount <= rockCount && scissorsCount <= paperCount
                && scissorsCount <= lizardCount && scissorsCount <= spockCount) {
            return Action.SCISSORS;
        } else if (lizardCount <= rockCount && lizardCount <= paperCount
                && lizardCount <= scissorsCount && lizardCount <= spockCount) {
            return Action.LIZARD;
        } else {
            return Action.SPOCK;
        }
    }

    private Action getRandomAction() {
        int randomIndex = random.nextInt(5);
        return Action.values()[randomIndex];
    }
}


