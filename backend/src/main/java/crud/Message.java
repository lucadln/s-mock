package crud;

public class Message {
    private String type;
    private String content;

    public Message(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String toString() {
        return "{" +
                "\"type\": \"" + type + "\"," +
                "\"content\":" + content +
                "}";
    }
}
