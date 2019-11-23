package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicConvolve3x3;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.R.attr.data;

public class PhotoEdit extends Activity {
    Button btn_Back, btn_Save, btn_Origin, btn_Eye, btn_Chin, btn_Whitening, btn_Blemish,
            btn_Crop, btn_Rotation, btn_Inversion, btn_Intensity, btn_Saturation, btn_Sharpening;
    // 뒤로, 저장, 원본, 눈확대, 갸름하게, 미백, 잡티제거, 자르기, 회전, 역, 명도, 채도, 선명도
    String str;
    String photoPath;
    Bitmap originBitmap;
    Bitmap bitmap;
    Bitmap adjustedBitmap;
    private ImageView imgview;
    SeekBar satBar;
    int cnt_Intensity =0;

    // 픽셀유동화 관련 변수
    int WIDTH = 20;
    int HEIGHT = 20;
    int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    float[] mVerts = new float[COUNT * 2];
    float[] mOrig = new float[COUNT * 2];
    Matrix mMatrix = new Matrix();
    Matrix mInverse = new Matrix();
    int warpcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoedit);
        imgview = (ImageView) findViewById(R.id.imageView);

        satBar = (SeekBar) findViewById(R.id.satBar);
        satBar.setVisibility(View.GONE);

        ColorWeight weight = (ColorWeight) getApplicationContext();
        if (weight.getWhich() == 1) {
            Intent intent = getIntent();
            photoPath = intent.getStringExtra("str");
            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inSampleSize = 4;
            options.inMutable = true;
            final Bitmap bmp;
            bmp = BitmapFactory.decodeFile(photoPath, options);
//            MyView m = new MyView(this);
//            setContentView(m);
            imgview.setImageBitmap(bmp);
            originBitmap = bmp;
            bitmap = bmp;
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            // Gallery 호출
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            // 잘라내기 셋팅
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 150);
            try {
                intent.putExtra("return-data", true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), 2);
            } catch (ActivityNotFoundException e) {
                // Do nothing for now
            }
        }

        // 원래 사진으로 돌아가기
        btn_Origin = (Button) findViewById(R.id.btn_Origin);
        btn_Origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgview.setImageBitmap(originBitmap);
                bitmap = originBitmap;
                satBar.setVisibility(View.INVISIBLE); // ProgressBar 없애기
            }
        });

        // 뒤로
        btn_Back = (Button) findViewById(R.id.btn_Back);
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                satBar.setVisibility(View.INVISIBLE);
                onBackPressed();
            }
        });


        // 저장
        btn_Save = (Button) findViewById(R.id.btn_Save);
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream outStream = null;
                try {
                    str = String.format("/sdcard/DCIM/edit/%d.jpg", System.currentTimeMillis());
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
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PhotoEdit.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        //눈키우기 픽셀유동화 _ none
        btn_Eye = (Button) findViewById(R.id.btn_Eye);
        btn_Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap eyeBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas eyeCanvas = new Canvas(eyeBitmap);
                eyeCanvas.drawBitmap(bitmap, 0, 0, null);
                float h; // 높이
                float w; // 너비
                FaceDetector faceDetector = new
                        FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                h = bitmap.getHeight();
                w = bitmap.getWidth();

                // MeshGrid , 그물 쳐주기
                int index = 0;
                for (int y = 0; y <= HEIGHT; y++){
                    float fy = h * y / HEIGHT;
                    for (int x = 0; x <= WIDTH; x++){
                        float fx = w * x / WIDTH;
                        setXY(mVerts, index, fx, fy); // 밑에 함수
                        setXY(mOrig, index, fx, fy);
                        index += 1;
                    }
                }
                mMatrix.invert(mInverse);
                // ↓눈찾기
                int landmark_count = 0;
                int lefteyex = 0;
                int lefteyey = 0;
                int righteyex = 0;
                int righteyey = 0;

                for (int i = 0; i<faces.size(); i++){
                    Face face = faces.valueAt(i);
                    for(Landmark landmark : face.getLandmarks()){
                        int cx = (int)(landmark.getPosition().x);
                        int cy = (int)(landmark.getPosition().y);
                        landmark_count++;

                        if(landmark_count == 1) {
                            lefteyex = cx;
                            lefteyey = cy;
                        }
                        if(landmark_count == 2) {
                            righteyex = cx;
                            righteyey = cy;
                        }
                    }
                }

                imgview.setImageDrawable(new BitmapDrawable(getResources(), eyeBitmap));
                eyeCanvas.concat(mMatrix);
                // warp _ 눈 키워주기
                eyewarp(lefteyex, lefteyey-5, w, h);
                eyewarp(righteyex, righteyey-7, w, h);
                eyeCanvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, mVerts, 0, null, 0, null);
                //    eyeCanvas.drawCircle(lefteyex, lefteyey, 5,paint);
                imgview.setImageBitmap(eyeBitmap);

                bitmap = eyeBitmap;

                //   satBar.setVisibility(View.INVISIBLE);
            }
        });

        //턱을갸름하게 픽셀유동화 _ none
        btn_Chin = (Button) findViewById(R.id.btn_Chin);
        btn_Chin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap eyeBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                Canvas eyeCanvas = new Canvas(eyeBitmap);
                eyeCanvas.drawBitmap(bitmap, 0, 0, null);
                float h; // 높이
                float w; // 너비
                FaceDetector faceDetector = new
                        FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                h = bitmap.getHeight();
                w = bitmap.getWidth();

                // MeshGrid , 그물 쳐주기
                int index = 0;
                for (int y = 0; y <= HEIGHT; y++){
                    float fy = h * y / HEIGHT;
                    for (int x = 0; x <= WIDTH; x++){
                        float fx = w * x / WIDTH;
                        setXY(mVerts, index, fx, fy); // 밑에 함수
                        setXY(mOrig, index, fx, fy);
                        index += 1;
                    }
                }
                mMatrix.invert(mInverse);
                // ↓코찾기
                int landmark_count = 0;
                int nosex=0;
                int nosey=0;
                int cheekx = 0;
                int cheeky = 0;

                for (int i = 0; i<faces.size(); i++){
                    Face face = faces.valueAt(i);
                    for(Landmark landmark : face.getLandmarks()){
                        int cx = (int)(landmark.getPosition().x);
                        int cy = (int)(landmark.getPosition().y);
                        landmark_count++;

                        if(landmark_count == 3) {
                            nosex = cx;
                            nosey = cy;
                           }

                        if(landmark_count==4) {
                            cheekx = cx;
                            cheeky = cy;
                        }

                    }
                }

                imgview.setImageDrawable(new BitmapDrawable(getResources(), eyeBitmap));
                eyeCanvas.concat(mMatrix);
                // warp _ 갸름하게
                thinwarp(cheekx, cheeky,  nosex, nosey, w, h);
                eyeCanvas.drawBitmapMesh(bitmap, WIDTH, HEIGHT, mVerts, 0, null, 0, null);
                //    eyeCanvas.drawCircle(lefteyex, lefteyey, 5,paint);
                imgview.setImageBitmap(eyeBitmap);
                bitmap = eyeBitmap;
                //   satBar.setVisibility(View.INVISIBLE);
            }
        });


//        // 미백효과 _ none
//        btn_Whitening = (Button) findViewById(R.id.btn_Whitening);
//        btn_Whitening.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                satBar.setVisibility(View.INVISIBLE);
//            }
//        });
//
//        // 피부 _ none
//        btn_Blemish = (Button) findViewById(R.id.btn_Blemish);
//        btn_Blemish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                satBar.setVisibility(View.INVISIBLE);
//            }
//        });

//        // 자르기 _ none
//        btn_Crop = (Button) findViewById(R.id.btn_Crop);
//        btn_Crop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                satBar.setVisibility(View.INVISIBLE);
//            }
//        });

        // 회전 _ fin
        // 회전 _ fin
        btn_Rotation = (Button) findViewById(R.id.btn_Rotation);
        btn_Rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix rotateMatrix = new Matrix();
                rotateMatrix.postRotate(90); // -360 ~ 360
                Bitmap rotateImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);
//                ((BitmapDrawable)imgview.getDrawable()).getBitmap().recycle();
                imgview.setImageBitmap(rotateImage);
                bitmap = rotateImage;
                satBar.setVisibility(View.INVISIBLE);
            }
        });

        // 좌우반전 _ fin
        btn_Inversion = (Button) findViewById(R.id.btn_Inversion);
        btn_Inversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix sideInversion = new Matrix();
                sideInversion.setScale(-1, 1);  // 좌우반전
                Bitmap inverseImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), sideInversion, false);
                imgview.setImageBitmap(inverseImage);
                bitmap = inverseImage;
                satBar.setVisibility(View.INVISIBLE);
            }
        });

        // 밝기조절 _ fin
        btn_Intensity = (Button) findViewById(R.id.btn_Intensity);
        btn_Intensity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                satBar.setOnSeekBarChangeListener(seekBarChangeListener2);
                satBar.setProgress(256);
                cnt_Intensity = 0;
                loadBitmapIntensity();
                satBar.setVisibility(View.VISIBLE);
            }
        });

        // 채도조절 _ fin
        btn_Saturation = (Button) findViewById(R.id.btn_Saturation);
        btn_Saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                satBar.setOnSeekBarChangeListener(seekBarChangeListener);
                satBar.setProgress(256);
                loadBitmapSat();
                satBar.setVisibility(View.VISIBLE);
            }
        });

        // 선명도조절 _ none (세모)
        btn_Sharpening = (Button) findViewById(R.id.btn_Sharpening);
        btn_Sharpening.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap bitmapSharpen = createBitmap_convolve(bitmap, matrix_sharpen);
                imgview.setImageBitmap(bitmapSharpen);
                bitmap = bitmapSharpen;
           }
        });

    }

    // eye _ warp 함수 : pixelfluid
    void eyewarp(float cx, float cy, float w, float h){

        final float K = 10000;
        float[] src = mOrig;
        float[] dst = mVerts;

        if(warpcount != 0){
            src = mVerts;
        }

        for(int i = 0; i < COUNT * 2; i += 2){
            float x = src[i+0];
            float y = src[i+1];
            float dx = cx - x;
            float dy = cy - y;
            float dd = dx*dx + dy*dy;
            float d = (float)Math.sqrt(dd);
            float pull = K / (dd + 0.000001f); // pull : 밀어주는 정도,,,
            pull /= (d + 0.000001f);

            if( pull >= 2.0 ) {
                dst[i + 0] = cx;
                dst[i + 1] = cy;
            }
            else {
                dst[i + 0] = x -(int)(w/5000*dx * pull);
                dst[i + 1] = y -(int)(h/4000*dy * pull);
            }
        }
        warpcount++;
    }


    void thinwarp(float cheekx, float cheeky, float cx, float cy, float w, float h){
        final float K = 10000;
        float[] src = mOrig;
        float[] dst = mVerts;

        if(warpcount != 0){
            src = mVerts;
        }

        float distancex = cheekx - cx;

// kim 사진 기준 : 코 축소할때는 pull이 10, w/500*dx*pull이 적절
        for(int i = 0; i < COUNT * 2; i += 2){
            float x = src[i+0];
            float y = src[i+1];
            float dx = cx - x;
            float dy = cy - y;
            float dd = dx*dx + dy*dy;
            float d = (float)Math.sqrt(dd);
            float pull = K / (dd + 0.000001f); // pull : 밀어주는 정도,,,
            pull /= (d + 0.000001f);

            double standard_push = h*w/240000;

            if( d <= 1.2 * distancex || d >= 1.9 * distancex) {
                dst[i + 0] = x;
                dst[i + 1] = y;
            }
            else {
                if (dx > 0 && dy < 0.75*distancex) {
                    dst[i + 0] = x + 2;
                    dst[i + 1] = y-1;

                }
                else if(dx < 0 && dy < 0.75 * distancex){
                    dst[i+0] = x - 2;
                    dst[i+1] = y-1;
                }
            }
        }
        warpcount++;
    }


    // eye _ XY축 정하기 : pixelfluid // 축그리기
    // chin _ XY축 정하기 : pixelfluid // 축그리기
    void setXY(float[] array, int index, float x, float y){
        array[index * 2 + 0] = x;
        array[index * 2 + 1] = y;
    }



    float[] matrix_sharpen =
            { 0, -1, 0, -1, 5, -1, 0, -1, 0 };
    private Bitmap createBitmap_convolve(Bitmap src, float[] coefficients) {

        Bitmap result = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), src.getConfig());

        RenderScript renderScript = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(renderScript, src);
        Allocation output = Allocation.createFromBitmap(renderScript, result);

        ScriptIntrinsicConvolve3x3 convolution = ScriptIntrinsicConvolve3x3
                .create(renderScript, Element.U8_4(renderScript));
        convolution.setInput(input);
        convolution.setCoefficients(coefficients);
        convolution.forEach(output);

        output.copyTo(result);
        renderScript.destroy();
        return result;
    }

    // 사진불러오기
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri imageUri = data.getData();
        try {
            Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            photoPath = imageUri.getPath();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            //Toast.makeText(getApplicationContext(), "path : "+photoPath, Toast.LENGTH_LONG).show();
            if (photo.getHeight() < photo.getWidth()) {
                photo = imgRotate(photo);
            }
            imgview.setImageBitmap(photo);
            originBitmap = photo;
            bitmap = photo;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "다른사진을 선택해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "다른사진을 선택해주세요", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        }
    }

    // 비트맵회전
    private Bitmap imgRotate(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();

        return resizedBitmap;
    }
    public void onBackPressed() {
        super.onBackPressed();
    }

    // 채도 함수 : 비트맵 _ fin
    private void loadBitmapSat() {
        if (bitmap != null) {
            int progressSat = satBar.getProgress();

            //Saturation, 0=gray-scale. 1=identity
            float sat = (float) progressSat / 256;
            //satText.setText("Saturation: " + String.valueOf(sat));
            imgview.setImageBitmap(updateSat(bitmap, sat));
        }
    }

    float oldintensity;
    // 밝기 함수 : 비트맵 _fin
    private void loadBitmapIntensity(){
        if(bitmap!=null){
            int progressIntensity = satBar.getProgress();

            float intensity;

            if(cnt_Intensity == 0)
                intensity = (float) progressIntensity / 256;


            else
                intensity = (float) progressIntensity / 8;


            cnt_Intensity++;

            if(oldintensity>intensity) {
                oldintensity = intensity;
                intensity = -intensity;
            }else
                oldintensity = intensity;

            imgview.setImageBitmap(updateIntenstiy(bitmap, intensity));
        }
    }
    // 채도 함수 : 채도 갱신 _ fin
    private Bitmap updateSat(Bitmap src, float settingSat) {

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(settingSat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvasResult.drawBitmap(src, 0, 0, paint);

        bitmap = bitmapResult;
        return bitmap;
    }

    // 밝기값갱신 _fin
    private Bitmap updateIntenstiy(Bitmap src, float brightness) {
        ColorMatrix cm = new ColorMatrix(new float[]
                {
                        1, 0, 0, 0, brightness,
                        0, 1, 0, 0, brightness,
                        0, 0, 1, 0, brightness,
                        0, 0, 0, 1, 0
                });

        Bitmap ret = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);

        bitmap = ret;
        return bitmap;
    }

    // SEEKBAR 조절 함수
    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            loadBitmapSat();
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarChangeListener2 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            loadBitmapIntensity();
        }
    };
}