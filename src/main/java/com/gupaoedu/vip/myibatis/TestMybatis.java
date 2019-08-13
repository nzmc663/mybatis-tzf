package com.gupaoedu.vip.myibatis;


import com.gupaoedu.vip.myibatis.mapper.Blog;
import com.gupaoedu.vip.myibatis.mapper.BlogMapper;
import com.gupaoedu.vip.myibatis.session.SqlSession;
import com.gupaoedu.vip.myibatis.session.SqlSessionFactory;

public class TestMybatis {

    public static void main(String[] args) {
        SqlSessionFactory factory = new SqlSessionFactory();
        SqlSession sqlSession = factory.build().openSqlSession();
        // 获取MapperProxy代理
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = mapper.selectBlogById(1);

        System.out.println("第一次查询: " + blog);
        System.out.println();
        blog = mapper.selectBlogById(1);
        System.out.println("第二次查询: " + blog);
    }
}
