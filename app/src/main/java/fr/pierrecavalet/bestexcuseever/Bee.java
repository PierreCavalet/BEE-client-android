package fr.pierrecavalet.bestexcuseever;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Pierre on 25/10/2015.
 */
public class Bee {

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
        this.user = (String) beeJSONObject.get("user");
        this.location = (String) beeJSONObject.get("location");
        this.time = (String) beeJSONObject.get("time");
        this.content = (String) beeJSONObject.get("content");
        this.up = (int) beeJSONObject.get("up");
        this.down = (int) beeJSONObject.get("down");
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

}
