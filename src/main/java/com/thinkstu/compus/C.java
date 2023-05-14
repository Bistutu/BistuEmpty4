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
public class C {
    PathInitial path;
    RequestUtils requestUtils;
    GetFormat getFormat;


    public C(PathInitial path, RequestUtils requestUtils, GetFormat getFormat) {
        this.path = path;
        this.requestUtils = requestUtils;
        this.getFormat = getFormat;
    }

    public void fetch(String yyyyMmDd, String md) {
        int KSJC = 0, JSJC = 0;
        int time = 0; // time是时段

        try (PrintWriter printWriter = new PrintWriter(path.getPath() + "/3/3" + md + ".json");) {
            StringBuilder sb = new StringBuilder();
            time = 0;
            sb.append("[");
            while (time < 1000) {
                time = getFormat.time(time, sb, KSJC, JSJC);
                KSJC = getFormat.K;
                JSJC = getFormat.J;

                ParamEntity param = new ParamEntity(yyyyMmDd, 3, KSJC, JSJC);
                String      data  = requestUtils.post(param);
                int         begin = data.indexOf("rows") + 6;
                int         end   = data.lastIndexOf("]");
                data = data.substring(begin, end + 1); // data是初始的的JSON数组
                List<EmptyEntity> userList = JSON.parseArray(data, EmptyEntity.class); // userList是原始JSON的对象数组
                // emptyClassArray是为了排除干扰和排序
                ArrayList<String> emptyClassArray = new ArrayList<String>();
                for (EmptyEntity assist : userList) { // assist是原始JSON的对象
                    emptyClassArray.add(assist.getJASMC().substring(2, assist.getJASMC().length()));
                }
                // 排序
                Collections.sort(emptyClassArray);

//                    if (time == 0) {
//                        sb.append("{\"a\":\"1\",\"b\":\"" + "试试我们的新应用？微信搜索小程序：Bis兔洞\",\"c\":\"1\",\"d\":\"\"},");
//                    }
                getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*1-.*", "第一教学楼", 2);
                getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*2-.*", "第二教学楼", 2);
                getFormat.start_no(sb, KSJC, JSJC, time, emptyClassArray, ".*3-.*", "第三教学楼", 2);
                time = getFormat.switchTime(time);
            }
            sb.append("]");
            printWriter.print(sb);
            // 日志记录
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


