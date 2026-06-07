package vgu.hihi.ttt.basic.svclarchitecture.securestateless;

public record ClientSecureMess(String moveText, String boardMessage, String hashBoard, String gameId) {
    
    public String toProtocolMessage() {
        return moveText + "|" + boardMessage + "|" + hashBoard + "|" + gameId;
    }
    
    public static ClientSecureMess parse(String requestLine) {
        String[] parts = requestLine.split("\\|", 4);
        if (parts.length != 4) {
            throw new IllegalArgumentException("Move_made|Board_String|Hash_Board|Game_ID");
        }

        String moveText = parts[0].trim();
        String boardMessage = parts[1].trim();
        String hashBoard = parts[2].trim();
        String gameId = parts[3].trim();


        return new ClientSecureMess(moveText, boardMessage, hashBoard, gameId);
    }
}
