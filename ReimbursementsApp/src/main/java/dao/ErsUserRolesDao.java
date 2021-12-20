package dao;

import models.ErsUserRoles;

import java.util.List;

public interface ErsUserRolesDao {
    List<ErsUserRoles> getAllErsUserRoles();
    ErsUserRoles getOneErsUserRole(Integer ersUserRoleId);
    // No write operations needed for lookup tables
}
