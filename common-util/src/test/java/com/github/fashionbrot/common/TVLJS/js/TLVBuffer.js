(function (global, factory) {
    if (typeof module === "object" && typeof module.exports === "object") {
        module.exports = factory(global);
    } else {
        factory(global);
    }
}(typeof window !== "undefined" ? window : this, function (window) {
    'use strict';

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

    let TLVBuffer = (function (){
        function TVLBuffer(){
            this.init();
        }

        TVLBuffer.prototype.init = function (){
            console.log("TVLBuffer init");
        };

        TVLBuffer.prototype.deserialize = function (object,buffer){
            if (object==null || !buffer){
                return null;
            }
            let objectType = Util.determineType(object);
            if ('object' == objectType){
                return deserialize.deserializeEntity(object,buffer);
            }

        };

        let deserialize = {
            deserializeEntity:function (object,buffer){
                let reader = new ByteArrayReader(buffer);
                const entity = Object.keys(object).sort();
                let returnObject = new Object();
                for (const key of entity) {

                    let readIndex = reader.getLastReadIndex();
                    let TAG = reader.readFrom(readIndex);
                    let TAGBit = Util.byteToBits(TAG);
                    console.log(TAGBit);

                    let TAGType = BinaryTypeUtil.getTypeFromBinaryCode(TAGBit);
                    let TAGValueLengthLength =BinaryCodeLengthUtil.getLengthFromBinaryCode(TAGBit);
                    reader.setLastBinaryType(TAGType);

                    let valueByteLength = TypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + TAGValueLengthLength));

                    let valueBuffer = reader.readFromTo(readIndex +1+ TAGValueLengthLength, readIndex +1 + TAGValueLengthLength+ valueByteLength)

                    let value = TypeHandleFactory.toJava(TAGType,valueBuffer);

                    returnObject[key] = value;
                    console.log("TAGType:"+TAGType+" TAGValueLengthLength:"+TAGValueLengthLength+" valueByteLength:"+valueByteLength +" value:"+value)
                }
                return returnObject;
            }
        };



        let TypeUtil = {
            encodeVarInteger:function (value) {
                const output = [];
                while ((value & 0xFFFFFF80) !== 0) {
                    output.push((value & 0x7F) | 0x80);
                    value >>>= 7;
                }
                output.push(value & 0x7F);
                return new Uint8Array(output);
            },decodeVarInteger:function (data) {
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
            },encodeVarShort:function (value) {
                let output = [];
                while ((value & 0xFFFFFF80) !== 0) {
                    output.push((value & 0x7F) | 0x80);
                    value >>>= 7;
                }
                output.push(value & 0x7F);
                return new Uint8Array(output);
            }, decodeVarShort:function (data) {
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
            },
            encodeVarLong:function (value) {
                let output = [];
                while ((value & 0xFFFFFFFFFFFFFF80) !== 0) {
                    output.push((value & 0x7F) | 0x80);
                    value >>>= 7;
                }
                output.push(value & 0x7F);
                return new Uint8Array(output);
            },decodeVarLong:function (data) {
                let result = BigInt(0);
                let shift = BigInt(0);
                let index = 0;
                let b;
                do {
                    if (index >= data.length) {
                        throw new Error("Varint 解码错误：数据不足");
                    }
                    b = BigInt(data[index++]);
                    result |= (b & BigInt(0x7F)) << shift;
                    shift += BigInt(7);
                } while ((b & BigInt(0x80)) !== BigInt(0));
                // 检查最高位是否为 1，如果是，说明为负数，需要转换为负数表示
                if (result & (BigInt(1) << BigInt(63))) {
                    result -= BigInt(1) << BigInt(64);
                }
                return result;
            },
            encodeVarFloat:function (value) {
                let intValue = Float32Array.from([value])[0];
                return this.encodeVarInteger(intValue);
            },decodeVarFloat:function (data) {
                let intValue = this.decodeVarInteger(data);
                let floatArray = new Float32Array([intValue]); // Convert 32-bit integer to float
                return floatArray[0];
            }

        };


        let  BinaryTypeUtil = {
            // 从二进制代码获取类型
            getTypeFromBinaryCode:function (binaryCode) {
                binaryCode = binaryCode.substring(0,5);
                for (const key in BinaryType) {
                    if (BinaryType[key].binaryCode === binaryCode) {
                        return BinaryType[key].type;
                    }
                }
                throw new Error("No type found for binary code: " + binaryCode);
                //// 从类型获取二进制代码
            },getBinaryCodeFromType:function (type) {
                for (const key in BinaryType) {
                    if (BinaryType[key].type === type) {
                        return BinaryType[key].binaryCode;
                    }
                }
                throw new Error("No binary code found for type: " + type);
            }
        };

        let BinaryCodeLengthUtil = {
            getLengthFromBinaryCode:function (binaryCode) {
                binaryCode = binaryCode.substring(5,8);
                for (const key in BinaryCodeLength) {
                    if (BinaryCodeLength[key].binaryCode === binaryCode) {
                        return BinaryCodeLength[key].length;
                    }
                }
                throw new Error("Invalid binary code: " + binaryCode);
            },getBinaryCodeFromLength:function (length) {
                for (const key in BinaryCodeLength) {
                    if (BinaryCodeLength[key].length === length) {
                        return BinaryCodeLength[key].binaryCode;
                    }
                }
                throw new Error("Invalid length: " + length);
            }
        };


        let Util = {
            determineType: function (input) {
                if (typeof input === 'string') return 'string';
                if (typeof input === 'number') return 'number';
                if (typeof input === 'boolean') return 'boolean';
                if (Array.isArray(input)) return 'array';
                if (typeof input === 'object') return 'object';
                if (typeof input === 'bigint') return 'bigint';
                throw new Error('Unsupported type: ' + typeof input);
            }, byteToBits : function (byte) {
                // 将字节转换为二进制字符串，并补齐到8位
                let bits = byte.toString(2).padStart(8, '0');
                return bits;
            },
            floatToIntBits:function (value) {
                let floatArray = new Float32Array(1);
                floatArray[0] = value;
                let intArray = new Int32Array(floatArray.buffer);
                return intArray[0];
            },
            // 将 32 位整数转换为浮点数
            intBitsToFloat:function (value) {
                let intArray = new Int32Array(1);
                intArray[0] = value;
                let floatArray = new Float32Array(intArray.buffer);
                return floatArray[0];
            }

    };


        class TypeHandle {
            toByte(value) {
                throw new Error("Method 'toByte()' must be implemented.");
            }

            toJava(bytes) {
                throw new Error("Method 'toJava()' must be implemented.");
            }
        }
        class IntegerTypeHandle extends TypeHandle {
            toByte(value) {
                return TypeUtil.encodeVarInteger(value);
            }
            toJava(bytes) {
                return TypeUtil.decodeVarInteger(bytes);
            }
        }
        class ShortTypeHandle extends TypeHandle {
            toByte(value) {
                return TypeUtil.encodeVarShort(value);
            }
            toJava(bytes) {
                return TypeUtil.decodeVarShort(bytes);
            }
        }
        class LongTypeHandle extends TypeHandle {
            toByte(value) {
                return TypeUtil.encodeVarLong(value);
            }
            toJava(bytes) {
                return TypeUtil.decodeVarLong(bytes);
            }
        }
        class FloatTypeHandle extends TypeHandle {
            toByte(value) {
                return TypeUtil.encodeVarFloat(value);
            }
            toJava(bytes) {
                return TypeUtil.decodeVarFloat(bytes);
            }
        }

        class TypeHandleFactory {
            static TYPE_HANDLE_MAP = new Map();

            static {
                TypeHandleFactory.addTypeHandle(new IntegerTypeHandle(), ['int']);
                TypeHandleFactory.addTypeHandle(new ShortTypeHandle(),['short']);
                TypeHandleFactory.addTypeHandle(new LongTypeHandle(),['long']);
                TypeHandleFactory.addTypeHandle(new FloatTypeHandle(),['float']);
                // Add other type handles similarly if needed.
            }

            static addTypeHandle(handle, classes) {
                if (classes && classes.length > 0) {
                    for (const clazz of classes) {
                        if (!TypeHandleFactory.TYPE_HANDLE_MAP.has(clazz)) {
                            TypeHandleFactory.TYPE_HANDLE_MAP.set(clazz, handle);
                        }
                    }
                }
            }

            static getTypeHandle(type) {
                const typeHandle = TypeHandleFactory.TYPE_HANDLE_MAP.get(type);
                if (typeHandle) {
                    return typeHandle;
                }
                throw new Error("Unsupported type: " + type);
            }

            static toJava(type, bytes) {
                if (!bytes || bytes.length === 0) {
                    return TypeHandleFactory.getDefaultForType(type);
                }
                const typeHandle = TypeHandleFactory.getTypeHandle(type);
                return typeHandle.toJava(bytes);
            }

            static toByte(type, value) {
                const typeHandle = TypeHandleFactory.getTypeHandle(type);
                return typeHandle.toByte(value);
            }

            static getDefaultForType(type) {
                if (type === Boolean) return false;
                if (type === Number) return 0;
                if (type === String) return '';
                if (type === BigInt) return BigInt(0);
                if (type === Date) return new Date(0);
                if (type === Array) return [];
                if (type === Object) return {};
                // Add more default types as needed.
                return null;
            }
        };

        class ByteArrayReader {
            constructor(data) {
                this.data = data;
                this.lastReadIndex = 0; // 初始化为0
                this.lastBinaryType = null;
            }

            readFrom(end) {
                if (end >= this.data.length || end < 0) {
                    throw new Error("Start or end index out of bounds");
                }
                this.lastReadIndex = end;
                return this.data[end];
            }

            readFromTo(start, end) {
                if (start < 0 || start > end || end > this.data.length) {
                    throw new Error("Start or end index out of bounds");
                }
                this.lastReadIndex = end;
                return this.data.slice(start, end);
            }

            getLastReadIndex() {
                return this.lastReadIndex;
            }

            getLastBinaryType() {
                return this.lastBinaryType;
            }

            setLastBinaryType(lastBinaryType) {
                this.lastBinaryType = lastBinaryType;
            }

            isReadComplete() {
                return this.lastReadIndex === this.data.length;
            }
        };


        return TVLBuffer;
    }());

    // 将TLVBuffer对象挂载到window对象上，使其可以在全局访问
    window.TLVBuffer = TLVBuffer;
}));