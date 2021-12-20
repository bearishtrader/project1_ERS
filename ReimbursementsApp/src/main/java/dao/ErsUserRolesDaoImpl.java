package dao;

import models.ErsUserRoles;
import models.ErsUserRoles;
import models.ErsUserRoles;
import org.apache.log4j.Logger;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErsUserRolesDaoImpl implements ErsUserRolesDao {
    static private Logger logger = Logger.getLogger(ErsUserRolesDaoImpl.class);

    @Override
    public List<ErsUserRoles> getAllErsUserRoles() {
        List<ErsUserRoles> ersUserRolesRoles = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_user_roles ORDER BY ers_user_role_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersUserRolesRoles.add(new ErsUserRoles(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersUserRolesRoles;
    }

    @Override
    public ErsUserRoles getOneErsUserRole(Integer ersUserRoleId) {
        ErsUserRoles ersUserRoles = null;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_user_roles WHERE ers_user_role_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ersUserRoleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersUserRoles = new ErsUserRoles(rs.getInt(1), rs.getString(2));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersUserRoles;
    }
}
