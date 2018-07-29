package pixel.academy.tutor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CHIRANJIT on 9/21/2016.
 */
public class Occupation implements Serializable
{
    public static List<Occupation> occupationList = new ArrayList<>();

    public int id, status;
    public String occupation, organization, start_date, end_date;


    public Occupation()
    {

    }

    public Occupation(String organization, String occupation, String start_date, String end_date)
    {
        this.organization = organization;
        this.occupation = occupation;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}
