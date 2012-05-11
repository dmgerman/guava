begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|testing
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
import|;
end_import

begin_comment
comment|/**  * A utility similar to {@link IteratorTester} for testing a  * {@link ListIterator} against a known good reference implementation. As with  * {@code IteratorTester}, a concrete subclass must provide target iterators on  * demand. It also requires three additional constructor parameters:  * {@code elementsToInsert}, the elements to be passed to {@code set()} and  * {@code add()} calls; {@code features}, the features supported by the  * iterator; and {@code expectedElements}, the elements the iterator should  * return in order.  *<p>  * The items in {@code elementsToInsert} will be repeated if {@code steps} is  * larger than the number of provided elements.  *  *<p>This class is GWT compatible.  *  * @author Chris Povirk  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ListIteratorTester
specifier|public
specifier|abstract
class|class
name|ListIteratorTester
parameter_list|<
name|E
parameter_list|>
extends|extends
name|AbstractIteratorTester
argument_list|<
name|E
argument_list|,
name|ListIterator
argument_list|<
name|E
argument_list|>
argument_list|>
block|{
DECL|method|ListIteratorTester (int steps, Iterable<E> elementsToInsert, Iterable<? extends IteratorFeature> features, Iterable<E> expectedElements, int startIndex)
specifier|protected
name|ListIteratorTester
parameter_list|(
name|int
name|steps
parameter_list|,
name|Iterable
argument_list|<
name|E
argument_list|>
name|elementsToInsert
parameter_list|,
name|Iterable
argument_list|<
name|?
extends|extends
name|IteratorFeature
argument_list|>
name|features
parameter_list|,
name|Iterable
argument_list|<
name|E
argument_list|>
name|expectedElements
parameter_list|,
name|int
name|startIndex
parameter_list|)
block|{
name|super
argument_list|(
name|steps
argument_list|,
name|elementsToInsert
argument_list|,
name|features
argument_list|,
name|expectedElements
argument_list|,
name|KnownOrder
operator|.
name|KNOWN_ORDER
argument_list|,
name|startIndex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|Iterable
argument_list|<
name|?
extends|extends
name|Stimulus
argument_list|<
name|E
argument_list|,
name|?
super|super
name|ListIterator
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
DECL|method|getStimulusValues ()
name|getStimulusValues
parameter_list|()
block|{
name|List
argument_list|<
name|Stimulus
argument_list|<
name|E
argument_list|,
name|?
super|super
name|ListIterator
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Stimulus
argument_list|<
name|E
argument_list|,
name|?
super|super
name|ListIterator
argument_list|<
name|E
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Helpers
operator|.
name|addAll
argument_list|(
name|list
argument_list|,
name|iteratorStimuli
argument_list|()
argument_list|)
expr_stmt|;
name|Helpers
operator|.
name|addAll
argument_list|(
name|list
argument_list|,
name|listIteratorStimuli
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
DECL|method|newTargetIterator ()
annotation|@
name|Override
specifier|protected
specifier|abstract
name|ListIterator
argument_list|<
name|E
argument_list|>
name|newTargetIterator
parameter_list|()
function_decl|;
block|}
end_class

end_unit

