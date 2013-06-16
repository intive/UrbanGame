package com.blstream.urbangame.database.helper;

import java.io.ByteArrayOutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class Base64ImageCoder {
	
	/**
	 * @param imageBase64 - image in base64 format
	 * @param res - can be obtained by getResources() from Activity
	 * @return - decoded image in drawable
	 */
	public static Drawable decodeImage(String imageBase64, Resources res) {
		byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		return new BitmapDrawable(res, decodedByte);
	}
	
	public static Bitmap getBitmapFromStringBase64(String imageBase64) {
		byte[] byteImage = Base64.decode(imageBase64, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
	}
	
	/**
	 * @param image - image to be encoded into base64 format
	 * @return string with encoded image to base64 format
	 */
	public static String convertImage(Drawable image) {
		Bitmap bitmap = drawableToBitmap(image);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, stream);
		byte[] im = stream.toByteArray();
		String encodedImage = Base64.encodeToString(im, Base64.DEFAULT);
		return encodedImage;
	}
	
	private static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();
		
		int width = drawable.getIntrinsicWidth();
		width = width > 0 ? width : 1;
		int height = drawable.getIntrinsicHeight();
		height = height > 0 ? height : 1;
		
		Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		
		return bitmap;
	}
}
