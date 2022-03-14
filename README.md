# commons-config

## Usage

```java
Try<Integer> code = Try.of(() -> {
    if(aBool) return 42;
    else throw new Exception();
});

// if aBool == true
int i = code.get(); // 42
Optional<Integer> opt = code.asOptional(); // Optional(42)

// if aBool == false
int i = code.getOrElse(-1); // -1
Optional<Integer> opt = code.asOptional(); // Optional.empty()
```

## Maven
### Repository
File: <i>pom.xml</i>
```Xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
### Dependency
File: <i>pom.xml</i>
```Xml
<dependency>
    <groupId>com.github.d-william</groupId>
    <artifactId>commons-try</artifactId>
    <version>2.0.0</version>
</dependency>
```
