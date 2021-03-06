package com.dudu.voice.semantic.chain;


import android.text.TextUtils;

import com.dudu.android.launcher.utils.Constants;
import com.dudu.map.NavigationProxy;
import com.dudu.navi.vauleObject.SearchType;
import com.dudu.voice.semantic.bean.SemanticBean;

/**
 * Created by lxh on 2015/11/10.
 */
public class NavigationDefaultChain extends DefaultChain {

    @Override
    public boolean matchSemantic(String service) {
        return true;
    }

    @Override
    public boolean doSemantic(SemanticBean semantic) {
        if (semantic != null && !TextUtils.isEmpty(semantic.getText())) {
            NavigationProxy.getInstance().searchControl(semantic.getText(), SearchType.SEARCH_PLACE);
        } else {
            mVoiceManager.startSpeaking(Constants.UNDERSTAND_MISUNDERSTAND);
        }
        return true;
    }

}
