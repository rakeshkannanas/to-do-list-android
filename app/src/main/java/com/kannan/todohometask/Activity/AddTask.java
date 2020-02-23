package com.kannan.todohometask.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kannan.todohometask.Utils.DatabaseHandler;
import com.kannan.todohometask.Utils.Global;
import com.kannan.todohometask.Utils.LinedEditText;
import com.kannan.todohometask.R;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTask extends AppCompatActivity {
String date,time;
String data,id,datefromintent,timefromintent;
DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        final LinedEditText textview = findViewById(R.id.todotext);
        FloatingActionButton save = findViewById(R.id.fab);
        setSupportActionBar(toolbar);
         db = new DatabaseHandler(getApplicationContext());
        data = getIntent().getStringExtra("Data");
        id = getIntent().getStringExtra("ID");
        datefromintent = getIntent().getStringExtra("Date");
        timefromintent = getIntent().getStringExtra("Time");
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        date = df.format(c);
        time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        if(Global.Flag.equalsIgnoreCase("O"))
        {
            getSupportActionBar().setTitle("Edit Todo");
            textview.setText(data);
        }
        else
        {
            getSupportActionBar().setTitle("New Todo");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textview.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(AddTask.this, "Your Todo list is empty !!!", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    if (Global.Flag.equalsIgnoreCase("O"))
                    {
                    if (db.updatedata(id, textview.getText().toString()))
                    {
                        Toast.makeText(AddTask.this, "Updated !!", Toast.LENGTH_SHORT).show();
                        Intent movetomain = new Intent(AddTask.this, MainActivity.class);
                        movetomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(movetomain);
                        finish();
                    }
                }
                    else
                        {
                    String res = db.addMsg(textview.getText().toString(), date, time);
                    if (res.equalsIgnoreCase("true"))
                    {
                        Toast.makeText(AddTask.this, "Added !!", Toast.LENGTH_SHORT).show();
                        Intent movetomain = new Intent(AddTask.this, MainActivity.class);
                        movetomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(movetomain);
                        finish();
                    }
                }
            }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder((AddTask.this));

                alertDialog.setTitle("Delete !!!");
                alertDialog.setMessage("Are you sure you want to delete? ");
                alertDialog.setIcon(R.mipmap.ic_launcher);

                alertDialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do the stuff..

                                if(db.delMsg(id))
                                {
                                    Toast.makeText(AddTask.this, "Deleted !!", Toast.LENGTH_SHORT).show();
                                    Intent movetomain = new Intent(AddTask.this,MainActivity.class);
                                    movetomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(movetomain);
                                    finish();
                                }
                            }
                        }
                );
                alertDialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do the stuff..

                            }
                        }
                );
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
