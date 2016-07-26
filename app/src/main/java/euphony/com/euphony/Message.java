package euphony.com.euphony;

/**
 * Created by pR0 on 20-06-2016.
 */
public class Message {
    String data, to, frm;

    public Message(String data, String to, String frm) {
        this.data = data;
        this.to = to;
        this.frm = frm;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFrm() {
        return frm;
    }

    public void setFrm(String frm) {
        this.frm = frm;
    }
}
