begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2012 Google Inc. All Rights Reserved
end_comment

begin_package
DECL|package|java.nio.charset
package|package
name|java
operator|.
name|nio
operator|.
name|charset
package|;
end_package

begin_comment
comment|/**  * GWT emulation of {@link UnsupportedCharsetException}.  *   * @author gak@google.com (Gregory Kick)  */
end_comment

begin_class
DECL|class|UnsupportedCharsetException
specifier|public
class|class
name|UnsupportedCharsetException
extends|extends
name|IllegalArgumentException
block|{
DECL|field|charsetName
specifier|private
specifier|final
name|String
name|charsetName
decl_stmt|;
DECL|method|UnsupportedCharsetException (String charsetName)
specifier|public
name|UnsupportedCharsetException
parameter_list|(
name|String
name|charsetName
parameter_list|)
block|{
name|super
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|charsetName
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|charsetName
operator|=
name|charsetName
expr_stmt|;
block|}
DECL|method|getCharsetName ()
specifier|public
name|String
name|getCharsetName
parameter_list|()
block|{
return|return
name|charsetName
return|;
block|}
block|}
end_class

end_unit

