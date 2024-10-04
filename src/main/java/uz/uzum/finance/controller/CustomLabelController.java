package uz.uzum.finance.controller;

import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.uzum.finance.model.CustomLabel;
import uz.uzum.finance.service.CustomLabelService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomLabelController {

    private final CustomLabelService customLabelService;

    @MutationMapping
    public CustomLabel createCustomLabel(@Argument String name,
                                         @Argument String color) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            System.out.println("Received email: " + email);
            // Your business logic here
        } else {
            System.out.println("ServletRequestAttributes is null");
            // Handle the case where attributes are null
        }

        return customLabelService.createCustomLabel(name, color);

    }

    @MutationMapping
    public CustomLabel updateCustomLabel(@Argument Long id,
                                         @Argument String name,
                                         @Argument String color) {
        return customLabelService.updateCustomLabel(id, name, color);
    }

    @MutationMapping
    public Void deleteCustomLabel(@Argument Long id) {
        return customLabelService.deleteCustomLabel(id);
    }

    @QueryMapping
    public List<CustomLabel> getAllCustomLabels() {
        return customLabelService.getAllCustomLabels();
    }

}
