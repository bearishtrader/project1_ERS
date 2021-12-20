package dao;

import models.ErsReimbursementType;

import java.util.List;

public interface ErsReimbursementTypeDao {
    List<ErsReimbursementType> getAllErsReimbursementType();
    ErsReimbursementType getOneErsReimbursementType(Integer reimbTypeId);
    // No write operations needed for lookup tables
}
