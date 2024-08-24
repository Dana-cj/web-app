package ro.exampledana.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ro.exampledana.entity.File;
import ro.exampledana.entity.Priority;
import ro.exampledana.entity.Status;
import ro.exampledana.entity.Task;
import ro.exampledana.service.TaskService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
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
   // private FileService fileService;
    public static String browserLanguage;
    private static final String UPLOAD_PATH= "C:\\upload\\";

    @Override
    public void init(ServletConfig config) {
               try {
            Context  initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/MyDB");
            dbConnection = ds.getConnection();
            taskService= new TaskService(dbConnection);
           // fileService= new FileService(dbConnection);
        } catch (NamingException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //set tasks active
        request.setAttribute("tasksActive", "active");
        // set the date format according to browser language
        String acceptLanguageHeader = request.getHeader("Accept-Language");
        if (acceptLanguageHeader != null && !acceptLanguageHeader.isEmpty()) {
            String[] languages = acceptLanguageHeader.split(",");
            if (languages.length > 0) {
                browserLanguage = languages[0].trim();
            }
        }
        //read action and fulfill request
        String action = readActionParameter(request);
        switch (action){
            case "home"-> request.getRequestDispatcher("jsps/home.jsp").forward(request,response);
            case "list"-> {
                request.setAttribute("dueDateSortingActive", "active");
                listTasksSortedByDueDate(request, response);
            }
            case "listTasksSortedByPriority"-> {
                request.setAttribute("prioritySortingActive", "active");
                listTasksSortedByPriority(request, response);
            }
            case "listTasksSortedByStatus"-> {
                request.setAttribute("statusSortingActive", "active");
                listTasksSortedByStatus(request, response);
            }
            case "add"->    request.getRequestDispatcher("jsps/task/add.jsp").forward(request,response);
//            case "addFile"->    {
//                int id= Integer.parseInt(request.getParameter("taskId"));
//                Task task= taskService.findById(id);
//                request.setAttribute("task", task);
//                request.getRequestDispatcher("jsps/task/addFile.jsp").forward(request,response);
//            }
            case "edit"-> {
                int id= Integer.parseInt(request.getParameter("taskId"));
                Task task= taskService.findTaskById(id);
                request.setAttribute("task", task);
                request.getRequestDispatcher("jsps/task/edit.jsp").forward(request,response);
            }
            case "delete"-> {
                int id= Integer.parseInt(request.getParameter("taskId"));
                taskService.delete(id);
                request.setAttribute("dueDateSortingActive", "active");
                listTasksSortedByDueDate(request, response);
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
                int taskId= taskService.taskNextId();
                taskService.createTask(new Task(taskId, description, dueDate, priority));

                List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
                try {
                    for (Part filePart : fileParts) {
                        int fileId= taskService.fileNextId();
                        String fileName = "(id="+fileId+")"+Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        String filePath = UPLOAD_PATH + fileName;
                        filePart.write(filePath);

                        taskService.createTaskFile(new File(fileId,taskId, fileName, filePath));
                        //taskService.findTaskById(taskId).addFiles(taskService.findFileById(fileId));
                    }
                   //response.getWriter().print(taskService.findTaskById(taskId).getFiles().get(0).getName());
                } catch (Exception exception) {
                    response.getWriter().print(exception);
                }

                request.setAttribute("dueDateSortingActive", "active");
                listTasksSortedByDueDate(request, response);
            }
//           case "add"-> {
//               String description = request.getParameter("description");
//
//               String dueDateAsString= request.getParameter("dueDate");
//               GregorianCalendar dueDate= null;
//               if(dueDateAsString.isBlank()){
//                   exceptionHandler(response);
//               } else {
//               SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                dueDate = new GregorianCalendar();
//                   try {
//                       java.util.Date date = new java.util.Date(formatter.parse(dueDateAsString).getTime());
//                       dueDate.setTime(date);
//                   } catch (ParseException e) {
//                       exceptionHandler(response);
//                 }
//               }
//               Priority priority=null;
//               try {
//                   priority = Priority.valueOf(request.getParameter("priority").replace(" ","_"));
//               } catch (Exception e) {
//                   exceptionHandler(response);
//               }
//
//               try {
//                   List<Part> fileParts = request.getParts().stream().filter(part -> "files".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList());
//
//                   for (Part filePart : fileParts) {
//                       String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
//                       filePart.write("C:\\upload\\" + fileName);
//                   }
//                   response.getWriter().print("Files uploaded sucessfully.");
//               } catch (Exception exception){
//                   response.getWriter().print(exception);
//               }
//
//               taskService.createTask(new Task(taskService.nextId(), description, dueDate, priority));
//               request.setAttribute("dueDateSortingActive", "active");
//               listTasksSortedByDueDate(request, response);
//           }
           case "edit"->{
               int id= Integer.parseInt(request.getParameter("taskId"));
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
               taskService.updateTask(id,description, dueDate, Priority.valueOf(priorityAsString.replace(" ","_")),
                       Status.valueOf(statusAsString.replace(" ","_")));
               request.setAttribute("dueDateSortingActive", "active");
               listTasksSortedByDueDate(request, response);
           }
       }
    }


    private void listTasksSortedByDueDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks= taskService.findAll()
                .stream()
                .filter(task->!task.getStatus().equalsIgnoreCase("DONE"))
                .sorted(Comparator.comparing(Task::getDueDate)
                        .thenComparing(Task::getPriorityValue)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
    }

    private void listTasksSortedByPriority(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks= taskService.findAll()
                .stream()
                .filter(task->!task.getStatus().equalsIgnoreCase("DONE"))
                //.filter(task->!task.isHistory())
                .sorted(Comparator.comparing(Task::getPriorityValue)
                        .thenComparing(Task::getDueDate)
                        .thenComparing(Task::getInitialDate))
                .collect(Collectors.toList());
        request.setAttribute("tasks", tasks);
        request.getRequestDispatcher("jsps/task/list.jsp").forward(request, response);
    }

    private void listTasksSortedByStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Task> tasks= taskService.findAll()
                .stream()
                .filter(task->!task.getStatus().equalsIgnoreCase("DONE"))
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
                "             <button type=\"button\" class=\"btn btn-primary btn-sm\">Go back to tasks page</button>\n" +
                "        </a>\n" +
                "    </body>\n" +
                "</html>");
    }


//    class BeeperControl {
//        private final ScheduledExecutorService scheduler =
//                Executors.newScheduledThreadPool(10);
//
//        public void beepForAnHour() {
////        final Runnable beeper = new Runnable() {
////            public void run() { System.out.println("beep"); }
////        };
//
//
//            final ScheduledFuture<?> beeperHandle =
//                    scheduler.scheduleAtFixedRate(new GmailMessage(), 0, 10, SECONDS);
//
//            scheduler.schedule(new Runnable() {
//                public void run() { beeperHandle.cancel(true); }
//            }, 15, SECONDS);
//        }
//    }
}
