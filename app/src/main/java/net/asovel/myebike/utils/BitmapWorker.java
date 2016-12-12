package net.asovel.myebike.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

public class BitmapWorker
{
    public static boolean cancelPotentialWork(String code, ImageView imageView)
    {
        final DownloadBitmapTask task = getBitmapWorkerTask(imageView);

        if (task != null)
        {
            final String bitmapCode = task.urlString;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapCode == null || !bitmapCode.equals(code))
            {
                // Cancel previous task
                task.cancel(true);
            } else
            {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static DownloadBitmapTask getBitmapWorkerTask(ImageView imageView)
    {
        if (imageView != null)
        {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable)
            {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static class AsyncDrawable extends BitmapDrawable
    {
        private final WeakReference<DownloadBitmapTask> weakTask;

        public AsyncDrawable(Resources res, Bitmap bitmap, DownloadBitmapTask task)
        {
            super(res, bitmap);
            weakTask = new WeakReference<DownloadBitmapTask>(task);
        }

        public DownloadBitmapTask getBitmapWorkerTask()
        {
            return weakTask.get();
        }
    }

    public static class DownloadBitmapTask extends AsyncTask<String, Integer, Bitmap>
    {
        private WeakReference<ImageView> weakImageView;
        private int imagenW;
        private int imagenH;
        private String urlString;

        public DownloadBitmapTask(ImageView image, int imagenW, int imagenH)
        {
            weakImageView = new WeakReference<>(image);
            this.imagenW = imagenW;
            this.imagenH = imagenH;
        }

        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            urlString = params[0];
            return downloadBitmap(urlString, imagenW, imagenH);
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (weakImageView != null && bitmap != null)
            {
                ImageView imageView = weakImageView.get();
                if (imageView != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        @Override
        protected void onCancelled()
        {
        }
    }

    public static Bitmap downloadBitmap(String urlString, int imagenW, int imagenH)
    {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream in = null;
        try
        {
            URL url = new URL(urlString);
            //HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //in = urlConnection.getInputStream();
            in = url.openConnection().getInputStream();

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();

            int inSampleSize = calculateInSampleSize(options, imagenW, imagenH);

            options.inJustDecodeBounds = false;
            options.inSampleSize = inSampleSize;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            //in = urlConnection.getInputStream();
            in = url.openConnection().getInputStream();
            bitmap = BitmapFactory.decodeStream(in, null, options);

        } catch (Exception e)
        {
        } finally
        {
            if (in != null)
                try
                {
                    in.close();
                } catch (Exception e)
                {
                }
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth)
            {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
