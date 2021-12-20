package dao;

import models.ErsUsers;
import org.apache.log4j.Logger;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ErsUsersDaoImpl implements ErsUsersDao{
    static private Logger logger = Logger.getLogger(ErsUsersDaoImpl.class);

    @Override
    public Boolean createErsUser(ErsUsers ersUser) {
        boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = (ersUser.getErsUsersId()==null) ? "INSERT INTO ers_users VALUES (DEFAULT, ?, ?, ?, ?, ?, ?);" :
                    "INSERT INTO ers_users VALUES (?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            if (ersUser.getErsUsersId()==null) {   // DEFAULT in prepared SQL is auto-increment field
                ps.setString(1, ersUser.getErsUsername());
                ps.setString(2, ersUser.getErsPassword());
                ps.setString(3, ersUser.getUserFirstName());
                ps.setString(4, ersUser.getUserLastName());
                ps.setString(5, ersUser.getUserEmail());
                ps.setInt(6, ersUser.getUserRoleId());
            } else {
                ps.setInt(1, ersUser.getErsUsersId()); // we didn't pass in null as ersUserId so for whatever reason we want ot directly set it
                ps.setString(2, ersUser.getErsUsername());
                ps.setString(3, ersUser.getErsPassword());
                ps.setString(4, ersUser.getUserFirstName());
                ps.setString(5, ersUser.getUserLastName());
                ps.setString(6, ersUser.getUserEmail());
                ps.setInt(7, ersUser.getUserRoleId());
            }
            if (ps.executeUpdate()==0) success = false;
        }
        catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public List<ErsUsers> getAllErsUsers() {
        List<ErsUsers> ersUsers = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_users ORDER BY ers_users_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersUsers.add(new ErsUsers(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersUsers;
    }

    @Override
    public ErsUsers getOneErsUser(Integer ersUserId) {
        ErsUsers ersUser = null;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_users WHERE ers_users_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ersUserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersUser = new ErsUsers(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),
                                rs.getString(5), rs.getString(6), rs.getInt(7));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersUser;
    }

    @Override
    public ErsUsers getOneErsUser(String ersUsername, String userEmail) {
        ErsUsers ersUser = null;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_users WHERE ers_username = ? AND user_email = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ersUsername);
            ps.setString(2, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersUser = new ErsUsers(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersUser;
    }

    @Override
    public List<ErsUsers> getCertainErsUsers(Set<Integer> ersUserIds) {
        List<ErsUsers> ersUsers = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            // The ersUserIds array when converted to a string will be [1, 5, 6, 8] for example just replace brackets with parentheses (1, 5, 6, 8) to formulate our set
            // in the SQL statement
            String ersUserIdsString = ersUserIds.toString();
            //System.out.println(ersUserIdsString);
            StringBuilder ersUserIdSet = new StringBuilder(ersUserIdsString);
            ersUserIdSet.replace(0,1, "(");
            ersUserIdSet.replace(ersUserIdSet.length()-1, ersUserIdSet.length(), ")");
            String sql = "SELECT * FROM ers_users WHERE ers_users_id IN " + ersUserIdSet + " ORDER BY ers_users_id;";
            //System.out.println(sql);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersUsers.add(new ErsUsers(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getInt(7)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersUsers;
    }

    @Override
    public Boolean updateErsUser(ErsUsers ersUser) {
        boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "UPDATE ers_users SET ers_username=?, ers_password=?, user_first_name=?, user_last_name=?, user_email=?, user_role_id=? WHERE ers_users_id=?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,ersUser.getErsUsername());
            ps.setString(2,ersUser.getErsPassword());
            ps.setString(3,ersUser.getUserFirstName());
            ps.setString(4,ersUser.getUserLastName());
            ps.setString(5,ersUser.getUserEmail());
            ps.setInt(6,ersUser.getUserRoleId());
            ps.setInt(7,ersUser.getErsUsersId());
            if (ps.executeUpdate()==0) success = false;
        }
        catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public Boolean deleteErsUser(Integer ersUserId) {
        boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "DELETE FROM ers_users WHERE ers_users_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ersUserId);
            if (ps.executeUpdate()==0) success = false;
        }
        catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        //System.out.println("ClientDaoImpl::deleteClient("+clientId+") success="+success);
        return success;
    }
}
