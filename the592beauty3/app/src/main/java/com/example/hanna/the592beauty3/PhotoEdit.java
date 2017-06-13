package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

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
            options.inSampleSize = 4;
            final Bitmap bmp = BitmapFactory.decodeFile(photoPath, options);
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

        //눈키우기 픽셀유동화
        btn_Eye = (Button) findViewById(R.id.btn_Eye);
        btn_Eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                satBar.setVisibility(View.INVISIBLE);
            }
        });

        //턱을갸름하게 픽셀유동화
        btn_Chin = (Button) findViewById(R.id.btn_Chin);
        btn_Chin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                satBar.setVisibility(View.INVISIBLE);
            }
        });

        //미백효과
        btn_Whitening = (Button) findViewById(R.id.btn_Whitening);
        btn_Whitening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                satBar.setVisibility(View.INVISIBLE);
            }
        });

        // 여드름제거
        btn_Blemish = (Button) findViewById(R.id.btn_Blemish);
        btn_Blemish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                satBar.setVisibility(View.INVISIBLE);
            }
        });

        // 자르기
        btn_Crop = (Button) findViewById(R.id.btn_Crop);
        btn_Crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                satBar.setVisibility(View.INVISIBLE);
            }
        });

        // 회전 _ 완료
        btn_Rotation = (Button) findViewById(R.id.btn_Rotation);
        btn_Rotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrix rotateMatrix = new Matrix();
                rotateMatrix.postRotate(90); //-360~360
                Bitmap rotateImage = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, false);
//                ((BitmapDrawable)imgview.getDrawable()).getBitmap().recycle();
                imgview.setImageBitmap(rotateImage);
                bitmap = rotateImage;
                satBar.setVisibility(View.INVISIBLE);
            }
        });

        // 좌우반전 _ 완료
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

        // 밝기조절 _
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

        // 채도조절 _
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

        // 선명도조절
        btn_Sharpening = (Button) findViewById(R.id.btn_Sharpening);
        btn_Sharpening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "찐따",
                        Toast.LENGTH_LONG);

                satBar.setVisibility(View.INVISIBLE);
            }
        });

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

    // 채도 함수 : 비트맵
    private void loadBitmapSat() {
        if (bitmap != null) {
            int progressSat = satBar.getProgress();

            //Saturation, 0=gray-scale. 1=identity
            float sat = (float) progressSat / 256;
            //satText.setText("Saturation: " + String.valueOf(sat));
            imgview.setImageBitmap(updateSat(bitmap, sat));
        }
    }

    // 밝기 함수 : 비트맵
    private void loadBitmapIntensity(){
        if(bitmap!=null){
            int progressIntensity = satBar.getProgress();
            float intensity;

            if(cnt_Intensity == 0)
            intensity = (float)progressIntensity/256;

            else
               intensity = (float)progressIntensity/8;

            cnt_Intensity++;

            imgview.setImageBitmap(updateIntenstiy(bitmap, intensity));
        }
    }
    // 채도 함수 : 채도 갱신
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

        return bitmapResult;
    }

    // 밝기값갱신
    private Bitmap updateIntenstiy(Bitmap src, float settingIntensity) {
        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int p = src.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = (int)settingIntensity + r;
                if (r >= 255)
                    r = 255;

                g = (int)settingIntensity + g;
                if (g >= 255)
                    g = 255;

                b = (int)settingIntensity + b;
                if (b >= 255)
                    b = 255;
                //alpha = 30 + alpha;
                bitmapResult.setPixel(i, j, Color.argb(alpha, r, g, b));
            }
        }
        return bitmapResult;
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