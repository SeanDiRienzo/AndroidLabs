package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;


public class ProfileActivity extends AppCompatActivity {
    public static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent priorActivity;
    CharSequence email;
    EditText emailField;
    ImageButton mImageButton;
    Button goToChatButton;
    Button goToWeatherActivityButton;
    Button goToToolbarActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(ACTIVITY_NAME, "In Function onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_activity);
        priorActivity = getIntent();
        email = priorActivity.getCharSequenceExtra("email");
        emailField = findViewById(R.id.profile_EmailField);
        emailField.setText(email);
        mImageButton = findViewById(R.id.profile_imageButton);
        goToWeatherActivityButton = findViewById(R.id.profile_goToWeatherButton);
        goToChatButton = findViewById(R.id.profile_goToChatButton);
        goToToolbarActivityButton = findViewById(R.id.profile_goToToolbarActivity);
        goToToolbarActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTestToolbarActivity();
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        goToChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChatRoomActivity();


            }
        });

        goToWeatherActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWeatherForecastActivity();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In Function onResume()");
    }

    @Override
    protected void onDestroy() {
        Log.e(ACTIVITY_NAME, "In Function onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.e(ACTIVITY_NAME, "In Function onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.e(ACTIVITY_NAME, "In Function onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(ACTIVITY_NAME, "In Function onStop()");
        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(ACTIVITY_NAME, "In Function onActivityResult()");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void startChatRoomActivity() {
        Intent chatRoomActivity = new Intent(this, ChatRoomActivity.class);
        startActivity(chatRoomActivity);
    }

    public void startWeatherForecastActivity() {
        Intent weatherActivity = new Intent(this, WeatherForecast.class);
        startActivity(weatherActivity);
    }

    public void startTestToolbarActivity() {
        Intent toolbarActivity = new Intent(this, TestToolbar.class);
        startActivity(toolbarActivity);
    }
}

