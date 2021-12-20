package dao;

import models.ErsUsers;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ErsUsersDao {
    Boolean createErsUser(ErsUsers ersUser);
    List<ErsUsers> getAllErsUsers();
    ErsUsers getOneErsUser(Integer ersUserId);
    ErsUsers getOneErsUser(String ersUsername, String userEmail);
    List<ErsUsers> getCertainErsUsers(Set<Integer> ersUserIds);
    Boolean updateErsUser(ErsUsers ersUser);
    Boolean deleteErsUser(Integer ersUserId);
}
