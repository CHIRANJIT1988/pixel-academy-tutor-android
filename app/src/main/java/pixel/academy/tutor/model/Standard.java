package pixel.academy.tutor.model;

/**
 * Created by CHIRANJIT on 12/18/2016.
 */

public class Standard
{
    public int _id, _status;
    public String _class, _timestamp;

    public Standard()
    {

    }

    public Standard(int _id, String _class, String _timestamp)
    {
        this._id = _id;
        this._class = _class;
        this._timestamp = _timestamp;
    }
}
