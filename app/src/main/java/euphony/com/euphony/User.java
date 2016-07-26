package euphony.com.euphony;

/**
 * Created by pR0 on 04-06-2016.
 */
public class User {
    String _id, name, type, pic, location, followOrnot;
    int members, no_followers;

    public User() {}

    public User(String id, String name, String type, String pic, String followOrnot, String location, int members, int no_followers) {
        this._id = id;
        this.name = name;
        this.type = type;
        this.pic = pic;
        this.followOrnot = followOrnot;
        this.members = members;
        this.location = location;
        this.no_followers = no_followers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setFollowOrnot(String followOrnot) {
        this.followOrnot = followOrnot;
    }

    public String getFollowOrnot() {
        return followOrnot;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNo_followers() {
        return no_followers;
    }

    public void setNo_followers(int no_followers) {
        this.no_followers = no_followers;
    }
}
