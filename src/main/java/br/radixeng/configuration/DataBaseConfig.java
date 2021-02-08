package br.radixeng.configuration;

import static com.wix.mysql.EmbeddedMysql.anEmbeddedMysql;
import static com.wix.mysql.distribution.Version.v5_6_21;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wix.mysql.config.MysqldConfig;

@Configuration
public class DataBaseConfig {

  private static MysqldConfig config;
  
  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.username}")
  private String username;
  
  @Value("${spring.datasource.password}")
  private String password;
  
  
  @Bean
  @SuppressWarnings("rawtypes")
  public DataSource dataSource() {
	  
	  embeddMySQL();
	  
	  DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
	  
      dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
      dataSourceBuilder.url(dbUrl);
      dataSourceBuilder.username(username);
      dataSourceBuilder.password(password);
      
      return dataSourceBuilder.build();
      
//      HikariConfig config = new HikariConfig();
//      
//      embeddMySQL();
//      
//      config.setJdbcUrl(dbUrl);
//      config.setUsername(username);
//      config.setPassword(password);
                                   
//      config.addDataSourceProperty("useUnicode", "true");
//      config.addDataSourceProperty("characterEncoding", "UTF-8");
//      config.addDataSourceProperty("useSSL", "false");
//      config.addDataSourceProperty("useJDBCCompliantTimezoneShift", "true");
//      config.addDataSourceProperty("useLegacyDatetimeCode", "false");
//      config.addDataSourceProperty("serverTimezone", "GMT-3");
//      config.addDataSourceProperty("nullNamePatternMatchesAll", "true");
      
//      return new HikariDataSource(config);
  }
  
	public void embeddMySQL() {
		
		config = MysqldConfig.aMysqldConfig(v5_6_21)
		        .withPort(3306)
		        .withUser(username, password)
		        .build();
		
		anEmbeddedMysql(config).start();
	}
}
	