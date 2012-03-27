begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2012 Google Inc. All Rights Reserved.
end_comment

begin_package
DECL|package|com.google.common.testing.anotherpackage
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|testing
operator|.
name|anotherpackage
package|;
end_package

begin_comment
comment|/** Does not check null, but should not matter since it's in a different package. */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
comment|// For use by NullPointerTester
DECL|class|SomeClassThatDoesNotUseNullable
specifier|public
class|class
name|SomeClassThatDoesNotUseNullable
block|{
DECL|method|packagePrivateButDoesNotCheckNull (String s)
name|void
name|packagePrivateButDoesNotCheckNull
parameter_list|(
name|String
name|s
parameter_list|)
block|{}
DECL|method|protectedButDoesNotCheckNull (String s)
specifier|protected
name|void
name|protectedButDoesNotCheckNull
parameter_list|(
name|String
name|s
parameter_list|)
block|{}
DECL|method|publicButDoesNotCheckNull (String s)
specifier|public
name|void
name|publicButDoesNotCheckNull
parameter_list|(
name|String
name|s
parameter_list|)
block|{}
DECL|method|staticButDoesNotCheckNull (String s)
specifier|public
specifier|static
name|void
name|staticButDoesNotCheckNull
parameter_list|(
name|String
name|s
parameter_list|)
block|{}
block|}
end_class

end_unit

