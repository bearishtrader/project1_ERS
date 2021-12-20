package dao;

import models.ErsUsers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.ConnectionUtil;
import util.H2Util;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class ErsUsersDaoImplIT {
    ErsUsersDao ersUsersDao;

    public ErsUsersDaoImplIT() {
        //ConnectionUtil.getConnectionParams();//uncomment to test real database
        ConnectionUtil.setConnectionParams(H2Util.url, H2Util.username, H2Util.password);// comment out to test real database
        ersUsersDao = new ErsUsersDaoImpl();
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
    void createErsUserIT_ErsUsersId_Is_Null() {
        List<ErsUsers> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersDao.createErsUser(expectedResult.get(0));
        ersUsersDao.createErsUser(expectedResult.get(1));
        expectedResult.get(0).setErsUsersId(1); // we passed in null for ersUsersId but it will be populated as 1 by the database
        expectedResult.get(1).setErsUsersId(2); // etc
        List<ErsUsers> actualResult = ersUsersDao.getAllErsUsers();
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void createErsUserIT_ersUsersId_Is_Not_Null() {
        List<ErsUsers> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUsers(4, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        expectedResult.add(new ErsUsers(5, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersDao.createErsUser(expectedResult.get(0));
        ersUsersDao.createErsUser(expectedResult.get(1));
        List<ErsUsers> actualResult = ersUsersDao.getAllErsUsers();
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getAllErsUsersIT() {
        List<ErsUsers> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        expectedResult.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(expectedResult.get(0));
        ersUsersDao.createErsUser(expectedResult.get(1));
        ersUsersDao.createErsUser(expectedResult.get(2));
        ersUsersDao.createErsUser(expectedResult.get(3));
        expectedResult.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        expectedResult.get(1).setErsUsersId(2); // etc
        expectedResult.get(2).setErsUsersId(3); // ..
        expectedResult.get(3).setErsUsersId(4); // ..
        List<ErsUsers> actualResult = ersUsersDao.getAllErsUsers();
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getAllErsUsersIT_NoUsers() {
        List<ErsUsers> expectedResult = new ArrayList<>();  // No users exist
        List<ErsUsers> actualResult = ersUsersDao.getAllErsUsers();
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getOneErsUserIT() {
        List<ErsUsers> ersUsersList = new ArrayList<>();
        ersUsersList.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersList.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(ersUsersList.get(0));
        ersUsersDao.createErsUser(ersUsersList.get(1));
        ersUsersDao.createErsUser(ersUsersList.get(2));
        ersUsersDao.createErsUser(ersUsersList.get(3));
        ersUsersList.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        ersUsersList.get(1).setErsUsersId(2); // etc
        ersUsersList.get(2).setErsUsersId(3); // ..
        ersUsersList.get(3).setErsUsersId(4); // ..
        ErsUsers expectedResult = ersUsersList.get(2);  // ers_users_id = 3
        ErsUsers actualResult = ersUsersDao.getOneErsUser(3);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getOneErsUserIT_By_ersUsername_userEmail() {
        List<ErsUsers> ersUsersList = new ArrayList<>();
        ersUsersList.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersList.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(ersUsersList.get(0));
        ersUsersDao.createErsUser(ersUsersList.get(1));
        ersUsersDao.createErsUser(ersUsersList.get(2));
        ersUsersDao.createErsUser(ersUsersList.get(3));
        ersUsersList.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        ersUsersList.get(1).setErsUsersId(2); // etc
        ersUsersList.get(2).setErsUsersId(3); // ..
        ersUsersList.get(3).setErsUsersId(4); // ..
        ErsUsers expectedResult = ersUsersList.get(2);  // ers_users_id = 3
        ErsUsers actualResult = ersUsersDao.getOneErsUser("madams", "marge@javadev.com");
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getOneErsUserIT_NotExist() {
        List<ErsUsers> ersUsersList = new ArrayList<>();
        ersUsersList.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersList.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(ersUsersList.get(0));
        ersUsersDao.createErsUser(ersUsersList.get(1));
        ersUsersDao.createErsUser(ersUsersList.get(2));
        ersUsersDao.createErsUser(ersUsersList.get(3));
        ersUsersList.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        ersUsersList.get(1).setErsUsersId(2); // etc
        ersUsersList.get(2).setErsUsersId(3); // ..
        ersUsersList.get(3).setErsUsersId(4); // ..
        ErsUsers actualResult = ersUsersDao.getOneErsUser(5);   // non-existent ers_users_id
        assertNull(actualResult);
    }

    @Test
    void updateErsUserIT() {
        List<ErsUsers> ersUsersList = new ArrayList<>();
        ersUsersList.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersList.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(ersUsersList.get(0));
        ersUsersDao.createErsUser(ersUsersList.get(1));
        ersUsersDao.createErsUser(ersUsersList.get(2));
        ersUsersDao.createErsUser(ersUsersList.get(3));
        ErsUsers expectedResult = ersUsersList.get(1);  // ers_users_id = 2 Sally Jones will become a regular employee user_role_id 2 -> 1
        expectedResult.setErsUsersId(2);    // we passed in null but the serial will set this to 2 based on our integration test setup
        expectedResult.setErsUsername("sjones2");
        expectedResult.setErsPassword("password2");
        expectedResult.setUserFirstName("Sally2");
        expectedResult.setUserLastName("Jones2");
        expectedResult.setUserEmail("sjboss_demoted@javadev.com");
        expectedResult.setUserRoleId(1);
        ErsUsers actualResult = (ersUsersDao.updateErsUser(expectedResult)==true) ? ersUsersDao.getOneErsUser(2): null;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void updateErsUserIT_User_Not_Exist() {
        List<ErsUsers> ersUsersList = new ArrayList<>();
        ersUsersList.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        ersUsersList.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        ersUsersList.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(ersUsersList.get(0));
        ersUsersDao.createErsUser(ersUsersList.get(1));
        ersUsersDao.createErsUser(ersUsersList.get(2));
        ersUsersDao.createErsUser(ersUsersList.get(3));
        ersUsersList.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        ersUsersList.get(1).setErsUsersId(2); // etc
        ersUsersList.get(2).setErsUsersId(3); // ..
        ersUsersList.get(3).setErsUsersId(4); // ..
        List<ErsUsers> expectedResult = ersUsersList;
        ErsUsers nonExistentUser = new ErsUsers();
        nonExistentUser.setErsUsersId(5);   // On purpose set to non-existent user ers_users_id = 5
        nonExistentUser.setErsUsername("sjones2");
        nonExistentUser.setErsPassword("password2");
        nonExistentUser.setUserFirstName("Sally2");
        nonExistentUser.setUserLastName("Jones2");
        nonExistentUser.setUserEmail("sjboss_demoted@javadev.com");
        nonExistentUser.setUserRoleId(1);
        List<ErsUsers> actualResult = new ArrayList<>();
        if (ersUsersDao.updateErsUser(nonExistentUser) == false) {  // ersUsersId = 5 is non-existent
            actualResult = ersUsersDao.getAllErsUsers();
        }
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void deleteErsUserIT() {
        List<ErsUsers> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        expectedResult.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(expectedResult.get(0));
        ersUsersDao.createErsUser(expectedResult.get(1));
        ersUsersDao.createErsUser(expectedResult.get(2));   // ers_users_id == 3 will be deleted Marge Simpson
        ersUsersDao.createErsUser(expectedResult.get(3));
        expectedResult.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        expectedResult.get(1).setErsUsersId(2); // etc
        expectedResult.get(2).setErsUsersId(3); // ..
        expectedResult.get(3).setErsUsersId(4); // ..
        expectedResult.remove(2); // remove Marge Simpson (ers_users_id == 3) from list
        List<ErsUsers> actualResult = new ArrayList<>();
        if ( ersUsersDao.deleteErsUser(3)) {    // delete Marge Simpson (ers_users_id == 3) from database
            actualResult = ersUsersDao.getAllErsUsers();
        }
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void deleteErsUserIT_User_Not_Exist() {
        List<ErsUsers> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        expectedResult.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(expectedResult.get(0));
        ersUsersDao.createErsUser(expectedResult.get(1));
        ersUsersDao.createErsUser(expectedResult.get(2));
        ersUsersDao.createErsUser(expectedResult.get(3));
        expectedResult.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        expectedResult.get(1).setErsUsersId(2); // etc
        expectedResult.get(2).setErsUsersId(3); // ..
        expectedResult.get(3).setErsUsersId(4); // ..
        List<ErsUsers> actualResult = new ArrayList<>();
        if ( ersUsersDao.deleteErsUser(5) == false) {    // ers_users_id == 5 doesn't exist
            actualResult = ersUsersDao.getAllErsUsers();
        }
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }

    @Test
    void getCertainErsUsers() {
        List<ErsUsers> expectedResult = new ArrayList<>();
        expectedResult.add(new ErsUsers(null, "jsmith", "1234", "John", "Smith", "jsmith@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "sjones", "4321AC", "Sally", "Jones", "sjboss@javadev.com", 2));
        expectedResult.add(new ErsUsers(null, "madams", "32112", "Marge", "Simpson", "marge@javadev.com", 1));
        expectedResult.add(new ErsUsers(null, "fwilson", "fred123", "Fred", "Wilson", "freddev@javadev.com", 1));
        ersUsersDao.createErsUser(expectedResult.get(0));
        ersUsersDao.createErsUser(expectedResult.get(1));
        ersUsersDao.createErsUser(expectedResult.get(2));
        ersUsersDao.createErsUser(expectedResult.get(3));
        expectedResult.get(0).setErsUsersId(1); // null means DEFAULT serial ers_users_id which is 1
        expectedResult.get(1).setErsUsersId(2); // etc
        expectedResult.get(2).setErsUsersId(3); // ..
        expectedResult.get(3).setErsUsersId(4); // ..
        expectedResult.remove(2); // remove the ersUsers object where ersUsersId==3
        expectedResult.remove(0); // remove the ersUsers object where ersUsersId==1
        Set<Integer> ersUsersIdSet = new TreeSet<>();
        ersUsersIdSet.add(2);
        ersUsersIdSet.add(4);
        List<ErsUsers> actualResult = ersUsersDao.getCertainErsUsers(ersUsersIdSet);
        assertArrayEquals(expectedResult.toArray(), actualResult.toArray());
    }
}