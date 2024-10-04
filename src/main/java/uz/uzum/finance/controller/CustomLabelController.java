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
            return customLabelService.createCustomLabel(email, name, color);
        } else {
            throw new IllegalArgumentException("User not found");
        }

    }

    @MutationMapping
    public CustomLabel updateCustomLabel(@Argument Long id,
                                         @Argument String name,
                                         @Argument String color) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return customLabelService.updateCustomLabel(email, id, name, color);
        } else {
            throw new IllegalArgumentException("Do not have authorization");
        }
    }

    @MutationMapping
    public String deleteCustomLabel(@Argument Long id) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return customLabelService.deleteCustomLabel(email, id);
        } else {
            throw new IllegalArgumentException("Do not have authorization");
        }
    }

    @QueryMapping
    public List<CustomLabel> getAllCustomLabels() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String email = request.getHeader("X-User-Email");
            return customLabelService.getAllCustomLabels(email);
        } else {
            throw new IllegalArgumentException("Do not have authorization");
        }
    }

}
