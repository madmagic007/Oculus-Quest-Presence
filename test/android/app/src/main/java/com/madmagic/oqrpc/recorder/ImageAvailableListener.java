package com.madmagic.oqrpc.recorder;

import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;
import com.madmagic.oqrpc.main.MainActivity;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class ImageAvailableListener implements ImageReader.OnImageAvailableListener {

    public StreamingSocket socket;

    @Override
    public void onImageAvailable(ImageReader reader) {
        try {
            Image image = reader.acquireLatestImage();

            if (image != null) {
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * MainActivity.mDisplayWidth;

                Bitmap bitmap = Bitmap.createBitmap((MainActivity.mDisplayWidth + rowPadding / pixelStride), MainActivity.mDisplayHeight, Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);

                if (socket != null) socket.streamImage(baos.toByteArray());

                image.close();
            } else {
                Log.d("OQRPC", "is null sad");
            }
        } catch (Exception e) {
            Log.d("OQRPC", Log.getStackTraceString(e));
        }
    }
}
