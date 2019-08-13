package com.gupaoedu.vip.myibatis.mapper;
/**
 * blog实体类
 *
 * @author tzf
 */
public class Blog {
    /**
     * 文章id
     */
    private Integer bid;
    /**
     * 文章标题
     */
    private String name;
    /**
     * 文章作者id
     */
    private Integer authorId;

    public Integer getBid() {
        return bid;
    }

    public void setBid(Integer bid) {
        this.bid = bid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "bid=" + bid +
                ", name='" + name + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
