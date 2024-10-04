package uz.uzum.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzum.finance.model.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByEmail(String email);

}