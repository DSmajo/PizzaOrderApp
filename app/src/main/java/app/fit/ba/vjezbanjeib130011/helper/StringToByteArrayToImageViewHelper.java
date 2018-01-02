package app.fit.ba.vjezbanjeib130011.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by HOME on 8.11.2016.
 */

public class StringToByteArrayToImageViewHelper
{
    public static void StringToByteArrayToImageView(Context context, String stringBytes, ImageView imageView)
    {
        byte[] bytes = Base64.decode(stringBytes, Base64.DEFAULT);
        // Convert bytes data into a Bitmap
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        // Set the Bitmap data to the ImageView
        imageView.setImageBitmap(bmp);
    }
}

