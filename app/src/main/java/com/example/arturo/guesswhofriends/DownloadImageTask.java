package com.example.arturo.guesswhofriends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arturo on 8/10/17.
 */

//This class downloads and process an image from internet
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

    //Variable declaration
    private View view; // The view where the image will be showed
    private Animation fadeIn, fadeOut;
    private Context context;
    private Bitmap bitmap;

    //Initialize this class
    public DownloadImageTask(View view, Context context){
        this.view = view;
        this.context = context;
    }

    //Get an image from internet and converts it to Bitmap object
    @Override
    protected Bitmap doInBackground(String... params) { //This method will be executed in a different thread
        String url = params[0];
        Bitmap image;
        try{
            InputStream inputStream = new URL(url).openStream();
            image = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (MalformedURLException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
        return image;
    }

    //When the doInBackground method finishes, this method process the result by adding a blur effect
    @Override
    public void onPostExecute(Bitmap result) { //This method is executed in the GUI thread
        bitmap = result;

        if(bitmap != null){
            //Gets fadeOut and fadeIn animations from resource file (xml)
            fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out);
            fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            //Creates listener to fadeOut animation
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //Starts fadeIn animation and changes the background when the fadeOut animation ends
                    bitmap = blurRenderScript(context, bitmap, 25);
                    view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
                    view.startAnimation(fadeIn);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            //Adds the listener above to fadeOut animation
            view.startAnimation(fadeOut);
        }
    }

    //Returns the given Bitmap object with a blur effect
    public static Bitmap blurRenderScript(Context context,Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    //Converts a given Bitmap from RGB565 to ARGB888
    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }
}
