package org.storevm.framework.remote.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
//@EnableTransactionManagement
@ImportResource(locations = {"classpath:META-INF/ctx/ctx_main.xml"})
@ComponentScan(basePackages = {"org.storevm.framework.remote"})
public class Application {
    // @Bean
    // public PlatformTransactionManager txManager(DataSource masterDataSource) {
    // return new DataSourceTransactionManager(masterDataSource);
    // }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
