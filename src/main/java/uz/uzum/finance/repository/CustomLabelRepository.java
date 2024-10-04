package uz.uzum.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.uzum.finance.model.CustomLabel;

import java.util.List;
import java.util.Optional;

public interface CustomLabelRepository extends JpaRepository<CustomLabel, Long> {

    CustomLabel findByName(String name);
    List<CustomLabel> findByUserInfo_Email(String email);

}