package hub.settings;

public class Neighbour {
    private String address;
    private boolean coming;

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setComing(boolean coming) {
        this.coming = coming;
    }

    @Override
    public String toString() {
        return address + " : " + (coming ? "Coming" : "Idle");
    }
}
