package com.forsrc.utils;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.forsrc.utils.JdbcUtils.HandlerResultSet;

public class JdbcUtilsTest {

	private static final String url = "jdbc:h2:~/db/jdbc.h2/test;AUTO_SERVER=TRUE;MVCC=TRUE;LOCK_TIMEOUT=3600000;DB_CLOSE_DELAY=-1;ACCESS_MODE_DATA=rws;AUTO_RECONNECT=TRUE;CACHE_SIZE=10240;PAGE_SIZE=1024;";
	private static final String user = "sa";
	private static final String password = "sa";
	private static final String driverName = "org.h2.Driver";
	private static final DataSource dataSource = JdbcUtils.getDataSource(
			driverName, url, user, password);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws SQLException {
		// fail("Not yet implemented");
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 30,
				50, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5),
				new ThreadPoolExecutor.CallerRunsPolicy());

		for (int i = 0; i < 1000; i++) {

			threadPoolExecutor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						testSelect();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
			});
		}
		threadPoolExecutor.shutdown();

	}

	@Test
	public void test1() throws SQLException {

		Connection connection = JdbcUtils.getDataSource(driverName, url, user,
				password).getConnection();

		Statement statement = connection.createStatement();

		int result = statement
				.executeUpdate("create table if not exists user (username varchar(100), password varchar(30), age int)");
		System.out.println(result);

		PreparedStatement preparedStatement = connection
				.prepareStatement("insert into user (username, password, age) values(?, ?, ?)");
		preparedStatement
				.setString(1, "username-" + System.currentTimeMillis());
		preparedStatement
				.setString(2, "password-" + System.currentTimeMillis());
		preparedStatement.setInt(3, (int) System.currentTimeMillis());
		preparedStatement.addBatch();
		result = preparedStatement.executeUpdate();
		System.out.println(result);

		ResultSet resultSet = statement.executeQuery("select * from user");
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		System.out.println("getRow()  ->" + resultSet.getRow());
		System.out.println("getRow()  ->" + resultSet.getRow());
		int columnCount = resultSetMetaData.getColumnCount();

		while (resultSet.next()) {
			System.out.println("getRow()  ->" + resultSet.getRow());
			for (int i = 1; i <= columnCount; i++) {
				System.out.print(resultSetMetaData.getColumnName(i) + " --> "
						+ resultSet.getString(i) + "  ");
			}
			System.out.println("getRow()  ->" + resultSet.getRow());
			System.out.println();
		}

		List<Map<String, Object>> list = JdbcUtils.list(connection,
				"select * from user", null);
		System.out.println(list);
		connection.close();
		testSelect();
	}

	private static void testSelect() throws SQLException {

		JdbcUtils jdbcUtils = new JdbcUtils();
		jdbcUtils
				.setDataSource(dataSource)
				.call("select * from user where username like ?",
						new Object[] { "username-%" }, new HandlerResultSet() {

							@Override
							public void handle(
									ResultSetMetaData resultSetMetaData)
									throws SQLException {
							}

							@Override
							public boolean handle(int row, String columnName,
									Object value) throws SQLException {
								System.out.println(MessageFormat.format(
										"{0} -- {1} -> {2}: {3}", Thread.currentThread(), row, columnName,
										value));
								return true;
							}

						}).close();
	}

}
