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


        String[] parts = requestLine.split("\\|", 6);
        if (parts.length != 6) {
            throw new IllegalArgumentException("moveText|nonce|creationTime|Board_String|Hash_String");
        }

        return new ClientAdvancedMess(parts[1].trim(),
            parts[2].trim(),
            Long.parseLong(parts[3].trim()),
            parts[4].trim(),
            parts[5].trim());

    }
}
