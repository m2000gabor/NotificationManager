package com.example.android.databasebasic;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements WordListAdapter.OnItemClicked {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public WordViewModel mWordViewModel;
    Word actualWordItem = new Word();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewRecordActivity.class);
                intent.putExtra("FOR_UPDATE",false);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        actualWordItem.setWord("üres");

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setOnClick(MainActivity.this); // Bind the listener

        //create notichannel
        createNotificationChannel();
    }

    @Override
    public void onItemClick(int actualItemId) {
        // The onClick implementation of the RecyclerView item click
        //ur intent code here

       // toolbar.setTitle("abc");

        actualWordItem = mWordViewModel.getOne(actualItemId);
        Intent intent = new Intent(getBaseContext(), NewRecordActivity.class);
        intent.putExtra("THE_TEXT",actualWordItem.getWord());
        intent.putExtra("THE_CATEGORY",actualWordItem.getCategory());
        intent.putExtra("THE_NUMBER",Integer.toString(actualWordItem.getNumber()));
        intent.putExtra("FOR_UPDATE",true);
        intent.putExtra("ID",actualItemId);
        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);

        /*int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_delete:
                mWordViewModel.deletingAll();

            case (R.id.action_make_notification):
                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSmallIcon(R.drawable.ic_add_black_24dp)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(33, mBuilder.build());

        }

        return super.onOptionsItemSelected(item);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //uj
        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE &&
                resultCode == RESULT_OK &&
                !data.getBooleanExtra("FOR_UPDATE",false) &&
                !data.getBooleanExtra("FOR_DELETE",false)
                ) {
            Word word = new Word();
            word.setWord(data.getStringExtra(NewRecordActivity.EXTRA_REPLY));
            word.setCategory(data.getStringExtra("EXTRA_CATEGORY"));
            word.setNumber(data.getIntExtra("EXTRA_NUMBER",0));
            mWordViewModel.insert(word);
        }
        //update
        else if (data.getBooleanExtra("FOR_UPDATE",false) && requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            actualWordItem.setWord(data.getStringExtra(NewRecordActivity.EXTRA_REPLY));
            actualWordItem.setCategory(data.getStringExtra("EXTRA_CATEGORY"));
            actualWordItem.setNumber(data.getIntExtra("EXTRA_NUMBER",0));
            mWordViewModel.updateOne(actualWordItem);
        }
        //del one
        else if (data.getBooleanExtra("FOR_DELETE",false)){
            // Word wordForDelete = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            mWordViewModel.delOne(actualWordItem);
        }
        //ures
        else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show();
        }
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