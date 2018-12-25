package com.example.android.databasebasic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewRecordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private EditText mEditWordView;
    private EditText mEditCatView;
    private EditText mEditNumber;
    boolean ForUpdate = false;
    Intent replyIntent = new Intent();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        mEditWordView = findViewById(R.id.edit_word);
        mEditCatView = findViewById(R.id.edit_cat);
        mEditNumber = findViewById(R.id.edit_number);
        Button button = findViewById(R.id.button_save);

        ForUpdate = getIntent().getBooleanExtra("FOR_UPDATE",false);

        //ha uj, a szoveget beallitani az editTextbe
        if ( ForUpdate){
            mEditWordView.setText(getIntent().getStringExtra("THE_TEXT"));
            mEditCatView.setText(getIntent().getStringExtra("THE_CATEGORY"));
            mEditNumber.setText(getIntent().getStringExtra("THE_NUMBER"));
            button.setText(R.string.update_one_item);
        }


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //üres mező eseteén
                if (TextUtils.isEmpty(mEditWordView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                }
                //amikor nem ures
                else {
                    String word = mEditWordView.getText().toString();
                    String cat = mEditCatView.getText().toString();
                    int num = Integer.parseInt(mEditNumber.getText().toString());
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra("EXTRA_CATEGORY", cat);
                    replyIntent.putExtra("FOR_UPDATE",ForUpdate);
                    replyIntent.putExtra("EXTRA_NUMBER",num);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

        createNotificationChannel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        if (!ForUpdate){getMenuInflater().inflate(R.menu.menu_new_record_activity, menu);}
        else {getMenuInflater().inflate(R.menu.menu_new_record_activity, menu);}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case (R.id.action_delete) :
                // custom action here
                replyIntent.putExtra("FOR_DELETE",true);
                ForUpdate=false;
                replyIntent.putExtra("FOR_UPDATE",false);

            case(R.id.action_cancel):
                // custom action here
                setResult(RESULT_CANCELED, replyIntent);
                finish();

            case(R.id.action_make_notification):
                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(33, mBuilder.build());
                finish();
    }

        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel_name";
            String description = "channel_description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
