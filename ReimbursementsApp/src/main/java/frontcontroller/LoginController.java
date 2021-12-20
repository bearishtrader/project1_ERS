package frontcontroller;

import dto.UserDTO;
import io.javalin.http.Context;
import models.ErsUsers;
import models.JsonResponse;
import org.apache.log4j.Logger;
import services.ErsUsersService;
import util.LookupTables;

public class LoginController {
    static ErsUsersService ersUsersService = new ErsUsersService();
    static Logger logger = Logger.getLogger(LoginController.class);

    public static void login(Context context) {
        ErsUsers ersUser = context.bodyAsClass(ErsUsers.class);
        JsonResponse jsonResponse = new JsonResponse();
        ErsUsers ersUserFromDao = ersUsersService.getOneErsUser(ersUser, jsonResponse);
        if (ersUserFromDao != null) {   // It'll be null if not found including matching password
            context.sessionAttribute("user-session", ersUserFromDao);   // This is the successful case
            context.status(jsonResponse.getStatus());
            logger.info(jsonResponse);
            context.json(jsonResponse);
        } else {
            logger.error(jsonResponse);
            context.status(jsonResponse.getStatus());
            context.json(jsonResponse);
        }
    }

    public static void checkSession(Context context) {
        ErsUsers ersUserFromDao = context.sessionAttribute("user-session"); // We stored the user as retrieve from our DAO which tracks the session
        if (ersUserFromDao == null) {
            JsonResponse jsonResponse = new JsonResponse(false, 400, "No session found.", null);
            context.status(400);
            logger.error(jsonResponse);
            context.json(jsonResponse);
        } else {
            JsonResponse jsonResponse = new JsonResponse(true, 200, "Session found.",
                    new UserDTO(ersUserFromDao.getErsUsersId(), ersUserFromDao.getErsUsername(), ersUserFromDao.getUserEmail(), LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())));
            logger.info(jsonResponse);
            context.status(200);
            context.json(jsonResponse);
        }
    }

    public static void logout(Context context) {
        ErsUsers ersUserFromDao = context.sessionAttribute("user-session");
        context.sessionAttribute("user-session", null);
        JsonResponse jsonResponse = new JsonResponse(true, 200,
                ((ersUserFromDao==null) ? "User was already logged out of session." : ersUserFromDao.getErsUsername()+" ("+ersUserFromDao.getUserEmail()+") successfully logged out."),
                ((ersUserFromDao==null) ? null : new UserDTO(ersUserFromDao.getErsUsersId(), ersUserFromDao.getErsUsername(), ersUserFromDao.getUserEmail(), LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId()))));
        logger.info(jsonResponse);
        context.status(200);
        context.json(jsonResponse);
    }
}