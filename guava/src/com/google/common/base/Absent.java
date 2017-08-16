begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
comment|/**  * Implementation of an {@link Optional} not containing a reference.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|Absent
specifier|final
class|class
name|Absent
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Optional
argument_list|<
name|T
argument_list|>
block|{
DECL|field|INSTANCE
specifier|static
specifier|final
name|Absent
argument_list|<
name|Object
argument_list|>
name|INSTANCE
init|=
operator|new
name|Absent
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// implementation is "fully variant"
DECL|method|withType ()
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|withType
parameter_list|()
block|{
return|return
operator|(
name|Optional
argument_list|<
name|T
argument_list|>
operator|)
name|INSTANCE
return|;
block|}
DECL|method|Absent ()
specifier|private
name|Absent
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|isPresent ()
specifier|public
name|boolean
name|isPresent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Optional.get() cannot be called on an absent value"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|or (T defaultValue)
specifier|public
name|T
name|or
parameter_list|(
name|T
name|defaultValue
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|defaultValue
argument_list|,
literal|"use Optional.orNull() instead of Optional.or(null)"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// safe covariant cast
annotation|@
name|Override
DECL|method|or (Optional<? extends T> secondChoice)
specifier|public
name|Optional
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Optional
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|secondChoice
parameter_list|)
block|{
return|return
operator|(
name|Optional
argument_list|<
name|T
argument_list|>
operator|)
name|checkNotNull
argument_list|(
name|secondChoice
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|or (Supplier<? extends T> supplier)
specifier|public
name|T
name|or
parameter_list|(
name|Supplier
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|supplier
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|supplier
operator|.
name|get
argument_list|()
argument_list|,
literal|"use Optional.orNull() instead of a Supplier that returns null"
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|orNull ()
specifier|public
name|T
name|orNull
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|asSet ()
specifier|public
name|Set
argument_list|<
name|T
argument_list|>
name|asSet
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|transform (Function<? super T, V> function)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|Optional
argument_list|<
name|V
argument_list|>
name|transform
parameter_list|(
name|Function
argument_list|<
name|?
super|super
name|T
argument_list|,
name|V
argument_list|>
name|function
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|function
argument_list|)
expr_stmt|;
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@ullable Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|Nullable
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|==
name|this
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
literal|0x79a31aac
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
literal|"Optional.absent()"
return|;
block|}
DECL|method|readResolve ()
specifier|private
name|Object
name|readResolve
parameter_list|()
block|{
return|return
name|INSTANCE
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
end_class

end_unit

