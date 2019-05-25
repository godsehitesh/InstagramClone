package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Log.i("Received Username:", username);

        getSupportActionBar().setTitle(username+"'s images");

        final LinearLayout imageLinearLayout = (LinearLayout)findViewById(R.id.imageLinearLayout);
        final ScrollView imageScrollView = (ScrollView)findViewById(R.id.imageScrollView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Image");
        query.whereEqualTo("username", username);
        query.orderByDescending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null){

                    if(objects.size()>0){

                    for(ParseObject object : objects){
                        Log.i("info", "image found!");
                        ParseFile file = (ParseFile) object.get("image");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if(e==null){
                                    Bitmap bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView image = new ImageView(getApplicationContext());
                                    image.setImageBitmap(bitmapImage);
                                    imageLinearLayout.addView(image);
                                }
                                else{
                                    Log.i("info", e.getMessage());
                                }
                            }
                        });
                    }
                    }else{

                        Toast.makeText(getApplicationContext(), username+" has not shared any images.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                        startActivity(intent);
                    }
                }
                else{
                    Log.i("info", e.getMessage());
                }
            }
        });
    }
}
