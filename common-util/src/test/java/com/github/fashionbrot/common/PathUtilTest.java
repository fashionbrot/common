package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.PathUtil;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;

import static org.junit.Assert.*;

/**
 * @author fashionbrot
 */
public class PathUtilTest {

    @Test
    public void testFormatPath() {
        String formattedPath1 = PathUtil.formatPath("my", "class", "path");
        assertEquals("/my/class/path", formattedPath1);

        String formattedPath2 = PathUtil.formatPath("/existing", "path");
        assertEquals("/existing/path", formattedPath2);

        String formattedPath3 = PathUtil.formatPath("", "empty", "path");
        assertEquals("/empty/path", formattedPath3);

        String formattedPath4 = PathUtil.formatPath("only", "");
        assertEquals("/only", formattedPath4);

        String formattedPath5 = PathUtil.formatPath();
        assertEquals("", formattedPath5);
    }



    @Test
    public void testFormatUrlWithSeparator() {
        String formattedUrl1 = PathUtil.formatUrl("http://example.com/");
        assertEquals("http://example.com", formattedUrl1);

        String formattedUrl2 = PathUtil.formatUrl("https://api.example.com//");
        assertEquals("https://api.example.com", formattedUrl2);
    }

    @Test
    public void testFormatUrlWithoutSeparator() {
        String formattedUrl3 = PathUtil.formatUrl("http://example.com");
        assertEquals("http://example.com", formattedUrl3);

        String formattedUrl4 = PathUtil.formatUrl("https://api.example.com");
        assertEquals("https://api.example.com", formattedUrl4);
    }

    @Test
    public void testFormatEmptyUrl() {
        String formattedUrl5 = PathUtil.formatUrl("");
        assertEquals("", formattedUrl5);
    }

    @Test
    public void testFormatNullUrl() {
        String formattedUrl6 = PathUtil.formatUrl(null);
        assertEquals("", formattedUrl6);
    }



    @Test
    public void testGetDefaultPathMatcher() {
        PathMatcher defaultMatcher = PathUtil.getPathMatcher("");
        System.out.println(defaultMatcher);
        assertNotNull(defaultMatcher);
    }

    @Test
    public void testGetPathMatcherWithPackages() {
        String pattern = "glob:package.*,prefix*,exactMatch";
        PathMatcher packageMatcher = PathUtil.getPathMatcher(pattern);
        assertNotNull(packageMatcher);
    }

    @Test
    public void testGetPathMatcherWithInvalidPrefix() {
        String invalidPattern = "invalidPattern";
        PathMatcher invalidMatcher = PathUtil.getPathMatcher(invalidPattern);
        assertNotNull(invalidMatcher);
    }


    @Test
    public void testMatches() {
        // 创建一个glob模式的PathMatcher对象
        PathMatcher globMatcher =PathUtil.getPathMatcher("com.github.fashionbrot");

        // 测试匹配一个匹配的路径
        boolean matchResult1 = PathUtil.matches(globMatcher, "com.github.fashionbrot.test");
        assertTrue(matchResult1);

        // 测试匹配一个不匹配的路径
        boolean matchResult2 = PathUtil.matches(globMatcher, "com.github.test.fashionbrot");
        assertFalse(matchResult2);
    }


    @Test
    public void testNotMatches() {
        // 创建一个glob模式的PathMatcher对象
        PathMatcher globMatcher =PathUtil.getPathMatcher("com.github.fashionbrot");

        // 测试不匹配一个匹配的路径
        boolean notMatchResult1 = PathUtil.notMatches(globMatcher, "com.github.fashionbrot.abc");
        assertFalse(notMatchResult1);

        // 测试不匹配一个不匹配的路径
        boolean notMatchResult2 = PathUtil.notMatches(globMatcher, "com.github.abc");
        assertTrue(notMatchResult2);
    }

}
