package com.example.layla.cameraopen;

<<<<<<< HEAD
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.hardware.Camera;
import android.view.SurfaceView;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private CameraDevice camera;
    private SurfaceView mCameraView;
    private SurfaceHolder mCameraHolder;
    private Camera mCamera;
    private Button mStart;
    private boolean recording = false;
    private MediaRecorder mediaRecorder;
    private ImageView imageView;
=======
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    ImageButton button_c;
    ImageButton button_switch;
    String str;
    Uri imageUri;
    Camera.PictureCallback jpegCallback;
    int camId = 0;
>>>>>>> parent of 31017ff... ㅇㅅㅇ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.activity_main);
        mStart = (Button)findViewById(R.id.button);
        imageView = (ImageView)findViewById(R.id.imageView);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCamera!=null){
                    mCamera.takePicture(null, null, takePicture);
                }
            }
        });
        mCameraView = (SurfaceView)findViewById(R.id.cameraView);

        init();
    }


    private Camera.PictureCallback takePicture = new Camera.PictureCallback(){
        public void onPictureTaken(byte[] data, Camera camera){
            if(data!=null){
                BitmapFactory.Options scalingOptions = new BitmapFactory.Options();
                scalingOptions.inSampleSize = camera.getParameters().getPictureSize().width / imageView.getMeasuredWidth();
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, scalingOptions);
                //imageView.setRotationX(90);
                bmp = imgRotate(bmp);
                imageView.setImageBitmap(bmp);
                imageView.setVisibility(ImageView.VISIBLE);


                camera.startPreview();
            }
        }
    };

    private void init(){

        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);

        // surfaceview setting
        mCameraHolder = mCameraView.getHolder();
        mCameraHolder.addCallback(this);
        mCameraHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    // surfaceholder 와 관련된 구현 내용
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (mCamera == null) {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // View 가 존재하지 않을 때
        if (mCameraHolder.getSurface() == null) {
            return;
        }
        // 작업을 위해 잠시 멈춘다
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // 에러가 나더라도 무시한다.
        }
        // 카메라 설정을 다시 한다.
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        mCamera.setParameters(parameters);

        // View 를 재생성한다.
        try {
            mCamera.setPreviewDisplay(mCameraHolder);
            mCamera.startPreview();
=======
        setContentView(R.layout.activity_camera);

        button_switch = (ImageButton) findViewById(R.id.switchb);
        button_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });
        button_c = (ImageButton) findViewById(R.id.capture);
        button_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, jpegCallback);
            }
        });
        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceView = (SurfaceView) findViewById(R.id.cameraView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, null);
                Matrix matrix = new Matrix();
                if (camId == 1) {
                    matrix.preRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    matrix.setScale(1, -1);
                    Bitmap adjustedBitmap = Bitmap.createBitmap(rotatedBitmap, 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix, false);
                    try {
                        str = String.format("/sdcard/DCIM/%d.jpg", System.currentTimeMillis());
                        //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        outStream = new FileOutputStream(str);
                        adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.write(data);
                        outStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                } else {
                    matrix.preRotate(90);
                    Bitmap adjustedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    try {
                        str = String.format("/sdcard/DCIM/%d.jpg", System.currentTimeMillis());
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        outStream = new FileOutputStream(str);
                        adjustedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                        outStream.write(data);
                        outStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                    }
                }
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                refreshCamera();

                Intent intent = new Intent(CameraActivity.this, PhotoActivity.class);
                intent.putExtra("str", str);
                startActivity(intent);
            }
        };
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        } catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
>>>>>>> parent of 31017ff... ㅇㅅㅇ
        } catch (Exception e) {
        }
    }

    @Override
<<<<<<< HEAD
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Bitmap imgRotate(Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }

=======
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open(camId);
        camera.stopPreview();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void switchCamera() {
        int camNum = 0;
        camNum = Camera.getNumberOfCameras();
        //int camBackId = Camera.CameraInfo.CAMERA_FACING_BACK;
        //int camFrontId = Camera.CameraInfo.CAMERA_FACING_FRONT;

        Camera.CameraInfo currentCamInfo = new Camera.CameraInfo();

        //if camera is running
        if (camera != null) {
            //and there is more than one camera
            if (camNum > 1) {
                //stop current camera
                camera.setDisplayOrientation(270);
                camera.stopPreview();
                camera.setPreviewCallback(null);
                //camera.takePicture(null, null, PictureCallback);
                camera.release();
                camera = null;
                //stop surfaceHolder?

                if (camId == 0) {
                    //switch camera to back camera 셀카
                    camId = 1;
                } else {
                    //switch camera to front camera
                    camId = 0;
                }
                //switch camera back on
                //specify surface?
                //Toast.makeText(getApplicationContext(), "camId"+camId, Toast.LENGTH_SHORT).show();
                camera = Camera.open(camId);
                camera.stopPreview();
                camera.setDisplayOrientation(90);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
>>>>>>> parent of 31017ff... ㅇㅅㅇ
}