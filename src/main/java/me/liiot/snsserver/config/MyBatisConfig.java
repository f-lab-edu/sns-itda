package me.liiot.snsserver.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/*
 @Configuration
 : 클래스가 하나 이상의`@Bean` 메소드를 선언하고, 런타임시 해당 Bean을 정의하거나 서비스
   요청을 생성하기 위해 Spring 컨테이너에서 처리 될 수 있음을 명시

 @Mapperscan
 : 클래스패스를 지정해서 마이바티스 스프링 연동모듈의 자동 스캔 기능을 사용하여 모든 매퍼를 등록
    * basePackages: 매퍼를 검색할 패키지 지정
    * 매퍼 인터페이스 파일이 있는 가장 상위 패키지를 지정하면 지정된 패키지에서 하위 패키지를 모두 검색

 @PropertySource
 : 지정한 위치의 프로퍼티 파일을 읽어 스프링 Environment 오브젝트에 저장
   @Configuration 클래스와 함께 사용

 @Autowired
 : 필요한 의존 객체의 타입에 해당하는 빈을 찾아 주입

 @Bean
 : 대상 메소드가 Spring 컨테이너가 관리 할 Bean을 생성함을 명시
 */
@Configuration
@MapperScan(basePackages = "me.liiot.snsserver.mapper")
@PropertySource("classpath:/db-secret.properties")
public class MyBatisConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
