package vgu.hihi.ttt.basic.svclarchitecture.stateless;

import vgu.hihi.ttt.basic.GameState;

public record ClientDumbMess(GameState state, String moveText, String boardMessage) {
    public static ClientDumbMess parse(String requestLine) {
            String[] parts = requestLine.split("\\|", 2);
            if (parts.length != 2) {
                return new ClientDumbMess(GameState.INVALID, "", "");
            }

            String moveText = parts[0].trim();
            String boardMessage = parts[1].trim();
            if (moveText.equalsIgnoreCase("q") || moveText.equalsIgnoreCase("end")) {
                return new ClientDumbMess(GameState.END, moveText, boardMessage);
            }

            try {
                Integer.parseInt(moveText);
            } catch (NumberFormatException e) {
                return new ClientDumbMess(GameState.INVALID, moveText, boardMessage);
            }

            return new ClientDumbMess(GameState.CONT, moveText, boardMessage);
        }
}
