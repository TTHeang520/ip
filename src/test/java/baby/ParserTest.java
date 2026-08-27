package baby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void getTodoDescription_validInput_returnDescription() {
        String result = Parser.getTodoDescription("todo read book");

        assertEquals("read book", result);
    }

    @Test
    public void getTodoDescription_extraSpaces_returnsTrimmedDescription() {
        String result = Parser.getTodoDescription("todo    read book   ");

        assertEquals("read book", result);
    }
}