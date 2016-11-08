package com.example.android.groupschedulecoordinator;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emily on 10/27/2016.
 */
public class Event {

    private String eventName;
    private int start;
    private int end;
    private ArrayList<String> pendingUsers;
    private ArrayList<String> completedUsers;

    public Event(){
    }

    public Event(String name, ArrayList<String> pendingU) {
        pendingUsers = new ArrayList<String>(pendingU);
        completedUsers = new ArrayList<String>();
        this.eventName = name;
        this.start = 0;
        this.end = 0;
    }

    public String getEventName(){
        return eventName;
    }

    public int getStart(){
        return start;
    }

    public int getEnd(){
        return end;
    }

    public ArrayList<String> getPendingUsers(){
        return pendingUsers;
    }

    public ArrayList<String> getCompletedUsers(){
        return completedUsers;
    }

    public void setStart(int start1){
        start = start1;
    }

    public void setEnd(int end1){
        end = end1;
    }

    public void setPendingUsers(ArrayList<String> in){
        pendingUsers = in;
    }

    public void setCompletedUsers(ArrayList<String> in){
        completedUsers = in;
    }

    @Exclude
    public boolean moveUser(String user){
        if(pendingUsers.indexOf(user)!=-1)
            pendingUsers.remove(pendingUsers.indexOf(user));
        if(completedUsers.indexOf(user)==-1)
            completedUsers.add(user);
        return pendingUsers.isEmpty();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", eventName);
        result.put("start", start);
        result.put("end", end);
        result.put("pendingUsers", pendingUsers);
        result.put("completedUsers", completedUsers);

        return result;
    }
}
