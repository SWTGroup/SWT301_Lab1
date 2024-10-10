package sample.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import sample.dto.Account;
import sample.utils.DBUtils;
import sample.exceptions.AccountException;

public class AccountDAO {

    // Define constants for field names
    private static final String FULLNAME = "fullname";
    private static final String ACCID = "accID";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PHONE = "phone";
    private static final String STATUS = "status";
    private static final String ROLE = "role";
    
    public static Account getAccount(String email, String password) {
        Account acc = null;
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "SELECT " + ACCID + ", " + EMAIL + ", " + PASSWORD + ", " + FULLNAME + ", " + PHONE + ", " + STATUS + ", " + ROLE
                        + " FROM dbo.Accounts "
                        + " WHERE " + STATUS + " = 1 AND " + EMAIL + " = ? AND " + PASSWORD + " = ? COLLATE Latin1_General_CS_AS";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setString(1, email);
                    pst.setString(2, password);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            int accID = rs.getInt(ACCID);
                            String emailDb = rs.getString(EMAIL);
                            String passwordDb = rs.getString(PASSWORD);
                            String fullname = rs.getString(FULLNAME);
                            String phone = rs.getString(PHONE);
                            int status = rs.getInt(STATUS);
                            int role = rs.getInt(ROLE);
                            acc = new Account(accID, emailDb, passwordDb, fullname, status, phone, role);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

   public static List<Account> getAccounts() throws AccountException {

    List<Account> accounts = new ArrayList<>();
    
    try (Connection cn = DBUtils.makeConnection()) {
        if (cn != null) {
            String sql = "SELECT " + ACCID + ", " + EMAIL + ", " + PASSWORD + ", " + FULLNAME + ", " + PHONE + ", " + STATUS + ", " + ROLE + " FROM dbo.Accounts";
            try (Statement st = cn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    // Retrieve account information
                    int accID = rs.getInt(ACCID);
                    String email = rs.getString(EMAIL);
                    String password = rs.getString(PASSWORD);
                    String fullname = rs.getString(FULLNAME);
                    String phone = rs.getString(PHONE);
                    int status = rs.getInt(STATUS);
                    int role = rs.getInt(ROLE);
                    
                    // Create account object
                    Account acc = new Account(accID, email, password, fullname, phone, status, role);
                    
                    // Add account to the list
                    accounts.add(acc);
                }
            }
        }
    } catch (SQLException e) {
        throw new AccountException("Error fetching accounts from the database", e);
    } catch (Exception e) {
        throw new AccountException("An unexpected error occurred", e);
    }
    
    return accounts;
}

    public static boolean updateAccountStatus(String email, int status) {
        boolean result = false;
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "UPDATE Accounts SET " + STATUS + " = ? WHERE " + EMAIL + " = ?";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setInt(1, status);
                    pst.setString(2, email);
                    result = pst.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean updateAccount(String email, String newPassword, String newFullname, String newPhone) {
        boolean result = false;
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "UPDATE Accounts SET " + PASSWORD + " = ?, " + FULLNAME + " = ?, " + PHONE + " = ? WHERE " + EMAIL + " = ?";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setString(1, newPassword);
                    pst.setString(2, newFullname);
                    pst.setString(3, newPhone);
                    pst.setString(4, email);
                    result = pst.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int insertAccount(String newEmail, String newPassword, String newFullname, String
newPhone, int newStatus, int newRole) throws Exception {
        int result = 0;
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "INSERT INTO dbo.Accounts (" + EMAIL + ", " + PASSWORD + ", " + FULLNAME + ", " + PHONE + ", " + STATUS + ", " + ROLE + ") "
                        + "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setString(1, newEmail);
                    pst.setString(2, newPassword);
                    pst.setString(3, newFullname);
                    pst.setString(4, newPhone);
                    pst.setInt(5, newStatus);
                    pst.setInt(6, newRole);
                    result = pst.executeUpdate();
                }
            }
        }
        return result;
    }

    public static boolean updateToken(String email, String token) {
        boolean result = false;
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "UPDATE Accounts SET token = ? WHERE " + EMAIL + " = ?";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setString(1, token);
                    pst.setString(2, email);
                    result = pst.executeUpdate() > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Account getAccountByToken(String token) {
        Account acc = null;
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "SELECT " + ACCID + ", " + EMAIL + ", " + PASSWORD + ", " + FULLNAME + ", " + PHONE + ", " + STATUS + ", " + ROLE + ", token "
                        + "FROM dbo.Accounts "
                        + "WHERE token = ? COLLATE Latin1_General_CS_AS";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setString(1, token);
                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            int accID = rs.getInt(ACCID);
                            String emailDb = rs.getString(EMAIL);
                            String passwordDb = rs.getString(PASSWORD);
                            String fullname = rs.getString(FULLNAME);
                            String phone = rs.getString(PHONE);
                            int status = rs.getInt(STATUS);
                            int role = rs.getInt(ROLE);
                            String tokenDb = rs.getString("token");
                            acc = new Account(accID, emailDb, passwordDb, fullname, status, phone, role, tokenDb);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acc;
    }

    public static ArrayList<Account> getAccountsByEmail(String email) throws Exception {
        ArrayList<Account> list = new ArrayList<>();
        try (Connection cn = DBUtils.makeConnection()) {
            if (cn != null) {
                String sql = "SELECT " + ACCID + ", " + EMAIL + ", " + PASSWORD + ", " + FULLNAME + ", " + PHONE + ", " + STATUS + ", " + ROLE 
                        + " FROM dbo.Accounts "
                        + "WHERE " + EMAIL + " = ? COLLATE Latin1_General_CS_AS";
                try (PreparedStatement pst = cn.prepareStatement(sql)) {
                    pst.setString(1, email);
                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            int accID = rs.getInt(ACCID);
                            String emailDb = rs.getString(EMAIL);
                            String password = rs.getString(PASSWORD);
                            String fullname = rs.getString(FULLNAME);
                            String phone = rs.getString(PHONE);
                            int status = rs.getInt(STATUS);
                            int role = rs.getInt(ROLE);
                            Account acc = new Account(accID, emailDb, password, fullname, status, phone, role);
                            list.add(acc);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static ArrayList<Account> getAccountByEmail(String search) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
