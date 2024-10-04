package uz.uzum.register.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.uzum.register.model.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
}
