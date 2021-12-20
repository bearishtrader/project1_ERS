package services;

import dao.ErsReimbursementDao;
import dao.ErsReimbursementDaoImpl;
import dao.ErsUsersDao;
import dao.ErsUsersDaoImpl;
import dto.ReimbStatusDTO;
import dto.ReimbursementDTO;
import models.ErsReimbursement;
import models.ErsUserRoles;
import models.ErsUsers;
import models.JsonResponse;
import util.LookupTables;

import java.util.*;
import java.util.stream.Collectors;

public class ErsReimbursementService {
    ErsReimbursementDao ersReimbursementDao;
    ErsUsersDao ersUsersDao;

    public ErsReimbursementService() {
        this.ersReimbursementDao = new ErsReimbursementDaoImpl();
        this.ersUsersDao = new ErsUsersDaoImpl();
    }
    public ErsReimbursementService(ErsReimbursementDao ersReimbursementDao, ErsUsersDao ersUsersDao) {
        this.ersReimbursementDao = ersReimbursementDao;
        this.ersUsersDao = ersUsersDao;
    }

    public Boolean getAllErsReimbursements(ErsUsers ersUserFromDao, JsonResponse jsonResponse) {
        Boolean success = false;
        if (ersUserFromDao == null) {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("No session to use API.  Try logout/login.");
            jsonResponse.setData(null);
        } else {    // We have a session the same endpoint will behave slightly different given the user role
            if ( (LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())).equals("FinManager")) {
                List<ErsReimbursement> ersReimbursements = ersReimbursementDao.getAllErsReimbursements();
                if (ersReimbursements.size() > 0) {
                    List<ReimbursementDTO> reimbursementDTOList = getReimbursementDTOList(ersReimbursements);
                    if (reimbursementDTOList.size() > 0) {
                        jsonResponse.setSuccessful(true);
                        jsonResponse.setStatus(200);
                        jsonResponse.setMessage("Successfully retrieved all reimbursements.");
                        jsonResponse.setData(reimbursementDTOList);
                        success = true;
                    } else {
                        jsonResponse.setSuccessful(false);
                        jsonResponse.setStatus(400);
                        jsonResponse.setMessage("No reimbursements found. (1)");
                        jsonResponse.setData(null);
                    }
                } else {
                    jsonResponse.setSuccessful(false);
                    jsonResponse.setStatus(400);
                    jsonResponse.setMessage("No reimbursements found. (2)");
                    jsonResponse.setData(null);
                }
            } else if ( (LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())).equals("Employee")) {
                List<ErsReimbursement> ersReimbursements = ersReimbursementDao.getAllErsReimbursementsByAuthor(ersUserFromDao.getErsUsersId());
                if (ersReimbursements.size() > 0) {
                    List<ReimbursementDTO> reimbursementDTOList = getReimbursementDTOList(ersReimbursements);
                    if (reimbursementDTOList.size() > 0) {
                        jsonResponse.setSuccessful(true);
                        jsonResponse.setStatus(200);
                        jsonResponse.setMessage("Successfully retrieved all reimbursements for ersUsersId="+ersUserFromDao.getErsUsersId());
                        jsonResponse.setData(reimbursementDTOList);
                        success = true;
                    } else {
                        jsonResponse.setSuccessful(false);
                        jsonResponse.setStatus(400);
                        jsonResponse.setMessage("No reimbursements found (3).");
                        jsonResponse.setData(null);
                    }
                } else {
                    jsonResponse.setSuccessful(false);
                    jsonResponse.setStatus(400);
                    jsonResponse.setMessage("No reimbursements found. (4)");
                    jsonResponse.setData(null);
                }
            }
        }
        return success;
    }

    public Boolean createErsReimbursement(ErsUsers ersUserFromDao, ErsReimbursement ersReimbursement, JsonResponse jsonResponse) {
        Boolean success = false;
        if (ersUserFromDao==null) {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("No session to use API.  Try logout/login.");
        } else if ((LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())).equals("Employee")) {
            if (ersReimbursementDao.createErsReimbursement(ersReimbursement)) {
                jsonResponse.setSuccessful(true);
                jsonResponse.setStatus(200);
                jsonResponse.setMessage("Request successfully created for "+ersReimbursement.getReimbDescription()+" $"+
                        ersReimbursement.getReimbAmount()+ " .");
                jsonResponse.setData(null);
                success = true;
            } else {
                jsonResponse.setSuccessful(false);
                jsonResponse.setStatus(400);
                jsonResponse.setMessage("Failed to create expense reimbursement request.");
                jsonResponse.setData(null);
            }
        } else {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("User role not authorized for this API.");
            jsonResponse.setData(null);
        }
        return success;
    }

    public Boolean deleteErsReimbursement(ErsUsers ersUserFromDao, Integer reimbId, JsonResponse jsonResponse) {
        Boolean success = false;
        if (ersUserFromDao==null) {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("No session to use API.  Try logout/login.");
        } else if ((LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())).equals("Employee")) {
            if (ersReimbursementDao.deleteErsReimbursement(reimbId)) {
                jsonResponse.setSuccessful(true);
                jsonResponse.setStatus(200);
                jsonResponse.setMessage("Request successfully deleted for Item# "+reimbId+".");
                jsonResponse.setData(null);
                success = true;
            } else {
                jsonResponse.setSuccessful(false);
                jsonResponse.setStatus(400);
                jsonResponse.setMessage("Failed to delete expense reimbursement request.");
                jsonResponse.setData(null);
            }
        } else {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("User role not authorized for this API.");
            jsonResponse.setData(null);
        }
        return success;
    }
    // It's to look up ErsUsers objects based on the authors or resolvers we found in the ErsReimbursement model object.
    // Given our ErsReimbursements list, find all the user id of all authors and resolvers to fetch just those ErsUsers
    // via the DAO layer in to a List then a Map so that we can look up the ErsUsers info based on the Integer user id
    // to populate our DTO later (ReimbursementDTO)
    public Map<Integer, ErsUsers> getErsUsersMapFromReimbursements(List<ErsReimbursement> ersReimbursements) {
        Map<Integer, ErsUsers> ersUsersM = null;
        Set<Integer> ersUserIds = new TreeSet<>();
        for (ErsReimbursement ersReimbursement:ersReimbursements) {
            if (ersReimbursement.getReimbAuthor()!=null) ersUserIds.add(ersReimbursement.getReimbAuthor());
            if (ersReimbursement.getReimbResolver()!=null) ersUserIds.add(ersReimbursement.getReimbResolver());
        }
        // Given a List of Integer ersUsersId values, ErsUsersDao can be used to fetch just those ErsUsers
        List<ErsUsers> ersUsers = ersUsersDao.getCertainErsUsers(ersUserIds);
        //System.out.println("getErsUsersMapFromReimbursements(...)"+ersUsers);
        if (ersUsers.size()>0) {
            ersUsersM = ersUsers.stream().collect(Collectors.toMap(ErsUsers::getErsUsersId, ErsUsers -> ErsUsers));
        }
        return ersUsersM;
    }
    // We are using this method of looking up ErsUsers values from a Map rather than doing an SQL join, the ErsReimbursements object
    // will contain author or resolver integer id numbers which can be looked up in the ErsUsersId to ErsUsers Map and filled in for our
    // DTO (Data Transfer Object) for client-side display of useful contact information to resolve expenses and reimbursement issues.
    public List<ReimbursementDTO> getReimbursementDTOList(List<ErsReimbursement> ersReimbursements) {
        List<ReimbursementDTO> reimbDTOs = new ArrayList<>();
        Map<Integer, ErsUsers> ersUsersM = this.getErsUsersMapFromReimbursements(ersReimbursements);
        if (ersUsersM != null) {
            ersReimbursements.forEach(ersReimbursement -> {
                ReimbursementDTO reimbDTO = new ReimbursementDTO(ersReimbursement);
                if (ersReimbursement.getReimbAuthor()!=null) {
                    reimbDTO.setAuthorEmail(ersUsersM.get(ersReimbursement.getReimbAuthor()).getUserEmail());
                    reimbDTO.setAuthorFirstname(ersUsersM.get(ersReimbursement.getReimbAuthor()).getUserFirstName());
                    reimbDTO.setAuthorLastname(ersUsersM.get(ersReimbursement.getReimbAuthor()).getUserLastName());
                }
                if (ersReimbursement.getReimbResolver()!=null) {
                    reimbDTO.setResolverEmail(ersUsersM.get(ersReimbursement.getReimbResolver()).getUserEmail());
                    reimbDTO.setResolverFirstname(ersUsersM.get(ersReimbursement.getReimbResolver()).getUserFirstName());
                    reimbDTO.setResolverLastname(ersUsersM.get(ersReimbursement.getReimbResolver()).getUserLastName());
                }
                reimbDTOs.add(reimbDTO);
            });
        }
        return reimbDTOs;
    }

    public Boolean updateErsReimbursement(ErsUsers ersUserFromDao, ReimbStatusDTO reimbStatusDTO, JsonResponse jsonResponse) {
        Boolean success = false;
        if (ersUserFromDao==null) {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("No session to use API.  Try logout/login.");
            jsonResponse.setData(null);
        } else if ( (LookupTables.getErsUserRole(ersUserFromDao.getUserRoleId())).equals("FinManager") ) {
            if (ersReimbursementDao.updateErsReimbursement(reimbStatusDTO)) {
                jsonResponse.setSuccessful(true);
                jsonResponse.setStatus(200);
                jsonResponse.setMessage("Status successfully updated to "+LookupTables.getErsReimbursementStatus(reimbStatusDTO.getReimbStatusId())+ " for Item# "+
                        reimbStatusDTO.getReimbId()+".");
                jsonResponse.setData(reimbStatusDTO);
                success = true;
            } else {
                jsonResponse.setSuccessful(false);
                jsonResponse.setStatus(400);
                jsonResponse.setMessage("Failed to update expense reimbursement status to " + LookupTables.getErsReimbursementStatus(reimbStatusDTO.getReimbStatusId()) +
                        "for Item# "+reimbStatusDTO.getReimbId()+".");
                jsonResponse.setData(reimbStatusDTO);
            }
        } else {
            jsonResponse.setSuccessful(false);
            jsonResponse.setStatus(400);
            jsonResponse.setMessage("User role not authorized for this API.");
            jsonResponse.setData(null);
        }
        return success;
    }
}
