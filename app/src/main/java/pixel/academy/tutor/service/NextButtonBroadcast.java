package pixel.academy.tutor.service;

import android.content.Context;
import android.content.Intent;

public final class NextButtonBroadcast
{

    /**
     * Tag used on log messages.
     */
    static final String TAG = "Golfing Nation Application";

    public static final String NEXT_BUTTON_ACTION =
            "golfing.nation.NEXT";
    public static final String EXTRA_INDEX = "index";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     */
    public static void sendToBroadcast(Context context, int index)
    {
        Intent intent = new Intent(NEXT_BUTTON_ACTION);
        intent.putExtra(EXTRA_INDEX, index);
        context.sendBroadcast(intent);
    }
}