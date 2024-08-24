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

    public void createTask(Task task){
        String command = "insert into tasks values (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(command);
            preparedStatement.setInt(1,task.getId());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, new Date(task.getInitialDate().getTimeInMillis()));
            preparedStatement.setDate(4,  new Date(task.getDueDate().getTimeInMillis()));
            preparedStatement.setString(5, task.getPriority());
            preparedStatement.setString(6,task.getStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
//*********************************


    public void createTaskFile(File file){//(int id, int taskId, String name, String path)
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

    public int fileNextId() {
        int nextIndex=1;
        try {
            Statement statement = dbConnection.createStatement();
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

    //***************************************
    public int taskNextId() {
        int nextIndex=1;
        try {
            Statement statement = dbConnection.createStatement();
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
        List<File> files = new ArrayList<>();
        try {
            PreparedStatement statement1 = dbConnection.prepareStatement("select * from tasks where id = ?");
            statement1.setInt(1, id);
            ResultSet result = statement1.executeQuery();
            result.next();

            PreparedStatement statement2 = dbConnection.prepareStatement("select * from files where task_id = ?");
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


//    public File findFileById(int id) {
//        File file=null;
//        try {
//            PreparedStatement statement = dbConnection.prepareStatement("select * from files where id = ?");
//            statement.setInt(1, id);
//            ResultSet result = statement.executeQuery();
//            result.next();
//
//
//            file=  new File(result.getInt(1),
//                    result.getInt(2),
//                    result.getString(3),
//                    result.getString(4)
//                   );
//        } catch (SQLException e) {
//            e.printStackTrace(System.err);
//        }
//        return file;
//    }



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
        String insertCommand = "delete from tasks where id= ?";
        try {
            PreparedStatement preparedStatement = this.dbConnection.prepareStatement(insertCommand);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try {
            Statement statement = dbConnection.createStatement();
            ResultSet results = statement.executeQuery("select * from tasks");
            while (results.next()) {
                GregorianCalendar date1= new GregorianCalendar();
                java.util.Date myDate1= new java.util.Date(results.getDate(3).getTime());
                date1.setTime(myDate1);

                GregorianCalendar date2= new GregorianCalendar();
                java.util.Date myDate2= new java.util.Date(results.getDate(4).getTime());
                date2.setTime(myDate2);

                tasks.add(
                        new Task(results.getInt(1),
                                results.getString(2),
                                date1,
                                date2,
                                Priority.valueOf(results.getString(5)),
                                Status.valueOf(results.getString(6).replace(" ","_"))));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return tasks;
    }
}
