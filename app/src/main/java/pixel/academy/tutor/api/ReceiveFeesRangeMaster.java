package pixel.academy.tutor.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pixel.academy.tutor.R;
import pixel.academy.tutor.app.MyApplication;
import pixel.academy.tutor.db.SQLiteHelper;
import pixel.academy.tutor.model.FeesRange;

import static pixel.academy.tutor.app.Global.ERROR_TAG;
import static pixel.academy.tutor.app.Global.KEY_FEES_RANGE;
import static pixel.academy.tutor.app.Global.KEY_ID;
import static pixel.academy.tutor.app.Global.KEY_TIMESTAMP;
import static pixel.academy.tutor.app.Global.MAX_RETRIES;
import static pixel.academy.tutor.app.Global.RESPONSE_TAG;
import static pixel.academy.tutor.app.Global.TIMEOUT;


public class ReceiveFeesRangeMaster
{
	private Context context;
	private SQLiteHelper helper;

	public ReceiveFeesRangeMaster(Context context)
	{
		this.context = context;
		this.helper = new SQLiteHelper(context);
	}

	public void execute()
	{
		/**
		 * Server target URL
		 */
		final String URL = context.getResources().getString(R.string.pixelServerBaseUrl)
				+ context.getResources().getString(R.string.pixelServerFeesRangeMasterUrl);

		final StringRequest postRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response)
			{
				try
				{
					Log.v(RESPONSE_TAG, response);

					JSONArray arr = new JSONArray(response);
					JSONObject jsonObj;

					if(arr.length() > 0)
					{
						for (int i = 0; i < arr.length(); i++)
						{
							jsonObj = arr.getJSONObject(i);

							int id = jsonObj.getInt(KEY_ID);
							String fees_range = jsonObj.getString(KEY_FEES_RANGE);
							String timestamp = jsonObj.getString(KEY_TIMESTAMP);

							helper.master_insert(new FeesRange(id, fees_range, timestamp));
						}
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
			}
		});

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