package vandy.mooc.assignments.assignment.downloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * A HaMeR downloader implementation that uses two Runnables and a background
 * thread to download a single image in a background thread.
 * <p/>
 * The base ImageDownloader class provides helper methods to perform the
 * download operation as well as to return the resulting image bitmap to the
 * framework where it will be displayed in a layout ImageView.
 */
public class HaMeRDownloader extends ImageDownloader {
    /**
     * Logging tag.
     */
    private static final String TAG = "HaMeRDownloader";

    /**
     * Create a new handler that is associated with the main thread looper.
     */
    // Create a private final Handler associated with the main thread looper.
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * A reference to the background thread to support the cancel hook.
     */
    private Thread mThread;

    private boolean finish = false;

    /**
     * Create a new handler that is associated with the main thread looper.
     */
    // TODO A2: Create a private final Handler associated with the main
    // thread looper. Note that this class and all its fields are instantiated
    // in the main thread.

    /**
     * Starts the asynchronous download request.
     */
    @Override
    public void execute() {
        // TODO A2: Create a new final Runnable called 'downloadRunnable' to
        // process the download request (replace the null).


            // TODO A2: Within the new runnable's run() method:

                // TODO A2: Call the HaMeRDownloader helper method to
                // determine if this thread has been interrupted; if so,
                // just return to terminate the thread.


                // TODO A2: Call ImageDownloader super class helper method
                // to download the bitmap.


                // TODO A2: Since the download my take a while, check again to
                // make sure that this thread has not been interrupted (using
                // the same helper method as above); if it has been interrupted
                // then just return to terminate the thread.


                // TODO A2: Use the mHandler post(...) helper method to post
                // a new Runnable to the main thread. This Runnable's run()
                // method should simply call the ImageDownloader super class
                // helper method postResult(...) to pass the downloaded bitmap
                // to the application UI layer (activity) to display.


        // TODO A2: Create a new Thread instance that will run the
        // 'downloadRunnable' created above, and assign it to the mThread
        // field. This assignment is necessary to support cancelling this
        // thread and the download operation.


        // TODO A2: Start the thread.
        Runnable downloadRunnable = new Runnable() {
            @Override
            public void run() {
                finish = false;
                // Make sure that this thread has not been interupted.
                if(isCancelled()) {
                    return;
                }
                // Call Downloader abstract class helper to perform the request download and decoding into the required resource type.
                final Bitmap image = download();

                // Make sure (again) that this thread has not been interupted.
                if(!isRunning()) {
                    return;
                }

                // Create a new runnable that calls setResource() and post it to be run on the main thread.
                final Runnable setResource = new Runnable() {
                    // Call the super class setResource helper method to set the ImageView bitmap and to make any callbacks needed.
                    @Override
                    public void run() {
                        postResult(image);
                    }
                };

                mHandler.post(setResource);
            }
        };
        mThread = new Thread(downloadRunnable);
        // Create and start an anonymous Thread to execute 'downloadRunnable'.
        mThread.start();
        finish = true;

    }

    /**
     * Cancels the current download operation.
     */
    @Override
    public void cancel() {
        // TODO A2: Call local isRunning() helper method to check if mThread
        // is currently running; if it is running, cancel it by calling its
        // interrupt() helper method.
        // If the download thread is alive and running, cancel it by invoking an interrupt.
        if(isRunning()) {
            mThread.interrupt();
        }
    }

    /**
     * Checks to see if the download thread has been interrupted.
     *
     * @return {@code true} if the calling thread has been interrupted; {@code
     * false} if not.
     */
    private boolean isInterrupted() {
        // TODO A2: return 'true' if mThread is not null and has been
        // interrupted (see isInterrupted() helper method).
        // Return 'true' if mThread is running.
        return mThread != null && mThread.isAlive();
    }

    /**
     * Reports if the download thread is currently running.
     *
     * @return {@code true} if the thread is running; {@code false} if not.
     */
    @Override
    public boolean isRunning() {
        // TODO A2: Return 'true' if mThread is not null and is running
        // (see isAlive() helper method)
        // Return 'true' if mThread is running.
        return mThread != null && mThread.isAlive();

    }

    /**
     * Reports if the download thread has been cancelled.
     *
     * @return {@code true} if the thread has cancelled ; {@code false} if not.
     */
    @Override
    public boolean isCancelled() {
        // TODO A2: Return 'true' if mThread is not null and has been
        // interrupted (see isInterrupted() helper method).
        return !isRunning();
    }

    /**
     * Reports if the download thread has completed.
     *
     * @return {@code true} if the thread has successfully completed; {@code
     * false} if not.
     */
    @Override
    public boolean hasCompleted() {
        // TODO A2: Return 'true' if mThread is not null and has successfully
        // terminated (completed). To determine if a thread has terminated,
        // you will need to use the Thread's getState() helper method and
        // compare it to the the appropriate Thread.State enumerated value.
        return finish;
    }
}
