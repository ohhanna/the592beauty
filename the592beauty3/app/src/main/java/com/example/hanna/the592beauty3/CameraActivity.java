package com.example.hanna.the592beauty3;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                final ColorWeight weight = (ColorWeight) getApplicationContext();
                if(weight.getWhich()==0) {
                    Intent intent = new Intent(CameraActivity.this, PhotoActivity.class);
                    intent.putExtra("str", str);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(CameraActivity.this, PhotoEdit.class);
                    intent.putExtra("str", str);
                    startActivity(intent);
                }
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
        } catch (Exception e) {
        }
    }

    @Override
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
                // camera.setDisplayOrientation(270);
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
}