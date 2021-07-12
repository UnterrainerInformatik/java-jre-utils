![GitHub forks](https://img.shields.io/github/forks/UnterrainerInformatik/java-jre-utils?style=social) ![GitHub stars](https://img.shields.io/github/stars/UnterrainerInformatik/java-jre-utils?style=social) ![GitHub repo size](https://img.shields.io/github/repo-size/UnterrainerInformatik/java-jre-utils) [![GitHub issues](https://img.shields.io/github/issues/UnterrainerInformatik/java-jre-utils)](https://github.com/UnterrainerInformatik/java-jre-utils/issues)

[![license](https://img.shields.io/github/license/unterrainerinformatik/FiniteStateMachine.svg?maxAge=2592000)](http://unlicense.org) [![Travis-build](https://travis-ci.org/UnterrainerInformatik/java-jre-utils.svg?branch=master)](https://travis-ci.org/github/UnterrainerInformatik/java-jre-utils) [![Maven Central](https://img.shields.io/maven-central/v/info.unterrainer.commons/jre-utils)](https://search.maven.org/artifact/org.webjars.npm/jre-utils) [![Twitter Follow](https://img.shields.io/twitter/follow/throbax.svg?style=social&label=Follow&maxAge=2592000)](https://twitter.com/throbax)




# jre-utils

A library to help with JRE stuff like `shutdownhooks` or reading resources.



## Collections

This package contains several collections I've come across and used in some programs.

### DataQueue

A synchronized, size-limited FIFO-queue.

### SizeLimitedHashMap

A normal HashMap, but limited in size.
If there is a size-overflow, the oldest value will be discarded.

### DataMap

A synchronized, size-limited HashMap.

### DataTable

This is a data-structure that holds arbitrary objects in a DataQueue and you may or may not add one or many `indexes`, which will generate a DataMap for each index, using the given keySupplier to generate the key for each entry.

When you add a new element, delete an element or clear the list, all indexes will be automatically equally affected.

The indexes use minimal memory, since the references to the corresponding values is shared.

```java
DataTable<String> dt = new DataTable<>(String.class, 10);
dt.addIndex("index1", e -> e);
dt.addIndex("index2", e -> e);
dt.add("test");
assertThat(dt.get("index1", "test")).isEqualTo("test");
assertThat(dt.get("index2", "test")).isEqualTo("test");
```





## DoubleBufferedFile

This is a data-structure that is thread-safe and uses two files to persist itself.
This way you have a file left, if something happens during persisting.

```java
DoubleBufferedFile dbf = new DoubleBufferedFile(Path.of("new"), "txt");
dbf.write(w -> w.write("test_old"));
dbf.write(w -> w.write("test_new"));
String value = dbf.read() // is equal to "test_new"
dbf.delete();
```

The above example generates two files: `new1.txt` and `new2.txt` and the accessors always give the correct file-handle to read or write-to.



## DateUtils

Offers some often used date-time conversions including UTC and ISO8601.



## ForName

This helps you to load and instantiate classes that haven't already been loaded using a given class-loader. This is of great help to strip boilerplate code when doing stuff like plugin-systems or the like.



## Resources

Helps reading and searching for resource files.

The special thing about these is, that they work in JAR-files AND server-deployments alike.



## ShutdownHook

If you'd like some code running before the Java-VM shuts down, then this is the way to go.
I use it for shutting down the EntityManagerFactory in some programs as gracefully as it gets.

### Example

Shut down the executor-service gracefully at the end of your program.

```java
ShutdownHook.register(() -> {
    executorService.shutdown();
    try {
        if (!executorService.awaitTermination(1, TimeUnit.SECONDS))
            executorService.shutdownNow();
    } catch (InterruptedException e) {
        executorService.shutdownNow();
    }
});
```



## Reflecting

Reflection utilities.
If you'd like to scan for a specific class-type within an object tree, then this could be of help.
Same if you'd like to get the instance of such a field within an instance, but get it by path-name.

### Example

```java
// getFieldByPath
TestClass tc = new TestClass();
MyType mt = Reflecting.getFieldByPath("subClass.myType", tc, ContainsMyType.class);
mt.setName("blubb");
assertThat(tc.getSubClass().getMyType().getName()).isEqualTo("blubb");
assertThat(tc.getMyType().getName()).isNull();

// getPathsOf
List<String> results = Reflecting.getPathsOf(TestClass.class, MyType.class, ContainsMyType.class);
assertThat(results).contains("myType");
assertThat(results).contains("subClass.myType");
```



## Exceptions

This utility-class contains `swallow` and `swallowReturning` which helps you to swallow a specific exception (checked or unchecked) silently.

### Example

#### Before

```java
@Test
public void BeforeTest() {
    try {
        // Some method you don't have control over throwing an exception.
        throw new IllegalArgumentException();
    } catch (IllegalArgumentException e) {
        // NOOP.
    }
}
```

The problem with this is that it's very verbose, possibly destroys the good visual style you're currently running in your library, or that your linter complains about NOOP catches.

#### After

```Java
Exceptions.swallow(() -> {
    throw new IllegalArgumentException();
}, IllegalArgumentException.class);
```

Obviously, you can use `swallowReturning` for methods that return a value and throw a checked or unchecked exception.

The `Exception` parameter is a `varArg`, so you may specify more than one Exception here.

