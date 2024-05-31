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
}

// 测试
// const byteArray = new ByteArrayReader([1, 2, 3, 4, 5]);
// console.log(byteArray.readFrom(2)); // 输出: 3
// console.log(byteArray.readFromTo(1, 4)); // 输出: [2, 3, 4]
// console.log(byteArray.getLastReadIndex()); // 输出: 4
// console.log(byteArray.isReadComplete()); // 输出: false
