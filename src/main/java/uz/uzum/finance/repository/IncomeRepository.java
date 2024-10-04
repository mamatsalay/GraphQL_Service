package uz.uzum.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzum.finance.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT i FROM Income i LEFT JOIN i.customLabels cl WHERE i.userInfo.email = :email AND i.date BETWEEN :startDate AND :endDate AND (:customLabelNames IS NULL OR cl.name IN :customLabelNames)")
    List<Income> findByUserEmailAndDateBetweenAndLabels(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("customLabelNames") List<String> customLabelNames,
                                            @Param("email") String email);

    List<Income> findByUserInfo_EmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

}