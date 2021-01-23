package top.xmdhs.ipsave.sql;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.Set;

public class json {
    private static final Gson gson = new Gson();

    public static String toJson(set s) {
        return gson.toJson(s);
    }

    public static set toSet(String json) {
        if (json.equals("")) {
            return null;
        }
        set s = null;
        try {
            s = gson.fromJson(json, set.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return s;
    }
}

class set {
    public Set<String> set;

    public set(Set<String> set) {
        this.set = set;
    }
}
