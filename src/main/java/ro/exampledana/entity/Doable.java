package ro.exampledana.entity;

public class Doable {
    private int id;
    private String description;
    private Status status;

    public Doable(int id, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status.toString().replace("_", " ");
    }

    public boolean isHistory(){
        return status== Status.DONE;
    };
}
