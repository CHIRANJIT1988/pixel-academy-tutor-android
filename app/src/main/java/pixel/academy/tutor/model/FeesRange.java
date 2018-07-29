package pixel.academy.tutor.model;

/**
 * Created by CHIRANJIT on 12/18/2016.
 */

public class FeesRange
{
    public int id, status;
    public String fees_range, timestamp;

    public FeesRange()
    {

    }

    public FeesRange(int id, String fees_range, String timestamp)
    {
        this.id = id;
        this.fees_range = fees_range;
        this.timestamp = timestamp;
    }
}
