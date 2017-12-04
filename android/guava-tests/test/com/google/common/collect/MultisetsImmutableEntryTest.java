begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2007 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.collect
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multiset
operator|.
name|Entry
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * Tests for {@link Multisets#immutableEntry}.  *  * @author Mike Bostock  */
end_comment

begin_class
annotation|@
name|GwtCompatible
DECL|class|MultisetsImmutableEntryTest
specifier|public
class|class
name|MultisetsImmutableEntryTest
extends|extends
name|TestCase
block|{
DECL|field|NE
specifier|private
specifier|static
specifier|final
name|String
name|NE
init|=
literal|null
decl_stmt|;
DECL|method|entry (final E element, final int count)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Entry
argument_list|<
name|E
argument_list|>
name|entry
parameter_list|(
specifier|final
name|E
name|element
parameter_list|,
specifier|final
name|int
name|count
parameter_list|)
block|{
return|return
name|Multisets
operator|.
name|immutableEntry
argument_list|(
name|element
argument_list|,
name|count
argument_list|)
return|;
block|}
DECL|method|control (E element, int count)
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|Entry
argument_list|<
name|E
argument_list|>
name|control
parameter_list|(
name|E
name|element
parameter_list|,
name|int
name|count
parameter_list|)
block|{
return|return
name|HashMultiset
operator|.
name|create
argument_list|(
name|Collections
operator|.
name|nCopies
argument_list|(
name|count
argument_list|,
name|element
argument_list|)
argument_list|)
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
DECL|method|testToString ()
specifier|public
name|void
name|testToString
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar x 2"
argument_list|,
name|entry
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringNull ()
specifier|public
name|void
name|testToStringNull
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"null"
argument_list|,
name|entry
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"null x 2"
argument_list|,
name|entry
argument_list|(
name|NE
argument_list|,
literal|2
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testEquals ()
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|control
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|,
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|control
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
argument_list|,
name|entry
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|control
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
name|control
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testEqualsNull ()
specifier|public
name|void
name|testEqualsNull
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|control
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
argument_list|,
name|entry
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|control
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
name|entry
argument_list|(
name|NE
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entry
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
name|control
argument_list|(
literal|"bar"
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entry
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|entry
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHashCode ()
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|control
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|entry
argument_list|(
literal|"foo"
argument_list|,
literal|1
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|control
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|entry
argument_list|(
literal|"bar"
argument_list|,
literal|2
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHashCodeNull ()
specifier|public
name|void
name|testHashCodeNull
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|control
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|,
name|entry
argument_list|(
name|NE
argument_list|,
literal|1
argument_list|)
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testNegativeCount ()
specifier|public
name|void
name|testNegativeCount
parameter_list|()
block|{
try|try
block|{
name|entry
argument_list|(
literal|"foo"
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

