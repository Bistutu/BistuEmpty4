package com.thinkstu.controller;

import com.thinkstu.utils.*;
import lombok.extern.slf4j.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;


/**
 * @author : ThinkStu
 * @since : 2023/5/14, 16:29, 周日
 **/
@Slf4j
@RestController
public class MonitorController {

    Integer count = 1;
    PathInitial pathInitial;

    public MonitorController(PathInitial pathInitial) {
        this.pathInitial = pathInitial;
    }

    @GetMapping("/{campus}/{forward}.json")
    String get1(@PathVariable("campus") String campus, @PathVariable("forward") String forward) {
        log.info("===》第 {} 次访问，目标：{}", count++, forward);    // 访问自增1
        String path = pathInitial.getPath() + campus + File.separator + forward + ".json";
        return new cn.hutool.core.io.file.FileReader(path).readString();
    }

    @GetMapping("/empty/{campus}/{forward}.json")
    String get2(@PathVariable("campus") String campus, @PathVariable("forward") String forward) {
        log.info("===》第 {} 次访问，目标：{}", count++, forward);    // 访问自增1
        String path = pathInitial.getPath() + campus + File.separator + forward + ".json";
        return new cn.hutool.core.io.file.FileReader(path).readString();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    void refresh() {
        count = 1;
    }
}