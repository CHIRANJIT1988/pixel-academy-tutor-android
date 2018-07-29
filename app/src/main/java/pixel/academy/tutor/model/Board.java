package pixel.academy.tutor.model;

/**
 * Created by CHIRANJIT on 12/18/2016.
 */

public class Board
{
    public int id, status;
    public String board, timestamp;

    public Board()
    {

    }

    public Board(int id, String board, String timestamp)
    {
        this.id = id;
        this.board = board;
        this.timestamp = timestamp;
    }
}
