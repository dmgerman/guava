begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2011 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|checkState
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
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
comment|/**  * A mutable object that may contain a reference to another object. An instance of this  * type either holds some non-null reference, or holds nothing; it is never said to "hold  * {@code null}". The instance that is held can be set, unset or changed at any time.  *  *<p>This class is the mutable version of {@link Optional}. It is also very similar to  * {@link AtomicReference} (without the atomic operations and thread-safety).  *  *<p>One common use of a {@code Holder} is to receive a result from an anonymous inner  * class:<pre class="code">   {@code  *  *   final Holder<Result> resultHolder = Holder.absent();  *   doIt(new InnerClass() {  *     public void foo() {  *       Result result = ...  *       resultHolder.set(result);  *     }  *   });}</pre>  *  *<b>Note:</b> two {@code Holder} instances are never considered equal, even if they are  * holding the same instance at the time.  *  * @author Kurt Alfred Kluever  * @author Kevin Bourrillion  * @since Guava release 10  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|Holder
specifier|public
specifier|final
class|class
name|Holder
parameter_list|<
name|T
parameter_list|>
implements|implements
name|BaseHolder
argument_list|<
name|T
argument_list|>
block|{
comment|// Static factories
comment|/**    * Returns a new, modifiable holder that initially does not hold any instance.    */
DECL|method|absent ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Holder
argument_list|<
name|T
argument_list|>
name|absent
parameter_list|()
block|{
return|return
name|fromNullable
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**    * Returns a new, modifiable holder that initially holds the instance {@code    * initialReference}.    */
DECL|method|of (T initialReference)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Holder
argument_list|<
name|T
argument_list|>
name|of
parameter_list|(
name|T
name|initialReference
parameter_list|)
block|{
return|return
name|fromNullable
argument_list|(
name|checkNotNull
argument_list|(
name|initialReference
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * If {@code nullableReference} is non-null, returns a new {@code Holder} instance    * containing that reference; otherwise returns {@link Holder#absent}.    */
DECL|method|fromNullable (@ullable T nullableReference)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Holder
argument_list|<
name|T
argument_list|>
name|fromNullable
parameter_list|(
annotation|@
name|Nullable
name|T
name|nullableReference
parameter_list|)
block|{
return|return
operator|new
name|Holder
argument_list|<
name|T
argument_list|>
argument_list|(
name|nullableReference
argument_list|)
return|;
block|}
comment|// Fields
DECL|field|instance
annotation|@
name|Nullable
specifier|private
name|T
name|instance
decl_stmt|;
comment|// Constructors
DECL|method|Holder (@ullable T initialReference)
specifier|private
name|Holder
parameter_list|(
annotation|@
name|Nullable
name|T
name|initialReference
parameter_list|)
block|{
name|this
operator|.
name|instance
operator|=
name|initialReference
expr_stmt|;
block|}
comment|// BaseHolder methods (accessors)
DECL|method|isPresent ()
annotation|@
name|Override
specifier|public
name|boolean
name|isPresent
parameter_list|()
block|{
return|return
name|instance
operator|!=
literal|null
return|;
block|}
DECL|method|get ()
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|()
block|{
name|checkState
argument_list|(
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|instance
return|;
block|}
DECL|method|or (T defaultValue)
annotation|@
name|Override
specifier|public
name|T
name|or
parameter_list|(
name|T
name|defaultValue
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|defaultValue
argument_list|,
literal|"use orNull() instead of or(null)"
argument_list|)
expr_stmt|;
return|return
name|isPresent
argument_list|()
condition|?
name|instance
else|:
name|defaultValue
return|;
block|}
DECL|method|orNull ()
annotation|@
name|Override
annotation|@
name|Nullable
specifier|public
name|T
name|orNull
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
comment|// Mutators
comment|/**    * Sets the contents of this holder to the given non-null reference.    */
DECL|method|set (T instance)
specifier|public
name|void
name|set
parameter_list|(
name|T
name|instance
parameter_list|)
block|{
name|setNullable
argument_list|(
name|checkNotNull
argument_list|(
name|instance
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Removes the reference from the holder if one exists.    */
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|setNullable
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * Sets the contents of this holder to the given instance if it is non-null;    * clears it otherwise.    */
DECL|method|setNullable (@ullable T instance)
specifier|public
name|void
name|setNullable
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
comment|// Object overrides
comment|/**    * Returns a string representation for this holder. The form of this string representation is    * unspecified.    */
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|isPresent
argument_list|()
condition|?
literal|"Holder.of("
operator|+
name|instance
operator|+
literal|")"
else|:
literal|"Holder.absent()"
return|;
block|}
comment|// TODO(kevinb): remove these temporary methods after callers are gone
comment|/**    * Old name of {@link #absent}.    */
DECL|method|unset ()
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Holder
argument_list|<
name|T
argument_list|>
name|unset
parameter_list|()
block|{
return|return
name|absent
argument_list|()
return|;
block|}
comment|/**    * Old name of {@link #isPresent}.    */
DECL|method|isSet ()
specifier|public
name|boolean
name|isSet
parameter_list|()
block|{
return|return
name|isPresent
argument_list|()
return|;
block|}
block|}
end_class

end_unit

