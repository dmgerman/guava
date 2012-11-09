begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.io
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|collect
operator|.
name|ImmutableMap
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
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|Method
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
name|Modifier
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

begin_comment
comment|/**  * @param<S> the source or sink type  * @param<T> the data type (byte[] or String)  * @param<F> the factory type  * @author Colin Decker  */
end_comment

begin_class
DECL|class|SourceSinkTester
specifier|public
class|class
name|SourceSinkTester
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|,
name|F
extends|extends
name|SourceSinkFactory
parameter_list|<
name|S
parameter_list|,
name|T
parameter_list|>
parameter_list|>
extends|extends
name|TestCase
block|{
DECL|field|LOREM_IPSUM
specifier|static
specifier|final
name|String
name|LOREM_IPSUM
init|=
literal|"Lorem ipsum dolor sit amet, consectetur adipiscing "
operator|+
literal|"elit. Cras fringilla elit ac ipsum adipiscing vulputate. Maecenas in lorem nulla, ac "
operator|+
literal|"sollicitudin quam. Praesent neque elit, sodales quis vestibulum vel, pellentesque nec "
operator|+
literal|"erat. Proin cursus commodo lacus eget congue. Aliquam erat volutpat. Fusce ut leo sed "
operator|+
literal|"risus tempor vehicula et a odio. Nam aliquet dolor viverra libero rutrum accumsan quis "
operator|+
literal|"in augue. Suspendisse id dui in lorem tristique placerat eget vel risus. Sed metus neque, "
operator|+
literal|"scelerisque in molestie ac, mattis quis lectus. Pellentesque viverra justo commodo quam "
operator|+
literal|"bibendum ut gravida leo accumsan. Nullam malesuada sagittis diam, quis suscipit mauris "
operator|+
literal|"euismod vulputate. Pellentesque ultrices tellus sed lorem aliquet pulvinar. Nam lorem "
operator|+
literal|"nunc, ultrices at auctor non, scelerisque eget turpis. Nullam eget varius erat. Sed a "
operator|+
literal|"lorem id arcu dictum euismod. Fusce lectus odio, elementum ullamcorper mattis viverra, "
operator|+
literal|"dictum sit amet lacus.\n"
operator|+
literal|"\n"
operator|+
literal|"Nunc quis lacus est. Sed aliquam pretium cursus. Sed eu libero eros. In hac habitasse "
operator|+
literal|"platea dictumst. Pellentesque molestie, nibh nec iaculis luctus, justo sem lobortis enim, "
operator|+
literal|"at feugiat leo magna nec libero. Mauris quis odio eget nisl rutrum cursus nec eget augue. "
operator|+
literal|"Sed nec arcu sem. In hac habitasse platea dictumst."
decl_stmt|;
DECL|field|TEST_STRINGS
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|TEST_STRINGS
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|String
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|"empty"
argument_list|,
literal|""
argument_list|)
decl|.
name|put
argument_list|(
literal|"1 char"
argument_list|,
literal|"0"
argument_list|)
decl|.
name|put
argument_list|(
literal|"1 word"
argument_list|,
literal|"hello"
argument_list|)
decl|.
name|put
argument_list|(
literal|"2 words"
argument_list|,
literal|"hello world"
argument_list|)
decl|.
name|put
argument_list|(
literal|"\\n line break"
argument_list|,
literal|"hello\nworld"
argument_list|)
decl|.
name|put
argument_list|(
literal|"\\r line break"
argument_list|,
literal|"hello\rworld"
argument_list|)
decl|.
name|put
argument_list|(
literal|"\\r\\n line break"
argument_list|,
literal|"hello\r\nworld"
argument_list|)
decl|.
name|put
argument_list|(
literal|"\\n at EOF"
argument_list|,
literal|"hello\nworld\n"
argument_list|)
decl|.
name|put
argument_list|(
literal|"\\r at EOF"
argument_list|,
literal|"hello\nworld\r"
argument_list|)
decl|.
name|put
argument_list|(
literal|"lorem ipsum"
argument_list|,
name|LOREM_IPSUM
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
DECL|field|factory
specifier|protected
specifier|final
name|F
name|factory
decl_stmt|;
DECL|field|data
specifier|protected
specifier|final
name|T
name|data
decl_stmt|;
DECL|field|expected
specifier|protected
specifier|final
name|T
name|expected
decl_stmt|;
DECL|field|suiteName
specifier|private
specifier|final
name|String
name|suiteName
decl_stmt|;
DECL|field|caseDesc
specifier|private
specifier|final
name|String
name|caseDesc
decl_stmt|;
DECL|method|SourceSinkTester (F factory, T data, String suiteName, String caseDesc, Method method)
name|SourceSinkTester
parameter_list|(
name|F
name|factory
parameter_list|,
name|T
name|data
parameter_list|,
name|String
name|suiteName
parameter_list|,
name|String
name|caseDesc
parameter_list|,
name|Method
name|method
parameter_list|)
block|{
name|super
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|checkNotNull
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|checkNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|this
operator|.
name|expected
operator|=
name|checkNotNull
argument_list|(
name|factory
operator|.
name|getExpected
argument_list|(
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|suiteName
operator|=
name|checkNotNull
argument_list|(
name|suiteName
argument_list|)
expr_stmt|;
name|this
operator|.
name|caseDesc
operator|=
name|checkNotNull
argument_list|(
name|caseDesc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|super
operator|.
name|getName
argument_list|()
operator|+
literal|" ["
operator|+
name|suiteName
operator|+
literal|" ["
operator|+
name|caseDesc
operator|+
literal|"]]"
return|;
block|}
DECL|method|getLines (final String string)
specifier|protected
specifier|static
name|ImmutableList
argument_list|<
name|String
argument_list|>
name|getLines
parameter_list|(
specifier|final
name|String
name|string
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|CharSource
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Reader
name|openStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|StringReader
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
operator|.
name|readLines
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|()
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|factory
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|getTestMethods (Class<?> testClass)
specifier|static
name|ImmutableList
argument_list|<
name|Method
argument_list|>
name|getTestMethods
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|testClass
parameter_list|)
block|{
name|List
argument_list|<
name|Method
argument_list|>
name|result
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|testClass
operator|.
name|getDeclaredMethods
argument_list|()
control|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
operator|&&
name|method
operator|.
name|getReturnType
argument_list|()
operator|==
name|void
operator|.
name|class
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
operator|&&
name|method
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|result
argument_list|)
return|;
block|}
block|}
end_class

end_unit

