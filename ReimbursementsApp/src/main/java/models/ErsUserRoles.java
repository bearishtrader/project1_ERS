package models;

import java.util.Objects;

public class ErsUserRoles {
    private Integer ersUserRoleId;
    private String userRole;

    public ErsUserRoles() {}
    public ErsUserRoles(Integer ersUserRoleId, String userRole) {
        this.ersUserRoleId = ersUserRoleId;
        this.userRole = userRole;
    }

    public Integer getErsUserRoleId() { return ersUserRoleId; }
    public void setErsUserRoleId(Integer ersUserRoleId) { this.ersUserRoleId = ersUserRoleId; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErsUserRoles that = (ErsUserRoles) o;
        return Objects.equals(ersUserRoleId, that.ersUserRoleId) && Objects.equals(userRole, that.userRole);
    }
    @Override
    public int hashCode() {
        return Objects.hash(ersUserRoleId, userRole);
    }

    @Override
    public String toString() {
        return "ErsUserRoles{" +
                "ersUserRoleId=" + ersUserRoleId +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}