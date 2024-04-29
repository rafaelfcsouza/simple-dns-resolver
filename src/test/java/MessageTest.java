import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MessageTest {
    @Test
    public void queryToHex() throws IOException {
        final Message query = new Message(new Message.Header((short) 22), new Message.Question("dns.google.com", QuestionType.A, QuestionClass.IN));
        String expectedResult = "00160100000100000000000003646e7306676f6f676c6503636f6d0000010001";
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : query.getBytes()) {
            String st = String.format("%02X", b);
           stringBuilder.append(st);
        }
        Assertions.assertEquals(expectedResult, stringBuilder.toString().toLowerCase());
    }
}
