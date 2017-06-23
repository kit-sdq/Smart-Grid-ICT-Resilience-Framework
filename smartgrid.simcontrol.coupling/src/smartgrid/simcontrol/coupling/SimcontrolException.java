package smartgrid.simcontrol.coupling;

public class SimcontrolException extends Exception {

    private static final long serialVersionUID = 4513252224023093890L;

    public SimcontrolException(String message) {
        super(message);
    }

    public SimcontrolException(String message, Throwable cause) {
        super(message, cause);
    }
}
