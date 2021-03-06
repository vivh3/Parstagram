package me.vivh.parstagram.model;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_USER = "user";
    private static final String KEY_TIME = "time";
    private static final String KEY_LIKED_BY = "liked_by";

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() { return getParseUser(KEY_USER); }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String getTime() {
        return getString(KEY_TIME);
    }

    public void setTime(String time) {
        put(KEY_TIME, time);
    }

    public String getUsername() {
        return getUser().getUsername();
    }


    public void initializeLikedBy() {
        if (getList(KEY_LIKED_BY) == null) {
            put(KEY_LIKED_BY, new ArrayList<String>());
        }
    }
    public String getLikeCount() {
        List<String> likes = getList(KEY_LIKED_BY);
        return Integer.toString(likes.size());
    }

    public void likePost () {
        List<String> likes = getList(KEY_LIKED_BY);
        add(KEY_LIKED_BY, getUsername());
        saveInBackground();
    }

    public void unlikePost () {
        //TODO - remove username from liked_by array
        List<String> likes = getList(KEY_LIKED_BY);
        //remove(KEY_LIKED_BY, getUsername());
        saveInBackground();
    }


    public static class Query extends ParseQuery<Post> {
        public Query() {
            super(Post.class);
        }

        public Query getTop() {
            setLimit(20);
            return this;
        }

        public Query withUser() {
            include(KEY_USER);
            return this;
        }

    }
}
