package ro.exampledana.service;

import ro.exampledana.entity.File;
import ro.exampledana.entity.Priority;
import ro.exampledana.entity.Status;
import ro.exampledana.entity.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class TaskService {
    private Connection dbConnection;

    public TaskService(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void createTask(Task task, int userTaskRelationId, String username){
        String command1 = "insert into tasks values (?, ?, ?, ?, ?, ?)";
        String command2 = "insert into user_task_relation values (?, ?, ?)";
        try {
            PreparedStatement preparedStatement1 = this.dbConnection.prepareStatement(command1);
            preparedStatement1.setInt(1,task.getId());
            preparedStatement1.setString(2, task.getDescription());
            preparedStatement1.setDate(3, new Date(task.getInitialDate().getTimeInMillis()));
            preparedStatement1.setDate(4,  new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement1.setString(5, task.getPriority());
            preparedStatement1.setString(6,task.getStatus());
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = this.dbConnection.prepareStatement(command2);
            preparedStatement2.setInt(1, userTaskRelationId);
            preparedStatement2.setString(2, username);
            preparedStatement2.setInt(3, task.getId());
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//*********************************


    public void createTaskFile(File file){
        String command = "insert into files values (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(command);
            preparedStatement.setInt(1,file.getId());
            preparedStatement.setInt(2, file.getTaskId());
            preparedStatement.setString(3, file.getName());
            preparedStatement.setString(4, file.getPath());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int userTaskRelationNextId() {
        int nextIndex=1;
        try {
            Statement statement = this.dbConnection.createStatement();
            ResultSet results = statement.executeQuery("select user_task_relation.id\n" +
                    "from user_task_relation\n" +
                    "order by id desc\n" +
                    "limit 1");
            results.next();
            nextIndex=  results.getInt(1)+1;

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return nextIndex;
    }
    public int fileNextId() {
        int nextIndex=1;
        try {
            Statement statement = this.dbConnection.createStatement();
            ResultSet results = statement.executeQuery("select files.id\n" +
                    "from files\n" +
                    "order by id desc\n" +
                    "limit 1");
            results.next();
            nextIndex=  results.getInt(1)+1;

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return nextIndex;
    }

    public int taskNextId() {
        int nextIndex=1;
        try {
            Statement statement = this.dbConnection.createStatement();
            ResultSet results = statement.executeQuery("select tasks.id\n" +
                    "from tasks\n" +
                    "order by id desc\n" +
                    "limit 1");
            results.next();
            nextIndex=  results.getInt(1)+1;

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return nextIndex;
    }

       public Task findTaskById(int id) {
        Task task=null;
        List<File> files =  findFilesByTaskId(id);;
        try {
            PreparedStatement statement1 = this.dbConnection.prepareStatement("select * from tasks where id = ?");
            statement1.setInt(1, id);
            ResultSet result = statement1.executeQuery();
            result.next();

            GregorianCalendar date1= new GregorianCalendar();
            java.util.Date myDate1= new java.util.Date(result.getDate(3).getTime());
            date1.setTime(myDate1);

            GregorianCalendar date2= new GregorianCalendar();
            java.util.Date myDate2= new java.util.Date(result.getDate(4).getTime());
            date2.setTime(myDate2);


            task=  new Task(result.getInt(1),
                            result.getString(2),
                            date1,
                            date2,
                    Priority.valueOf(result.getString(5)),
                    Status.valueOf(result.getString(6).replace(" ","_")),
                    files
            );
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return task;
    }

    public List<File> findFilesByTaskId(int id) {
        List<File> files = new ArrayList<>();
        try {
            PreparedStatement statement2 = this.dbConnection.prepareStatement("select * from files where task_id = ?");
            statement2.setInt(1, id);
            ResultSet results = statement2.executeQuery();

            while (results.next()) {
                files.add(
                        new File(results.getInt(1),
                                results.getInt(2),
                                results.getString(3),
                                results.getString(4)
                        ));
            }
        } catch (SQLException e){
            e.printStackTrace(System.err);
        }
        return files;
    }


    public File findFileById(int id) {
        File file=null;
        try {
            PreparedStatement statement = this.dbConnection.prepareStatement("select * from files where id = ?");
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();


            file=  new File(result.getInt(1),
                    result.getInt(2),
                    result.getString(3),
                    result.getString(4)
                   );
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return file;
    }



    public void updateTask(int id, String description, GregorianCalendar dueDate, Priority priority, Status status) {
       // String priorityAsString= priority.toString();
        String insertCommand = "update tasks set description = ?, due_date = ?, priority = ?, status = ?  where id = ?";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(insertCommand);
            preparedStatement.setString(1, description);
            //preparedStatement.setDate(2, new Date(initialDate.getTimeInMillis()));
            preparedStatement.setDate(2,  new Date(dueDate.getTimeInMillis()));
            preparedStatement.setString(3, priority.toString());
            preparedStatement.setString(4, status.toString());
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        String insertCommand1 = "delete from files where task_id= ?";
        String insertCommand2 = "delete from tasks where id= ?";
        try {
            PreparedStatement preparedStatement1 = this.dbConnection.prepareStatement(insertCommand1);
            preparedStatement1.setInt(1,id);
            preparedStatement1.executeUpdate();

            PreparedStatement preparedStatement2 = this.dbConnection.prepareStatement(insertCommand2);
            preparedStatement2.setInt(1,id);
            preparedStatement2.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> findAll(String username) {
        List<Task> tasks = new ArrayList<>();
        String insertCommand ="SELECT * FROM tasks\n" +
                "JOIN user_task_relation\n" +
                "ON tasks.id = user_task_relation.task_id\n" +
                "JOIN users\n" +
                "ON users.username= user_task_relation.username\n" +
                "WHERE user_task_relation.username= ?";
        try {
            PreparedStatement statement = this.dbConnection.prepareStatement(insertCommand);
            statement.setString(1, username);
            ResultSet results = statement.executeQuery();

            while (results.next()) {
                int taskId=results.getInt(1);
                List<File> files= findFilesByTaskId(taskId);
                GregorianCalendar date1= new GregorianCalendar();
                java.util.Date myDate1= new java.util.Date(results.getDate(3).getTime());
                date1.setTime(myDate1);

                GregorianCalendar date2= new GregorianCalendar();
                java.util.Date myDate2= new java.util.Date(results.getDate(4).getTime());
                date2.setTime(myDate2);


                tasks.add(
                        new Task(taskId,
                                results.getString(2),
                                date1,
                                date2,
                                Priority.valueOf(results.getString(5)),
                                Status.valueOf(results.getString(6).replace(" ","_")),
                                files
                                )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return tasks;
    }
}
