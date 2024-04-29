import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message {
    private final Header header;
    private final Question question;

    public Message(Header header, Question question) {
        this.header = header;
        this.question = question;
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.write(header.getBytes());
        dataOutputStream.write(question.getBytes());
        return byteArrayOutputStream.toByteArray();

    }

    public static class Header {
        private final short id;
        private final short flags = Short.parseShort("0000000100000000", 2);
        private final short questionCount = 1;
        private final short answerCount = 0;
        private final short authorityCount = 0;
        private final short additionalRecordsCount = 0;

        public Header(short id) {
             this.id = id;
        }

        public byte[] getBytes() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            ByteBuffer flagsByteBuffer = ByteBuffer.allocate(2).putShort(flags);
            byte[] flagsByteArray = flagsByteBuffer.array();

            dataOutputStream.writeShort(id);
            dataOutputStream.write(flagsByteArray);
            dataOutputStream.writeShort(questionCount);
            dataOutputStream.writeShort(answerCount);
            dataOutputStream.writeShort(authorityCount);
            dataOutputStream.writeShort(additionalRecordsCount);

            return byteArrayOutputStream.toByteArray();
        }
    }

    public static class Question {
        private final String domain;
        private final QuestionType questionType;
        private final QuestionClass questionClass;

        public Question(String domain, QuestionType questionType, QuestionClass questionClass) {
            this.domain = domain;
            this.questionType = questionType;
            this.questionClass = questionClass;
        }

        public byte[] getBytes() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            byte[] domainBytes = getDomainBytes();
            dataOutputStream.write(domainBytes);
            dataOutputStream.writeShort(questionType.value);
            dataOutputStream.writeShort(questionClass.value);

            return byteArrayOutputStream.toByteArray();

        }

        private byte[] getDomainBytes() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            String[] domainParts = domain.split("\\.");
            for (String domainPart : domainParts) {
                byte[] domainBytes = domainPart.getBytes(StandardCharsets.UTF_8);
                dataOutputStream.writeByte(domainBytes.length);
                dataOutputStream.write(domainBytes);
            }
            dataOutputStream.writeByte(0);

            return byteArrayOutputStream.toByteArray();
        }
    }
}
