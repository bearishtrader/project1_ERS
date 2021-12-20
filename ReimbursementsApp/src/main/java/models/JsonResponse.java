package models;

public class JsonResponse {
    private Boolean successful;
    private Integer status;
    private String message;
    private Object data=null;

    public JsonResponse() {}
    public JsonResponse(Boolean successful, Integer status, String message, Object data) {
        this.successful = successful;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Boolean getSuccessful() {
        return successful;
    }
    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "successful=" + successful +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
