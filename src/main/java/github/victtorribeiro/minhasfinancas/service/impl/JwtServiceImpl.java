package github.victtorribeiro.minhasfinancas.service.impl;

import github.victtorribeiro.minhasfinancas.domain.entity.Usuario;
import github.victtorribeiro.minhasfinancas.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiracao}")
    private String expiracao;
    @Value("${jwt.chave-assinatura}")
    private String assinatura;

    @Override
    public String gerarToken(Usuario usuario) {
        long exp = Long.valueOf(expiracao);
        LocalDateTime dataHoraExpiracao = LocalDateTime.now().plusMinutes(exp);
        Instant instant = dataHoraExpiracao.atZone(ZoneId.systemDefault()).toInstant();
        Date data = Date.from(instant);

        String horaExpiracaoToken = dataHoraExpiracao.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));

        String token = Jwts.builder()
                .setExpiration(data)
                .setSubject(usuario.getEmail())
                .claim("nome", usuario.getNome())
                .claim("horaExpiracao", horaExpiracaoToken)
                .signWith(SignatureAlgorithm.HS512, assinatura)
                .compact();

        return token;
    }

    @Override
    public Claims obterClaims(String token) throws ExpiredJwtException {
        return null;
    }

    @Override
    public boolean isTokenValido(String token) {
        return false;
    }

    @Override
    public String obterLoginUsuario(String token) {
        return null;
    }
}
