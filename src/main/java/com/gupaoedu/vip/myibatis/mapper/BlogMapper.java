package com.gupaoedu.vip.myibatis.mapper;

import com.gupaoedu.vip.myibatis.annotation.Entity;
import com.gupaoedu.vip.myibatis.annotation.Select;
/**
 * mapper接口
 *
 * @author tzf
 */
@Entity(Blog.class)
public interface BlogMapper {
    /**
     * 根据主键查询文章
     */
    @Select("select * from blog where bid = ?")
    Blog selectBlogById(Integer bid);

}

