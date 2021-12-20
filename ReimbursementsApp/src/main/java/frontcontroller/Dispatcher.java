package frontcontroller;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class Dispatcher {
    public Dispatcher(Javalin javalin) {
        javalin.routes(() -> {
            ApiBuilder.path("api", () -> {
                ApiBuilder.path("login", () -> {
                    ApiBuilder.post(LoginController::login);
                });
                ApiBuilder.path("check-session", () -> {
                    ApiBuilder.get(LoginController::checkSession);
                });
                ApiBuilder.path("logout", () -> {
                    ApiBuilder.delete(LoginController::logout);
                });
                ApiBuilder.path("reimbursements", () -> {
                    ApiBuilder.get(ReimbursementController::getAllErsReimbursements);
                    ApiBuilder.post(ReimbursementController::createErsReimbursement);
                    ApiBuilder.path("{reimb_id}", () -> {
                        // Employee can delete their own expense reimbursement request in case of their own error.  FinManager can't delete.
                        ApiBuilder.delete(ReimbursementController::deleteErsReimbursement);
                        ApiBuilder.patch(ReimbursementController::approveOrDenyErsReimbursement);   // Only FinManager can flip status to Approved or Denied
                    });
                });
            });
        });
    }
}
