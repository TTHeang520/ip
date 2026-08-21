/**
 * Represents the fixed set of command words that Baby understands.
 */
public enum Command {
    TODO("todo"),
    DEADLINE("deadline"),
    EVENT("event"),
    MARK("mark"),
    UNMARK("unmark"),
    DELETE("delete");

    private final String commandWord;

    /**
     * Creates a command type with the exact word the user types.
     *
     * @param commandWord Text used to invoke this command.
     */
    Command(String commandWord) {
        this.commandWord = commandWord;
    }

    /**
     * Returns the exact command word typed by the user.
     *
     * @return The command word.
     */
    public String getCommandWord() {
        return commandWord;
    }
}
