package sample.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import sample.dao.OrderDAO;
import sample.dto.Plant;

@WebServlet(name = "SaveShoppingCartServlet", urlPatterns = {"/saveShoppingCartServlet"})
public class saveShoppingCartServlet extends HttpServlet {

    private static final String WARNING = "WARNING";
    private static final String CART_SESSION_ATTR = "cart";
    private static final String NAME_SESSION_ATTR = "name";
    private static final String EMAIL_SESSION_ATTR = "email";
    private static final String VIEW_CART_JSP = "viewCart.jsp";
    private static final String INDEX_JSP = "index.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String name = (String) session.getAttribute(NAME_SESSION_ATTR);
                String email = (String) session.getAttribute(EMAIL_SESSION_ATTR);
                HashMap<String, Plant> cart = (HashMap<String, Plant>) session.getAttribute(CART_SESSION_ATTR);

                if (cart != null && !cart.isEmpty()) {
                    if (name == null || name.isEmpty()) {
                        setWarningAndForward(request, response, "You must log in to finish the shopping.");
                    } else {
                        boolean result = OrderDAO.insertOrder(email, cart);
                        if (result) {
                            session.removeAttribute(CART_SESSION_ATTR);
                            setWarningAndForward(request, response, "Your cart has been saved successfully.");
                        } else {
                            setWarningAndForward(request, response, "Some products are out of stock.");
                        }
                    }
                } else {
                    setWarningAndForward(request, response, "Your cart is empty.");
                }
            } else {
                response.sendRedirect(INDEX_JSP);
            }
        } catch (ServletException | IOException ex) {
            log("Error in SaveShoppingCartServlet: " + ex.getMessage(), ex);
            setWarningAndForward(request, response, "An error occurred while processing your request.");
        }
    }

    private void setWarningAndForward(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {
        request.setAttribute(WARNING, message);
        request.getRequestDispatcher(VIEW_CART_JSP).forward(request, response);
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
        return "Saves the shopping cart to the order database.";
    }
}
