begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Array
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Methods factored out so that they can be emulated differently in GWT.  *  * @author Hayward Chan  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|Platform
specifier|final
class|class
name|Platform
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
name|logger
init|=
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
operator|.
name|getLogger
argument_list|(
name|Platform
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Returns the platform preferred implementation of a map based on a hash table. */
DECL|method|newHashMapWithExpectedSize (int expectedSize)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newHashMapWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Returns the platform preferred implementation of an insertion ordered map based on a hash    * table.    */
DECL|method|newLinkedHashMapWithExpectedSize (int expectedSize)
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|newLinkedHashMapWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/** Returns the platform preferred implementation of a set based on a hash table. */
DECL|method|newHashSetWithExpectedSize (int expectedSize)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|newHashSetWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Returns the platform preferred implementation of an insertion ordered set based on a hash    * table.    */
DECL|method|newLinkedHashSetWithExpectedSize (int expectedSize)
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|newLinkedHashSetWithExpectedSize
parameter_list|(
name|int
name|expectedSize
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|newLinkedHashSetWithExpectedSize
argument_list|(
name|expectedSize
argument_list|)
return|;
block|}
comment|/**    * Returns the platform preferred map implementation that preserves insertion order when used only    * for insertions.    */
DECL|method|preservesInsertionOrderOnPutsMap ()
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|preservesInsertionOrderOnPutsMap
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
return|;
block|}
comment|/**    * Returns the platform preferred set implementation that preserves insertion order when used only    * for insertions.    */
DECL|method|preservesInsertionOrderOnAddsSet ()
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Set
argument_list|<
name|E
argument_list|>
name|preservesInsertionOrderOnAddsSet
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|newLinkedHashSet
argument_list|()
return|;
block|}
comment|/**    * Returns a new array of the given length with the same type as a reference array.    *    * @param reference any array of the desired type    * @param length the length of the new array    */
DECL|method|newArray (T[] reference, int length)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|T
index|[]
name|reference
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|reference
operator|.
name|getClass
argument_list|()
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
comment|// the cast is safe because
comment|// result.getClass() == reference.getClass().getComponentType()
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
index|[]
name|result
init|=
operator|(
name|T
index|[]
operator|)
name|Array
operator|.
name|newInstance
argument_list|(
name|type
argument_list|,
name|length
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
comment|/** Equivalent to Arrays.copyOfRange(source, from, to, arrayOfType.getClass()). */
DECL|method|copy (Object[] source, int from, int to, T[] arrayOfType)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|copy
parameter_list|(
name|Object
index|[]
name|source
parameter_list|,
name|int
name|from
parameter_list|,
name|int
name|to
parameter_list|,
name|T
index|[]
name|arrayOfType
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|source
argument_list|,
name|from
argument_list|,
name|to
argument_list|,
operator|(
name|Class
operator|<
condition|?
then|extends
name|T
index|[]
operator|>
operator|)
name|arrayOfType
operator|.
name|getClass
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Configures the given map maker to use weak keys, if possible; does nothing otherwise (i.e., in    * GWT). This is sometimes acceptable, when only server-side code could generate enough volume    * that reclamation becomes important.    */
DECL|method|tryWeakKeys (MapMaker mapMaker)
specifier|static
name|MapMaker
name|tryWeakKeys
parameter_list|(
name|MapMaker
name|mapMaker
parameter_list|)
block|{
return|return
name|mapMaker
operator|.
name|weakKeys
argument_list|()
return|;
block|}
DECL|method|reduceIterationsIfGwt (int iterations)
specifier|static
name|int
name|reduceIterationsIfGwt
parameter_list|(
name|int
name|iterations
parameter_list|)
block|{
return|return
name|iterations
return|;
block|}
DECL|method|reduceExponentIfGwt (int exponent)
specifier|static
name|int
name|reduceExponentIfGwt
parameter_list|(
name|int
name|exponent
parameter_list|)
block|{
return|return
name|exponent
return|;
block|}
DECL|method|checkGwtRpcEnabled ()
specifier|static
name|void
name|checkGwtRpcEnabled
parameter_list|()
block|{
name|String
name|propertyName
init|=
literal|"guava.gwt.emergency_reenable_rpc"
decl_stmt|;
if|if
condition|(
operator|!
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|,
literal|"false"
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
operator|.
name|lenientFormat
argument_list|(
literal|"We are removing GWT-RPC support for Guava types. You can temporarily reenable"
operator|+
literal|" support by setting the system property %s to true. For more about system"
operator|+
literal|" properties, see %s. For more about Guava's GWT-RPC support, see %s."
argument_list|,
name|propertyName
argument_list|,
literal|"https://stackoverflow.com/q/5189914/28465"
argument_list|,
literal|"https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"
argument_list|)
argument_list|)
throw|;
block|}
name|logger
operator|.
name|log
argument_list|(
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this"
operator|+
literal|" warning because you are sending a Guava type over GWT-RPC, which will break. You"
operator|+
literal|" can identify which type by looking at the class name in the attached stack trace."
argument_list|,
operator|new
name|Throwable
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

