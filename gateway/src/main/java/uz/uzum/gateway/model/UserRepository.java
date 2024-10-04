package uz.uzum.gateway.model;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, Long> {

    @Query("SELECT * FROM users WHERE email = :email")
    Mono<Users> findByEmail(String email);

}
