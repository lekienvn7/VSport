package model;

public class BoLoc  {
    private String value;
    private String label;
    private int count;

    public BoLoc() {
    }

    public BoLoc(String value, String label, int count) {
        this.value = value;
        this.label = label;
        this.count = count;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}