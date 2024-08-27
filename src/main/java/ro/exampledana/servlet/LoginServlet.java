package ro.exampledana.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsps/login/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String newUsername = req.getParameter("newusername");
        String newPassword = req.getParameter("newpassword");

        if(username!=null&&password!=null) {
            if (username.equals("admin") && password.equals("admin")||username.equals("dana") && password.equals("dana")) {
                HttpSession session=req.getSession();
                session.setAttribute("username", username);
                resp.sendRedirect(getServletContext().getContextPath() + "/tasks");
            } else {
                req.setAttribute("error", "Invalid username or password!");
                req.getRequestDispatcher("/jsps/login/login.jsp").forward(req, resp);
            }
        }
        if(newUsername!=null&&newPassword!=null) {
            // userService.create(newUsername, newPassword);
            if (!newUsername.equals("admin")) {

                req.setAttribute("accountCreated", "Thanks for signing up. Your account has been created");
                req.getRequestDispatcher("/jsps/login/login.jsp").forward(req, resp);
            } else {
                req.setAttribute("errorNewAccount", "This username is not available. Try another one!");
                req.getRequestDispatcher("/jsps/login/login.jsp").forward(req, resp);
            }
        }
    }
}
