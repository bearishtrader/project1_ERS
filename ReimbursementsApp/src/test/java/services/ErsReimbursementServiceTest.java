package services;

import dao.ErsReimbursementDao;
import dao.ErsUsersDao;
import dto.ReimbStatusDTO;
import dto.ReimbursementDTO;
import models.ErsReimbursement;
import models.ErsUsers;
import models.JsonResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.LookupTables;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ErsReimbursementServiceTest {

    ErsReimbursementDao ersReimbursementDao = Mockito.mock(ErsReimbursementDao.class);
    ErsUsersDao ersUsersDao = Mockito.mock(ErsUsersDao.class);
    ErsReimbursementService ersReimbursementService;
    public ErsReimbursementServiceTest() {
        this.ersReimbursementService = new ErsReimbursementService(ersReimbursementDao, ersUsersDao);
    }

    @Test
    public void getAllErsReimbursementsTest_FinManager() {
        List<ErsUsers> ersUsersL = new ArrayList<>();
        ersUsersL.add(new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersL.add(new ErsUsers(2, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ersReimbursements.add(new ErsReimbursement(3,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(4,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        List<ReimbursementDTO> expectedResult = new ArrayList<>();
        expectedResult.add(new ReimbursementDTO(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3,
                "John", "Smith", "jsmith@javadev.com", null, null, null));
        expectedResult.add(new ReimbursementDTO(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2,
                "John", "Smith", "jsmith@javadev.com", "John", "Smith", "jsmith@javadev.com"));
        expectedResult.add(new ReimbursementDTO(3,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3,
                "Jane", "Bosshart", "jboss@javadev.com", null, null, null));
        expectedResult.add(new ReimbursementDTO(4,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2,
                "Jane", "Bosshart", "jboss@javadev.com", "John", "Smith", "jsmith@javadev.com"));

        JsonResponse jsonResponse = new JsonResponse();
        LookupTables.getLookupTablesMockito();
        Mockito.when(ersReimbursementDao.getAllErsReimbursements()).thenReturn(ersReimbursements);
        Set<Integer> ersUserIds = new TreeSet<>();
        ersUserIds.add(1);  // jsmith
        ersUserIds.add(2);  // jboss
        List<ErsUsers> certainErsUsers = new ArrayList<>();
        certainErsUsers.add(ersUsersL.get(0));
        certainErsUsers.add(ersUsersL.get(1));
        Mockito.when(ersUsersDao.getCertainErsUsers(ersUserIds)).thenReturn(certainErsUsers);

        List<ReimbursementDTO> actualResult = new ArrayList<>();
        if ( ersReimbursementService.getAllErsReimbursements(ersUsersL.get(1), jsonResponse) ) {
            actualResult = (List<ReimbursementDTO>) jsonResponse.getData();
        }
        //System.out.println(jsonResponse);
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    public void getAllErsReimbursementsTest_Employee() {
        List<ErsUsers> ersUsersL = new ArrayList<>();
        ersUsersL.add(new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersL.add(new ErsUsers(2, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        List<ReimbursementDTO> expectedResult = new ArrayList<>();
        expectedResult.add(new ReimbursementDTO(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3,
                "John", "Smith", "jsmith@javadev.com", null, null, null));
        expectedResult.add(new ReimbursementDTO(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2,
                "John", "Smith", "jsmith@javadev.com", "John", "Smith", "jsmith@javadev.com"));

        JsonResponse jsonResponse = new JsonResponse();
        LookupTables.getLookupTablesMockito();
        ErsUsers ersUserFromDao = ersUsersL.get(0);
        Mockito.when(ersReimbursementDao.getAllErsReimbursementsByAuthor(ersUserFromDao.getErsUsersId())).thenReturn(ersReimbursements);
        Set<Integer> ersUserIds = new TreeSet<>();
        ersUserIds.add(1);  // jsmith
        List<ErsUsers> certainErsUsers = new ArrayList<>();
        certainErsUsers.add(ersUsersL.get(0));
        certainErsUsers.add(ersUsersL.get(1));
        Mockito.when(ersUsersDao.getCertainErsUsers(ersUserIds)).thenReturn(certainErsUsers);

        List<ReimbursementDTO> actualResult = new ArrayList<>();
        if ( ersReimbursementService.getAllErsReimbursements(ersUsersL.get(0), jsonResponse) ) {
            actualResult = (List<ReimbursementDTO>) jsonResponse.getData();
        }
        //System.out.println(jsonResponse);
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    public void createErsReimbursementTest_Employee() {
        List<ErsUsers> ersUsersL = new ArrayList<>();
        ersUsersL.add(new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        JsonResponse jsonResponse = new JsonResponse();
        LookupTables.getLookupTablesMockito();
        byte[] byteA = null;
        ErsReimbursement ersReimbursement = new ErsReimbursement(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3);
        Mockito.when(ersReimbursementDao.createErsReimbursement(ersReimbursement)).thenReturn(true);
        Boolean actualResult = ersReimbursementService.createErsReimbursement(ersUsersL.get(0), ersReimbursement, jsonResponse);
        assertTrue(actualResult);
    }

    @Test
    public void createErsReimbursementTest_FinManager() {  // Finance managers are not allowed to create expense reimbursements wrong role
        JsonResponse jsonResponse = new JsonResponse();
        LookupTables.getLookupTablesMockito();
        ErsUsers ersUserFinManager = new ErsUsers(2, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2);
        byte[] byteA = null;
        ErsReimbursement ersReimbursement = new ErsReimbursement(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3);
        Mockito.when(ersReimbursementDao.createErsReimbursement(ersReimbursement)).thenReturn(true);
        Boolean actualResult = ersReimbursementService.createErsReimbursement(ersUserFinManager, ersReimbursement, jsonResponse);
        assertFalse(actualResult);
    }

    @Test
    public void deleteErsReimbursementTest() {
        ErsUsers ersUserFromDao = new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1);
        Integer reimbId = 1;
        LookupTables.getLookupTablesMockito();
        JsonResponse jsonResponse = new JsonResponse();
        Mockito.when(ersReimbursementDao.deleteErsReimbursement(reimbId)).thenReturn(true);
        Boolean actualResult = ersReimbursementService.deleteErsReimbursement(ersUserFromDao, reimbId, jsonResponse);
        assertTrue(actualResult);
    }
    // Unit test Map user's ersUsersId to the actual ErsUsers model to populate our ReimbursementsDTO with firstName, lastName, e-mail
    @Test
    public void getErsUsersMapFromReimbursementsTest() {
        List<ErsUsers> ersUsers = new ArrayList<>();
        ersUsers.add(new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsers.add(new ErsUsers(2, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        Set<Integer> ersUserIds = new TreeSet<>();
        ersUserIds.add(1);
        ersUserIds.add(2);
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ersReimbursements.add(new ErsReimbursement(3,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(4,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        Map<Integer, ErsUsers> expectedResult = new HashMap<Integer, ErsUsers>();
        expectedResult.put(1, ersUsers.get(0));
        expectedResult.put(2, ersUsers.get(1));
        Mockito.when(ersUsersDao.getCertainErsUsers(ersUserIds)).thenReturn(ersUsers);
        Map<Integer, ErsUsers> actualResult = ersReimbursementService.getErsUsersMapFromReimbursements(ersReimbursements);
        //System.out.println(expectedResult.values());
        //System.out.println(actualResult.values());
        assertArrayEquals(expectedResult.values().toArray(), actualResult.values().toArray());
    }
    // The Map from previous test is used to lookup ErsUsers model info firstName, lastName, email to populate our ReimbursementsDTO which has this additional display
    // info added to the model used for the DAO, or ErsReimbursements
    @Test
    public void getReimbursementDTOListTest() {
        List<ErsUsers> ersUsersL = new ArrayList<>();
        ersUsersL.add(new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersL.add(new ErsUsers(2, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2));
        List<ErsReimbursement> ersReimbursements = new ArrayList<>();
        // Grab our test receipt image to put in the database
        byte[] byteA = null;    // Not testing the receipt image here
        ersReimbursements.add(new ErsReimbursement(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2));
        ersReimbursements.add(new ErsReimbursement(3,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3));
        ersReimbursements.add(new ErsReimbursement(4,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2));
        List<ReimbursementDTO> expectedResult = new ArrayList<>();
        expectedResult.add(new ReimbursementDTO(1,50.25, Timestamp.valueOf("2021-12-01 15:30:00"), null,
                "Burger King", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3,
                "John", "Smith", "jsmith@javadev.com", null, null, null));
        expectedResult.add(new ReimbursementDTO(2,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Mobile", /*receipt img file byte array=*/byteA, /*reimb_author=*/1, /*reimb_resolver=*/1,
                /*reimb_status (Approved)=*/2, /*reimb_type_id=2(TRAVEL)*/2,
                "John", "Smith", "jsmith@javadev.com", "John", "Smith", "jsmith@javadev.com"));
        expectedResult.add(new ReimbursementDTO(3,50.25, Timestamp.valueOf("2020-11-01 12:30:00"), null,
                "McDonalds", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/null,
                /*reimb_status (Pending)=*/1, /*reimb_type_id=3(FOOD)*/3,
                "Jane", "Bosshart", "jboss@javadev.com", null, null, null));
        expectedResult.add(new ReimbursementDTO(4,11.95, Timestamp.valueOf("2021-12-02 17:30:00"), Timestamp.valueOf("2021-12-05 17:30:00"),
                "Quick-Mart", /*receipt img file byte array=*/byteA, /*reimb_author=*/2, /*reimb_resolver=*/1,
                /*reimb_status (DENIED)=*/3, /*reimb_type_id=2(TRAVEL)*/2,
                "Jane", "Bosshart", "jboss@javadev.com", "John", "Smith", "jsmith@javadev.com"));
        LookupTables.getLookupTablesMockito();
        Set<Integer> ersUserIds = new TreeSet<>();
        ersUserIds.add(1);  // jsmith
        ersUserIds.add(2);  // jboss
        List<ErsUsers> certainErsUsers = new ArrayList<>();
        certainErsUsers.add(ersUsersL.get(0));  // jsmith
        certainErsUsers.add(ersUsersL.get(1));  // jboss
        Mockito.when(ersUsersDao.getCertainErsUsers(ersUserIds)).thenReturn(certainErsUsers);
        List<ReimbursementDTO> actualResult = ersReimbursementService.getReimbursementDTOList(ersReimbursements);
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    public void updateErsReimbursementTest() {
        ErsUsers ersUserFromDao = new ErsUsers(2, "jboss", "abc1234", "Jane", "Bosshart", "jboss@javadev.com", 2);
        Integer reimbId = 2;
        ReimbStatusDTO reimbStatusDTO = new ReimbStatusDTO(2, Timestamp.valueOf("2021-12-15 15:30:00"), 2/*jboss*/, 2/*Approved*/);
        reimbStatusDTO.setReimbId(reimbId);
        JsonResponse jsonResponse = new JsonResponse();
        LookupTables.getLookupTablesMockito();
        Mockito.when(ersReimbursementDao.updateErsReimbursement(reimbStatusDTO)).thenReturn(true);
        Boolean actualResult = ersReimbursementService.updateErsReimbursement(ersUserFromDao, reimbStatusDTO, jsonResponse);
        //System.out.println(jsonResponse);
        assertTrue(actualResult);
    }
}
