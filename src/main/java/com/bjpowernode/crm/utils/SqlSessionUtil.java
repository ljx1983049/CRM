package com.bjpowernode.crm.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {

    //构造方法私有化
    private SqlSessionUtil(){}

    private static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    private static ThreadLocal<SqlSession> t = new ThreadLocal<SqlSession>();

    //取得SqlSession对象
    public static SqlSession getSqlSession(){
        SqlSession session = t.get();

        if (session==null){
            session = sqlSessionFactory.openSession();
            t.set(session);
        }

        return session;
    }

    public static void myClose(SqlSession session){
        if (session != null) {
            session.close();
            t.remove();
        }
    }

}
