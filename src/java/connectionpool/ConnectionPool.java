/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connectionpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cis
 */
public class ConnectionPool {
    
    private final List<Connection> availableConnections;
    private final List<Connection> inUseConnections;
    private final int maxNumberOfConnections;
    private final String databaseName;
    private final Executor executor;
    private boolean closed;

    public ConnectionPool(String databaseName, int maxNumberOfConnections, int initialNumberOfConnections) {
        this.databaseName = databaseName;
        
        this.maxNumberOfConnections = maxNumberOfConnections;
        
        this.availableConnections = new LinkedList<>();
        this.inUseConnections = new LinkedList<>();
        
        this.executor = Executors.newCachedThreadPool();
        
        this.closed = false;
               
        for(int i = 1; i <= initialNumberOfConnections; i++) {
            createConnection();
        }
    }
    
    public synchronized Connection getConnection() {
        if(this.closed) throw new IllegalStateException();
        
        if(!availableConnections.isEmpty())
        {
            Connection connection = availableConnections.remove(availableConnections.size()-1);
            inUseConnections.add(connection);
            return connection;
        }
        
        createConnectionInBackground();
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getConnection();
    }
    
    private synchronized void returnConnection(Connection connection) {
        if(this.closed) throw new IllegalStateException();

        inUseConnections.remove(connection);
        try {
            connection.rollback();
            connection.setAutoCommit(false);
            availableConnections.add(connection);
            notifyAll();
        } catch (SQLException ex) { 
            closeConnection(connection);
        } 
    }

    private void closeConnection(Connection connection) {
        try {
            if (Proxy.isProxyClass(connection.getClass())) {
                ((ConnectionProxy) Proxy.getInvocationHandler(connection)).realClose();
            } else {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private synchronized void createConnection() {
        if(totalNumberOfConnections() >= maxNumberOfConnections) {
            return;
        }
        try {
            
            Connection connection = DriverManager.getConnection("jdbc:derby:"+databaseName+";create=true");
                      
            Connection proxiedConnection = ConnectionProxy.getInstance(connection, this);
            availableConnections.add(proxiedConnection);
            notifyAll();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int totalNumberOfConnections() {
        return availableConnections.size() + inUseConnections.size();
    }

    
    private void createConnectionInBackground() {
        this.executor.execute(() -> createConnection() );
    }

    public synchronized void close() {
        if(this.closed) throw new IllegalStateException();
        this.closed = true;
        availableConnections.forEach(c -> closeConnection(c));
        inUseConnections.forEach(c -> closeConnection(c));
    }

    private static class ConnectionProxy implements InvocationHandler {

        public static Connection getInstance(Connection connectionToWrap, ConnectionPool connectionPool) {
            return (Connection) java.lang.reflect.Proxy.newProxyInstance(
                    connectionToWrap.getClass().getClassLoader(), 
                    connectionToWrap.getClass().getInterfaces(),
                    new ConnectionProxy(connectionToWrap, connectionPool));
           
        }
        
        private final Connection connection;
        private final ConnectionPool connectionPool;
        private ConnectionProxy(Connection connection, ConnectionPool connectionPool) {
            this.connection = connection; 
            this.connectionPool = connectionPool;
        }

        public void realClose() throws SQLException {
            connection.close();
        }
        
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {          
            String methodName = method.getName();
            switch (methodName) {
                case "close":
                    connectionPool.returnConnection((Connection)proxy);
                    return null;
                case "hashCode":
                    return connection.hashCode();
                case "equals":
                    return internalEquals(proxy,args[0]);         
                default:
                    break;
            }
           return method.invoke(connection, args);
        }   

        private Boolean internalEquals(Object me, Object arg) {
           if(arg == null) return false;
           if(arg.getClass() != me.getClass()) return null;
           
            InvocationHandler argHandler = Proxy.getInvocationHandler(arg);
            if (!(argHandler instanceof ConnectionProxy)) {
                // the proxies behave differently.
                return false;
            }
            return ((ConnectionProxy) argHandler).connection.equals(connection);     
        }
    }

    public static enum DbType {
       DERBY,
       MySQL;        
    }
}
