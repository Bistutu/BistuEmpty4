package com.thinkstu.compus;


import com.alibaba.fastjson2.*;
import com.thinkstu.entity.*;
import com.thinkstu.utils.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Slf4j
@Component
public class B {
    PathInitial path;
    RequestUtils requestUtils;
    GetFormat getFormat;


    public B(PathInitial path, RequestUtils requestUtils, GetFormat getFormat) {
        this.path = path;
        this.requestUtils = requestUtils;
        this.getFormat = getFormat;
    }

    public void fetch(String yyyyMmDd, String md) {
        int KSJC = 0, JSJC = 0;
        int time = 0; // time是时段
        try (PrintWriter printWriter = new PrintWriter(path.getPath() + "/2/2" + md + ".json")) {
            StringBuilder sb = new StringBuilder();
            time = 0;
            int colorNumber = 0;
            sb.append("[");
            while (time < 1000) {
                time = getFormat.time(time, sb, KSJC, JSJC);
                KSJC = getFormat.K;
                JSJC = getFormat.J;
                // 构造请求参数，发送请求，获得数据
                ParamEntity                                           param    = new ParamEntity(yyyyMmDd, 2, KSJC, JSJC);
                String                                                data     = requestUtils.post(param);
                EmptyResultEntity                                     result   = JSON.parseObject(data, EmptyResultEntity.class);
                List<EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean> userList = result.getDatas().getCxkxjs().getRows();
                String                                                emptyClass;
                // emptyClassArray是为了排除干扰和排序
                ArrayList<String> emptyClassArray = new ArrayList<String>();
                for (EmptyEntity assist : userList) { // assist是原始JSON的对象
                    emptyClass = assist.getJASMC().substring(2).replaceAll("\\(.*?\\)", "");// emptyClass是教室名称
                    emptyClass = emptyClass.replaceAll("3-七阶梯", "7七阶梯");
                    emptyClass = emptyClass.replaceAll("3-六阶梯", "6六阶梯");
                    emptyClass = emptyClass.replaceAll("3-五阶梯", "5五阶梯");
                    emptyClass = emptyClass.replaceAll("1-四阶梯", "4四阶梯");
                    emptyClass = emptyClass.replaceAll("1-三阶梯", "3三阶梯");
                    emptyClass = emptyClass.replaceAll("1-二阶梯", "2二阶梯");
                    emptyClass = emptyClass.replaceAll("1-一阶梯", "1一阶梯");
                    if (emptyClass.matches(".*2-.*") && !emptyClass.matches(".*102.*")
                            && !emptyClass.matches(".*108.*")
                            && !emptyClass.matches(".*303.*") && !emptyClass.matches(".*407.*")) {
                        emptyClassArray.add(emptyClass);
                    }
                }
                // 排序
                Collections.sort(emptyClassArray);
                // 至此，emptyClassArray里面均为阶梯教室或者二教教室

//                    if (time == 0) {
//                        sb.append("{\"a\":\"1\",\"b\":\"" + "试试我们的新应用？微信搜索小程序：Bis兔洞\",\"c\":\"1\",\"d\":\"\"},");
//                    }
                // 函数化的操作方式
                getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*阶梯.*", "阶梯教室", 1);
                getFormat.start_no(sb, KSJC, JSJC, time, emptyClassArray, ".*2-.*", "第二教学楼", 2);
                time = getFormat.switchTime(time);
            }
            sb.append("]");
            printWriter.print(sb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


