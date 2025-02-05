package com.wemo.backend.domain.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@Component
public class IpUtils {

    private static final List<String> TRUSTED_PROXIES = List.of("192.168.1.1", "10.0.0.1");

    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            String[] ips = ip.split(",");
            ip = ips[0].trim(); // 첫 번째 IP를 가져옴
        } else {
            ip = request.getRemoteAddr(); // X-Forwarded-For가 없는 경우
        }

        // 신뢰할 수 있는 프록시 확인
        String proxyIp = request.getRemoteAddr();
        if (!TRUSTED_PROXIES.contains(proxyIp)) {
            return normalizeIp(proxyIp);
        }

        return normalizeIp(ip);
    }

    /**
     * IP 주소를 정규화하고 IPv6 localhost를 IPv4로 변환
     */
    private static String normalizeIp(String ip) {
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1"; // IPv6 localhost를 IPv4로 변환
        }

        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            if (inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress()) {
                return "127.0.0.1"; // 로컬 주소 반환
            }
            return inetAddress.getHostAddress(); // 정규화된 IP 반환
        } catch (UnknownHostException e) {
            return "127.0.0.1"; // 기본값 반환
        }
    }
}
