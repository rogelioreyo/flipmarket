package mx.com.agurno.flipmarket.config;

import static org.hibernate.cfg.AvailableSettings.C3P0_ACQUIRE_INCREMENT;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_STATEMENTS;
import static org.hibernate.cfg.AvailableSettings.C3P0_MIN_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_TIMEOUT;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mx.com.agurno.flipmarket.repository.CustomRepositoryImpl;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "mx.com.agurno.flipmarket.repository", repositoryBaseClass = CustomRepositoryImpl.class)
@ComponentScan(basePackages = { "mx.com.agurno.flipmarket" })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync
@EnableWebMvc
@EnableCaching
@EnableSpringConfigured
@EnableScheduling
public class Application implements WebMvcConfigurer {
	
	/** The Constant LOG. */
	private static final Logger LOG  = Logger.getLogger(Application.class);

	@Autowired
	private Environment env;

	@Bean
	public LocalSessionFactoryBean getSessionFactory() {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

		Properties props = new Properties();

		// Setting Hibernate properties
		props.put(SHOW_SQL, env.getProperty("hibernate.show_sql"));
		props.put(HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));

		// Setting C3P0 properties
		props.put(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
		props.put(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
		props.put(C3P0_ACQUIRE_INCREMENT, env.getProperty("hibernate.c3p0.acquire_increment"));
		props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
		props.put(C3P0_MAX_STATEMENTS, env.getProperty("hibernate.c3p0.max_statements"));

		factoryBean.setDataSource(dataSource());
		factoryBean.setHibernateProperties(props);

		return factoryBean;
	}

	@Bean
    DataSource dataSource() {
	    DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setUsername("flipmarket");
	    dataSource.setPassword("flipmarket");
	    dataSource.setUrl("jdbc:mysql://localhost:3306/flipmarket");
	    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }
	
	@Bean
	public EntityManagerFactory entityManagerFactory() {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("mx.com.agurno.flipmarket.entity");
		factory.setDataSource(dataSource());
		factory.setPersistenceUnitName("persistenceUnitSQLServer");
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("operationsehcache", "myoperationsehcache");
	}

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(env.getProperty("spring.mail.host"));
		mailSender.setPort(Integer.parseInt(env.getProperty("spring.mail.port")));
		mailSender.setUsername(env.getProperty("spring.mail.username"));
		mailSender.setPassword(env.getProperty("spring.mail.password"));
		mailSender.setDefaultEncoding(env.getProperty("spring.mail.default-encoding"));
		mailSender.setProtocol(env.getProperty("spring.mail.protocol"));

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.smtp.timeout", "20000");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "true");

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp().prefix("/WEB-INF/views/").suffix(".jsp");
	}
	

//    @Bean
//    public ViewResolver internalResourceViewResolver() {
//        InternalResourceViewResolver bean = new InternalResourceViewResolver();
//        bean.setViewClass(JstlView.class);
//        bean.setPrefix("/WEB-INF/views/");
//        bean.setSuffix(".jsp");
//        return bean;
//    }

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/inicio").setViewName("login");
		registry.addViewController("/flipmarket").setViewName("flipmarket");
		registry.addViewController("/apis/flipmarket").setViewName("flipmarket");
	}

	@Bean
	public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
		OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor = new OpenEntityManagerInViewInterceptor();
		openEntityManagerInViewInterceptor.setEntityManagerFactory(entityManagerFactory());
		return openEntityManagerInViewInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
	}
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
    
}
