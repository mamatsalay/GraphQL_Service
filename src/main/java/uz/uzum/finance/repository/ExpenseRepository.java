package uz.uzum.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzum.finance.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e LEFT JOIN e.customLabels cl WHERE e.userInfo.email = :email AND e.date BETWEEN :startDate AND :endDate AND (:customLabelNames IS NULL OR cl.name IN :customLabelNames)")
    List<Expense> findByUserEmailAndDateBetweenAndLabels(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             @Param("customLabelNames") List<String> customLabelNames,
                                             @Param("email") String email);

    List<Expense> findByUserInfo_EmailAndDateBetween(String email, LocalDate startDate, LocalDate endDate);

}