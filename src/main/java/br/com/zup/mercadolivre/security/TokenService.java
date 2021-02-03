package br.com.zup.mercadolivre.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${ml.jwt.secret}")
	private String secret;	
	
	@Value("${ml.jwt.expiration}")
	private String expiration;
	
	public String gerar(Authentication authentication) {
		var authUser = (AuthUser) authentication.getPrincipal();
		var data = new Date();
		var dataExpiracao = new Date(data.getTime() + Long.valueOf(expiration));
		
		return Jwts.builder()
				.setIssuer("API do mercado livre(ZUP)")
				.setSubject(authUser.getUserId().toString())
				.setIssuedAt(data)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, this.secret)
				.compact();
	}

	public boolean isTokenValido(String token) {
		try {
			if(token == null) {
				throw new Exception("");
			}
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (AuthenticationException e) {
			return false;
		}catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.valueOf(claims.getSubject());
	}
	
}
