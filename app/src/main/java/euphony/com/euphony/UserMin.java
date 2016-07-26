package euphony.com.euphony;

/**
 * Created by pR0 on 04-06-2016.
 */
public class UserMin {
    String id, name, type, pic, genre;

    public UserMin() {}

    public UserMin(String id, String name, String type, String pic, String genre) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.pic = pic;
        this.genre = genre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public String get_id() {
        return id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
