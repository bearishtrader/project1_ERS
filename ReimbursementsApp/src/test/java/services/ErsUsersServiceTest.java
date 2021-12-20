package services;

import dao.ErsReimbursementStatusDao;
import dao.ErsReimbursementTypeDao;
import dao.ErsUserRolesDao;
import dao.ErsUsersDao;
import dto.UserDTO;
import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.LookupTables;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErsUsersServiceTest {

    ErsUsersDao ersUsersDao = Mockito.mock(ErsUsersDao.class);
    ErsUsersService ersUsersService;

    public ErsUsersServiceTest() {
        this.ersUsersService = new ErsUsersService(ersUsersDao);
    }

    @Test
    void getOneErsUserTest_Check_ErsUsers() {
        // LoginController::Login calls ersUsersDao.getOneErsUser(...) for login validation only 3 fields will be filled in for object, email, username and password
        ErsUsers ersUserFromLogin = new ErsUsers(null, "jsmith", "1234", null, null, "jsmith@javadev.com", null);
        JsonResponse jsonResponse = new JsonResponse();
        ErsUsers ersUserFromDao = new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1);
        Mockito.when(ersUsersDao.getOneErsUser(ersUserFromLogin.getErsUsername(), ersUserFromLogin.getUserEmail())).thenReturn(ersUserFromDao);
        // Note that our LookupTables utility class is being tested here from services with the mock-up of DAO objects
        LookupTables.getLookupTablesMockito();  // Had to make a special test-setup method in our utility class that stores the lookup table codes
        ErsUsers expectedResult = ersUserFromDao;
        ErsUsers actualResult = ersUsersService.getOneErsUser(ersUserFromLogin, jsonResponse);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getOneErsUserTest_Check_UserDTO() {  // same test as above but this time check that the UserDTO was populated correctly
        // LoginController::Login calls ersUsersDao.getOneErsUser(...) for login validation only 3 fields will be filled in for object, email, username and password
        ErsUsers ersUserFromLogin = new ErsUsers(null, "jsmith", "1234", null, null, "jsmith@javadev.com", null);
        JsonResponse jsonResponse = new JsonResponse();
        ErsUsers ersUserFromDao = new ErsUsers(1, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1);
        Mockito.when(ersUsersDao.getOneErsUser(ersUserFromLogin.getErsUsername(), ersUserFromLogin.getUserEmail())).thenReturn(ersUserFromDao);
        LookupTables.getLookupTablesMockito();  // Had to make a special test-setup method in our utility class that stores the lookup table codes
        ErsUsers expectedResult = ersUserFromDao;
        ErsUsers actualResult = ersUsersService.getOneErsUser(ersUserFromLogin, jsonResponse);
        UserDTO expectedDTO = new UserDTO(ersUserFromDao.getErsUsersId(), ersUserFromDao.getErsUsername(), ersUserFromDao.getUserEmail(), "Employee");
        UserDTO actualDTO = (UserDTO) jsonResponse.getData();
        assertEquals(expectedDTO, actualDTO);
    }
}