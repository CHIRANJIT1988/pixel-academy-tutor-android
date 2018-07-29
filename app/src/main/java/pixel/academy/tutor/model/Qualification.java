package pixel.academy.tutor.model;

/**
 * Created by CHIRANJIT on 12/18/2016.
 */

public class Qualification
{
    public int id, status;
    public String qualification, timestamp;

    public Qualification()
    {

    }

    public Qualification(int id, String qualification, String timestamp)
    {
        this.id = id;
        this.qualification = qualification;
        this.timestamp = timestamp;
    }
}
