package com.springbootdaily.repositories;

import com.springbootdaily.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
//    @Query(value = """
//      select t from Token t inner join User u\s
//      on t.user.id = u.id\s
//      where u.id = :id and (t.expired = false or t.revoked = false)\s
//      """)
//    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

    @Query(value = "DELETE FROM tokens t WHERE t.user_id = :userId", nativeQuery = true)
    void deleteAllTokensByUserId(Long userId);

}