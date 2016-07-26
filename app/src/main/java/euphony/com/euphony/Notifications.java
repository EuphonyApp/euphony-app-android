package euphony.com.euphony;

/**
 * Created by pR0 on 03-07-2016.
 */
public class Notifications {
    String _id, details, pic, option, type;

    public Notifications(String _id, String details, String pic, String option, String type) {
        this._id =_id;
        this.details = details;
        this.pic = pic;
        this.option = option;
        this.type = type;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public String getDetails() {
        return details;
    }

    public String getOption() {
        return option;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
