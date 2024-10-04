package uz.uzum.register;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.uzum.register.dto.RegRequest;
import uz.uzum.register.model.UserInfo;
import uz.uzum.register.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserInfo registerUser(RegRequest regRequest) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(regRequest.getUsername());
        userInfo.setPassword(passwordEncoder.encode(regRequest.getPassword()));
        userInfo.setEmail(regRequest.getEmail());
        return userRepository.save(userInfo);
    }

}
