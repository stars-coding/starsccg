package com.stars.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康控制器
 *
 * @author stars
 * @version 1.0.0
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @GetMapping
    public String healthCheck() {
        return "ok";
    }
}
