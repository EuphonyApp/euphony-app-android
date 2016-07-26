package euphony.com.euphony;

/**
 * Created by pR0 on 01-07-2016.
 */
public class Booking {
    String _id, bookedBy, booked, type, date, time, status;

    public Booking(String _id, String bookedBy, String booked, String type, String date, String time, String status) {
        this._id = _id;
        this.booked = booked;
        this.bookedBy = bookedBy;
        this.type = type;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    public Booking(){};

    public String getBooked() {
        return booked;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setBooked(String booked) {
        this.booked = booked;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
