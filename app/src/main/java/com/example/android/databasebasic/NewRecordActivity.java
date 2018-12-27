package com.example.android.databasebasic;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class NewRecordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";
    private EditText mEditWordView;
    private EditText mEditCatView;
    private EditText mEditNumber;
    private CheckBox mNotiCheckbox;
    boolean ForUpdate = false;
    Intent replyIntent = new Intent();
    boolean checkBox1State = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        mEditWordView = findViewById(R.id.edit_word);
        mEditCatView = findViewById(R.id.edit_cat);
        mEditNumber = findViewById(R.id.edit_number);
        mNotiCheckbox = findViewById(R.id.chb1);
        Button button = findViewById(R.id.button_save);

        ForUpdate = getIntent().getBooleanExtra("FOR_UPDATE",false);

        //ha uj, a szoveget beallitani az editTextbe
        if ( ForUpdate){
            mEditWordView.setText(getIntent().getStringExtra("THE_TEXT"));
            mEditCatView.setText(getIntent().getStringExtra("THE_CATEGORY"));
            mEditNumber.setText(getIntent().getStringExtra("THE_NUMBER"));
            mNotiCheckbox.setChecked(getIntent().getBooleanExtra("NOTIFICATION",false));
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
                    boolean mNotiB= mNotiCheckbox.isChecked();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    replyIntent.putExtra("EXTRA_CATEGORY", cat);
                    replyIntent.putExtra("FOR_UPDATE",ForUpdate);
                    replyIntent.putExtra("EXTRA_NUMBER",num);
                    replyIntent.putExtra("NOTIFICATION",mNotiB);
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });

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
    }

        return super.onOptionsItemSelected(item);
    }
}
