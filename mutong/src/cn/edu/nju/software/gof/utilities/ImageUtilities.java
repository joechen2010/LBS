package cn.edu.nju.software.gof.utilities;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtilities {

	public static Drawable resizeImage(int height, int width, InputStream in,
			Resources resources) {
		Bitmap image = BitmapFactory.decodeStream(in);

		int origWidth = image.getWidth();
		int origHeight = image.getHeight();

		float scaleWidth = ((float) width) / origWidth;
		float scaleHeight = ((float) height) / origHeight;

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, origWidth, origHeight,
				matrix, true);

		return new BitmapDrawable(resources, resizedBitmap);
	}
}
