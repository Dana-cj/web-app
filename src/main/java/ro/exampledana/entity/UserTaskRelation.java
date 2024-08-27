package ro.exampledana.entity;

public class UserTaskRelation {
    private int id;
    private String username;
    private int taskId;

    public UserTaskRelation(int id, String username, int taskId) {
        this.id = id;
        this.username = username;
        this.taskId = taskId;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getTaskId() {
        return taskId;
    }
}
