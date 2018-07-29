package pixel.academy.tutor.app;

/**
 * Created by CHIRANJIT on 12/6/2016.
 */

public class Global
{
    /**
     * Application Tag
     */
    public static final String JSON_TAG = "responseJSON";
    public static final String ERROR_TAG = "json_error";
    public static final String RESPONSE_TAG = "json_response";
    public static final String CONNECTIVITY_ERROR = "Internet Connection Failure";

    /**
     * Retry Policy
     */
    public static final int MAX_RETRIES = 5;
    public static final int TIMEOUT = 3000;

    /**
     * Preference and JSON Tag Name
     */
    public static final String PREF = "pixel.tutor.pref";
    public static final String TOKEN = "push_token";
    public static final String FIRST_RUN = "first_run";
    public static final String KEY = "key";

    /**
     * Database Version and Version
     */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "pixel.db";

    /**
     * Table Names
     */
    public static final String TABLE_EDUCATION = "education";
    public static final String TABLE_OCCUPATION = "occupation";
    public static final String TABLE_MASTER_QUALIFICATION = "qualification";
    public static final String TABLE_MASTER_MEDIUM = "medium";
    public static final String TABLE_MASTER_BOARD = "board";
    public static final String TABLE_MASTER_CLASS = "class";
    public static final String TABLE_MASTER_SUBJECT = "subject";
    public static final String TABLE_MASTER_FEES_RANGE = "fees_range";
    /**
     * Column Names
     */
    public static final String KEY_ID = "id";
    public static final String KEY_QUALIFICATION = "qualification";
    public static final String KEY_DIVISION = "division";
    public static final String KEY_QUALIFICATION_STATUS = "qualification_status";
    public static final String KEY_ACHIEVEMENT = "achievement";
    public static final String KEY_COLLEGE = "college";
    public static final String KEY_ORGANIZATION = "organization";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_STATUS = "status";
    public static final String KEY_SYNC_STATUS = "sync_status";

    public static final String KEY_MEDIUM = "medium";
    public static final String KEY_BOARD = "board";
    public static final String KEY_CLASS = "class";
    public static final String KEY_STANDARD = "standard";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_FEES_RANGE = "fees_range";

    public static final String ADDRESS_DETAILS = "address_details";

    public static final String USER_ID = "user_id";
    public static final String USER = "user";
    public static final String USER_NAME = "user_name";
    public static final String USER_TYPE = "user_type";
    public static final String MOBILE_NUMBER = "mobile_no";
    public static final String PASSWORD = "password";
    public static final String DEVICE_ID = "device_id";
    public static final String STATUS_CODE = "status_code";
    public static final String MESSAGE = "message";
    public static final String ADDRESS = "address";
    public static final String LOCALITY = "locality";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String COUNTRY = "country";
    public static final String PINCODE = "pincode";
    public static final String LATITUDE = "longitude";
    public static final String LONGITUDE = "latitude";
}
