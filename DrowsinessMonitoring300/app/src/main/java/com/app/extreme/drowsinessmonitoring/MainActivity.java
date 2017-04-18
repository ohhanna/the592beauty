package com.app.extreme.drowsinessmonitoring;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.media.MediaPlayer;
import android.widget.FrameLayout;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2
{
    private static final String TAG = "Activity";

    private CameraBridgeViewBase   m_CameraView;

    private Mat m_Rgba;
    private Mat m_Gray;

    private int m_FrameWidth = 640;
    private int m_FrameHeight = 480;

    FaceDetector facedetector = new FaceDetector(240, 320, 5);

    private BaseLoaderCallback m_LoaderCallback = new BaseLoaderCallback(this)
    {
        public void onManagerConnected(int status)
        {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    m_CameraView.enableView();
                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public MainActivity()
    {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    public void onCreate(Bundle savedInstanceState)
    {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        m_CameraView = (CameraBridgeViewBase) findViewById(R.id.activity_java_surface_view);
        m_CameraView.setVisibility(SurfaceView.VISIBLE);
        m_CameraView.setCvCameraViewListener(this);
        m_CameraView.setMaxFrameSize(640, 480);

    }

    public void onPause()
    {
        super.onPause();

        if (m_CameraView != null)
            m_CameraView.disableView();
    }

    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug())
        {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, m_LoaderCallback);
        }
        else
        {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            m_LoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        if (m_CameraView != null)
            m_CameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height)
    {
        m_Rgba = new Mat(height, width, CvType.CV_8UC4);
        m_Gray = new Mat(height, width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped()
    {
        if (m_CameraView != null)
            m_CameraView.disableView();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame)
    {
        m_Rgba = inputFrame.rgba();
//        Core.transpose(inputFrame.rgba(), m_Rgba);
        Core.flip(m_Rgba, m_Rgba, 1);
        Bitmap bitmap = Bitmap.createBitmap(m_Rgba.cols(), m_Rgba.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(m_Rgba, bitmap);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 240, 320, true);

        FaceDetector.Face[] faces = new FaceDetector.Face[5];
        int num = 0;
        num = facedetector.findFaces(resizedBitmap, faces);

//        for(int i = 0; i < num; i++)
//        {
//            PointF point = new PointF();
//            faces[i].getMidPoint(point);
//            Point cvPoint = new Point(point.x *2, point.y *2);
//            Scalar scalar = new Scalar(0,255,0);
//            Imgproc.circle(m_Rgba, cvPoint, 10, scalar, -1);
//        }
        //faces[0].eyesDistance();
         resizedBitmap.recycle();

        return m_Rgba;
    }
}