// 枚举类
const BinaryCodeLength = {
    B1: { binaryCode: "000", length: 1 },
    B2: { binaryCode: "001", length: 2 },
    B3: { binaryCode: "010", length: 3 },
    B4: { binaryCode: "011", length: 4 },
    B5: { binaryCode: "100", length: 5 },
    B6: { binaryCode: "101", length: 6 },
    B7: { binaryCode: "110", length: 7 },
    B8: { binaryCode: "111", length: 8 }
};

// 获取长度
function getLengthFromBinaryCode(binaryCode) {
    for (const key in BinaryCodeLength) {
        if (BinaryCodeLength[key].binaryCode === binaryCode) {
            return BinaryCodeLength[key].length;
        }
    }
    throw new Error("Invalid binary code: " + binaryCode);
}

// 获取二进制代码
function getBinaryCodeFromLength(length) {
    for (const key in BinaryCodeLength) {
        if (BinaryCodeLength[key].length === length) {
            return BinaryCodeLength[key].binaryCode;
        }
    }
    throw new Error("Invalid length: " + length);
}

// 测试
console.log(getLengthFromBinaryCode("010")); // 输出: 3
console.log(getBinaryCodeFromLength(5)); // 输出: 100
