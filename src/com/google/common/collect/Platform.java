begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|logging
operator|.
name|Logger
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
class|class
name|Platform
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
name|Platform
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
decl_stmt|;
comment|/**    * Clone the given array using {@link Object#clone()}.  It is factored out so    * that it can be emulated in GWT.    */
DECL|method|clone (T[] array)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|clone
parameter_list|(
name|T
index|[]
name|array
parameter_list|)
block|{
return|return
name|array
operator|.
name|clone
argument_list|()
return|;
block|}
comment|/**    * Wrapper around {@link System#arraycopy} so that it can be emulated    * correctly in GWT.    *    *<p>It is only intended for the case {@code src} and {@code dest} are    * different.  It also doesn't validate the types and indices.    *    *<p>As of GWT 2.0, The built-in {@link System#arraycopy} doesn't work    * in general case.  See    * http://code.google.com/p/google-web-toolkit/issues/detail?id=3621    * for more details.    */
DECL|method|unsafeArrayCopy ( Object[] src, int srcPos, Object[] dest, int destPos, int length)
specifier|static
name|void
name|unsafeArrayCopy
parameter_list|(
name|Object
index|[]
name|src
parameter_list|,
name|int
name|srcPos
parameter_list|,
name|Object
index|[]
name|dest
parameter_list|,
name|int
name|destPos
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|src
argument_list|,
name|srcPos
argument_list|,
name|dest
argument_list|,
name|destPos
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns a new array of the given length with the specified component type.    *    * @param type the component type    * @param length the length of the new array    */
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Array.newInstance(Class, int)"
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|newArray (Class<T> type, int length)
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
index|[]
name|newArray
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
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
return|;
block|}
comment|/**    * Returns a new array of the given length with the same type as a reference    * array.    *    * @param reference any array of the desired type    * @param length the length of the new array    */
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
comment|/**    * Configures the given map maker to use weak keys, if possible; does nothing    * otherwise (i.e., in GWT). This is sometimes acceptable, when only    * server-side code could generate enough volume that reclamation becomes    * important.    */
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
DECL|method|Platform ()
specifier|private
name|Platform
parameter_list|()
block|{}
block|}
end_class

end_unit

