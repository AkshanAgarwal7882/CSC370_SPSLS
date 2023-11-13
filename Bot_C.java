
public class Bot_C implements RoShamBot {

    /**
     * Returns an action that beats the opponent's previous action.
     * 
     * @param lastOpponentMove the action that was played by the opposing
     *                         agent on the last round.
     *
     * @return the next action to play.
     */
    public Action getNextMove(Action lastOpponentMove) {
        switch (lastOpponentMove) {
            case ROCK:
                return Action.PAPER;
            case PAPER:
                return Action.SCISSORS;
            case SCISSORS:
                return Action.SPOCK;
            case LIZARD:
                return Action.ROCK;
            default:
                return Action.LIZARD;
        }
    }
}