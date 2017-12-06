package zeng.com.opencv_test03;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private Button btnOne,btnTwo;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        verifyStoragePermissions(this);
        TextView tv = (TextView) findViewById(R.id.textView);
        //tv.setText(stringFromJNI());
        try {
            tv.setText(Environment.getExternalStorageDirectory().getCanonicalPath().toString());
        }catch (Exception e) {

        }



    }

    public void initView()
    {
        setContentView(R.layout.activity_main);
        btnOne = (Button) findViewById(R.id.button);
        btnTwo = (Button) findViewById(R.id.button2);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = (ImageView) findViewById(R.id.imageView2);
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                        R.drawable.people)).getBitmap();
                img.setImageBitmap(bitmap);

                TextView tv = (TextView) findViewById(R.id.textView);
                StringBuffer strsBuffer = new StringBuffer();
                try {
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getCanonicalPath() + "/1.txt");
                    FileInputStream fileR = new FileInputStream(file);
                    BufferedReader reads = new BufferedReader(
                            new InputStreamReader(fileR));
                    String st = null;
                    while ((st = reads.readLine()) != null) {
                        strsBuffer.append(st);
                    }
                    fileR.close();
                    tv.setText(strsBuffer.toString());
                }catch (Exception e){
                    tv.setText("11");
                }
            }
        });

        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView img = (ImageView) findViewById(R.id.imageView2);
                Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                        R.drawable.people)).getBitmap();

                int w = bitmap.getWidth(), h=bitmap.getHeight();
                int[] pix = new int[w*h];
                bitmap.getPixels(pix,0, w, 0, 0, w, h);
                int[] resultpix = new int[w*h];
                rgb2Gray(pix,resultpix,w,h);
                Bitmap result = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
                result.setPixels(resultpix, 0, w, 0, 0,w, h);

                img.setImageBitmap(result);

                TextView tv = (TextView) findViewById(R.id.textView);
                tv.setText(stringFromJNI());
            }
        });

    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    public static void verifyStoragePermissions(Activity activity) {

        try {

            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native int rgb2Gray(int[] srcImg, int[] resImg, int w, int h);
}