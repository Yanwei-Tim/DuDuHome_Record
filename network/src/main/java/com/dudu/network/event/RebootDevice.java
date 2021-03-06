package com.dudu.network.event;

import com.dudu.network.d01code.message.MessagePackage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dengjun on 2015/12/2.
 * Description :  重启设备
 */
public class RebootDevice extends MessagePackage {
    private String messageId;
    private String method;

    private String reboot;

    private boolean rebootDeviceFlag = false;

    @Override
    public void setMessageId(String messageId) {

    }

    @Override
    public String getMessageId() {
        return messageId;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public boolean isNeedWaitResponse() {
        return false;
    }

    @Override
    public boolean isNeedEncrypt() {
        return false;
    }

    @Override
    public void createFromJsonString(String messageJsonString) {
        try {
            JSONObject jsonObject = new JSONObject(messageJsonString);
            messageId = jsonObject.getString("messageId");
            method = jsonObject.getString("method");
            reboot=jsonObject.getString("reboot");
          if (reboot.equals("1")){
             rebootDeviceFlag= true;
          }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public boolean isNeedCache() {
        return false;
    }

    public boolean isRebootDeviceFlag() {
        return rebootDeviceFlag;
    }
}
