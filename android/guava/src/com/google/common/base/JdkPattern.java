begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
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
name|GwtIncompatible
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/** A regex pattern implementation which is backed by the {@link Pattern}. */
end_comment

begin_class
annotation|@
name|ElementTypesAreNonnullByDefault
annotation|@
name|GwtIncompatible
DECL|class|JdkPattern
specifier|final
class|class
name|JdkPattern
extends|extends
name|CommonPattern
implements|implements
name|Serializable
block|{
DECL|field|pattern
specifier|private
specifier|final
name|Pattern
name|pattern
decl_stmt|;
DECL|method|JdkPattern (Pattern pattern)
name|JdkPattern
parameter_list|(
name|Pattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matcher (CharSequence t)
specifier|public
name|CommonMatcher
name|matcher
parameter_list|(
name|CharSequence
name|t
parameter_list|)
block|{
return|return
operator|new
name|JdkMatcher
argument_list|(
name|pattern
operator|.
name|matcher
argument_list|(
name|t
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|pattern ()
specifier|public
name|String
name|pattern
parameter_list|()
block|{
return|return
name|pattern
operator|.
name|pattern
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|flags ()
specifier|public
name|int
name|flags
parameter_list|()
block|{
return|return
name|pattern
operator|.
name|flags
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
name|pattern
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|JdkMatcher
specifier|private
specifier|static
specifier|final
class|class
name|JdkMatcher
extends|extends
name|CommonMatcher
block|{
DECL|field|matcher
specifier|final
name|Matcher
name|matcher
decl_stmt|;
DECL|method|JdkMatcher (Matcher matcher)
name|JdkMatcher
parameter_list|(
name|Matcher
name|matcher
parameter_list|)
block|{
name|this
operator|.
name|matcher
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|matcher
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|matches ()
specifier|public
name|boolean
name|matches
parameter_list|()
block|{
return|return
name|matcher
operator|.
name|matches
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|find ()
specifier|public
name|boolean
name|find
parameter_list|()
block|{
return|return
name|matcher
operator|.
name|find
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|find (int index)
specifier|public
name|boolean
name|find
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|matcher
operator|.
name|find
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|replaceAll (String replacement)
specifier|public
name|String
name|replaceAll
parameter_list|(
name|String
name|replacement
parameter_list|)
block|{
return|return
name|matcher
operator|.
name|replaceAll
argument_list|(
name|replacement
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|end ()
specifier|public
name|int
name|end
parameter_list|()
block|{
return|return
name|matcher
operator|.
name|end
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|int
name|start
parameter_list|()
block|{
return|return
name|matcher
operator|.
name|start
argument_list|()
return|;
block|}
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

