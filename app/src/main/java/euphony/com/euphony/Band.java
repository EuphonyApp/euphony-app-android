package euphony.com.euphony;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pR0 on 01-06-2016.
 */
public class Band {
    String name, _id, location, contact, pic, fbPage, scloud, twitter, gplus, utube, type, genre, manager, foundedOn;
    List<String> followers = new ArrayList<String>();
    List<String> following = new ArrayList<String>();
    List<String> subgenre = new ArrayList<>();
    List<String> members = new ArrayList<String>();
    List<String> positions = new ArrayList<String>();

    public Band( String name, String location, String type, String contact, List<String> subgenre, String manager, String pic, String fb_page, List<String > positions, List<String> followers,
                List<String> following, String genre, String foundedOn) {
        this.name = name;
        this.contact = contact;
        this.fbPage = fb_page;
        this.pic = pic;
        this.subgenre = subgenre;
        this.manager = manager;
        this.location = location;
        this.type = type;
        this.positions = positions;
        this.followers = followers;
        this.following = following;
        this.genre = genre;
        this.foundedOn = foundedOn;
    }

    public Band() {}

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getFollowing() {
        return following;
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public String getContact() {
        return contact;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getFb_page() {
        return fbPage;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setFb_page(String fb_page) {
        this.fbPage = fb_page;
    }

    public String getPic() {
        return pic;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getManager() {
        return manager;
    }

    public List<String> getSubgenre() {
        return subgenre;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public void setSubgenre(List<String> subgenre) {
        this.subgenre = subgenre;
    }

    public String getGplus() {
        return gplus;
    }

    public String getScloud() {
        return scloud;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getUtube() {
        return utube;
    }

    public void setGplus(String gplus) {
        this.gplus = gplus;
    }

    public void setScloud(String scloud) {
        this.scloud = scloud;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setUtube(String utube) {
        this.utube = utube;
    }

    public String getFbPage() {
        return fbPage;
    }

    public void setFbPage(String fbPage) {
        this.fbPage = fbPage;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getFoundedOn() {
        return foundedOn;
    }

    public void setFoundedOn(String foundedOn) {
        this.foundedOn = foundedOn;
    }
}

