package kg.example.task2.objects;

import jakarta.xml.bind.annotation.XmlElement;

public class Method {
    private String methodName;
    private String methodType;
    private String assembly;

    @XmlElement(name = "Name")
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @XmlElement(name = "Type")
    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    @XmlElement(name = "Assembly")
    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }
}

