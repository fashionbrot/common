package com.github.fashionbrot.common.tlv;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fashionbrot
 */
@AllArgsConstructor
@Getter
public  enum BinaryType{

    /**
     * 00000 (0)
     * 00001 (1)
     * 00010 (2)
     * 00011 (3)
     * 00100 (4)
     * 00101 (5)
     * 00110 (6)
     * 00111 (7)
     * 01000 (8)
     * 01001 (9)
     * 01010 (10)
     * 01011 (11)
     * 01100 (12)
     * 01101 (13)
     * 01110 (14)
     * 01111 (15)
     *
     * 10000 (16)
     * 10001 (17)
     * 10010 (18)
     * 10011 (19)
     * 10100 (20)
     * 10101 (21)
     * 10110 (22)
     * 10111 (23)
     * 11000 (24)
     * 11001 (25)
     * 11010 (26)
     * 11011 (27)
     * 11100 (28)
     * 11101 (29)
     * 11110 (30)
     * 11111 (31)
     */
    BOOLEAN("00000",boolean.class),
    BYTE("00001",byte.class),
    CHAR("00010",char.class),
    SHORT("00011",short.class),
    INTEGER("00100",int.class),
    FLOAT("00101",float.class),
    LONG("00110",long.class),
    DOUBLE("00111",double.class),
    STRING("01000",String.class),
    DATE("01001", Date.class),
    LOCAL_TIME("01010", LocalTime.class),
    LOCAL_DATE("01011", LocalDate.class),
    LOCAL_DATE_TIME("01100", LocalDateTime.class),
    BIG_DECIMAL("01101", BigDecimal.class),
    ARRAY("01110", Array.class),
    LIST("01111", List.class),
    ;

    private final String binaryCode;
    private final Class<?> type;
    private static final Map<String, BinaryType> BINARY_CODE_MAP = new HashMap<>();
    private static final Map<Class<?>, BinaryType> TYPE_MAP = new HashMap<>();

    static {
        for (BinaryType binaryType : values()) {
            BINARY_CODE_MAP.put(binaryType.getBinaryCode(), binaryType);
            TYPE_MAP.put(binaryType.getType(), binaryType);
        }

        // Add primitive types and their wrappers
        addPrimitiveTypeMappings();
    }

    private static void addPrimitiveTypeMappings() {
        TYPE_MAP.put(boolean.class, BOOLEAN);
        TYPE_MAP.put(Boolean.class, BOOLEAN);

        TYPE_MAP.put(byte.class, BYTE);
        TYPE_MAP.put(Byte.class, BYTE);

        TYPE_MAP.put(char.class, CHAR);
        TYPE_MAP.put(Character.class, CHAR);

        TYPE_MAP.put(short.class, SHORT);
        TYPE_MAP.put(Short.class, SHORT);

        TYPE_MAP.put(int.class, INTEGER);
        TYPE_MAP.put(Integer.class, INTEGER);

        TYPE_MAP.put(float.class, FLOAT);
        TYPE_MAP.put(Float.class, FLOAT);

        TYPE_MAP.put(long.class, LONG);
        TYPE_MAP.put(Long.class, LONG);

        TYPE_MAP.put(double.class, DOUBLE);
        TYPE_MAP.put(Double.class, DOUBLE);

        TYPE_MAP.put(String.class,STRING);
        TYPE_MAP.put(CharSequence.class,STRING);
    }

    /**
     * Retrieves the BinaryType corresponding to the given binary code.
     *
     * @param binaryCode the binary code as a string
     * @return the corresponding BinaryType
     * @throws IllegalArgumentException if no BinaryType is found for the given binary code
     */
    public static BinaryType fromBinaryCode(String binaryCode) {
        BinaryType result = BINARY_CODE_MAP.get(binaryCode);
        if (result == null) {
            throw new IllegalArgumentException("No enum constant found for binary code: " + binaryCode);
        }
        return result;
    }

    /**
     * Retrieves the BinaryType corresponding to the given Java class.
     *
     * @param type the Java class
     * @return the corresponding BinaryType
     * @throws IllegalArgumentException if the type is not supported
     */
    public static BinaryType getBinaryType(Class<?> type) {
        BinaryType binaryType = TYPE_MAP.get(type);
        if (binaryType != null) {
            return binaryType;
        }

        if (List.class.isAssignableFrom(type)) {
            return LIST;
        }

        if (type.isArray()) {
            return ARRAY;
        }

        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
