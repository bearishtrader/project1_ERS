package models;

import java.util.Objects;

public class ErsReimbursementStatus {
    private Integer reimbStatusId;
    private String reimbStatus;

    public ErsReimbursementStatus(){}
    public ErsReimbursementStatus(Integer reimbStatusId, String reimbStatus) {
        this.reimbStatusId = reimbStatusId;
        this.reimbStatus = reimbStatus;
    }

    public Integer getReimbStatusId() { return reimbStatusId; }
    public void setReimbStatusId(Integer reimbStatusId) { this.reimbStatusId = reimbStatusId; }
    public String getReimbStatus() { return reimbStatus; }
    public void setReimbStatus(String reimbStatus) { this.reimbStatus = reimbStatus; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErsReimbursementStatus that = (ErsReimbursementStatus) o;
        return Objects.equals(reimbStatusId, that.reimbStatusId) && Objects.equals(reimbStatus, that.reimbStatus);
    }
    @Override
    public int hashCode() {
        return Objects.hash(reimbStatusId, reimbStatus);
    }

    @Override
    public String toString() {
        return "ErsReimbursementStatus{" +
                "reimbStatusId=" + reimbStatusId +
                ", reimbStatus='" + reimbStatus + '\'' +
                '}';
    }
}
