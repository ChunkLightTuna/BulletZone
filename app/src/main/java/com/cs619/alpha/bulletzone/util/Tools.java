package com.cs619.alpha.bulletzone.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by Chris Oelerich on 5/1/16.
 */
public class Tools {


  /**
   * http://stackoverflow.com/a/11306854
   *
   * @return String
   */
  public static String getCallerCallerClassName() {
    StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
    String callerClassName = null;
    for (int i = 1; i < stElements.length; i++) {
      StackTraceElement ste = stElements[i];
      if (!ste.getClassName().equals(Tools.class.getName()) && ste.getClassName().indexOf("java.lang.Thread") != 0) {
        if (callerClassName == null) {
          callerClassName = ste.getClassName();
        } else if (!callerClassName.equals(ste.getClassName())) {
          return ste.getClassName();
        }
      }
    }
    return null;
  }


  /**
   * @see //groups.google.com/group/android-developers/browse_thread/thread/9e215c83c3819953
   * @see //gskinner.com/blog/archives/2007/12/colormatrix_cla.html
   */
  public static class ColorFilterGenerator {

    /**
     * Creates a HUE adjustment ColorFilter
     *
     * @param value float
     * @return ColorFilter
     */
    public static ColorFilter adjustHue(float value) {
      ColorMatrix cm = new ColorMatrix();

      adjustHue(cm, value);

      return new ColorMatrixColorFilter(cm);
    }

    /**
     * @param cm    ColorMatrix
     * @param value float
     */
    public static void adjustHue(ColorMatrix cm, float value) {
      value = cleanValue(value, 180f) / 180f * (float) Math.PI;
      if (value == 0) {
        return;
      }
      float cosVal = (float) Math.cos(value);
      float sinVal = (float) Math.sin(value);
      float lumR = 0.213f;
      float lumG = 0.715f;
      float lumB = 0.072f;
      float[] mat = new float[]
          {
              lumR + cosVal * (1 - lumR) + sinVal * (-lumR), lumG + cosVal * (-lumG) + sinVal * (-lumG), lumB + cosVal * (-lumB) + sinVal * (1 - lumB), 0, 0,
              lumR + cosVal * (-lumR) + sinVal * (0.143f), lumG + cosVal * (1 - lumG) + sinVal * (0.140f), lumB + cosVal * (-lumB) + sinVal * (-0.283f), 0, 0,
              lumR + cosVal * (-lumR) + sinVal * (-(1 - lumR)), lumG + cosVal * (-lumG) + sinVal * (lumG), lumB + cosVal * (1 - lumB) + sinVal * (lumB), 0, 0,
              0f, 0f, 0f, 1f, 0f,
              0f, 0f, 0f, 0f, 1f};
      cm.postConcat(new ColorMatrix(mat));
    }

    private static float cleanValue(float p_val, float p_limit) {
      return Math.min(p_limit, Math.max(-p_limit, p_val));
    }
  }

  /**
   *
   * @param image image to be rescalled
   * @param scaleFactor
   * @param context for resources
   * @return Drawable
   */
  public static Drawable scaleImage(Drawable image, float scaleFactor, Context context) {

    if ((image == null) || !(image instanceof BitmapDrawable)) {
      return image;
    }

    Bitmap b = ((BitmapDrawable) image).getBitmap();

    int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
    int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);

    Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

    image = new BitmapDrawable(context.getResources(), bitmapResized);

    return image;

  }

}
