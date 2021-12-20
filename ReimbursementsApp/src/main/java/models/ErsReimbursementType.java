package models;

import java.util.Objects;

public class ErsReimbursementType {
    private Integer reimbTypeId;
    private String reimbType;

    public ErsReimbursementType() {}
    public ErsReimbursementType(Integer reimbTypeId, String reimbType) {
        this.reimbTypeId = reimbTypeId;
        this.reimbType = reimbType;
    }

    public Integer getReimbTypeId() { return reimbTypeId; }
    public void setReimbTypeId(Integer reimbTypeId) { this.reimbTypeId = reimbTypeId; }
    public String getReimbType() { return reimbType; }
    public void setReimbType(String reimbType) { this.reimbType = reimbType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErsReimbursementType that = (ErsReimbursementType) o;
        return Objects.equals(reimbTypeId, that.reimbTypeId) && Objects.equals(reimbType, that.reimbType);
    }
    @Override
    public int hashCode() {
        return Objects.hash(reimbTypeId, reimbType);
    }

    @Override
    public String toString() {
        return "ErsReimbursementType{" +
                "reimbTypeId=" + reimbTypeId +
                ", reimbType='" + reimbType + '\'' +
                '}';
    }
}
