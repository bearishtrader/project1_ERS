package dao;

import models.ErsReimbursementType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConnectionUtil;
import util.H2Util;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErsReimbursementTypeDaoImplIT {

    ErsReimbursementTypeDao ersReimbursementTypeDao;

    public ErsReimbursementTypeDaoImplIT(){
        //ConnectionUtil.getConnectionParams();
        ConnectionUtil.setConnectionParams(H2Util.url, H2Util.username, H2Util.password);// comment out to test real database
        ersReimbursementTypeDao = new ErsReimbursementTypeDaoImpl();
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
    void getAllErsReimbursementTypeIT() {
        List<ErsReimbursementType> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsReimbursementType(1, "LODGING"));
        expectedResult.add(new ErsReimbursementType(2, "TRAVEL"));
        expectedResult.add(new ErsReimbursementType(3, "FOOD"));
        expectedResult.add(new ErsReimbursementType(4, "OTHER"));
        List<ErsReimbursementType> actualResult = ersReimbursementTypeDao.getAllErsReimbursementType();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getOneErsReimbursementTypeIT() {
        ErsReimbursementType expectedResult = new ErsReimbursementType(3, "FOOD");
        ErsReimbursementType actualResult = ersReimbursementTypeDao.getOneErsReimbursementType(3);
        assertEquals(expectedResult, actualResult);
    }
}