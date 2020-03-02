package mx.com.agurno.flipmarket.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {
	
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public String encode(String password) {
			return passwordEncoder.encode(password);
	}
	
	
}