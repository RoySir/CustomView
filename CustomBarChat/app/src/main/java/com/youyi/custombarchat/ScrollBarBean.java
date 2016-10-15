package com.youyi.custombarchat;

import java.util.List;

/**
 * Created by Roy on 16/10/14.
 */
public class ScrollBarBean {
    private int total;
    List<ScrollPerBarBean> lists;

    public List<ScrollPerBarBean> getLists() {
        return lists;
    }

    public void setLists(List<ScrollPerBarBean> lists) {
        this.lists = lists;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
