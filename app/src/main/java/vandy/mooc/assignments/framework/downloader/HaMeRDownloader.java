package vandy.mooc.assignments.framework.downloader;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import vandy.mooc.assignments.assignment.downloader.ImageDownloader;

/**
 * Created by trach on 10/30/2016.
 */
/**
 * A HaMeR downloader implementation that uses two Runnables and a background
 * thread to download a single image in a background thread.
 * <p/>
 * The base ImageDownloader class provides helper methods to perform the
 * download operation as well as to return the resulting image bitmap to the
 * framework where it will be displayed in a layout ImageView.
 */
public class HaMeRDownloader extends ImageDownloader{
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

    @Override
    public void execute() {
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
                if(isCancelled()) {
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

    @Override
    public void cancel() {
        // If the download thread is alive and running, cancel it by invoking an interrupt.
        if(isRunning()) {
            mThread.interrupt();
        }
    }

    @Override
    public boolean isRunning() {
        // Return 'true' if mThread is running.
        return mThread != null && mThread.isAlive();
    }

    @Override
    public boolean isCancelled() {
        // Return 'true' if mThread has been cancelled.
        return !mThread.isAlive();    }

    @Override
    public boolean hasCompleted() {
        // Return 'true' if mThread has sucessfully completed.
        return finish;
    }
}
