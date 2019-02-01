begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|java.util.concurrent.atomic
package|package
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
package|;
end_package

begin_comment
comment|/**  * GWT emulated version of {@link AtomicLong}. It's a thin wrapper around the primitive {@code  * long}.  *  * @author Jige Yu  */
end_comment

begin_class
DECL|class|AtomicLong
specifier|public
class|class
name|AtomicLong
extends|extends
name|Number
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
DECL|field|value
specifier|private
name|long
name|value
decl_stmt|;
DECL|method|AtomicLong (long initialValue)
specifier|public
name|AtomicLong
parameter_list|(
name|long
name|initialValue
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|initialValue
expr_stmt|;
block|}
DECL|method|AtomicLong ()
specifier|public
name|AtomicLong
parameter_list|()
block|{}
DECL|method|get ()
specifier|public
specifier|final
name|long
name|get
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|set (long newValue)
specifier|public
specifier|final
name|void
name|set
parameter_list|(
name|long
name|newValue
parameter_list|)
block|{
name|value
operator|=
name|newValue
expr_stmt|;
block|}
DECL|method|lazySet (long newValue)
specifier|public
specifier|final
name|void
name|lazySet
parameter_list|(
name|long
name|newValue
parameter_list|)
block|{
name|set
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
block|}
DECL|method|getAndSet (long newValue)
specifier|public
specifier|final
name|long
name|getAndSet
parameter_list|(
name|long
name|newValue
parameter_list|)
block|{
name|long
name|current
init|=
name|value
decl_stmt|;
name|value
operator|=
name|newValue
expr_stmt|;
return|return
name|current
return|;
block|}
DECL|method|compareAndSet (long expect, long update)
specifier|public
specifier|final
name|boolean
name|compareAndSet
parameter_list|(
name|long
name|expect
parameter_list|,
name|long
name|update
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
name|expect
condition|)
block|{
name|value
operator|=
name|update
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|getAndIncrement ()
specifier|public
specifier|final
name|long
name|getAndIncrement
parameter_list|()
block|{
return|return
name|value
operator|++
return|;
block|}
DECL|method|getAndDecrement ()
specifier|public
specifier|final
name|long
name|getAndDecrement
parameter_list|()
block|{
return|return
name|value
operator|--
return|;
block|}
DECL|method|getAndAdd (long delta)
specifier|public
specifier|final
name|long
name|getAndAdd
parameter_list|(
name|long
name|delta
parameter_list|)
block|{
name|long
name|current
init|=
name|value
decl_stmt|;
name|value
operator|+=
name|delta
expr_stmt|;
return|return
name|current
return|;
block|}
DECL|method|incrementAndGet ()
specifier|public
specifier|final
name|long
name|incrementAndGet
parameter_list|()
block|{
return|return
operator|++
name|value
return|;
block|}
DECL|method|decrementAndGet ()
specifier|public
specifier|final
name|long
name|decrementAndGet
parameter_list|()
block|{
return|return
operator|--
name|value
return|;
block|}
DECL|method|addAndGet (long delta)
specifier|public
specifier|final
name|long
name|addAndGet
parameter_list|(
name|long
name|delta
parameter_list|)
block|{
name|value
operator|+=
name|delta
expr_stmt|;
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
name|Long
operator|.
name|toString
argument_list|(
name|value
argument_list|)
return|;
block|}
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
name|value
return|;
block|}
DECL|method|longValue ()
specifier|public
name|long
name|longValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
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
name|value
return|;
block|}
DECL|method|doubleValue ()
specifier|public
name|double
name|doubleValue
parameter_list|()
block|{
return|return
operator|(
name|double
operator|)
name|value
return|;
block|}
block|}
end_class

end_unit

