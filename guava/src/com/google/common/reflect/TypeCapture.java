begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2012 Google Inc. All Rights Reserved.
end_comment

begin_package
DECL|package|com.google.common.reflect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|reflect
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
name|checkArgument
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
name|ParameterizedType
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
name|Type
import|;
end_import

begin_comment
comment|/**  * Captures the actual type of {@code T}.  *  * @author benyu@google.com (Ben Yu)  */
end_comment

begin_class
DECL|class|TypeCapture
specifier|abstract
class|class
name|TypeCapture
parameter_list|<
name|T
parameter_list|>
block|{
comment|/** Returns the captured type. */
DECL|method|capture ()
specifier|final
name|Type
name|capture
parameter_list|()
block|{
name|Type
name|superclass
init|=
name|getClass
argument_list|()
operator|.
name|getGenericSuperclass
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|superclass
operator|instanceof
name|ParameterizedType
argument_list|,
literal|"%s isn't parameterized"
argument_list|,
name|superclass
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|ParameterizedType
operator|)
name|superclass
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
return|;
block|}
block|}
end_class

end_unit

