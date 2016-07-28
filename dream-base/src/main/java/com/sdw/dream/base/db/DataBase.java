package com.sdw.dream.base.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DataBase {
    static Logger logger = Logger.getLogger(DataBase.class);

    public DataBase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private final DataSource dataSource;

    private Connection openConnection() throws SQLException {
        return this.dataSource.getConnection();
    }

    private Connection transConntection;

    private Connection openConnectionTrans() throws SQLException {
        if (transConntection == null) {
            transConntection = this.dataSource.getConnection();
            transConntection.setAutoCommit(false);
        }
        return transConntection;
    }

    public DataReader executeQuery(String sql, Object... values) {
        Connection connection = null;
        try {
            connection = this.openConnection();
            PreparedStatement statement = createStatement(sql, connection, values);
            logger.info("executeQuery:" + sql);
            return new DataReader(connection, statement.executeQuery(), true);
        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ee) {
                    logger.error("connection closed error.", ee);
                }
            }
            throw new DataBaseException("sql:" + sql + "--" + this.joinValues(values) + "--" + e.getMessage(), e);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ee) {
                    logger.error("connection closed error.", ee);
                }
            }
            throw new DataBaseException("sql:" + sql + "--" + this.joinValues(values) + "--" + e.getMessage(), e);
        }
    }

    public boolean execute(String sql) {
        Connection connection = null;
        try {
            connection = this.openConnection();
            PreparedStatement statement = createStatement(sql, connection);
            logger.info("executeQuery:" + sql);
            return statement.execute();
        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ee) {
                    logger.error("connection closed error.", ee);
                }
            }
            throw new DataBaseException("sql:" + sql + "--" + e.getMessage(), e);
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ee) {
                    logger.error("connection closed error.", ee);
                }
            }
            throw new DataBaseException("sql:" + sql + "--" + e.getMessage(), e);
        }
    }

    private String joinValues(Object[] values) {
        int length = values.length;
        String[] vs = new String[length];
        for (int i = 0; i < length; i++) {
            if (values[i] == null)
                vs[i] = "null";
            else
                vs[i] = values[i].toString();
        }
        return StringUtils.join(vs, ',');
    }

    public DataReader executeQueryTrans(String sql, Object... values) {
        try {
            Connection connection = this.openConnectionTrans();
            PreparedStatement statement = createStatement(sql, connection, values);
            logger.info("executeQueryTrans:" + sql);
            return new DataReader(connection, statement.executeQuery(), false);
        } catch (SQLException e) {
            throw new DataBaseException(
                    "executeQueryTrans:" + sql + "--" + this.joinValues(values) + "--" + e.getMessage(), e);
        }

    }

    private PreparedStatement createStatement(String sql, Connection connection, Object... values) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            int length = values.length;
            for (int i = 0; i < length; i++) {
                if (values[i] != null) {
                    if (values[i] instanceof Date) {
                        Date date = (Date) values[i];
                        statement.setTimestamp(i + 1, new java.sql.Timestamp(date.getTime()));
                        // new java.sql.Timestamp
                    } else if (values[i] instanceof UUID) {
                        statement.setString(i + 1, ((UUID) values[i]).toString());
                    } else if (values[i] instanceof Integer) {
                        statement.setInt(i + 1, ((Integer) values[i]));
                    } else if (values[i] instanceof String) {
                        statement.setString(i + 1, ((String) values[i]));
                    } else
                        statement.setObject(i + 1, values[i]);
                } else
                    statement.setObject(i + 1, values[i]);
            }
            return statement;
        } catch (SQLException e) {
            throw new DataBaseException("sql:" + sql + "--" + this.joinValues(values) + "--" + e.getMessage(), e);

        }
    }

    public int executeUpdate(String sql, Object... values) {
        Connection connection = null;
        try {
            connection = this.openConnection();
            PreparedStatement statement = createStatement(sql, connection, values);
            logger.info("executeUpdate:" + sql);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("sql:" + sql + "--" + this.joinValues(values) + e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("connection closed.", e);
                }
            }
        }
    }

    public int executeUpdateTrans(String sql, Object... values) {
        Connection connection = null;
        try {
            connection = this.openConnectionTrans();
            PreparedStatement statement = createStatement(sql, connection, values);
            logger.info("executeUpdateTrans:" + sql);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataBaseException("sql:" + sql + "--" + this.joinValues(values) + "--" + e.getMessage(), e);
        }
    }

    public Object executeScalar(String sql, Object... values) {
        DataReader result = this.executeQuery(sql, values);
        try {
            if (result.next())
                return result.getObject(1);
            else
                throw new DataBaseException("sql:" + sql + "-- No one line of data.");
        } finally {
            result.close();
        }
    }

    public Object executeScalarTrans(String sql, Object... values) {
        DataReader result = this.executeQueryTrans(sql, values);
        try {
            if (result.next())
                return result.getObject(1);
            else
                throw new DataBaseException("sql:" + sql + "-- No one line of data.");
        } finally {
            result.close();
        }
    }

    public void commit() {

        try {
            if (this.transConntection != null && !this.transConntection.isClosed()) {
                this.transConntection.commit();

            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        } finally {
            try {
                if (this.transConntection != null && !this.transConntection.isClosed()) {
                    this.transConntection.close();
                }
            } catch (SQLException e) {
                logger.error("close exp", e);
            }
            this.transConntection = null;
        }
    }

    public void rallback() {
        try {
            if (this.transConntection != null && !this.transConntection.isClosed()) {
                this.transConntection.rollback();
                this.transConntection.close();
                this.transConntection = null;

            }
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage(), e);
        } finally {
            try {
                if (this.transConntection != null && !this.transConntection.isClosed()) {
                    this.transConntection.close();
                }
            } catch (SQLException e) {
                logger.error("close exp", e);
            }
            this.transConntection = null;
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
