begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc. All Rights Reserved.
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
name|checkState
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
name|Joiner
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
name|cache
operator|.
name|CacheBuilder
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
name|cache
operator|.
name|CacheLoader
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
name|cache
operator|.
name|LoadingCache
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
name|Maps
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
name|Sets
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
name|GenericArrayType
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|TypeVariable
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
name|WildcardType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * This class can be used by any generic super class to resolve one of its type  * parameter to the actual type argument used by the subclass, provided the type  * argument is carried on the class declaration.  *   * @author benyu@google.com (Jige Yu)  */
end_comment

begin_class
DECL|class|TypeResolver
class|class
name|TypeResolver
block|{
specifier|private
specifier|static
specifier|final
DECL|field|typeTableCache
name|LoadingCache
argument_list|<
name|Type
argument_list|,
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
argument_list|>
name|typeTableCache
init|=
name|CacheBuilder
operator|.
name|newBuilder
argument_list|()
operator|.
name|weakKeys
argument_list|()
operator|.
name|build
argument_list|(
operator|new
name|CacheLoader
argument_list|<
name|Type
argument_list|,
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|load
parameter_list|(
name|Type
name|contextType
parameter_list|)
block|{
return|return
name|TypeMappingIntrospector
operator|.
name|getTypeMappings
argument_list|(
name|contextType
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
DECL|method|getTypeTable (Type type)
specifier|private
specifier|static
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|getTypeTable
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
operator|||
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|typeTableCache
operator|.
name|getUnchecked
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
block|}
DECL|field|typeTable
specifier|private
specifier|final
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|typeTable
decl_stmt|;
DECL|method|accordingTo (Type type)
specifier|static
name|TypeResolver
name|accordingTo
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
return|return
operator|new
name|TypeResolver
argument_list|()
operator|.
name|where
argument_list|(
name|getTypeTable
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
DECL|method|TypeResolver ()
name|TypeResolver
parameter_list|()
block|{
name|this
operator|.
name|typeTable
operator|=
name|ImmutableMap
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
DECL|method|TypeResolver (ImmutableMap<TypeVariable<?>, Type> typeTable)
specifier|private
name|TypeResolver
parameter_list|(
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|typeTable
parameter_list|)
block|{
name|this
operator|.
name|typeTable
operator|=
name|typeTable
expr_stmt|;
block|}
comment|/** Returns a new {@code TypeResolver} with {@code variable} mapping to {@code type}. */
DECL|method|where (Map<? extends TypeVariable<?>, ? extends Type> mappings)
specifier|final
name|TypeResolver
name|where
parameter_list|(
name|Map
argument_list|<
name|?
extends|extends
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|?
extends|extends
name|Type
argument_list|>
name|mappings
parameter_list|)
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|builder
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|putAll
argument_list|(
name|typeTable
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|?
extends|extends
name|Type
argument_list|>
name|mapping
range|:
name|mappings
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|variable
init|=
name|mapping
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Type
name|type
init|=
name|mapping
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
operator|!
name|variable
operator|.
name|equals
argument_list|(
name|type
argument_list|)
argument_list|,
literal|"Type variable %s bound to itself"
argument_list|,
name|variable
argument_list|)
expr_stmt|;
name|builder
operator|.
name|put
argument_list|(
name|variable
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|TypeResolver
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns a new {@code TypeResolver} with type variables in {@code mapFrom} mapping to types in    * {@code type}. Either {@code mapFrom} is a type variable, or {@code mapFrom} and {@code mapTo}    * are both {@link ParameterizedType} of the same raw type, or {@link GenericArrayType} of the    * same component type. Caller needs to ensure this before calling.    */
DECL|method|where (Type mapFrom, Type mapTo)
specifier|final
name|TypeResolver
name|where
parameter_list|(
name|Type
name|mapFrom
parameter_list|,
name|Type
name|mapTo
parameter_list|)
block|{
name|Map
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|mappings
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
name|populateTypeMappings
argument_list|(
name|mappings
argument_list|,
name|mapFrom
argument_list|,
name|mapTo
argument_list|)
expr_stmt|;
return|return
name|where
argument_list|(
name|mappings
argument_list|)
return|;
block|}
DECL|method|populateTypeMappings ( Map<TypeVariable<?>, Type> mappings, Type from, Type to)
specifier|private
specifier|static
name|void
name|populateTypeMappings
parameter_list|(
name|Map
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|mappings
parameter_list|,
name|Type
name|from
parameter_list|,
name|Type
name|to
parameter_list|)
block|{
if|if
condition|(
name|from
operator|instanceof
name|TypeVariable
condition|)
block|{
name|mappings
operator|.
name|put
argument_list|(
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|from
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|from
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|populateTypeMappings
argument_list|(
name|mappings
argument_list|,
operator|(
operator|(
name|GenericArrayType
operator|)
name|from
operator|)
operator|.
name|getGenericComponentType
argument_list|()
argument_list|,
name|Types
operator|.
name|getComponentType
argument_list|(
name|to
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|from
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|Type
index|[]
name|fromArgs
init|=
operator|(
operator|(
name|ParameterizedType
operator|)
name|from
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|Type
index|[]
name|toArgs
init|=
operator|(
operator|(
name|ParameterizedType
operator|)
name|to
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|fromArgs
operator|.
name|length
operator|==
name|toArgs
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fromArgs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|populateTypeMappings
argument_list|(
name|mappings
argument_list|,
name|fromArgs
index|[
name|i
index|]
argument_list|,
name|toArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * Resolves all type variables in {@code type} and all downstream types and    * returns a corresponding type with type variables resolved.    */
DECL|method|resolve (Type type)
specifier|final
name|Type
name|resolve
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|TypeVariable
argument_list|<
name|?
argument_list|>
condition|)
block|{
return|return
name|resolveTypeVariable
argument_list|(
operator|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
operator|)
name|type
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
name|resolveParameterizedType
argument_list|(
operator|(
name|ParameterizedType
operator|)
name|type
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
return|return
name|resolveGenericArrayType
argument_list|(
operator|(
name|GenericArrayType
operator|)
name|type
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|WildcardType
condition|)
block|{
name|WildcardType
name|wildcardType
init|=
operator|(
name|WildcardType
operator|)
name|type
decl_stmt|;
return|return
operator|new
name|Types
operator|.
name|WildcardTypeImpl
argument_list|(
name|resolve
argument_list|(
name|wildcardType
operator|.
name|getLowerBounds
argument_list|()
argument_list|)
argument_list|,
name|resolve
argument_list|(
name|wildcardType
operator|.
name|getUpperBounds
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// if Class<?>, no resolution needed, we are done.
return|return
name|type
return|;
block|}
block|}
DECL|method|resolve (Type[] types)
specifier|private
name|Type
index|[]
name|resolve
parameter_list|(
name|Type
index|[]
name|types
parameter_list|)
block|{
name|Type
index|[]
name|result
init|=
operator|new
name|Type
index|[
name|types
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|types
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|result
index|[
name|i
index|]
operator|=
name|resolve
argument_list|(
name|types
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|resolveGenericArrayType (GenericArrayType type)
specifier|private
name|Type
name|resolveGenericArrayType
parameter_list|(
name|GenericArrayType
name|type
parameter_list|)
block|{
name|Type
name|componentType
init|=
name|resolve
argument_list|(
name|type
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Types
operator|.
name|newArrayType
argument_list|(
name|componentType
argument_list|)
return|;
block|}
DECL|method|resolveTypeVariable (final TypeVariable<?> var)
specifier|private
name|Type
name|resolveTypeVariable
parameter_list|(
specifier|final
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|var
parameter_list|)
block|{
specifier|final
name|TypeResolver
name|unguarded
init|=
name|this
decl_stmt|;
name|TypeResolver
name|guarded
init|=
operator|new
name|TypeResolver
argument_list|(
name|typeTable
argument_list|)
block|{
annotation|@
name|Override
name|Type
name|resolveTypeVariable
parameter_list|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|intermediateVar
parameter_list|,
name|TypeResolver
name|guardedResolver
parameter_list|)
block|{
if|if
condition|(
name|intermediateVar
operator|.
name|getGenericDeclaration
argument_list|()
operator|.
name|equals
argument_list|(
name|var
operator|.
name|getGenericDeclaration
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|intermediateVar
return|;
block|}
return|return
name|unguarded
operator|.
name|resolveTypeVariable
argument_list|(
name|intermediateVar
argument_list|,
name|guardedResolver
argument_list|)
return|;
block|}
block|}
decl_stmt|;
return|return
name|resolveTypeVariable
argument_list|(
name|var
argument_list|,
name|guarded
argument_list|)
return|;
block|}
comment|/**    * Resolves {@code var} using the encapsulated type mapping. If it maps to yet another    * non-reified type, {@code guardedResolver} is used to do further resolution, which doesn't try    * to resolve any type variable on generic declarations that are already being resolved.    */
DECL|method|resolveTypeVariable (TypeVariable<?> var, TypeResolver guardedResolver)
name|Type
name|resolveTypeVariable
parameter_list|(
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|var
parameter_list|,
name|TypeResolver
name|guardedResolver
parameter_list|)
block|{
name|Type
name|type
init|=
name|typeTable
operator|.
name|get
argument_list|(
name|var
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|Type
index|[]
name|bounds
init|=
name|var
operator|.
name|getBounds
argument_list|()
decl_stmt|;
if|if
condition|(
name|bounds
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|var
return|;
block|}
return|return
name|Types
operator|.
name|newTypeVariable
argument_list|(
name|var
operator|.
name|getGenericDeclaration
argument_list|()
argument_list|,
name|var
operator|.
name|getName
argument_list|()
argument_list|,
name|guardedResolver
operator|.
name|resolve
argument_list|(
name|bounds
argument_list|)
argument_list|)
return|;
block|}
return|return
name|guardedResolver
operator|.
name|resolve
argument_list|(
name|type
argument_list|)
return|;
comment|// in case the type is yet another type variable.
block|}
DECL|method|resolveParameterizedType (ParameterizedType type)
specifier|private
name|ParameterizedType
name|resolveParameterizedType
parameter_list|(
name|ParameterizedType
name|type
parameter_list|)
block|{
name|Type
name|owner
init|=
name|type
operator|.
name|getOwnerType
argument_list|()
decl_stmt|;
name|Type
name|resolvedOwner
init|=
operator|(
name|owner
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|resolve
argument_list|(
name|owner
argument_list|)
decl_stmt|;
name|Type
name|resolvedRawType
init|=
name|resolve
argument_list|(
name|type
operator|.
name|getRawType
argument_list|()
argument_list|)
decl_stmt|;
name|Type
index|[]
name|vars
init|=
name|type
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|Type
index|[]
name|resolvedArgs
init|=
operator|new
name|Type
index|[
name|vars
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|vars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|resolvedArgs
index|[
name|i
index|]
operator|=
name|resolve
argument_list|(
name|vars
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
name|resolvedOwner
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|resolvedRawType
argument_list|,
name|resolvedArgs
argument_list|)
return|;
block|}
DECL|class|TypeMappingIntrospector
specifier|private
specifier|static
specifier|final
class|class
name|TypeMappingIntrospector
block|{
DECL|field|wildcardCapturer
specifier|private
specifier|static
specifier|final
name|WildcardCapturer
name|wildcardCapturer
init|=
operator|new
name|WildcardCapturer
argument_list|()
decl_stmt|;
DECL|field|mappings
specifier|private
specifier|final
name|Map
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|mappings
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
DECL|field|introspectedTypes
specifier|private
specifier|final
name|Set
argument_list|<
name|Type
argument_list|>
name|introspectedTypes
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
comment|/**      * Returns type mappings using type parameters and type arguments found in      * the generic superclass and the super interfaces of {@code contextClass}.      */
DECL|method|getTypeMappings ( Type contextType)
specifier|static
name|ImmutableMap
argument_list|<
name|TypeVariable
argument_list|<
name|?
argument_list|>
argument_list|,
name|Type
argument_list|>
name|getTypeMappings
parameter_list|(
name|Type
name|contextType
parameter_list|)
block|{
name|TypeMappingIntrospector
name|introspector
init|=
operator|new
name|TypeMappingIntrospector
argument_list|()
decl_stmt|;
name|introspector
operator|.
name|introspect
argument_list|(
name|wildcardCapturer
operator|.
name|capture
argument_list|(
name|contextType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|introspector
operator|.
name|mappings
argument_list|)
return|;
block|}
DECL|method|introspect (Type type)
specifier|private
name|void
name|introspect
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
operator|!
name|introspectedTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|introspectParameterizedType
argument_list|(
operator|(
name|ParameterizedType
operator|)
name|type
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|introspectClass
argument_list|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|introspectClass (Class<?> clazz)
specifier|private
name|void
name|introspectClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
name|introspect
argument_list|(
name|clazz
operator|.
name|getGenericSuperclass
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Type
name|interfaceType
range|:
name|clazz
operator|.
name|getGenericInterfaces
argument_list|()
control|)
block|{
name|introspect
argument_list|(
name|interfaceType
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|introspectParameterizedType ( ParameterizedType parameterizedType)
specifier|private
name|void
name|introspectParameterizedType
parameter_list|(
name|ParameterizedType
name|parameterizedType
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|rawClass
init|=
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|parameterizedType
operator|.
name|getRawType
argument_list|()
decl_stmt|;
name|TypeVariable
argument_list|<
name|?
argument_list|>
index|[]
name|vars
init|=
name|rawClass
operator|.
name|getTypeParameters
argument_list|()
decl_stmt|;
name|Type
index|[]
name|typeArgs
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|vars
operator|.
name|length
operator|==
name|typeArgs
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|vars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|map
argument_list|(
name|vars
index|[
name|i
index|]
argument_list|,
name|typeArgs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|introspectClass
argument_list|(
name|rawClass
argument_list|)
expr_stmt|;
name|introspect
argument_list|(
name|parameterizedType
operator|.
name|getOwnerType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|map (final TypeVariable<?> var, final Type arg)
specifier|private
name|void
name|map
parameter_list|(
specifier|final
name|TypeVariable
argument_list|<
name|?
argument_list|>
name|var
parameter_list|,
specifier|final
name|Type
name|arg
parameter_list|)
block|{
if|if
condition|(
name|mappings
operator|.
name|containsKey
argument_list|(
name|var
argument_list|)
condition|)
block|{
comment|// Mapping already established
comment|// This is possible when following both superClass -> enclosingClass
comment|// and enclosingclass -> superClass paths.
comment|// Since we follow the path of superclass first, enclosing second,
comment|// superclass mapping should take precedence.
return|return;
block|}
comment|// First, check whether var -> arg forms a cycle
for|for
control|(
name|Type
name|t
init|=
name|arg
init|;
name|t
operator|!=
literal|null
condition|;
name|t
operator|=
name|mappings
operator|.
name|get
argument_list|(
name|t
argument_list|)
control|)
block|{
if|if
condition|(
name|var
operator|.
name|equals
argument_list|(
name|t
argument_list|)
condition|)
block|{
comment|// cycle detected, remove the entire cycle from the mapping so that
comment|// each type variable resolves deterministically to itself.
comment|// Otherwise, a F -> T cycle will end up resolving both F and T
comment|// nondeterministically to either F or T.
for|for
control|(
name|Type
name|x
init|=
name|arg
init|;
name|x
operator|!=
literal|null
condition|;
name|x
operator|=
name|mappings
operator|.
name|remove
argument_list|(
name|x
argument_list|)
control|)
block|{}
return|return;
block|}
block|}
name|mappings
operator|.
name|put
argument_list|(
name|var
argument_list|,
name|arg
argument_list|)
expr_stmt|;
block|}
block|}
comment|// This is needed when resolving types against a context with wildcards
comment|// For example:
comment|// class Holder<T> {
comment|//   void set(T data) {...}
comment|// }
comment|// Holder<List<?>> should *not* resolve the set() method to set(List<?> data).
comment|// Instead, it should create a capture of the wildcard so that set() rejects any List<T>.
DECL|class|WildcardCapturer
specifier|private
specifier|static
specifier|final
class|class
name|WildcardCapturer
block|{
DECL|field|id
specifier|private
specifier|final
name|AtomicInteger
name|id
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|capture (Type type)
name|Type
name|capture
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|type
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
return|return
name|type
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|TypeVariable
condition|)
block|{
return|return
name|type
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|GenericArrayType
condition|)
block|{
name|GenericArrayType
name|arrayType
init|=
operator|(
name|GenericArrayType
operator|)
name|type
decl_stmt|;
return|return
name|Types
operator|.
name|newArrayType
argument_list|(
name|capture
argument_list|(
name|arrayType
operator|.
name|getGenericComponentType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
return|return
name|Types
operator|.
name|newParameterizedTypeWithOwner
argument_list|(
name|captureNullable
argument_list|(
name|parameterizedType
operator|.
name|getOwnerType
argument_list|()
argument_list|)
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|parameterizedType
operator|.
name|getRawType
argument_list|()
argument_list|,
name|capture
argument_list|(
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
if|if
condition|(
name|type
operator|instanceof
name|WildcardType
condition|)
block|{
name|WildcardType
name|wildcardType
init|=
operator|(
name|WildcardType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|lowerBounds
init|=
name|wildcardType
operator|.
name|getLowerBounds
argument_list|()
decl_stmt|;
if|if
condition|(
name|lowerBounds
operator|.
name|length
operator|==
literal|0
condition|)
block|{
comment|// ? extends something changes to capture-of
name|Type
index|[]
name|upperBounds
init|=
name|wildcardType
operator|.
name|getUpperBounds
argument_list|()
decl_stmt|;
name|String
name|name
init|=
literal|"capture#"
operator|+
name|id
operator|.
name|incrementAndGet
argument_list|()
operator|+
literal|"-of ? extends "
operator|+
name|Joiner
operator|.
name|on
argument_list|(
literal|'&'
argument_list|)
operator|.
name|join
argument_list|(
name|upperBounds
argument_list|)
decl_stmt|;
return|return
name|Types
operator|.
name|newTypeVariable
argument_list|(
name|WildcardCapturer
operator|.
name|class
argument_list|,
name|name
argument_list|,
name|wildcardType
operator|.
name|getUpperBounds
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
comment|// TODO(benyu): handle ? super T somehow.
return|return
name|type
return|;
block|}
block|}
throw|throw
operator|new
name|AssertionError
argument_list|(
literal|"must have been one of the known types"
argument_list|)
throw|;
block|}
DECL|method|captureNullable (@ullable Type type)
specifier|private
name|Type
name|captureNullable
parameter_list|(
annotation|@
name|Nullable
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|capture
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|capture (Type[] types)
specifier|private
name|Type
index|[]
name|capture
parameter_list|(
name|Type
index|[]
name|types
parameter_list|)
block|{
name|Type
index|[]
name|result
init|=
operator|new
name|Type
index|[
name|types
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|types
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|result
index|[
name|i
index|]
operator|=
name|capture
argument_list|(
name|types
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

