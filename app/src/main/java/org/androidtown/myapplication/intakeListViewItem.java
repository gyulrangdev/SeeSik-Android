package org.androidtown.myapplication;

/**
 * Created by sohyeon on 2017-05-17.
 */

public class intakeListViewItem {
    private String ItemNameStr;
    private int ItemNum;
    private boolean starLiked;

    public String getItemNameStr() { return ItemNameStr;
    }

    public void setItemNameStr(String itemNameStr) {
        ItemNameStr = itemNameStr;
    }

    public void setStarLiked(boolean starliked) {
        starLiked= starliked;
    }

    public int getItemNum() {
        return ItemNum;
    }

    public void setItemNum(int itemNum) {
        ItemNum = itemNum;
    }

    public boolean getStarLiked() {return starLiked;}

}
