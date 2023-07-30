package com.github.fashionbrot.common.cookie;

import com.github.fashionbrot.common.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * cookie 工具
 * @author fashionbrot
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCookie {

    public static final String SET_COOKIE = "Set-Cookie";

    private String name;

    private String value;

    private  Duration maxAge;

    private  String domain;

    private String path;

    private boolean secure;

    private boolean httpOnly;
    /**
     * Strict:
     * 仅允许一方请求携带Cookie，即浏览器将只发送相同站点请求的Cookie，即当前网页URL与请求目标URL完全一致，浏览器默认该模式。
     * Lax:
     * 允许部分第三方请求携带Cookie
     * “”
     * 无论是否跨站都会发送Cookie
     */
    private String sameSite;


    private static final ZoneId GMT = ZoneId.of("GMT");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US).withZone(GMT);
    static String formatDate(long date) {
        Instant instant = Instant.ofEpochMilli(date);
        ZonedDateTime time = ZonedDateTime.ofInstant(instant, GMT);
        return DATE_FORMATTER.format(time);
    }

    @Override
    public String toString() {
        Rfc6265Utils.validateCookieName(name);
        Rfc6265Utils.validateCookieValue(value);
        Rfc6265Utils.validateDomain(domain);
        Rfc6265Utils.validatePath(path);
        Rfc6265Utils.validateSameSite(sameSite);

        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append('=').append(this.value);
        if (ObjectUtil.isNotEmpty(getPath())) {
            sb.append("; Path=").append(getPath());
        }
        if (ObjectUtil.isNotEmpty(this.domain)) {
            sb.append("; Domain=").append(this.domain);
        }
        if (!this.maxAge.isNegative()) {
            sb.append("; Max-Age=").append(this.maxAge.getSeconds());
            sb.append("; Expires=");
            long millis = this.maxAge.getSeconds() > 0 ? System.currentTimeMillis() + this.maxAge.toMillis() : 0;
            sb.append(formatDate(millis));
        }
        if (this.secure) {
            sb.append("; Secure");
        }
        if (this.httpOnly) {
            sb.append("; HttpOnly");
        }
        if (ObjectUtil.isNotEmpty(this.sameSite)) {
            sb.append("; SameSite=").append(this.sameSite);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.domain.hashCode();
        result = 31 * result + this.path.hashCode();
        return result;
    }


    @Override
    public boolean equals( Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ResponseCookie)) {
            return false;
        }
        ResponseCookie otherCookie = (ResponseCookie) other;
        return (this.name.equalsIgnoreCase(otherCookie.getName()) &&
                ObjectUtil.nullSafeEquals(this.path, otherCookie.getPath()) &&
                ObjectUtil.nullSafeEquals(this.domain, otherCookie.getDomain()));
    }




    private static class Rfc6265Utils {

        private static final String SEPARATOR_CHARS = new String(new char[] {
                '(', ')', '<', '>', '@', ',', ';', ':', '\\', '"', '/', '[', ']', '?', '=', '{', '}', ' '
        });

        private static final String DOMAIN_CHARS =
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-";


        public static void validateCookieName(String name) {
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                // CTL = <US-ASCII control chars (octets 0 - 31) and DEL (127)>
                if (c <= 0x1F || c == 0x7F) {
                    throw new IllegalArgumentException(
                            name + ": RFC2616 token cannot have control chars");
                }
                if (SEPARATOR_CHARS.indexOf(c) >= 0) {
                    throw new IllegalArgumentException(
                            name + ": RFC2616 token cannot have separator chars such as '" + c + "'");
                }
                if (c >= 0x80) {
                    throw new IllegalArgumentException(
                            name + ": RFC2616 token can only have US-ASCII: 0x" + Integer.toHexString(c));
                }
            }
        }

        public static void validateCookieValue( String value) {
            if (value == null) {
                return;
            }
            int start = 0;
            int end = value.length();
            if (end > 1 && value.charAt(0) == '"' && value.charAt(end - 1) == '"') {
                start = 1;
                end--;
            }
            for (int i = start; i < end; i++) {
                char c = value.charAt(i);
                if (c < 0x21 || c == 0x22 || c == 0x2c || c == 0x3b || c == 0x5c || c == 0x7f) {
                    throw new IllegalArgumentException(
                            "RFC2616 cookie value cannot have '" + c + "'");
                }
                if (c >= 0x80) {
                    throw new IllegalArgumentException(
                            "RFC2616 cookie value can only have US-ASCII chars: 0x" + Integer.toHexString(c));
                }
            }
        }

        public static void validateDomain( String domain) {
            if (ObjectUtil.isEmpty(domain)) {
                return;
            }
            int char1 = domain.charAt(0);
            int charN = domain.charAt(domain.length() - 1);
            if (char1 == '-' || charN == '.' || charN == '-') {
                throw new IllegalArgumentException("Invalid first/last char in cookie domain: " + domain);
            }
            for (int i = 0, c = -1; i < domain.length(); i++) {
                int p = c;
                c = domain.charAt(i);
                if (DOMAIN_CHARS.indexOf(c) == -1 || (p == '.' && (c == '.' || c == '-')) || (p == '-' && c == '.')) {
                    throw new IllegalArgumentException(domain + ": invalid cookie domain char '" + c + "'");
                }
            }
        }

        public static void validatePath( String path) {
            if (path == null) {
                return;
            }
            for (int i = 0; i < path.length(); i++) {
                char c = path.charAt(i);
                if (c < 0x20 || c > 0x7E || c == ';') {
                    throw new IllegalArgumentException(path + ": Invalid cookie path char '" + c + "'");
                }
            }
        }
        public static void validateSameSite(String sameSite){
            if (ObjectUtil.isNotEmpty(sameSite)){
                if (!"Strict".equals(sameSite) && !"Lax".equals(sameSite)){
                    throw new IllegalArgumentException("sameSite null or Strict or Lax");
                }
            }
        }
    }
}
