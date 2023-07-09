package com.chengjunjie.web.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;import java.util.ArrayList;import java.util.List;

@Component
public class SSRFProtectionUtils {
    private final static List<String> whitelist = new ArrayList<>();

    static {
        whitelist.add("");
    }

    /**
     * 验证用户输入的URL是否合法
     * @param url 要检测的URL
     * @return 是否合法
     */
    public static boolean isValidURL(String url) {
        try {
            UriComponentsBuilder.fromHttpUrl(url).build();
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isAllowedURL(String url) {
        for (String allowedURL: whitelist) {
            if (url.startsWith(allowedURL)) return true;
        }
        return false;
    }
}
