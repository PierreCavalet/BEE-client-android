package fr.pierrecavalet.bestexcuseever.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pierre on 25/10/2015.
 */
public class Comment {

    private int idbee;
    private String username;
    private String content;
    private String time;

    public Comment(int idBee, String username, String content) {
        this.idbee = idBee;
        this.username = username;
        this.content = content;
    }

    public Comment(JSONObject beeJSONObject) throws JSONException {
        this.username = (String) beeJSONObject.get("user");
        this.time = (String) beeJSONObject.get("time");
        this.content = (String) beeJSONObject.get("content");
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("idbee", this.idbee);
        jo.put("user", this.username);
        jo.put("content", this.content);
        jo.put("time", this.time);
        return jo;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}
