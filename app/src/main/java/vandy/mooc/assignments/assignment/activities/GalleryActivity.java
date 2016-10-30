
package vandy.mooc.assignments.assignment.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import vandy.mooc.assignments.R;
import vandy.mooc.assignments.assignment.downloader.HaMeRDownloader;
import vandy.mooc.assignments.framework.application.activities.GalleryActivityBase;
import vandy.mooc.assignments.framework.utils.UriUtils;
import vandy.mooc.assignments.framework.utils.ViewUtils;

import static android.app.Activity.RESULT_OK;

/**
 * This activity class contains helper methods to support different was you can
 * use Intents to communicate between activities (in the assignments case
 * between the MainActivity and this GalleryActivity).
 */
public class GalleryActivity
        extends GalleryActivityBase {
    /**
     * Intent "extra" string key used to identify the list of image URLs
     */
    public static final String INTENT_EXTRA_URLS = "extra_urls";
    /**
     * Debug logging tag.
     */
    private static final String TAG = "GalleryActivity";

    /*
     * Activity Lifecycle methods.
     */

    /**
     * Factory method that creates and returns and implicit intent that can be
     * used to start this activity.
     *
     * @param context   The context of the calling activity
     * @param inputUrls A list list of input URLs to include as intent extras.
     * @return An intent that can be used to start this activity
     */
    public static Intent makeStartIntent(
            Context context,
            ArrayList<Uri> inputUrls) {
        // TODO (A1,A2,A3): Create a new intent for starting this activity
        // using the passed context along with the class identifier
        // for this class.
        Intent intent = new Intent(context, GalleryActivity.class);

        // TODO (A1,A2,A3): Put the received list of input URLs as an intent
        // use putParcelableArrayListExtra(String, ArrayList<Uri>) on the intent
        // using the predefined INTENT_EXTRA_URLS extra name.
        intent.putParcelableArrayListExtra(INTENT_EXTRA_URLS, inputUrls);

        // TODO (A1,A2,A3): Return the intent.
        return intent;
    }

    /*
     * Factory methods.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class method first. Normally you would follow this
        // call with a call to inflate the activity's layout from XML, but this
        // is not necessary here because the assignment super class method will
        // do that for you.
        super.onCreate(savedInstanceState);

        // When savedInstanceState is null, the activity is being started for
        // the first time, and when not null, the activity is being recreated
        // after a configuration change.
        //noinspection StatementWithEmptyBody
        if (savedInstanceState == null) {
            // The activity is being started for the first time.
            List<Uri> uris = extractInputUrlsFromIntent(getIntent());
            // TODO (A1,A2,A3,A4):
            // Call local help method to extract the URLs from the activity's
            // starting intent and pass these URLs into the super class using
            // the setItems() helper method.
            if(uris!=null){
                super.setItems(uris);
            }
        } else {
            // The activity is being recreated after configuration change.
            // You can restore your activity's saved state from the passed
            // savedInstanceState either here or in onRestoreInstanceState().
            // This framework will automatically save and restore the
            // displayed URL list using onSaveInstanceState() and
            // onRestoreInstanceState() so you don't need to do anything here.
        }

        // TODO(A2): Call base class helper method to register your downloader
        // implementation class.
        super.registerDownloader(HaMeRDownloader.class);
    }

    /**
     * Extract, validate, and returns the image urls received in the starting
     * Intent.
     *
     * @return A list of image URLs.
     */
    private List<Uri> extractInputUrlsFromIntent(Intent intent) {
        // TODO (A1): First extract the list of input urls from the passed
        // intent extras using the provided INTENT_EXTRA_URLS name string.
        // Next, validate the extracted list URL strings by calling the local
        // validateInput() helper method. If the entire list of received URLs
        // are valid, then return this list. Otherwise return null.
        ArrayList<Uri> uriArrayList = intent.getParcelableArrayListExtra(INTENT_EXTRA_URLS);
        if(validateInput(uriArrayList)){
            return uriArrayList;
        }
        return null;
    }

    /**
     * Ensures that the input to this activity is valid and reports errors if
     * the received URL list is null, empty, or contains any mal-formed URLs.
     *
     * @param inputUrls A List of image URLs to validate.
     * @return {@code true} if the input URL list is valid; {@code false} on
     * first error encountered.
     */
    private boolean validateInput(ArrayList<Uri> inputUrls) {
        // TODO (A1,A2,A3,A4): Validate the passed URL.
        //
        // If the list is null call ViewUtils.showToast() to display the
        // string R.string.input_url_list_is_null.
        //
        if(inputUrls==null ){
            ViewUtils.showToast(this, R.string.input_url_list_is_null,null);
            return false;
        }
        // If the list has a size of 0 then call ViewUtils.showToast()
        // to display the the string R.string.input_url_list_is_empty
        //
        if(inputUrls.isEmpty()){
            ViewUtils.showToast(this, R.string.input_url_list_is_empty,null);
            return false;
        }
        // Otherwise check if each list entry is valid using the
        // UriUtils.isValidUrl() helper and if any URL is not valid
        // return false.
        //
        for(Uri uri: inputUrls){
            if(!UriUtils.isValidUrl(uri.toString())){
                return false;
            }
        }

        // Return true if all the URLs are valid.
        return true;

    }

    /**
     * Factory method that creates and returns a results (data) intent that
     * contains the list of downloaded image URIs. This intent can then be
     * passed to this activity's setResult() method which will inform the
     * application framework to return this intent to the parent activity.
     * <p/>
     * Note: this method is declared public for integration tests.
     *
     * @param outputUrls A List of local image URLs to add as an intent extra.
     * @return An intent to return the parent activity
     */
    @SuppressWarnings("WeakerAccess")
    protected Intent makeResultIntent(ArrayList<Uri> outputUrls) {
        // TODO (A2): create a new data intent, put the received
        // outputUrls list into the intent as an ParcelableArrayListExtra,
        // and return the intent.
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putParcelableArrayListExtra(INTENT_EXTRA_URLS, outputUrls);
        return intent;
    }

    /**
     * Creates a data results intent, sets the activity result to this intent,
     * and finishes this activity.
     *
     * @param urls The currently displayed list of image URLs.
     */
    protected void createAndReturnResultsIntent(
            @NonNull ArrayList<Uri> urls) {
        Log.d(TAG, "Setting a result intent.");

        // TODO (A2): Call makeResultIntent to construct a return
        // intent that contains the list of currently displayed URLs
        // as an intent extra.
        Intent intent = makeResultIntent(urls);
        // TODO (A2): Now set the result intent to return.

        setResult(RESULT_OK, intent);
        finish();

        // TODO (A2): Call the appropriate Activity base class method to end
        // this activity and return to parent activity.


        Log.d(TAG, "Activity finished.");
    }

}
