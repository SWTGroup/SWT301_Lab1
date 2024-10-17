package sample.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import javax.security.auth.login.AccountException;
import sample.dao.AccountDAO;
import sample.dto.Account;

@WebServlet(name = "manageAccountsServlet", urlPatterns = {"/manageAccountsServlet"})
public class manageAccountsServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            ArrayList<Account> list = (ArrayList<Account>) AccountDAO.getAccounts();
            request.setAttribute("accountList", list);
            request.getRequestDispatcher("ManageAccounts.jsp").forward(request, response);
        } catch (ServletException | IOException | AccountException ex) {
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
