import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Message {
    private final Header header;
    private final Question question;

    public Message(Header header, Question question) {
        this.header = header;
        this.question = question;
    }

    public byte[] getBytes()  {
        final byte[] header = this.header.getBytes();
        final byte[] question = this.question.getBytes();
        ByteBuffer messageByteBuffer = ByteBuffer.allocate(header.length + question.length);
        messageByteBuffer.put(header);
        messageByteBuffer.put(question);
        return messageByteBuffer.array();
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

        public byte[] getBytes() {

            ByteBuffer headerByteBuffer = ByteBuffer.allocate(12);
            headerByteBuffer.putShort(id);
            headerByteBuffer.putShort(flags);
            headerByteBuffer.putShort(questionCount);
            headerByteBuffer.putShort(answerCount);
            headerByteBuffer.putShort(authorityCount);
            headerByteBuffer.putShort(additionalRecordsCount);
            return headerByteBuffer.array();
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

        public byte[] getBytes() {

            byte[] domainBytes = getDomainBytes();
            ByteBuffer questionByteBuffer = ByteBuffer.allocate(domainBytes.length + 4);
            questionByteBuffer.put(domainBytes);
            questionByteBuffer.putShort(questionType.value);
            questionByteBuffer.putShort(questionClass.value);
            return questionByteBuffer.array();

        }

        private byte[] getDomainBytes() {

            List<byte[]> domainParts = Arrays.stream(domain.split("\\."))
                    .map(part -> part.getBytes(StandardCharsets.UTF_8))
                    .toList();
            int domainLength = domainParts.stream().mapToInt(bytes -> bytes.length).sum() + domainParts.size();
            ByteBuffer domainByteBuffer = ByteBuffer.allocate(domainLength + 1);
            domainParts.forEach(part -> {
                domainByteBuffer.put((byte) part.length);
                domainByteBuffer.put(part);
            });
            domainByteBuffer.put((byte) 0);
            return domainByteBuffer.array();
        }
    }
}
