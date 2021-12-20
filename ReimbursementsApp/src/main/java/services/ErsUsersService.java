package services;

import dao.ErsUsersDao;
import dao.ErsUsersDaoImpl;
import dto.UserDTO;
import models.ErsUsers;
import models.JsonResponse;
import util.LookupTables;

public class ErsUsersService {
    ErsUsersDao ersUsersDao;
    public ErsUsersService() { this.ersUsersDao = new ErsUsersDaoImpl(); }
    public ErsUsersService(ErsUsersDao ersUsersDao) { this.ersUsersDao = ersUsersDao; }

    // login validation and set any error messages, send back user DTO (data transfer object)
    public ErsUsers getOneErsUser(ErsUsers ersUser, JsonResponse jsonResponse) {
        ErsUsers ersUserFromDao = null; // ersUserFromDao has info from the database, which is to be distinguished from ersUser which we got from login screen
        jsonResponse.setSuccessful(false);
        jsonResponse.setStatus(400);
        ersUserFromDao = ersUsersDao.getOneErsUser(ersUser.getErsUsername(), ersUser.getUserEmail());
        if (ersUserFromDao==null) {
            jsonResponse.setMessage("Login unsuccessful.  Username and/or e-mail not found.");
        } else if ( ersUserFromDao!= null && !(ersUser.getErsPassword().equals(ersUserFromDao.getErsPassword())) ) {
            ersUserFromDao = null;  // password didn't match so set to null
            jsonResponse.setMessage("Login unsuccessful.  Password incorrect.");
        } else { // everything is OK
            jsonResponse.setSuccessful(true);
            jsonResponse.setStatus(200);
            jsonResponse.setMessage("Login successful.");
            jsonResponse.setData(new UserDTO(ersUserFromDao.getErsUsersId(), ersUserFromDao.getErsUsername(), ersUserFromDao.getUserEmail(), LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())));
        }
        return ersUserFromDao;
    }
}
