package com.koh.feign;

import com.koh.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "koh-user", url = "http://koh-user:8080")
public interface UserFeign {

    @GetMapping("/api/users/{id}")
    User getUserById(@PathVariable Long id);
}
