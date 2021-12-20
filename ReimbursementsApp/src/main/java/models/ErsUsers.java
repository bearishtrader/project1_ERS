package models;

import java.util.Objects;

public class ErsUsers {
    private Integer ersUsersId=null;
    private String ersUsername;
    private String ersPassword;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private Integer userRoleId;

    public ErsUsers() {}

    public ErsUsers(Integer ersUsersId, String ersUsername, String ersPassword, String userFirstName, String userLastName, String userEmail, Integer userRoleId) {
        this.ersUsersId = ersUsersId;
        this.ersUsername = ersUsername;
        this.ersPassword = ersPassword;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userRoleId = userRoleId;
    }

    public Integer getErsUsersId() {
        return ersUsersId;
    }
    public String getErsUsername() {
        return ersUsername;
    }
    public String getErsPassword() {
        return ersPassword;
    }
    public String getUserFirstName() {
        return userFirstName;
    }
    public String getUserLastName() {
        return userLastName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setErsUsersId(Integer ersUsersId) {
        this.ersUsersId = ersUsersId;
    }
    public void setErsUsername(String ersUsername) {
        this.ersUsername = ersUsername;
    }
    public void setErsPassword(String ersPassword) {
        this.ersPassword = ersPassword;
    }
    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }
    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErsUsers ersUsers = (ErsUsers) o;
        return Objects.equals(ersUsersId, ersUsers.ersUsersId) && Objects.equals(ersUsername, ersUsers.ersUsername) && Objects.equals(ersPassword, ersUsers.ersPassword) && Objects.equals(userFirstName, ersUsers.userFirstName) && Objects.equals(userLastName, ersUsers.userLastName) && Objects.equals(userEmail, ersUsers.userEmail) && Objects.equals(userRoleId, ersUsers.userRoleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ersUsersId, ersUsername, ersPassword, userFirstName, userLastName, userEmail, userRoleId);
    }

    @Override
    public String toString() {
        return "ErsUsers{" +
                "ersUsersId=" + ersUsersId +
                ", ersUsername='" + ersUsername + '\'' +
                ", ersPassword='" + ersPassword + '\'' +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userRoleId=" + userRoleId +
                '}';
    }
}
