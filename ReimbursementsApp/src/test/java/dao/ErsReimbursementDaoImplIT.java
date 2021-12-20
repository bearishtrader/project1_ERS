package dao;

import dto.ReimbStatusDTO;
import models.ErsReimbursement;
import models.ErsUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConnectionUtil;
import util.H2Util;
import util.ReceiptFileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErsReimbursementDaoImplIT {
    ErsUsersDao ersUsersDao;
    ErsReimbursementDao ersReimbursementDao;


    public ErsReimbursementDaoImplIT () {
        //ConnectionUtil.getConnectionParams();
        ConnectionUtil.setConnectionParams(H2Util.url, H2Util.username, H2Util.password);// comment out to test real database
        ersUsersDao = new ErsUsersDaoImpl();
        ersReimbursementDao = new ErsReimbursementDaoImpl();
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
    void createErsReimbursementIT() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = ReceiptFileIO.getReceiptFileBytes("src/main/resources/test_receipt.gif");
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/2,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) &&
            ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
        }

        List<ErsReimbursement> actualResult = ersReimbursementDao.getAllErsReimbursementsByAuthor(1);
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getAllErsReimbursements() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Bossman", "jboss@javadev.com", 2));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Erhart", "jerhart@javadev.com", 1));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) &&
                ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1)) &&
                ersReimbursementDao.createErsReimbursement(ersReimbursements.get(2)) &&
                ersReimbursementDao.createErsReimbursement(ersReimbursements.get(3))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
            expectedResult.get(2).setReimbId(3); // ..
            expectedResult.get(3).setReimbId(4); // ..
        }
        List<ErsReimbursement> actualResult = ersReimbursementDao.getAllErsReimbursements();
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getAllErsReimbursementsByAuthorIT() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Bossman", "jboss@javadev.com", 2));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Erhart", "jerhart@javadev.com", 1));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) &&
            ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1)) &&
            ersReimbursementDao.createErsReimbursement(ersReimbursements.get(2)) &&
            ersReimbursementDao.createErsReimbursement(ersReimbursements.get(3))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
            expectedResult.get(2).setReimbId(3); // ..
            expectedResult.get(3).setReimbId(4); // ..
            expectedResult.remove(0);   // Remove both list elements where the reimbursement author id is 1 so we keep only those which are 2 to compare
            expectedResult.remove(0);
        }
        List<ErsReimbursement> actualResult = ersReimbursementDao.getAllErsReimbursementsByAuthor(2);
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getAllErsReimbursementsByResolverIT() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Bossman", "jboss@javadev.com", 2));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Erhart", "jerhart@javadev.com", 1));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) &&
                ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1)) &&
                ersReimbursementDao.createErsReimbursement(ersReimbursements.get(2)) &&
                ersReimbursementDao.createErsReimbursement(ersReimbursements.get(3))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
            expectedResult.get(2).setReimbId(3); // ..
            expectedResult.get(3).setReimbId(4); // ..
            Iterator it = expectedResult.iterator();
            while (it.hasNext()) {
                ErsReimbursement ersReimbursement = (ErsReimbursement) it.next();
                if (ersReimbursement.getReimbResolver()==null) it.remove();
            }
        }
        List<ErsReimbursement> actualResult = ersReimbursementDao.getAllErsReimbursementsByResolver(1);
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getOneErsReimbursementIT() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = ReceiptFileIO.getReceiptFileBytes("src/main/resources/test_receipt.gif");
        ersReimbursements.add(new ErsReimbursement(null,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/2,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ErsReimbursement expectedResult = null;
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) && ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1))) {
            ersReimbursements.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            ersReimbursements.get(1).setReimbId(2); // etc
            expectedResult = ersReimbursements.get(1);
        }

        ErsReimbursement actualResult = ersReimbursementDao.getOneErsReimbursement(2);
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void updateErsReimbursementIT() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = ReceiptFileIO.getReceiptFileBytes("src/main/resources/test_receipt2.gif");
        ersReimbursements.add(new ErsReimbursement(null,19.95, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,29.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/2,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) && ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
            expectedResult.get(0).setReimbResolved(Timestamp.valueOf("2021-12-11 15:30:00"));
            expectedResult.get(0).setReimbResolver(2);
            expectedResult.get(0).setReimbStatusId(2);
        }
        List<ErsReimbursement> actualResult = new ArrayList<>();
        if (ersReimbursementDao.updateErsReimbursement(expectedResult.get(0))) {
            actualResult.add(ersReimbursementDao.getOneErsReimbursement(1));    // We are checking both records to make sure the update did not hit both
            actualResult.add(ersReimbursementDao.getOneErsReimbursement(2));
        }
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void updateErsReimbursementIT_PATCH_ReimbStatusDTO() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // we're not testing this for this test
        ersReimbursements.add(new ErsReimbursement(null,19.95, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,29.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/2,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) && ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
            expectedResult.get(0).setReimbResolved(Timestamp.valueOf("2021-12-11 15:30:00"));
            expectedResult.get(0).setReimbResolver(2);
            expectedResult.get(0).setReimbStatusId(2);
        }
        List<ErsReimbursement> actualResult = new ArrayList<>();
        ReimbStatusDTO reimbStatusDTO = new ReimbStatusDTO(expectedResult.get(0).getReimbId(), expectedResult.get(0).getReimbResolved(),
                expectedResult.get(0).getReimbResolver(), expectedResult.get(0).getReimbStatusId());
        if (ersReimbursementDao.updateErsReimbursement(reimbStatusDTO)) {
            actualResult.add(ersReimbursementDao.getOneErsReimbursement(1));    // We are checking both records to make sure the update did not hit both
            actualResult.add(ersReimbursementDao.getOneErsReimbursement(2));
        }
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void deleteErsReimbursementIT() {
        ersUsersDao.createErsUser(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersDao.createErsUser(new ErsUsers(null, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // not testing the receipt image
        ersReimbursements.add(new ErsReimbursement(null,19.95, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(null,29.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/2,
                /*reimb_status (Denied)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        List<ErsReimbursement> expectedResult = new ArrayList<>();
        if (ersReimbursementDao.createErsReimbursement(ersReimbursements.get(0)) && ersReimbursementDao.createErsReimbursement(ersReimbursements.get(1))) {
            expectedResult = ersReimbursements;
            expectedResult.get(0).setReimbId(1); // passed in null serial will make it 1 in database so make it 1 here
            expectedResult.get(1).setReimbId(2); // etc
            expectedResult.remove(1);   // Remove reimb_id = 2, or the Denied status=3 reimbursement from our expected list
        }
        List<ErsReimbursement> actualResult = new ArrayList<>();
        if (ersReimbursementDao.deleteErsReimbursement(2)) {
            actualResult = ersReimbursementDao.getAllErsReimbursementsByAuthor(1);  // Should only be one instead of original 2
        }
        //System.out.println("expectedResult.get(0)="+expectedResult.get(0));
        //System.out.println("actualResult.get(0)="+actualResult.get(0));
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }
}