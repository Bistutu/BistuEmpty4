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
public class D {
    PathInitial path;
    RequestUtils requestUtils;
    GetFormat getFormat;


    public D(PathInitial path, RequestUtils requestUtils, GetFormat getFormat) {
        this.path = path;
        this.requestUtils = requestUtils;
        this.getFormat = getFormat;
    }

    public void fetch(String yyyyMmDd, String md) {
        int KSJC = 0, JSJC = 0;

        int time = 0; // time是时段
        try (PrintWriter printWriter = new PrintWriter(path.getPath() + "/4/4" + md + ".json")) {
            StringBuilder sb = new StringBuilder();
            time = 0;
            sb.append("[");
            while (time < 1000)
                try {
                    time = getFormat.time(time, sb, KSJC, JSJC);
                    KSJC = getFormat.K;
                    JSJC = getFormat.J;
                    ParamEntity param = new ParamEntity(yyyyMmDd, 10, KSJC, JSJC);
                    String      data  = requestUtils.post(param);
                    int         begin = data.indexOf("rows") + 6;
                    int         end   = data.lastIndexOf("]");
                    data = data.substring(begin, end + 1);
                    //
                    List<EmptyEntity> userList = JSON.parseArray(data, EmptyEntity.class); // userList是原始JSON的对象数组
                    // emptyClassArray是为了排除干扰和排序
                    ArrayList<String> emptyClassArray = new ArrayList<>();
                    String            emptyClass;
                    for (EmptyEntity assist : userList) {
                        emptyClass = assist.getJASMC().substring(0, assist.getJASMC().length());
                        if (!emptyClass.matches(".*报.*")
                                // 开始新的过滤，WLA、WLC，这里只列出特例，其余的教务网自行判断了
                                && !emptyClass.matches(".*WLA-(10[123]|30[1259]).*")
                                && !emptyClass.matches(".*WLC-112.*")
                        ) {
                            // assist是原始JSON的对象
                            emptyClassArray.add(emptyClass);
                        }
                    }
//                        if (time == 0) {
//                            sb.append("{\"a\":\"1\",\"b\":\"" + "试试我们的新应用？微信搜索小程序：Bis兔洞\",\"c\":\"1\",\"d\":\"\"},");
//                        }

                    // 排序
                    Collections.sort(emptyClassArray);
                    getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*WLA.*", "文理楼A\uD83D\uDCD4", 4);
                    getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*WLB.*", "文理楼B\uD83D\uDCD4", 4);
                    getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*WLC.*", "文理楼C\uD83D\uDCD4", 4);

                    // TODO 2023-2-25号新增以下内容
                    getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*XXA.*", "信息楼A\uD83D\uDCBB", 4);
                    getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*XXB.*", "信息楼B\uD83D\uDCBB", 4);
                    getFormat.start(sb, KSJC, JSJC, time, emptyClassArray, ".*XXC.*", "信息楼C\uD83D\uDCBB", 4);
                    getFormat.start_no(sb, KSJC, JSJC, time, emptyClassArray, ".*XXD.*", "信息楼D\uD83D\uDCBB", 4);

                    time = getFormat.switchTime(time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            sb.append("]");
            printWriter.print(sb);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
