export  class TypeHandleFactory {
    static TYPE_HANDLE_MAP = new Map();

    static {
        TypeHandleFactory.addTypeHandle(new IntegerTypeHandle(), ['short','int','long']);
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
}

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
        return encodeVarInteger(value);
    }

    toJava(bytes) {
        return decodeVarInteger(bytes);
    }
}