// 枚举类
const BinaryType = {
    BOOLEAN: { binaryCode: "00000", type: "boolean" },
    BYTE: { binaryCode: "00001", type: "byte" },
    CHAR: { binaryCode: "00010", type: "char" },
    SHORT: { binaryCode: "00011", type: "short" },
    INTEGER: { binaryCode: "00100", type: "int" },
    FLOAT: { binaryCode: "00101", type: "float" },
    LONG: { binaryCode: "00110", type: "long" },
    DOUBLE: { binaryCode: "00111", type: "double" },
    STRING: { binaryCode: "01000", type: "String" },
    DATE: { binaryCode: "01001", type: "Date" },
    LOCAL_TIME: { binaryCode: "01010", type: "LocalTime" },
    LOCAL_DATE: { binaryCode: "01011", type: "LocalDate" },
    LOCAL_DATE_TIME: { binaryCode: "01100", type: "LocalDateTime" },
    BIG_DECIMAL: { binaryCode: "01101", type: "BigDecimal" },
    ARRAY: { binaryCode: "01110", type: "Array" },
    LIST: { binaryCode: "01111", type: "List" }
};

// 从二进制代码获取类型
function getTypeFromBinaryCode(binaryCode) {
    binaryCode = binaryCode.substring(0,5);
    for (const key in BinaryType) {
        if (BinaryType[key].binaryCode === binaryCode) {
            return BinaryType[key].type;
        }
    }
    throw new Error("No type found for binary code: " + binaryCode);
}

// 从类型获取二进制代码
function getBinaryCodeFromType(type) {
    for (const key in BinaryType) {
        if (BinaryType[key].type === type) {
            return BinaryType[key].binaryCode;
        }
    }
    throw new Error("No binary code found for type: " + type);
}

// 测试
// console.log(getTypeFromBinaryCode("01001")); // 输出: Date
// console.log(getBinaryCodeFromType("String")); // 输出: 01000
