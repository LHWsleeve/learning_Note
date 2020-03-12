### 1.Java 面向对象编程三大特性: 封装 继承 多态

#### 封装

封装把一个对象的属性私有化，同时提供一些可以被外界访问的属性的方法，如果属性不想被外界访问，我们大可不必提供方法给外界访问。但是如果一个类没有提供给外界访问的方法，那么这个类也没有什么意义了。

#### 继承

继承是使用已存在的类的定义作为基础建立新类的技术，新类的定义可以增加新的数据或新的功能，也可以用父类的功能，但不能选择性地继承父类。通过使用继承我们能够非常方便地复用以前的代码。

**关于继承如下 3 点请记住：**

子类拥有父类对象所有的属性和方法（包括私有属性和私有方法），但是父类中的私有属性和方法子类是无法访问，_只是拥有_。
子类可以拥有自己属性和方法，即子类可以对父类进行扩展。
子类可以用自己的方式实现父类的方法。（以后介绍）。

#### 多态

所谓多态就是指程序中定义的引用变量所指向的具体类型和通过该引用变量发出的方法调用在编程时并不确定，而是在程序运行期间才确定，即一个引用变量到底会指向哪个类的实例对象，该引用变量发出的方法调用到底是哪个类中实现的方法，必须在由程序运行期间才能决定。

**<font color="blue" size="4">在 Java 中有两种形式可以实现多态：继承（多个子类对同一方法的重写）和接口（实现接口并覆盖接口中同一方法）。</font>**

### 2.String、StringBuffer 和 StringBuilder 的区别是什么? String 为什么是不可变的?

#### 可变性

简单的来说：`String` 类中使用 `final` 关键字修饰字符数组来保存字符串，`private final char value[]`，所以`String` 对象是不可变的。而 `StringBuilder`与 ` StringBuffer ``都继承自 AbstractStringBuilder ` 类，在 `AbstractStringBuilder` 中也是使用字符数组保存字符串`char[]value` 但是没有用 `final` 关键字修饰，所以这两种对象都是可变的。

当创建 String 类型的对象时，虚拟机会在常量池中查找有没有**已经存在的值和要创建的值相同的对象**，如果有就把它赋给当前引用。如果没有就在常量池中重新创建一个 String 对象。

**_注意:_** JDK11 中我发现所有 String\*\*类型都是由字节(`byte[]`)数组保存而不是字符(`char[]`)数组

#### 线程安全性

String 由 final 修饰，是不可变对象，是线程安全的。

StringBuffer 的方法由`synchronized`修饰(对方法加了同步锁或对调用的方法加了同步锁)，所以线程安全

StringBuilder 没有对方法加锁，所以非线程安全。

#### 性能

每次对 String**改变**的时候，会生成一个新的 String 对象，然后将指针纸箱新的对象。

StringBuffer 和 StringBuilder 每次改变时对会对**对象本身**进行操作，不会生成新的对象。
这二者相比后者只比前者性能高 10-15%，但存在线程不安全的风险。

#### 使用总结

少量数据：String；
单线程大量数据：StringBuilder；
多线程大连数据：StringBuffer；

### 3.自动装箱和拆箱

装箱：基本类型用他们对应的引用类型包装起来(变成包装类)
拆箱：将包装类型转换为基本数据类型

### 4.在 Java 中定义一个不做事且没有参数的构造方法的作用

Java 程序在执行子类的构造方法之前，如果没有用 `super()`来调用父类特定的构造方法，则会调用父类中“没有参数的构造方法”。因此，如果父类中只定义了有参数的构造方法，而在子类的构造方法中又没有用 `super()`来调用父类中特定的构造方法，则编译时将发生错误，因为 Java 程序在父类中找不到没有参数的构造方法可供执行。解决办法是在父类里加上一个不做事且没有参数的构造方法。
即：如果子类继承与父类，在子类实例化前先实例化父类。若子类中没有用`super()`方法调用父类的有参构造器，JVM 自动调用无参构造器。无参构造器就是为了防止报错。

注：我在手动实现 JDBC 时，数据库和 POJO 的映射关系时必须要生成无参构造器。（印象）

### 5. 接口和抽象类的区别是什么？

1. 接口的方法默认是 `public`，所有方法在接口中不能有实现(Java 8 开始接口方法可以有默认实现），而抽象类可以有非抽象的方法。
2. 接口中除了 `static`、`final` 变量，不能有其他变量，而抽象类中则不一定。
3. 一个类可以实现多个接口，但**只能实现一个抽象类**。接口自己本身可以通过 `extends` 关键字扩展多个接口。
4. 接口方法默认修饰符是`public`，抽象方法可以有`public、protected 和 default` 这些修饰符 **<font color="red">_（抽象方法就是为了被重写所以不能使用 private 关键字修饰！）</font>_**。
5. 从设计层面来说，抽象是对类的抽象，是一种模板设计，而接口是对行为的抽象，是一种行为的规范。

**备注：**

```markdown
1. 在 jdk 7 或更早版本中，接口里面只能有常量、变量和抽象方法。这些接口方法必须由选择实现接口的类实现。
2. jdk8 的时候接口可以有默认方法和静态方法功能。---静态方法可以直接用接口名调用。**实现类和实现是不可以调用的**。
3. 如果同时实现两个接口，接口中定义了一样的默认方法，则必须重写
4. Jdk 9 在接口中引入了私有方法和私有静态方法。
5. 关于抽象类
   JDK 1.8 以前，抽象类的方法默认访问权限为 protected
   JDK 1.8 时，抽象类的方法默认访问权限变为 default
   关于接口
   JDK 1.8 以前，接口中的方法必须是 public 的
   JDK 1.8 时，接口中的方法可以是 public 的，也可以是 default 的
   JDK 1.9 时，接口中的方法可以是 private 的
```

### 6. 成员变量与局部变量的区别有哪些？

1. 从**语法形式**上看:`成员变量`是属于类的(**类开始定义的变量，经常被我认为是全局变量，实际上 Java 不存在全局变量，static 可以冒充一下**)，而`局部变量`是在方法中定义的变量或是方法的参数；`成员变量`可以被 `public,private,static` 等访问控制符所修饰，而`局部变量`不能被访问控制修饰符及 `static` 所修饰；但是，成员变量和局部变量都能被 `final` 所修饰。
2. 变量在内存中的**存储方式**来看:如果`成员变量`是使用`static`修饰的，那么这个成员变量是属于类的(**此时冒充全局变量**)，如果没有使用 static 修饰，这个成员变量是属于`实例`的。而**对象存在于堆内存，局部变量则存在于栈内存**。<font color="red" size="5">重要</font>
3. 从变量在内存中的**生存时间**上看:成员变量是对象的一部分，它随着对象的创建而存在，而局部变量随着方法的调用而自动消失。
4. **成员变量如果没有被赋初值**:则会自动以类型的默认值而赋值（一种情况例外:被 `final`修饰的成员变量也必须显式地赋值(而且还是大写)），而**局部变量则不会自动赋值**。

### 7.创建一个对象用什么运算符?对象实体与对象引用有何不同?

new 运算符，new 创建对象实例（**对象实例在堆内存中**），对象引用指向对象实例（**对象引用存放在栈内存中**）。一个对象引用可以指向 0 个或 1 个对象（一根绳子可以不系气球，也可以系一个气球）;一个对象可以有 n 个引用指向它（可以用 n 条绳子系住一个气球）。

### 8.静态方法和实例方法有何不同

1. 在外部调用静态方法时，可以使用"类名.方法名"的方式，也可以使用"对象名.方法名"的方式。而实例方法只有后面这种方式。也就是说，调用静态方法可以无需创建对象。

2. **静态方法**在访问**本类**的成员时，只允许访问静态成员（即静态成员变量和静态方法），而**不允许访问实例成员变量和实例方法**；实例方法则无此限制。
   **原因：** 静态方法随着类的创建而创建，实例成员变量和实例方法此时可能并未创建。

### 9.对象的相等(equals)与指向他们的引用(==)相等,两者有什么不同?

<font color="blue" size="4">对象的相等，比的是内存中存放的内容是否相等。而引用相等，比较的是他们指向的内存地址是否相等。</font>

#### 9.1 ：<font color="red" size="4">比较:</font>

**== :** 它的作用是判断两个**对象的地址**是不是相等。即，判断两个对象是不是同一个对象(基本数据类型`==`比较的是值，引用数据类型`==`比较的是内存地址,即对象存放的地址是不是一样)。

**equals() :** 它的作用也是判断两个对象是否相等。但它一般有两种使用情况：

- 情况 1：类没有覆盖 equals() 方法。则通过 equals() 比较该类的两个对象时，对象值得比较。对于没有重写过 equals()的对象，本质和`==`没区别。
  **注意：**_String 中的 equals 方法是被重写过的，因为 object 的 equals 方法是比较的对象的内存地址，而 String 的 equals 方法比较的是对象的值。_

```java
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }
```

- 情况 2：类覆盖了 equals() 方法。一般，我们都覆盖 equals() 方法来比较两个对象的内容是否相等；若它们的内容相等，则返回 true (即，认为这两个对象相等)。

#### 9.2 hashCode 与 equals(重要)

**hashCode（）：** hashCode() 的作用是获取哈希码，也称为散列码；它实际上是返回一个`int`整数。这个哈希码的作用是确定该对象在哈希表中的索引位置。
`hashCode()` 定义在 `JDK` 的 `Object.java` 中，这就意味着 Java 中的任何类都包含有 `hashCode()` 函数。

**以“HashSet 如何检查重复”为例子来说明为什么要有 hashCode：** 当你把对象加入 `HashSet` 时，`HashSet` 会先计算对象的 `hashcode` 值来判断对象加入的位置，同时也会与该位置其他已经加入的对象的 `hashcode` 值作比较，如果没有相符的 `hashcode`，`HashSet` 会假设对象没有重复出现。但是如果发现有相同`hashcode`值的对象，这时会调用 equals()方法来检查`hashcode` 相等的对象是否真的相同。如果两者相同，`HashSet` 就不会让其加入操作成功。如果不同的话，就会重新散列到其他位置。这样我们就大大减少了 `equals` 的次数，相应就大大提高了执行速度。
<font color="red">HashSet 计算对象的 hashcode 值--->若哈希值相同，则 equals 比较，不同则插入，相同则舍弃</font>

**<font color="red">hashcode（）和 equals（）的关系：</font>**
（1）如果不创建“类对应的散列表的话”（就是当我们不会把一个类放到在 `HashSet, Hashtable, HashMap`这种底层实现是以 `hashcode` 来去定位存储位置的话），如果不是这种情况下的话，此时这个类的 `hashcode（）和 equals（）`是没有一点关系的

（2）如果恰好用到了上面所说的“创建了类对应的散列表的话”，那么也就是你把这个类作为 key 来去存储其他的 value 的话，这种情况下是可以进行比较的

如果两个对象相等，那么它们的 hashCode()值一定相同。这里的相等是指，通过 equals()比较两个对象时返回 true。
如果两个对象 hashCode()相等，它们并不一定相等。
因为在散列表中，hashCode()相等，即两个键值对的哈希值相等。**然而哈希值相等，并不一定能得出键值对相等。补充说一句：“两个不同的键值对，哈希值相等”，这就是哈希冲突。**（若要判断两个对象是否相等，除了要覆盖 equals()之外，也要覆盖 hashCode()函数。否则，equals()无效。 ）**即，哈希值相等可能是因为处理哈希冲突造成的**
<font color="red" size="5">equal 相等-->hashCode()相等，反之不成立</font>

#### 9.3 hashCode（）与 equals（）的相关规定

- 如果两个对象相等，则 `hashcode` 一定也是相同的
- 两个对象相等,对两个对象分别调用 `equals` 方法都返回`true`
- 两个对象有相同的 `hashcode`值，它们也不一定是相等的
- 因此，`equals` 方法被覆盖过，则 `hashCode` 方法也必须被覆盖
- `hashCode()` 的默认行为是对堆上的对象产生**独特值**。如果没有重写 `hashCode()`，则该 `class` 的两个对象无论如何都不会相等（即使这两个对象指向相同的数据---如果类需要放进类似`HashMap` 之类的基于哈希表来实现的容器中并作为 `key` 时，就有必要覆盖 `hashCode`方法，按照我们自定的规则来得到一个矩形对象的 `hash`值。（不指定，一般是通过将该对象的内部地址转换成一个整数来实现的））

### 10.为什么 Java 中只有值传递？

按值调用(call by value)表示方法接收的是调用者提供的值，而按引用调用(call by reference)表示方法接收的是调用者提供的变量地址。一个方法可以修改传递引用所对应的变量值，而不能修改传递值调用所对应的变量值。 它用来描述各种程序设计语言（不只是 Java)中方法参数传递方式。

Java 程序设计语言**总是采用按值调用**。也就是说，方法得到的是所有参数值(或引用)的一个拷贝，也就是说，方法不能修改传递给它的任何参数变量的内容(可以修改拷贝的引用所指向的对象的内容)。
example:基本类型

```java
public static void main(String[] args) {
    int num1 = 10;
    int num2 = 20;

    swap(num1, num2);

    System.out.println("num1 = " + num1);
    System.out.println("num2 = " + num2);
}

public static void swap(int a, int b) {
    int temp = a;
    a = b;
    b = temp;

    System.out.println("a = " + a);
    System.out.println("b = " + b);
}
显然：
a = 20
b = 10
num1 = 10
num2 = 20
```

example2：对象

```java
    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5 };
        System.out.println(arr[0]);
        change(arr);
        System.out.println(arr[0]);
    }

    public static void change(int[] array) {
        // 将数组的第一个元素变为0
        array[0] = 0;
    }
显然：
1
0
```

此时似乎是引用调用，实际上并不是。传入的`array`是`arr`的一个**拷贝**（浅拷贝还是深拷贝，需要验证），因为拷贝的引用和原引用指向同一个对象所以可以修改对象内的内容。
Java 程序设计语言对对象采用的不是引用调用，实际上，对象引用是按值传递的。

下面再总结一下 Java 中方法参数的使用情况：

- 一个方法不能修改一个基本数据类型的参数（即数值型或布尔型）。
- 一个方法可以改变一个对象参数的状态。
- 一个方法不能让对象参数引用一个新的对象。

### 11.简述线程、程序、进程的基本概念

- **_进程_** 是程序的一次执行过程，是系统运行程序的基本单位，因此进程是动态的。系统运行一个程序即是一个进程从创建，运行到消亡的过程。简单来说，一个进程就是一个执行中的程序，它在计算机中一个指令接着一个指令地执行着，同时，每个进程还占有某些系统资源如 CPU 时间，内存空间，文件，输入输出设备的使用权等等。换句话说，当程序在执行时，将会被操作系统载入内存中。 线程是进程划分成的更小的运行单位。**线程和进程最大的不同在于基本上各进程是独立的，而各线程则不一定，因为同一进程中的线程极有可能会相互影响**。从另一角度来说，进程属于操作系统的范畴，主要是**同一段时间**内，可以同时执行一个以上的程序，而线程则是在**同一程序**内几乎同时执行一个以上的程序段。
- **_线程_** 与进程相似，但线程是一个比进程更小的执行单位。一个进程在其执行的过程中可以产生多个线程。与进程不同的是同类的多个线程 **_共享同一块内存空间和一组系统资源(所以线程安全很重要)_**，所以系统在产生一个线程，或是在各个线程之间作切换工作时，负担要比进程小得多，也正因为如此，线程也被称为**轻量级进程**。

- **_程序_** 是含有指令和数据的文件，被存储在磁盘或其他的数据存储设备中，也就是说程序是静态的代码。

#### 11 线程有哪些基本状态?

Java 线程在运行的生命周期中的指定时刻只可能处于下面 6 种不同状态的其中一个状态：
![线程状态](assserts/12.png)

线程在生命周期中并不是固定处于某一个状态而是随着代码的执行在不同状态之间切换。
**Java 线程状态变迁图：要能手画**
![Java 线程状态变迁](assserts/Java线程状态变迁.png)
**变迁图解析：**
线程创建之后它将处于 NEW（新建） 状态，调用 `start()` 方法后开始运行，线程这时候处于 **READY（可运行）** 状态。可运行状态的线程获得了 cpu 时间片（timeslice）后就处于 **RUNNING（运行）** 状态。

```
操作系统隐藏 Java 虚拟机（JVM）中的 READY 和 RUNNING 状态，它只能看到 RUNNABLE 状态，所以 Java 系统一般将这两个状态统称为 RUNNABLE（运行中） 状态。
```

cpu 调度图：
![RUNNABLE-VS-RUNNING.png](assserts/RUNNABLE-VS-RUNNING.png)

当线程执行 `wait()`方法之后，线程进入 WAITING（等待）状态。进入等待状态的线程需要*依靠其他线程的通知*才能够返回到运行状态，而 TIME_WAITING(超时等待,其实是定时等待) 状态相当于在*等待状态的基础上*增加了超时限制，比如通过`sleep（long millis）`方法或`wait（long millis）`方法可以将 Java 线程置于 TIMED WAITING 状态。当超时时间到达后 Java 线程将会返回到 RUNNABLE 状态。当线程调用同步方法时，在没有获取到锁的情况下，线程将会进入到 BLOCKED（阻塞） 状态。线程在执行 Runnable 的`run()`方法之后将会进入到 TERMINATED（终止） 状态。

### 12.final 关键字总结

final 关键字主要用在三个地方：变量、方法、类。

- 对于一个 final 变量，如果是**基本数据类型**的变量，则其数值一旦在初始化之后便不能更改；如果是**引用类型**的变量，则在对其初始化之后便不能再让其指向另一个对象。
- 当用 final 修饰一个类时，表明这个**类不能被继承**。final 类中的所有成员方法都会被**隐式地**指定为 final 方法。
- 使用 final 方法的原因有两个。第一个原因是**把方法锁定**，以防任何继承类修改它的含义；第二个原因是**效率**。在早期的 Java 实现版本中，会将 final 方法转为内嵌调用。但是如果方法过于庞大，可能看不到内嵌调用带来的任何性能提升（现在的 Java 版本已经不需要使用 final 方法进行这些优化了）。**类中所有的 private 方法都隐式地指定为 final。**

### 13.Java 中的异常处理

#### 13.1 **Java 异常类层次结构图：**

![Exception.png](assserts/Exception.png)
在 Java 中，所有的异常都有一个共同的祖先 java.lang 包中的 **Throwable 类**。
Throwable： 有两个重要的子类：**Exception（异常）** 和 **Error（错误） **，二者都是 Java 异常处理的重要子类，各自都包含大量子类。
**Error（错误）:是程序无法处理的错误**，表示运行应用程序中较严重问题。大多数错误与代码编写者执行的操作无关，而==表示代码运行时 JVM（Java 虚拟机）出现的问题==。例如，Java 虚拟机运行错误（Virtual MachineError），当 JVM 不再有继续执行操作所需的内存资源时，将出现 OutOfMemoryError。这些异常发生时，Java 虚拟机（JVM）一般会选择线程终止。
**这些错误表示故障发生于虚拟机自身、或者发生在虚拟机试图执行应用时：** 如 Java 虚拟机运行错误（Virtual MachineError）、类定义错误（NoClassDefFoundError）等。==这些错误是不可查的==，因为它们在应用程序的控制和处理能力之 外，而且绝大多数是程序运行时不允许出现的状况。对于设计合理的应用程序来说，即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状况。在 Java 中，错误通过 Error 的子类描述。
**Exception（异常）:是程序本身可以处理的异常。** Exception 类有一个重要的子类 ` RuntimeException``。RuntimeException ` 异常由 Java 虚拟机抛出。`NullPointerException`（要访问的变量没有引用任何对象时，抛出该异常）、`ArithmeticException`（算术运算异常，一个整数除以 0 时，抛出该异常）和 `ArrayIndexOutOfBoundsException` （下标越界异常）。
**注意：异常和错误的区别：异常能被程序本身处理，错误是无法处理。**

#### 13.2 Throwable 类常用方法

- public string getMessage():返回异常发生时的简要描述
- public string toString():返回异常发生时的详细信息
- public string getLocalizedMessage():返回异常对象的本地化信息。使用 Throwable 的子类**覆盖这个方法，可以生成本地化信息**。如果子==类没有覆盖该方法，则该方法返回的信息与 getMessage（）返回的结果相同==
- public void printStackTrace():在**控制台上打印** Throwable 对象封装的异常信息

#### 13.3 异常处理总结

- try 块： 用于捕获异常。其后可接零个或多个 catch 块，==如果没有 catch 块，则必须跟一个 finally 块。==
- catch 块： 用于处理 try 捕获到的异常。
- finally 块： 无论是否捕获或处理异常，finally 块里的语句**都会被执行**。当在 try 块或 catch 块中遇到 return 语句时，finally 语句块将在方法返回之前被执行。
  **即，先执行 finally 中剩下部分，然后返回 return 对象**

**<font color="red" size="5">在以下 4 种特殊情况下，finally 块不会被执行：</font>**

1. 在 finally ==语句块第一行==发生了异常。 因为在其他行，finally 块还是会得到执行
2. 在前面的代码中用了 `System.exit(int)`已退出程序。 exit 是带参函数 ；若该语句在异常语句之后，finally 会执行
   **这个函数就是无理由强制退出，放在任何一个地方都不会继续执行该语句之后的代码**
3. 程序所在的线程死亡。
4. 关闭 CPU。

**<font color="red" size="4">注意：</font>** 当 try 语句和 finally 语句中都有 return 语句时，在方法返回之前，finally 语句的内容将被执行，并且==finally 语句的返回值将会覆盖原始的返回值==。如下：

```java
  public static int f(int value) {
        try {
            return value * value;
        } finally {
            if (value == 2) {
                return 0;
            }
        }
    }
显然：最终返回值是0
```

### 14.Java 序列化中如果有些字段不想进行序列化，怎么办？

**可以用在深拷贝**
在不想序列化的字段前加上 transient 关键字修饰

```java
	private transient String name2;
```

**transient 关键字的作用是：**阻止实例中那些用此关键字修饰的的变量序列化；当对象被反序列化时，被 transient 修饰的变量值不会被持久化和恢复。transient 只能修饰变量，不能修饰类和方法。

**<font color="red" size="4">注意：</font>** 这和 spring 中的@transient 注解不同。
实体类(pojo)中使用了`@Table`注解后，想要==添加表中不存在的字段==，就要使用`@Transient`这个注解了。
使用 `@Transient` 表示该属性并非是一个要映射到数据库表中的字段,只是起辅助作用,ORM 框架将会忽略该属性

### 15. 获取用键盘输入常用的两种方法

这东西经常记不清，不过刷题作为输入流很重要。
**方法一：**通过 Scanner,这是最常见和简单的

```java
Scanner input = new Scanner(System.in);
String s  = input.nextLine();
input.close();
```

**方法二：**通过 BufferdReader，这实际上和接受任何刘的形式一样

```java
BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
String s = input.readLine();
```

### 16.重要：Java 中的 IO 流

#### 16.1 Java 中 IO 流分为几种?

- 按照流的流向分，可以分为输入流和输出流；
- 按照操作单元划分，可以划分为字节流和字符流；
- 按照流的角色划分为节点流和处理流。
  Java Io 流共涉及 40 多个类，这些类看上去很杂乱，但实际上很有规则，而且彼此之间存在非常紧密的联系，**Java IO 流的 40 多个类都是从如下 4 个抽象类基类中派生出来的。**

- ==InputStream/Reader==: 所有的输入流的基类，前者是字节输入流，后者是字符输入流。
- ==OutputStream/Writer==: 所有输出流的基类，前者是字节输出流，后者是字符输出流。
  **按操作方式分类结构图：**
  ![IO-操作方式分类.png](assserts/IO-操作方式分类.png)

**按操作对象分类结构图：**
![IO-操作对象分类.png](assserts/IO-操作对象分类.png)
**问处理流和节点流的区别？**

#### 16.2 既然有了字节流,为什么还要有字符流?

问题本质想问：**不管是文件读写还是网络发送接收，信息的最小存储单元都是字节，那为什么 I/O 流操作要分为字节流操作和字符流操作呢？**

**回答：** ==字符流是由 Java 虚拟机将字节转换得到的==，问题就出在这个过程还算是非常耗时，并且，如果我们不知道编码类型就很容易出现乱码问题。所以， I/O 流就干脆提供了一个直接操作字符的接口，==方便我们平时对字符进行流操作==。**如果音频文件、图片等媒体文件用字节流比较好，如果涉及到字符的话使用字符流比较好。**

#### 16.3 BIO,NIO,AIO 有什么区别?重要的很！！

- **BIO (Blocking I/O): 同步阻塞 I/O 模式，**==数据的读取写入必须阻塞在一个线程内等待其完成==。在活动连接数不是特别高（小于单机 1000）的情况下，这种模型是比较不错的，可以让每一个连接专注于自己的 I/O 并且编程模型简单，也==不用过多考虑系统的过载、限流等问题==。==线程池本身就是一个天然的漏斗，可以缓冲一些系统处理不了的连接或请求==。但是，当面对十万甚至百万级连接的时候，传统的 BIO 模型是无能为力的。因此，我们需要一种更高效的 I/O 处理模型来应对更高的并发量。
- **NIO (New I/O): NIO 是一种同步非阻塞的 I/O 模型**，在 Java 1.4 中引入了 NIO 框架，对应 java.nio 包，提供了 Channel , Selector，Buffer 等抽象类。NIO 中的 N 可以理解为 Non-blocking，不单纯是 New。它支持面向缓冲的，基于通道的 I/O 操作方法。 NIO 提供了与传统 BIO 模型中的 `Socket`和 `ServerSocket` 相对应的 `SocketChannel` 和 `ServerSocketChannel` 两种不同的套接字通道实现,==两种通道都支持阻塞和非阻塞两种模式==。阻塞模式使用就像传统中的支持一样，比较简单，但是性能和可靠性都不好；非阻塞模式正好与之相反。
  对于低负载、低并发的应用程序，可以使用同步阻塞 I/O 来提升开发速率和更好的维护性；
  对于高负载、高并发的（网络）应用，应使用 NIO 的非阻塞模式来开发
- **AIO (Asynchronous I/O): AIO 也就是 NIO 2**。在 Java 7 中引入了 NIO 的改进版 NIO 2,它是==异步==非阻塞的 IO 模型。
  ==异步 IO 是基于事件和回调机制实现的==，也就是应用操作之后会直接返回，不会堵塞在那里，当后台处理完成，操作系统会通知相应的线程进行后续的操作。AIO 是异步 IO 的缩写，虽然 NIO 在网络操作中，提供了非阻塞的方法，但是 NIO 的 IO 行为还是同步的。对于 NIO 来说，我们的业务线程是在 IO 操作准备好时，得到通知，接着就由这个线程自行进行 IO 操作，IO 操作本身是同步的。查阅网上相关资料，我发现就目前来说 AIO 的应用还不是很广泛，Netty 之前也尝试使用过 AIO，不过又放弃了。

### 17.final,static,this,super 关键字总结(重要)

[basic/关键字总结.md](basic/关键字总结.md)

### 18. Collections 工具类和 Arrays 工具类常见方法总结(重要)

[basic/工具类总结.md](basic/工具类总结.md)

### 19。深拷贝 vs 浅拷贝

1. **浅拷贝：**创建一个新对象，然后将当前对象的非静态字段复制到该新对象，如果字段是值类型的，那么对该字段执行复制；如果该字段是引用类型的话，则复制引用但不复制引用的对象。因此，原始对象及其副本引用同一个对象。
2. **深拷贝：**创建一个新对象，然后将当前对象的非静态字段复制到该新对象，无论该字段是值类型的还是引用类型，都复制独立的一份。当你修改其中一个对象的任何内容时，都不会影响另一个对象的内容。
   ![java-deep-and-shallow-copy.png](assserts/java-deep-and-shallow-copy.png)
   **如何实现深拷贝：**
3. 让每个引用类型属性内部都重写 `clone()`方法:既然引用类型不能实现深拷贝，那么我们将每个引用类型都拆分为基本类型，分别进行浅拷贝。
4. 利用序列化：序列化是将对象写到流中便于传输，而反序列化则是把对象从流中读取出来。这里写到流中的对象则是原始对象的一个拷贝，因为原始对象还存在 JVM 中，所以我们可以利用对象的序列化产生克隆对象，然后通过反序列化获取这个对象。

```java
//深度拷贝
public Object deepClone() throws Exception{
    // 序列化
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);

    oos.writeObject(this);

    // 反序列化
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bis);

    return ois.readObject();
}
```

**注意：** 每个需要序列化的类都要实现 `Serializable` 接口，如果有某个属性不需要序列化，可以将其声明为 `transient`，即将其排除在克隆属性之外。
　　因为序列化产生的是两个完全独立的对象，所有无论嵌套多少个引用类型，序列化都是能实现深拷贝的。

### 20 Java 疑难点

#### 20.1 基本数据类型

##### 所有整型包装类对象值的比较必须使用 equals 方法。

```java
public class Interger {
    public static void main(String[] args) {
        Integer a = 200;
        Integer b = 200;
        System.out.println(a==b);
        int c=200;
        int d = 200;
        System.out.println(c==d);
    }
}

第一个是false，但是如果a,b在-128~127之间是true
第二个是 true
```

当使用==自动装箱方式创建一个 Integer 对象时，当数值在-128 ~127 时，会将创建的 Integer 对象缓存起来，当下次再出现该数值时，直接从缓存中取出对应的 Integer 对象==。所以上述代码中，x 和 y 引用的是相同的 Integer 对象。

##### BigDecimal 的用处

《阿里巴巴 Java 开发手册》中提到：==浮点数之间的等值判断，基本数据类型不能用==来比较==，包装数据类型不能用 equals 来判断。 具体原理和浮点数的编码方式有关，这里就不多提了，我们下面直接上实例：

```java
float a = 1.0f - 0.9f;
float b = 0.9f - 0.8f;
System.out.println(a);// 0.100000024
System.out.println(b);// 0.099999964
System.out.println(a == b);// false
```

具有基本数学知识的我们很清楚的知道输出并不是我们想要的结果（==精度丢失==），我们如何解决这个问题呢？一种很常用的方法是：**使用使用 BigDecimal 来定义浮点数的值，再进行浮点数的运算操作。 BigDecimal(string)**

```java
fBigDecimal a = new BigDecimal("1.0");
BigDecimal b = new BigDecimal("0.9");
BigDecimal c = new BigDecimal("0.8");
BigDecimal x = a.subtract(b);// 0.1
BigDecimal y = b.subtract(c);// 0.1
System.out.println(x.equals(y));// true
```

##### 基本数据类型与包装数据类型的使用标准

Reference:《阿里巴巴 Java 开发手册》

- 【强制】所有的 POJO 类属性必须使用包装数据类型。
- 【强制】RPC 方法的返回值和参数必须使用包装数据类型。
- 【推荐】所有的局部变量使用基本数据类型。

#### 20.2 集合

##### Arrays.asList()使用指南

`Arrays.asList()`在平时开发中还是比较常见的，我们可以使用它将一个数组转换为一个 List 集合。**数组是数组，list 的集合是集合**

```java
String[] myArray = { "Apple", "Banana", "Orange" }；
List<String> myList = Arrays.asList(myArray);
//上面两个语句等价于下面一条语句
List<String> myList = Arrays.asList("Apple","Banana", "Orange");

//源码
/**
 *返回由指定数组支持的固定大小的列表。此方法作为基于数组和基于集合的API之间的桥梁，与Collection.toArray()结合使用。返回的List是可序列化并实现RandomAccess接口。
 */
public static <T> List<T> asList(T... a) {
    return new ArrayList<>(a);
}
```

**Reference:《阿里巴巴 Java 开发手册》**
`Arrays.asList()`将数组转换为集合后,==底层其实还是数组==，《阿里巴巴 Java 开发手册》对于这个方法有如下描述：
![arrays.aslist.png](assserts/arrays.aslist.png)
**使用时的注意事项总结**
传递的数组==必须是对象数组==，而不是基本类型。

```java
int[] myArray = { 1, 2, 3 };
List myList = Arrays.asList(myArray);
System.out.println(myList.size());//1
System.out.println(myList.get(0));//数组地址值
System.out.println(myList.get(1));//报错：ArrayIndexOutOfBoundsException
int [] array=(int[]) myList.get(0);
System.out.println(array[0]);//1
```

当传入一个原生数据类型数组时，`Arrays.asList()` 的真正得到的参数就不是数组中的元素，而是==数组对象本身==！此时==List 的唯一元素就是这个数组==，这也就解释了上面的代码。
我们使用包装类型数组就可以解决这个问题。

```java
Integer[] myArray = { 1, 2, 3 };
```

使用集合的修改方法:`add()、remove()、clear()`会抛出异常。
`Arrays.asList()` 方法返回的并不是`java.util.ArrayList` ，而是 j`ava.util.Arrays` 的一个内部类,这个内部类并==没有实现集合的修改方法==或者说并没有重写这些方法。

##### 将数组转换为 ArrayList?

- `List list = new ArrayList<>(Arrays.asList("a", "b", "c"))`
- JDK1.8：

```java
Integer [] myArray = { 1, 2, 3 };
List myList = Arrays.stream(myArray).collect(Collectors.toList());
//基本类型也可以实现转换（依赖boxed的装箱操作）
int [] myArray2 = { 1, 2, 3 };
List myList = Arrays.stream(myArray2).boxed().collect(Collectors.toList());
```

- 使用 Guava(推荐)?

##### Collection.toArray()方法使用的坑&如何反转数组

该方法是一个泛型方法：<T> T[] toArray(T[] a); 如果 toArray 方法中没有传递任何参数的话返回的是 Object 类型数组。

```java
String [] s= new String[]{
    "dog", "lazy", "a", "over", "jumps", "fox", "brown", "quick", "A"
};
List<String> list = Arrays.asList(s);
Collections.reverse(list);//这是一个静态方法，此时list已经反转。内部实现是迭代器。得到的是一个反转后的泛型集合
s=list.toArray(new String[0]);//没有指定类型的话会报错。这个方法是按照指定类型返回数组
```

由于 JVM 优化，`new String[0]`作为`Collection.toArray()`方法的参数现在使用更好，`new String[0]`就是起一个模板的作用，指定了返回数组的类型，0 是为了节省空间，因为它只是为了说明返回的类型。详见：https://shipilev.net/blog/2016/arrays-wisdom-ancients/

##### 不要在 foreach 循环里进行元素的 remove/add 操作(是个坑要注意，一定要记住)

如果要进行`remove`操作，可以==调用迭代器的 `remove` 方法而不是集合类的 `remove` 方法==。因为如果列表在任何时间从结构上修改创建迭代器之后，以任何方式除非通过迭代器自身`remove/add`方法，迭代器都将抛出一个`ConcurrentModificationException`,这就是单线程状态下产生的**fail-fast** 机制。

```
fail-fast:多个线程对 fail-fast 集合进行修改的时，可能会抛出ConcurrentModificationException，单线程下也会出现这种情况，上面已经提到过
```

`java.util`包下面的所有的集合类都是`fail-fast`的，而`java.util.concurrent`包下面的所有的类都是`fail-safe`的。
![fail-fast.png](assserts/fail-fast.png)

```java
ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
//      //迭代器方式,正确
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()){
//            String s = iterator.next();
//            if (s.equals("1")){
//                iterator.remove();
//            }
//        }
失败的方式：Exception in thread "main" java.util ConcurrentModificationException

        for (String item:list){
            if (item.equals("2")){///神奇的是，删除第二个位置是可以成功的
                list.remove(item);
            }
        }
//这种方式也是正确的，即，只是不能用foreach的形式。
for (int i=0;i<list.size();i++){
            if (list.get(i).equals("1")){
                list.remove(list.get(i));
            }
        }

        System.out.println(list);
    }
```
