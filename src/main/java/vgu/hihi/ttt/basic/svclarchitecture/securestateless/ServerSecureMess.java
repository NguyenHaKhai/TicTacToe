package vgu.hihi.ttt.basic.svclarchitecture.securestateless;

import vgu.hihi.ttt.basic.GameState;
public record ServerSecureMess(GameState state, String boardMessage, String hashBoard) {
    
    public String toProtocolMessage() {
        return state.name() + "|" + boardMessage + "|" + hashBoard;
    }

    public static ServerSecureMess parse(String responseLine) {
        String[] parts = responseLine.split("\\|", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("expected STATE|Board_String|Hash_Board");
        }

        GameState state = GameState.valueOf(parts[0].trim());
        return new ServerSecureMess(state, parts[1].trim(), parts[2].trim());
    }
}
