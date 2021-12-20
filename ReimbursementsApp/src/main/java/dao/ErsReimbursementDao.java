package dao;

import dto.ReimbStatusDTO;
import models.ErsReimbursement;

import java.util.List;

public interface ErsReimbursementDao {
    Boolean createErsReimbursement(ErsReimbursement ersReimbursement);
    List<ErsReimbursement> getAllErsReimbursements();
    List<ErsReimbursement> getAllErsReimbursementsByAuthor(Integer ersUsersId);
    List<ErsReimbursement> getAllErsReimbursementsByResolver(Integer ersUsersId);
    ErsReimbursement getOneErsReimbursement(Integer reimbId);
    Boolean updateErsReimbursement(ErsReimbursement ersReimbursement);
    Boolean updateErsReimbursement(ReimbStatusDTO reimbStatusDTO);
    Boolean deleteErsReimbursement(Integer reimbId);
}
