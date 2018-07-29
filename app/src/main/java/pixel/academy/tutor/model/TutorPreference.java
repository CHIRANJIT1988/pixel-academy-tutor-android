package pixel.academy.tutor.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import static pixel.academy.tutor.app.Global.KEY_BOARD;
import static pixel.academy.tutor.app.Global.KEY_MEDIUM;
import static pixel.academy.tutor.app.Global.KEY_STANDARD;
import static pixel.academy.tutor.app.Global.KEY_SUBJECT;

/**
 * Created by CHIRANJIT on 9/21/2016.
 */
public class TutorPreference implements Serializable
{

    public int id, status;
    public String preference_name;
    public String classes, subjects, fees_range, duration_per_day, days_per_week, medium, board;


    public TutorPreference()
    {

    }

    public TutorPreference(int id, String preference_name, int status)
    {
        this.id = id;
        this.preference_name = preference_name;
        this.status = status;
    }

    public TutorPreference(String classes, String subjects, String fees_range, String duration_per_day, String days_per_week, String medium, String board)
    {

        this.classes = classes;
        this.subjects = subjects;
        this.fees_range = fees_range;
        this.duration_per_day = duration_per_day;
        this.days_per_week = days_per_week;
        this.medium = medium;
        this.board = board;
    }

    private static String get_preference_string(List<TutorPreference> p)
    {
        String value= "";

        for(TutorPreference preference: p)
        {
            if(preference.status == 1)
            {
                value += preference.preference_name + ", ";
            }
        }

        return value;
    }

    public static String buildJSONData(List<TutorPreference> medium, List<TutorPreference> board,
                                       List<TutorPreference> standard, List<TutorPreference> subject)
    {
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put(KEY_MEDIUM, get_preference_string(medium));
            jsonObject.put(KEY_SUBJECT, get_preference_string(subject));
            jsonObject.put(KEY_STANDARD, get_preference_string(standard));
            jsonObject.put(KEY_BOARD, get_preference_string(board));
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}