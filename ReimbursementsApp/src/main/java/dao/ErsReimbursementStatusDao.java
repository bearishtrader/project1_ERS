package dao;

import models.ErsReimbursementStatus;

import java.util.List;

public interface ErsReimbursementStatusDao {
    List<ErsReimbursementStatus> getAllErsReimbursementStatus();
    ErsReimbursementStatus getOneErsReimbursementStatus(Integer reimbStatusId);
    // No write operations needed for lookup tables
}
