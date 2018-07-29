package pixel.academy.tutor.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pixel.academy.tutor.model.Board;
import pixel.academy.tutor.model.Education;
import pixel.academy.tutor.model.FeesRange;
import pixel.academy.tutor.model.Medium;
import pixel.academy.tutor.model.Occupation;
import pixel.academy.tutor.model.Qualification;
import pixel.academy.tutor.model.Standard;
import pixel.academy.tutor.model.Subject;

import static pixel.academy.tutor.app.Global.DATABASE_NAME;
import static pixel.academy.tutor.app.Global.DATABASE_VERSION;
import static pixel.academy.tutor.app.Global.KEY_ACHIEVEMENT;
import static pixel.academy.tutor.app.Global.KEY_BOARD;
import static pixel.academy.tutor.app.Global.KEY_CLASS;
import static pixel.academy.tutor.app.Global.KEY_COLLEGE;
import static pixel.academy.tutor.app.Global.KEY_DIVISION;
import static pixel.academy.tutor.app.Global.KEY_END_DATE;
import static pixel.academy.tutor.app.Global.KEY_FEES_RANGE;
import static pixel.academy.tutor.app.Global.KEY_ID;
import static pixel.academy.tutor.app.Global.KEY_MEDIUM;
import static pixel.academy.tutor.app.Global.KEY_OCCUPATION;
import static pixel.academy.tutor.app.Global.KEY_ORGANIZATION;
import static pixel.academy.tutor.app.Global.KEY_QUALIFICATION;
import static pixel.academy.tutor.app.Global.KEY_QUALIFICATION_STATUS;
import static pixel.academy.tutor.app.Global.KEY_START_DATE;
import static pixel.academy.tutor.app.Global.KEY_STATUS;
import static pixel.academy.tutor.app.Global.KEY_SUBJECT;
import static pixel.academy.tutor.app.Global.KEY_SYNC_STATUS;
import static pixel.academy.tutor.app.Global.KEY_TIMESTAMP;
import static pixel.academy.tutor.app.Global.TABLE_EDUCATION;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_BOARD;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_CLASS;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_FEES_RANGE;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_MEDIUM;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_QUALIFICATION;
import static pixel.academy.tutor.app.Global.TABLE_MASTER_SUBJECT;
import static pixel.academy.tutor.app.Global.TABLE_OCCUPATION;

/**
 * Created by CHIRANJIT on 12/2/2016.
 */

public class SQLiteHelper  extends SQLiteOpenHelper {

    private Context context;

    private static final String CREATE_TABLE_EDUCATION= "CREATE TABLE "
            + TABLE_EDUCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_QUALIFICATION + " TEXT," + KEY_DIVISION + " TEXT,"
            + KEY_QUALIFICATION_STATUS + " TEXT," + KEY_ACHIEVEMENT + " TEXT," + KEY_COLLEGE + " TEXT, " + KEY_STATUS + " INTEGER DEFAULT 1,"
            + KEY_SYNC_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_OCCUPATION= "CREATE TABLE "
            + TABLE_OCCUPATION + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_OCCUPATION + " TEXT," + KEY_ORGANIZATION + " TEXT,"
            + KEY_START_DATE + " TEXT," + KEY_END_DATE + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 1," + KEY_SYNC_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_BOARD_MASTER = "CREATE TABLE "
            + TABLE_MASTER_BOARD + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_BOARD + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_SUBJECT_MASTER = "CREATE TABLE "
            + TABLE_MASTER_SUBJECT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SUBJECT + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_CLASS_MASTER= "CREATE TABLE "
            + TABLE_MASTER_CLASS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CLASS + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_FEES_RANGE_MASTER= "CREATE TABLE "
            + TABLE_MASTER_FEES_RANGE + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FEES_RANGE + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_QUALIFICATION_MASTER= "CREATE TABLE "
            + TABLE_MASTER_QUALIFICATION + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUALIFICATION + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 0)";

    private static final String CREATE_TABLE_MEDIUM_MASTER= "CREATE TABLE "
            + TABLE_MASTER_MEDIUM + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MEDIUM + " TEXT," + KEY_TIMESTAMP + " TEXT," + KEY_STATUS + " INTEGER DEFAULT 0)";

    public SQLiteHelper(Context context)
    {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_TABLE_EDUCATION);
        database.execSQL(CREATE_TABLE_OCCUPATION);
        database.execSQL(CREATE_TABLE_MEDIUM_MASTER);
        database.execSQL(CREATE_TABLE_QUALIFICATION_MASTER);
        database.execSQL(CREATE_TABLE_CLASS_MASTER);
        database.execSQL(CREATE_TABLE_BOARD_MASTER);
        database.execSQL(CREATE_TABLE_FEES_RANGE_MASTER);
        database.execSQL(CREATE_TABLE_SUBJECT_MASTER);
        Log.v("CREATE TABLE: ", "Inside onCreate()");
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_OCCUPATION);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_MEDIUM);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_QUALIFICATION);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_CLASS);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_BOARD);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_FEES_RANGE);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_SUBJECT);
        onCreate(database);
        Log.v("UPGRADE TABLE: ", "Inside onUpgrade()");
    }


    public boolean insert(Education education)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUALIFICATION, education.qualification);
        values.put(KEY_DIVISION, education.division);
        values.put(KEY_QUALIFICATION_STATUS, education.qualification_status);
        values.put(KEY_ACHIEVEMENT, education.academic_achievement);
        values.put(KEY_COLLEGE, education.college);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_EDUCATION, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }

    public boolean insert(Occupation occupation)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_OCCUPATION, occupation.occupation);
        values.put(KEY_ORGANIZATION, occupation.organization);
        values.put(KEY_START_DATE, occupation.start_date);
        values.put(KEY_END_DATE, occupation.end_date);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_OCCUPATION, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }

    public boolean master_insert(Medium medium)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, medium.id);
        values.put(KEY_MEDIUM, medium.medium);
        values.put(KEY_TIMESTAMP, medium.timestamp);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_MASTER_MEDIUM, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }


    public boolean master_insert(Board board)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, board.id);
        values.put(KEY_BOARD, board.board);
        values.put(KEY_TIMESTAMP, board.timestamp);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_MASTER_BOARD, null, values) > 0;

        Log.v("createSuccessfulBoard", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }


    public boolean master_insert(Subject subject)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, subject.id);
        values.put(KEY_SUBJECT, subject.subject);
        values.put(KEY_TIMESTAMP, subject.timestamp);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_MASTER_SUBJECT, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }


    public boolean master_insert(Qualification qualification)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, qualification.id);
        values.put(KEY_QUALIFICATION, qualification.qualification);
        values.put(KEY_TIMESTAMP, qualification.timestamp);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_MASTER_QUALIFICATION, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }


    public boolean master_insert(Standard standard)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, standard._id);
        values.put(KEY_CLASS, standard._class);
        values.put(KEY_TIMESTAMP, standard._timestamp);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_MASTER_CLASS, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }


    public boolean master_insert(FeesRange range)
    {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID, range.id);
        values.put(KEY_FEES_RANGE, range.fees_range);
        values.put(KEY_TIMESTAMP, range.timestamp);

        // Inserting Row
        boolean createSuccessful = database.insert(TABLE_MASTER_FEES_RANGE, null, values) > 0;

        Log.v("createSuccessful ", String.valueOf(createSuccessful));

        // Closing database connection
        database.close();

        return createSuccessful;
    }

    public List<Subject> getAllSubjectMaster()
    {
        List<Subject> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_SUBJECT + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Subject subject = new Subject();
                subject.id = cursor.getInt(0);
                subject.subject = cursor.getString(1);
                subject.timestamp = cursor.getString(2);
                subject.status = cursor.getInt(3);
                list.add(subject);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
        return list;
    }

    public List<Standard> getAllClassMaster()
    {
        List<Standard> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_CLASS + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Standard standard = new Standard();

                standard._id = cursor.getInt(0);
                standard._class = cursor.getString(1);
                standard._timestamp = cursor.getString(2);
                standard._status = cursor.getInt(3);

                list.add(standard);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
        return list;
    }


    public List<Medium> getAllMediumMaster()
    {
        List<Medium> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_MEDIUM + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Medium medium = new Medium();

                medium.id = cursor.getInt(0);
                medium.medium = cursor.getString(1);
                medium.timestamp = cursor.getString(2);
                medium.status = cursor.getInt(3);

                list.add(medium);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
        return list;
    }

    public List<Board> getAllBoardMaster()
    {
        List<Board> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_BOARD + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Board board = new Board();

                board.id = cursor.getInt(0);
                board.board = cursor.getString(1);
                board.timestamp = cursor.getString(2);
                board.status = cursor.getInt(3);

                list.add(board);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
        return list;
    }

    public List<Qualification> getAllQualificationMaster()
    {
        List<Qualification> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_QUALIFICATION + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                Qualification qualification = new Qualification();

                qualification.id = cursor.getInt(0);
                qualification.qualification = cursor.getString(1);
                qualification.timestamp = cursor.getString(2);
                qualification.status = cursor.getInt(3);

                list.add(qualification);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
        return list;
    }


    public List<FeesRange> getAllFeesRangeMaster()
    {
        List<FeesRange> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MASTER_FEES_RANGE + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                FeesRange range = new FeesRange();

                range.id = cursor.getInt(0);
                range.fees_range = cursor.getString(1);
                range.timestamp = cursor.getString(2);
                range.status = cursor.getInt(3);

                list.add(range);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
        return list;
    }

    public void getAllEducation()
    {

        String selectQuery = "SELECT * FROM " + TABLE_EDUCATION + " WHERE " + KEY_STATUS + "='1'" + " ORDER BY " + KEY_ID + " ASC";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            Education.educations.clear();

            do
            {
                Education education = new Education();

                education.id = cursor.getInt(0);
                education.qualification = cursor.getString(1);
                education.division = cursor.getString(2);
                education.qualification_status = cursor.getString(3);
                education.academic_achievement = cursor.getString(4);
                education.college = cursor.getString(5);
                education.status = cursor.getInt(6);

                Education.educations.add(education);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
    }

    public void getAllOccupation()
    {
        String selectQuery = "SELECT * FROM " + TABLE_OCCUPATION + " WHERE " + KEY_STATUS + "='1'" + " ORDER BY " + KEY_ID + " ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            Occupation.occupationList.clear();

            do
            {
                Occupation occupation = new Occupation();

                occupation.id = cursor.getInt(0);
                occupation.occupation = cursor.getString(1);
                occupation.organization = cursor.getString(2);
                occupation.start_date = cursor.getString(3);
                occupation.end_date = cursor.getString(4);
                occupation.status = cursor.getInt(5);

                Occupation.occupationList.add(occupation);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();
    }

    public void update(Occupation occupation)
    {
        String updateQuery = "UPDATE " + TABLE_OCCUPATION + " SET " + KEY_OCCUPATION + "='" + occupation.occupation + "', "
                + KEY_ORGANIZATION + "='" + occupation.organization + "', " + KEY_START_DATE + "='" + occupation.start_date + "', "
                + KEY_END_DATE+ "='" + occupation.end_date + "', " + KEY_STATUS + "='" + occupation.status + "', "
                + KEY_SYNC_STATUS + "='0'" + " WHERE " + KEY_ID + "='" + occupation.id +"'";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(updateQuery);
        database.close();
    }

    public void update(Education education)
    {
        String updateQuery = "UPDATE " + TABLE_EDUCATION + " SET " + KEY_QUALIFICATION + "='" + education.qualification + "', "
                + KEY_DIVISION + "='" + education.division + "', " + KEY_QUALIFICATION_STATUS + "='" + education.qualification_status + "', "
                + KEY_ACHIEVEMENT+ "='" + education.academic_achievement + "', " + KEY_COLLEGE + "='" + education.college + "', "
                + KEY_STATUS + "='" + education.status + "', " + KEY_SYNC_STATUS + "='0'" + " WHERE " + KEY_ID + "='" + education.id +"'";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(updateQuery);
        database.close();
    }

    public void update_master(String table, int id, int status)
    {
        String updateQuery = "UPDATE " + table + " SET " + KEY_STATUS + "='" + status + "' WHERE " + KEY_ID + "='" + id +"'";

        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(updateQuery);
        database.close();
    }

    /**
     * Update application behaviour
     * @param table_name Pass table name
     */
    public void update(String table_name, int id, int status)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String updateQuery = "UPDATE " + table_name + " SET " + KEY_SYNC_STATUS + " = '" + status + "'"
                + " WHERE " + KEY_ID + " = '" + id + "'";
        Log.d("query",updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    /**
     * Compose education table data to JSON format
     * @return JSON string
     */
    public String composeEducationJSON()
    {
        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_EDUCATION + " WHERE " + KEY_SYNC_STATUS + " = '0'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<>();

                map.put(KEY_ID, cursor.getString(0));
                map.put(KEY_QUALIFICATION, cursor.getString(1));
                map.put(KEY_DIVISION, cursor.getString(2));
                map.put(KEY_QUALIFICATION_STATUS, cursor.getString(3));
                map.put(KEY_ACHIEVEMENT, cursor.getString(4));
                map.put(KEY_COLLEGE, cursor.getString(5));
                map.put(KEY_STATUS, cursor.getString(6));

                wordList.add(map);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();

        Gson gson = new GsonBuilder().create();

        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }


    /**
     * Compose education table data to JSON format
     * @return JSON string
     */
    public String composeOccupationJSON()
    {
        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_OCCUPATION + " WHERE " + KEY_SYNC_STATUS + " = '0'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<>();

                map.put(KEY_ID, cursor.getString(0));
                map.put(KEY_OCCUPATION, cursor.getString(1));
                map.put(KEY_ORGANIZATION, cursor.getString(2));
                map.put(KEY_START_DATE, cursor.getString(3));
                map.put(KEY_END_DATE, cursor.getString(4));
                map.put(KEY_STATUS, cursor.getString(5));

                wordList.add(map);
            }

            while (cursor.moveToNext());
        }

        database.close();
        cursor.close();

        Gson gson = new GsonBuilder().create();

        // Use GSON to serialize Array List to JSON
        return gson.toJson(wordList);
    }
}