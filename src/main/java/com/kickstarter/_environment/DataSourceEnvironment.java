package com.kickstarter._environment;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@Configuration
@EnableTransactionManagement
@MapperScan(basePackages={"com.kickstarter.**.dao"})
public class DataSourceEnvironment {
	//private static final Logger LOG = Logger.getLogger(DataSourceConfiguration.class);
	@Value("${db.catalog}")
	private String dbCatalog; // aka schema
	@Value("${db.url}") 
	private String dbUrl;
	@Value("${db.user}") 
	private String dbUser;
	@Value("${db.password}") 
	private String dbPass;
	
	// new HikariCP configs
	@Value("${db.auto.commit}")
	private Boolean autoCommit;
	
	@Value("${db.connection.timeout.in.ms}")
	private Long connectionTimeout;
	
	@Value("${db.max.lifetime.in.ms}")
	private Long maxLifeTime;
	
	@Value("${db.max.poolsize}")
	private Integer maxPoolSize;
	
	@Value("${db.initialization.failfast}")
	private Boolean initFailFast;
	
	@Value("${db.validation.timeout.in.ms}")
	private Long validationTimeout;
	
	@Value("${db.leak.detection.threshold.in.ms}")
	private Long leakDetectionThreshold;
	@Value("${myBatis.map.underscore.toCamelCase}")
	private Boolean myBatisMapUnderscoreToCamelCase;
	
	@Value("${db.auto.populate}") 
	private Boolean dbAutoPopulate;
	@Value("${db.auto.clean}") 
	private Boolean dbAutoClean;
	
	@Bean(name="dataSource", destroyMethod="close")
	public HikariDataSource dataSource(){
		/*
		 * HikariCP docs:
		 * MysqlDataSource is known to be broken, use jdbcUrl configuration instead.
		 * Note that you do not need this property if you are using jdbcUrl 
		 * for "old-school" DriverManager-based JDBC driver configuration. Default: none
		 */
		
		HikariConfig config = new HikariConfig();
		// config.setDriverClassName("com.mysql.jdbc.Driver"); //"com.mysql.cj.jdbc.Driver" 
		
		config.setDataSourceClassName("com.microsoft.sqlserver.jdbc.SQLServerDataSource");
		//config.setDriverClassName("com.mysql.jdbc.Driver");
		
		// required configs : no defaults
		config.setJdbcUrl(dbUrl);
		config.setUsername(dbUser);
		config.setPassword(dbPass);
	    config.setCatalog(dbCatalog);
	    // optional configs : overriden defaults
		config.setAutoCommit(autoCommit);
		config.setConnectionTimeout(connectionTimeout);
		config.setMaxLifetime(maxLifeTime);
		config.setMaximumPoolSize(maxPoolSize);
		config.setInitializationFailFast(initFailFast);
		config.setValidationTimeout(validationTimeout);
		config.setLeakDetectionThreshold(leakDetectionThreshold);
		//config.setConnectionInitSql("INSERT INTO `alpha`.`logs` (`tag`, `message`) VALUES ('DEBUG', 'SERVER Initial Connection');");
		
		return new HikariDataSource(config);
	}
	
	@Autowired
	@Bean
	public SqlSessionFactory getSqlSessionFactoryBean(DataSource dataSource) throws Exception{
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage("com.kickstarter");
		// for timestamps and the like..
		// sqlSessionFactoryBean.setTypeHandlersPackage("com.pccw.tks._mybatis.typehandlers");
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(myBatisMapUnderscoreToCamelCase);
		sqlSessionFactory.getConfiguration().setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
		sqlSessionFactory.getConfiguration().setUseGeneratedKeys(true);
		return sqlSessionFactory;
	}
	
	@Autowired
	@Bean
	public SqlSession getSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
		return sqlSessionTemplate;
	}
	
	@Autowired
	@Bean
	public DataSourceTransactionManager getDataSourceTransactionManager(DataSource dataSource){
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
		dataSourceTransactionManager.setDataSource(dataSource);
		return dataSourceTransactionManager;
	}
}
