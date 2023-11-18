
public class Bot_D implements RoShamBot {

    /**
     * Returns an action that loses to the opponent's previous action.
     * 
     * @param lastOpponentMove the action that was played by the opposing
     *                         agent on the last round.
     *
     * @return the next action to play.
     */
    public Action getNextMove(Action lastOpponentMove) {
        switch (lastOpponentMove) {
            case ROCK:
                return Action.LIZARD;
            case PAPER:
                return Action.ROCK;
            case SCISSORS:
                return Action.PAPER;
            case LIZARD:
                return Action.SPOCK;
            default:
                return Action.SCISSORS;
        }
    }

    public static void main(String[] args) {
        System.out.println("yass");
    }
}