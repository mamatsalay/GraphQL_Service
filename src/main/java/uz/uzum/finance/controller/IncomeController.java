package uz.uzum.finance.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.uzum.finance.model.Income;
import uz.uzum.finance.model.Income;
import uz.uzum.finance.service.IncomeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IncomeController {
    
    private final IncomeService incomeService;

    @MutationMapping
    public Income addIncome(@Argument BigDecimal amount,
                              @Argument String description,
                              @Argument LocalDate date,
                              @Argument List<String> customLabelNames) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return incomeService.addIncome(email, amount, description, date, customLabelNames);
        } else{
            throw new IllegalArgumentException("Bad Credentials");
        }

    }

    @MutationMapping
    public Income updateIncome(@Argument Long id,
                                 @Argument BigDecimal amount,
                                 @Argument String description,
                                 @Argument LocalDate date,
                                 @Argument List<String> customLabelNames){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return incomeService.updateIncome(email, id, amount, description, date, customLabelNames);
        } else{
            throw new IllegalArgumentException("User is not authorized");
        }
    }

    @MutationMapping
    public String deleteIncome(@Argument Long id) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return incomeService.deleteIncome(email, id);
        } else{
            throw new IllegalArgumentException("Bad Credentials, User is not authorized");
        }
    }

    @QueryMapping
    public List<Income> getAllIncomes(@Argument @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate StartDate,
                                        @Argument @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate EndDate,
                                        @Argument List<String> customLabelNames) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return incomeService.getAllIncomes(email, StartDate, EndDate, customLabelNames);
        } else{
            throw new IllegalArgumentException("Bad Credentials");
        }
    }
}
