package com.dcamp.pad.ui.basic.bean;

import com.easyder.wrapper.model.BaseVo;

import java.util.List;

/**
 * Auther:  winds
 * Data:    2017/7/8
 * Desc:
 */

public class CateVo extends BaseVo{

    public List<ListBean> list;

    public static class ListBean {
        public int id;
        public String name;
        public String no;
        public int timeDownLimit;
    }
}
