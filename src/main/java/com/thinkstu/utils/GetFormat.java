package com.thinkstu.utils;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

/**
 * @author: ThinkStu
 * @create: 2021-12-11 6:18 下午
 * @function: 格式化操作
 */
@Component
public class GetFormat {
    int paraTransfer = 0, colorNumber = 0;
    char para;
    int inline = 0;
    // K is KSJC     J is JSJC
    public int K, J;

    public void start(StringBuilder json, int KSJC, int JSJC, int time,
                      ArrayList<String> emptyClassArray, String regex, String assist, int cut) {
        int inline = 0;
        color(time);
        // 开始阶梯教室
        json.append("{\"a\":\"1\",\"b\":\"").append(assist).append("(").append(KSJC).append("~").append(JSJC).append("节)\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"},");
        int ladderNumber = 0;
        paraTransfer = 0;
        for (String emptyNumber : emptyClassArray) {

            if (paraTransfer == 4) {
                paraTransfer = 0;
            }
            para = (char) ('a' + paraTransfer);
            if (emptyNumber.matches(regex)) {
                ladderNumber++;
                if (cut != 0) {
                    emptyNumber = emptyNumber.substring(cut);
                }
                if (inline == 0) {
                    json.append("{");
                }
                if (inline == 3) {
                    json.append("\"").append(para).append("\":").append("\"").append(emptyNumber).append("\"").append("},");
                    inline = 0;
                } else {
                    json.append("\"").append(para).append("\":").append("\"").append(emptyNumber).append("\"").append(",");
                    inline++;
                }
                paraTransfer++;
            }
        }
        switch (inline) {
            case 1 -> json.append("\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
            case 2 -> json.append("\"c\":\"\",\"d\":\"\"},");
            case 3 -> json.append("\"d\":\"\"},");
        }
        if (ladderNumber == 0) {
            json.append("{\"a\":\"无\",\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
        }
    }


    //
    public void start_no(StringBuilder json, int KSJC, int JSJC, int time,
                         ArrayList<String> emptyClassArray, String regex, String assist, int cut) {
        color(time);
        json.append("{\"a\":\"1\",\"b\":\"").append(assist).append("(").append(KSJC).append("~").append(JSJC).append("节)\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"},");
        paraTransfer = 0;
        int ladderNumber = 0;
        inline = 0;
        for (String emptyNumber : emptyClassArray) {
            if (emptyNumber.matches(regex)) {
                ladderNumber++;
                if (cut != 0) {
                    emptyNumber = emptyNumber.substring(cut);
                }
                if (paraTransfer == 4) {
                    paraTransfer = 0;
                }
                para = (char) ('a' + paraTransfer);
                if (inline == 0) {
                    json.append("{");
                }
                if (inline == 3) {
                    json.append("\"").append(para).append("\":").append("\"").append(emptyNumber).append("\"").append("},");
                    inline = 0;
                } else {
                    json.append("\"").append(para).append("\":").append("\"").append(emptyNumber).append("\"").append(",");
                    inline++;
                }
                paraTransfer++;
            }
        }

        switch (inline) {
            case 0 -> {
                if (ladderNumber == 0) {
                    json.append("{\"a\":\"无\",\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
                }
                if (time == 3) {
                    json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"}");
                } else {
                    if (time == 135 || time == 289 || time == 0) {
                        json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"" + colorNumber + "\",\"d\":\"\"},");
                    }
                }
            }
            case 1 -> {
                json.append("\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
                if (ladderNumber == 0) {
                    json.append("{\"a\":\"无\",\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
                }
                if (time == 3) {
                    json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"}");
                } else {
                    if (time == 135 || time == 289 || time == 0) {
                        json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"},");
                    }
                }
            }
            case 2 -> {
                json.append("\"c\":\"\",\"d\":\"\"},");
                if (ladderNumber == 0) {
                    json.append("{\"a\":\"无\",\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
                }
                if (time == 3) {
                    json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"}");
                } else {
                    if (time == 135 || time == 289 || time == 0) {
                        json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"},");
                    }
                }
            }
            case 3 -> {
                json.append("\"d\":\"\"},");
                if (ladderNumber == 0) {
                    json.append("{\"a\":\"无\",\"b\":\"\",\"c\":\"\",\"d\":\"\"},");
                }
                if (time == 3) {
                    json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"}");
                } else {
                    if (time == 135 || time == 289 || time == 0) {
                        json.append("{\"a\":\"1\",\"b\":\"到底了~\",\"c\":\"").append(colorNumber).append("\",\"d\":\"\"},");
                    }
                }
            }
        }
    }


    public int time(int time, StringBuilder json, int KSJC, int JSJC) {
        if (time == 0 || time == 1 || time == 2 || time == 3) {
            json.append("{\"a\":\"\",\"b\":\"\",\"c\":\"\",\"d\":\"").append(time).append("\"},");
        }
        if (time == 0) {
            KSJC = 1;
            JSJC = 12;
        }
        if (time == 1) {
            KSJC = 1;
            JSJC = 5;
        }
        if (time == 2) {
            KSJC = 6;
            JSJC = 9;
        }
        if (time == 3) {
            KSJC = 10;
            JSJC = 12;
        }
        if (time == 112) {
            KSJC = 1;
            JSJC = 2;
        }
        if (time == 135) {
            KSJC = 3;
            JSJC = 5;
        }
        if (time == 267) {
            KSJC = 6;
            JSJC = 7;
        }
        if (time == 289) {
            KSJC = 8;
            JSJC = 9;
        }
        K = KSJC;
        J = JSJC;
        return time;
    }

    public void color(int time) {
        switch (time) {
            case 0, 1, 2, 3 -> colorNumber = 0;
            case 112, 267 -> colorNumber = 1;
            case 135, 289 -> colorNumber = 2;
        }
    }

    ;

    public int switchTime(int time) {
        switch (time) {
            case 0 -> time = 1;
            case 1 -> time = 112;
            case 112 -> time = 135;
            case 135 -> time = 2;
            case 2 -> time = 267;
            case 267 -> time = 289;
            case 289 -> time = 3;
            case 3 -> time = 1000;
        }
        return time;
    }

}
