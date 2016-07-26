package euphony.com.euphony;

/**
 * Created by pR0 on 20-06-2016.
 */
public class ChatDetails {
    String name, pic, id;
    int unread;

    public ChatDetails(String name, String pic, String id, int unread) {
        this.name = name;
        this.pic = pic;
        this.id = id;
        this.unread = unread;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public void setName(String name) {
        this.name = name;
    }
}
