package com.github.fashionbrot.common.system;


import cn.hutool.system.SystemUtil;

/**
 * @author fashionbrot
 */
public class OsUtil {


    private final String OS_NAME = SystemUtil.get("os.name", false);
    private final boolean IS_OS_LINUX = getOSMatches("Linux") || getOSMatches("LINUX");
    private final boolean IS_OS_MAC = getOSMatches("Mac");
    private final boolean IS_OS_WINDOWS = getOSMatches("Windows");


    public static String getProperty(String key){
        try {
            return System.getProperty(key);
        } catch (SecurityException e) {
            return null;
        }
    }


    /**
     * 匹配OS名称。
     *
     * @param osNamePrefix OS名称前缀
     *
     * @return 如果匹配，则返回<code>true</code>
     */
    private final boolean getOSMatches(String osNamePrefix) {
        if (OS_NAME == null) {
            return false;
        }
        return OS_NAME.startsWith(osNamePrefix);
    }


    /**
     * 判断当前OS的类型。
     *
     * <p>
     * 如果不能取得系统属性<code>os.name</code>（因为Java安全限制），则总是返回<code>false</code>
     * </p>
     *
     * @return 如果当前OS类型为Linux，则返回<code>true</code>
     */
    public final boolean isLinux() {
        return IS_OS_LINUX;
    }

    /**
     * 判断当前OS的类型。
     *
     * <p>
     * 如果不能取得系统属性<code>os.name</code>（因为Java安全限制），则总是返回<code>false</code>
     * </p>
     *
     * @return 如果当前OS类型为Mac，则返回<code>true</code>
     */
    public final boolean isMac() {
        return IS_OS_MAC;
    }


    /**
     * 判断当前OS的类型。
     *
     * <p>
     * 如果不能取得系统属性<code>os.name</code>（因为Java安全限制），则总是返回<code>false</code>
     * </p>
     *
     * @return 如果当前OS类型为Windows，则返回<code>true</code>
     */
    public final boolean isWindows() {
        return IS_OS_WINDOWS;
    }



}