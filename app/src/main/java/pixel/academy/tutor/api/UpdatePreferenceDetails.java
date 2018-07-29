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
import pixel.academy.tutor.helper.OnTaskCompleted;
import pixel.academy.tutor.helper.Security;

import static pixel.academy.tutor.app.Global.CONNECTIVITY_ERROR;
import static pixel.academy.tutor.app.Global.ERROR_TAG;
import static pixel.academy.tutor.app.Global.JSON_TAG;
import static pixel.academy.tutor.app.Global.MAX_RETRIES;
import static pixel.academy.tutor.app.Global.MESSAGE;
import static pixel.academy.tutor.app.Global.RESPONSE_TAG;
import static pixel.academy.tutor.app.Global.STATUS_CODE;
import static pixel.academy.tutor.app.Global.TIMEOUT;
import static pixel.academy.tutor.app.Global.USER;
import static pixel.academy.tutor.configuration.Configuration.SECRET_KEY;


public class UpdatePreferenceDetails
{
	private OnTaskCompleted listener;
	private Context context;

	public UpdatePreferenceDetails(Context context , OnTaskCompleted listener)
	{
		this.listener = listener;
		this.context = context;
	}

	public void execute(final String json_data, final String user_id)
	{
		/**
		 * Server target URL
		 */
		final String URL = context.getResources().getString(R.string.pixelServerBaseUrl)
				+ context.getResources().getString(R.string.pixelServerPreferenceUrl);

		final StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response)
			{
				try
				{
					Log.v(RESPONSE_TAG, "" + response);

					JSONObject jsonObj = new JSONObject(response);

					int status_code = jsonObj.getInt(STATUS_CODE);
					String message = jsonObj.getString(MESSAGE);

					if (status_code == 200)
					{
						/**
						 * Successful
						 */
						listener.onTaskCompleted(true, status_code, message);
					}

					else
					{
						/**
						 * Unsuccessful
						 */
						listener.onTaskCompleted(false, status_code, message);
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
				listener.onTaskCompleted(false, 500, CONNECTIVITY_ERROR);
			}
		})

		{

			@Override
			protected Map<String, String> getParams()
			{

				Map<String, String> params = new HashMap<>();

				try
				{
					//params.put("responseJSON", Security.encrypt(json_data, preferences.getString("key", null)));
					params.put(USER, Security.encrypt(user_id, SECRET_KEY));
					params.put(JSON_TAG, json_data);
				}

				catch (Exception e)
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