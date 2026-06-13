package vgu.hihi.ttt.basic.svclarchitecture.securestateless;

public record ClientSecureMess(String moveText, String boardMessage, String hashBoard) {
    
    public String toProtocolMessage() {
        return moveText + "|" + boardMessage + "|" + hashBoard;
    }
    
    public static ClientSecureMess parse(String requestLine) {
        String[] parts = requestLine.split("\\|", 3);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Move_made|Board_String|Hash_Board");
        }

        String moveText = parts[0].trim();
        String boardMessage = parts[1].trim();
        String hashBoard = parts[2].trim();

        return new ClientSecureMess(moveText, boardMessage, hashBoard);
    }
}
