package mx.com.agurno.flipmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NotFoundException - NotFoundException.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@ResponseStatus(HttpStatus.NO_CONTENT)
public class NotFoundException extends RuntimeException {
    
	private static final long serialVersionUID = -3644559825887111175L;

	public NotFoundException() {
        super();
    }

    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException(final Throwable cause) {
        super(cause);
    }
    
}