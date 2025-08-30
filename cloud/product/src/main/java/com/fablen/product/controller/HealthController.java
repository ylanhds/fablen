package com.fablen.product.controller;

import com.fablen.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/actuator/health")
    public Result health() {
        return Result.success("UP");
    }
}
