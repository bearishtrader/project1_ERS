package frontcontroller;

import dto.ReimbStatusDTO;
import io.javalin.http.Context;
import models.ErsReimbursement;
import models.ErsUsers;
import models.JsonResponse;
import org.apache.log4j.Logger;
import services.ErsReimbursementService;
import services.ErsUsersService;
import util.LookupTables;

public class ReimbursementController {
    static ErsReimbursementService ersReimbursementService = new ErsReimbursementService();
    static Logger logger = Logger.getLogger(ReimbursementController.class);

    public static void getAllErsReimbursements(Context context) {
        // Check the session to make sure we are authorized to use this API
        ErsUsers ersUserFromDao = context.sessionAttribute("user-session");
        JsonResponse jsonResponse = new JsonResponse(false, 400, "", null);
        if (ersReimbursementService.getAllErsReimbursements(ersUserFromDao, jsonResponse)) {
            logger.info(jsonResponse);
        } else {
            logger.error(jsonResponse);
        }
        context.status(jsonResponse.getStatus());
        context.json(jsonResponse);
    }

    public static void createErsReimbursement(Context context) {
        // Check the session to make sure we are authorized to use this API
        ErsUsers ersUserFromDao = context.sessionAttribute("user-session");
        ErsReimbursement ersReimbursement = (ErsReimbursement)context.bodyAsClass(ErsReimbursement.class);
        JsonResponse jsonResponse = new JsonResponse();
        if (ersReimbursementService.createErsReimbursement(ersUserFromDao, ersReimbursement, jsonResponse)) {
            logger.info(jsonResponse);
        } else {
            logger.error(jsonResponse);
        }
        context.status(jsonResponse.getStatus());
        context.json(jsonResponse);
    }

    public static void deleteErsReimbursement(Context context) {
        ErsUsers ersUserFromDao = context.sessionAttribute("user-session");
        Integer reimbId = Integer.parseInt(context.pathParam("reimb_id"));
        JsonResponse jsonResponse = new JsonResponse();
        if (ersReimbursementService.deleteErsReimbursement(ersUserFromDao, reimbId, jsonResponse)) {
            logger.info(jsonResponse);
        } else {
            logger.error(jsonResponse);
        }
        context.status(jsonResponse.getStatus());
        context.json(jsonResponse);
    }

    public static void approveOrDenyErsReimbursement(Context context) {
        ErsUsers ersUserFromDao = context.sessionAttribute("user-session");
        Integer reimbId = Integer.parseInt(context.pathParam("reimb_id"));
        ReimbStatusDTO reimbStatusDTO = (ReimbStatusDTO)context.bodyAsClass(ReimbStatusDTO.class);
        reimbStatusDTO.setReimbId(reimbId);
        JsonResponse jsonResponse = new JsonResponse();
        if (ersReimbursementService.updateErsReimbursement(ersUserFromDao, reimbStatusDTO, jsonResponse)) {
            logger.info(jsonResponse);
        } else {
            logger.error(jsonResponse);
        }
        context.status(jsonResponse.getStatus());
        context.json(jsonResponse);
    }
}
