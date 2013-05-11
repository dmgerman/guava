begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (C) 2012 The Guava Authors  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Splitter
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
name|FluentIterable
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
name|ImmutableSet
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
name|ImmutableSortedSet
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
name|Ordering
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
comment|/**  * Scans the source of a {@link ClassLoader} and finds all loadable classes and resources.  *  * @author Ben Yu  * @since 14.0  */
end_comment

begin_class
annotation|@
name|Beta
DECL|class|ClassPath
specifier|public
specifier|final
class|class
name|ClassPath
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|Logger
operator|.
name|getLogger
argument_list|(
name|ClassPath
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|/** Separator for the Class-Path manifest attribute value in jar files. */
DECL|field|CLASS_PATH_ATTRIBUTE_SEPARATOR
specifier|private
specifier|static
specifier|final
name|Splitter
name|CLASS_PATH_ATTRIBUTE_SEPARATOR
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|" "
argument_list|)
operator|.
name|omitEmptyStrings
argument_list|()
decl_stmt|;
DECL|field|CLASS_FILE_NAME_EXTENSION
specifier|private
specifier|static
specifier|final
name|String
name|CLASS_FILE_NAME_EXTENSION
init|=
literal|".class"
decl_stmt|;
DECL|field|resources
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|ResourceInfo
argument_list|>
name|resources
decl_stmt|;
DECL|method|ClassPath (ImmutableSet<ResourceInfo> resources)
specifier|private
name|ClassPath
parameter_list|(
name|ImmutableSet
argument_list|<
name|ResourceInfo
argument_list|>
name|resources
parameter_list|)
block|{
name|this
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
block|}
comment|/**    * Returns a {@code ClassPath} representing all classes and resources loadable from {@code    * classloader} and its parent class loaders.    *    *<p>Currently only {@link URLClassLoader} and only {@code file://} urls are supported.    *    * @throws IOException if the attempt to read class path resources (jar files or directories)    *         failed.    */
DECL|method|from (ClassLoader classloader)
specifier|public
specifier|static
name|ClassPath
name|from
parameter_list|(
name|ClassLoader
name|classloader
parameter_list|)
throws|throws
name|IOException
block|{
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|URI
argument_list|,
name|ClassLoader
argument_list|>
name|entry
range|:
name|getClassPathEntries
argument_list|(
name|classloader
argument_list|)
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|scanner
operator|.
name|scan
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ClassPath
argument_list|(
name|scanner
operator|.
name|getResources
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns all resources loadable from the current class path, including the class files of all    * loadable classes but excluding the "META-INF/MANIFEST.MF" file.    */
DECL|method|getResources ()
specifier|public
name|ImmutableSet
argument_list|<
name|ResourceInfo
argument_list|>
name|getResources
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
comment|/** Returns all top level classes loadable from the current class path. */
DECL|method|getTopLevelClasses ()
specifier|public
name|ImmutableSet
argument_list|<
name|ClassInfo
argument_list|>
name|getTopLevelClasses
parameter_list|()
block|{
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|resources
argument_list|)
operator|.
name|filter
argument_list|(
name|ClassInfo
operator|.
name|class
argument_list|)
operator|.
name|toSet
argument_list|()
return|;
block|}
comment|/** Returns all top level classes whose package name is {@code packageName}. */
DECL|method|getTopLevelClasses (String packageName)
specifier|public
name|ImmutableSet
argument_list|<
name|ClassInfo
argument_list|>
name|getTopLevelClasses
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|ClassInfo
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassInfo
name|classInfo
range|:
name|getTopLevelClasses
argument_list|()
control|)
block|{
if|if
condition|(
name|classInfo
operator|.
name|getPackageName
argument_list|()
operator|.
name|equals
argument_list|(
name|packageName
argument_list|)
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|classInfo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Returns all top level classes whose package name is {@code packageName} or starts with    * {@code packageName} followed by a '.'.    */
DECL|method|getTopLevelClassesRecursive (String packageName)
specifier|public
name|ImmutableSet
argument_list|<
name|ClassInfo
argument_list|>
name|getTopLevelClassesRecursive
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
name|String
name|packagePrefix
init|=
name|packageName
operator|+
literal|'.'
decl_stmt|;
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|ClassInfo
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|ClassInfo
name|classInfo
range|:
name|getTopLevelClasses
argument_list|()
control|)
block|{
if|if
condition|(
name|classInfo
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|packagePrefix
argument_list|)
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|classInfo
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**    * Represents a class path resource that can be either a class file or any other resource file    * loadable from the class path.    *    * @since 14.0    */
annotation|@
name|Beta
DECL|class|ResourceInfo
specifier|public
specifier|static
class|class
name|ResourceInfo
block|{
DECL|field|resourceName
specifier|private
specifier|final
name|String
name|resourceName
decl_stmt|;
DECL|field|loader
specifier|final
name|ClassLoader
name|loader
decl_stmt|;
DECL|method|of (String resourceName, ClassLoader loader)
specifier|static
name|ResourceInfo
name|of
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
if|if
condition|(
name|resourceName
operator|.
name|endsWith
argument_list|(
name|CLASS_FILE_NAME_EXTENSION
argument_list|)
operator|&&
operator|!
name|resourceName
operator|.
name|contains
argument_list|(
literal|"$"
argument_list|)
condition|)
block|{
return|return
operator|new
name|ClassInfo
argument_list|(
name|resourceName
argument_list|,
name|loader
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ResourceInfo
argument_list|(
name|resourceName
argument_list|,
name|loader
argument_list|)
return|;
block|}
block|}
DECL|method|ResourceInfo (String resourceName, ClassLoader loader)
name|ResourceInfo
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|this
operator|.
name|resourceName
operator|=
name|checkNotNull
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
name|this
operator|.
name|loader
operator|=
name|checkNotNull
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
comment|/** Returns the url identifying the resource. */
DECL|method|url ()
specifier|public
specifier|final
name|URL
name|url
parameter_list|()
block|{
return|return
name|checkNotNull
argument_list|(
name|loader
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
argument_list|,
literal|"Failed to load resource: %s"
argument_list|,
name|resourceName
argument_list|)
return|;
block|}
comment|/** Returns the fully qualified name of the resource. Such as "com/mycomp/foo/bar.txt". */
DECL|method|getResourceName ()
specifier|public
specifier|final
name|String
name|getResourceName
parameter_list|()
block|{
return|return
name|resourceName
return|;
block|}
DECL|method|hashCode ()
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|resourceName
operator|.
name|hashCode
argument_list|()
return|;
block|}
DECL|method|equals (Object obj)
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|ResourceInfo
condition|)
block|{
name|ResourceInfo
name|that
init|=
operator|(
name|ResourceInfo
operator|)
name|obj
decl_stmt|;
return|return
name|resourceName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|resourceName
argument_list|)
operator|&&
name|loader
operator|==
name|that
operator|.
name|loader
return|;
block|}
return|return
literal|false
return|;
block|}
comment|// Do not change this arbitrarily. We rely on it for sorting ResourceInfo.
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|resourceName
return|;
block|}
block|}
comment|/**    * Represents a class that can be loaded through {@link #load}.    *    * @since 14.0    */
annotation|@
name|Beta
DECL|class|ClassInfo
specifier|public
specifier|static
specifier|final
class|class
name|ClassInfo
extends|extends
name|ResourceInfo
block|{
DECL|field|className
specifier|private
specifier|final
name|String
name|className
decl_stmt|;
DECL|method|ClassInfo (String resourceName, ClassLoader loader)
name|ClassInfo
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|ClassLoader
name|loader
parameter_list|)
block|{
name|super
argument_list|(
name|resourceName
argument_list|,
name|loader
argument_list|)
expr_stmt|;
name|this
operator|.
name|className
operator|=
name|getClassName
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
comment|/** Returns the package name of the class, without attempting to load the class. */
DECL|method|getPackageName ()
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|Reflection
operator|.
name|getPackageName
argument_list|(
name|className
argument_list|)
return|;
block|}
comment|/** Returns the simple name of the underlying class as given in the source code. */
DECL|method|getSimpleName ()
specifier|public
name|String
name|getSimpleName
parameter_list|()
block|{
name|String
name|packageName
init|=
name|getPackageName
argument_list|()
decl_stmt|;
if|if
condition|(
name|packageName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|className
return|;
block|}
comment|// Since this is a top level class, its simple name is always the part after package name.
return|return
name|className
operator|.
name|substring
argument_list|(
name|packageName
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/** Returns the fully qualified name of the class. */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|className
return|;
block|}
comment|/**      * Loads (but doesn't link or initialize) the class.      *      * @throws LinkageError when there were errors in loading classes that this class depends on.      *         For example, {@link NoClassDefFoundError}.      */
DECL|method|load ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|load
parameter_list|()
block|{
try|try
block|{
return|return
name|loader
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// Shouldn't happen, since the class name is read from the class path.
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|toString ()
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|className
return|;
block|}
block|}
DECL|method|getClassPathEntries ( ClassLoader classloader)
annotation|@
name|VisibleForTesting
specifier|static
name|ImmutableMap
argument_list|<
name|URI
argument_list|,
name|ClassLoader
argument_list|>
name|getClassPathEntries
parameter_list|(
name|ClassLoader
name|classloader
parameter_list|)
block|{
name|LinkedHashMap
argument_list|<
name|URI
argument_list|,
name|ClassLoader
argument_list|>
name|entries
init|=
name|Maps
operator|.
name|newLinkedHashMap
argument_list|()
decl_stmt|;
comment|// Search parent first, since it's the order ClassLoader#loadClass() uses.
name|ClassLoader
name|parent
init|=
name|classloader
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|entries
operator|.
name|putAll
argument_list|(
name|getClassPathEntries
argument_list|(
name|parent
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|classloader
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URLClassLoader
name|urlClassLoader
init|=
operator|(
name|URLClassLoader
operator|)
name|classloader
decl_stmt|;
for|for
control|(
name|URL
name|entry
range|:
name|urlClassLoader
operator|.
name|getURLs
argument_list|()
control|)
block|{
name|URI
name|uri
decl_stmt|;
try|try
block|{
name|uri
operator|=
name|entry
operator|.
name|toURI
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|entries
operator|.
name|containsKey
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|entries
operator|.
name|put
argument_list|(
name|uri
argument_list|,
name|classloader
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|entries
argument_list|)
return|;
block|}
DECL|class|Scanner
annotation|@
name|VisibleForTesting
specifier|static
specifier|final
class|class
name|Scanner
block|{
DECL|field|resources
specifier|private
specifier|final
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|ResourceInfo
argument_list|>
name|resources
init|=
operator|new
name|ImmutableSortedSet
operator|.
name|Builder
argument_list|<
name|ResourceInfo
argument_list|>
argument_list|(
name|Ordering
operator|.
name|usingToString
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|scannedUris
specifier|private
specifier|final
name|Set
argument_list|<
name|URI
argument_list|>
name|scannedUris
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
DECL|method|getResources ()
name|ImmutableSortedSet
argument_list|<
name|ResourceInfo
argument_list|>
name|getResources
parameter_list|()
block|{
return|return
name|resources
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|scan (URI uri, ClassLoader classloader)
name|void
name|scan
parameter_list|(
name|URI
name|uri
parameter_list|,
name|ClassLoader
name|classloader
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|uri
operator|.
name|getScheme
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
operator|&&
name|scannedUris
operator|.
name|add
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|scanFrom
argument_list|(
operator|new
name|File
argument_list|(
name|uri
argument_list|)
argument_list|,
name|classloader
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|scanFrom (File file, ClassLoader classloader)
annotation|@
name|VisibleForTesting
name|void
name|scanFrom
parameter_list|(
name|File
name|file
parameter_list|,
name|ClassLoader
name|classloader
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|scanDirectory
argument_list|(
name|file
argument_list|,
name|classloader
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scanJar
argument_list|(
name|file
argument_list|,
name|classloader
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|scanDirectory (File directory, ClassLoader classloader)
specifier|private
name|void
name|scanDirectory
parameter_list|(
name|File
name|directory
parameter_list|,
name|ClassLoader
name|classloader
parameter_list|)
block|{
name|scanDirectory
argument_list|(
name|directory
argument_list|,
name|classloader
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|scanDirectory ( File directory, ClassLoader classloader, String packagePrefix)
specifier|private
name|void
name|scanDirectory
parameter_list|(
name|File
name|directory
parameter_list|,
name|ClassLoader
name|classloader
parameter_list|,
name|String
name|packagePrefix
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|directory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|warning
argument_list|(
literal|"Cannot read directory "
operator|+
name|directory
argument_list|)
expr_stmt|;
comment|// IO error, just skip the directory
return|return;
block|}
for|for
control|(
name|File
name|f
range|:
name|files
control|)
block|{
name|String
name|name
init|=
name|f
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|scanDirectory
argument_list|(
name|f
argument_list|,
name|classloader
argument_list|,
name|packagePrefix
operator|+
name|name
operator|+
literal|"/"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|resourceName
init|=
name|packagePrefix
operator|+
name|name
decl_stmt|;
if|if
condition|(
operator|!
name|resourceName
operator|.
name|equals
argument_list|(
name|JarFile
operator|.
name|MANIFEST_NAME
argument_list|)
condition|)
block|{
name|resources
operator|.
name|add
argument_list|(
name|ResourceInfo
operator|.
name|of
argument_list|(
name|resourceName
argument_list|,
name|classloader
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|scanJar (File file, ClassLoader classloader)
specifier|private
name|void
name|scanJar
parameter_list|(
name|File
name|file
parameter_list|,
name|ClassLoader
name|classloader
parameter_list|)
throws|throws
name|IOException
block|{
name|JarFile
name|jarFile
decl_stmt|;
try|try
block|{
name|jarFile
operator|=
operator|new
name|JarFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Not a jar file
return|return;
block|}
try|try
block|{
for|for
control|(
name|URI
name|uri
range|:
name|getClassPathFromManifest
argument_list|(
name|file
argument_list|,
name|jarFile
operator|.
name|getManifest
argument_list|()
argument_list|)
control|)
block|{
name|scan
argument_list|(
name|uri
argument_list|,
name|classloader
argument_list|)
expr_stmt|;
block|}
name|Enumeration
argument_list|<
name|JarEntry
argument_list|>
name|entries
init|=
name|jarFile
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|entries
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|JarEntry
name|entry
init|=
name|entries
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|isDirectory
argument_list|()
operator|||
name|entry
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|JarFile
operator|.
name|MANIFEST_NAME
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|resources
operator|.
name|add
argument_list|(
name|ResourceInfo
operator|.
name|of
argument_list|(
name|entry
operator|.
name|getName
argument_list|()
argument_list|,
name|classloader
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|jarFile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignored
parameter_list|)
block|{}
block|}
block|}
comment|/**      * Returns the class path URIs specified by the {@code Class-Path} manifest attribute, according      * to<a href="http://docs.oracle.com/javase/6/docs/technotes/guides/jar/jar.html#Main%20Attributes">      * JAR File Specification</a>. If {@code manifest} is null, it means the jar file has no      * manifest, and an empty set will be returned.      */
DECL|method|getClassPathFromManifest ( File jarFile, @Nullable Manifest manifest)
annotation|@
name|VisibleForTesting
specifier|static
name|ImmutableSet
argument_list|<
name|URI
argument_list|>
name|getClassPathFromManifest
parameter_list|(
name|File
name|jarFile
parameter_list|,
annotation|@
name|Nullable
name|Manifest
name|manifest
parameter_list|)
block|{
if|if
condition|(
name|manifest
operator|==
literal|null
condition|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|URI
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
name|String
name|classpathAttribute
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
operator|.
name|getValue
argument_list|(
name|Attributes
operator|.
name|Name
operator|.
name|CLASS_PATH
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|classpathAttribute
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|path
range|:
name|CLASS_PATH_ATTRIBUTE_SEPARATOR
operator|.
name|split
argument_list|(
name|classpathAttribute
argument_list|)
control|)
block|{
name|URI
name|uri
decl_stmt|;
try|try
block|{
name|uri
operator|=
name|getClassPathEntry
argument_list|(
name|jarFile
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// Ignore bad entry
name|logger
operator|.
name|warning
argument_list|(
literal|"Invalid Class-Path entry: "
operator|+
name|path
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|builder
operator|.
name|add
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|/**      * Returns the absolute uri of the Class-Path entry value as specified in      *<a href="http://docs.oracle.com/javase/6/docs/technotes/guides/jar/jar.html#Main%20Attributes">      * JAR File Specification</a>. Even though the specification only talks about relative urls,      * absolute urls are actually supported too (for example, in Maven surefire plugin).      */
DECL|method|getClassPathEntry (File jarFile, String path)
annotation|@
name|VisibleForTesting
specifier|static
name|URI
name|getClassPathEntry
parameter_list|(
name|File
name|jarFile
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
return|return
name|uri
return|;
block|}
else|else
block|{
return|return
operator|new
name|File
argument_list|(
name|jarFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|path
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
name|File
operator|.
name|separatorChar
argument_list|)
argument_list|)
operator|.
name|toURI
argument_list|()
return|;
block|}
block|}
block|}
DECL|method|getClassName (String filename)
annotation|@
name|VisibleForTesting
specifier|static
name|String
name|getClassName
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|int
name|classNameEnd
init|=
name|filename
operator|.
name|length
argument_list|()
operator|-
name|CLASS_FILE_NAME_EXTENSION
operator|.
name|length
argument_list|()
decl_stmt|;
return|return
name|filename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|classNameEnd
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
return|;
block|}
block|}
end_class

end_unit

