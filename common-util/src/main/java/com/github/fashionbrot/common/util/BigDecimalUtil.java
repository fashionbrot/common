package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author fashionbrot
 */
@Slf4j
public class BigDecimalUtil {


    private BigDecimalUtil() {
    }

    /**
     * 格式化 BigDecimal 值，将 null 值转换为零值。
     *
     * @param val 要格式化的 BigDecimal 值
     * @return 格式化后的 BigDecimal 值，如果输入为 null 则返回零值
     */
    public static BigDecimal format(final BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }

    /**
     * 将对象格式化为 BigDecimal。
     *
     * @param object 要格式化为 BigDecimal 的对象
     * @return 格式化后的 BigDecimal 对象，如果输入对象为 null 或无法转换为 BigDecimal，则返回 BigDecimal.ZERO
     */
    public static BigDecimal format(final Object object) {
        if (object != null) {
            try {
                return new BigDecimal(object.toString());
            } catch (NumberFormatException e) {
                log.error("formatBigDecimal object:{} error:{}", object, e);
            }
        }
        return BigDecimal.ZERO;
    }


    /**
     * 将 Integer 转换为 BigDecimal，如果为 null 则返回 BigDecimal.ZERO。
     *
     * @param value Integer 值
     * @return 对应的 BigDecimal 值，如果 value 为 null 则返回 BigDecimal.ZERO
     */
    public static BigDecimal format(final Integer value) {
        if (value != null) {
            try {
                return new BigDecimal(Integer.toString(value));
            } catch (NumberFormatException e) {
                log.error("formatBigDecimal value:{} error:{}", value, e);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 将 Long 格式化为 BigDecimal。
     *
     * @param value 要格式化为 BigDecimal 的 Long 值
     * @return 格式化后的 BigDecimal 对象，如果输入值为 null 或无法转换为 BigDecimal，则返回 BigDecimal.ZERO
     */
    public static BigDecimal format(final Long value) {
        if (value != null) {
            try {
                return new BigDecimal(value.toString() );
            } catch (Exception var2) {
                log.error("formatBigDecimal value:{} error:{}", value, var2);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 将 Double 格式化为 BigDecimal。
     *
     * @param value 要格式化为 BigDecimal 的 Double 值
     * @return 格式化后的 BigDecimal 对象，如果输入值为 null，则返回 BigDecimal.ZERO
     */
    public static BigDecimal format(final Double value) {
        if (value != null) {
            try {
                return new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                log.error("formatBigDecimal value:{} error:{}", value, e);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 格式化String 为 BigDecimal
     *
     * @param value value
     * @return BigDecimal
     */
    public static BigDecimal format(final String value) {
        if (ObjectUtil.isNotEmpty(value)) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException exception) {
                log.error("formatBigDecimal value:{} error:{}", value, exception);
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 执行 BigDecimal 减法，同时处理空值情况。
     *
     * @param a1 被减数
     * @param b2 减数
     * @return 减法结果，如果输入为 null 则当作零值处理
     */
    public static BigDecimal subtract(BigDecimal a1, BigDecimal b2) {
        return format(a1).subtract(format(b2));
    }

    /**
     * 执行 BigDecimal 减法，同时处理空值情况。
     *
     * @param a1 被减数
     * @param b2 减数
     * @return 减法结果，如果输入为 null 则当作零值处理
     */
    public static double subtractDouble(Double a1, Double b2) {
        return subtract(format(a1), format(b2)).doubleValue();
    }


    /**
     * 执行 BigDecimal 乘法，默认使用精度为6和舍入模式为HALF_UP。
     *
     * @param a1 被乘数
     * @param b2 乘数
     * @return 乘法结果，如果输入为 null 则当作零值处理
     */
    public static BigDecimal multiply(BigDecimal a1, BigDecimal b2) {
        return multiply(a1, b2, 6, RoundingMode.HALF_UP);
    }


    /**
     * 执行 BigDecimal 乘法，使用指定的乘数和自定义精度，舍入模式默认为 HALF_UP。
     *
     * @param a1       被乘数
     * @param b2       乘数
     * @param newScale 新精度
     * @return 乘法结果，按指定精度和默认舍入模式（HALF_UP）计算，如果输入为 null 则当作零值处理
     */
    public static BigDecimal multiply(BigDecimal a1, BigDecimal b2, int newScale) {
        return multiply(a1, b2, newScale, RoundingMode.HALF_UP);
    }

    /**
     * 执行 BigDecimal 乘法，同时处理空值情况和精度控制。
     *
     * @param a1           被乘数
     * @param b2           乘数
     * @param newScale     新精度
     * @param roundingMode 舍入模式
     * @return 乘法结果，如果输入为 null 则当作零值处理
     */
    public static BigDecimal multiply(BigDecimal a1, BigDecimal b2, int newScale, RoundingMode roundingMode) {
        return format(a1).multiply(format(b2)).setScale(newScale, roundingMode);
    }


    /**
     * 执行双精度浮点数乘法，并限定结果精度为6位小数，使用舍入模式 HALF_UP。
     *
     * @param a1 第一个浮点数因子
     * @param b2 第二个浮点数因子
     * @return 乘法结果，保留6位小数并按照舍入模式 HALF_UP 进行舍入
     */
    public static double multiplyDouble(Double a1, Double b2) {
        return multiply(format(a1), format(b2)).setScale(6, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 执行双精度浮点数乘法，并限定结果精度为指定的小数位数，使用舍入模式 HALF_UP。
     *
     * @param a1       第一个浮点数因子
     * @param b2       第二个浮点数因子
     * @param newScale 新精度，指定结果保留的小数位数
     * @return 乘法结果，按照指定精度和舍入模式 HALF_UP 进行舍入
     */
    public static double multiplyDouble(Double a1, Double b2, int newScale) {
        return multiply(format(a1), format(b2)).setScale(newScale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 执行双精度浮点数乘法，返回 BigDecimal 结果。
     *
     * @param a1 第一个浮点数因子
     * @param b2 第二个浮点数因子
     * @return 乘法结果，作为 BigDecimal 返回
     */
    public static BigDecimal multiply(Double a1, Double b2) {
        return multiply(format(a1), format(b2));
    }

    /**
     * 执行 BigDecimal 相加操作，返回相加结果。
     *
     * @param a1 第一个 BigDecimal 数值
     * @param b2 第二个 BigDecimal 数值
     * @return 相加结果，作为 BigDecimal 返回
     */
    public static BigDecimal add(BigDecimal a1, BigDecimal b2) {
        return format(a1).add(format(b2));
    }

    /**
     * 执行双精度浮点数相加操作，并返回相加结果。
     *
     * @param a1 第一个浮点数操作数
     * @param b2 第二个浮点数操作数
     * @return 相加结果，作为双精度浮点数返回
     */
    public static double addDouble(Double a1, Double b2) {
        return add(format(a1), format(b2)).doubleValue();
    }


    /**
     * 对多个双精度浮点数进行累加操作，并返回累加结果。
     *
     * @param values 多个双精度浮点数操作数（可变参数）
     * @return 累加结果，作为双精度浮点数返回
     */
    public static double add(Double... values) {
        if (ObjectUtil.isEmpty(values)){
            return 0D;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (Double value : values) {
            sum = add(sum, format(value));
        }
        return sum.doubleValue();
    }

    /**
     * 对多个 BigDecimal 进行相加操作，并返回累加结果。
     *
     * @param values 多个 BigDecimal 操作数（可变参数）
     * @return 累加结果，作为 BigDecimal 返回
     */
    public static BigDecimal add(BigDecimal... values) {
        if (ObjectUtil.isEmpty(values)){
            return BigDecimal.ZERO;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
            }
        }
        return sum;
    }


    /**
     * 计算一组 BigDecimal 数值的平均值。
     *
     * @param values 要计算平均值的 BigDecimal 数值数组
     * @return 平均值结果，如果输入数组为空则返回 0
     */
    public static BigDecimal avg(BigDecimal... values) {
        if (ObjectUtil.isEmpty(values)){
            return BigDecimal.ZERO;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
            }
        }
        return divide(sum,format(values.length));
    }

    /**
     * 计算一组双精度浮点数的平均值。
     *
     * @param values 多个双精度浮点数操作数（可变参数）
     * @return 平均值，作为双精度浮点数返回
     */
    public static double avg(Double... values) {
        if (ObjectUtil.isEmpty(values)){
            return 0D;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (Double value : values) {
            sum = add(sum, format(value));
        }
        return divide(sum,format(values.length)).doubleValue();
    }


    /**
     * 执行 BigDecimal 除法运算，默认使用精度为6和舍入模式为HALF_UP。
     *
     * @param a1 被除数
     * @param b2 除数
     * @return 除法结果，如果被除数或除数为零则返回 BigDecimal.ZERO
     */
    public static BigDecimal divide(BigDecimal a1, BigDecimal b2) {
        return divide(a1,b2,6,RoundingMode.HALF_UP);
    }


    /**
     * 执行 BigDecimal 除法运算，并指定结果的精度为 newScale，舍入模式为 HALF_UP。
     *
     * @param a1 被除数
     * @param b2 除数
     * @param newScale 新精度，指定结果的小数位数
     * @return 除法结果，如果被除数或除数为零则返回 BigDecimal.ZERO
     */
    public static BigDecimal divide(BigDecimal a1, BigDecimal b2,int newScale) {
        return divide(a1,b2,newScale,RoundingMode.HALF_UP);
    }

    /**
     * 执行 BigDecimal 除法运算，可以指定精度和舍入方式，同时处理除数为零的情况。
     *
     * @param a1 被除数
     * @param b2 除数
     * @param newScale 新精度，指定结果的小数位数
     * @param roundingMode 舍入模式，用于指定舍入规则
     * @return 除法结果，如果被除数或除数为零则返回 BigDecimal.ZERO
     */
    public static BigDecimal divide(BigDecimal a1, BigDecimal b2,int newScale,RoundingMode roundingMode) {
        if (a1==null && b2==null){
            return BigDecimal.ZERO;
        }
        BigDecimal a = format(a1);
        BigDecimal b = format(b2);
        // 检查被除数和除数是否为零，如果是零则直接返回 BigDecimal.ZERO
        if (a.compareTo(BigDecimal.ZERO) ==0 || b.compareTo(BigDecimal.ZERO)==0){
            return BigDecimal.ZERO;
        }
        return a.divide(b, newScale,roundingMode);
    }

    /**
     * 执行双精度浮点数除法，默认使用精度为6和舍入模式为HALF_UP。
     *
     * @param a1 被除数
     * @param b2 除数
     * @return 除法结果，如果被除数或除数为零则返回 BigDecimal.ZERO
     */
    public static BigDecimal divide(Double a1, Double b2) {
        return divide(format(a1),format(b2));
    }


    /**
     * 执行双精度浮点数除法，并指定结果的精度为 newScale。
     *
     * @param a1 被除数
     * @param b2 除数
     * @param newScale 新精度，指定结果的小数位数
     * @return 除法结果，如果被除数或除数为零则返回 BigDecimal.ZERO
     */
    public static BigDecimal divide(Double a1, Double b2,int newScale) {
        return divide(format(a1),format(b2),newScale);
    }

    /**
     * 执行双精度浮点数除法，默认使用精度为6和舍入模式为HALF_UP。
     *
     * @param a1 被除数
     * @param b2 除数
     * @return 除法结果，如果被除数或除数为零则返回 0.0
     */
    public static double divideDouble(Double a1, Double b2) {
        return divide(a1,b2).doubleValue();
    }

    /**
     * 执行双精度浮点数除法，并将结果转换为双精度浮点数。
     *
     * @param a1 被除数
     * @param b2 除数
     * @param newScale 新精度，指定结果的小数位数
     * @return 除法结果的双精度浮点数表示，如果被除数或除数为零则返回 0D
     */
    public static double divideDouble(Double a1, Double b2, int newScale) {
        return divide(a1,b2,newScale).doubleValue();
    }

    /**
     * 格式化对象为双精度浮点数，并指定结果的精度。
     *
     * @param value 要格式化的对象，可以是 Double、Integer 等数值类型
     * @param scale 新精度，指定结果的小数位数
     * @return 格式化后的双精度浮点数，如果输入对象为 null 则返回 0.0D
     */
    public static double format(Object value, int scale) {
        if (value == null) {
            return 0.0D;
        }
        BigDecimal v = format(value);
        return v.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 格式化双精度浮点数为指定精度的双精度浮点数。
     *
     * @param value 要格式化的双精度浮点数
     * @param scale 新精度，指定结果的小数位数
     * @return 格式化后的双精度浮点数，如果输入值为 null 则返回 0.00D
     */
    public static double format(Double value, int scale) {
        if (value == null) {
            return 0.00D;
        }
        BigDecimal v = format(value);
        return v.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 将 BigDecimal 转换为双精度浮点数。
     *
     * @param value 要转换的 BigDecimal 值
     * @return 转换后的双精度浮点数，如果输入值为 null 则返回 0.0D
     */
    public static double formatDouble(BigDecimal value) {
        if (value == null) {
            return 0.0D;
        }
        return value.doubleValue();
    }

    /**
     * 将 BigDecimal 转换为指定精度的双精度浮点数。
     *
     * @param value 要转换的 BigDecimal 值
     * @param newScale 新精度，指定结果的小数位数
     * @return 转换后的双精度浮点数，如果输入值为 null 则返回 0.0D
     */
    public static double formatDouble(BigDecimal value, int newScale) {
        return format(value,newScale).doubleValue();
    }

    /**
     * 格式化 BigDecimal 值为指定精度的 BigDecimal。
     *
     * @param value 要格式化的 BigDecimal 值
     * @param newScale 新精度，指定结果的小数位数
     * @return 格式化后的 BigDecimal，如果输入值为 null 则返回 BigDecimal.ZERO
     */
    public static BigDecimal format(BigDecimal value, int newScale) {
        return format(value,newScale,RoundingMode.HALF_UP);
    }

    /**
     * 格式化 BigDecimal 对象，并指定新的小数位数和舍入模式。
     *
     * @param value           要格式化的 BigDecimal 对象
     * @param newScale        新的小数位数
     * @param roundingMode    舍入模式
     * @return 格式化后的 BigDecimal 对象
     */
    public static BigDecimal format(BigDecimal value, int newScale ,RoundingMode roundingMode) {
        return format(value).setScale(newScale, roundingMode);
    }

    /**
     * 格式化 BigDecimal 值为指定精度的 BigDecimal。
     *
     * @param value 要格式化的 BigDecimal 值
     * @param newScale 新精度，指定结果的小数位数
     * @return 格式化后的 BigDecimal，如果输入值为 null 则返回 BigDecimal.ZERO
     */
    public static BigDecimal formatBigDecimal(Double value, int newScale) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal v = format(value);
        return v.setScale(newScale, RoundingMode.HALF_UP);
    }

    /**
     * 格式化字符串为指定精度的 BigDecimal。
     *
     * @param value 要格式化的字符串
     * @param newScale 新精度，指定结果的小数位数
     * @return 格式化后的 BigDecimal，如果输入字符串为空则返回 BigDecimal.ZERO
     */
    public static BigDecimal format(String value, int newScale) {
        return format(value,newScale,RoundingMode.HALF_UP);
    }

    /**
     * 格式化字符串为 BigDecimal 对象，并指定新的小数位数和舍入模式。
     *
     * @param value           要格式化的字符串值
     * @param newScale        新的小数位数
     * @param roundingMode    舍入模式
     * @return 格式化后的 BigDecimal 对象
     */
    public static BigDecimal format(String value, int newScale ,RoundingMode roundingMode) {
        if (ObjectUtil.isEmpty(value)) {
            return BigDecimal.ZERO;
        }
        BigDecimal v = format(value);
        return v.setScale(newScale, roundingMode);
    }

    /**
     * 格式化字符串为指定精度的双精度浮点数。
     *
     * @param value 要格式化的字符串
     * @param newScale 新精度，指定结果的小数位数
     * @return 格式化后的双精度浮点数，如果输入字符串为空则返回 0.00D
     */
    public static double formatDouble(String value, int newScale) {
        return format(value,newScale).doubleValue();
    }


    /**
     * 比较两个 BigDecimal 数值是否相等。
     *
     * @param a1 第一个 BigDecimal 数值
     * @param b2 第二个 BigDecimal 数值
     * @return 如果两个数值相等，返回 true，否则返回 false；如果任一数值为 null，也返回 false
     */
    public static boolean equals(BigDecimal a1 ,BigDecimal b2){
        if (a1==null || b2==null){
            return false;
        }
        return a1.compareTo(b2)==0;
    }

    /**
     * 比较两个BigDecimal对象是否相等，忽略空值。
     *
     * @param b1 第一个BigDecimal对象
     * @param b2 第二个BigDecimal对象
     * @return 如果两个对象相等（忽略空值），则返回true；否则返回false。
     */
    public static boolean equalsIgnoreNull(BigDecimal b1,BigDecimal b2){
        return format(b1).compareTo(format(b2))==0;
    }

    /**
     * 比较两个BigDecimal对象的大小。
     *
     * @param b1 第一个BigDecimal对象
     * @param b2 第二个BigDecimal对象
     * @return 如果b1大于b2，返回正数；如果b1等于b2，返回0；如果b1小于b2，返回负数。
     */
    public static int compareTo(BigDecimal b1 ,BigDecimal b2){
        return format(b1).compareTo(format(b2));
    }

    /**
     * 比较两个 Double 值是否相等。
     *
     * @param a1 第一个 Double 值
     * @param b2 第二个 Double 值
     * @return 如果两个 Double 值相等则返回 true，否则返回 false
     */
    public static boolean equals(Double a1,Double b2){
        return equals(format(a1),format(b2));
    }


    /**
     * 比较一个 BigDecimal 值和一个 Double 值是否相等。
     *
     * @param a1 第一个 BigDecimal 值
     * @param b2 第二个 Double 值
     * @return 如果两个值相等则返回 true，否则返回 false
     */
    public static boolean equals(BigDecimal a1,Double b2){
        return equals(format(a1),format(b2));
    }


    /**
     * 返回一个数的相反数。
     *
     * @param value 要取反的数
     * @return 输入数的相反数
     */
    public static BigDecimal negate(BigDecimal value){
        return format(value).negate();
    }



    /**
     * 返回两个 BigDecimal 值中的最小值。
     *
     * @param value1 第一个 BigDecimal 值。
     * @param value2 第二个 BigDecimal 值。
     * @return value1 和 value2 中的最小值。
     */
    public static BigDecimal min(BigDecimal value1, BigDecimal value2) {
        if (format(value1).compareTo(format(value2)) < 0) {
            return value1;
        } else {
            return value2;
        }
    }

    /**
     * 返回两个 BigDecimal 值中的最大值。
     *
     * @param value1 第一个 BigDecimal 值。
     * @param value2 第二个 BigDecimal 值。
     * @return value1 和 value2 中的最大值。
     */
    public static BigDecimal max(BigDecimal value1, BigDecimal value2) {
        if (format(value1).compareTo(format(value2)) > 0) {
            return value1;
        } else {
            return value2;
        }
    }

}
