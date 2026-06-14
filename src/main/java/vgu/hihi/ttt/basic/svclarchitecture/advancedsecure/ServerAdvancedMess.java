package vgu.hihi.ttt.basic.svclarchitecture.advancedsecure;

import vgu.hihi.ttt.basic.GameState;

public record ServerAdvancedMess(
    GameState state,
    String nonce,
    long creationTime,
    String boardMessage,
    String hash
) {

    public String toProtocolMessage() {
        return state + "|" + nonce + "|" + creationTime + "|" + boardMessage + "|" + hash;
    }


    public static ServerAdvancedMess parse(String responseLine) {
        String[] parts = responseLine.split("\\|", 5);
        if (parts.length != 5) {
            throw new IllegalArgumentException("expected STATE|nonce|creationTime|Board_String|Hash_String");
        }

        GameState state = GameState.valueOf(parts[0].trim());
        return new ServerAdvancedMess(
            state,
            parts[1].trim(),
            Long.parseLong(parts[2].trim()),
            parts[3].trim(),
            parts[4].trim()
        );
    }
}
