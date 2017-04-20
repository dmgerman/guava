begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A dummy superclass to support GWT serialization of the element type of an {@link  * ImmutableMultiset}. The GWT supersource for this class contains a field of type {@code E}.  *  *<p>For details about this hack, see {@link GwtSerializationDependencies}, which takes the same  * approach but with a subclass rather than a superclass.  *  *<p>TODO(cpovirk): Consider applying this subclass approach to our other types.  *  *<p>For {@code ImmutableMultiset} in particular, I ran into a problem with the {@code  * GwtSerializationDependencies} approach: When autogenerating a serializer for the new class, GWT  * tries to refer to our dummy serializer for the superclass,  * ImmutableMultiset_CustomFieldSerializer. But that type has no methods (since it's never actually  * used). We could probably fix the problem by adding dummy methods to that class, but that is  * starting to sound harder than taking the superclass approach, which I've been coming to like,  * anyway, since it doesn't require us to declare dummy methods (though occasionally constructors)  * and make types non-final.  */
end_comment

begin_class
annotation|@
name|GwtCompatible
argument_list|(
name|emulated
operator|=
literal|true
argument_list|)
DECL|class|ImmutableMultisetGwtSerializationDependencies
specifier|abstract
class|class
name|ImmutableMultisetGwtSerializationDependencies
parameter_list|<
name|E
parameter_list|>
extends|extends
name|ImmutableCollection
argument_list|<
name|E
argument_list|>
block|{}
end_class

end_unit

