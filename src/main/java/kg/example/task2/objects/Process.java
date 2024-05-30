package kg.example.task2.objects;

import jakarta.xml.bind.annotation.XmlElement;

public class Process {
    private String name;
    private String id;
    private Start start;

    @XmlElement(name = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "Id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "Start")
    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }
}

