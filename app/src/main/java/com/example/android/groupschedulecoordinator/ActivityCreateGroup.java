package com.example.android.groupschedulecoordinator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityCreateGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void createGroup(View v) {
        EditText text =  (EditText) findViewById(R.id.tbMemberName);
        String memberName = text.getText().toString();
        EditText text2 = (EditText) findViewById(R.id.tbMemberEmail);
        String memberEmail = text2.getText().toString();



        if(memberName.isEmpty())
        {
            android.content.Context context = getApplicationContext();
            CharSequence warning = "Please enter a valid name.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, warning, duration);
            toast.show();
        }
        if(memberEmail.isEmpty())
        {
            android.content.Context context = getApplicationContext();
            CharSequence warning = "Please enter a valid email.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, warning, duration);
            toast.show();
        }
        else
        {
            Intent intent = new Intent(ActivityCreateGroup.this, GroupActivity.class);

            Bundle extras = getIntent().getExtras();
            ArrayList<String> group_list = new ArrayList<>();
            if( extras != null ) {
                group_list = extras.getStringArrayList("groupList");
            }

            if(group_list == null)
            {
                group_list = new ArrayList<>();
            }
            group_list.add(memberEmail);

            //intent.putExtra("memberName", memberName);
            intent.putStringArrayListExtra("groupList", group_list);

            startActivity(intent);
        }





    }
}
