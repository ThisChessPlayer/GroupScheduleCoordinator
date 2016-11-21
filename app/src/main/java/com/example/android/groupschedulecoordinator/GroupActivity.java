package com.example.android.groupschedulecoordinator;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GroupActivity extends AppCompatActivity {

    private ListView lvMem;
    private ListView lvEvent;
    private ArrayList<String> group_list;
    private ArrayList<String> event_list;
    private ArrayList<String> eventID_list;
    private HashMap<String, Event> currEvents;
    private final Context c = this;
    private WeekView mWeekView;
    private WeekView.EventClickListener mEventClickListener;
    private WeekView.EventLongPressListener mEventLongPressListener;
    private DatabaseReference mDatabase;
    private DatabaseReference mGroupsReference;
    private Group currentGroup;
    private String groupID;
    private ValueEventListener mListener;
    private String calling;


    WeekView.MonthChangeListener mMonthChangeListener = new WeekView.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
            List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, 3);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.MONTH, newMonth-1);
            startTime.set(Calendar.YEAR, newYear);
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, 1);
            endTime.set(Calendar.MONTH, newMonth-1);
            WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
            //event.setColor(getResources().getColor(R.color.event_color_01));
            events.add(event);

            return events;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_group);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        System.out.println("GroupActivity Database Reference: " + mDatabase.toString());

        lvMem = (ListView) findViewById(R.id.lvMembers);
        group_list = new ArrayList<String>();

        lvEvent = (ListView) findViewById(R.id.lvMeetings);
        event_list = new ArrayList<String>();
        eventID_list = new ArrayList<String>();

        final Bundle bundle1 = getIntent().getExtras();
        if(bundle1 != null)
        {
            groupID = bundle1.getString("groupID");
            Log.d("jlogs", "groupID: " + groupID);
            if(groupID == null) {
                groupID = "null";
                Log.d("jlogs", "null groupID");
            }

            currentGroup = new Group();

            mGroupsReference = mDatabase.child("groups").child(groupID);
            System.out.println("GroupActivity Database Group Reference: " + mGroupsReference.toString());
            group_list = bundle1.getStringArrayList("groupList");
            event_list = bundle1.getStringArrayList("eventList");

            calling = bundle1.getString("calling");

        }
        else
            Log.d("jlogs", "bundle is null");

        if(group_list != null) {
            ArrayAdapter<String> arrayAdapterMember = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    group_list);

            if( lvMem != null ) {
                lvMem.setAdapter(arrayAdapterMember);
            }
        }

        if(event_list != null) {
            ArrayAdapter<String> arrayAdapterEvent = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    event_list);

            if(lvEvent != null) {
                lvEvent.setAdapter(arrayAdapterEvent);
            }
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        String groupName = extras.getString("groupName");

        TextView tbGroupName = (TextView) findViewById(R.id.groupName);
        tbGroupName.setText(groupName);


        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.info);
        spec.setIndicator("Info");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.members);
        spec.setIndicator("Members");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.calendar);
        spec.setIndicator("Calendar");
        host.addTab(spec);




        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) findViewById(R.id.weekView);

        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(mEventClickListener);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(mMonthChangeListener);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(mEventLongPressListener);


        Button fab = (Button) findViewById(R.id.addmembers);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_popup, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.enterEmail);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                group_list.add(userInputDialogEditText.getText().toString());
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        c,
                                        android.R.layout.simple_list_item_1,
                                        group_list);

                                lvMem.setAdapter(arrayAdapter);
                            }
                        })

                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        //dialogBox.cancel();
                                    }
                                });



                Intent intent = new Intent(GroupActivity.this, AddMemberToGroup.class);
                intent.putStringArrayListExtra("groupList", group_list);
                startActivity(intent);
                //startActivity(new Intent(GroupActivity.this, ActivityCreateGroup.class));
            }

        });

        Button fab1 = (Button) findViewById(R.id.btnNewMeeting);
        fab1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(c);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_popup, null);
                AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(c);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.enterEmail);

                alertDialogBuilderUserInput
                      .setCancelable(false)
                      .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialogBox, int id) {
                              event_list.add(userInputDialogEditText.getText().toString());
                              ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    c,
                                    android.R.layout.simple_list_item_1,
                                    event_list);

                              lvEvent.setAdapter(arrayAdapter);
                              //addEvent(userInputDialogEditText.getText().toString(), 1, 2);
                          }
                      })

                      .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogBox, int id) {
                                    //dialogBox.cancel();
                                }
                            });
                Intent intent = new Intent(GroupActivity.this, ActivityCreateMeeting.class);;
                intent.putStringArrayListExtra("eventList", event_list);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
            }

        });

        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("onDataChange");
                if (dataSnapshot.exists()){
                    // Get CustomUser object and use the values to update the UI
                    //System.out.println("Data change for " + userName);
                    System.out.println("Data: "+dataSnapshot.toString());
                    Group tempGroup = dataSnapshot.getValue(Group.class);

                    if(tempGroup.getEvents() == null)
                        tempGroup.setEvents(new HashMap<String, Event>());
                    if(tempGroup.getMembers() == null)
                        tempGroup.setMembers(new HashMap<String, String>());
                    if(tempGroup.getGroupName() == null)
                        tempGroup.setGroupName("");
                    currentGroup.setEvents(tempGroup.getEvents());
                    currentGroup.setMembers(tempGroup.getMembers());
                    currentGroup.setGroupName(tempGroup.getGroupName());
                    updateEventList();
                }
                else{
                    System.out.println(dataSnapshot.toString()+"Does not exist");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("jasonlogs", "loadUser:onCancelled", databaseError.toException());
                // ...
            }
        };

        mGroupsReference.addValueEventListener(dataListener);
        mListener = dataListener;
        //mListener = dataListener;

        System.out.println("Calling: " + calling);
        if(calling.equals("createMeeting")) {
            String name = bundle1.getString("eventName");

            addEvent(name, 1, 2);
        }
        
    }

    private void updateEventList(){

        System.out.println("Entered updateEventList");
        lvEvent = (ListView) findViewById(R.id.lvMeetings);

        event_list = new ArrayList<String>();
        eventID_list = new ArrayList<String>();
        HashMap<String,Event> eventMap;

        if(currentGroup.getEvents() != null)
            eventMap = currentGroup.getEvents();
        else
            eventMap = new HashMap<>();
        Set<String> keySet = eventMap.keySet();
        ArrayList<String> sortedKeys = new ArrayList<String>();
        for(String i:keySet){
            sortedKeys.add(i);
        }
        Collections.sort(sortedKeys);
        System.out.println(sortedKeys);

        if(currEvents == null)
            currEvents = new HashMap<>();
        for(String s: sortedKeys){
            eventID_list.add(s);
            event_list.add(eventMap.get(s).getEventName());
            currEvents.put(s, eventMap.get(s));
        }

        if(event_list != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                  this,
                  android.R.layout.simple_list_item_1,
                  event_list);
            lvEvent.setAdapter(arrayAdapter);
        }

        /*
        lvEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String groupID = groupID_list.get(position);
                Intent intent = new Intent(MainActivity.this, GroupActivity.class);
                intent.putExtra("groupID", groupID);
                startActivity(intent);
            }
        });
        */
    }

    private void addEvent(String eventName, int start, int duration){
        System.out.println("Entered addEvent: " + eventName + " " + start + " " + duration);
        System.out.println("EventList: " + event_list);
        System.out.println("currentGroup: " + currentGroup.toString());
        Event tempEvent = new Event();
        tempEvent.setEventName(eventName);
        tempEvent.setStart(start);
        tempEvent.setEnd(start + duration);

        //String eventID = mDatabase.child("groups").child(groupID).push().getKey();
        //mDatabase.child("groups").child(groupID).child(eventID).setValue(tempEvent);

        //update group's events
        if(currEvents == null)
            currEvents = new HashMap<>();
        currEvents.put(eventName, tempEvent);
        //currentGroup.setEvents(currEvents);

        //push group events changes to firebase
        mGroupsReference.child("events").setValue(currEvents);
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGroupsReference.removeEventListener(mListener);
    }
}
