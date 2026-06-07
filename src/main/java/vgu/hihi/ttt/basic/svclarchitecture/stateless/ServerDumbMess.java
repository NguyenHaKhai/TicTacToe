package vgu.hihi.ttt.basic.svclarchitecture.stateless;

import vgu.hihi.ttt.basic.GameState;
public record ServerDumbMess(GameState state, String boardMessage) {
    public String toProtocolMessage() {
        return state.name() + "|" + boardMessage;
    }

    public static ServerDumbMess parse(String responseLine) {
        String[] parts = responseLine.split("\\|", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("expected STATE|Board_String");
        }

        GameState state = GameState.valueOf(parts[0].trim());
        return new ServerDumbMess(state, parts[1].trim());
    }
}