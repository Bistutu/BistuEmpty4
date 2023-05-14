package com.thinkstu.timer;

import com.thinkstu.compus.*;
import com.thinkstu.utils.*;
import lombok.extern.slf4j.*;
import okhttp3.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.concurrent.*;


/**
 * @author : ThinkStu
 * @since : 2022/9/20, 1:25 PM, 周二
 **/

@Slf4j
@Component
public class TimeToCatch {
    OkHttpClient client;
    PathInitial path;
    CookieUtils cookieUtils;
    ExecutorService executor;

    A a;
    B b;
    C c;
    D d;

    public TimeToCatch(OkHttpClient client, PathInitial path, CookieUtils cookieUtils, ExecutorService executor, A a, B b, C c, D d) {
        this.client = client;
        this.path = path;
        this.cookieUtils = cookieUtils;
        this.executor = executor;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    @Scheduled(initialDelay = 1_000, fixedRate = 10800_000)
    // 每天 6 点、12 点各执行一次
//    @Scheduled(cron = "0 20 6,12 * * ?")
    private void crawl() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 5; i++) {
            now = now.plus(i, ChronoUnit.DAYS);
            String yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(now);
            String MM_DD      = DateTimeFormatter.ofPattern("Md").format(now);
            executor.submit(() -> {
                try {
                    a.fetch(yyyy_MM_dd, MM_DD);
                    b.fetch(yyyy_MM_dd, MM_DD);
                    c.fetch(yyyy_MM_dd, MM_DD);
                    d.fetch(yyyy_MM_dd, MM_DD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
