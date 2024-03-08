package com.tutorial.authservice.service;

import com.netflix.discovery.converters.Auto;
import com.tutorial.authservice.dto.AuthUserDto;
import com.tutorial.authservice.dto.TokenDto;
import com.tutorial.authservice.entity.AuthUser;
import com.tutorial.authservice.repository.AuthUserRepository;
import com.tutorial.authservice.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserService {

    @Autowired
    AuthUserRepository authUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    public AuthUser save(AuthUserDto dto){
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName()); //obtenemos el usuario
        if(user.isPresent()){
            return null;
        }
        String password = passwordEncoder.encode(dto.getPassword()); //codificamos la contraseña
        AuthUser authUser = new AuthUser(); //formamos un nuevo ususario
        authUser.setUserName(dto.getUserName());
        authUser.setPassword(password);
        return authUserRepository.save(authUser); // guardamos el usuario
    }

    public TokenDto login(AuthUserDto dto){
        Optional<AuthUser> user = authUserRepository.findByUserName(dto.getUserName()); //obtenemos el usuario
        if(!user.isPresent()){ // si no existe el usuario se devuelve null
            return null;
        }
        if(passwordEncoder.matches(dto.getPassword(), user.get().getPassword())){ // si las contraseñas coinciden entonces
            // creamos un token del login
            TokenDto token = new TokenDto();
            token.setToken(jwtProvider.createToken(user.get()));
            return token; // devolvemos un token perteneciente al usuario logueado
        }
        return null;
    }

    public TokenDto validate(String token){
        if(!jwtProvider.validate(token)){ //validamos que la contraseña del token sea la correcta
            return null;
        }
        String username = jwtProvider.getUserNameFromToken(token);
        if(!authUserRepository.findByUserName(username).isPresent()){ //validamos que el usuario del token exista
            return null;
        }
        TokenDto newToken = new TokenDto();
        newToken.setToken(token);
        return newToken;
    }
}
