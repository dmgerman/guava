begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2020 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A class that uses a couple Java 8 features but doesn't really do anything. This lets us attempt  * to load it and log a warning if that fails, giving users advance notice of our dropping Java 8  * support.  */
end_comment

begin_comment
comment|/*  * This class should be annotated @GwtCompatible. But if we annotate it @GwtCompatible, then we need  * to build GwtCompatible.java (-source 7 -target 7 in the Android flavor) before we build  * Java8Usage.java (-source 8 target 8, which we already need to build before the rest of  * common.base). We could configure Maven to do that, but it's easier to just skip the annotation.  */
end_comment

begin_class
DECL|class|Java8Usage
specifier|final
class|class
name|Java8Usage
block|{
annotation|@
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
argument_list|(
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|TYPE_USE
argument_list|)
annotation|@
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
argument_list|(
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
DECL|annotation|SomeTypeAnnotation
specifier|private
annotation_defn|@interface
name|SomeTypeAnnotation
block|{}
annotation|@
name|CanIgnoreReturnValue
DECL|method|performCheck ()
specifier|static
annotation|@
name|SomeTypeAnnotation
name|String
name|performCheck
parameter_list|()
block|{
name|Runnable
name|r
init|=
parameter_list|()
lambda|->
block|{}
decl_stmt|;
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
return|return
literal|""
return|;
block|}
DECL|method|Java8Usage ()
specifier|private
name|Java8Usage
parameter_list|()
block|{}
block|}
end_class

end_unit

