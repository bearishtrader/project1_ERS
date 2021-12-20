package util;

import dao.*;
import models.ErsReimbursementStatus;
import models.ErsReimbursementType;
import models.ErsUserRoles;
import org.apache.log4j.Logger;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// Cache all lookup table values (user roles, reimbursement types and reimbursement status) on start-up in to Maps for quick lookup later
public class LookupTables {
    static Logger logger = Logger.getLogger(LookupTables.class);
    private static ErsUserRolesDao ersUserRolesDao = new ErsUserRolesDaoImpl();
    private static ErsReimbursementTypeDao ersReimbursementTypeDao = new ErsReimbursementTypeDaoImpl();
    private static ErsReimbursementStatusDao ersReimbursementStatusDao = new ErsReimbursementStatusDaoImpl();

    public static String getErsUserRole(Integer ersUsersRoleId) {
        return ersUserRolesM.get(ersUsersRoleId).getUserRole();
    }

    public static String getErsReimbursementType(Integer reimbTypeId) {
        return ersReimbursementTypeM.get(reimbTypeId).getReimbType();
    }

    public static String getErsReimbursementStatus(Integer reimbStatusId) {
        return ersReimbursementStatusM.get(reimbStatusId).getReimbStatus();
    }

    private static Map<Integer, ErsUserRoles> ersUserRolesM = null;
    private static Map<Integer, ErsReimbursementType> ersReimbursementTypeM = null;
    private static Map<Integer, ErsReimbursementStatus> ersReimbursementStatusM = null;
    public static void getLookupTables() {  // Make sure ConnectionUtil.getConnectionParams() has been called first
        List<ErsUserRoles> ersUserRolesL = ersUserRolesDao.getAllErsUserRoles();
        if (ersUserRolesL.size()>0) {
            ersUserRolesM = ersUserRolesL.stream().collect(Collectors.toMap(ErsUserRoles::getErsUserRoleId, ErsUserRoles -> ErsUserRoles));
            logger.info("ErsUserRoles code successfully retrieved from database.");
        } else {
            logger.error("Unable to retrieve ErsUserRoles lookup codes.");
        }
        List<ErsReimbursementType> ersReimbursementTypeL = ersReimbursementTypeDao.getAllErsReimbursementType();
        if (ersReimbursementTypeL.size()>0) {
            ersReimbursementTypeM = ersReimbursementTypeL.stream().collect(Collectors.toMap(ErsReimbursementType::getReimbTypeId, ErsReimbursementType -> ErsReimbursementType));
            logger.info("ErsReimbursementType code successfully retrieved from database.");
        } else {
            logger.error("Unable to retrieve ErsReimbursementType lookup codes.");
        }
        List<ErsReimbursementStatus> ersReimbursementStatusL = ersReimbursementStatusDao.getAllErsReimbursementStatus();
        if (ersReimbursementStatusL.size()>0) {
            ersReimbursementStatusM = ersReimbursementStatusL.stream().collect(Collectors.toMap(ErsReimbursementStatus::getReimbStatusId, ErsReimbursementStatus -> ErsReimbursementStatus));
            logger.info("ErsReimbursementStatus code successfully retrieved from database.");
        } else {
            logger.error("Unable to retrieve ErsReimbursementStatus lookup codes.");
        }
    }

    public static void getLookupTablesMockito() {   // Call this one before unit testing
        List<ErsUserRoles> ersUserRolesL = new ArrayList<>();
        ersUserRolesL.add(new ErsUserRoles(1, "Employee"));
        ersUserRolesL.add(new ErsUserRoles(2, "FinManager"));
        List<ErsReimbursementType> ersReimbursementTypeL = new ArrayList<>();
        ersReimbursementTypeL.add(new ErsReimbursementType(1, "LODGING"));
        ersReimbursementTypeL.add(new ErsReimbursementType(2, "TRAVEL"));
        ersReimbursementTypeL.add(new ErsReimbursementType(3, "FOOD"));
        ersReimbursementTypeL.add(new ErsReimbursementType(4, "OTHER"));
        List<ErsReimbursementStatus> ersReimbursementStatusL = new ArrayList<>();
        ersReimbursementStatusL.add(new ErsReimbursementStatus(1,"Pending"));
        ersReimbursementStatusL.add(new ErsReimbursementStatus(2,"Approved"));
        ersReimbursementStatusL.add(new ErsReimbursementStatus(3,"Denied"));
        ersUserRolesDao = Mockito.mock(ErsUserRolesDao.class);
        ersReimbursementTypeDao = Mockito.mock(ErsReimbursementTypeDao.class);
        ersReimbursementStatusDao = Mockito.mock(ErsReimbursementStatusDao.class);
        Mockito.when(ersUserRolesDao.getAllErsUserRoles()).thenReturn(ersUserRolesL);
        Mockito.when(ersReimbursementTypeDao.getAllErsReimbursementType()).thenReturn(ersReimbursementTypeL);
        Mockito.when(ersReimbursementStatusDao.getAllErsReimbursementStatus()).thenReturn(ersReimbursementStatusL);
        getLookupTables();  // Now we can call the normal method to populate our lookup Maps after the DAO objects/calls have been mocked
    }
}
