begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtIncompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InvalidObjectException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
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
name|javax
operator|.
name|annotation
operator|.
name|CheckForNull
import|;
end_import

begin_comment
comment|/**  * List returned by {@link ImmutableCollection#asList} that delegates {@code contains} checks to the  * backing collection.  *  * @author Jared Levy  * @author Louis Wasserman  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|serializable
operator|=
literal|true
argument_list|,
name|emulated
operator|=
literal|true
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|ImmutableAsList
specifier|abstract
class|class
name|ImmutableAsList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableList
argument_list|<
name|E
argument_list|>
block|{
DECL|method|delegateCollection ()
specifier|abstract
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
name|delegateCollection
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|contains (@heckForNull Object target)
specifier|public
name|boolean
name|contains
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|target
parameter_list|)
block|{
comment|// The collection's contains() is at least as fast as ImmutableList's
comment|// and is often faster.
return|return
name|delegateCollection
argument_list|()
operator|.
name|contains
argument_list|(
name|target
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|delegateCollection
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|delegateCollection
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isPartialView ()
name|boolean
name|isPartialView
parameter_list|()
block|{
return|return
name|delegateCollection
argument_list|()
operator|.
name|isPartialView
argument_list|()
return|;
block|}
comment|/** Serialized form that leads to the same performance as the original list. */
annotation|@
name|GwtIncompatible
comment|// serialization
DECL|class|SerializedForm
specifier|static
class|class
name|SerializedForm
implements|implements
name|Serializable
block|{
DECL|field|collection
specifier|final
name|ImmutableCollection
argument_list|<
name|?
argument_list|>
name|collection
decl_stmt|;
DECL|method|SerializedForm (ImmutableCollection<?> collection)
name|SerializedForm
parameter_list|(
name|ImmutableCollection
argument_list|<
name|?
argument_list|>
name|collection
parameter_list|)
block|{
name|this
operator|.
name|collection
operator|=
name|collection
expr_stmt|;
block|}
DECL|method|readResolve ()
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|collection
operator|.
name|asList
argument_list|()
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
name|GwtIncompatible
comment|// serialization
DECL|method|readObject (ObjectInputStream stream)
specifier|private
name|void
name|readObject
parameter_list|(
name|ObjectInputStream
name|stream
parameter_list|)
throws|throws
name|InvalidObjectException
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Use SerializedForm"
argument_list|)
throw|;
block|}
annotation|@
name|GwtIncompatible
comment|// serialization
annotation|@
name|Override
DECL|method|writeReplace ()
name|Object
name|writeReplace
parameter_list|()
block|{
return|return
operator|new
name|SerializedForm
argument_list|(
name|delegateCollection
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

