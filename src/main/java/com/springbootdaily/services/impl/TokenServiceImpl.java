package com.springbootdaily.services.impl;

import com.springbootdaily.entities.Token;
import com.springbootdaily.repositories.TokenRepository;
import com.springbootdaily.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public List<Token> findAllValidTokenByUser(Integer id) {
        return null;
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return this.tokenRepository.findByToken(token);
    }
}
