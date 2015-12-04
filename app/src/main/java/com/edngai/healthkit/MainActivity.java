package com.edngai.healthkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String totalString;
    String goalStr;
    String overUnder;
    int goal;
    int totalCalories;
    List<ParseObject> tempObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "zQ6SZImPJDvJrrroriwiLT9kds0SG9V1KrTWTglM", "NNvWMkDmlcgW0s91VbIyytvAqV0K3i3R4Yb5zRhs");

        /*Image button to go to user profile.*/
        ImageButton userButton = (ImageButton) findViewById(R.id.userProfileButton);

        userButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToPage(userProfile.class);

            }

        });

        /*Image button to go to calorie counter.*/
        ImageButton calorieButton = (ImageButton) findViewById(R.id.calorieCounterButton);

        calorieButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToPage(calories.class);

            }

        });

        /*Image button to go to bmi.*/
        ImageButton bmiButton = (ImageButton) findViewById(R.id.bmiButton);

        bmiButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToPage(bmi.class);


            }

        });

        /*Image button to go to tips.*/
        ImageButton tipsButton = (ImageButton) findViewById(R.id.tipsButton);

        tipsButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToPage(tips.class);

            }

        });

        /*button to go to settings.*/
        Button settingsButton = (Button) findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                goToPage(Settings.class);

            }

        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("CalorieGoal");
        query.orderByDescending("createdAt");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (user == null) {

                } else {
                    dataHolder g = dataHolder.getInstance();
                    goal = (int) user.get("caloriesGoal");
                    goalStr = Integer.toString(goal);
                    g.setGoalInput(goal);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToPage(Class page) {

        Intent intent = new Intent(this, page);

        startActivity(intent);

    }

    // displays the daily summary
    public void clickSummary(View view){

        dataHolder g = dataHolder.getInstance();

        // get total daily calories
        totalCalories = getTotal();
        // make totalCalories into a string
        totalString = Integer.toString(totalCalories);

        if (totalCalories < goal ){
            overUnder = "under";
        }

        else if (totalCalories > goal){
            overUnder = "over";
        }

        else {
            overUnder = "equal to";
        }

        AlertDialog.Builder builder1;
        builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("You have inputted a total of " + totalString + " out of " + goalStr + ". You are " + overUnder + " your goal.");

        builder1.setCancelable(true);
        builder1.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    //get total of column in parse
    public int getTotal() {

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Calories");
        try {

            totalCalories = 0;
            List<ParseObject> objects = query.find();

            // for loop that adds up the calories for the current day
            for (int i = 0; i < objects.size(); i++) {
                ParseObject temp = objects.get(i);
                int tempDay = temp.getInt("dayOfWeek");
                // if the tempDay is the current day of the week
                if (dayOfWeek == tempDay) {
                    String tempCalString = temp.getString("calories");
                    int tempCal = Integer.parseInt(tempCalString);
                    totalCalories = tempCal + totalCalories;

                }
            }

            return totalCalories;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

}