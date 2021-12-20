package dao;

import models.ErsUserRoles;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConnectionUtil;
import util.H2Util;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErsUserRolesDaoImplIT {

    ErsUserRolesDao ersUserRolesDao;
    public ErsUserRolesDaoImplIT() {
        //ConnectionUtil.getConnectionParams();
        ConnectionUtil.setConnectionParams(H2Util.url, H2Util.username, H2Util.password);// comment out to test real database
        ersUserRolesDao = new ErsUserRolesDaoImpl();
    }

    @BeforeEach
    void setUp() {
        // comment out to test real database
        H2Util.createErsUserRolesTable();
        H2Util.createErsUsersTable();
        H2Util.createErsReimbursementStatusTable();
        H2Util.createErsReimbursementTypeTable();
        H2Util.createErsReimbursementTable();
        H2Util.populateErsReimbursementStatusTable();
        H2Util.populateErsReimbursementTypeTable();
        H2Util.populateErsUserRolesTable();
    }

    @AfterEach
    void tearDown() {
        // comment out to test real database you will have to do cleanup yourself
        H2Util.dropErsReimbursementTable();
        H2Util.dropErsReimbursementTypeTable();
        H2Util.dropErsReimbursementStatusTable();
        H2Util.dropErsUsersTable();
        H2Util.dropErsUserRolesTable();
    }

    @Test
    void getAllErsUserRolesIT() {
        List<ErsUserRoles> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUserRoles(1, "Employee"));
        expectedResult.add(new ErsUserRoles(2, "FinManager"));
        List<ErsUserRoles> actualResult = ersUserRolesDao.getAllErsUserRoles();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getOneErsUserRoleIT() {
        ErsUserRoles expectedResult = new ErsUserRoles(2, "FinManager");
        ErsUserRoles actualResult = ersUserRolesDao.getOneErsUserRole(2);
        assertEquals(expectedResult, actualResult);
    }
}