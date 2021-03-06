package com.dudu.workflow.tpms;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TPMSInfo {
    public enum POSITION {
        RIGHT_FRONT(1), LEFT_FRONT(2), RIGHT_BACK(3), LEFT_BACK(4), UNKNOW(0xff);
        private int value;

        POSITION(int i) {
            value = i;
        }

        public int value() {
            return value;
        }

        public static POSITION valueOf(int i) {
            switch (i) {
                case 1:
                    return RIGHT_FRONT;
                case 2:
                    return LEFT_FRONT;
                case 3:
                    return RIGHT_BACK;
                case 4:
                    return LEFT_BACK;
                default:
                    return UNKNOW;
            }
        }
    }

    public int postion;   //轮胎位置
    public int sensorID;  //传感器ID
    public float pressure; //压力 Bar
    public int temperature; //温度 摄氏度
    public int gasLeaks; //0:正常, 1:急漏气, 2:正常, 3:加气
    public boolean battery; //0:正常, 1:低压
    public boolean noData;  //0:正常, 1:30分钟未收到发射器到数据
    public boolean barometerHigh; //气压高
    public boolean barometerLow;  //气压低
    public boolean temperatureHigh; //0:正常, 1:温度高

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("轮胎位置:");
        sb.append(postion);
        sb.append(", 传感器ID:");
        sb.append(Integer.toHexString(sensorID));
        sb.append(", 压力:");
        sb.append(new BigDecimal(pressure).setScale(3, RoundingMode.HALF_UP));
        sb.append(" Bar, 温度:");
        sb.append(temperature);
        sb.append(", 电池低压:");
        sb.append(battery);
        sb.append(", 无数据:");
        sb.append(noData);
        sb.append(", 气压高:");
        sb.append(barometerHigh);
        sb.append(", 气压低:");
        sb.append(barometerLow);
        sb.append(", 温度高:");
        sb.append(temperatureHigh);
        sb.append(", 漏气:");
        switch (gasLeaks) {
            case 0:
            case 2:
                sb.append("正常");
                break;
            case 1:
                sb.append("急漏气");
                break;
            case 3:
                sb.append("加气");
                break;
        }
        sb.append("\n");
        return sb.toString();
    }


    public int getPostion() {
        return postion;
    }

    public int getSensorID() {
        return sensorID;
    }

    public float getPressure() {
        return pressure;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getGasLeaks() {
        return gasLeaks;
    }

    public boolean isBattery() {
        return battery;
    }

    public boolean isNoData() {
        return noData;
    }

    public boolean isBarometerHigh() {
        return barometerHigh;
    }

    public boolean isBarometerLow() {
        return barometerLow;
    }

    public boolean isTemperatureHigh() {
        return temperatureHigh;
    }
}
