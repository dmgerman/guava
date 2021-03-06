begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except  * in compliance with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software distributed under the License  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express  * or implied. See the License for the specific language governing permissions and limitations under  * the License.  */
end_comment

begin_package
DECL|package|com.google.common.reflect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|reflect
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
name|collect
operator|.
name|FluentIterable
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
name|ImmutableList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|AnnotatedElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|AnnotatedType
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

begin_import
import|import
name|org
operator|.
name|checkerframework
operator|.
name|checker
operator|.
name|nullness
operator|.
name|qual
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * Represents a method or constructor parameter.  *  * @author Ben Yu  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|ElementTypesAreNonnullByDefault
DECL|class|Parameter
specifier|public
specifier|final
class|class
name|Parameter
implements|implements
name|AnnotatedElement
block|{
DECL|field|declaration
specifier|private
specifier|final
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|declaration
decl_stmt|;
DECL|field|position
specifier|private
specifier|final
name|int
name|position
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|TypeToken
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
DECL|field|annotations
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|Annotation
argument_list|>
name|annotations
decl_stmt|;
DECL|field|annotatedType
specifier|private
specifier|final
name|AnnotatedType
name|annotatedType
decl_stmt|;
DECL|method|Parameter ( Invokable<?, ?> declaration, int position, TypeToken<?> type, Annotation[] annotations, AnnotatedType annotatedType)
name|Parameter
parameter_list|(
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|declaration
parameter_list|,
name|int
name|position
parameter_list|,
name|TypeToken
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|AnnotatedType
name|annotatedType
parameter_list|)
block|{
name|this
operator|.
name|declaration
operator|=
name|declaration
expr_stmt|;
name|this
operator|.
name|position
operator|=
name|position
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|annotations
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|annotations
argument_list|)
expr_stmt|;
name|this
operator|.
name|annotatedType
operator|=
name|annotatedType
expr_stmt|;
block|}
comment|/** Returns the type of the parameter. */
DECL|method|getType ()
specifier|public
name|TypeToken
argument_list|<
name|?
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/** Returns the {@link Invokable} that declares this parameter. */
DECL|method|getDeclaringInvokable ()
specifier|public
name|Invokable
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getDeclaringInvokable
parameter_list|()
block|{
return|return
name|declaration
return|;
block|}
annotation|@
name|Override
DECL|method|isAnnotationPresent (Class<? extends Annotation> annotationType)
specifier|public
name|boolean
name|isAnnotationPresent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
name|annotationType
parameter_list|)
block|{
return|return
name|getAnnotation
argument_list|(
name|annotationType
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|getAnnotation (Class<A> annotationType)
specifier|public
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|A
name|getAnnotation
parameter_list|(
name|Class
argument_list|<
name|A
argument_list|>
name|annotationType
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|annotationType
argument_list|)
expr_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotationType
operator|.
name|isInstance
argument_list|(
name|annotation
argument_list|)
condition|)
block|{
return|return
name|annotationType
operator|.
name|cast
argument_list|(
name|annotation
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getAnnotations ()
specifier|public
name|Annotation
index|[]
name|getAnnotations
parameter_list|()
block|{
return|return
name|getDeclaredAnnotations
argument_list|()
return|;
block|}
comment|/** @since 18.0 */
comment|// @Override on JDK8
annotation|@
name|Override
DECL|method|getAnnotationsByType (Class<A> annotationType)
specifier|public
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|A
index|[]
name|getAnnotationsByType
parameter_list|(
name|Class
argument_list|<
name|A
argument_list|>
name|annotationType
parameter_list|)
block|{
return|return
name|getDeclaredAnnotationsByType
argument_list|(
name|annotationType
argument_list|)
return|;
block|}
comment|/** @since 18.0 */
comment|// @Override on JDK8
annotation|@
name|Override
DECL|method|getDeclaredAnnotations ()
specifier|public
name|Annotation
index|[]
name|getDeclaredAnnotations
parameter_list|()
block|{
return|return
name|annotations
operator|.
name|toArray
argument_list|(
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|)
return|;
block|}
comment|/** @since 18.0 */
comment|// @Override on JDK8
annotation|@
name|Override
annotation|@
name|CheckForNull
DECL|method|getDeclaredAnnotation (Class<A> annotationType)
specifier|public
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|A
name|getDeclaredAnnotation
parameter_list|(
name|Class
argument_list|<
name|A
argument_list|>
name|annotationType
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|annotationType
argument_list|)
expr_stmt|;
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|annotations
argument_list|)
operator|.
name|filter
argument_list|(
name|annotationType
argument_list|)
operator|.
name|first
argument_list|()
operator|.
name|orNull
argument_list|()
return|;
block|}
comment|/** @since 18.0 */
comment|// @Override on JDK8
annotation|@
name|Override
DECL|method|getDeclaredAnnotationsByType (Class<A> annotationType)
specifier|public
parameter_list|<
name|A
extends|extends
name|Annotation
parameter_list|>
name|A
index|[]
name|getDeclaredAnnotationsByType
parameter_list|(
name|Class
argument_list|<
name|A
argument_list|>
name|annotationType
parameter_list|)
block|{
annotation|@
name|Nullable
name|A
index|[]
name|result
init|=
name|FluentIterable
operator|.
name|from
argument_list|(
name|annotations
argument_list|)
operator|.
name|filter
argument_list|(
name|annotationType
argument_list|)
operator|.
name|toArray
argument_list|(
name|annotationType
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"nullness"
argument_list|)
comment|// safe because the input list contains no nulls
name|A
index|[]
name|cast
init|=
operator|(
name|A
index|[]
operator|)
name|result
decl_stmt|;
return|return
name|cast
return|;
block|}
comment|/** @since 25.1 */
comment|// @Override on JDK8
DECL|method|getAnnotatedType ()
specifier|public
name|AnnotatedType
name|getAnnotatedType
parameter_list|()
block|{
return|return
name|annotatedType
return|;
block|}
annotation|@
name|Override
DECL|method|equals (@heckForNull Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
annotation|@
name|CheckForNull
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|Parameter
condition|)
block|{
name|Parameter
name|that
init|=
operator|(
name|Parameter
operator|)
name|obj
decl_stmt|;
return|return
name|position
operator|==
name|that
operator|.
name|position
operator|&&
name|declaration
operator|.
name|equals
argument_list|(
name|that
operator|.
name|declaration
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
name|position
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
name|type
operator|+
literal|" arg"
operator|+
name|position
return|;
block|}
block|}
end_class

end_unit

