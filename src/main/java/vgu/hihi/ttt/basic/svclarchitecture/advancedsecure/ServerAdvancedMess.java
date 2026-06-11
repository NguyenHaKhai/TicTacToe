package vgu.hihi.ttt.basic.svclarchitecture.advancedsecure;

import vgu.hihi.ttt.basic.GameState;
public record ServerAdvancedMess(GameState state, String boardMessage, String hashBoard, String gameId) {
    
    public String toProtocolMessage() {
        return state.name() + "|" + boardMessage + "|" + hashBoard + "|" + gameId;
    }

    public static ServerAdvancedMess parse(String responseLine) {
        String[] parts = responseLine.split("\\|", 4);
        if (parts.length != 4) {
            throw new IllegalArgumentException("expected STATE|Board_String|Hash_Board|Game_ID");
        }

        GameState state = GameState.valueOf(parts[0].trim());
        return new ServerAdvancedMess(state, parts[1].trim(), parts[2].trim(), parts[3].trim());
    }
}
