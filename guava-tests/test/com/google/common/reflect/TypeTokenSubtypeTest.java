begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2016 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
annotation|@
name|AndroidIncompatible
comment|// lots of failures, possibly some related to bad equals() implementations?
DECL|class|TypeTokenSubtypeTest
specifier|public
class|class
name|TypeTokenSubtypeTest
extends|extends
name|TestCase
block|{
DECL|method|testOwnerTypeSubtypes ()
specifier|public
name|void
name|testOwnerTypeSubtypes
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|OwnerTypeSubtypingTests
argument_list|()
operator|.
name|testAllDeclarations
argument_list|()
expr_stmt|;
block|}
DECL|method|testWildcardSubtypes ()
specifier|public
name|void
name|testWildcardSubtypes
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|WildcardSubtypingTests
argument_list|()
operator|.
name|testAllDeclarations
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubtypeOfInnerClass_nonStaticAnonymousClass ()
specifier|public
name|void
name|testSubtypeOfInnerClass_nonStaticAnonymousClass
parameter_list|()
block|{
name|TypeToken
argument_list|<
name|?
argument_list|>
name|supertype
init|=
operator|new
name|TypeToken
argument_list|<
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|subclass
init|=
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|subclass
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|supertype
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubtypeOfInnerClass_nonStaticAnonymousClass_typeParameterOfOwnerTypeNotMatch ()
specifier|public
name|void
name|testSubtypeOfInnerClass_nonStaticAnonymousClass_typeParameterOfOwnerTypeNotMatch
parameter_list|()
block|{
name|TypeToken
argument_list|<
name|?
argument_list|>
name|supertype
init|=
operator|new
name|TypeToken
argument_list|<
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|subclass
init|=
operator|new
name|Mall
argument_list|<
name|Indoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|subclass
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|supertype
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubtypeOfInnerClass_nonStaticAnonymousClass_typeParameterOfInnerTypeNotMatch ()
specifier|public
name|void
name|testSubtypeOfInnerClass_nonStaticAnonymousClass_typeParameterOfInnerTypeNotMatch
parameter_list|()
block|{
name|TypeToken
argument_list|<
name|?
argument_list|>
name|supertype
init|=
operator|new
name|TypeToken
argument_list|<
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|subclass
init|=
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Grocery
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|subclass
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|supertype
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubtypeOfInnerClass_staticAnonymousClass ()
specifier|public
specifier|static
name|void
name|testSubtypeOfInnerClass_staticAnonymousClass
parameter_list|()
block|{
name|TypeToken
argument_list|<
name|?
argument_list|>
name|supertype
init|=
operator|new
name|TypeToken
argument_list|<
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|subclass
init|=
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|subclass
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|supertype
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubtypeOfStaticAnonymousClass ()
specifier|public
specifier|static
name|void
name|testSubtypeOfStaticAnonymousClass
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|superclass
init|=
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|superclass
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|superclass
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|superclass
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testSubtypeOfNonStaticAnonymousClass ()
specifier|public
name|void
name|testSubtypeOfNonStaticAnonymousClass
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|superclass
init|=
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
name|superclass
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|superclass
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|TypeToken
operator|.
name|of
argument_list|(
operator|new
name|Mall
argument_list|<
name|Outdoor
argument_list|>
argument_list|()
operator|.
operator|new
name|Shop
argument_list|<
name|Electronics
argument_list|>
argument_list|()
block|{}
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|isSubtypeOf
argument_list|(
name|superclass
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|OwnerTypeSubtypingTests
specifier|private
specifier|static
class|class
name|OwnerTypeSubtypingTests
extends|extends
name|SubtypeTester
block|{
annotation|@
name|TestSubtype
DECL|method|innerTypeIsSubtype ( Mall<Outdoor>.Retailer<Electronics> retailer)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|innerTypeIsSubtype
parameter_list|(
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|retailer
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|retailer
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|innerTypeIsSubtype_supertypeWithWildcard ( Mall<Outdoor>.Retailer<Electronics> retailer)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|?
extends|extends
name|Electronics
argument_list|>
name|innerTypeIsSubtype_supertypeWithWildcard
parameter_list|(
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|retailer
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|retailer
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|innerTypeIsSubtype_withWildcards ( Mall<? extends Indoor>.Retailer<? extends Electronics> retailer)
specifier|public
name|Mall
argument_list|<
name|?
argument_list|>
operator|.
name|Shop
argument_list|<
name|?
argument_list|>
name|innerTypeIsSubtype_withWildcards
parameter_list|(
name|Mall
argument_list|<
name|?
extends|extends
name|Indoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|?
extends|extends
name|Electronics
argument_list|>
name|retailer
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|retailer
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|ownerTypeIsSubtype ( Outlet<Outdoor>.Shop<Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|ownerTypeIsSubtype
parameter_list|(
name|Outlet
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|ownerTypeIsSubtype_supertypeWithWildcard ( Outlet<Outdoor>.Shop<Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|?
extends|extends
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|ownerTypeIsSubtype_supertypeWithWildcard
parameter_list|(
name|Outlet
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|ownerTypeIsSubtype_withWildcards ( Outlet<? extends Outdoor>.Shop<Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|?
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|ownerTypeIsSubtype_withWildcards
parameter_list|(
name|Outlet
argument_list|<
name|?
extends|extends
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|bothOwnerTypeAndInnerTypeAreSubtypes ( Outlet<Outdoor>.Retailer<Electronics> retailer)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|bothOwnerTypeAndInnerTypeAreSubtypes
parameter_list|(
name|Outlet
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|retailer
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|retailer
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
specifier|public
name|Mall
argument_list|<
name|?
super|super
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|?
extends|extends
name|Electronics
argument_list|>
DECL|method|bothOwnerTypeAndInnerTypeAreSubtypes_supertypeWithWildcard ( Outlet<Outdoor>.Retailer<Electronics> retailer)
name|bothOwnerTypeAndInnerTypeAreSubtypes_supertypeWithWildcard
parameter_list|(
name|Outlet
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|retailer
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|retailer
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
specifier|public
name|Mall
argument_list|<
name|?
super|super
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|?
extends|extends
name|Electronics
argument_list|>
DECL|method|bothOwnerTypeAndInnerTypeAreSubtypes_withWildcards ( Outlet<Outdoor>.Retailer<Electronics> retailer)
name|bothOwnerTypeAndInnerTypeAreSubtypes_withWildcards
parameter_list|(
name|Outlet
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|retailer
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|retailer
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|ownerTypeDoesNotMatch ( Mall<Indoor>.Shop<Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|ownerTypeDoesNotMatch
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|ownerTypeDoesNotMatch_subtypeWithWildcard ( Mall<? extends Outdoor>.Shop<Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|ownerTypeDoesNotMatch_subtypeWithWildcard
parameter_list|(
name|Mall
argument_list|<
name|?
extends|extends
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|ownerTypeDoesNotMatch_supertypeWithWildcard ( Mall<?>.Shop<Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|?
extends|extends
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|ownerTypeDoesNotMatch_supertypeWithWildcard
parameter_list|(
name|Mall
argument_list|<
name|?
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|innerTypeDoesNotMatch ( Mall<Outdoor>.Shop<Grocery> shop)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|innerTypeDoesNotMatch
parameter_list|(
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Grocery
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|innerTypeDoesNotMatch_subtypeWithWildcard ( Mall<Outdoor>.Shop<? extends Electronics> shop)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|innerTypeDoesNotMatch_subtypeWithWildcard
parameter_list|(
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|?
extends|extends
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|innerTypeDoesNotMatch_supertypeWithWildcard ( Mall<Outdoor>.Shop<Grocery> shop)
specifier|public
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|?
extends|extends
name|Electronics
argument_list|>
name|innerTypeDoesNotMatch_supertypeWithWildcard
parameter_list|(
name|Mall
argument_list|<
name|Outdoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Grocery
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|supertypeIsNestedClass ( Mall<Indoor>.Retailer<Electronics> shop)
specifier|public
name|ConsumerFacing
argument_list|<
name|Electronics
argument_list|>
name|supertypeIsNestedClass
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|nestedClassIsNotSupertypeDueToTypeParameter ( Mall<Indoor>.Shop<Electronics> shop)
specifier|public
name|ConsumerFacing
argument_list|<
name|Grocery
argument_list|>
name|nestedClassIsNotSupertypeDueToTypeParameter
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|nestedClassIsNotSupertype ( Mall<Indoor>.Shop<Grocery> shop)
specifier|public
name|ConsumerFacing
argument_list|<
name|Grocery
argument_list|>
name|nestedClassIsNotSupertype
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Grocery
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|supertypeIsTopLevelClass ( Mall<Indoor>.Retailer<Electronics> shop)
specifier|public
name|Comparator
argument_list|<
name|Electronics
argument_list|>
name|supertypeIsTopLevelClass
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|topLevelClassIsNotSupertypeDueToTypeParameter ( Mall<Indoor>.Retailer<Grocery> shop)
specifier|public
name|Comparator
argument_list|<
name|Electronics
argument_list|>
name|topLevelClassIsNotSupertypeDueToTypeParameter
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Retailer
argument_list|<
name|Grocery
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
DECL|method|topLevelClassIsNotSupertype ( Mall<Indoor>.Shop<Electronics> shop)
specifier|public
name|Comparator
argument_list|<
name|Electronics
argument_list|>
name|topLevelClassIsNotSupertype
parameter_list|(
name|Mall
argument_list|<
name|Indoor
argument_list|>
operator|.
name|Shop
argument_list|<
name|Electronics
argument_list|>
name|shop
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|shop
argument_list|)
return|;
block|}
block|}
DECL|class|WildcardSubtypingTests
specifier|private
specifier|static
class|class
name|WildcardSubtypingTests
extends|extends
name|SubtypeTester
block|{
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|supertypeWithWildcardUpperBound (List<T> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|supertypeWithWildcardUpperBound
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|supertypeWithWildcardUpperBound_notMatch (List<String> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|supertypeWithWildcardUpperBound_notMatch
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|supertypeWithWildcardULowerBound (List<T> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
super|super
name|T
argument_list|>
name|supertypeWithWildcardULowerBound
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|supertypeWithWildcardULowerBound_notMatch (List<String> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|supertypeWithWildcardULowerBound_notMatch
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|wildcardsMatchByUpperBound (List<? extends T> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
argument_list|>
name|wildcardsMatchByUpperBound
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|wildCardsDoNotMatchByUpperBound (List<?> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|wildCardsDoNotMatchByUpperBound
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|wildcardsMatchByLowerBound ( List<? super CharSequence> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
super|super
name|String
argument_list|>
name|wildcardsMatchByLowerBound
parameter_list|(
name|List
argument_list|<
name|?
super|super
name|CharSequence
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|isSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
annotation|@
name|TestSubtype
argument_list|(
name|suppressGetSupertype
operator|=
literal|true
argument_list|,
name|suppressGetSubtype
operator|=
literal|true
argument_list|)
DECL|method|wildCardsDoNotMatchByLowerBound ( List<? super String> list)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Iterable
argument_list|<
name|?
super|super
name|CharSequence
argument_list|>
name|wildCardsDoNotMatchByLowerBound
parameter_list|(
name|List
argument_list|<
name|?
super|super
name|String
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|notSubtype
argument_list|(
name|list
argument_list|)
return|;
block|}
block|}
comment|// TODO(benyu): migrate all subtyping tests from TypeTokenTest to this class using SubtypeTester.
DECL|interface|Outdoor
specifier|private
interface|interface
name|Outdoor
block|{}
DECL|interface|Indoor
specifier|private
interface|interface
name|Indoor
block|{}
DECL|interface|Grocery
specifier|private
interface|interface
name|Grocery
block|{}
DECL|interface|Electronics
specifier|private
interface|interface
name|Electronics
block|{}
DECL|interface|ConsumerFacing
specifier|private
interface|interface
name|ConsumerFacing
parameter_list|<
name|T
parameter_list|>
block|{}
DECL|class|Mall
specifier|private
specifier|static
class|class
name|Mall
parameter_list|<
name|T
parameter_list|>
block|{
DECL|class|Shop
class|class
name|Shop
parameter_list|<
name|ProductT
parameter_list|>
block|{}
DECL|class|Retailer
specifier|abstract
class|class
name|Retailer
parameter_list|<
name|ProductT
parameter_list|>
extends|extends
name|Shop
argument_list|<
name|ProductT
argument_list|>
implements|implements
name|Comparator
argument_list|<
name|ProductT
argument_list|>
implements|,
name|ConsumerFacing
argument_list|<
name|ProductT
argument_list|>
block|{}
block|}
DECL|class|Outlet
specifier|private
specifier|static
class|class
name|Outlet
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Mall
argument_list|<
name|T
argument_list|>
block|{}
block|}
end_class

end_unit

