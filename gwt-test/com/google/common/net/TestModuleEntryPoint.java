begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2010 Google Inc. All Rights Reserved.
end_comment

begin_package
DECL|package|com.google.common.net
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|net
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|EntryPoint
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author hhchan@google.com (Hayward Chan)  */
end_comment

begin_class
DECL|class|TestModuleEntryPoint
specifier|public
class|class
name|TestModuleEntryPoint
implements|implements
name|EntryPoint
block|{
comment|// It matches all classes that are both @GwtCompatible and Google internal.
annotation|@
name|Override
DECL|method|onModuleLoad ()
specifier|public
name|void
name|onModuleLoad
parameter_list|()
block|{
comment|// TODO: Auto generate this list.
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|allClasses
init|=
name|Arrays
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|>
name|asList
argument_list|(
name|InternetDomainName
operator|.
name|class
argument_list|,
name|TldPatterns
operator|.
name|class
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

