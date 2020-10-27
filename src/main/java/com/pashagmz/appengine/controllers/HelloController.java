package com.pashagmz.appengine.controllers;

import com.pashagmz.appengine.models.entity.HistoryLog;
import com.pashagmz.appengine.repositories.HistoryLogRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/history-log")
public class HelloController {

    private final HistoryLogRepository repository;

    public HelloController(HistoryLogRepository repository) {
        this.repository = repository;
    }

//    @Value("${message}")
//    private String message;

    @GetMapping
    public List<String> getAll() {
        return repository.findAll().stream().map(HistoryLog::getIp).collect(Collectors.toList());

    }

    @PostMapping
    public void add(HttpServletRequest request) {

        HistoryLog entity = new HistoryLog();
        entity.setIp(request.getRemoteAddr());
        entity.setCreateUtc(new Date());

        repository.save(entity);
    }
}
