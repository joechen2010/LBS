package cn.edu.nju.software.util;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UUIDUtils {
    private static final long   TIME_MILLIS_OF_2007_01_01 = 1170313096428L;

    private static final String NODE_ID_PARAM_NAME        = "node.id";

    private static final String DEFAULT_NODE_ID           = "10";

    private static AtomicLong   atomicLong;

    private static String       nodeId;

    private static Properties   properties;

    private static Logger       logger;

    static {
        init();
    }

    private static void init() {
        logger = LoggerFactory.getLogger(UUIDUtils.class);
		properties = PropertyUtils
				.loadProperties("classpath:application.properties");
        nodeId = properties.getProperty(NODE_ID_PARAM_NAME);
        atomicLong = new AtomicLong((System.currentTimeMillis() - TIME_MILLIS_OF_2007_01_01) * 100L);
    }

    public static Long generate() {
        if (StringUtils.isBlank(nodeId)) {
            logger.warn("node id is missed");
            nodeId = DEFAULT_NODE_ID;
        }
        return new Long(new StringBuilder().append(nodeId).append(atomicLong.incrementAndGet()).toString());
    }

    public static void setNodeId(String nodeId) {
        UUIDUtils.nodeId = nodeId;
    }

}
