/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.start.db.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * global dynamic config
 *
 * @author jimo
 **/
@Slf4j
public class DynamicConfig {

    public static final String DB_SPARK_JARS = "livy.spark.db.jars";

    private final static Properties properties = new Properties();

    static {
        Configuration conf = new Configuration();
        String hdfsPath = getHdfsPath();
        System.setProperty("HADOOP_USER_NAME", getHadoopUser());
        try {
            FileSystem fs = FileSystem.get(new URI(hdfsPath), conf);
            final String confPath = "/opt/spark-apps/start.conf";
            properties.load(new InputStreamReader(fs.open(new Path(confPath))));
            log.info(properties.toString());
        } catch (Exception e) {
            log.error("", e);
        }

    }

    public static void updateProperties(String key, String value) {
        properties.put(key, value);
    }

    /**
     * 不要统一域名，而是当前机器的IP，因为数据要发回提交spark任务的机器
     * 参考https://cloud.tencent.com/developer/article/1610919获取IP方式
     */
    public static String getRemoteServerHostname() {
        try {
            // String hostName = getLocalHostExactAddress().getHostName();
            String hostName = "" + InetAddress.getLocalHost().getHostName();
            log.info("get host name " + hostName);
            return hostName;
        } catch (Exception e) {
            log.warn("get hostname error", e);
            return "localhost";
        }
    }

    public static InetAddress getLocalHostExactAddress() throws SocketException,
        UnknownHostException {
        InetAddress candidateAddress = null;

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = networkInterfaces.nextElement();
            // 该网卡接口下的ip会有多个，需要一个个的遍历判断
            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs
                .hasMoreElements();) {
                InetAddress inetAddr = inetAddrs.nextElement();
                // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                if (!inetAddr.isLoopbackAddress()) {
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，绝大部分情况下是ip地址值
                        return inetAddr;
                    }
                    // 若不是site-local地址 那就记录下该地址当作候选
                    if (candidateAddress == null) {
                        candidateAddress = inetAddr;
                    }
                }
            }
        }
        // 如果除去loopback回环之外无其它地址了，回退到原始方案
        return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
    }

    public static int getRemoteServerPort() {
        return Integer.parseInt(properties.getProperty("server.remote.port", "8848"));
    }

    public static String getLivyUrl() {
        return properties.getProperty("livy.url", "http://livy-local:8998");
    }

    public static int getSparkDriverCores() {
        return Integer.parseInt(properties.getProperty("livy.spark.driverCores", "1"));
    }

    public static String getSparkDriverMemory() {
        return properties.getProperty("livy.spark.driverMemory", "1G");
    }

    public static int getSparkNumExecutors() {
        return Integer.parseInt(properties.getProperty("livy.spark.numExecutors", "1"));
    }

    public static int getSparkExecutorCores() {
        return Integer.parseInt(properties.getProperty("livy.spark.executorCores", "1"));
    }

    public static String getSparkExecutorMemory() {
        return properties.getProperty("livy.spark.executorMemory", "1G");
    }

    /**
     * 这个jar包要放在Livy Server能够访问的地方，如果是本地，就要放在Livy Server机器上
     */
    public static List<String> getDbSparkJars() {
        final String jar = properties.getProperty(
            DB_SPARK_JARS,
            "/opt/spark-apps/start-db-spark-1.0.0-SNAPSHOT.jar"
        );
        final String[] jars = jar.split(",");
        return Arrays.asList(jars);
    }

    public static String getHdfsPath() {
        return properties.getProperty("hdfs.path", "hdfs://hadoop-local:9000");
    }

    public static String getHdfsDataSplitter() {
        return properties.getProperty("hdfs.data.splitter", "\001");
    }

    public static String getHadoopUser() {
        return properties.getProperty("hadoop.user", "hadoop");
    }

    public static String getSparkHdfsResultPath() {
        return getHdfsPath() + properties.getProperty("hdfs.spark.res-dir", "/result/");
    }

    public static String getResultSchemaName(String sqlId) {
        return sqlId + "_schema";
    }

    public static String getResultDataName(String sqlId) {
        return sqlId + "_data";
    }

    public static String getHBaseZookeepers() {
        return properties.getProperty("hbase.zookeepers", "geomesa-hbase-local:2181");
    }

    public static String getSparkRedisHost() {
        return properties.getProperty("spark.redis.host", "10.242.6.21");
    }

    public static int getSparkRedisPort() {
        return Integer.parseInt(properties.getProperty("spark.redis.port", "6379"));
    }

    public static String getSparkRedisAuth() {
        return properties.getProperty("spark.redis.auth", "123qwe");
    }
}
