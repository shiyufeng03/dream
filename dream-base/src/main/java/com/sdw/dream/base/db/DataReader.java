package com.sdw.dream.base.db;

import java.io.Closeable;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class DataReader implements Closeable {
    static Logger logger = Logger.getLogger(DataReader.class);

    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    DataReader(Connection connection, ResultSet resultSet, boolean autoCloseConnection) {
        this.connection = connection;
        this.resultSet = resultSet;
        this.autoCloseConnection = autoCloseConnection;
    }

    private final boolean autoCloseConnection;

    private final Connection connection;

    private final ResultSet resultSet;

    private boolean isclose = false;

    public void reset() {
        try {
            this.resultSet.beforeFirst();
        } catch (SQLException e) {
            throw new DataReadException("resultset", "can not move to beforefirst.", e);
        }
    }

    public boolean next() {
        try {
            boolean flag = this.resultSet.next();
            if (!flag) {
                try {
                    this.close();
                } catch (Exception e) {
                    throw new DataBaseException("close error.", e);
                }
            }
            return flag;

        } catch (SQLException e) {
            throw new DataReadException("resultset", "next error.", e);
        }
    }

    public int getColumnCount() {
        try {
            return resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new DataReadException("resultSet", "getColumnCount error.", e);
        }
    }

    public String getColumnTypeName(int columnIndex) {
        try {
            return resultSet.getMetaData().getColumnTypeName(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getColumnTypeName error.", e);
        }
    }

    public int getColumnType(int columnIndex) {
        try {
            return resultSet.getMetaData().getColumnType(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getColumnTypeName error.", e);
        }
    }

    public String getColumnClassName(int columnIndex) {
        try {
            return resultSet.getMetaData().getColumnClassName(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getColumnClassName error.", e);
        }
    }

    public String getColumnLable(int columnIndex) {
        try {
            return resultSet.getMetaData().getColumnLabel(columnIndex);

        } catch (SQLException e) {
            throw new DataReadException(Integer.toString(columnIndex), "getColumnLable error.", e);
        }
    }

    public int getRow() {
        try {
            return resultSet.getRow();
        } catch (SQLException e) {
            throw new DataReadException("resultset", "getRow error.", e);
        }
    }

    /**
     * 是否为Null
     * 
     * @param columnLabel
     * @return
     */
    public boolean isNull(String columnLabel) {
        Object value = this.getObject(columnLabel);
        if (value == null)
            return true;
        else
            return false;
    }

    public boolean isNull(int columnIndex) {
        Object value = this.getObject(columnIndex);
        if (value == null)
            return true;
        else
            return false;
    }

    public int findColumn(String columnName) {
        try {
            return resultSet.findColumn(columnName);
        } catch (SQLException e) {
            throw new DataReadException(columnName, "findColumn error.", e);
        }
    }

    public void close() throws DataBaseException {
        if (!isclose) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new DataBaseException("close error.", e);
            } finally {
                if (this.autoCloseConnection)
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        throw new DataBaseException("close error.", e);
                    }
            }
            this.isclose = true;
        }

    }

    public String getString(int columnIndex) {
        try {
            return resultSet.getString(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getString error.", e);
        }
    }

    public java.util.Date getDate(String columnLabel) {
        try {
            String date = resultSet.getString(columnLabel);
            if (StringUtils.isEmpty(date))
                throw new DataReadException(columnLabel, "value is null,can't conver.", null);
            return format.parse(date);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getDate error.", e);
        } catch (ParseException e) {
            throw new DataReadException(columnLabel, "getDate error.", e);
        }
    }

    public java.util.Date getDate(int columnIndex) {
        try {
            String date = resultSet.getString(columnIndex);
            if (StringUtils.isEmpty(date))
                throw new DataReadException(this.getColumnLable(columnIndex), "value is null,can't conver.", null);
            return format.parse(date);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getDate error.", e);
        } catch (ParseException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getDate error.", e);
        }

    }

    public boolean getBoolean(int columnIndex) {
        try {
            return resultSet.getBoolean(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getBoolean error.", e);
        }
    }

    public byte getByte(int columnIndex) {
        try {
            return resultSet.getByte(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getByte error.", e);
        }
    }

    public short getShort(int columnIndex) {
        try {
            return resultSet.getShort(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getShort error.", e);
        }
    }

    public int getInt(int columnIndex) {
        try {
            return resultSet.getInt(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getInt error.", e);
        }
    }

    public long getLong(int columnIndex) {
        try {
            return resultSet.getLong(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getLong error.", e);
        }
    }

    public float getFloat(int columnIndex) {
        try {
            return resultSet.getFloat(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getFloat error.", e);
        }
    }

    public double getDouble(int columnIndex) {
        try {
            return resultSet.getDouble(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getDouble error.", e);
        }
    }

    public byte[] getBytes(int columnIndex) {
        try {
            return resultSet.getBytes(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getBytes error.", e);
        }
    }

    public java.io.InputStream getAsciiStream(int columnIndex) {
        try {
            return resultSet.getAsciiStream(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getAsciiStream error.", e);
        }
    }

    public java.io.InputStream getBinaryStream(int columnIndex) {
        try {
            return resultSet.getBinaryStream(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getBinaryStream error.", e);
        }
    }

    public String getString(String columnLabel) {
        try {
            return resultSet.getString(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getString error.", e);
        }
    }

    public boolean getBoolean(String columnLabel) {
        try {
            return resultSet.getBoolean(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getBoolean error.", e);
        }
    }

    public byte getByte(String columnLabel) {
        try {
            return resultSet.getByte(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getByte error.", e);
        }
    }

    public short getShort(String columnLabel) {
        try {
            return resultSet.getShort(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getShort error.", e);
        }
    }

    public int getInt(String columnLabel) {
        try {
            return resultSet.getInt(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getInt error.", e);
        }
    }

    public long getLong(String columnLabel) {
        try {
            return resultSet.getLong(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getLong error.", e);
        }
    }

    public float getFloat(String columnLabel) {
        try {
            return resultSet.getFloat(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getFloat error.", e);
        }
    }

    public double getDouble(String columnLabel) {
        try {
            return resultSet.getDouble(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getDouble error.", e);
        }
    }

    public byte[] getBytes(String columnLabel) {
        try {
            return resultSet.getBytes(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getBytes error.", e);
        }
    }

    public java.io.InputStream getAsciiStream(String columnLabel) {
        try {
            return resultSet.getAsciiStream(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getAsciiStream error.", e);
        }
    }

    public Object getObject(int columnIndex) {
        try {
            return resultSet.getObject(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getObject error.", e);
        }
    }

    public Object getObject(String columnLabel) {
        try {
            return resultSet.getObject(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getObject error.", e);
        }
    }

    public java.io.Reader getCharacterStream(int columnIndex) {
        try {
            return resultSet.getCharacterStream(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getCharacterStream error.", e);
        }
    }

    public java.io.Reader getCharacterStream(String columnLabel) {
        try {
            return resultSet.getCharacterStream(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getCharacterStream error.", e);
        }
    }

    public BigDecimal getBigDecimal(int columnIndex) {
        try {
            return resultSet.getBigDecimal(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getBigDecimal error.", e);
        }
    }

    public BigDecimal getBigDecimal(String columnLabel) {
        try {
            return resultSet.getBigDecimal(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getBigDecimal error.", e);
        }
    }

    public Blob getBlob(int columnIndex) {
        try {
            return resultSet.getBlob(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getBlob error.", e);
        }
    }

    public Clob getClob(int columnIndex) {
        try {
            return resultSet.getClob(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getClob error.", e);
        }
    }

    public Array getArray(int columnIndex) {
        try {
            return resultSet.getArray(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getArray error.", e);
        }
    }

    public Blob getBlob(String columnLabel) {
        try {
            return resultSet.getBlob(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getBlob error.", e);
        }
    }

    public Clob getClob(String columnLabel) {
        try {
            return resultSet.getClob(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getClob error.", e);
        }
    }

    public Array getArray(String columnLabel) {
        try {
            return resultSet.getArray(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getArray error.", e);
        }
    }

    public String getNString(int columnIndex) {
        try {
            return resultSet.getNString(columnIndex);
        } catch (SQLException e) {
            throw new DataReadException(this.getColumnLable(columnIndex), "getNString error.", e);
        }
    }

    public String getNString(String columnLabel) {
        try {
            return resultSet.getNString(columnLabel);
        } catch (SQLException e) {
            throw new DataReadException(columnLabel, "getNString error.", e);
        }
    }

    // public <T> T getObject(int columnIndex, Class<T> type) {
    // try {
    // return resultSet.getObject(columnIndex, type);
    // } catch (SQLException e) {
    // throw new DataReadException(this.getColumnLable(columnIndex),
    // "getObject error.", e);
    // }
    // }
    //
    // public <T> T getObject(String columnLabel, Class<T> type) {
    // try {
    // return resultSet.getObject(columnLabel, type);
    // } catch (SQLException e) {
    // throw new DataReadException(columnLabel, "getObject error.", e);
    // }
    // }

    public Object[] getValues() {
        int count;
        try {
            count = resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            throw new DataReadException("dataReader", "getValues error.", e);
        }
        Object[] values = new Object[count];
        for (int i = 0; i < count; i++) {
            try {
                values[i] = this.getObject(i + 1);
            } catch (DataReadException e) {
                values[i] = null;
                logger.info("column first name: " + this.getColumnLable(1) + ",value: " + values[0] + ",error coloum: "
                    + this.getColumnLable(i + 1) +  ",error message:" + e.getCause().getMessage());
            }
        }
        return values;
    }
}
