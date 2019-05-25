/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

    EditText usernameEditText, passwordEditText;
    Button LISUButton;
    TextView LISUText;
    Boolean signUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    usernameEditText = (EditText)findViewById(R.id.usernameEditText);
    passwordEditText = (EditText)findViewById(R.id.passwordEditText);

    LISUButton = (Button)findViewById(R.id.LISUButton);
    LISUText = (TextView)findViewById(R.id.LISUText);

    signUp = true;
//
//    ParseObject score = new ParseObject("Score");
//    score.put("username", "hitesh");
//    score.put("score", 150);
//
//    score.saveInBackground(new SaveCallback() {
//      @Override
//      public void done(ParseException e) {
//
//          if (e == null){
//              Log.i("SaveInBackground", "Successful");
//          }else
//          {
//              Log.i("SaveInBackground", "Failed. Error: "+e.toString());
//          }
//
//      }
//    });

/*
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");

      query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {

              if(e==null){

                  Log.i("findInBackground", "Retrieved" + objects.size() + "objects");

                  if(objects.size()>0)
                  {
                      for(ParseObject object: objects){

                          if(object.getInt("score")>200){

                              int newScore = object.getInt("score")+50;
                              object.put("score", newScore);
                              object.saveInBackground();
                          }

                          Log.i("findInBackground", "Updated object: "+ object.getString("username")+" "+String.valueOf(object.getInt("score")));
                      }
                  }
              }
          }
      });
*/
//      query.getInBackground("wSJpA4Q9BS", new GetCallback<ParseObject>() {
//          @Override
//          public void done(ParseObject object, ParseException e) {
//
//              if(e==null && object!=null){
//
//                  object.put("tweet", "Second tweet!");
//                  object.saveInBackground();
//
//                  Log.i("ObjectValue", object.getString("username"));
//                  Log.i("ObjectValue", object.getString("tweet"));
//
//              }
//          }
//      });

      passwordEditText.setOnKeyListener(this);
      getSupportActionBar().setTitle("Instagram");

      if(ParseUser.getCurrentUser()!=null)
      {
          Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
          startActivity(intent);
      }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  public void LISUClicked(View view){

      Log.i("LISUClicked", "in LISUClicked");

      if(usernameEditText.getText().toString().equalsIgnoreCase("")||passwordEditText.getText().toString().equalsIgnoreCase(""))
      {
          Toast.makeText(getApplicationContext(), "Both Username and Password fields are required.", Toast.LENGTH_LONG).show();

          Log.i("LISUClicked", "Both Username and Password fields are required.");
      }
      else{

          if(signUp == true){

              ParseUser user = new ParseUser();

              user.setUsername(usernameEditText.getText().toString());
              user.setPassword(passwordEditText.getText().toString());

              user.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(ParseException e) {

                      if(e==null){
                          Toast.makeText(getApplicationContext(), "Signed up successfully.", Toast.LENGTH_LONG).show();
                          Log.i("SignUp", "Successful");

                          Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                          startActivity(intent);
                      }else{

                          switch (e.getCode()) {
                              case ParseException.USERNAME_TAKEN: {
                                  Toast.makeText(getApplicationContext(), "The username already exists.", Toast.LENGTH_LONG).show();
                                  // report error
                                  break;
                              }
                              case ParseException.EMAIL_TAKEN: {
                                  // report error
                                  break;
                              }
                              default: {
                                  // Something else went wrong
                              }
                          }

                          Log.i("SignUp", "Failed");
                      }
                  }
              });
          }
          else{

              ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                  @Override
                  public void done(ParseUser user, ParseException e) {

                      if(user!=null){
                          Toast.makeText(getApplicationContext(), "Logged In Successfully.", Toast.LENGTH_LONG).show();
                          Log.i("LogIn", "Successful");

                          Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                          startActivity(intent);
                      }
                      else{
                          String[] splitStrings = e.toString().split(":");

                          Toast.makeText(getApplicationContext(), splitStrings[1], Toast.LENGTH_LONG).show();
                          Log.i("LogIn", "Failed: "+e.toString());
                      }
                  }
              });
          }
      }

  }

  public void switchLISU(View view){

      if(LISUButton.getText().toString().equalsIgnoreCase("Sign Up")){

          LISUButton.setText("Log In");
          LISUText.setText("or, Sign Up");
          signUp = false;
      }else{

          LISUButton.setText("Sign Up");
          LISUText.setText("or, Log In");
          signUp = true;
      }

      Log.i("switchLISU", "signUp"+signUp);
  }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            LISUClicked(view);
        }
        return false;
    }

    public void hideKeyboard(View view){

      if(view.getId()==R.id.backgroundRelativeLayout || view.getId()==R.id.loginLogo){

          InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
          inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
      }
    }
}