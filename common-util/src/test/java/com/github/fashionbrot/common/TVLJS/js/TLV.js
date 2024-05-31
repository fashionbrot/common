
function serialize(input){


}



function deserialize(object,buffer){
    if (object==null){
        return null;
    }
    let reader = new ByteArrayReader(buffer);

    let objectType = determineType(object);
    if ('object' == objectType){
        const keys = Object.keys(object).sort();
        keys.flatMap(key => {
            let readIndex = reader.getLastReadIndex();
            let TAG = reader.readFrom(readIndex);
            let TAGBit = byteToBits(TAG);
            console.log(TAGBit);
            let TAGType = getTypeFromBinaryCode(TAGBit);
            let TAGValueLengthLength = getLengthFromBinaryCode(TAGBit);
            reader.setLastBinaryType(TAGType);

            let valueByteLength = decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + TAGValueLengthLength));

            let valueBuffer = reader.readFromTo(readIndex +1+ TAGValueLengthLength, readIndex +1 + TAGValueLengthLength+ valueByteLength)

            let value = TypeHandleFactory.toJava(TAGType,valueBuffer);

            console.log("TAGType:"+TAGType+" TAGValueLengthLength:"+TAGValueLengthLength+" valueByteLength:"+valueByteLength +" value:"+value)
            // TypeHandleFactory.toJava()
        })
    }
    return null;
}

function determineType(input) {
    if (typeof input === 'string') return 'string';
    if (typeof input === 'number') return 'number';
    if (typeof input === 'boolean') return 'boolean';
    if (Array.isArray(input)) return 'array';
    if (typeof input === 'object') return 'object';
    if (typeof input === 'bigint') return 'bigint';
    throw new Error('Unsupported type: ' + typeof input);
}

function byteToBits(byte) {
    // 将字节转换为二进制字符串，并补齐到8位
    let bits = byte.toString(2).padStart(8, '0');
    return bits;
}