package com.example.layla.imageview_facedetection;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.FileNotFoundException;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
        //double scale;
        int WIDTH = 20;
        int HEIGHT = 20;
        int COUNT = (WIDTH + 1) * (HEIGHT + 1);
        float[] mVerts = new float[COUNT * 2];
        float[] mOrig = new float[COUNT * 2];
        //Bitmap mBitmap;
        Matrix mMatrix = new Matrix();
        Matrix mInverse = new Matrix();

        int warpcount = 0;

        Button button;
        ImageView imageView;
        Bitmap image_bitmap;
        final int REQ_CODE_SELECT_IMAGE = 100;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
//갤러리버튼
            Button gallery = (Button) findViewById(R.id.btn_select_gallery);
            gallery.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

                }
            });

            imageView = (ImageView) findViewById(R.id.imageView); // imgview
            button = (Button) findViewById(R.id.button);

            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //paint 색 지정 - 눈 위치 제대로 잡았는 지 확인하려고 넣은 거 - 지워도 됨
                    Paint paint = new Paint();
                    paint.setColor(Color.GREEN);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(5);

                    //임시로 쓸 비트맵과 캔버스 생성 : 캔버스에는 mybitmap을 넣음 - 혹시 모르니까 다 넣으셈
                    Bitmap tempBitmap = Bitmap.createBitmap(image_bitmap.getWidth(), image_bitmap.getHeight(), Bitmap.Config.RGB_565);
                    // tempBitmap = > eyeBitmap
                    Canvas tempCanvas = new Canvas(tempBitmap); // tempCanvas => eyeCanvas
                    tempCanvas.drawBitmap(image_bitmap,0, 0, null);

                    int h; // 높이
                    int w; // 너비

                    // 얼굴을 찾기
                    FaceDetector faceDetector = new
                            FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).setLandmarkType(FaceDetector.ALL_LANDMARKS).build();

                    Frame frame = new Frame.Builder().setBitmap(image_bitmap).build();
                    SparseArray<Face> faces = faceDetector.detect(frame);

                    h = image_bitmap.getHeight();
                    w = image_bitmap.getWidth();

                    Toast.makeText(getBaseContext(), " h:" + h + "w : " + w, Toast.LENGTH_SHORT).show();


                    // meshgrid.. 픽셀유동화하기 위해서 그물쳐주는 거 , 꼭 넣기
                    int index = 0;
                    for (int y = 0; y <= HEIGHT; y++) {
                        float fy = h * y / HEIGHT;
                        for (int x = 0; x <= WIDTH; x++) {
                            float fx = w * x / WIDTH;
                            setXY(mVerts, index, fx, fy); // 밑에 함수 있음
                            setXY(mOrig, index, fx, fy);
                            index += 1;
                        }
                    }
                    mMatrix.setTranslate(10, 10);
                    mMatrix.invert(mInverse);

                    // 아래로, 눈 찾는 것
                    int landmark_count = 0;
                    //랜드마크 찾기

                    int lefteyex = 0;
                    int lefteyey = 0;
                    int righteyex = 0;
                    int righteyey = 0;

                    for(int i=0; i<faces.size(); i++){
                        Face face = faces.valueAt(i);
                        for(Landmark landmark : face.getLandmarks()){
                            int cx = (int)(landmark.getPosition().x);
                            int cy = (int)(landmark.getPosition().y);
                            landmark_count++;

                            if(landmark_count==1 ) {
                                lefteyex = cx;
                                lefteyey = cy;
                            }
                            if(landmark_count==2){
                                righteyex = cx;
                                righteyey = cy;
                            }

                        }
                    }

                    // 이미지뷰에 뭐 해주는 건데 일단 넣기
                    imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

                    //bb사진 : 왼쪽눈좌표(796, 321) / 오른쪽눈좌표 (933, 311)
                    tempCanvas.concat(mMatrix);
                    //왼쪽눈좌표
                    // warp로 눈 키워주기
                    warp(lefteyex,lefteyey); // 픽셀 움직이는 함수
                    warp(righteyex, righteyey-3);
                    tempCanvas.drawBitmapMesh(image_bitmap, WIDTH, HEIGHT, mVerts, 0, null, 0, null);
                    imageView.setImageBitmap(tempBitmap);

                }
            });
        }

        //warp함수 : pixelfluid
        void warp(float cx, float cy) {
            final float K = 10000;
            float[] src = mOrig;
            float[] dst = mVerts;

            if(warpcount!=0){ // warpcount 안하면 한 번한 담에 담꺼 리셋됨..
                src = mVerts;
            }

            for (int i = 0; i < COUNT * 2; i += 2) {
                float x = src[i + 0];
                float y = src[i + 1];
                float dx = cx - x;
                float dy = cy - y;
                float dd = dx * dx + dy * dy;
                float d = (float)Math.sqrt(dd);
                float pull = K / (dd + 0.000001f); // pull : 밀어주는 정도의 느낌,,
                pull /= (d + 0.000001f);

                if (pull >= 2.5 ) { // 거리가 ~보다 크면 이동x, 1.5보다 아래면 이동o // 거리를 우리가 적절하게 조정해야됨
                    dst[i + 0] = cx;
                    dst[i + 1] = cy;
                } else {
                    dst[i + 0] = x- (int) (0.2 * dx * pull); // 숫자를 키우면 눈 커지는 게 커짐.. // 적절하게 숫자 조정해야됨 // -하면 눈이 커지고, +하면 눈이 작아짐 -> 갸름하게는 +하고 눈 키우는 거는 -로 해주기
                    dst[i + 1] = y- (int) (0.1 * dy * pull); // dst자체는 커지는 영역을 조정
                }
            }
            warpcount++;
        }

        // XY축 정하기 : pixelfluid // 축그리기
        void setXY(float[] array, int index, float x, float y) {
            array[index * 2 + 0] = x;
            array[index * 2 + 1] = y;
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode==Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    //배치해놓은 ImageView에 set
                    imageView.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


}
