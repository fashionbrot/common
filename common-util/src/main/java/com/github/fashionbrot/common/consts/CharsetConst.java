package com.github.fashionbrot.common.consts;

import cn.hutool.core.util.CharsetUtil;

import java.nio.charset.Charset;

public final class CharsetConst {

    /** 默认编码，使用平台相关编码 */
    public static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

    public static final String UTF_8 = "UTF-8";

    public static final String ISO_8859_1 = "ISO-8859-1";

    public static final String GBK = "GBK";

}
