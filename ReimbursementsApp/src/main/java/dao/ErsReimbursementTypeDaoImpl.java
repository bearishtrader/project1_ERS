package dao;

import models.ErsReimbursementType;
import models.ErsReimbursementType;
import org.apache.log4j.Logger;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErsReimbursementTypeDaoImpl implements ErsReimbursementTypeDao {
    static private Logger logger = Logger.getLogger(ErsReimbursementTypeDaoImpl.class);

    @Override
    public List<ErsReimbursementType> getAllErsReimbursementType() {
        List<ErsReimbursementType> ersReimbursementTypes = new ArrayList<>();
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement_type ORDER BY reimb_type_id;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursementTypes.add(new ErsReimbursementType(rs.getInt(1), rs.getString(2)));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursementTypes;
    }

    @Override
    public ErsReimbursementType getOneErsReimbursementType(Integer reimbTypeId) {
        ErsReimbursementType ersReimbursementType = null;
        try(Connection conn = DriverManager.getConnection(ConnectionUtil.jdbcConnectionURL, ConnectionUtil.databaseUsername, ConnectionUtil.databasePassword)) {
            String sql = "SELECT * FROM ers_reimbursement_type WHERE reimb_type_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reimbTypeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ersReimbursementType = new ErsReimbursementType(rs.getInt(1), rs.getString(2));
            }
        }
        catch (SQLException e) {
            logger.error(e);
        }
        return ersReimbursementType;
    }
}
