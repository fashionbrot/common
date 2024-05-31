function encodeVarInteger(value) {
    const output = [];
    while ((value & 0xFFFFFF80) !== 0) {
        output.push((value & 0x7F) | 0x80);
        value >>>= 7;
    }
    output.push(value & 0x7F);
    return output;
}

function decodeVarInteger(data) {
    let result = 0;
    let shift = 0;
    let index = 0;
    let b;
    do {
        if (index >= data.length) {
            throw new Error("Varint decoding error: Insufficient data");
        }
        b = data[index++];
        result |= (b & 0x7F) << shift;
        shift += 7;
    } while ((b & 0x80) !== 0);
    return result;
}

// 测试
// const encoded = encodeVarInteger(300);
// console.log(encoded); // 输出: [172, 2]
// console.log(decodeVarInteger(encoded)); // 输出: 300
