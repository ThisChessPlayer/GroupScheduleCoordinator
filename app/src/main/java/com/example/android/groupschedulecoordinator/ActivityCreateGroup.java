package com.example.android.groupschedulecoordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityCreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void createGroup(View v) {
        EditText text =  (EditText) findViewById(R.id.tbGroupName);
        String groupName = text.getText().toString();
        if(groupName.isEmpty())
        {
            android.content.Context context = getApplicationContext();
            CharSequence warning = "Please enter a valid group name.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, warning, duration);
            toast.show();
        }
        else
        {
//            Intent intent = new Intent(ActivityCreateGroup.this, MainActivity.class);

//            intent.putExtra("groupName", groupName);

//            startActivity(intent);
            Bundle bundle = new Bundle();
            bundle.putString("groupName", groupName);
            Intent intent = new Intent(ActivityCreateGroup.this, MainActivity.class);
            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }





    }
}
