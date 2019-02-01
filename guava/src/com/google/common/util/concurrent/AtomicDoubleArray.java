begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Written by Doug Lea with assistance from members of JCP JSR-166  * Expert Group and released to the public domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  */
end_comment

begin_comment
comment|/*  * Source:  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/extra/AtomicDoubleArray.java?revision=1.5  * (Modified to adapt to guava coding conventions and  * to use AtomicLongArray instead of sun.misc.Unsafe)  */
end_comment

begin_package
DECL|package|com.google.common.util.concurrent
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|doubleToRawLongBits
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|longBitsToDouble
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|GwtIncompatible
import|;
end_import

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
name|ImmutableLongArray
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLongArray
import|;
end_import

begin_comment
comment|/**  * A {@code double} array in which elements may be updated atomically. See the {@link  * java.util.concurrent.atomic} package specification for description of the properties of atomic  * variables.  *  *<p><a name="bitEquals"></a>This class compares primitive {@code double} values in methods such as  * {@link #compareAndSet} by comparing their bitwise representation using {@link  * Double#doubleToRawLongBits}, which differs from both the primitive double {@code ==} operator and  * from {@link Double#equals}, as if implemented by:  *  *<pre>{@code  * static boolean bitEquals(double x, double y) {  *   long xBits = Double.doubleToRawLongBits(x);  *   long yBits = Double.doubleToRawLongBits(y);  *   return xBits == yBits;  * }  * }</pre>  *  * @author Doug Lea  * @author Martin Buchholz  * @since 11.0  */
end_comment

begin_class
annotation|@
name|GwtIncompatible
DECL|class|AtomicDoubleArray
specifier|public
class|class
name|AtomicDoubleArray
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0L
decl_stmt|;
comment|// Making this non-final is the lesser evil according to Effective
comment|// Java 2nd Edition Item 76: Write readObject methods defensively.
DECL|field|longs
specifier|private
specifier|transient
name|AtomicLongArray
name|longs
decl_stmt|;
comment|/**    * Creates a new {@code AtomicDoubleArray} of the given length, with all elements initially zero.    *    * @param length the length of the array    */
DECL|method|AtomicDoubleArray (int length)
specifier|public
name|AtomicDoubleArray
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|this
operator|.
name|longs
operator|=
operator|new
name|AtomicLongArray
argument_list|(
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a new {@code AtomicDoubleArray} with the same length as, and all elements copied from,    * the given array.    *    * @param array the array to copy elements from    * @throws NullPointerException if array is null    */
DECL|method|AtomicDoubleArray (double[] array)
specifier|public
name|AtomicDoubleArray
parameter_list|(
name|double
index|[]
name|array
parameter_list|)
block|{
specifier|final
name|int
name|len
init|=
name|array
operator|.
name|length
decl_stmt|;
name|long
index|[]
name|longArray
init|=
operator|new
name|long
index|[
name|len
index|]
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|longArray
index|[
name|i
index|]
operator|=
name|doubleToRawLongBits
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|longs
operator|=
operator|new
name|AtomicLongArray
argument_list|(
name|longArray
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the length of the array.    *    * @return the length of the array    */
DECL|method|length ()
specifier|public
specifier|final
name|int
name|length
parameter_list|()
block|{
return|return
name|longs
operator|.
name|length
argument_list|()
return|;
block|}
comment|/**    * Gets the current value at position {@code i}.    *    * @param i the index    * @return the current value    */
DECL|method|get (int i)
specifier|public
specifier|final
name|double
name|get
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|longBitsToDouble
argument_list|(
name|longs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Sets the element at position {@code i} to the given value.    *    * @param i the index    * @param newValue the new value    */
DECL|method|set (int i, double newValue)
specifier|public
specifier|final
name|void
name|set
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|newValue
parameter_list|)
block|{
name|long
name|next
init|=
name|doubleToRawLongBits
argument_list|(
name|newValue
argument_list|)
decl_stmt|;
name|longs
operator|.
name|set
argument_list|(
name|i
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
comment|/**    * Eventually sets the element at position {@code i} to the given value.    *    * @param i the index    * @param newValue the new value    */
DECL|method|lazySet (int i, double newValue)
specifier|public
specifier|final
name|void
name|lazySet
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|newValue
parameter_list|)
block|{
name|long
name|next
init|=
name|doubleToRawLongBits
argument_list|(
name|newValue
argument_list|)
decl_stmt|;
name|longs
operator|.
name|lazySet
argument_list|(
name|i
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
comment|/**    * Atomically sets the element at position {@code i} to the given value and returns the old value.    *    * @param i the index    * @param newValue the new value    * @return the previous value    */
DECL|method|getAndSet (int i, double newValue)
specifier|public
specifier|final
name|double
name|getAndSet
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|newValue
parameter_list|)
block|{
name|long
name|next
init|=
name|doubleToRawLongBits
argument_list|(
name|newValue
argument_list|)
decl_stmt|;
return|return
name|longBitsToDouble
argument_list|(
name|longs
operator|.
name|getAndSet
argument_list|(
name|i
argument_list|,
name|next
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Atomically sets the element at position {@code i} to the given updated value if the current    * value is<a href="#bitEquals">bitwise equal</a> to the expected value.    *    * @param i the index    * @param expect the expected value    * @param update the new value    * @return true if successful. False return indicates that the actual value was not equal to the    *     expected value.    */
DECL|method|compareAndSet (int i, double expect, double update)
specifier|public
specifier|final
name|boolean
name|compareAndSet
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|expect
parameter_list|,
name|double
name|update
parameter_list|)
block|{
return|return
name|longs
operator|.
name|compareAndSet
argument_list|(
name|i
argument_list|,
name|doubleToRawLongBits
argument_list|(
name|expect
argument_list|)
argument_list|,
name|doubleToRawLongBits
argument_list|(
name|update
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Atomically sets the element at position {@code i} to the given updated value if the current    * value is<a href="#bitEquals">bitwise equal</a> to the expected value.    *    *<p>May<a    * href="http://download.oracle.com/javase/7/docs/api/java/util/concurrent/atomic/package-summary.html#Spurious">    * fail spuriously</a> and does not provide ordering guarantees, so is only rarely an appropriate    * alternative to {@code compareAndSet}.    *    * @param i the index    * @param expect the expected value    * @param update the new value    * @return true if successful    */
DECL|method|weakCompareAndSet (int i, double expect, double update)
specifier|public
specifier|final
name|boolean
name|weakCompareAndSet
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|expect
parameter_list|,
name|double
name|update
parameter_list|)
block|{
return|return
name|longs
operator|.
name|weakCompareAndSet
argument_list|(
name|i
argument_list|,
name|doubleToRawLongBits
argument_list|(
name|expect
argument_list|)
argument_list|,
name|doubleToRawLongBits
argument_list|(
name|update
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Atomically adds the given value to the element at index {@code i}.    *    * @param i the index    * @param delta the value to add    * @return the previous value    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndAdd (int i, double delta)
specifier|public
specifier|final
name|double
name|getAndAdd
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|delta
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|long
name|current
init|=
name|longs
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|double
name|currentVal
init|=
name|longBitsToDouble
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|double
name|nextVal
init|=
name|currentVal
operator|+
name|delta
decl_stmt|;
name|long
name|next
init|=
name|doubleToRawLongBits
argument_list|(
name|nextVal
argument_list|)
decl_stmt|;
if|if
condition|(
name|longs
operator|.
name|compareAndSet
argument_list|(
name|i
argument_list|,
name|current
argument_list|,
name|next
argument_list|)
condition|)
block|{
return|return
name|currentVal
return|;
block|}
block|}
block|}
comment|/**    * Atomically adds the given value to the element at index {@code i}.    *    * @param i the index    * @param delta the value to add    * @return the updated value    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addAndGet (int i, double delta)
specifier|public
name|double
name|addAndGet
parameter_list|(
name|int
name|i
parameter_list|,
name|double
name|delta
parameter_list|)
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|long
name|current
init|=
name|longs
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|double
name|currentVal
init|=
name|longBitsToDouble
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|double
name|nextVal
init|=
name|currentVal
operator|+
name|delta
decl_stmt|;
name|long
name|next
init|=
name|doubleToRawLongBits
argument_list|(
name|nextVal
argument_list|)
decl_stmt|;
if|if
condition|(
name|longs
operator|.
name|compareAndSet
argument_list|(
name|i
argument_list|,
name|current
argument_list|,
name|next
argument_list|)
condition|)
block|{
return|return
name|nextVal
return|;
block|}
block|}
block|}
comment|/**    * Returns the String representation of the current values of array.    *    * @return the String representation of the current values of array    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|int
name|iMax
init|=
name|length
argument_list|()
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|iMax
operator|==
operator|-
literal|1
condition|)
block|{
return|return
literal|"[]"
return|;
block|}
comment|// Double.toString(Math.PI).length() == 17
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
operator|(
literal|17
operator|+
literal|2
operator|)
operator|*
operator|(
name|iMax
operator|+
literal|1
operator|)
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
condition|;
name|i
operator|++
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|longBitsToDouble
argument_list|(
name|longs
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|==
name|iMax
condition|)
block|{
return|return
name|b
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Saves the state to a stream (that is, serializes it).    *    * @serialData The length of the array is emitted (int), followed by all of its elements (each a    *     {@code double}) in the proper order.    */
DECL|method|writeObject (java.io.ObjectOutputStream s)
specifier|private
name|void
name|writeObject
parameter_list|(
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
name|s
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
block|{
name|s
operator|.
name|defaultWriteObject
argument_list|()
expr_stmt|;
comment|// Write out array length
name|int
name|length
init|=
name|length
argument_list|()
decl_stmt|;
name|s
operator|.
name|writeInt
argument_list|(
name|length
argument_list|)
expr_stmt|;
comment|// Write out all elements in the proper order.
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|s
operator|.
name|writeDouble
argument_list|(
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Reconstitutes the instance from a stream (that is, deserializes it). */
DECL|method|readObject (java.io.ObjectInputStream s)
specifier|private
name|void
name|readObject
parameter_list|(
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
name|s
parameter_list|)
throws|throws
name|java
operator|.
name|io
operator|.
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|s
operator|.
name|defaultReadObject
argument_list|()
expr_stmt|;
name|int
name|length
init|=
name|s
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|ImmutableLongArray
operator|.
name|Builder
name|builder
init|=
name|ImmutableLongArray
operator|.
name|builder
argument_list|()
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
name|length
condition|;
name|i
operator|++
control|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|doubleToRawLongBits
argument_list|(
name|s
operator|.
name|readDouble
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|longs
operator|=
operator|new
name|AtomicLongArray
argument_list|(
name|builder
operator|.
name|build
argument_list|()
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

