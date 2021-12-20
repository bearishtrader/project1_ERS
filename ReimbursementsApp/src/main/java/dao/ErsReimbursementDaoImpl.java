package dao;

import dto.ReimbStatusDTO;
import models.ErsReimbursement;
import org.apache.log4j.Logger;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErsReimbursementDaoImpl implements ErsReimbursementDao {

    static private Logger logger = Logger.getLogger(ErsReimbursementDaoImpl.class);

    @Override
    public Boolean createErsReimbursement(ErsReimbursement ersReimbursement) {
        Boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = (ersReimbursement.getReimbId() == null) ? "INSERT INTO ers_reimbursement VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?);" :
                    "INSERT INTO ers_reimbursement VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql);
            if (ersReimbursement.getReimbId()==null) {   // DEFAULT in prepared SQL is auto-increment field
                ps.setDouble(1, ersReimbursement.getReimbAmount());
                ps.setTimestamp(2, ersReimbursement.getReimbSubmitted());
                ps.setTimestamp(3, ersReimbursement.getReimbResolved());
                ps.setString(4, ersReimbursement.getReimbDescription());
                ps.setBytes(5, ersReimbursement.getReimbReceipt());
                ps.setInt(6, ersReimbursement.getReimbAuthor());
                if (ersReimbursement.getReimbResolver()==null) ps.setNull(7, java.sql.Types.INTEGER);
                else ps.setInt(7, ersReimbursement.getReimbResolver());
                ps.setInt(8, ersReimbursement.getReimbStatusId());
                ps.setInt(9, ersReimbursement.getReimbTypeId());
            } else {
                ps.setInt(1, ersReimbursement.getReimbId()); // we didn't pass in null as ersReimbursementId so for whatever reason we want ot directly set it
                ps.setDouble(2, ersReimbursement.getReimbAmount());
                ps.setTimestamp(3, ersReimbursement.getReimbSubmitted());
                ps.setTimestamp(4, ersReimbursement.getReimbResolved());
                ps.setString(5, ersReimbursement.getReimbDescription());
                ps.setBytes(6, ersReimbursement.getReimbReceipt());
                ps.setInt(7, ersReimbursement.getReimbAuthor());
                ps.setInt(8, ersReimbursement.getReimbResolver());
                ps.setInt(9, ersReimbursement.getReimbStatusId());
                ps.setInt(10, ersReimbursement.getReimbTypeId());
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
    public List<ErsReimbursement> getAllErsReimbursements() {
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement ORDER BY reimb_author, reimb_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursements.add(new ErsReimbursement(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3),
                        rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7),
                        rs.getInt(8)==0 ? null:rs.getInt(8),  // Resolver is a special case where if its null in the table it brings back 0 so make it null
                        rs.getInt(9), rs.getInt(10)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursements;
    }

    @Override
    public List<ErsReimbursement> getAllErsReimbursementsByAuthor(Integer ersUsersId) {
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_author = ? ORDER BY reimb_author, reimb_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ersUsersId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursements.add(new ErsReimbursement(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3),
                        rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7),
                        rs.getInt(8)==0 ? null:rs.getInt(8),  // Resolver is a special case where if its null in the table it brings back 0 so make it null
                        rs.getInt(9), rs.getInt(10)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursements;
    }

    @Override
    public List<ErsReimbursement> getAllErsReimbursementsByResolver(Integer ersUsersId) {
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_resolver = ? ORDER BY reimb_resolver, reimb_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ersUsersId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursements.add(new ErsReimbursement(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3),
                        rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7), rs.getInt(8),
                        rs.getInt(9), rs.getInt(10)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursements;
    }

    @Override
    public ErsReimbursement getOneErsReimbursement(Integer reimbId) {
        ErsReimbursement ersReimbursement = null;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement WHERE reimb_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reimbId);            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursement = new ErsReimbursement(rs.getInt(1), rs.getDouble(2), rs.getTimestamp(3),
                        rs.getTimestamp(4), rs.getString(5), rs.getBytes(6), rs.getInt(7), rs.getInt(8),
                        rs.getInt(9), rs.getInt(10));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursement;
    }

    @Override
    public Boolean updateErsReimbursement(ErsReimbursement ersReimbursement) {
        Boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "UPDATE ers_reimbursement SET reimb_amount=?, reimb_submitted=?, reimb_resolved=?, reimb_description=?, reimb_receipt=?, reimb_author=?, " +
                "reimb_resolver=?, reimb_status_id=?, reimb_type_id=? WHERE reimb_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, ersReimbursement.getReimbAmount());
            ps.setTimestamp(2, ersReimbursement.getReimbSubmitted());
            ps.setTimestamp(3, ersReimbursement.getReimbResolved());
            ps.setString(4, ersReimbursement.getReimbDescription());
            ps.setBytes(5, ersReimbursement.getReimbReceipt());
            ps.setInt(6, ersReimbursement.getReimbAuthor());
            ps.setInt(7, ersReimbursement.getReimbResolver());
            ps.setInt(8, ersReimbursement.getReimbStatusId());
            ps.setInt(9, ersReimbursement.getReimbTypeId());
            ps.setInt(10, ersReimbursement.getReimbId());   // <- WHERE reimb_id = ?
            if (ps.executeUpdate()==0) success = false;
        }
        catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public Boolean updateErsReimbursement(ReimbStatusDTO reimbStatusDTO) {
        Boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "UPDATE ers_reimbursement SET reimb_resolved=?, reimb_resolver=?, reimb_status_id=? WHERE reimb_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, reimbStatusDTO.getReimbResolved());
            ps.setInt(2, reimbStatusDTO.getReimbResolver());
            ps.setInt(3, reimbStatusDTO.getReimbStatusId());
            ps.setInt(4, reimbStatusDTO.getReimbId());
            if (ps.executeUpdate()==0) success = false;
        }
        catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }

    @Override
    public Boolean deleteErsReimbursement(Integer reimbId) {
        Boolean success = true;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "DELETE FROM ers_reimbursement WHERE reimb_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reimbId);
            if (ps.executeUpdate()==0) success = false;
        }
        catch (SQLException e) {
            logger.error(e);
            success = false;
        }
        return success;
    }
}