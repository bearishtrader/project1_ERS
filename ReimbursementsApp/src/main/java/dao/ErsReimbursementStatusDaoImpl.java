package dao;

import models.ErsReimbursementStatus;
import models.ErsReimbursementStatus;
import models.ErsReimbursementStatus;
import org.apache.log4j.Logger;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErsReimbursementStatusDaoImpl implements ErsReimbursementStatusDao{
    static private Logger logger = Logger.getLogger(ErsReimbursementStatusDaoImpl.class);
    
    @Override
    public List<ErsReimbursementStatus> getAllErsReimbursementStatus() {
        List<ErsReimbursementStatus> ersReimbursementStatuses = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement_status ORDER BY reimb_status_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursementStatuses.add(new ErsReimbursementStatus(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursementStatuses;
    }

    @Override
    public ErsReimbursementStatus getOneErsReimbursementStatus(Integer reimbStatusId) {
        ErsReimbursementStatus ersReimbursementStatus = null;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement_status WHERE reimb_status_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reimbStatusId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursementStatus = new ErsReimbursementStatus(rs.getInt(1), rs.getString(2));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursementStatus;
    }
}
