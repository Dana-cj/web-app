package ro.exampledana.entity;

public class File {

    private int id;
    private int taskId;

    private String name;
    private String path;

    public File(int id, int taskId, String name, String path) {
        this.id = id;
        this.taskId = taskId;
        this.name= name;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}
