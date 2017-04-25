begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|GwtCompatible
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|errorprone
operator|.
name|annotations
operator|.
name|CanIgnoreReturnValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_comment
comment|/**  * This class provides a skeletal implementation of the {@code Iterator}  * interface, to make this interface easier to implement for certain types of  * data sources.  *  *<p>{@code Iterator} requires its implementations to support querying the  * end-of-data status without changing the iterator's state, using the {@link  * #hasNext} method. But many data sources, such as {@link  * java.io.Reader#read()}, do not expose this information; the only way to  * discover whether there is any data left is by trying to retrieve it. These  * types of data sources are ordinarily difficult to write iterators for. But  * using this class, one must implement only the {@link #computeNext} method,  * and invoke the {@link #endOfData} method when appropriate.  *  *<p>Another example is an iterator that skips over null elements in a backing  * iterator. This could be implemented as:<pre>   {@code  *  *   public static Iterator<String> skipNulls(final Iterator<String> in) {  *     return new AbstractIterator<String>() {  *       protected String computeNext() {  *         while (in.hasNext()) {  *           String s = in.next();  *           if (s != null) {  *             return s;  *           }  *         }  *         return endOfData();  *       }  *     };  *   }}</pre>  *  *<p>This class supports iterators that include null elements.  *  * @author Kevin Bourrillion  * @since 2.0  */
end_comment

begin_comment
comment|// When making changes to this class, please also update the copy at
end_comment

begin_comment
comment|// com.google.common.base.AbstractIterator
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|AbstractIterator
specifier|public
specifier|abstract
class|class
name|AbstractIterator
parameter_list|<
name|T
parameter_list|>
extends|extends
name|UnmodifiableIterator
argument_list|<
name|T
argument_list|>
block|{
DECL|field|state
specifier|private
name|State
name|state
init|=
name|State
operator|.
name|NOT_READY
decl_stmt|;
comment|/** Constructor for use by subclasses. */
DECL|method|AbstractIterator ()
specifier|protected
name|AbstractIterator
parameter_list|()
block|{}
DECL|enum|State
specifier|private
enum|enum
name|State
block|{
comment|/** We have computed the next element and haven't returned it yet. */
DECL|enumConstant|READY
name|READY
block|,
comment|/** We haven't yet computed or have already returned the element. */
DECL|enumConstant|NOT_READY
name|NOT_READY
block|,
comment|/** We have reached the end of the data and are finished. */
DECL|enumConstant|DONE
name|DONE
block|,
comment|/** We've suffered an exception and are kaput. */
DECL|enumConstant|FAILED
name|FAILED
block|,   }
DECL|field|next
specifier|private
name|T
name|next
decl_stmt|;
comment|/**    * Returns the next element.<b>Note:</b> the implementation must call {@link    * #endOfData()} when there are no elements left in the iteration. Failure to    * do so could result in an infinite loop.    *    *<p>The initial invocation of {@link #hasNext()} or {@link #next()} calls    * this method, as does the first invocation of {@code hasNext} or {@code    * next} following each successful call to {@code next}. Once the    * implementation either invokes {@code endOfData} or throws an exception,    * {@code computeNext} is guaranteed to never be called again.    *    *<p>If this method throws an exception, it will propagate outward to the    * {@code hasNext} or {@code next} invocation that invoked this method. Any    * further attempts to use the iterator will result in an {@link    * IllegalStateException}.    *    *<p>The implementation of this method may not invoke the {@code hasNext},    * {@code next}, or {@link #peek()} methods on this instance; if it does, an    * {@code IllegalStateException} will result.    *    * @return the next element if there was one. If {@code endOfData} was called    *     during execution, the return value will be ignored.    * @throws RuntimeException if any unrecoverable error happens. This exception    *     will propagate outward to the {@code hasNext()}, {@code next()}, or    *     {@code peek()} invocation that invoked this method. Any further    *     attempts to use the iterator will result in an    *     {@link IllegalStateException}.    */
DECL|method|computeNext ()
specifier|protected
specifier|abstract
name|T
name|computeNext
parameter_list|()
function_decl|;
comment|/**    * Implementations of {@link #computeNext}<b>must</b> invoke this method when    * there are no elements left in the iteration.    *    * @return {@code null}; a convenience so your {@code computeNext}    *     implementation can use the simple statement {@code return endOfData();}    */
annotation|@
name|CanIgnoreReturnValue
DECL|method|endOfData ()
specifier|protected
specifier|final
name|T
name|endOfData
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|DONE
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(kak): Should we remove this? Some people are using it to prefetch?
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
specifier|final
name|boolean
name|hasNext
parameter_list|()
block|{
name|checkState
argument_list|(
name|state
operator|!=
name|State
operator|.
name|FAILED
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|DONE
case|:
return|return
literal|false
return|;
case|case
name|READY
case|:
return|return
literal|true
return|;
default|default:
block|}
return|return
name|tryToComputeNext
argument_list|()
return|;
block|}
DECL|method|tryToComputeNext ()
specifier|private
name|boolean
name|tryToComputeNext
parameter_list|()
block|{
name|state
operator|=
name|State
operator|.
name|FAILED
expr_stmt|;
comment|// temporary pessimism
name|next
operator|=
name|computeNext
argument_list|()
expr_stmt|;
if|if
condition|(
name|state
operator|!=
name|State
operator|.
name|DONE
condition|)
block|{
name|state
operator|=
name|State
operator|.
name|READY
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|CanIgnoreReturnValue
comment|// TODO(kak): Should we remove this?
annotation|@
name|Override
DECL|method|next ()
specifier|public
specifier|final
name|T
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
name|state
operator|=
name|State
operator|.
name|NOT_READY
expr_stmt|;
name|T
name|result
init|=
name|next
decl_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/**    * Returns the next element in the iteration without advancing the iteration,    * according to the contract of {@link PeekingIterator#peek()}.    *    *<p>Implementations of {@code AbstractIterator} that wish to expose this    * functionality should implement {@code PeekingIterator}.    */
DECL|method|peek ()
specifier|public
specifier|final
name|T
name|peek
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
return|return
name|next
return|;
block|}
block|}
end_class

end_unit
