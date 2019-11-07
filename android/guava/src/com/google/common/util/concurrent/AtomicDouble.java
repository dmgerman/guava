begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Written by Doug Lea and Martin Buchholz with assistance from  * members of JCP JSR-166 Expert Group and released to the public  * domain, as explained at  * http://creativecommons.org/publicdomain/zero/1.0/  */
end_comment

begin_comment
comment|/*  * Source:  * http://gee.cs.oswego.edu/cgi-bin/viewcvs.cgi/jsr166/src/jsr166e/extra/AtomicDouble.java?revision=1.13  * (Modified to adapt to guava coding conventions and  * to use AtomicLong instead of sun.misc.Unsafe)  */
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
name|AtomicLong
import|;
end_import

begin_comment
comment|/**  * A {@code double} value that may be updated atomically. See the {@link  * java.util.concurrent.atomic} package specification for description of the properties of atomic  * variables. An {@code AtomicDouble} is used in applications such as atomic accumulation, and  * cannot be used as a replacement for a {@link Double}. However, this class does extend {@code  * Number} to allow uniform access by tools and utilities that deal with numerically-based classes.  *  *<p><a id="bitEquals"></a>This class compares primitive {@code double} values in methods such as  * {@link #compareAndSet} by comparing their bitwise representation using {@link  * Double#doubleToRawLongBits}, which differs from both the primitive double {@code ==} operator and  * from {@link Double#equals}, as if implemented by:  *  *<pre>{@code  * static boolean bitEquals(double x, double y) {  *   long xBits = Double.doubleToRawLongBits(x);  *   long yBits = Double.doubleToRawLongBits(y);  *   return xBits == yBits;  * }  * }</pre>  *  *<p>It is possible to write a more scalable updater, at the cost of giving up strict atomicity.  * See for example<a  * href="http://gee.cs.oswego.edu/dl/jsr166/dist/docs/java.base/java/util/concurrent/atomic/DoubleAdder.html">  * DoubleAdder</a>.  *  * @author Doug Lea  * @author Martin Buchholz  * @since 11.0  */
end_comment

begin_class
DECL|class|AtomicDouble
specifier|public
class|class
name|AtomicDouble
extends|extends
name|Number
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
comment|// We would use AtomicLongFieldUpdater, but it has issues on some Android devices.
DECL|field|value
specifier|private
specifier|transient
name|AtomicLong
name|value
decl_stmt|;
comment|/**    * Creates a new {@code AtomicDouble} with the given initial value.    *    * @param initialValue the initial value    */
DECL|method|AtomicDouble (double initialValue)
specifier|public
name|AtomicDouble
parameter_list|(
name|double
name|initialValue
parameter_list|)
block|{
name|value
operator|=
operator|new
name|AtomicLong
argument_list|(
name|doubleToRawLongBits
argument_list|(
name|initialValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a new {@code AtomicDouble} with initial value {@code 0.0}. */
DECL|method|AtomicDouble ()
specifier|public
name|AtomicDouble
parameter_list|()
block|{
name|this
argument_list|(
literal|0.0
argument_list|)
expr_stmt|;
block|}
comment|/**    * Gets the current value.    *    * @return the current value    */
DECL|method|get ()
specifier|public
specifier|final
name|double
name|get
parameter_list|()
block|{
return|return
name|longBitsToDouble
argument_list|(
name|value
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Sets to the given value.    *    * @param newValue the new value    */
DECL|method|set (double newValue)
specifier|public
specifier|final
name|void
name|set
parameter_list|(
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
name|value
operator|.
name|set
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
comment|/**    * Eventually sets to the given value.    *    * @param newValue the new value    */
DECL|method|lazySet (double newValue)
specifier|public
specifier|final
name|void
name|lazySet
parameter_list|(
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
name|value
operator|.
name|lazySet
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
comment|/**    * Atomically sets to the given value and returns the old value.    *    * @param newValue the new value    * @return the previous value    */
DECL|method|getAndSet (double newValue)
specifier|public
specifier|final
name|double
name|getAndSet
parameter_list|(
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
name|value
operator|.
name|getAndSet
argument_list|(
name|next
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Atomically sets the value to the given updated value if the current value is<a    * href="#bitEquals">bitwise equal</a> to the expected value.    *    * @param expect the expected value    * @param update the new value    * @return {@code true} if successful. False return indicates that the actual value was not    *     bitwise equal to the expected value.    */
DECL|method|compareAndSet (double expect, double update)
specifier|public
specifier|final
name|boolean
name|compareAndSet
parameter_list|(
name|double
name|expect
parameter_list|,
name|double
name|update
parameter_list|)
block|{
return|return
name|value
operator|.
name|compareAndSet
argument_list|(
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
comment|/**    * Atomically sets the value to the given updated value if the current value is<a    * href="#bitEquals">bitwise equal</a> to the expected value.    *    *<p>May<a    * href="http://download.oracle.com/javase/7/docs/api/java/util/concurrent/atomic/package-summary.html#Spurious">    * fail spuriously</a> and does not provide ordering guarantees, so is only rarely an appropriate    * alternative to {@code compareAndSet}.    *    * @param expect the expected value    * @param update the new value    * @return {@code true} if successful    */
DECL|method|weakCompareAndSet (double expect, double update)
specifier|public
specifier|final
name|boolean
name|weakCompareAndSet
parameter_list|(
name|double
name|expect
parameter_list|,
name|double
name|update
parameter_list|)
block|{
return|return
name|value
operator|.
name|weakCompareAndSet
argument_list|(
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
comment|/**    * Atomically adds the given value to the current value.    *    * @param delta the value to add    * @return the previous value    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|getAndAdd (double delta)
specifier|public
specifier|final
name|double
name|getAndAdd
parameter_list|(
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
name|value
operator|.
name|get
argument_list|()
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
name|value
operator|.
name|compareAndSet
argument_list|(
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
comment|/**    * Atomically adds the given value to the current value.    *    * @param delta the value to add    * @return the updated value    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|addAndGet (double delta)
specifier|public
specifier|final
name|double
name|addAndGet
parameter_list|(
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
name|value
operator|.
name|get
argument_list|()
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
name|value
operator|.
name|compareAndSet
argument_list|(
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
comment|/**    * Returns the String representation of the current value.    *    * @return the String representation of the current value    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Double
operator|.
name|toString
argument_list|(
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the value of this {@code AtomicDouble} as an {@code int} after a narrowing primitive    * conversion.    */
annotation|@
name|Override
DECL|method|intValue ()
specifier|public
name|int
name|intValue
parameter_list|()
block|{
return|return
operator|(
name|int
operator|)
name|get
argument_list|()
return|;
block|}
comment|/**    * Returns the value of this {@code AtomicDouble} as a {@code long} after a narrowing primitive    * conversion.    */
annotation|@
name|Override
DECL|method|longValue ()
specifier|public
name|long
name|longValue
parameter_list|()
block|{
return|return
operator|(
name|long
operator|)
name|get
argument_list|()
return|;
block|}
comment|/**    * Returns the value of this {@code AtomicDouble} as a {@code float} after a narrowing primitive    * conversion.    */
annotation|@
name|Override
DECL|method|floatValue ()
specifier|public
name|float
name|floatValue
parameter_list|()
block|{
return|return
operator|(
name|float
operator|)
name|get
argument_list|()
return|;
block|}
comment|/** Returns the value of this {@code AtomicDouble} as a {@code double}. */
annotation|@
name|Override
DECL|method|doubleValue ()
specifier|public
name|double
name|doubleValue
parameter_list|()
block|{
return|return
name|get
argument_list|()
return|;
block|}
comment|/**    * Saves the state to a stream (that is, serializes it).    *    * @serialData The current value is emitted (a {@code double}).    */
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
name|s
operator|.
name|writeDouble
argument_list|(
name|get
argument_list|()
argument_list|)
expr_stmt|;
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
name|value
operator|=
operator|new
name|AtomicLong
argument_list|()
expr_stmt|;
name|set
argument_list|(
name|s
operator|.
name|readDouble
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

