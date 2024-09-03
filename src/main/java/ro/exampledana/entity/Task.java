package ro.exampledana.entity;

import ro.exampledana.servlet.TasksServlet;

import java.sql.Date;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;

public class Task extends Doable{
    private int id;
    private String description;
    private GregorianCalendar initialDate;
    private GregorianCalendar dueDate;
    private Priority priority;
    private Status status;
    private String project;
    private List<File> files;

    public Task(int id, String description, GregorianCalendar initialDate, GregorianCalendar dueDate, Priority priority,
                Status status, String project, List<File> files) {
//        this.id = id;
//        this.description = description;
        super(id, description, status);
        this.initialDate = initialDate;
        this.dueDate = dueDate;
        this.priority=priority;
//        this.status=status;
        this.project=project;
        this.files= new ArrayList<>();
        this.files.addAll(files);
    }
    public Task(int id, String description, GregorianCalendar initialDate, GregorianCalendar dueDate, Priority priority,
                Status status, String project) {
//        this.id = id;
//        this.description = description;
        super(id, description, status);
        this.initialDate = initialDate;
        this.dueDate = dueDate;
        this.priority=priority;
//        this.status=status;
        this.project= project;
        this.files= new ArrayList<>();
    }
    public Task(int id, String description, GregorianCalendar dueDate, Priority priority, String project) {
        this(id, description,(GregorianCalendar) GregorianCalendar.getInstance(), dueDate, priority, Status.TO_DO, project);
    }


    public void addFiles(File file){
        files.add(new File(file.getId(), file.getTaskId(), file.getName(),file.getPath()));
    }
//    public int getId() {
//        return super.id;
//    }
//
//    public String getDescription() {
//        return description;
//    }

    public GregorianCalendar getInitialDate() {
        return initialDate;
    }

    public String getFormattedInitialDate(){
        String browserLanguage= TasksServlet.browserLanguage;
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale(browserLanguage));
        String formattedDate = df.format(new java.util.Date(initialDate.getTimeInMillis()));
        return formattedDate;
    }

    public GregorianCalendar getDueDate() {
        return dueDate;
    }

    public String getFormattedDueDate(){
        String browserLanguage= TasksServlet.browserLanguage;
        DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, new Locale(browserLanguage));
        String formattedDate = df.format(new java.util.Date(dueDate.getTimeInMillis()));
        return formattedDate;
    }

    public LocalDate getLocalDueDate(){
        return new Date(dueDate.getTimeInMillis()).toLocalDate();
    }

    public String getPriority() {
        return priority.toString().replace("_", " ");
    }
    public int getPriorityValue(){
        return priority.getValue();
    }

    public List<File> getFiles() {
        List<File> copy= new ArrayList<>();
        copy.addAll(files);
        return copy;
    }

    public String getProject() {
        return project;
    }

    public boolean getIsOverdue(){
        GregorianCalendar day= (GregorianCalendar) GregorianCalendar.getInstance();
        return (dueDate.before(day)||dueDate.equals(day));
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", initialDate=" + initialDate +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", status=" + status +
                ", files=" + files +
                "} " + super.toString();
    }

    //    public String getStatus() {
//        return super.getStatus().toString().replace("_", " ");
//    }


}
