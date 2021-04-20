begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2015 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.hash
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|hash
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|primitives
operator|.
name|Longs
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteOrder
import|;
end_import

begin_import
import|import
name|sun
operator|.
name|misc
operator|.
name|Unsafe
import|;
end_import

begin_comment
comment|/**  * Utility functions for loading and storing values from a byte array.  *  * @author Kevin Damm  * @author Kyle Maddison  */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|LittleEndianByteArray
specifier|final
class|class
name|LittleEndianByteArray
block|{
comment|/** The instance that actually does the work; delegates to Unsafe or a pure-Java fallback. */
DECL|field|byteArray
specifier|private
specifier|static
specifier|final
name|LittleEndianBytes
name|byteArray
decl_stmt|;
comment|/**    * Load 8 bytes into long in a little endian manner, from the substring between position and    * position + 8. The array must have at least 8 bytes from offset (inclusive).    *    * @param input the input bytes    * @param offset the offset into the array at which to start    * @return a long of a concatenated 8 bytes    */
DECL|method|load64 (byte[] input, int offset)
specifier|static
name|long
name|load64
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
comment|// We don't want this in production code as this is the most critical part of the loop.
assert|assert
name|input
operator|.
name|length
operator|>=
name|offset
operator|+
literal|8
assert|;
comment|// Delegates to the fast (unsafe) version or the fallback.
return|return
name|byteArray
operator|.
name|getLongLittleEndian
argument_list|(
name|input
argument_list|,
name|offset
argument_list|)
return|;
block|}
comment|/**    * Similar to load64, but allows offset + 8> input.length, padding the result with zeroes. This    * has to explicitly reverse the order of the bytes as it packs them into the result which makes    * it slower than the native version.    *    * @param input the input bytes    * @param offset the offset into the array at which to start reading    * @param length the number of bytes from the input to read    * @return a long of a concatenated 8 bytes    */
DECL|method|load64Safely (byte[] input, int offset, int length)
specifier|static
name|long
name|load64Safely
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|long
name|result
init|=
literal|0
decl_stmt|;
comment|// Due to the way we shift, we can stop iterating once we've run out of data, the rest
comment|// of the result already being filled with zeros.
comment|// This loop is critical to performance, so please check HashBenchmark if altering it.
name|int
name|limit
init|=
name|Math
operator|.
name|min
argument_list|(
name|length
argument_list|,
literal|8
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|limit
condition|;
name|i
operator|++
control|)
block|{
comment|// Shift value left while iterating logically through the array.
name|result
operator||=
operator|(
name|input
index|[
name|offset
operator|+
name|i
index|]
operator|&
literal|0xFFL
operator|)
operator|<<
operator|(
name|i
operator|*
literal|8
operator|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**    * Store 8 bytes into the provided array at the indicated offset, using the value provided.    *    * @param sink the output byte array    * @param offset the offset into the array at which to start writing    * @param value the value to write    */
DECL|method|store64 (byte[] sink, int offset, long value)
specifier|static
name|void
name|store64
parameter_list|(
name|byte
index|[]
name|sink
parameter_list|,
name|int
name|offset
parameter_list|,
name|long
name|value
parameter_list|)
block|{
comment|// We don't want to assert in production code.
assert|assert
name|offset
operator|>=
literal|0
operator|&&
name|offset
operator|+
literal|8
operator|<=
name|sink
operator|.
name|length
assert|;
comment|// Delegates to the fast (unsafe)version or the fallback.
name|byteArray
operator|.
name|putLongLittleEndian
argument_list|(
name|sink
argument_list|,
name|offset
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**    * Load 4 bytes from the provided array at the indicated offset.    *    * @param source the input bytes    * @param offset the offset into the array at which to start    * @return the value found in the array in the form of a long    */
DECL|method|load32 (byte[] source, int offset)
specifier|static
name|int
name|load32
parameter_list|(
name|byte
index|[]
name|source
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
comment|// TODO(user): Measure the benefit of delegating this to LittleEndianBytes also.
return|return
operator|(
name|source
index|[
name|offset
index|]
operator|&
literal|0xFF
operator|)
operator||
operator|(
operator|(
name|source
index|[
name|offset
operator|+
literal|1
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|8
operator|)
operator||
operator|(
operator|(
name|source
index|[
name|offset
operator|+
literal|2
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|16
operator|)
operator||
operator|(
operator|(
name|source
index|[
name|offset
operator|+
literal|3
index|]
operator|&
literal|0xFF
operator|)
operator|<<
literal|24
operator|)
return|;
block|}
comment|/**    * Indicates that the loading of Unsafe was successful and the load and store operations will be    * very efficient. May be useful for calling code to fall back on an alternative implementation    * that is slower than Unsafe.get/store but faster than the pure-Java mask-and-shift.    */
DECL|method|usingUnsafe ()
specifier|static
name|boolean
name|usingUnsafe
parameter_list|()
block|{
return|return
operator|(
name|byteArray
operator|instanceof
name|UnsafeByteArray
operator|)
return|;
block|}
comment|/**    * Common interface for retrieving a 64-bit long from a little-endian byte array.    *    *<p>This abstraction allows us to use single-instruction load and put when available, or fall    * back on the slower approach of using Longs.fromBytes(byte...).    */
DECL|interface|LittleEndianBytes
specifier|private
interface|interface
name|LittleEndianBytes
block|{
DECL|method|getLongLittleEndian (byte[] array, int offset)
name|long
name|getLongLittleEndian
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|)
function_decl|;
DECL|method|putLongLittleEndian (byte[] array, int offset, long value)
name|void
name|putLongLittleEndian
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|,
name|long
name|value
parameter_list|)
function_decl|;
block|}
comment|/**    * The only reference to Unsafe is in this nested class. We set things up so that if    * Unsafe.theUnsafe is inaccessible, the attempt to load the nested class fails, and the outer    * class's static initializer can fall back on a non-Unsafe version.    */
DECL|enum|UnsafeByteArray
specifier|private
enum|enum
name|UnsafeByteArray
implements|implements
name|LittleEndianBytes
block|{
comment|// Do *not* change the order of these constants!
DECL|enumConstant|UNSAFE_LITTLE_ENDIAN
name|UNSAFE_LITTLE_ENDIAN
block|{
annotation|@
name|Override
specifier|public
name|long
name|getLongLittleEndian
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|theUnsafe
operator|.
name|getLong
argument_list|(
name|array
argument_list|,
operator|(
name|long
operator|)
name|offset
operator|+
name|BYTE_ARRAY_BASE_OFFSET
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|putLongLittleEndian
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|theUnsafe
operator|.
name|putLong
argument_list|(
name|array
argument_list|,
operator|(
name|long
operator|)
name|offset
operator|+
name|BYTE_ARRAY_BASE_OFFSET
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|,
DECL|enumConstant|UNSAFE_BIG_ENDIAN
name|UNSAFE_BIG_ENDIAN
block|{
annotation|@
name|Override
specifier|public
name|long
name|getLongLittleEndian
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|long
name|bigEndian
init|=
name|theUnsafe
operator|.
name|getLong
argument_list|(
name|array
argument_list|,
operator|(
name|long
operator|)
name|offset
operator|+
name|BYTE_ARRAY_BASE_OFFSET
argument_list|)
decl_stmt|;
comment|// The hardware is big-endian, so we need to reverse the order of the bytes.
return|return
name|Long
operator|.
name|reverseBytes
argument_list|(
name|bigEndian
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|putLongLittleEndian
parameter_list|(
name|byte
index|[]
name|array
parameter_list|,
name|int
name|offset
parameter_list|,
name|long
name|value
parameter_list|)
block|{
comment|// Reverse the order of the bytes before storing, since we're on big-endian hardware.
name|long
name|littleEndianValue
init|=
name|Long
operator|.
name|reverseBytes
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|theUnsafe
operator|.
name|putLong
argument_list|(
name|array
argument_list|,
operator|(
name|long
operator|)
name|offset
operator|+
name|BYTE_ARRAY_BASE_OFFSET
argument_list|,
name|littleEndianValue
argument_list|)
expr_stmt|;
block|}
block|}
block|;
comment|// Provides load and store operations that use native instructions to get better performance.
DECL|field|theUnsafe
specifier|private
specifier|static
specifier|final
name|Unsafe
name|theUnsafe
decl_stmt|;
comment|// The offset to the first element in a byte array.
DECL|field|BYTE_ARRAY_BASE_OFFSET
specifier|private
specifier|static
specifier|final
name|int
name|BYTE_ARRAY_BASE_OFFSET
decl_stmt|;
comment|/**      * Returns a sun.misc.Unsafe. Suitable for use in a 3rd party package. Replace with a simple      * call to Unsafe.getUnsafe when integrating into a jdk.      *      * @return a sun.misc.Unsafe instance if successful      */
DECL|method|getUnsafe ()
specifier|private
specifier|static
name|sun
operator|.
name|misc
operator|.
name|Unsafe
name|getUnsafe
parameter_list|()
block|{
try|try
block|{
return|return
name|sun
operator|.
name|misc
operator|.
name|Unsafe
operator|.
name|getUnsafe
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|tryReflectionInstead
parameter_list|)
block|{
comment|// We'll try reflection instead.
block|}
try|try
block|{
return|return
name|java
operator|.
name|security
operator|.
name|AccessController
operator|.
name|doPrivileged
argument_list|(
operator|new
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
argument_list|<
name|sun
operator|.
name|misc
operator|.
name|Unsafe
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|sun
operator|.
name|misc
operator|.
name|Unsafe
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|sun
operator|.
name|misc
operator|.
name|Unsafe
argument_list|>
name|k
init|=
name|sun
operator|.
name|misc
operator|.
name|Unsafe
operator|.
name|class
decl_stmt|;
for|for
control|(
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
name|f
range|:
name|k
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|f
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Object
name|x
init|=
name|f
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|.
name|isInstance
argument_list|(
name|x
argument_list|)
condition|)
block|{
return|return
name|k
operator|.
name|cast
argument_list|(
name|x
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|NoSuchFieldError
argument_list|(
literal|"the Unsafe"
argument_list|)
throw|;
block|}
block|}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not initialize intrinsics"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
static|static
block|{
name|theUnsafe
operator|=
name|getUnsafe
argument_list|()
expr_stmt|;
name|BYTE_ARRAY_BASE_OFFSET
operator|=
name|theUnsafe
operator|.
name|arrayBaseOffset
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
expr_stmt|;
comment|// sanity check - this should never fail
if|if
condition|(
name|theUnsafe
operator|.
name|arrayIndexScale
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
block|}
comment|/** Fallback implementation for when Unsafe is not available in our current environment. */
DECL|enum|JavaLittleEndianBytes
specifier|private
enum|enum
name|JavaLittleEndianBytes
implements|implements
name|LittleEndianBytes
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|{
annotation|@
name|Override
specifier|public
name|long
name|getLongLittleEndian
parameter_list|(
name|byte
index|[]
name|source
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
return|return
name|Longs
operator|.
name|fromBytes
argument_list|(
name|source
index|[
name|offset
operator|+
literal|7
index|]
argument_list|,
name|source
index|[
name|offset
operator|+
literal|6
index|]
argument_list|,
name|source
index|[
name|offset
operator|+
literal|5
index|]
argument_list|,
name|source
index|[
name|offset
operator|+
literal|4
index|]
argument_list|,
name|source
index|[
name|offset
operator|+
literal|3
index|]
argument_list|,
name|source
index|[
name|offset
operator|+
literal|2
index|]
argument_list|,
name|source
index|[
name|offset
operator|+
literal|1
index|]
argument_list|,
name|source
index|[
name|offset
index|]
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|putLongLittleEndian
parameter_list|(
name|byte
index|[]
name|sink
parameter_list|,
name|int
name|offset
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|long
name|mask
init|=
literal|0xFFL
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|mask
operator|<<=
literal|8
operator|,
name|i
operator|++
control|)
block|{
name|sink
index|[
name|offset
operator|+
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
operator|(
name|value
operator|&
name|mask
operator|)
operator|>>
operator|(
name|i
operator|*
literal|8
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|;   }
static|static
block|{
name|LittleEndianBytes
name|theGetter
init|=
name|JavaLittleEndianBytes
operator|.
name|INSTANCE
decl_stmt|;
try|try
block|{
comment|/*        * UnsafeByteArray uses Unsafe.getLong() in an unsupported way, which is known to cause        * crashes on Android when running in 32-bit mode. For maximum safety, we shouldn't use        * Unsafe.getLong() at all, but the performance benefit on x86_64 is too great to ignore, so        * as a compromise, we enable the optimization only on platforms that we specifically know to        * work.        *        * In the future, the use of Unsafe.getLong() should be replaced by ByteBuffer.getLong(),        * which will have an efficient native implementation in JDK 9.        *        */
specifier|final
name|String
name|arch
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.arch"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"amd64"
operator|.
name|equals
argument_list|(
name|arch
argument_list|)
condition|)
block|{
name|theGetter
operator|=
name|ByteOrder
operator|.
name|nativeOrder
argument_list|()
operator|.
name|equals
argument_list|(
name|ByteOrder
operator|.
name|LITTLE_ENDIAN
argument_list|)
condition|?
name|UnsafeByteArray
operator|.
name|UNSAFE_LITTLE_ENDIAN
else|:
name|UnsafeByteArray
operator|.
name|UNSAFE_BIG_ENDIAN
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ensure we really catch *everything*
block|}
name|byteArray
operator|=
name|theGetter
expr_stmt|;
block|}
comment|/** Deter instantiation of this class. */
DECL|method|LittleEndianByteArray ()
specifier|private
name|LittleEndianByteArray
parameter_list|()
block|{}
block|}
end_class

end_unit

