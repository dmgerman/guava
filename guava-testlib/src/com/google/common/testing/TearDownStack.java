begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Beta
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
name|collect
operator|.
name|Lists
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
name|concurrent
operator|.
name|GuardedBy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_comment
comment|/**  * A {@code TearDownStack} contains a stack of {@link TearDown} instances.  *  *<p>This class is thread-safe.  *  * @author Kevin Bourrillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|TearDownStack
specifier|public
class|class
name|TearDownStack
implements|implements
name|TearDownAccepter
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|TearDownStack
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|GuardedBy
argument_list|(
literal|"stack"
argument_list|)
DECL|field|stack
specifier|final
name|LinkedList
argument_list|<
name|TearDown
argument_list|>
name|stack
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|suppressThrows
specifier|private
specifier|final
name|boolean
name|suppressThrows
decl_stmt|;
DECL|method|TearDownStack ()
specifier|public
name|TearDownStack
parameter_list|()
block|{
name|this
operator|.
name|suppressThrows
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|TearDownStack (boolean suppressThrows)
specifier|public
name|TearDownStack
parameter_list|(
name|boolean
name|suppressThrows
parameter_list|)
block|{
name|this
operator|.
name|suppressThrows
operator|=
name|suppressThrows
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addTearDown (TearDown tearDown)
specifier|public
specifier|final
name|void
name|addTearDown
parameter_list|(
name|TearDown
name|tearDown
parameter_list|)
block|{
synchronized|synchronized
init|(
name|stack
init|)
block|{
name|stack
operator|.
name|addFirst
argument_list|(
name|checkNotNull
argument_list|(
name|tearDown
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Causes teardown to execute. */
DECL|method|runTearDown ()
specifier|public
specifier|final
name|void
name|runTearDown
parameter_list|()
block|{
name|List
argument_list|<
name|Throwable
argument_list|>
name|exceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TearDown
argument_list|>
name|stackCopy
decl_stmt|;
synchronized|synchronized
init|(
name|stack
init|)
block|{
name|stackCopy
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|stack
argument_list|)
expr_stmt|;
name|stack
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|TearDown
name|tearDown
range|:
name|stackCopy
control|)
block|{
try|try
block|{
name|tearDown
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|suppressThrows
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"exception thrown during tearDown"
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exceptions
operator|.
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|(
operator|!
name|suppressThrows
operator|)
operator|&&
operator|(
name|exceptions
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|)
block|{
throw|throw
name|ClusterException
operator|.
name|create
argument_list|(
name|exceptions
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

