package fr.pierrecavalet.bestexcuseever;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pierre on 25/10/2015.
 */
public class Bee {

    private int id;
    private String user;
    private String location;
    private String time;
    private String content;
    private int up;
    private int down;
    private ArrayList<Comment> comments;

    public Bee() {
        comments = new ArrayList<Comment>();
    }

    public Bee(JSONObject beeJSONObject) throws JSONException {
        this.id = (int) beeJSONObject.get("id");
        this.user = (String) beeJSONObject.get("user");
        this.location = (String) beeJSONObject.get("location");
        this.time = (String) beeJSONObject.get("time");
        this.content = (String) beeJSONObject.get("content");
        this.up = (int) beeJSONObject.get("up");
        this.down = (int) beeJSONObject.get("down");
        comments = new ArrayList<Comment>();
    }

    public Bee(String user, String location, String time, String content, int up, int down) {
        this.id = 0;
        this.user = user;
        this.location = location;
        this.time = time;
        this.content = content;
        this.up = up;
        this.down = down;
        comments = new ArrayList<Comment>();
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("id", this.id);
        jo.put("user", this.user);
        jo.put("location", this.location);
        jo.put("time", this.time);
        jo.put("content", this.content);
        jo.put("up", this.up);
        jo.put("down", this.down);
        return jo;
    }

    public void loadComments(JSONArray commentsJSONArray) throws JSONException {
        for(int i=0; i < commentsJSONArray.length(); i++) {
            JSONObject commentJSONObject = (JSONObject) commentsJSONArray.get(i);
            //this.comments.add(new Comment());
        }
    }

}
