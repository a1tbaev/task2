package kg.example.task2.objects;

import jakarta.xml.bind.annotation.XmlElement;

public class Creation {
    private String epoch;
    private String date;

    @XmlElement(name = "Epoch")
    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    @XmlElement(name = "Date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
