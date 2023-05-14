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
public class A {
    PathInitial path;
    RequestUtils requestUtils;
    GetFormat getFormat;

    public A(PathInitial path, RequestUtils requestUtils, GetFormat getFormat) {
        this.path = path;
        this.requestUtils = requestUtils;
        this.getFormat = getFormat;
    }

    public void fetch(String yyyyMmDd, String md) {
        int KSJC = 0, JSJC = 0;
        int time = 0; // time是时段
        // 但凡发生一点意外，都不应该更新
        try (PrintWriter printWriter = new PrintWriter(path.getPath() + "/1/1" + md + ".json");) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            while (time < 1000) {
                time = getFormat.time(time, sb, KSJC, JSJC);
                KSJC = getFormat.K;
                JSJC = getFormat.J;
                // 获取数据
                ParamEntity                                           param    = new ParamEntity(yyyyMmDd, 1, KSJC, JSJC);
                String                                                data     = requestUtils.post(param);
                EmptyResultEntity                                     result   = JSON.parseObject(data, EmptyResultEntity.class);
                List<EmptyResultEntity.DatasBean.CxkxjsBean.RowsBean> userList = result.getDatas().getCxkxjs().getRows();
                String                                                emptyClass;
                // emptyClassArray是为了排除干扰和排序
                ArrayList<String> emptyClassArray = new ArrayList<String>();
                for (EmptyEntity assist : userList) { // assist是原始JSON的对象
                    emptyClass = assist.getJASMC().substring(2);// emptyClass是教室名称
                    emptyClassArray.add(emptyClass);
                }

                // 排序
                Collections.sort(emptyClassArray);
                // 写入一行“advertise”
//                    if (time == 0) {
//                        sb.append("{\"a\":\"1\",\"b\":\"" + "试试我们的新应用？微信搜索小程序：Bis兔洞\",\"c\":\"1\",\"d\":\"\"},");
//                    }
                // time的跳转关系
                getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*1-.*", "第一教学楼", 2);
                getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*2-.*", "第二教学楼", 2);
                getFormat.start_no(sb, KSJC, JSJC, time, emptyClassArray, ".*4-.*", "第四教学楼", 2);
                time = getFormat.switchTime(time);
            }
            sb.append("]");
            printWriter.print(sb);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
