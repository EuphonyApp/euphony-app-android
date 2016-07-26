package euphony.com.euphony;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pR0 on 02-06-2016.
 */
public class Venue {
    String _id, g_id, f_id, name, location, minCapacity, maxCapacity, pic, email, contact, type, fbPage;
    List<String> followers = new ArrayList<String>();
    List<String> following = new ArrayList<String>();

    public Venue(String g_id, String f_id, String name, String location, String email, String minCapacity, String maxCapacity,
                 String pic, String contact) {
        this.name = name;
        this.location = location;
        this.email = email;
        this.f_id = f_id;
        this.g_id = g_id;
        this.minCapacity = minCapacity;
        this.maxCapacity = maxCapacity;
        this.pic = pic;
        this.contact = contact;
    }

    public Venue() {}

    public String getId() {
        return this._id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName()  {
        return this.name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLocation() {
        return location;
    }

    public void setContacts(String contacts) {
        this.contact = contacts;
    }

    public String getContacts() {
        return contact;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(String minCapacity) {
        this.minCapacity = minCapacity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getG_id() {
        return g_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public List<String> getFollowing() {
        return following;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getFbPage() {
        return fbPage;
    }

    public void setFbPage(String fbPage) {
        this.fbPage = fbPage;
    }

    public String get_id() {
        return _id;
    }

    public String getContact() {
        return contact;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }
}
