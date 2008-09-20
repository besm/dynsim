package dynsim.exceptions;

public class DynSimException extends Exception {
	private static final long serialVersionUID = 6029737102958164296L;

	public DynSimException(String msg) {
		super(msg);
	}

	public DynSimException() {
		super();
	}

	public DynSimException(String message, Throwable cause) {
		super(message, cause);
	}

	public DynSimException(Throwable cause) {
		super(cause);
	}

}
