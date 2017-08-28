package eu.geekhome.asymptote.model;

public class BoardNotSupportedException extends Exception {
    private int _boardIdAsInt;

    public BoardNotSupportedException(int boardIdAsInt) {

        _boardIdAsInt = boardIdAsInt;
    }

    public int getBoardIdAsInt() {
        return _boardIdAsInt;
    }
}
