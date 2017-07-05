package smartgrid.simcontrol.coupling.Exceptions;

public class SimcontrolInitializationException extends SimcontrolException {

    private static final long serialVersionUID = 1438743556473201823L;

    public SimcontrolInitializationException(String message) {
        super(message);
    }

    public SimcontrolInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
