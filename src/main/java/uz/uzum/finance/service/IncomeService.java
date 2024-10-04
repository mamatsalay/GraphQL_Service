package uz.uzum.finance.service;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import uz.uzum.finance.model.CustomLabel;
import uz.uzum.finance.model.Income;
import uz.uzum.finance.model.UserInfo;
import uz.uzum.finance.repository.CustomLabelRepository;
import uz.uzum.finance.repository.IncomeRepository;
import uz.uzum.finance.repository.UserInfoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final CustomLabelRepository customLabelRepository;
    private final UserInfoRepository userInfoRepository;

    @Transactional(readOnly = true)
    public List<Income> getAllIncomes(String email, LocalDate startDate, LocalDate endDate, List<String> customLabelNames) {

        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (customLabelNames != null && !customLabelNames.isEmpty()) {
            return incomeRepository.findByUserEmailAndDateBetweenAndLabels(startDate, endDate, customLabelNames, email);
        } else {
            return incomeRepository.findByUserInfo_EmailAndDateBetween(email, startDate, endDate);
        }
    }

    @Transactional
    public Income addIncome(String email, BigDecimal amount, String description, LocalDate date, List<String> customLabelNames) {

        UserInfo user = userInfoRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));

        Income income = new Income();
        income.setAmount(amount);
        income.setDescription(description);
        income.setDate(date);
        income.setUserInfo(user);
        income.setCustomLabels(fetchCustomLabelsByNames(customLabelNames));
        return incomeRepository.save(income);
    }

    @Transactional
    public String deleteIncome(String email, Long id) {
        Income income = incomeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Income with ID " + id + " not found"));
        if (!income.getUserInfo().getEmail().equals(email)) {
            throw new IllegalArgumentException("Unauthorized to delete this expense");
        }
        incomeRepository.delete(income);
        return "Income with ID " + id + " deleted";
    }

    @Transactional
    public Income updateIncome(String email, Long id, BigDecimal amount, String description, LocalDate date, List<String> customLabelNames) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Income with ID " + id + " not found"));
        if (!income.getUserInfo().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this expense");
        }

        // Update basic fields
        income.setAmount(amount);
        income.setDescription(description);
        income.setDate(date);
        income.setCustomLabels(fetchCustomLabelsByNames(customLabelNames));

        return incomeRepository.save(income);
    }

    private Set<CustomLabel> fetchCustomLabelsByNames(List<String> customLabelNames) {
        return customLabelNames.stream()
                .map(customLabelRepository::findByName)
                .collect(Collectors.toSet());
    }

}
