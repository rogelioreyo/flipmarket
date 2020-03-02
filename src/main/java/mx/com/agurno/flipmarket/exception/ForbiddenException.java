package mx.com.agurno.flipmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ForbiddenException - ForbiddenException.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 9/06/2018
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
    
	private static final long serialVersionUID = -3644559825887111175L;

	public ForbiddenException() {
        super();
    }

    public ForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(final String message) {
        super(message);
    }

    public ForbiddenException(final Throwable cause) {
        super(cause);
    }
    
}