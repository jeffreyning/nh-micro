package com.nh.micro.datasource;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;


import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;


/**
 * 
 * @author ninghao
 *
 */
public class MicroPooledConnection implements javax.sql.PooledConnection, Connection {
	private Connection conn;
	public MicroPooledConnection(Connection connection){
		this.conn=connection;
	}
	public void closeReal() throws SQLException {
		conn.close();
		return;
	}
	public void close() throws SQLException {
		return;
	}
	public void commitReal() throws SQLException {
		conn.commit();
		return;
	}
    public void commit() throws SQLException {
    	return;
    }

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return conn.unwrap(iface);
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return conn.isWrapperFor(iface);
	}
	@Override
	public Statement createStatement() throws SQLException {
		return conn.createStatement();
	}
	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}
	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		return conn.prepareCall(sql);
	}
	@Override
	public String nativeSQL(String sql) throws SQLException {
		return conn.nativeSQL(sql);
	}
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		conn.setAutoCommit(autoCommit);
	}
	@Override
	public boolean getAutoCommit() throws SQLException {
		return conn.getAutoCommit();
	}
	@Override
	public void rollback() throws SQLException {
		return;
		
	}
	public void rollbackReal() throws SQLException {
		conn.rollback();
		
	}	
	@Override
	public boolean isClosed() throws SQLException {
		return conn.isClosed();
	}
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return conn.getMetaData();
	}
	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		conn.setReadOnly(readOnly);	
	}
	@Override
	public boolean isReadOnly() throws SQLException {
		return conn.isReadOnly();
	}
	@Override
	public void setCatalog(String catalog) throws SQLException {
		conn.setCatalog(catalog);
	}
	@Override
	public String getCatalog() throws SQLException {
		return conn.getCatalog();
	}
	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		conn.setTransactionIsolation(level);
	}
	@Override
	public int getTransactionIsolation() throws SQLException {
		return conn.getTransactionIsolation();
	}
	@Override
	public SQLWarning getWarnings() throws SQLException {
		return conn.getWarnings();
	}
	@Override
	public void clearWarnings() throws SQLException {
		conn.clearWarnings();
	}
	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return conn.createStatement(resultSetType, resultSetConcurrency);
	}
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
	}
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return conn.getTypeMap();
	}
	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		conn.setTypeMap(map);
		
	}
	@Override
	public void setHoldability(int holdability) throws SQLException {
		conn.setHoldability(holdability);
		
	}
	@Override
	public int getHoldability() throws SQLException {
		return conn.getHoldability();
	}
	@Override
	public Savepoint setSavepoint() throws SQLException {
		return conn.setSavepoint();
	}
	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return conn.setSavepoint(name);
	}
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		conn.rollback(savepoint);
		
	}
	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		conn.releaseSavepoint(savepoint);
		
	}
	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return conn.prepareStatement(sql, autoGeneratedKeys);
	}
	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return conn.prepareStatement(sql, columnIndexes);
	}
	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return conn.prepareStatement(sql, columnNames);
	}
	@Override
	public Clob createClob() throws SQLException {
		return conn.createClob();
	}
	@Override
	public Blob createBlob() throws SQLException {
		return conn.createBlob();
	}
	@Override
	public NClob createNClob() throws SQLException {
		return conn.createNClob();
	}
	@Override
	public SQLXML createSQLXML() throws SQLException {
		return conn.createSQLXML();
	}
	@Override
	public boolean isValid(int timeout) throws SQLException {
		return conn.isValid(timeout);
	}
	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		conn.setClientInfo(name, value);
		
	}
	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		conn.setClientInfo(properties);
		
	}
	@Override
	public String getClientInfo(String name) throws SQLException {
		return conn.getClientInfo(name);
	}
	@Override
	public Properties getClientInfo() throws SQLException {
		return conn.getClientInfo();
	}
	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		return conn.createArrayOf(typeName, elements);
	}
	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		return conn.createStruct(typeName, attributes);
	}
	@Override
	public Connection getConnection() throws SQLException {
		return conn;
	}
	@Override
	public void addConnectionEventListener(ConnectionEventListener listener) {
		
		
	}
	@Override
	public void removeConnectionEventListener(ConnectionEventListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addStatementEventListener(StatementEventListener listener) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeStatementEventListener(StatementEventListener listener) {
		// TODO Auto-generated method stub
		
	}
}
