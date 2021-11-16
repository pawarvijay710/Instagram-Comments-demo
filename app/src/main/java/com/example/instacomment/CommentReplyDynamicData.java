package com.example.instacomment;

public class CommentReplyDynamicData {
    private Integer nid = -1;
    private Boolean favoriteSatus = false;
    private Integer favoriteCount = 0;

    public Integer getNid() {
        return nid;
    }

    public void setNid(Integer nid) {
        this.nid = nid;
    }

    public Boolean getFavoriteSatus() {
        return favoriteSatus;
    }

    public void setFavoriteSatus(Boolean favoriteSatus) {
        this.favoriteSatus = favoriteSatus;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
