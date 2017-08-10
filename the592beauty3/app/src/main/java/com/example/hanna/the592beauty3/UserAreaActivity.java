package com.example.hanna.the592beauty3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class UserAreaActivity extends Activity {
    ImageView imageView1;
    ImageView imageView2;
    Button logout;
    private static final String null_URL1 = "https://phreshout999.000webhostapp.com/Penguins.jpg";
    private static final String Spring_URL1 = "https://phreshout999.000webhostapp.com/spring_1.png";
    private static final String Spring_URL2 = "https://phreshout999.000webhostapp.com/spring_2.png";
    private static final String Summer_URL1 = "https://phreshout999.000webhostapp.com/summer_1.png";
    private static final String Summer_URL2 = "https://phreshout999.000webhostapp.com/summer_2.png";
    private static final String Autumn_URL1 = "https://phreshout999.000webhostapp.com/autumn_1.png";
    private static final String Autumn_URL2 = "https://phreshout999.000webhostapp.com/autumn_2.png";
    private static final String Winter_URL1 = "https://phreshout999.000webhostapp.com/winter_1.png";
    private static final String Winter_URL2 = "https://phreshout999.000webhostapp.com/winter_2.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);
        final ColorWeight weight = (ColorWeight) getApplicationContext();
        imageView1 = (ImageView) findViewById(R.id.imgT);
        imageView2 = (ImageView) findViewById(R.id.imgD);
        final EditText etID = (EditText) findViewById(R.id.etID);
        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight.initAll();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
        if (weight.getUsername() == null) {
            Intent intent = getIntent();
            String name = intent.getStringExtra("Name");
            String ID = intent.getStringExtra("ID");
            weight.setUsername(name);
            weight.setId(ID);
        }
        String message = weight.getUsername() + " welcome to your user area";
        welcomeMessage.setText(message);
        etID.setText(weight.getId());
        ImageRequest imageRequest1, imageRequest2;

        switch (weight.getUsername()) {
            case "봄": weight.setTone("spring"); break;
            case "여름": weight.setTone("summer"); break;
            case "가을": weight.setTone("autumn"); break;
            case "겨울": weight.setTone("winter"); break;
        }

        if (weight.getTone() == null) {
            imageRequest1 = new ImageRequest(null_URL1,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imageView1.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            });
            CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest1);
        } else {
            switch (weight.getTone()) {
                case "spring":
//                String Spring_URL1 = "https://phreshout999.000webhostapp.com/spring_1.png";
//                String Spring_URL2 = "https://phreshout999.000webhostapp.com/spring_2.png";
                    imageRequest1 = new ImageRequest(Spring_URL1,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView1.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest1);
                    imageRequest2 = new ImageRequest(Spring_URL2,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView2.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest2);
                    break;

                case "summer":
//                String Summer_URL1 = "https://phreshout999.000webhostapp.com/summer_1.png";
//                String Summer_URL2 = "https://phreshout999.000webhostapp.com/summer_2.png";
                    imageRequest1 = new ImageRequest(Summer_URL1,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView1.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest1);
                    imageRequest2 = new ImageRequest(Summer_URL2,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView2.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest2);
                    break;

                case "autumn":
//                String Autumn_URL1 = "https://phreshout999.000webhostapp.com/autumn_1.png";
//                String Autumn_URL2 = "https://phreshout999.000webhostapp.com/autumn_2.png";
                    imageRequest1 = new ImageRequest(Autumn_URL1,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView1.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest1);
                    imageRequest2 = new ImageRequest(Autumn_URL2,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView2.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest2);
                    break;

                case "winter":
//                String Winter_URL1 = "https://phreshout999.000webhostapp.com/winter_1.png";
//                String Winter_URL2 = "https://phreshout999.000webhostapp.com/winter_2.png";
                    imageRequest1 = new ImageRequest(Winter_URL1,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView1.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest1);
                    imageRequest2 = new ImageRequest(Winter_URL2,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    imageView2.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UserAreaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    });
                    CosmeticArea.getmInstance(UserAreaActivity.this).addToRequestQue(imageRequest2);
                    break;
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
