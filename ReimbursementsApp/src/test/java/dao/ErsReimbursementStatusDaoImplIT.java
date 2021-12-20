package dao;

import models.ErsReimbursementStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConnectionUtil;
import util.H2Util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErsReimbursementStatusDaoImplIT {

    ErsReimbursementStatusDao ersReimbursementStatusDao;
    public ErsReimbursementStatusDaoImplIT() {
        //ConnectionUtil.getConnectionParams();
        ConnectionUtil.setConnectionParams(H2Util.url, H2Util.username, H2Util.password);// comment out to test real database
        ersReimbursementStatusDao = new ErsReimbursementStatusDaoImpl();
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
    void getAllErsReimbursementStatusIT() {
        List<ErsReimbursementStatus> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsReimbursementStatus(1, "Pending"));
        expectedResult.add(new ErsReimbursementStatus(2, "Approved"));
        expectedResult.add(new ErsReimbursementStatus(3, "Denied"));
        List<ErsReimbursementStatus> actualResult = ersReimbursementStatusDao.getAllErsReimbursementStatus();
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getOneErsReimbursementStatusIT() {
        ErsReimbursementStatus expectedResult = new ErsReimbursementStatus(2, "Approved");
        ErsReimbursementStatus actualResult = ersReimbursementStatusDao.getOneErsReimbursementStatus(2);
        assertEquals(expectedResult, actualResult);
    }
}