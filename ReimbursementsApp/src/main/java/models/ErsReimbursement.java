package models;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

public class ErsReimbursement {
    private Integer reimbId = null;
    private Double reimbAmount;
    private Timestamp reimbSubmitted;
    private Timestamp reimbResolved;
    private String reimbDescription;
    private byte[] reimbReceipt;
    private Integer reimbAuthor;
    private Integer reimbResolver;
    private Integer reimbStatusId;
    private Integer reimbTypeId;

    public ErsReimbursement(){}
    public ErsReimbursement(Integer reimbId, Double reimbAmount, Timestamp reimbSubmitted, Timestamp reimbResolved, String reimbDescription, byte[] reimbReceipt, Integer reimbAuthor, Integer reimbResolver, Integer reimbStatusId, Integer reimbTypeId) {
        this.reimbId = reimbId;
        this.reimbAmount = reimbAmount;
        this.reimbSubmitted = reimbSubmitted;
        this.reimbResolved = reimbResolved;
        this.reimbDescription = reimbDescription;
        this.reimbReceipt = reimbReceipt;
        this.reimbAuthor = reimbAuthor;
        this.reimbResolver = reimbResolver;
        this.reimbStatusId = reimbStatusId;
        this.reimbTypeId = reimbTypeId;
    }

    public Integer getReimbId() { return reimbId; }
    public Double getReimbAmount() { return reimbAmount; }
    public Timestamp getReimbSubmitted() { return reimbSubmitted; }
    public Timestamp getReimbResolved() {        return reimbResolved;    }
    public String getReimbDescription() {        return reimbDescription;    }
    public byte[] getReimbReceipt() {        return reimbReceipt;    }
    public Integer getReimbAuthor() {        return reimbAuthor;    }
    public Integer getReimbResolver() {        return reimbResolver;    }
    public Integer getReimbStatusId() {        return reimbStatusId;    }
    public Integer getReimbTypeId() {        return reimbTypeId;    }

    public void setReimbId(Integer reimbId) { this.reimbId = reimbId; }
    public void setReimbAmount(Double reimbAmount) { this.reimbAmount = reimbAmount; }
    public void setReimbSubmitted(Timestamp reimbSubmitted) { this.reimbSubmitted = reimbSubmitted; }
    public void setReimbResolved(Timestamp reimbResolved) { this.reimbResolved = reimbResolved; }
    public void setReimbDescription(String reimbDescription) { this.reimbDescription = reimbDescription; }
    public void setReimbReceipt(byte[] reimbReceipt) { this.reimbReceipt = reimbReceipt; }
    public void setReimbAuthor(Integer reimbAuthor) { this.reimbAuthor = reimbAuthor; }
    public void setReimbResolver(Integer reimbResolver) { this.reimbResolver = reimbResolver; }
    public void setReimbStatusId(Integer reimbStatusId) { this.reimbStatusId = reimbStatusId; }
    public void setReimbTypeId(Integer reimbTypeId) { this.reimbTypeId = reimbTypeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErsReimbursement that = (ErsReimbursement) o;
        return Objects.equals(reimbId, that.reimbId) && Objects.equals(reimbAmount, that.reimbAmount) && Objects.equals(reimbSubmitted, that.reimbSubmitted) && Objects.equals(reimbResolved, that.reimbResolved) && Objects.equals(reimbDescription, that.reimbDescription) && Arrays.equals(reimbReceipt, that.reimbReceipt) && Objects.equals(reimbAuthor, that.reimbAuthor) && Objects.equals(reimbResolver, that.reimbResolver) && Objects.equals(reimbStatusId, that.reimbStatusId) && Objects.equals(reimbTypeId, that.reimbTypeId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(reimbId, reimbAmount, reimbSubmitted, reimbResolved, reimbDescription, reimbAuthor, reimbResolver, reimbStatusId, reimbTypeId);
        result = 31 * result + Arrays.hashCode(reimbReceipt);
        return result;
    }

    @Override
    public String toString() {
        return "ErsReimbursement{" +
                "reimbId=" + reimbId +
                ", reimbAmount=" + reimbAmount +
                ", reimbSubmitted=" + reimbSubmitted +
                ", reimbResolved=" + reimbResolved +
                ", reimbDescription='" + reimbDescription + '\'' +
                ", reimbReceipt=" + Arrays.toString(reimbReceipt) +
                ", reimbAuthor=" + reimbAuthor +
                ", reimbResolver=" + reimbResolver +
                ", reimbStatusId=" + reimbStatusId +
                ", reimbTypeId=" + reimbTypeId +
                '}';
    }
}
