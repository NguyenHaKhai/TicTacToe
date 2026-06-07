package vgu.hihi.ttt.basic.svclarchitecture.stateless;

import vgu.hihi.ttt.basic.GameState;

public record RequestStatelessDumb(GameState state, String moveText, String boardMessage) {
    public static RequestStatelessDumb parse(String requestLine) {
            String[] parts = requestLine.split("\\|", 2);
            if (parts.length != 2) {
                return new RequestStatelessDumb(GameState.INVALID, "", "");
            }

            String moveText = parts[0].trim();
            String boardMessage = parts[1].trim();
            if (moveText.equalsIgnoreCase("q") || moveText.equalsIgnoreCase("end")) {
                return new RequestStatelessDumb(GameState.END, moveText, boardMessage);
            }

            try {
                Integer.parseInt(moveText);
            } catch (NumberFormatException e) {
                return new RequestStatelessDumb(GameState.INVALID, moveText, boardMessage);
            }

            return new RequestStatelessDumb(GameState.CONT, moveText, boardMessage);
        }
}
