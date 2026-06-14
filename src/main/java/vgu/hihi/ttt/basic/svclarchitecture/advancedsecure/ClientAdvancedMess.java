package vgu.hihi.ttt.basic.svclarchitecture.advancedsecure;


public record ClientAdvancedMess(
    String moveText,
    String nonce,
    long creationTime,
    String boardMessage,
    String hash
) {

    public String toProtocolMessage() {
        return moveText + "|" + nonce + "|" + creationTime + "|" + boardMessage + "|" + hash;
    }

    public static ClientAdvancedMess parse(String requestLine) {


        String[] parts = requestLine.split("\\|", 5);
        if (parts.length != 5) {
            throw new IllegalArgumentException("moveText|nonce|creationTime|Board_String|Hash_String");
        }

        return new ClientAdvancedMess(parts[0].trim(),
            parts[1].trim(),
            Long.parseLong(parts[2].trim()),
            parts[3].trim(),
            parts[4].trim());

    }
}
