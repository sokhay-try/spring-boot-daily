package com.springbootdaily.Specifications;

import com.springbootdaily.entities.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserSpecification {

    public static Specification<User> buildSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter out specific parameters
            Map<String, String> filteredParams = params.entrySet().stream()
                    .filter(entry -> !entry.getKey().equals("pageSize") && !entry.getKey().equals("page"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            System.out.println(">>>filteredParams: " + filteredParams);

            // search by q. Ex: api/users?q=abc
            if (filteredParams.get("q") != null) {
                filteredParams = filteredParams.entrySet().stream().filter(entry -> entry.getKey().equals("q"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            for (Map.Entry<String, String> entry : filteredParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (StringUtils.hasLength(value)) {
                    if(Objects.equals(key, "q")) {
                        predicates.add(criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + value.toLowerCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + value.toLowerCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + value.toLowerCase() + "%"),
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + value.toLowerCase() + "%")
                        ));
                    }
                    else {
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                    }

                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));


        };
    }
}
