package org.teapotech.taskforce.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostUtils {

	private static String hostname;
	private static String hostId;

	private static Logger LOG = LoggerFactory.getLogger(HostUtils.class);

	public static String geteHostname() {
		if (hostname != null) {
			return hostname;
		}
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			LOG.error(e.getMessage());
			return "unknown";
		}
		return hostname;
	}

	public static String getHostIdentifier() {

		hostId = geteHostname();
		if (hostId.equals("unknown")) {
			hostId = RandomStringUtils.randomAlphanumeric(10);
		}
		return hostId;
	}
}
