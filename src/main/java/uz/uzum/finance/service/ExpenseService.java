package uz.uzum.finance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.uzum.finance.model.CustomLabel;
import uz.uzum.finance.model.Expense;
import uz.uzum.finance.model.UserInfo;
import uz.uzum.finance.repository.CustomLabelRepository;
import uz.uzum.finance.repository.ExpenseRepository;
import uz.uzum.finance.repository.UserInfoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CustomLabelRepository customLabelRepository;
    private final UserInfoRepository userInfoRepository;


    private Set<CustomLabel> fetchCustomLabelsByNames(List<String> customLabelNames) {
        return customLabelNames.stream()
                .map(customLabelRepository::findByName)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public List<Expense> getAllExpenses(String email, LocalDate startDate, LocalDate endDate, List<String> customLabelNames) {

        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (customLabelNames != null && !customLabelNames.isEmpty()) {
            return expenseRepository.findByUserEmailAndDateBetweenAndLabels(startDate, endDate, customLabelNames, email);
        } else {
            return expenseRepository.findByUserInfo_EmailAndDateBetween(email, startDate, endDate);
        }
    }

    @Transactional
    public Expense addExpense(String email ,BigDecimal amount, String description, LocalDate date, List<String> customLabelNames) {

        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Expense expense = new Expense();
        expense.setUserInfo(userInfo);
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setDate(date);
        expense.setCustomLabels(fetchCustomLabelsByNames(customLabelNames));
        return expenseRepository.save(expense);
    }

    @Transactional
    public String deleteExpense(String email, Long id) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Expense not found"));
        if (!expense.getUserInfo().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.delete(expense);
        return "Expense with id " + id + " was deleted";
    }

    @Transactional
    public Expense updateExpense(String email, Long id, BigDecimal amount, String description, LocalDate date, List<String> customLabelNames) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Expense with ID " + id + " not found"));

        if (!expense.getUserInfo().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this expense");
        }

        // Update basic fields
        expense.setAmount(amount);
        expense.setDescription(description);
        expense.setDate(date);
        expense.setCustomLabels(fetchCustomLabelsByNames(customLabelNames));

        return expenseRepository.save(expense);
    }
}
