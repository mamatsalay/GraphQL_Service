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
import uz.uzum.finance.model.Expense;
import uz.uzum.finance.service.ExpenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @MutationMapping
    public Expense addExpense(@Argument BigDecimal amount,
                              @Argument String description,
                              @Argument LocalDate date,
                              @Argument List<String> customLabelNames) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return expenseService.addExpense(email, amount, description, date, customLabelNames);
        } else{
            throw new IllegalArgumentException("Bad Credentials");
        }

    }

    @MutationMapping
    public Expense updateExpense(@Argument Long id,
                                 @Argument BigDecimal amount,
                                 @Argument String description,
                                 @Argument LocalDate date,
                                 @Argument List<String> customLabelNames){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return expenseService.updateExpense(email, id, amount, description, date, customLabelNames);
        } else{
            throw new IllegalArgumentException("User is not authorized");
        }
    }

    @MutationMapping
    public String deleteExpense(@Argument Long id) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return expenseService.deleteExpense(email, id);
        } else{
            throw new IllegalArgumentException("Bad Credentials, User is not authorized");
        }
    }

    @QueryMapping
    public List<Expense> getAllExpenses(@Argument @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate StartDate,
                                        @Argument @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate EndDate,
                                        @Argument List<String> customLabelNames) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return expenseService.getAllExpenses(email, StartDate, EndDate, customLabelNames);
        } else{
            throw new IllegalArgumentException("Bad Credentials");
        }
    }

}
