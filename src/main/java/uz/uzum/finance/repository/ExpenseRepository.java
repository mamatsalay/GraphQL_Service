package uz.uzum.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzum.finance.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT e FROM Expense e JOIN e.customLabels cl WHERE e.date BETWEEN :startDate AND :endDate AND :customLabelNames IS NULL OR cl.name IN :customLabelNames")
    List<Expense> findByDateBetweenAndExactLabels(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             @Param("customLabelNames") List<String> customLabelNames);

    List<Expense> findByDateBetween(LocalDate startDate, LocalDate endDate);

}