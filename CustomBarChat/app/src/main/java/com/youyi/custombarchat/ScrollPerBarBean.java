package com.youyi.custombarchat;

import java.io.Serializable;

/**
 * Created by Roy on 16/10/14.
 */
public class ScrollPerBarBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private float ratio;
    /**
     * X轴坐标
     */
    private String xIndexStr;
    /**
     * 柱形图实际高度
     */
    private int actualHeight;
    private boolean flag;// 记录是不是点击状态
    private int leftX, rightX, topY;// 记录左右上边距

    public ScrollPerBarBean() {
    }

    public ScrollPerBarBean(String xIndexStr, float ratio, boolean flag) {
        this.xIndexStr = xIndexStr;
        this.ratio = ratio;
        this.flag = flag;
    }


    public String getxIndexStr() {
        return xIndexStr;
    }

    public void setxIndexStr(String xIndexStr) {
        this.xIndexStr = xIndexStr;
    }

    public int getActualHeight() {
        return actualHeight;
    }

    public void setActualHeight(int actualHeight) {
        this.actualHeight = actualHeight;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getLeftX() {
        return leftX;
    }

    public void setLeftX(int leftX) {
        this.leftX = leftX;
    }

    public int getRightX() {
        return rightX;
    }

    public void setRightX(int rightX) {
        this.rightX = rightX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }
}
