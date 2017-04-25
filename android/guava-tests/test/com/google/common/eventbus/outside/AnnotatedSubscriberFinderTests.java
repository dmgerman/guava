begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.google.common.eventbus.outside
package|package
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
operator|.
name|outside
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|eventbus
operator|.
name|EventBus
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
name|eventbus
operator|.
name|Subscribe
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

begin_comment
comment|/**  * Test that EventBus finds the correct subscribers.  *  * This test must be outside the c.g.c.eventbus package to test correctly.  *  * @author Louis Wasserman  */
end_comment

begin_class
DECL|class|AnnotatedSubscriberFinderTests
specifier|public
class|class
name|AnnotatedSubscriberFinderTests
block|{
DECL|field|EVENT
specifier|private
specifier|static
specifier|final
name|Object
name|EVENT
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|class|AbstractEventBusTest
specifier|abstract
specifier|static
class|class
name|AbstractEventBusTest
parameter_list|<
name|H
parameter_list|>
extends|extends
name|TestCase
block|{
DECL|method|createSubscriber ()
specifier|abstract
name|H
name|createSubscriber
parameter_list|()
function_decl|;
DECL|field|subscriber
specifier|private
name|H
name|subscriber
decl_stmt|;
DECL|method|getSubscriber ()
name|H
name|getSubscriber
parameter_list|()
block|{
return|return
name|subscriber
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|subscriber
operator|=
name|createSubscriber
argument_list|()
expr_stmt|;
name|EventBus
name|bus
init|=
operator|new
name|EventBus
argument_list|()
decl_stmt|;
name|bus
operator|.
name|register
argument_list|(
name|subscriber
argument_list|)
expr_stmt|;
name|bus
operator|.
name|post
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|subscriber
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/*    * We break the tests up based on whether they are annotated or abstract in the superclass.    */
DECL|class|BaseSubscriberFinderTest
specifier|public
specifier|static
class|class
name|BaseSubscriberFinderTest
extends|extends
name|AbstractEventBusTest
argument_list|<
name|BaseSubscriberFinderTest
operator|.
name|Subscriber
argument_list|>
block|{
DECL|class|Subscriber
specifier|static
class|class
name|Subscriber
block|{
DECL|field|nonSubscriberEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|nonSubscriberEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|subscriberEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|subscriberEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|method|notASubscriber (Object o)
specifier|public
name|void
name|notASubscriber
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|nonSubscriberEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
DECL|method|subscriber (Object o)
specifier|public
name|void
name|subscriber
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|subscriberEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNonSubscriber ()
specifier|public
name|void
name|testNonSubscriber
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|nonSubscriberEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testSubscriber ()
specifier|public
name|void
name|testSubscriber
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|subscriberEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
name|Subscriber
name|createSubscriber
parameter_list|()
block|{
return|return
operator|new
name|Subscriber
argument_list|()
return|;
block|}
block|}
DECL|class|AnnotatedAndAbstractInSuperclassTest
specifier|public
specifier|static
class|class
name|AnnotatedAndAbstractInSuperclassTest
extends|extends
name|AbstractEventBusTest
argument_list|<
name|AnnotatedAndAbstractInSuperclassTest
operator|.
name|SubClass
argument_list|>
block|{
DECL|class|SuperClass
specifier|abstract
specifier|static
class|class
name|SuperClass
block|{
annotation|@
name|Subscribe
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
specifier|abstract
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
annotation|@
name|Subscribe
DECL|method|overriddenInSubclass (Object o)
specifier|public
specifier|abstract
name|void
name|overriddenInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
block|}
DECL|class|SubClass
specifier|static
class|class
name|SubClass
extends|extends
name|SuperClass
block|{
DECL|field|overriddenAndAnnotatedInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenAndAnnotatedInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|overriddenInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenAndAnnotatedInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|overriddenInSubclass (Object o)
specifier|public
name|void
name|overriddenInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testOverriddenAndAnnotatedInSubclass ()
specifier|public
name|void
name|testOverriddenAndAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenAndAnnotatedInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testOverriddenNotAnnotatedInSubclass ()
specifier|public
name|void
name|testOverriddenNotAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
name|SubClass
name|createSubscriber
parameter_list|()
block|{
return|return
operator|new
name|SubClass
argument_list|()
return|;
block|}
block|}
DECL|class|AnnotatedNotAbstractInSuperclassTest
specifier|public
specifier|static
class|class
name|AnnotatedNotAbstractInSuperclassTest
extends|extends
name|AbstractEventBusTest
argument_list|<
name|AnnotatedNotAbstractInSuperclassTest
operator|.
name|SubClass
argument_list|>
block|{
DECL|class|SuperClass
specifier|static
class|class
name|SuperClass
block|{
DECL|field|notOverriddenInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|notOverriddenInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|overriddenNotAnnotatedInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenNotAnnotatedInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|overriddenAndAnnotatedInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenAndAnnotatedInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|differentlyOverriddenNotAnnotatedInSubclassBadEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|differentlyOverriddenNotAnnotatedInSubclassBadEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|differentlyOverriddenAnnotatedInSubclassBadEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|differentlyOverriddenAnnotatedInSubclassBadEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Subscribe
DECL|method|notOverriddenInSubclass (Object o)
specifier|public
name|void
name|notOverriddenInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|notOverriddenInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
DECL|method|overriddenNotAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenNotAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenNotAnnotatedInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenAndAnnotatedInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
DECL|method|differentlyOverriddenNotAnnotatedInSubclass (Object o)
specifier|public
name|void
name|differentlyOverriddenNotAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
comment|// the subclass overrides this and does *not* call super.dONAIS(o)
name|differentlyOverriddenNotAnnotatedInSubclassBadEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
DECL|method|differentlyOverriddenAnnotatedInSubclass (Object o)
specifier|public
name|void
name|differentlyOverriddenAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
comment|// the subclass overrides this and does *not* call super.dOAIS(o)
name|differentlyOverriddenAnnotatedInSubclassBadEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|SubClass
specifier|static
class|class
name|SubClass
extends|extends
name|SuperClass
block|{
DECL|field|differentlyOverriddenNotAnnotatedInSubclassGoodEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|differentlyOverriddenNotAnnotatedInSubclassGoodEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|differentlyOverriddenAnnotatedInSubclassGoodEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|differentlyOverriddenAnnotatedInSubclassGoodEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|overriddenNotAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenNotAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|super
operator|.
name|overriddenNotAnnotatedInSubclass
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|super
operator|.
name|overriddenAndAnnotatedInSubclass
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|differentlyOverriddenNotAnnotatedInSubclass (Object o)
specifier|public
name|void
name|differentlyOverriddenNotAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|differentlyOverriddenNotAnnotatedInSubclassGoodEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|differentlyOverriddenAnnotatedInSubclass (Object o)
specifier|public
name|void
name|differentlyOverriddenAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|differentlyOverriddenAnnotatedInSubclassGoodEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNotOverriddenInSubclass ()
specifier|public
name|void
name|testNotOverriddenInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|notOverriddenInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testOverriddenNotAnnotatedInSubclass ()
specifier|public
name|void
name|testOverriddenNotAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenNotAnnotatedInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferentlyOverriddenNotAnnotatedInSubclass ()
specifier|public
name|void
name|testDifferentlyOverriddenNotAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|differentlyOverriddenNotAnnotatedInSubclassGoodEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|differentlyOverriddenNotAnnotatedInSubclassBadEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testOverriddenAndAnnotatedInSubclass ()
specifier|public
name|void
name|testOverriddenAndAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenAndAnnotatedInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testDifferentlyOverriddenAndAnnotatedInSubclass ()
specifier|public
name|void
name|testDifferentlyOverriddenAndAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|differentlyOverriddenAnnotatedInSubclassGoodEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|differentlyOverriddenAnnotatedInSubclassBadEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
name|SubClass
name|createSubscriber
parameter_list|()
block|{
return|return
operator|new
name|SubClass
argument_list|()
return|;
block|}
block|}
DECL|class|AbstractNotAnnotatedInSuperclassTest
specifier|public
specifier|static
class|class
name|AbstractNotAnnotatedInSuperclassTest
extends|extends
name|AbstractEventBusTest
argument_list|<
name|AbstractNotAnnotatedInSuperclassTest
operator|.
name|SubClass
argument_list|>
block|{
DECL|class|SuperClass
specifier|abstract
specifier|static
class|class
name|SuperClass
block|{
DECL|method|overriddenInSubclassNowhereAnnotated (Object o)
specifier|public
specifier|abstract
name|void
name|overriddenInSubclassNowhereAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
specifier|abstract
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
block|}
DECL|class|SubClass
specifier|static
class|class
name|SubClass
extends|extends
name|SuperClass
block|{
DECL|field|overriddenInSubclassNowhereAnnotatedEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenInSubclassNowhereAnnotatedEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|overriddenAndAnnotatedInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenAndAnnotatedInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|overriddenInSubclassNowhereAnnotated (Object o)
specifier|public
name|void
name|overriddenInSubclassNowhereAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenInSubclassNowhereAnnotatedEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenAndAnnotatedInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testOverriddenAndAnnotatedInSubclass ()
specifier|public
name|void
name|testOverriddenAndAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenAndAnnotatedInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testOverriddenInSubclassNowhereAnnotated ()
specifier|public
name|void
name|testOverriddenInSubclassNowhereAnnotated
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenInSubclassNowhereAnnotatedEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
name|SubClass
name|createSubscriber
parameter_list|()
block|{
return|return
operator|new
name|SubClass
argument_list|()
return|;
block|}
block|}
DECL|class|NeitherAbstractNorAnnotatedInSuperclassTest
specifier|public
specifier|static
class|class
name|NeitherAbstractNorAnnotatedInSuperclassTest
extends|extends
name|AbstractEventBusTest
argument_list|<
name|NeitherAbstractNorAnnotatedInSuperclassTest
operator|.
name|SubClass
argument_list|>
block|{
DECL|class|SuperClass
specifier|static
class|class
name|SuperClass
block|{
DECL|field|neitherOverriddenNorAnnotatedEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|neitherOverriddenNorAnnotatedEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|overriddenInSubclassNowhereAnnotatedEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenInSubclassNowhereAnnotatedEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|overriddenAndAnnotatedInSubclassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|overriddenAndAnnotatedInSubclassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|method|neitherOverriddenNorAnnotated (Object o)
specifier|public
name|void
name|neitherOverriddenNorAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|neitherOverriddenNorAnnotatedEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
DECL|method|overriddenInSubclassNowhereAnnotated (Object o)
specifier|public
name|void
name|overriddenInSubclassNowhereAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenInSubclassNowhereAnnotatedEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|overriddenAndAnnotatedInSubclassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|SubClass
specifier|static
class|class
name|SubClass
extends|extends
name|SuperClass
block|{
annotation|@
name|Override
DECL|method|overriddenInSubclassNowhereAnnotated (Object o)
specifier|public
name|void
name|overriddenInSubclassNowhereAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|super
operator|.
name|overriddenInSubclassNowhereAnnotated
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|overriddenAndAnnotatedInSubclass (Object o)
specifier|public
name|void
name|overriddenAndAnnotatedInSubclass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|super
operator|.
name|overriddenAndAnnotatedInSubclass
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testNeitherOverriddenNorAnnotated ()
specifier|public
name|void
name|testNeitherOverriddenNorAnnotated
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|neitherOverriddenNorAnnotatedEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testOverriddenInSubclassNowhereAnnotated ()
specifier|public
name|void
name|testOverriddenInSubclassNowhereAnnotated
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenInSubclassNowhereAnnotatedEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
DECL|method|testOverriddenAndAnnotatedInSubclass ()
specifier|public
name|void
name|testOverriddenAndAnnotatedInSubclass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|overriddenAndAnnotatedInSubclassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
name|SubClass
name|createSubscriber
parameter_list|()
block|{
return|return
operator|new
name|SubClass
argument_list|()
return|;
block|}
block|}
DECL|class|DeepInterfaceTest
specifier|public
specifier|static
class|class
name|DeepInterfaceTest
extends|extends
name|AbstractEventBusTest
argument_list|<
name|DeepInterfaceTest
operator|.
name|SubscriberClass
argument_list|>
block|{
DECL|interface|Interface1
interface|interface
name|Interface1
block|{
annotation|@
name|Subscribe
DECL|method|annotatedIn1 (Object o)
name|void
name|annotatedIn1
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
annotation|@
name|Subscribe
DECL|method|annotatedIn1And2 (Object o)
name|void
name|annotatedIn1And2
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
annotation|@
name|Subscribe
DECL|method|annotatedIn1And2AndClass (Object o)
name|void
name|annotatedIn1And2AndClass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
DECL|method|declaredIn1AnnotatedIn2 (Object o)
name|void
name|declaredIn1AnnotatedIn2
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
DECL|method|declaredIn1AnnotatedInClass (Object o)
name|void
name|declaredIn1AnnotatedInClass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
DECL|method|nowhereAnnotated (Object o)
name|void
name|nowhereAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
block|}
DECL|interface|Interface2
interface|interface
name|Interface2
extends|extends
name|Interface1
block|{
annotation|@
name|Override
annotation|@
name|Subscribe
DECL|method|declaredIn1AnnotatedIn2 (Object o)
name|void
name|declaredIn1AnnotatedIn2
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
annotation|@
name|Override
annotation|@
name|Subscribe
DECL|method|annotatedIn1And2 (Object o)
name|void
name|annotatedIn1And2
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
annotation|@
name|Override
annotation|@
name|Subscribe
DECL|method|annotatedIn1And2AndClass (Object o)
name|void
name|annotatedIn1And2AndClass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
DECL|method|declaredIn2AnnotatedInClass (Object o)
name|void
name|declaredIn2AnnotatedInClass
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
annotation|@
name|Subscribe
DECL|method|annotatedIn2 (Object o)
name|void
name|annotatedIn2
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
block|}
DECL|class|SubscriberClass
specifier|static
class|class
name|SubscriberClass
implements|implements
name|Interface2
block|{
DECL|field|annotatedIn1Events
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|annotatedIn1Events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|annotatedIn1And2Events
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|annotatedIn1And2Events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|annotatedIn1And2AndClassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|annotatedIn1And2AndClassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|declaredIn1AnnotatedIn2Events
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|declaredIn1AnnotatedIn2Events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|declaredIn1AnnotatedInClassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|declaredIn1AnnotatedInClassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|declaredIn2AnnotatedInClassEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|declaredIn2AnnotatedInClassEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|annotatedIn2Events
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|annotatedIn2Events
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
DECL|field|nowhereAnnotatedEvents
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|nowhereAnnotatedEvents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|annotatedIn1 (Object o)
specifier|public
name|void
name|annotatedIn1
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|annotatedIn1Events
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|declaredIn1AnnotatedInClass (Object o)
specifier|public
name|void
name|declaredIn1AnnotatedInClass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|declaredIn1AnnotatedInClassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|declaredIn1AnnotatedIn2 (Object o)
specifier|public
name|void
name|declaredIn1AnnotatedIn2
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|declaredIn1AnnotatedIn2Events
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|annotatedIn1And2 (Object o)
specifier|public
name|void
name|annotatedIn1And2
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|annotatedIn1And2Events
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|annotatedIn1And2AndClass (Object o)
specifier|public
name|void
name|annotatedIn1And2AndClass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|annotatedIn1And2AndClassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Subscribe
annotation|@
name|Override
DECL|method|declaredIn2AnnotatedInClass (Object o)
specifier|public
name|void
name|declaredIn2AnnotatedInClass
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|declaredIn2AnnotatedInClassEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|annotatedIn2 (Object o)
specifier|public
name|void
name|annotatedIn2
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|annotatedIn2Events
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|nowhereAnnotated (Object o)
specifier|public
name|void
name|nowhereAnnotated
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|nowhereAnnotatedEvents
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testAnnotatedIn1 ()
specifier|public
name|void
name|testAnnotatedIn1
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|annotatedIn1Events
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testAnnotatedIn2 ()
specifier|public
name|void
name|testAnnotatedIn2
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|annotatedIn2Events
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testAnnotatedIn1And2 ()
specifier|public
name|void
name|testAnnotatedIn1And2
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|annotatedIn1And2Events
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testAnnotatedIn1And2AndClass ()
specifier|public
name|void
name|testAnnotatedIn1And2AndClass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|annotatedIn1And2AndClassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testDeclaredIn1AnnotatedIn2 ()
specifier|public
name|void
name|testDeclaredIn1AnnotatedIn2
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|declaredIn1AnnotatedIn2Events
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testDeclaredIn1AnnotatedInClass ()
specifier|public
name|void
name|testDeclaredIn1AnnotatedInClass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|declaredIn1AnnotatedInClassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testDeclaredIn2AnnotatedInClass ()
specifier|public
name|void
name|testDeclaredIn2AnnotatedInClass
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|declaredIn2AnnotatedInClassEvents
argument_list|)
operator|.
name|contains
argument_list|(
name|EVENT
argument_list|)
expr_stmt|;
block|}
DECL|method|testNowhereAnnotated ()
specifier|public
name|void
name|testNowhereAnnotated
parameter_list|()
block|{
name|assertThat
argument_list|(
name|getSubscriber
argument_list|()
operator|.
name|nowhereAnnotatedEvents
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createSubscriber ()
name|SubscriberClass
name|createSubscriber
parameter_list|()
block|{
return|return
operator|new
name|SubscriberClass
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
