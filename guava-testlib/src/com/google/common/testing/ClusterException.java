begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2009 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.testing
package|package
name|com
operator|.
name|google
operator|.
name|common
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_comment
comment|/**  * An {@link ClusterException} is a data structure that allows for some code to "throw multiple  * exceptions", or something close to it. The prototypical code that calls for this class is  * presented below:  *  *<pre>  * void runManyThings({@literal List<ThingToRun>} thingsToRun) {  *   for (ThingToRun thingToRun : thingsToRun) {  *     thingToRun.run(); // say this may throw an exception, but you want to  *                       // always run all thingsToRun  *   }  * }  *</pre>  *  *<p>This is what the code would become:  *  *<pre>  * void runManyThings({@literal List<ThingToRun>} thingsToRun) {  *   {@literal List<Exception>} exceptions = Lists.newArrayList();  *   for (ThingToRun thingToRun : thingsToRun) {  *     try {  *       thingToRun.run();  *     } catch (Exception e) {  *       exceptions.add(e);  *     }  *   }  *   if (exceptions.size()&gt; 0) {  *     throw ClusterException.create(exceptions);  *   }  * }  *</pre>  *  *<p>See semantic details at {@link #create(Collection)}.  *  * @author Luiz-Otavio Zorzella  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|ClusterException
specifier|final
class|class
name|ClusterException
extends|extends
name|RuntimeException
block|{
DECL|field|exceptions
specifier|public
specifier|final
name|Collection
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptions
decl_stmt|;
DECL|method|ClusterException (Collection<? extends Throwable> exceptions)
specifier|private
name|ClusterException
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptions
parameter_list|)
block|{
name|super
argument_list|(
name|exceptions
operator|.
name|size
argument_list|()
operator|+
literal|" exceptions were thrown. The first exception is listed as a cause."
argument_list|,
name|exceptions
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
name|temp
init|=
operator|new
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
argument_list|()
decl_stmt|;
name|temp
operator|.
name|addAll
argument_list|(
name|exceptions
argument_list|)
expr_stmt|;
name|this
operator|.
name|exceptions
operator|=
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|temp
argument_list|)
expr_stmt|;
block|}
comment|/**    * @see #create(Collection)    */
DECL|method|create (Throwable... exceptions)
specifier|public
specifier|static
name|RuntimeException
name|create
parameter_list|(
name|Throwable
modifier|...
name|exceptions
parameter_list|)
block|{
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
name|temp
init|=
operator|new
name|ArrayList
argument_list|<
name|Throwable
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|exceptions
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|create
argument_list|(
name|temp
argument_list|)
return|;
block|}
comment|/**    * Given a collection of exceptions, returns a {@link RuntimeException}, with    * the following rules:    *    *<ul>    *<li>If {@code exceptions} has a single exception and that exception is a    *    {@link RuntimeException}, return it    *<li>If {@code exceptions} has a single exceptions and that exceptions is    *<em>not</em> a {@link RuntimeException}, return a simple    *    {@code RuntimeException} that wraps it    *<li>Otherwise, return an instance of {@link ClusterException} that wraps    *    the first exception in the {@code exceptions} collection.    *</ul>    *    *<p>Though this method takes any {@link Collection}, it often makes most    * sense to pass a {@link java.util.List} or some other collection that    * preserves the order in which the exceptions got added.    *    * @throws NullPointerException if {@code exceptions} is null    * @throws IllegalArgumentException if {@code exceptions} is empty    */
DECL|method|create (Collection<? extends Throwable> exceptions)
specifier|public
specifier|static
name|RuntimeException
name|create
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptions
parameter_list|)
block|{
if|if
condition|(
name|exceptions
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't create an ExceptionCollection with no exceptions"
argument_list|)
throw|;
block|}
if|if
condition|(
name|exceptions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|Throwable
name|temp
init|=
name|exceptions
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|temp
operator|instanceof
name|RuntimeException
condition|)
block|{
return|return
operator|(
name|RuntimeException
operator|)
name|temp
return|;
block|}
else|else
block|{
return|return
operator|new
name|RuntimeException
argument_list|(
name|temp
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|ClusterException
argument_list|(
name|exceptions
argument_list|)
return|;
block|}
block|}
end_class

end_unit

