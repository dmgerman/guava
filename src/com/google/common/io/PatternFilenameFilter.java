begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2006 Google Inc.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|base
operator|.
name|Preconditions
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilenameFilter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_comment
comment|/**  * File name filter that only accepts files matching a regular expression.  *  * @author Apple Chow  * @since 1  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|PatternFilenameFilter
specifier|public
specifier|final
class|class
name|PatternFilenameFilter
implements|implements
name|FilenameFilter
block|{
DECL|field|pattern
specifier|private
specifier|final
name|Pattern
name|pattern
decl_stmt|;
comment|/**    * Constructs a pattern file name filter object.    * @param patternStr the pattern string on which to filter file names    *    * @throws PatternSyntaxException if pattern compilation fails (runtime)    */
DECL|method|PatternFilenameFilter (String patternStr)
specifier|public
name|PatternFilenameFilter
parameter_list|(
name|String
name|patternStr
parameter_list|)
block|{
name|this
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|patternStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Constructs a pattern file name filter object.    * @param pattern the pattern on which to filter file names    */
DECL|method|PatternFilenameFilter (Pattern pattern)
specifier|public
name|PatternFilenameFilter
parameter_list|(
name|Pattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|Preconditions
operator|.
name|checkNotNull
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
DECL|method|accept (File dir, String fileName)
annotation|@
name|Override
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
return|return
name|pattern
operator|.
name|matcher
argument_list|(
name|fileName
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
block|}
end_class

end_unit

