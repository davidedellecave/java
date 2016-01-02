package ddc.jbdc;

/**
 * @author davidedc 2014
 *
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import ddc.core.ftp.FtpLiteClient;
import ddc.core.ftp.FtpLitePoolFactory;

//
// Here's a simple example of how to use the PoolingDataSource.
//

//
// Note that this example is very similar to the PoolingDriver
// example.  In fact, you could use the same pool in both a
// PoolingDriver and a PoolingDataSource
//

//
// To compile this example, you'll want:
//  * commons-pool2-2.2.jar
//  * commons-dbcp2-2.0.jar
// in your classpath.
//
// To run this example, you'll want:
//  * commons-pool2-2.2.jar
//  * commons-dbcp2-2.0.jar
//  * commons-logging-1.1.3.jar
//  * the classes for your (underlying) JDBC driver
// in your classpath.
//
// Invoke the class using two arguments:
//  * the connect string for your underlying JDBC driver
//  * the query you'd like to execute
// You'll also want to ensure your underlying JDBC driver
// is registered.  You can use the "jdbc.drivers"
// property to do this.
//
// For example:
//  java -Djdbc.drivers=org.h2.Driver \
//       -classpath commons-pool2-2.2.jar:commons-dbcp2-2.0.jar:commons-logging-1.1.3.jar:h2-1.3.152.jar:. \
//       PoolingDataSourceExample \
//       "jdbc:h2:~/test" \
//       "SELECT 1"
//
public class PooledDatasourceFactory {

	public DataSource createDataSource(JdbcConnection conf) {
		//
		// First, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string passed in the command line
		// arguments.
		//
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(conf.getUrl(), conf.getJdbcConnection().user, conf.getJdbcConnection().password);

		//
		// Next we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

		//
		// Now we'll need a ObjectPool that serves as the
		// actual pool of connections.
		//
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
//		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>((PoolableObjectFactory<PoolableConnection>) poolableConnectionFactory);		
		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnectionFactory);
		

		// Set the factory's pool property to the owning pool
		poolableConnectionFactory.setPool((org.apache.commons.pool2.ObjectPool<PoolableConnection>) connectionPool);

		//
		// Finally, we create the PoolingDriver itself,
		// passing in the object pool we created.
		//
		DataSource ds = new PoolingDataSource<PoolableConnection>((org.apache.commons.pool2.ObjectPool<PoolableConnection>) connectionPool);
		return ds;
	}
}
