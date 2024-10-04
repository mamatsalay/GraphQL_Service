package uz.uzum.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.uzum.finance.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query("SELECT i FROM Income i JOIN i.customLabels cl WHERE i.date BETWEEN :startDate AND :endDate AND :customLabelNames IS NULL OR cl.name IN :customLabelNames")
    List<Income> findByDateBetweenAndExactLabels(@Param("startDate") LocalDate startDate,
                                            @Param("endDate") LocalDate endDate,
                                            @Param("customLabelNames") List<String> customLabelNames);

    List<Income> findByDateBetween(LocalDate startDate, LocalDate endDate);

}