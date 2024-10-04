package uz.uzum.finance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.uzum.finance.model.CustomLabel;
import uz.uzum.finance.model.UserInfo;
import uz.uzum.finance.repository.CustomLabelRepository;
import uz.uzum.finance.repository.UserInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomLabelService {

    private final CustomLabelRepository customLabelRepository;
    private final UserInfoRepository userInfoRepository;

    public CustomLabel createCustomLabel(String email, String name, String color){

        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        CustomLabel customLabel = new CustomLabel();
        customLabel.setUserInfo(userInfo);
        customLabel.setName(name);
        customLabel.setColor(color);
        return customLabelRepository.save(customLabel);
    }

    public CustomLabel updateCustomLabel(String email, Long id, String name, String color){
        CustomLabel customLabel = customLabelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Label with id " + id + " not found"));

        if (!customLabel.getUserInfo().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this CustomLabel");
        }

        customLabel.setName(name);
        customLabel.setColor(color);
        return customLabelRepository.save(customLabel);
    }

    public String deleteCustomLabel(String email, Long id){
        CustomLabel customLabel = customLabelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Label with id " + id + " not found"));

        if (!customLabel.getUserInfo().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this CustomLabel");
        }

        customLabelRepository.delete(customLabel);
        return "Custom label with id " + id + " deleted successfully";
    }

    public List<CustomLabel> getAllCustomLabels(String email){
        return customLabelRepository.findByUserInfo_Email(email);
    }

}