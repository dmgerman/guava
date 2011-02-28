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
name|primitives
operator|.
name|Primitives
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A class-to-instance map backed by an {@link ImmutableMap}. See also {@link  * MutableClassToInstanceMap}.  *  * @author Kevin Bourrillion  * @since 2 (imported from Google Collections Library)  */
end_comment

begin_class
DECL|class|ImmutableClassToInstanceMap
specifier|public
specifier|final
class|class
name|ImmutableClassToInstanceMap
parameter_list|<
name|B
parameter_list|>
extends|extends
name|ForwardingMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
implements|implements
name|ClassToInstanceMap
argument_list|<
name|B
argument_list|>
block|{
comment|/**    * Returns a new builder. The generated builder is equivalent to the builder    * created by the {@link Builder} constructor.    */
DECL|method|builder ()
specifier|public
specifier|static
parameter_list|<
name|B
parameter_list|>
name|Builder
argument_list|<
name|B
argument_list|>
name|builder
parameter_list|()
block|{
return|return
operator|new
name|Builder
argument_list|<
name|B
argument_list|>
argument_list|()
return|;
block|}
comment|/**    * A builder for creating immutable class-to-instance maps. Example:    *<pre>   {@code    *    *   static final ImmutableClassToInstanceMap<Handler> HANDLERS =    *       new ImmutableClassToInstanceMap.Builder<Handler>()    *           .put(FooHandler.class, new FooHandler())    *           .put(BarHandler.class, new SubBarHandler())    *           .put(Handler.class, new QuuxHandler())    *           .build();}</pre>    *    * After invoking {@link #build()} it is still possible to add more entries    * and build again. Thus each map generated by this builder will be a superset    * of any map generated before it.    *    * @since 2 (imported from Google Collections Library)    */
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
parameter_list|<
name|B
parameter_list|>
block|{
DECL|field|mapBuilder
specifier|private
specifier|final
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|mapBuilder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
comment|/**      * Associates {@code key} with {@code value} in the built map. Duplicate      * keys are not allowed, and will cause {@link #build} to fail.      */
DECL|method|put (Class<T> type, T value)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|Builder
argument_list|<
name|B
argument_list|>
name|put
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|mapBuilder
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Associates all of {@code map's} keys and values in the built map.      * Duplicate keys are not allowed, and will cause {@link #build} to fail.      *      * @throws NullPointerException if any key or value in {@code map} is null      * @throws ClassCastException if any value is not an instance of the type      *     specified by its key      */
DECL|method|putAll ( Map<? extends Class<? extends T>, ? extends T> map)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|Builder
argument_list|<
name|B
argument_list|>
name|putAll
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|map
parameter_list|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|,
name|?
extends|extends
name|T
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|type
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|T
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|mapBuilder
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|cast
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|cast (Class<T> type, B value)
specifier|private
specifier|static
parameter_list|<
name|B
parameter_list|,
name|T
extends|extends
name|B
parameter_list|>
name|T
name|cast
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|B
name|value
parameter_list|)
block|{
return|return
name|Primitives
operator|.
name|wrap
argument_list|(
name|type
argument_list|)
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * Returns a new immutable class-to-instance map containing the entries      * provided to this builder.      *      * @throws IllegalArgumentException if duplicate keys were added      */
DECL|method|build ()
specifier|public
name|ImmutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
name|build
parameter_list|()
block|{
return|return
operator|new
name|ImmutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
argument_list|(
name|mapBuilder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**    * Returns an immutable map containing the same entries as {@code map}. If    * {@code map} somehow contains entries with duplicate keys (for example, if    * it is a {@code SortedMap} whose comparator is not<i>consistent with    * equals</i>), the results of this method are undefined.    *    *<p><b>Note:</b> Despite what the method name suggests, if {@code map} is    * an {@code ImmutableClassToInstanceMap}, no copy will actually be performed.    *    * @throws NullPointerException if any key or value in {@code map} is null    * @throws ClassCastException if any value is not an instance of the type    *     specified by its key    */
DECL|method|copyOf ( Map<? extends Class<? extends S>, ? extends S> map)
specifier|public
specifier|static
parameter_list|<
name|B
parameter_list|,
name|S
extends|extends
name|B
parameter_list|>
name|ImmutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
name|copyOf
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|Class
argument_list|<
name|?
extends|extends
name|S
argument_list|>
argument_list|,
name|?
extends|extends
name|S
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|map
operator|instanceof
name|ImmutableClassToInstanceMap
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// covariant casts safe (unmodifiable)
comment|// Eclipse won't compile if we cast to the parameterized type.
name|ImmutableClassToInstanceMap
argument_list|<
name|B
argument_list|>
name|cast
init|=
operator|(
name|ImmutableClassToInstanceMap
operator|)
name|map
decl_stmt|;
return|return
name|cast
return|;
block|}
return|return
operator|new
name|Builder
argument_list|<
name|B
argument_list|>
argument_list|()
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|field|delegate
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
decl_stmt|;
DECL|method|ImmutableClassToInstanceMap ( ImmutableMap<Class<? extends B>, B> delegate)
specifier|private
name|ImmutableClassToInstanceMap
parameter_list|(
name|ImmutableMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
DECL|method|delegate ()
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|B
argument_list|>
argument_list|,
name|B
argument_list|>
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// value could not get in if not a T
DECL|method|getInstance (Class<T> type)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|(
name|T
operator|)
name|delegate
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**    * Guaranteed to throw an exception and leave the map unmodified.    *    * @throws UnsupportedOperationException always    */
DECL|method|putInstance (Class<T> type, T value)
specifier|public
parameter_list|<
name|T
extends|extends
name|B
parameter_list|>
name|T
name|putInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

