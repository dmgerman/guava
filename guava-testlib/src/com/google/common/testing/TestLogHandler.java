begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2008 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|logging
operator|.
name|Handler
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
name|LogRecord
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
comment|/**  * Tests may use this to intercept messages that are logged by the code under test. Example:  *  *<pre>  *   TestLogHandler handler;  *  *   protected void setUp() throws Exception {  *     super.setUp();  *     handler = new TestLogHandler();  *     SomeClass.logger.addHandler(handler);  *     addTearDown(new TearDown() {  *       public void tearDown() throws Exception {  *         SomeClass.logger.removeHandler(handler);  *       }  *     });  *   }  *  *   public void test() {  *     SomeClass.foo();  *     LogRecord firstRecord = handler.getStoredLogRecords().get(0);  *     assertEquals("some message", firstRecord.getMessage());  *   }  *</pre>  *  * @author Kevin Bourrillion  * @since 10.0  */
end_comment

begin_class
annotation|@
name|Beta
annotation|@
name|GwtCompatible
DECL|class|TestLogHandler
specifier|public
class|class
name|TestLogHandler
extends|extends
name|Handler
block|{
comment|/** We will keep a private list of all logged records */
DECL|field|list
specifier|private
specifier|final
name|List
argument_list|<
name|LogRecord
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** Adds the most recently logged record to our list. */
annotation|@
name|Override
DECL|method|publish (@ullable LogRecord record)
specifier|public
specifier|synchronized
name|void
name|publish
parameter_list|(
annotation|@
name|Nullable
name|LogRecord
name|record
parameter_list|)
block|{
name|list
operator|.
name|add
argument_list|(
name|record
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{}
DECL|method|clear ()
specifier|public
specifier|synchronized
name|void
name|clear
parameter_list|()
block|{
name|list
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/** Returns a snapshot of the logged records. */
comment|/*    * TODO(cpovirk): consider higher-level APIs here (say, assertNoRecordsLogged(),    * getOnlyRecordLogged(), getAndClearLogRecords()...)    *    * TODO(cpovirk): consider renaming this method to reflect that it takes a snapshot (and/or return    * an ImmutableList)    */
DECL|method|getStoredLogRecords ()
specifier|public
specifier|synchronized
name|List
argument_list|<
name|LogRecord
argument_list|>
name|getStoredLogRecords
parameter_list|()
block|{
name|List
argument_list|<
name|LogRecord
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|list
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
end_class

end_unit

