package exception.customException;

public class DBConnectionException extends RuntimeException {
    public DBConnectionException(String message) {
        super(message);
    }
}
