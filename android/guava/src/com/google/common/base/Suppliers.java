begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.base
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
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
name|annotations
operator|.
name|GwtCompatible
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
name|VisibleForTesting
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Useful suppliers.  *  *<p>All methods return serializable suppliers as long as they're given serializable parameters.  *  * @author Laurence Gonsalves  * @author Harry Heymann  * @since 2.0  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Suppliers
specifier|public
specifier|final
class|class
name|Suppliers
block|{
DECL|method|Suppliers ()
specifier|private
name|Suppliers
parameter_list|()
block|{}
comment|/**    * Returns a new supplier which is the composition of the provided function and supplier. In other    * words, the new supplier's value will be computed by retrieving the value from {@code supplier},    * and then applying {@code function} to that value. Note that the resulting supplier will not    * call {@code supplier} or invoke {@code function} until it is called.    */
DECL|method|compose (Function<? super F, T> function, Supplier<F> supplier)
specifier|public
specifier|static
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|compose
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|T
argument_list|>
name|function
parameter_list|,
name|Supplier
argument_list|<
name|F
argument_list|>
name|supplier
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|supplier
argument_list|)
expr_stmt|;
return|return
operator|new
name|SupplierComposition
argument_list|<
name|F
argument_list|,
name|T
argument_list|>
argument_list|(
name|function
argument_list|,
name|supplier
argument_list|)
return|;
block|}
DECL|class|SupplierComposition
specifier|private
specifier|static
class|class
name|SupplierComposition
parameter_list|<
name|F
parameter_list|,
name|T
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|function
specifier|final
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|T
argument_list|>
name|function
decl_stmt|;
DECL|field|supplier
specifier|final
name|Supplier
argument_list|<
name|F
argument_list|>
name|supplier
decl_stmt|;
DECL|method|SupplierComposition (Function<? super F, T> function, Supplier<F> supplier)
name|SupplierComposition
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|F
argument_list|,
name|T
argument_list|>
name|function
parameter_list|,
name|Supplier
argument_list|<
name|F
argument_list|>
name|supplier
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|function
expr_stmt|;
name|this
operator|.
name|supplier
operator|=
name|supplier
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|supplier
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|SupplierComposition
condition|)
block|{
name|SupplierComposition
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|that
init|=
operator|(
name|SupplierComposition
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|function
operator|.
name|equals
argument_list|(
name|that
operator|.
name|function
argument_list|)
operator|&&
name|supplier
operator|.
name|equals
argument_list|(
name|that
operator|.
name|supplier
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|function
argument_list|,
name|supplier
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Suppliers.compose("
operator|+
name|function
operator|+
literal|", "
operator|+
name|supplier
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Returns a supplier which caches the instance retrieved during the first call to {@code get()}    * and returns that value on subsequent calls to {@code get()}. See:<a    * href="http://en.wikipedia.org/wiki/Memoization">memoization</a>    *    *<p>The returned supplier is thread-safe. The delegate's {@code get()} method will be invoked at    * most once. The supplier's serialized form does not contain the cached value, which will be    * recalculated when {@code get()} is called on the reserialized instance.    *    *<p>If {@code delegate} is an instance created by an earlier call to {@code memoize}, it is    * returned directly.    */
DECL|method|memoize (Supplier<T> delegate)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|memoize
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
if|if
condition|(
name|delegate
operator|instanceof
name|NonSerializableMemoizingSupplier
operator|||
name|delegate
operator|instanceof
name|MemoizingSupplier
condition|)
block|{
return|return
name|delegate
return|;
block|}
return|return
name|delegate
operator|instanceof
name|Serializable
condition|?
operator|new
name|MemoizingSupplier
argument_list|<
name|T
argument_list|>
argument_list|(
name|delegate
argument_list|)
else|:
operator|new
name|NonSerializableMemoizingSupplier
argument_list|<
name|T
argument_list|>
argument_list|(
name|delegate
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|class|MemoizingSupplier
specifier|static
class|class
name|MemoizingSupplier
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|initialized
specifier|transient
specifier|volatile
name|boolean
name|initialized
decl_stmt|;
comment|// "value" does not need to be volatile; visibility piggy-backs
comment|// on volatile read of "initialized".
DECL|field|value
specifier|transient
name|T
name|value
decl_stmt|;
DECL|method|MemoizingSupplier (Supplier<T> delegate)
name|MemoizingSupplier
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
comment|// A 2-field variant of Double Checked Locking.
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
name|T
name|t
init|=
name|delegate
operator|.
name|get
argument_list|()
decl_stmt|;
name|value
operator|=
name|t
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
return|return
name|t
return|;
block|}
block|}
block|}
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Suppliers.memoize("
operator|+
name|delegate
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|class|NonSerializableMemoizingSupplier
specifier|static
class|class
name|NonSerializableMemoizingSupplier
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
block|{
DECL|field|delegate
specifier|volatile
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|initialized
specifier|volatile
name|boolean
name|initialized
decl_stmt|;
comment|// "value" does not need to be volatile; visibility piggy-backs
comment|// on volatile read of "initialized".
DECL|field|value
name|T
name|value
decl_stmt|;
DECL|method|NonSerializableMemoizingSupplier (Supplier<T> delegate)
name|NonSerializableMemoizingSupplier
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
comment|// A 2-field variant of Double Checked Locking.
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
name|T
name|t
init|=
name|delegate
operator|.
name|get
argument_list|()
decl_stmt|;
name|value
operator|=
name|t
expr_stmt|;
name|initialized
operator|=
literal|true
expr_stmt|;
comment|// Release the delegate to GC.
name|delegate
operator|=
literal|null
expr_stmt|;
return|return
name|t
return|;
block|}
block|}
block|}
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Suppliers.memoize("
operator|+
name|delegate
operator|+
literal|")"
return|;
block|}
block|}
comment|/**    * Returns a supplier that caches the instance supplied by the delegate and removes the cached    * value after the specified time has passed. Subsequent calls to {@code get()} return the cached    * value if the expiration time has not passed. After the expiration time, a new value is    * retrieved, cached, and returned. See:    *<a href="http://en.wikipedia.org/wiki/Memoization">memoization</a>    *    *<p>The returned supplier is thread-safe. The supplier's serialized form does not contain the    * cached value, which will be recalculated when {@code    * get()} is called on the reserialized instance.    *    * @param duration the length of time after a value is created that it should stop being returned    *     by subsequent {@code get()} calls    * @param unit the unit that {@code duration} is expressed in    * @throws IllegalArgumentException if {@code duration} is not positive    * @since 2.0    */
DECL|method|memoizeWithExpiration ( Supplier<T> delegate, long duration, TimeUnit unit)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|memoizeWithExpiration
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
operator|new
name|ExpiringMemoizingSupplier
argument_list|<
name|T
argument_list|>
argument_list|(
name|delegate
argument_list|,
name|duration
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|class|ExpiringMemoizingSupplier
specifier|static
class|class
name|ExpiringMemoizingSupplier
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|field|durationNanos
specifier|final
name|long
name|durationNanos
decl_stmt|;
DECL|field|value
specifier|transient
specifier|volatile
name|T
name|value
decl_stmt|;
comment|// The special value 0 means "not yet initialized".
DECL|field|expirationNanos
specifier|transient
specifier|volatile
name|long
name|expirationNanos
decl_stmt|;
DECL|method|ExpiringMemoizingSupplier (Supplier<T> delegate, long duration, TimeUnit unit)
name|ExpiringMemoizingSupplier
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|this
operator|.
name|durationNanos
operator|=
name|unit
operator|.
name|toNanos
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkArgument
argument_list|(
name|duration
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
comment|// Another variant of Double Checked Locking.
comment|//
comment|// We use two volatile reads. We could reduce this to one by
comment|// putting our fields into a holder class, but (at least on x86)
comment|// the extra memory consumption and indirection are more
comment|// expensive than the extra volatile reads.
name|long
name|nanos
init|=
name|expirationNanos
decl_stmt|;
name|long
name|now
init|=
name|Platform
operator|.
name|systemNanoTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|nanos
operator|==
literal|0
operator|||
name|now
operator|-
name|nanos
operator|>=
literal|0
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|nanos
operator|==
name|expirationNanos
condition|)
block|{
comment|// recheck for lost race
name|T
name|t
init|=
name|delegate
operator|.
name|get
argument_list|()
decl_stmt|;
name|value
operator|=
name|t
expr_stmt|;
name|nanos
operator|=
name|now
operator|+
name|durationNanos
expr_stmt|;
comment|// In the very unlikely event that nanos is 0, set it to 1;
comment|// no one will notice 1 ns of tardiness.
name|expirationNanos
operator|=
operator|(
name|nanos
operator|==
literal|0
operator|)
condition|?
literal|1
else|:
name|nanos
expr_stmt|;
return|return
name|t
return|;
block|}
block|}
block|}
return|return
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// This is a little strange if the unit the user provided was not NANOS,
comment|// but we don't want to store the unit just for toString
return|return
literal|"Suppliers.memoizeWithExpiration("
operator|+
name|delegate
operator|+
literal|", "
operator|+
name|durationNanos
operator|+
literal|", NANOS)"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Returns a supplier that always supplies {@code instance}.    */
DECL|method|ofInstance (@ullable T instance)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|ofInstance
parameter_list|(
annotation|@
name|Nullable
name|T
name|instance
parameter_list|)
block|{
return|return
operator|new
name|SupplierOfInstance
argument_list|<
name|T
argument_list|>
argument_list|(
name|instance
argument_list|)
return|;
block|}
DECL|class|SupplierOfInstance
specifier|private
specifier|static
class|class
name|SupplierOfInstance
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|instance
specifier|final
name|T
name|instance
decl_stmt|;
DECL|method|SupplierOfInstance (@ullable T instance)
name|SupplierOfInstance
parameter_list|(
annotation|@
name|Nullable
name|T
name|instance
parameter_list|)
block|{
name|this
operator|.
name|instance
operator|=
name|instance
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|SupplierOfInstance
condition|)
block|{
name|SupplierOfInstance
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|SupplierOfInstance
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|instance
argument_list|,
name|that
operator|.
name|instance
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hashCode
argument_list|(
name|instance
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Suppliers.ofInstance("
operator|+
name|instance
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Returns a supplier whose {@code get()} method synchronizes on {@code delegate} before calling    * it, making it thread-safe.    */
DECL|method|synchronizedSupplier (Supplier<T> delegate)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Supplier
argument_list|<
name|T
argument_list|>
name|synchronizedSupplier
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
return|return
operator|new
name|ThreadSafeSupplier
argument_list|<
name|T
argument_list|>
argument_list|(
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|delegate
argument_list|)
argument_list|)
return|;
block|}
DECL|class|ThreadSafeSupplier
specifier|private
specifier|static
class|class
name|ThreadSafeSupplier
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Supplier
argument_list|<
name|T
argument_list|>
implements|,
name|Serializable
block|{
DECL|field|delegate
specifier|final
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ThreadSafeSupplier (Supplier<T> delegate)
name|ThreadSafeSupplier
parameter_list|(
name|Supplier
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
synchronized|synchronized
init|(
name|delegate
init|)
block|{
return|return
name|delegate
operator|.
name|get
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Suppliers.synchronizedSupplier("
operator|+
name|delegate
operator|+
literal|")"
return|;
block|}
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|0
decl_stmt|;
block|}
comment|/**    * Returns a function that accepts a supplier and returns the result of invoking {@link    * Supplier#get} on that supplier.    *    *<p><b>Java 8 users:</b> use the method reference {@code Supplier::get} instead.    *    * @since 8.0    */
DECL|method|supplierFunction ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Function
argument_list|<
name|Supplier
argument_list|<
name|T
argument_list|>
argument_list|,
name|T
argument_list|>
name|supplierFunction
parameter_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// implementation is "fully variant"
name|SupplierFunction
argument_list|<
name|T
argument_list|>
name|sf
init|=
operator|(
name|SupplierFunction
argument_list|<
name|T
argument_list|>
operator|)
name|SupplierFunctionImpl
operator|.
name|INSTANCE
decl_stmt|;
return|return
name|sf
return|;
block|}
DECL|interface|SupplierFunction
specifier|private
interface|interface
name|SupplierFunction
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Function
argument_list|<
name|Supplier
argument_list|<
name|T
argument_list|>
argument_list|,
name|T
argument_list|>
block|{}
DECL|enum|SupplierFunctionImpl
specifier|private
enum|enum
name|SupplierFunctionImpl
implements|implements
name|SupplierFunction
argument_list|<
name|Object
argument_list|>
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
comment|// Note: This makes T a "pass-through type"
annotation|@
name|Override
DECL|method|apply (Supplier<Object> input)
specifier|public
name|Object
name|apply
parameter_list|(
name|Supplier
argument_list|<
name|Object
argument_list|>
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Suppliers.supplierFunction()"
return|;
block|}
block|}
block|}
end_class

end_unit
