package com.koh.feign;

import com.koh.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "koh-user")
public interface UserFeign {

    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable Long id);
}
