package ro.exampledana.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ro.exampledana.entity.File;
import ro.exampledana.entity.Priority;
import ro.exampledana.entity.Status;
import ro.exampledana.entity.Task;
import ro.exampledana.service.TaskService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@WebServlet(name = "TasksServlet", urlPatterns = {"/tasks"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class TasksServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private Connection dbConnection;
    public TaskService taskService;
    public static String browserLanguage;
    private static final String UPLOAD_PATH= "C:\\upload\\";
    private String username;
    private String project;

    @Override
    public void init(ServletConfig config) {
               try {
            Context  initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
            dbConnection = ds.getConnection();
            taskService= new TaskService(dbConnection);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //set tasks active
        request.setAttribute("tasksActive", "active");
        //retrieve username
        HttpSession session = request.getSession(false);
        username = (String) session.getAttribute("username");
        // set the date format according to browser language
        String acceptLanguageHeader = request.getHeader("Accept-Language");
        if (acceptLanguageHeader != null && !acceptLanguageHeader.isEmpty()) {
            String[] languages = acceptLanguageHeader.split(",");
            if (languages.length > 0) {
                browserLanguage = languages[0].trim();
            }
        }

        //List<String> projects= taskService.findAllProjectsOfUser(username);
        List<String> projects=new ArrayList<>();
        taskService.findAllTasksOfUser(username)
                .stream()
                .filter(task->!task.getStatus().equals("DONE"))
                .map(task->task.getProject().toUpperCase())
                .filter(project->project!=null)
                .forEach(project->{
                    if(!projects.contains(project)){
                        projects.add(project);
                    }
                });

        request.setAttribute("projects", projects);
        project= request.getParameter("project");
        if(project!=null&&!project.isBlank()){
            request.setAttribute("filterActive", "active");
            request.setAttribute("project", project);
        }
        //read action and fulfill request
        String action = readActionParameter(request);
        switch (action){
            //case "home"->response.getWriter().println("Username: "+username);//request.getRequestDispatcher("jsps/home.jsp").forward(request,response);
            case "list"-> {
                request.setAttribute("dueDateSortingActive", "active");
                listTasksSortedByDueDate(request, response, project);
            }
            case "listTasksSortedByPriority"-> {
                request.setAttribute("prioritySortingActive", "active");
                listTasksSortedByPriority(request, response, project);
            }
            case "listTasksSortedByStatus"-> {
                request.setAttribute("statusSortingActive", "active");
                listTasksSortedByStatus(request, response, project);
            }
//            case "filterByProject"-> {
//                request.setAttribute("filterActive", "active");
//                response.getWriter().print(request.getContextPath());
//            }
            case "add"->    request.getRequestDispatcher("jsps/task/add.jsp").forward(request,response);
//            case "edit"-> {
//                int id= Integer.parseInt(request.getParameter("taskId"));
//                Task task= taskService.findTaskById(id);
//                request.setAttribute("task", task);
//                request.getRequestDispatcher("jsps/task/editOld.jsp").forward(request,response);
//            }
            case "editTask"-> {
                int id= Integer.parseInt(request.getParameter("taskId"));
                Task task= taskService.findTaskById(id);
                request.setAttribute("task", task);
                request.getRequestDispatcher("jsps/task/editTask.jsp").forward(request,response);
            }
            case "delete"-> {
                int id= Integer.parseInt(request.getParameter("taskId"));
                taskService.deleteTask(id);
                request.setAttribute("dueDateSortingActive", "active");
                listTasksSortedByDueDate(request, response, project);
                //response.sendRedirect(request.getContextPath()+"/tasks");
            }
            case "deleteFile"->{
                int taskId= Integer.parseInt(request.getParameter("taskId"));
                int fileId= Integer.parseInt(request.getParameter("fileId"));
                taskService.deleteFile(fileId);
                request.setAttribute("task", taskService.findTaskById(taskId));
                request.getRequestDispatcher("jsps/task/editTask.jsp").forward(request,response);
            }
            case "download"-> {
                String fileName = request.getParameter("fileName");
                // Get the upload directory path
                String filePath = UPLOAD_PATH + fileName;
                java.io.File downloadFile = new java.io.File(filePath);
                // Check if file exists
                if (!downloadFile.exists()) {
                    response.getWriter().print("File not found");
                    return;
                }
                // Set response content type
                response.setContentType("application/octet-stream");
                response.setContentLength((int) downloadFile.length());
                response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

                // Write file content to response
                try (FileInputStream inStream = new FileInputStream(downloadFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = inStream.read(buffer)) != -1) {
                        response.getOutputStream().write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tasksActive", "active");
        String action = readActionParameter(request);
        switch (action){
            case "add"-> {
                String description = request.getParameter("description");
                String dueDateAsString = request.getParameter("dueDate");
                GregorianCalendar dueDate = null;
                if (dueDateAsString.isBlank()) {
                    exceptionHandler(response);
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    dueDate = new GregorianCalendar();
                    try {
                        java.util.Date date = new java.util.Date(formatter.parse(dueDateAsString).getTime());
                        dueDate.setTime(date);
                    } catch (ParseException e) {
                        exceptionHandler(response);
                    }
                }
                Priority priority = null;
                try {
                    priority = Priority.valueOf(request.getParameter("priority").replace(" ", "_"));
                } catch (Exception e) {
                    exceptionHandler(response);
                }
                String project= request.getParameter("project");
                int taskId= taskService.taskNextId();
                taskService.createTask(new Task(taskId, description, dueDate, priority, project), taskService.userTaskRelationNextId(), username);

                List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
                try {
                    for (Part filePart : fileParts) {
                        int fileId= taskService.fileNextId();
                        String fileName = "(id="+fileId+")"+Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        String filePath = UPLOAD_PATH + fileName;
                        filePart.write(filePath);

                        taskService.createTaskFile(new File(fileId,taskId, fileName, filePath));
                        taskService.findTaskById(taskId).addFiles(taskService.findFileById(fileId));
                    }
                   //response.getWriter().print(taskService.findTaskById(taskId).getFiles().get(0).getName());
                } catch (Exception exception) {
                    response.getWriter().print(exception);
                }
                request.setAttribute("dueDateSortingActive", "active");
                //listTasksSortedByDueDate(request, response);
                response.sendRedirect(request.getContextPath()+"/tasks");
            }
//           case "edit"->{
//               int id= Integer.parseInt(request.getParameter("taskId"));
//               String description = request.getParameter("description");
//               String dueDateAsString= request.getParameter("dueDate");
//               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//               GregorianCalendar dueDate= new GregorianCalendar();
//               try {
//                   java.util.Date date= new java.util.Date(formatter.parse(dueDateAsString).getTime());
//                   dueDate.setTime(date);
//               } catch (ParseException e) {
//                   throw new RuntimeException(e);
//               }
//               String priorityAsString= request.getParameter("priority");
//               String statusAsString= request.getParameter("status");
//               String project= request.getParameter("project");
//               taskService.updateTask(id,description, dueDate, Priority.valueOf(priorityAsString.replace(" ","_")),
//                       Status.valueOf(statusAsString.replace(" ","_")), project);
//               request.setAttribute("dueDateSortingActive", "active");
//               //listTasksSortedByDueDate(request, response);
//               response.sendRedirect(request.getContextPath()+"/tasks");
//           }
            case "editTask"->{
                int taskId = Integer.parseInt(request.getParameter("taskId"));
                String description = request.getParameter("description");
                String dueDateAsString= request.getParameter("dueDate");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                GregorianCalendar dueDate= new GregorianCalendar();
                try {
                    java.util.Date date= new java.util.Date(formatter.parse(dueDateAsString).getTime());
                    dueDate.setTime(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String priorityAsString= request.getParameter("priority");
                String statusAsString= request.getParameter("status");
                String project= request.getParameter("project");
                taskService.updateTask(taskId,description, dueDate, Priority.valueOf(priorityAsString.replace(" ","_")),
                        Status.valueOf(statusAsString.replace(" ","_")), project);


                //int taskId= Integer.parseInt(request.getParameter("taskId"));
                List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
                try {
                    for (Part filePart : fileParts) {
                        int fileId= taskService.fileNextId();
                        String fileName = "(id="+fileId+")"+Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        String filePath = UPLOAD_PATH + fileName;
                        filePart.write(filePath);

                        taskService.createTaskFile(new File(fileId,taskId, fileName, filePath));
                    }
                } catch (Exception exception) {
                    response.getWriter().print(exception);
                }
                request.setAttribute("dueDateSortingActive", "active");
                //listTasksSortedByDueDate(request, response);
                response.sendRedirect(request.getContextPath()+"/tasks");
            }
       }
    }

    private void listTasksSortedByDueDate(HttpServletRequest request, HttpServletResponse response, String project) throws ServletException, IOException {
        List<Task> tasks= taskService.findAllTasksOfUser(username)
                .stream()
                .filter(task->!task.getStatus().equalsIgnoreCase("DONE"))
                .filter(task-> task.getProject().toUpperCase().matches((project==null||project.isBlank()||project.equalsIgnoreCase("all"))?".*":project.toUpperCase()))
                .sorted(Comparator.comparing(Task::getDueDate)
                        .thenComparing(Task::getPriorityValue)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
    }

    private void listTasksSortedByPriority(HttpServletRequest request, HttpServletResponse response, String project) throws ServletException, IOException {
        List<Task> tasks= taskService.findAllTasksOfUser(username)
                .stream()
                .filter(task->!task.getStatus().equalsIgnoreCase("DONE"))
                .filter(task-> task.getProject().toUpperCase().matches((project==null||project.isBlank()||project.equalsIgnoreCase("all"))?".*":project.toUpperCase()))
                //.filter(task->!task.isHistory())
                .sorted(Comparator.comparing(Task::getPriorityValue)
                        .thenComparing(Task::getDueDate)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
    }

    private void listTasksSortedByStatus(HttpServletRequest request, HttpServletResponse response, String project) throws ServletException, IOException {
        List<Task> tasks= taskService.findAllTasksOfUser(username)
                .stream()
                .filter(task->!task.getStatus().equalsIgnoreCase("DONE"))
                .filter(task-> task.getProject().toUpperCase().matches((project==null||project.isBlank()||project.equalsIgnoreCase("all"))?".*":project.toUpperCase()))
                //.filter(task-> !task.isHistory())
                .sorted(Comparator.comparing(Task::getStatus)
                        .thenComparing(Task::getDueDate)
                        .thenComparing(Task::getPriority)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
    }

    private static String readActionParameter(HttpServletRequest request) {
        String action= request.getParameter("action");
        action= (action==null)? "list":action;
        return action;
    }

    private static void exceptionHandler(HttpServletResponse response) throws IOException {
        // Set response content type
        response.setContentType("text/html");

        // Respond back with the user data
        PrintWriter out = response.getWriter();
        out.println("<html>\n" +
                "    <body>\n" +
                "        <h2>Invalid value! Try again!</h2>\n" +
                "        <a href=\"/new-web-app/tasks?action=list\">\n" +
                "             <h3>Go back to list page</h3>\n" +
                "        </a>\n" +
                "    </body>\n" +
                "</html>");
    }
}
