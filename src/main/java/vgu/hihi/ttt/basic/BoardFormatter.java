package vgu.hihi.ttt.basic;

import vgu.hihi.ttt.basic.settings.Constant;

public final class BoardFormatter {
    private BoardFormatter() {
    }

    public static String formatSymbols(Board board) {
        String result = "";

        for (int i = 0; i < board.getSize(); i++) {
            board.getPrinter().print("| " + symbolFor(board.getCell(i)) + " ");
            result += "| " + symbolFor(board.getCell(i)) + " ";

            if ((i + 1) % board.getCol() == 0) {
                board.getPrinter().print("|\n");
                result += "|\n";
            }
        }

        return result;
    }

    private static String symbolFor(int cellValue) {
        return switch (cellValue) {
            case 0 -> Constant.EMPTY_SYMBOL;
            case Constant.HUMAN_ID -> Constant.HUMAN_SYMBOL;
            case Constant.COMPUTER_ID -> Constant.COMPUTER_SYMBOL;
            default -> "?";
        };
    }
}
