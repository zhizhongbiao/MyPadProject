package com.dcamp.pad.ui.platform.bean;

import com.easyder.wrapper.model.BaseVo;

/**
 * Auther   : ZZB
 * Date     : 2017/7/4
 * Desc     :
 */

public class LikeBean extends BaseVo {


    /**
     * code : 0
     * msg : 点赞成功
     * data : {"info":{"curGoodsNum":4}}
     */

    public int code;
    public String msg;

        /**
         * info : {"curGoodsNum":4}
         */

        public InfoBean info;

        public static class InfoBean {
            /**
             * curGoodsNum : 4
             */

            public int curGoodsNum;

    }
}
