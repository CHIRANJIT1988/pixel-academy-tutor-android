package pixel.academy.tutor.model;

/**
 * Created by CHIRANJIT on 12/18/2016.
 */

public class Subject
{
    public int id, status;
    public String subject, timestamp;

    public Subject()
    {

    }

    public Subject(int id, String subject, String timestamp)
    {
        this.id = id;
        this.subject = subject;
        this.timestamp = timestamp;
    }
}
