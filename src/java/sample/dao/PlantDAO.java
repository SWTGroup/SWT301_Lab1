package sample.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sample.dto.Plant;
import sample.utils.DBUtils;

public class PlantDAO {

    public static ArrayList<Plant> getPlants(String keyword, String searchby) {
        ArrayList<Plant> list = new ArrayList<>();
        Connection cn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            cn = DBUtils.makeConnection();
            if (cn != null) {
                String sql = """
                         select PID, PName, price, imgPath, description, status, Plants.CateID as 'CateID', CateName
                         from Plants join Categories on Plants.CateID=Categories.CateID
                         """;
                if (searchby != null && !searchby.isEmpty()) {
                    if (searchby.equalsIgnoreCase("byname")) {
                        sql += " where Plants.PName like ?";
                    } else if (searchby.equalsIgnoreCase("bycategory")) {
                        sql += " where CateName like ?";
                    }
                }

                pst = cn.prepareStatement(sql); // Initialize pst here
                if (searchby != null && !searchby.isEmpty()) {
                    pst.setString(1, "%" + keyword + "%");
                }
                rs = pst.executeQuery(); // Execute query after setting parameters
                while (rs.next()) {
                    int id = rs.getInt("PID");
                    String name = rs.getString("PName");
                    int price = rs.getInt("price");
                    String imgpath = rs.getString("imgPath");
                    String description = rs.getString("description");
                    int status = rs.getInt("status");
                    int cateid = rs.getInt("CateID");
                    String catename = rs.getString("CateName");
                    Plant plant = new Plant(id, name, price, imgpath, description, status, cateid, catename);
                    list.add(plant);
                }
            }
        } catch (SQLException e) {
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
            }
        }
        return list;
    }

    public static Plant getPlant(int id) throws Exception {
        Plant p = null;
        Connection cn = DBUtils.makeConnection();
        if (cn != null) {
            String sql = """
                         select PID, PName, price, imgPath, description, status, CateID
                         from dbo.Plants
                         where PID=?""";
            try (PreparedStatement pst = cn.prepareStatement(sql)) {
                pst.setInt(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs != null && rs.next()) {
                        String name = rs.getString("PName");
                        int price = rs.getInt("price");
                        String imgpath = rs.getString("imgPath");
                        String description = rs.getString("description");
                        int status = rs.getInt("status");
                        int cateid = rs.getInt("CateID");
                        p = new Plant(id, name, price, imgpath, description, status, cateid, name);
                    }
                }
            }
            cn.close();
        }
        return p;
    }
}
