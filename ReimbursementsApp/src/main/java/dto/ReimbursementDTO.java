package dto;

import models.ErsReimbursement;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

public class ReimbursementDTO {
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
    // This block of following properties is needed for display on the client side so people know who to contact, our ErsReimbursementService will handle populating these
    private String authorFirstname;
    private String authorLastname;
    private String authorEmail;
    private String resolverFirstname = null;
    private String resolverLastname = null;
    private String resolverEmail = null;

    public ReimbursementDTO(Integer reimbId, Double reimbAmount, Timestamp reimbSubmitted, Timestamp reimbResolved, String reimbDescription, byte[] reimbReceipt, Integer reimbAuthor, Integer reimbResolver, Integer reimbStatusId, Integer reimbTypeId, String authorFirstname, String authorLastname, String authorEmail, String resolverFirstname, String resolverLastname, String resolverEmail) {
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
        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
        this.authorEmail = authorEmail;
        this.resolverFirstname = resolverFirstname;
        this.resolverLastname = resolverLastname;
        this.resolverEmail = resolverEmail;
    }
    public ReimbursementDTO(ErsReimbursement ersReimbursement){
        this.reimbId = ersReimbursement.getReimbId();
        this.reimbAmount = ersReimbursement.getReimbAmount();
        this.reimbSubmitted = ersReimbursement.getReimbSubmitted();
        this.reimbResolved = ersReimbursement.getReimbResolved();
        this.reimbDescription = ersReimbursement.getReimbDescription();
        this.reimbReceipt = ersReimbursement.getReimbReceipt();
        this.reimbAuthor = ersReimbursement.getReimbAuthor();
        this.reimbResolver = ersReimbursement.getReimbResolver();
        this.reimbStatusId = ersReimbursement.getReimbStatusId();
        this.reimbTypeId = ersReimbursement.getReimbTypeId();
        this.authorFirstname = null;
        this.authorLastname = null;
        this.authorEmail = null;
        this.resolverFirstname = null;
        this.resolverLastname = null;
        this.resolverEmail = null;
    }

    public Integer getReimbId() {
        return reimbId;
    }
    public void setReimbId(Integer reimbId) {
        this.reimbId = reimbId;
    }

    public Double getReimbAmount() {
        return reimbAmount;
    }
    public void setReimbAmount(Double reimbAmount) {
        this.reimbAmount = reimbAmount;
    }

    public Timestamp getReimbSubmitted() {
        return reimbSubmitted;
    }
    public void setReimbSubmitted(Timestamp reimbSubmitted) {
        this.reimbSubmitted = reimbSubmitted;
    }

    public Timestamp getReimbResolved() {
        return reimbResolved;
    }
    public void setReimbResolved(Timestamp reimbResolved) {
        this.reimbResolved = reimbResolved;
    }

    public String getReimbDescription() {
        return reimbDescription;
    }
    public void setReimbDescription(String reimbDescription) {
        this.reimbDescription = reimbDescription;
    }

    public byte[] getReimbReceipt() {
        return reimbReceipt;
    }
    public void setReimbReceipt(byte[] reimbReceipt) {
        this.reimbReceipt = reimbReceipt;
    }

    public Integer getReimbAuthor() {
        return reimbAuthor;
    }
    public void setReimbAuthor(Integer reimbAuthor) {
        this.reimbAuthor = reimbAuthor;
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

    public Integer getReimbTypeId() {
        return reimbTypeId;
    }
    public void setReimbTypeId(Integer reimbTypeId) {
        this.reimbTypeId = reimbTypeId;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }
    public void setAuthorFirstname(String authorFirstname) {
        this.authorFirstname = authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }
    public void setAuthorLastname(String authorLastname) {
        this.authorLastname = authorLastname;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }
    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getResolverFirstname() {
        return resolverFirstname;
    }
    public void setResolverFirstname(String resolverFirstname) {
        this.resolverFirstname = resolverFirstname;
    }

    public String getResolverLastname() {
        return resolverLastname;
    }
    public void setResolverLastname(String resolverLastname) {
        this.resolverLastname = resolverLastname;
    }

    public String getResolverEmail() {
        return resolverEmail;
    }
    public void setResolverEmail(String resolverEmail) {
        this.resolverEmail = resolverEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReimbursementDTO that = (ReimbursementDTO) o;
        return Objects.equals(reimbId, that.reimbId) && Objects.equals(reimbAmount, that.reimbAmount) && Objects.equals(reimbSubmitted, that.reimbSubmitted) && Objects.equals(reimbResolved, that.reimbResolved) && Objects.equals(reimbDescription, that.reimbDescription) && Arrays.equals(reimbReceipt, that.reimbReceipt) && Objects.equals(reimbAuthor, that.reimbAuthor) && Objects.equals(reimbResolver, that.reimbResolver) && Objects.equals(reimbStatusId, that.reimbStatusId) && Objects.equals(reimbTypeId, that.reimbTypeId) && Objects.equals(authorFirstname, that.authorFirstname) && Objects.equals(authorLastname, that.authorLastname) && Objects.equals(authorEmail, that.authorEmail) && Objects.equals(resolverFirstname, that.resolverFirstname) && Objects.equals(resolverLastname, that.resolverLastname) && Objects.equals(resolverEmail, that.resolverEmail);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(reimbId, reimbAmount, reimbSubmitted, reimbResolved, reimbDescription, reimbAuthor, reimbResolver, reimbStatusId, reimbTypeId, authorFirstname, authorLastname, authorEmail, resolverFirstname, resolverLastname, resolverEmail);
        result = 31 * result + Arrays.hashCode(reimbReceipt);
        return result;
    }

    @Override
    public String toString() {
        return "ReimbursementDTO{" +
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
                ", authorFirstname='" + authorFirstname + '\'' +
                ", authorLastname='" + authorLastname + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", resolverFirstname='" + resolverFirstname + '\'' +
                ", resolverLastname='" + resolverLastname + '\'' +
                ", resolverEmail='" + resolverEmail + '\'' +
                '}';
    }
}
