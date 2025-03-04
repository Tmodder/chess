package service;

public class ServiceError extends RuntimeException {
    public ServiceError(String message) {
        super(message);
    }
    public String toJson()
    {
        String out = "\"message\": ";
        out += getMessage();
        return out.trim();
    }
}
