package dto;

import java.sql.Timestamp;
import java.util.Objects;
// This is specifically used for our PATCH to update the reimbursement status (FinManager Approve or Deny)
public class ReimbStatusDTO {
    private Integer reimbId;
    private Timestamp reimbResolved=null;
    private Integer reimbResolver=null;
    private Integer reimbStatusId=null;

    public ReimbStatusDTO(){}
    public ReimbStatusDTO(Integer reimbId, Timestamp reimbResolved, Integer reimbResolver, Integer reimbStatusId) {
        this.reimbId = reimbId;
        this.reimbResolved = reimbResolved;
        this.reimbResolver = reimbResolver;
        this.reimbStatusId = reimbStatusId;
    }

    public Integer getReimbId() {
        return reimbId;
    }
    public void setReimbId(Integer reimbId) {
        this.reimbId = reimbId;
    }

    public Timestamp getReimbResolved() {
        return reimbResolved;
    }
    public void setReimbResolved(Timestamp reimbResolved) {
        this.reimbResolved = reimbResolved;
    }

    public Integer getReimbResolver() {
        return reimbResolver;
    }
    public void setReimbResolver(Integer reimbResolver) {
        this.reimbResolver = reimbResolver;
    }

    public Integer getReimbStatusId() {
        return reimbStatusId;
    }
    public void setReimbStatusId(Integer reimbStatusId) {
        this.reimbStatusId = reimbStatusId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbStatusDTO that = (ReimbStatusDTO) o;
        return Objects.equals(reimbId, that.reimbId) && Objects.equals(reimbResolved, that.reimbResolved) && Objects.equals(reimbResolver, that.reimbResolver) && Objects.equals(reimbStatusId, that.reimbStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reimbId, reimbResolved, reimbResolver, reimbStatusId);
    }

    @Override
    public String toString() {
        return "ReimbStatusDTO{" +
                "reimbId=" + reimbId +
                ", reimbResolved=" + reimbResolved +
                ", reimbResolver=" + reimbResolver +
                ", reimbStatusId=" + reimbStatusId +
                '}';
    }
}
