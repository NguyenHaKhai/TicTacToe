package vgu.hihi.ttt.basic.svclarchitecture.stateless;

public record ClientDumbMess(String moveText, String boardMessage) {
    public static ClientDumbMess parse(String requestLine) {
        String[] parts = requestLine.split("\\|", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("expected STATE|Board_String");
        }

        String moveText = parts[0].trim();
        String boardMessage = parts[1].trim();

        return new ClientDumbMess(moveText, boardMessage);
    }

    public String toProtocolMessage() {
        return moveText + "|" + boardMessage;
    }
}
