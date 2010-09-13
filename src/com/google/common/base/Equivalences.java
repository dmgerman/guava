begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2010 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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

begin_comment
comment|/**  * Contains static factory methods for creating {@code Equivalence} instances.  *  *<p>All methods returns serializable instances.  *  * @author Bob Lee  * @since 4  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|Equivalences
specifier|public
specifier|final
class|class
name|Equivalences
block|{
DECL|method|Equivalences ()
specifier|private
name|Equivalences
parameter_list|()
block|{}
comment|/**    * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.    * Does not support null values.    */
DECL|method|equals ()
specifier|public
specifier|static
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|equals
parameter_list|()
block|{
return|return
name|Impl
operator|.
name|EQUALS
return|;
block|}
comment|/**    * Returns an equivalence that delegates to {@link Object#equals} and {@link Object#hashCode}.    * {@link Equivalence#equivalent} returns {@code true} if both values are null, or if neither    * value is null and {@link Object#equals} returns {@code true}. {@link Equivalence#hash} throws a    * {@link NullPointerException} if passed a null value.    */
DECL|method|nullAwareEquals ()
specifier|public
specifier|static
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|nullAwareEquals
parameter_list|()
block|{
return|return
name|Impl
operator|.
name|NULL_AWARE_EQUALS
return|;
block|}
comment|/**    * Returns an equivalence that uses {@code ==} to compare values and {@link    * System#identityHashCode(Object)} to compute the hash code.  {@link Equivalence#equivalent}    * returns {@code true} if both values are null, or if neither value is null and {@code ==}    * returns {@code true}. {@link Equivalence#hash} throws a {@link NullPointerException} if passed    * a null value.    */
DECL|method|identity ()
specifier|public
specifier|static
name|Equivalence
argument_list|<
name|Object
argument_list|>
name|identity
parameter_list|()
block|{
return|return
name|Impl
operator|.
name|IDENTITY
return|;
block|}
DECL|enum|Impl
specifier|private
enum|enum
name|Impl
implements|implements
name|Equivalence
argument_list|<
name|Object
argument_list|>
block|{
DECL|enumConstant|EQUALS
name|EQUALS
block|{
specifier|public
name|boolean
name|equivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|equals
argument_list|(
name|b
argument_list|)
return|;
block|}
specifier|public
name|int
name|hash
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
block|,
DECL|enumConstant|IDENTITY
name|IDENTITY
block|{
specifier|public
name|boolean
name|equivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|a
operator|==
name|b
return|;
block|}
specifier|public
name|int
name|hash
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|System
operator|.
name|identityHashCode
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
block|,
DECL|enumConstant|NULL_AWARE_EQUALS
name|NULL_AWARE_EQUALS
block|{
specifier|public
name|boolean
name|equivalent
parameter_list|(
name|Object
name|a
parameter_list|,
name|Object
name|b
parameter_list|)
block|{
return|return
name|Objects
operator|.
name|equal
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
return|;
block|}
specifier|public
name|int
name|hash
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|.
name|hashCode
argument_list|()
return|;
comment|// TODO(kevinb): why NPE? counter-intuitive.
block|}
block|}
block|,   }
block|}
end_class

end_unit

