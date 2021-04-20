begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|base
operator|.
name|Supplier
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
comment|/**  * Source of {@link LongAddable} objects that deals with GWT, Unsafe, and all that.  *  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|LongAddables
specifier|final
class|class
name|LongAddables
block|{
DECL|field|SUPPLIER
specifier|private
specifier|static
specifier|final
name|Supplier
argument_list|<
name|LongAddable
argument_list|>
name|SUPPLIER
decl_stmt|;
static|static
block|{
name|Supplier
argument_list|<
name|LongAddable
argument_list|>
name|supplier
decl_stmt|;
try|try
block|{
operator|new
name|LongAdder
argument_list|()
expr_stmt|;
comment|// trigger static initialization of the LongAdder class, which may fail
name|supplier
operator|=
operator|new
name|Supplier
argument_list|<
name|LongAddable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|LongAddable
name|get
parameter_list|()
block|{
return|return
operator|new
name|LongAdder
argument_list|()
return|;
block|}
block|}
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// we really want to catch *everything*
name|supplier
operator|=
operator|new
name|Supplier
argument_list|<
name|LongAddable
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|LongAddable
name|get
parameter_list|()
block|{
return|return
operator|new
name|PureJavaLongAddable
argument_list|()
return|;
block|}
block|}
expr_stmt|;
block|}
name|SUPPLIER
operator|=
name|supplier
expr_stmt|;
block|}
DECL|method|create ()
specifier|public
specifier|static
name|LongAddable
name|create
parameter_list|()
block|{
return|return
name|SUPPLIER
operator|.
name|get
argument_list|()
return|;
block|}
DECL|class|PureJavaLongAddable
specifier|private
specifier|static
specifier|final
class|class
name|PureJavaLongAddable
extends|extends
name|AtomicLong
implements|implements
name|LongAddable
block|{
annotation|@
name|Override
DECL|method|increment ()
specifier|public
name|void
name|increment
parameter_list|()
block|{
name|getAndIncrement
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (long x)
specifier|public
name|void
name|add
parameter_list|(
name|long
name|x
parameter_list|)
block|{
name|getAndAdd
argument_list|(
name|x
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sum ()
specifier|public
name|long
name|sum
parameter_list|()
block|{
return|return
name|get
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

