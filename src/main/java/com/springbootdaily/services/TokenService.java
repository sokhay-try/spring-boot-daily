package com.springbootdaily.services;

import com.springbootdaily.entities.Token;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenService {

    @Query(value = """
      select t from Token t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);
    void deleteById(Long id);
}
