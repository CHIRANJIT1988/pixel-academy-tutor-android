package pixel.academy.tutor.model;

/**
 * Created by CHIRANJIT on 12/18/2016.
 */

public class Medium
{
    public int id, status;
    public String medium, timestamp;

    public Medium()
    {

    }

    public Medium(int id, String medium, String timestamp)
    {
        this.id = id;
        this.medium = medium;
        this.timestamp = timestamp;
    }
}
