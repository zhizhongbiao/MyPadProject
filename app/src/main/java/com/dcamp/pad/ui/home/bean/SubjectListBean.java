package com.dcamp.pad.ui.home.bean;

import com.easyder.wrapper.model.BaseVo;

import java.util.List;

/**
 * Auther   : ZZB
 * Date     : 2017/7/4
 * Desc     : 首页专题活动bean
 */

public class SubjectListBean extends BaseVo {


    /**
     * code : 0
     * msg :
     * data : {"list":[{"id":2,"name":"内衣饰品","no":"01"},{"id":3,"name":"母婴童装","no":"02"}]}
     */

    public int code;
    public String msg;


    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 2
         * name : 内衣饰品
         * no : 01
         */

        public int id;
        public String name;
        public String no;

    }
}
