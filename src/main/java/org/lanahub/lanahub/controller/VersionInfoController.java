package org.lanahub.lanahub.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/version-info")
public class VersionInfoController {

    @Value("${app.api.version}")
    private String version;

    @Value("${app.env}")
    private String env;

    @GetMapping
    public String info() {
        return "Running API version: " + version + " on environment: " + env;
    }
}
