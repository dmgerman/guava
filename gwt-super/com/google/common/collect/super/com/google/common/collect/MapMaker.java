begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|base
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|Timer
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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

begin_comment
comment|/**  * MapMaker emulation. Since Javascript is single-threaded and have no  * references, this reduces to the creation of expiring and computing maps.  *  * @author Charles Fry  */
end_comment

begin_class
DECL|class|MapMaker
specifier|public
class|class
name|MapMaker
block|{
DECL|class|ExpiringComputingMap
specifier|private
specifier|static
class|class
name|ExpiringComputingMap
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|expirationMillis
specifier|private
name|long
name|expirationMillis
decl_stmt|;
DECL|field|computer
specifier|private
specifier|final
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computer
decl_stmt|;
DECL|method|ExpiringComputingMap (long expirationMillis)
specifier|public
name|ExpiringComputingMap
parameter_list|(
name|long
name|expirationMillis
parameter_list|)
block|{
name|this
argument_list|(
name|expirationMillis
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ExpiringComputingMap (long expirationMillis, Function<? super K, ? extends V> computer)
specifier|public
name|ExpiringComputingMap
parameter_list|(
name|long
name|expirationMillis
parameter_list|,
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computer
parameter_list|)
block|{
name|this
operator|.
name|expirationMillis
operator|=
name|expirationMillis
expr_stmt|;
name|this
operator|.
name|computer
operator|=
name|computer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|put (K key, V value)
specifier|public
name|V
name|put
parameter_list|(
name|K
name|key
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|V
name|result
init|=
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|expirationMillis
operator|>
literal|0
condition|)
block|{
name|scheduleRemoval
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|scheduleRemoval (final K key, final V value)
specifier|private
name|void
name|scheduleRemoval
parameter_list|(
specifier|final
name|K
name|key
parameter_list|,
specifier|final
name|V
name|value
parameter_list|)
block|{
comment|// from MapMaker
comment|/*        * TODO: Keep weak reference to map, too. Build a priority        * queue out of the entries themselves instead of creating a        * task per entry. Then, we could have one recurring task per        * map (which would clean the entire map and then reschedule        * itself depending upon when the next expiration comes). We        * also want to avoid removing an entry prematurely if the        * entry was set to the same value again.        */
name|Timer
name|timer
init|=
operator|new
name|Timer
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|remove
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|timer
operator|.
name|schedule
argument_list|(
operator|(
name|int
operator|)
name|expirationMillis
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get (Object k)
specifier|public
name|V
name|get
parameter_list|(
name|Object
name|k
parameter_list|)
block|{
comment|// from CustomConcurrentHashMap
name|V
name|result
init|=
name|super
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
operator|&&
name|computer
operator|!=
literal|null
condition|)
block|{
comment|/*          * This cast isn't safe, but we can rely on the fact that K is almost          * always passed to Map.get(), and tools like IDEs and Findbugs can          * catch situations where this isn't the case.          *          * The alternative is to add an overloaded method, but the chances of          * a user calling get() instead of the new API and the risks inherent          * in adding a new API outweigh this little hole.          */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|K
name|key
init|=
operator|(
name|K
operator|)
name|k
decl_stmt|;
name|result
operator|=
name|compute
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|compute (K key)
specifier|private
name|V
name|compute
parameter_list|(
name|K
name|key
parameter_list|)
block|{
comment|// from MapMaker
name|V
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|computer
operator|.
name|apply
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
throw|throw
operator|new
name|ComputationException
argument_list|(
name|t
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
name|computer
operator|+
literal|" returned null for key "
operator|+
name|key
operator|+
literal|"."
decl_stmt|;
throw|throw
operator|new
name|NullPointerException
argument_list|(
name|message
argument_list|)
throw|;
block|}
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
block|}
DECL|field|initialCapacity
specifier|private
name|int
name|initialCapacity
init|=
literal|16
decl_stmt|;
DECL|field|loadFactor
specifier|private
name|float
name|loadFactor
init|=
literal|0.75f
decl_stmt|;
DECL|field|expirationMillis
specifier|private
name|long
name|expirationMillis
init|=
literal|0
decl_stmt|;
DECL|field|useCustomMap
specifier|private
name|boolean
name|useCustomMap
decl_stmt|;
DECL|method|MapMaker ()
specifier|public
name|MapMaker
parameter_list|()
block|{   }
DECL|method|initialCapacity (int initialCapacity)
specifier|public
name|MapMaker
name|initialCapacity
parameter_list|(
name|int
name|initialCapacity
parameter_list|)
block|{
if|if
condition|(
name|initialCapacity
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|this
operator|.
name|initialCapacity
operator|=
name|initialCapacity
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|loadFactor (float loadFactor)
specifier|public
name|MapMaker
name|loadFactor
parameter_list|(
name|float
name|loadFactor
parameter_list|)
block|{
if|if
condition|(
name|loadFactor
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|this
operator|.
name|loadFactor
operator|=
name|loadFactor
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|expiration (long duration, TimeUnit unit)
specifier|public
name|MapMaker
name|expiration
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
name|expirationMillis
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"expiration time of "
operator|+
name|expirationMillis
operator|+
literal|" ns was already set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|duration
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"invalid duration: "
operator|+
name|duration
argument_list|)
throw|;
block|}
name|this
operator|.
name|expirationMillis
operator|=
name|unit
operator|.
name|toMillis
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|useCustomMap
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|makeMap ()
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeMap
parameter_list|()
block|{
return|return
name|useCustomMap
condition|?
operator|new
name|ExpiringComputingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|expirationMillis
argument_list|)
else|:
operator|new
name|ConcurrentHashMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|initialCapacity
argument_list|,
name|loadFactor
argument_list|)
return|;
block|}
DECL|method|makeComputingMap ( Function<? super K, ? extends V> computer)
specifier|public
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|ConcurrentMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|makeComputingMap
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|K
argument_list|,
name|?
extends|extends
name|V
argument_list|>
name|computer
parameter_list|)
block|{
return|return
operator|new
name|ExpiringComputingMap
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|(
name|expirationMillis
argument_list|,
name|computer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

