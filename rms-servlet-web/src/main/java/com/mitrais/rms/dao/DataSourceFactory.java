package com.mitrais.rms.dao;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javafx.scene.chart.PieChart;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This class provides MySQL datasource to be used to connect to database.
 * It implements singleton pattern See <a href="http://www.oodesign.com/singleton-pattern.html">Singleton Pattern</a>
 */
public class DataSourceFactory
{
    private DataSource dataSource;

    DataSourceFactory()
    {
        Properties prop = new Properties();
        String propFile = "database.properties";
        InputStream is = null;
        MysqlDataSource dataSource = null;
        String url = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream("database.properties");
            prop.load(is);
            dataSource = new MysqlDataSource();
            url = prop.getProperty("databaseUrl") + prop.getProperty("serverName") + ":" + prop.getProperty("port") + "/" + prop.getProperty("databaseName");
            dataSource.setURL(url);
            dataSource.setDatabaseName(prop.getProperty("databaseName"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            dataSource.setPort(Integer.valueOf(prop.getProperty("port")));
            this.dataSource = dataSource;
        } catch (IOException e) {
            System.out.println("Unable to read file " + propFile);
        }
    }

    /**
     * Get a data source to database
     *
     * @return DataSource object
     */
    public static Connection getConnection() throws SQLException
    {
        return SingletonHelper.INSTANCE.dataSource.getConnection();
    }

    private static class SingletonHelper
    {
        private static final DataSourceFactory INSTANCE = new DataSourceFactory();
    }
}
