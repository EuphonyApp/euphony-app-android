package euphony.com.euphony;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pR0 on 30-05-2016.
 */
public class Artist {
    String _id, f_id, g_id, email, name, type, location, genre, fbPage, utube, pic, dis, twitter, scloud, contact, gplus;
    List<String> subGenre = new ArrayList<String>();
    List<String> followers = new ArrayList<String>();
    List<String> following = new ArrayList<String>();
    List<String> bands = new ArrayList<String>();

    public Artist(String name, String type, String location, String genre, List<String> subGenre, String fbPage, String utube,
                           String email, String pic, String f_id, String g_id, String dis, String twitter, String contact, String scloud,
                  List<String> followers, List<String> following, List<String> bands) {
        this.name = name;
        this.type = type;
        this.location = location;
        this.genre = genre;
        this.subGenre = subGenre;
        this.fbPage = fbPage;
        this.utube = utube;
        this.f_id = f_id;
        this.g_id = g_id;
        this.email = email;
        this.pic = pic;
        this.dis = dis;
        this.twitter = twitter;
        this.scloud = scloud;
        this.followers = followers;
        this.following = following;
        this.bands = bands;
        this.contact = contact;
    }

    public Artist() {};

    public String getId() {
        return this._id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation(){
        return this.location;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setSubGenre(List<String> subGenre) {
        this.subGenre = subGenre;
    }

    public List<String> getSubGenre() {
        return this.subGenre;
    }

    public void setFbPage(String fbPage) {
        this.fbPage = fbPage;
    }

    public String getFbPage() {
        return this.fbPage;
    }

    public void setUtube(String utube) {
        this.utube = utube;
    }

    public String getUtube() {
        return this.utube;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getF_id() {
        return this.f_id;
    }

    public void setG_id(String g_id) {
        this.g_id = g_id;
    }

    public String getG_id() {
        return this.g_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return this.pic;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getBands() {
        return bands;
    }

    public void setBands(List<String> bands) {
        this.bands = bands;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getScloud() {
        return scloud;
    }

    public void setScloud(String scloud) {
        this.scloud = scloud;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String get_id() {
        return _id;
    }

    public String getGplus() {
        return gplus;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setGplus(String gplus) {
        this.gplus = gplus;
    }
}
