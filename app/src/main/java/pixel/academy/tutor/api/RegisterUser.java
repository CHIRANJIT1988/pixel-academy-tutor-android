package pixel.academy.tutor.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pixel.academy.tutor.R;
import pixel.academy.tutor.app.MyApplication;
import pixel.academy.tutor.configuration.Configuration;
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.helper.Security;
import pixel.academy.tutor.model.User;
import pixel.academy.tutor.session.SessionManager;

import static pixel.academy.tutor.app.Global.DEVICE_ID;
import static pixel.academy.tutor.app.Global.KEY;
import static pixel.academy.tutor.app.Global.MESSAGE;
import static pixel.academy.tutor.app.Global.MOBILE_NUMBER;
import static pixel.academy.tutor.app.Global.PASSWORD;
import static pixel.academy.tutor.app.Global.RESPONSE_TAG;
import static pixel.academy.tutor.app.Global.STATUS_CODE;
import static pixel.academy.tutor.app.Global.TOKEN;
import static pixel.academy.tutor.app.Global.USER_ID;
import static pixel.academy.tutor.app.Global.USER_NAME;
import static pixel.academy.tutor.app.Global.USER_TYPE;
import static pixel.academy.tutor.app.MyApplication.getInstance;
import static pixel.academy.tutor.app.MyApplication.prefs;
import static pixel.academy.tutor.app.Global.ERROR_TAG;
import static pixel.academy.tutor.app.Global.JSON_TAG;
import static pixel.academy.tutor.app.Global.MAX_RETRIES;
import static pixel.academy.tutor.app.Global.TIMEOUT;


public class RegisterUser
{

	private OnTaskCompleted listener;
	private Context context;

	
	public RegisterUser(Context _context , OnTaskCompleted listener)
	{
		this.listener = listener;
		this.context = _context;
	}


	public void execute(final User user)
	{
		/**
		 * Server target URL
		 */
		final String URL = getInstance().getResources().getString(R.string.pixelServerBaseUrl)
				+ getInstance().getResources().getString(R.string.pixelServerRegisterUrl);

		final StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response)
			{
				try
				{
					Log.v(RESPONSE_TAG, response);

					JSONObject jsonObj = new JSONObject(response);

					int status_code = jsonObj.getInt(STATUS_CODE);
					String message = jsonObj.getString(MESSAGE);

					if (status_code == 200) // checking for error node in json
					{
						/**
						 * Registration Successful
						 */
						SessionManager session = new SessionManager(context);
						user.user_id = Security.decrypt(jsonObj.getString(USER_ID), Configuration.SECRET_KEY);
						session.createLoginSession(user);

						prefs.edit().putString(KEY, Security.decrypt(jsonObj.getString(KEY), Configuration.SECRET_KEY)).apply();

						listener.onTaskCompleted(true, status_code, message); // Successful
					}

					else
					{
						/**
						 * Registration Unsuccessful
						 */
						listener.onTaskCompleted(false, 500, message);
					}
				}

				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error)
			{
				/**
				 * if (error instanceof TimeoutError)
				 */
				Log.v(ERROR_TAG, "" + error.getMessage());
				listener.onTaskCompleted(false, 500, "Internet connection fail. Try Again");
			}
		})

		{
			@Override
			protected Map<String, String> getParams()
			{

				Map<String, String> params = new HashMap<>();

				try
				{
					JSONObject jsonObject = new JSONObject();

					jsonObject.put(TOKEN, user.fcm_reg_id);
					jsonObject.put(USER_NAME, user.name);
					jsonObject.put(MOBILE_NUMBER, user.phone_number);
					jsonObject.put(PASSWORD, user.password);
					jsonObject.put(DEVICE_ID, user.device_id);
					jsonObject.put(USER_TYPE, "T");

					params.put(JSON_TAG, jsonObject.toString());
				}

				catch (JSONException e)
				{
					e.printStackTrace();
				}

				Log.v(JSON_TAG, "" + params);

				return params;
			}
		};

		/**
		 * Retry if Server time out
		 */
		RetryPolicy policy = new DefaultRetryPolicy(TIMEOUT, MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		postRequest.setRetryPolicy(policy);

		/**
		 * Add request to queue
		 */
		MyApplication.getInstance().addToRequestQueue(postRequest);
	}
}