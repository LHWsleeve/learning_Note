- [redis 简介](#redis-简介)
  - [redis 优点](#redis-优点)
- [Redis 五种基本数据结构](#redis-五种基本数据结构)
- [为什么要用 redis/为什么要用缓存](#为什么要用-redis为什么要用缓存)
- [为什么要用 redis 而不用 map/guava 做缓存?](#为什么要用-redis-而不用-mapguava-做缓存)
- [redis 的线程模型](#redis-的线程模型)
- [redis 和 memcached 的区别!](#redis-和-memcached-的区别)
- [redis 常见数据结构以及使用场景分析](#redis-常见数据结构以及使用场景分析)
  - [1.String](#1string)
  - [2.字典 Hash](#2字典-hash)
  - [3.List](#3list)
  - [4.Set](#4set)
  - [5.Sorted Set(Zset)](#5sorted-setzset)
- [redis 设置过期时间](#redis-设置过期时间)
- [redis 内存淘汰机制(MySQL 里有 2000w 数据，Redis 中只存 20w 的数据，如何保证 Redis 中的数据都是热点数据?)](#redis-内存淘汰机制mysql-里有-2000w-数据redis-中只存-20w-的数据如何保证-redis-中的数据都是热点数据)
- [redis 持久化机制(==怎么保证 redis 挂掉之后再重启数据可以进行恢复==)](#redis-持久化机制怎么保证-redis-挂掉之后再重启数据可以进行恢复)
  - [redis 事务](#redis-事务)
- [缓存雪崩和缓存穿透问题解决方案](#缓存雪崩和缓存穿透问题解决方案)
  - [**缓存雪崩**](#缓存雪崩)
  - [**缓存穿透**](#缓存穿透)
- [如何解决 Redis 的并发竞争 Key 问题](#如何解决-redis-的并发竞争-key-问题)
- [如何保证缓存与数据库双写时的数据一致性?](#如何保证缓存与数据库双写时的数据一致性)
- [跳跃表](#跳跃表)
- [Redis使用场景](#redis使用场景)
- [参考](#参考)

<!-- /TOC -->

## redis 简介

简单来说 redis 就是一个数据库，不过与传统数据库不同的是 redis 的数据是==存在内存==中的，所以读写速度非常快，因此 redis 被广泛应用于缓存方向。另外，redis 也经常用来做分布式锁。redis 提供了多种数据类型来支持不同的业务场景。除此之外，redis 支持事务 、持久化、LUA 脚本、LRU 驱动事件、多种集群方案。

### redis 优点

1. 异常快 - Redis 非常快，每秒可执行大约 110000 次的设置(SET)操作，每秒大约可执行 81000 次的读取/获取(GET)操作。
2. 支持丰富的数据类型 - Redis 支持开发人员常用的大多数数据类型，例如列表，集合，排序集和散列等等。这使得 Redis 很容易被用来解决各种问题，因为我们知道哪些问题可以更好使用地哪些数据类型来处理解决。
3. 操作具有原子性 - ==所有 Redis 操作都是原子操作==，这确保如果两个客户端并发访问，Redis 服务器能接收更新的值。
4. 多实用工具 - Redis 是一个多实用工具，可用于多种用例，如：缓存，消息队列(Redis 本地支持发布/订阅)，应用程序中的任何短期数据，例如，web 应用程序中的会话，网页命中计数等。

## Redis 五种基本数据结构

## 为什么要用 redis/为什么要用缓存

主要从“高性能”和“高并发”这两点来看待这个问题。

**高性能：**

假如用户第一次访问数据库中的某些数据。这个过程会比较慢，因为是从硬盘上读取的。将该用户访问的数据存在缓存中，这样下一次再访问这些数据的时候就可以直接从缓存中获取了。操作缓存就是直接操作内存，所以速度相当快。如果数据库中的对应数据改变的之后，同步改变缓存中相应的数据即可！

![](http://my-blog-to-use.oss-cn-beijing.aliyuncs.com/18-9-24/54316596.jpg)

**注意：** ==数据库本身自带缓存功能==
1、mysql、oracle 这些关系数据库自带的缓存都是对==热数据进行自动存储及替换==，也是用的内存空间
(MySQL 缓存机制即缓存 sql 文本及缓存结果，用 KV 形式保存再服务器内存中，如果运行相同的 sql，服务器直接从缓存中去获取结果，不需要再去解析、优化、执行 sql)
2、redis、memcache 这类缓存系统是由开发人员==自由选择数据对象==，全部采用内存，也有些带持久化到磁盘存储机制

**优缺点：**

1. mysql 这类缓存是完全凭借数据库自行运作，没有开发量，但是也完全基于数据库服务器资源处理连接请求，==极其消耗 cpu 和内存甚至线程资源==，对一个中大型业务系统来说，**高并发请求全部放在数据库对业务系统是致命的**，除非你有无限量的硬件资源支持横向扩展，而这更加需要极高的成本，这无疑是极其浪费资源的。
2. redis 这类专门的缓存数据库都是基于快速响应、业务灵活变更的需求诞生的，主从或者集群模式也非常成熟，对于几千数万甚至更高并发的处理都不在话下，当然这也要看使用方法。针对业务特性使用不同的缓存方法才是最重要的，比如对实时性更新要求不高、静态数据这一类，所以总的来说要根据业务找最适用的系统！

**高并发：**

直接操作缓存能够承受的请求是远远大于直接访问数据库的，所以我们可以考虑把数据库中的部分数据转移到缓存中去，这样用户的一部分请求会直接到缓存这里而**不用经过数据库**。

![](http://my-blog-to-use.oss-cn-beijing.aliyuncs.com/18-9-24/85146760.jpg)

## 为什么要用 redis 而不用 map/guava 做缓存?

> 下面的内容来自 segmentfault 一位网友的提问，地址：https://segmentfault.com/q/1010000009106416

缓存分为**本地缓存**和**分布式缓存**。
以 Java 为例，使用自带的 map 或者 guava 实现的是本地缓存，最主要的特点是轻量以及快速，生命周期随着 jvm 的销毁而结束，并且在多实例的情况下，==每个实例都需要各自保存一份缓存，缓存不具有一致性==。

使用 redis 或 memcached 之类的称为分布式缓存，**在多实例的情况下，各实例共用一份缓存数据，缓存具有一致性**。缺点是需要保持 redis 或 memcached 服务的高可用，整个程序架构上较为复杂。

1. 对大部分分布式系统来说，缓存不适合用程序实现，比如 Java 访问数据库时，可以自建 HashMap/LinkedHashMap 用作缓存，可以用开源缓存类库如 EHCache，也可以用 Hibernate 自带的缓存，**但由于负载均衡的关系，多台机器上的这些缓存不便于共享，所以不如 Redis、Memcached 等集中式的缓存来得方便合高效**。

2. 其次，很多大型互联网公司的经验表明，数据库缓存带来的效率提升并不明显。因为更新频繁的表，缓存命中率很低；而指定数据库对哪些表用缓存、哪些表不用缓存，可能难度很大

## redis 的线程模型

> 参考地址:https://www.javazhiyin.com/22943.html

redis 内部使用文件事件处理器 `file event handler`，这个文件事件处理器是**单线程**的，所以 redis 才叫做单线程的模型。它采用 ==IO 多路复用机制同时监听多个 socket，根据 socket 上的事件来选择对应的事件处理器进行处理==。

文件事件处理器的结构包含 4 个部分：

- 多个 socket
- IO 多路复用程序
- 文件事件分派器
- 事件处理器（连接应答处理器、命令请求处理器、命令回复处理器）

多个 socket 可能会并发产生不同的操作，每个操作对应不同的文件事件，但是 IO 多路复用程序会监听多个 socket，会将 socket 产生的事件放入队列中排队，事件分派器每次从队列中取出一个事件，把该事件交给对应的事件处理器进行处理。

## redis 和 memcached 的区别!

对于 redis 和 memcached 我总结了下面四点。现在公司一般都是用 redis 来实现缓存，而且 redis 自身也越来越强大了！

1. **redis 支持更丰富的数据类型（支持更复杂的应用场景）**
   >Redis 不仅仅支持简单的 k/v 类型的数据，同时还提供 list，set，zset，hash 等数据结构的存储。memcache 支持简单的数据类型，String。
2. **Redis 支持数据的持久化**
   >Redis 支持两种持久化策略：RDB 快照和 AOF 日志，而 Memcached 不支持持久化。
3. **内存管理机制**
   >在 Redis 中，并不是所有数据都一直存储在内存中，可以将一些很久没用的 value 交换到磁盘，而 Memcached 的数据则会一直在内存中。
   >Memcached 将内存分割成特定长度的块来存储数据，以完全解决内存碎片的问题。但是这种方式会使得内存的利用率不高，例如块的大小为 128 bytes，只存储 100 bytes 的数据，那么剩下的 28 bytes 就浪费掉了。
4. **集群模式**：
   >memcached 没有原生的集群模式，需要依靠客户端来实现往集群中分片写入数据；但是 redis 目前是原生支持 cluster 模式的.
5. **Memcached 是多线程，非阻塞 IO 复用的网络模型；Redis 使用单线程的多路 IO 复用模型。**


![redis 和 memcached 的区别](http://my-blog-to-use.oss-cn-beijing.aliyuncs.com/18-9-24/61603179.jpg)

## redis 常见数据结构以及使用场景分析

Redis 有 5 种基础数据结构，它们分别是：string(字符串)、list(列表)、hash(字典)、set(集合) 和 zset(有序集合)。
| 数据类型 | 可以存储的值 | 操作 |
| :--: | :--: | :--: |
| STRING | 字符串、整数或者浮点数 | 对整个字符串或者字符串的其中一部分执行操作</br> 对整数和浮点数执行自增或者自减操作 |
| LIST | 列表 | 从两端压入或者弹出元素 </br> 对单个或者多个元素进行修剪，</br> 只保留一个范围内的元素 |
| SET | 无序集合 | 添加、获取、移除单个元素</br> 检查一个元素是否存在于集合中</br> 计算交集、并集、差集</br> 从集合里面随机获取元素 |
| HASH | 包含键值对的无序散列表 | 添加、获取、移除单个键值对</br> 获取所有键值对</br> 检查某个键是否存在|
| ZSET | 有序集合 | 添加、获取、删除元素</br> 根据分值范围或者成员来获取元素</br> 计算一个键的排名 |


### 1.String

Redis 中的字符串是一种 **动态字符串**，这意味着使用者可以修改，它的底层实现有点类似于 Java 中的 ArrayList，有一个字符数组，从源码的 **sds.h/sdshdr 文件** 中可以看到 Redis 底层对于字符串的定义 SDS，即 Simple Dynamic String 结构：

```c
/* Note: sdshdr5 is never used, we just access the flags byte directly.
 * However is here to document the layout of type 5 SDS strings. */
struct __attribute__ ((__packed__)) sdshdr5 {
    unsigned char flags; /* 3 lsb of type, and 5 msb of string length */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr8 {
    uint8_t len; /* used */
    uint8_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr16 {
    uint16_t len; /* used */
    uint16_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr32 {
    uint32_t len; /* used */
    uint32_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr64 {
    uint64_t len; /* used */
    uint64_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
```

**为什么不直接使用 int 类型呢？**

因为当字符串比较短的时候，len 和 alloc 可以使用 byte 和 short 来表示，**Redis 为了对内存做极致的优化，不同长度的字符串使用不同的结构体来表示。**
**SDS 与 C 字符串的区别**
C 语言这种简单的字符串表示方式 不符合 Redis 对字符串在安全性、效率以及功能方面的要求,可能会造成如下问题：

- **获取字符串长度为 O(N) 级别的操作** → 因为 C 不保存数组的长度，每次都需要遍历一遍整个数组；
- **不能很好的杜绝 缓冲区溢出/内存泄漏 的问题** → 跟上述问题原因一样，如果执行拼接 or 缩短字符串的操作，如果操作不当就很容易造成上述问题；
- **C 字符串 只能保存文本数据** → 因为 C 语言中的字符串必须符合某种编码（比如 ASCII），例如中间出现的 '\0' 可能会被判定为提前结束的字符串而识别不了；

> **常用命令:** set,get,decr,incr,mget 等。

String 数据结构是简单的 key-value 类型，value 其实不仅可以是 String，也可以是数字。
常规 key-value 缓存应用；
常规计数：微博数，粉丝数等。

### 2.字典 Hash

> **常用命令：** hget,hset,hgetall 等。

Redis 中的字典相当于 Java 中的 **HashMap，**内部实现也差不多类似，都是通过 "**数组 + 链表**" 的链地址法来解决部分**哈希冲突**，同时这样的结构也吸收了两种不同数据结构的优点。源码定义如 dict.h/dictht 定义：

hash 是一个 string 类型的 field 和 value 的映射表，**hash 特别适合用于存储对象**，后续操作的时候，你可以直接仅仅修改这个对象中的某个字段的值。 比如我们可以 hash 数据结构来存储用户信息，商品信息等等：

```
key=JavaUser293847
value={
  “id”: 1,
  “name”: “SnailClimb”,
  “age”: 22,
  “location”: “Wuhan, Hubei”
}

```

Redis 的字典 dict 中包含**两个哈希表 dictht**，这是为了方便进行 rehash 操作。在扩容时，将其中一个 dictht 上的键值对 rehash 到另一个 dictht 上面，完成之后释放空间并交换两个 dictht 的角色。

**rehash 操作不是一次性完成，而是采用渐进方式，这是为了避免一次性执行过多的 rehash 操作给服务器带来过大的负担。**

渐进式 rehash 通过记录 dict 的 rehashidx 完成，它从 0 开始，然后每执行一次 rehash 都会递增。例如在一次 rehash 中，要把 dict[0] rehash 到 dict[1]，这一次会把 dict[0] 上 table[rehashidx] 的键值对 rehash 到 dict[1] 上，dict[0] 的 table[rehashidx] 指向 null，并令 rehashidx++。

在 rehash 期间，每次对字典执行添加、删除、查找或者更新操作时，都会执行一次渐进式 rehash。

采用渐进式 rehash 会导致字典中的数据分散在两个 dictht 上，因此对字典的查找操作也需要到对应的 dictht 去执行。

### 3.List

> **常用命令:** lpush,rpush,lpop,rpop,lrange 等

Redis 的列表相当于 Java 语言中的 **LinkedList**，注意是链表而不是数组。**这意味着 list 的插入和删除操作非常快，时间复杂度为 O(1)，但是索引定位很慢，时间复杂度为 O(n)。**
Redis list 的应用场景非常多，也是 Redis 最重要的数据结构之一，比如微博的关注列表，粉丝列表，消息列表等功能都可以用 Redis 的 list 结构来实现。

Redis list 的实现为一个**双向链表，即可以支持反向查找和遍**历，更方便操作，不过带来了部分额外的内存开销。

另外可以通过 lrange 命令，就是从某个元素开始读取多少个元素，**可以基于 list 实现分页查询，这个很棒的一个功能，基于 redis 实现简单的高性能分页**，可以做类似微博那种下拉不断分页的东西（一页一页的往下走），性能高。

### 4.Set

> **常用命令：**
> sadd,spop,smembers,sunion 等
> Redis 的集合**相当于 Java 语言中的 HashSet**，它内部的键值对是无序、唯一的。它的内部实现相当于一个特殊的字典，字典中所有的 value 都是一个值 NULL。
> set 对外提供的功能与 list 类似是一个列表的功能，特殊之处在于 set 是可以**自动排重**的。

当你需要存储一个列表数据，又不希望出现重复数据时，set 是一个很好的选择，并且 set 提供了**判断某个成员是否在一个 set 集合内**的重要接口，这个也是 list 所不能提供的。可以基于 set 轻易实现**交集、并集、差集**的操作。

比如：在微博应用中，可以将一个用户所有的关注人存在一个集合中，将其所有粉丝存在一个集合。Redis 可以非常方便的实现如共同关注、共同粉丝、共同喜好等功能。这个过程也就是求交集的过程，具体命令如下：

```
sinterstore key1 key2 key3     将交集存在key1内
```

### 5.Sorted Set(Zset)

> **常用命令：** zadd,zrange,zrem,zcard 等

和 set 相比，sorted set 增加了一个权重参数 score，使得集合中的元素能够按 **score 进行有序排列**。
类似于 Java 中 SortedSet 和 HashMap 的结合体，一方面它是一个 set，保证了内部 value 的唯一性，另一方面它可以为每个 value 赋予一个 score 值，用来代表排序的权重。

内部实现用的是一种叫做 **「跳跃表」** 的数据结构--见目录，详解。

**举例：** 在直播系统中，实时排行信息包含直播间在线用户列表，各种礼物排行榜，弹幕消息（可以理解为按消息维度的消息排行榜）等信息，适合使用 Redis 中的 Sorted Set 结构进行存储。

## redis 设置过期时间

Redis 中有个设置时间过期的功能，即对存储在 redis 数据库中的值可以设置一个过期时间。==作为一个缓存数据库，这是非常实用的。如我们一般项目中的 token 或者一些登录信息，尤其是短信验证码都是有时间限制的，按照传统的数据库处理方式，一般都是自己判断过期，这样无疑会严重影响项目性能。==

我们 set key 的时候，都可以给一个 expire time，就是过期时间，通过过期时间我们可以指定这个 key 可以存活的时间。

如果假设你设置了一批 key 只能存活 1 个小时，那么接下来 1 小时后，redis 是怎么对这批 key 进行删除的？

**定期删除+惰性删除。**

通过名字大概就能猜出这两个删除方式的意思了。

- **定期删除**：redis 默认是每隔 100ms 就**随机抽取**一些设置了过期时间的 key，检查其是否过期，如果过期就删除。注意这里是随机抽取的。为什么要随机呢？你想一想假如 redis 存了几十万个 key ，每隔 100ms 就遍历所有的设置过期时间的 key 的话，就会给 CPU 带来很大的负载！
- **惰性删除** ：定期删除可能会导致很多过期 key 到了时间并没有被删除掉。所以就有了惰性删除。假如你的过期 key，靠定期删除没有被删除掉，还停留在内存里，除非你的系统去查一下那个 key，才会被 redis 给删除掉。这就是所谓的惰性删除，也是够懒的哈！

但是仅仅通过设置过期时间还是有问题的。我们想一下：如果定期删除漏掉了很多过期 key，然后你也没及时去查，也就没走惰性删除，此时会怎么样？如果大量过期 key 堆积在内存里，导致 redis 内存块耗尽了。怎么解决这个问题呢？ ==**redis 内存淘汰机制。**==

## redis 内存淘汰机制(MySQL 里有 2000w 数据，Redis 中只存 20w 的数据，如何保证 Redis 中的数据都是热点数据?)

redis 配置文件 redis.conf 中有相关注释，我这里就不贴了，大家可以自行查阅或者通过这个网址查看： [http://download.redis.io/redis-stable/redis.conf](http://download.redis.io/redis-stable/redis.conf)

**redis 提供 6 种数据淘汰策略：**
策略|描述
-|-|-
**volatile-lru**|从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
**volatile-ttl**|从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
**volatile-random**|从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
**allkeys-lru**|当内存不足以容纳新写入数据时，在键空间中，移除最近最少使用的 key（==这个是最常用的==）
**allkeys-random**|从数据集（server.db[i].dict）中任意选择数据淘汰
**no-eviction**|禁止驱逐数据，也就是说当内存不足以容纳新写入数据时，新写入操作会报错。这个应该没人使用吧！

4.0 版本后增加以下两种：

1. **volatile-lfu**：从已设置过期时间的数据集(server.db[i].expires)中挑选最不经常使用的数据淘汰
2. **allkeys-lfu**：当内存不足以容纳新写入数据时，在键空间中，移除最不经常使用的 key
   
LFU 策略通过统计访问频率，将访问频率最少的键值对淘汰


## redis 持久化机制(==怎么保证 redis 挂掉之后再重启数据可以进行恢复==)

很多时候我们需要持久化数据也就是将内存中的数据写入到硬盘里面，大部分原因是为了之后重用数据（比如重启机器、机器故障之后恢复数据），或者是为了防止系统故障而将数据备份到一个远程位置。

Redis 不同于 Memcached 的很重一点就是，Redis 支持持久化，而且支持两种不同的持久化操作。**Redis 的一种持久化方式叫快照（snapshotting，RDB），另一种方式是只追加文件（append-only file,AOF）**。这两种方法各有千秋，下面我会详细这两种持久化方法是什么，怎么用，如何选择适合自己的持久化方法。

1. **快照（snapshotting）持久化（RDB）**

Redis 可以通过创建快照来获得存储在内存里面的数据在某个时间点上的副本。Redis 创建快照之后，可以对**快照进行备份**，可以将快照**复制到其他服务器**从而创建具有相同数据的服务器副本（Redis 主从结构，主要用来提高 Redis 性能），还可以将**快照留在原地以便重启服务器的时候使用**。

>将某个时间点的所有数据都存放到硬盘上。
可以将快照复制到其它服务器从而创建具有相同数据的服务器副本。
如果系统发生故障，将会丢失最后一次创建快照之后的数据。
如果数据量很大，保存快照的时间会很长。

快照持久化是 Redis ==默认==采用的持久化方式，在 redis.conf 配置文件中默认有此下配置：
```conf

save 900 1           #在900秒(15分钟)之后，如果至少有1个key发生变化，Redis就会自动触发BGSAVE命令创建快照。

save 300 10          #在300秒(5分钟)之后，如果至少有10个key发生变化，Redis就会自动触发BGSAVE命令创建快照。

save 60 10000        #在60秒(1分钟)之后，如果至少有10000个key发生变化，Redis就会自动触发BGSAVE命令创建快照。
```

2. **AOF（append-only file）持久化**

与快照持久化相比，AOF 持久化 的**实时性**更好，因此已成为主流的持久化方案。默认情况下 Redis 没有开启 AOF（append only file）方式的持久化，可以通过 appendonly 参数开启：

```conf
appendonly yes
```

开启 AOF 持久化后**每执行一条会更改 Redis 中的数据的命令，Redis 就会将该命令写入硬盘中的 AOF 文件**。AOF 文件的保存位置和 RDB 文件的**位置相同**，都是通过 dir 参数设置的，默认的文件名是 appendonly.aof。

使用 AOF 持久化需要设置同步选项，从而确保写命令同步到磁盘文件上的时机。这是因为**对文件进行写入并不会马上将内容同步到磁盘上，而是先存储到缓冲区，然后由操作系统决定什么时候同步到磁盘**。有以下同步选项：

选项|同步频率
-|-|-
appendfsync always|    每写一个命令都同步
appendfsync everysec|  每秒同步一次
appendfsync no   |     操作系统来决定何时同步

- always选项会严重降低服务器性能
- everysec 比较适合，可以保证系统崩溃时只会丢失1s左右的数据，并且Redis每秒执行一次同步对服务器性能几乎没有任何影响
- no 选型不能带来服务器性能提升，会增加系统崩溃时丢失的数据量

3. **Redis 4.0 对于持久化机制的优化**

Redis 4.0 开始支持 RDB 和 AOF 的混合持久化（默认关闭，可以通过配置项 `aof-use-rdb-preamble` 开启）。

如果把混合持久化打开，AOF 重写的时候就直接把 RDB 的内容写到 AOF 文件开头。这样做的好处是可以结合 RDB 和 AOF 的优点, 快速加载同时避免丢失过多的数据。当然缺点也是有的， AOF 里面的 RDB 部分是压缩格式不再是 AOF 格式，可读性较差。
>**补充内容：AOF 重写**
AOF 重写可以产生一个新的 AOF 文件，这个新的 AOF 文件和原有的 AOF 文件所保存的数据库状态一样，但体积更小。
AOF 重写是一个有歧义的名字，**该功能是通过读取数据库中的键值对来实现的，程序无须对现有 AOF 文件进行任何读入、分析或者写入操作**。

在执行 BGREWRITEAOF 命令时，Redis 服务器会维护一个 AOF 重写缓冲区，该缓冲区会在子进程创建新 AOF 文件期间，记录服务器执行的所有写命令。当子进程完成创建新 AOF 文件的工作之后，服务器会将重写缓冲区中的所有内容追加到新 AOF 文件的末尾，使得新旧两个 AOF 文件所保存的数据库状态一致。最后，服务器用新的 AOF 文件替换旧的 AOF 文件，以此来完成 AOF 文件重写操作

- [Redis 持久化](Redis持久化.md)

### redis 事务

Redis 通过 MULTI、EXEC、WATCH 等命令来实现事务(transaction)功能。事务提供了一种将多个命令请求打包，然后一次性、按顺序地执行多个命令的机制，并且在事务执行期间，服务器不会中断事务而改去执行其他客户端的命令请求，它会将事务中的所有命令都执行完毕，然后才去处理其他客户端的命令请求。

在传统的关系式数据库中，常常用 ACID 性质来检验事务功能的可靠性和安全性。在 Redis 中，事务总是具有原子性（Atomicity）、一致性（Consistency）和隔离性（Isolation），并且当 Redis 运行在某种特定的持久化模式下时，事务也具有持久性（Durability）。

补充内容：

> 1. ==redis 同一个事务中如果有一条命令执行失败，其后的命令仍然会被执行，没有回滚==。

2. Redis 在执行事务命令的时候，在命令入队的时候， Redis 就会检测事务的命令是否正确，如果不正确则会产生错误。无论之前和之后的命令都会被事务所回滚，就变为什么都没有执行。
3. 当命令格式正确，而因为操作数据结构引起的错误 ，则该命令执行出现错误，而其之前和之后的命令都会被正常执行。这点和数据库很不一样，这是需注意的地方。

（来自[issue:关于 Redis 事务不是原子性问题](https://github.com/Snailclimb/JavaGuide/issues/452) ）

## 缓存雪崩和缓存穿透问题解决方案

### **缓存雪崩**

**什么是缓存雪崩？**

简介：缓存同一时间大面积的失效，所以，后面的请求都会落到数据库上，造成数据库短时间内承受大量请求而崩掉。

**有哪些解决办法？**

（中华石杉老师在他的视频中提到过，视频地址在最后一个问题中有提到）：

- 事前：尽量保证整个 redis 集群的高可用性，发现机器宕机尽快补上。选择合适的内存淘汰策略。
- 事中：本地 ehcache 缓存 + hystrix 限流&降级，避免 MySQL 崩掉
- 事后：利用 redis 持久化机制保存的数据尽快恢复缓存

![](http://my-blog-to-use.oss-cn-beijing.aliyuncs.com/18-9-25/6078367.jpg)

### **缓存穿透**

**什么是缓存穿透？**

缓存穿透说简单点就是大量请求的 key 根本不存在于缓存中，导致请求直接到了数据库上，根本没有经过缓存这一层。举个例子：某个黑客故意制造我们缓存中不存在的 key 发起大量请求，导致大量请求落到数据库。下面用图片展示一下(这两张图片不是我画的，为了省事直接在网上找的，这里说明一下)：

**正常缓存处理流程：**

<img src="https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-11/正常缓存处理流程-redis.png" style="zoom:50%;" />

**缓存穿透情况处理流程：**

<img src="https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-11/缓存穿透处理流程-redis.png" style="zoom:50%;" />

一般 MySQL 默认的最大连接数在 150 左右，这个可以通过 `show variables like '%max_connections%';`命令来查看。最大连接数一个还只是一个指标，cpu，内存，磁盘，网络等无力条件都是其运行指标，这些指标都会限制其并发能力！所以，一般 3000 个并发请求就能打死大部分数据库了。

**有哪些解决办法？**

最基本的就是**首先做好参数校验**，一些**不合法的参数请求直接抛出异常信息返回给客户端**。比如查询的数据库 id 不能小于 0、传入的邮箱格式不对的时候直接返回错误消息给客户端等等。

**1）缓存无效 key** : 如果缓存和数据库都查不到某个 key 的数据就写一个到 redis 中去并设置过期时间，具体命令如下：`SET key value EX 10086`。这种**方式可以解决请求的 key 变化不频繁的情况**，如果黑客恶意攻击，每次构建不同的请求 key，会导致 redis 中缓存大量无效的 key 。很明显，这种方案并不能从根本上解决此问题。如果非要用这种方式来解决穿透问题的话，尽量将无效的 key 的过期时间设置短一点比如 1 分钟。

另外，这里多说一嘴，一般情况下我们是这样设计 key 的： `表名:列名:主键名:主键值`。

如果用 Java 代码展示的话，差不多是下面这样的：

```java
public Object getObjectInclNullById(Integer id) {
    // 从缓存中获取数据
    Object cacheValue = cache.get(id);
    // 缓存为空
    if (cacheValue == null) {
        // 从数据库中获取
        Object storageValue = storage.get(key);
        // 缓存空对象
        cache.set(key, storageValue);
        // 如果存储数据为空，需要设置一个过期时间(300秒)
        if (storageValue == null) {
            // 必须设置过期时间，否则有被攻击的风险
            cache.expire(key, 60 * 5);
        }
        return storageValue;
    }
    return cacheValue;
}
```

**2）布隆过滤器：** 布隆过滤器是一个非常神奇的数据结构，通过它我们可以非常方便地判断一个给定数据是否存在与海量数据中。我们需要的就是判断 key 是否合法，有没有感觉布隆过滤器就是我们想要找的那个“人”。具体是这样做的：把所有可能存在的请求的值都存放在布隆过滤器中，当用户请求过来，我会先判断用户发来的请求的值是否存在于布隆过滤器中。不存在的话，直接返回请求参数错误信息给客户端，存在的话才会走下面的流程。总结一下就是下面这张图(这张图片不是我画的，为了省事直接在网上找的)：
**布隆过滤器说某个元素存在，小概率会误判。布隆过滤器说某个元素不在，那么这个元素一定不在**

<img src="https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-11/布隆过滤器-缓存穿透-redis.png" style="zoom:50%;" />

更多关于布隆过滤器的内容可以看我的这篇原创：[《不了解布隆过滤器？一文给你整的明明白白！》](https://github.com/Snailclimb/JavaGuide/blob/master/docs/dataStructures-algorithms/data-structure/bloom-filter.md) ，强烈推荐，个人感觉网上应该找不到总结的这么明明白白的文章了。

## 如何解决 Redis 的并发竞争 Key 问题

所谓 Redis 的并发竞争 Key 的问题也就是多个系统同时对一个 key 进行操作，但是最后执行的顺序和我们期望的顺序不同，这样也就导致了结果的不同！

推荐一种方案：分布式锁（zookeeper 和 redis 都可以实现分布式锁）。（如果不存在 Redis 的并发竞争 Key 问题，不要使用分布式锁，这样会影响性能）

基于 zookeeper 临时有序节点可以实现的分布式锁。大致思想为：每个客户端对某个方法加锁时，在 zookeeper 上的与该方法对应的指定节点的目录下，生成一个唯一的瞬时有序节点。 判断是否获取锁的方式很简单，只需要判断有序节点中序号最小的一个。 当释放锁的时候，只需将这个瞬时节点删除即可。同时，其可以避免服务宕机导致的锁无法释放，而产生的死锁问题。完成业务流程后，删除对应的子节点释放锁。

在实践中，当然是从以可靠性为主。所以首推 Zookeeper。

参考：

- https://www.jianshu.com/p/8bddd381de06

## 如何保证缓存与数据库双写时的数据一致性?

> 一般情况下我们都是这样使用缓存的：先读缓存，缓存没有的话，就读数据库，然后取出数据后放入缓存，同时返回响应。这种方式很明显会存在缓存和数据库的数据不一致的情况。

你只要用缓存，就可能会涉及到缓存与数据库双存储双写，你只要是双写，就一定会有数据一致性的问题，那么你如何解决一致性问题？

一般来说，就是如果你的系统不是严格要求缓存+数据库必须一致性的话，缓存可以稍微的跟数据库偶尔有不一致的情况，最好不要做这个方案,**读请求和写请求串行化，串到一个内存队列里去，这样就可以保证一定不会出现不一致的情况**

串行化之后，就==会导致系统的吞吐量会大幅度的降低==，用比正常情况下多几倍的机器去支撑线上的一个请求。

更多内容可以查看：https://github.com/doocs/advanced-java/blob/master/docs/high-concurrency/redis-consistence.md

## 跳跃表
跳跃表是由多层有序链表组合而成的，最底一层的链表保存了所有的数据，每向上的一层链表依次保存了下一层链表的部分数据。

跳跃表是有序集合的底层实现之一，基于多指针有序链表实现的，可以看成多个有序链表。
![asserts/1.png](asserts/1.png)
在查找时，从上层指针开始查找，找到对应的区间之后再到下一层去查找。下图演示了查找 22 的过程。
![asserts/1.png](asserts/2.png)
**跳跃表的插入流程**
1. 新节点和各层索引节点逐一比较，确定原链表的插入位置。O（logN）
2. 把索引插入到原链表。O（1）
3. 利用抛硬币的随机方式，决定新节点是否提升为上一级索引。结果为“正”则提升并继续抛硬币，结果为“负”则停止。O（logN）
**总体上，跳跃表插入操作的时间复杂度是O（logN），而这种数据结构所占空间是2N，既空间复杂度是 O（N）**

**跳跃表删除的流程**
1. 自上而下，查找第一次出现节点的索引，并逐层找到每一层对应的节点。O（logN）
2. 删除每一层查找到的节点，如果该层只剩下1个节点，删除整个一层（原链表除外）。O（logN）

总体上，跳跃表删除操作的时间复杂度是O（logN）。

与红黑树等平衡树相比，跳跃表具有以下优点：

- 插入速度非常快速，因为不需要进行旋转等操作来维护平衡性；
- 更容易实现；
- 支持无锁操作。

## Redis使用场景
1. 计数器
   可以对String进行自增自减运算，从而实现计数器功能
   Redis这种内存行数据库的读写性能非常高，适合存储频繁读写的计数量
2. 缓存
   将热点数据放到内存中，设置内存的最大使用量以及淘汰策略来保证缓存的命中率
3. 查找表
   例如DNS记录就很适合Redis进行存储
   查找表和缓存类似，也是利用了Redis快速查找的特性。但是查找表的内容不能失效，而缓存的内容可以失效，因为缓存不作为可靠的数据来源。
4. 消息队列
   List是一个双向链表，可以通过lpush和rpop写入和读取消息
   不过最好使用消息中间件
5. 会话缓存
   可以使用redis来统一存储多台应用服务器的会话信息
   当应用服务器不在存储用户的会话信息，也就不再具有状态，一个用户可以请求任意一个应用服务器，从而更容易实现高可用性以及可伸缩性。
6. 分布式锁实现
   分布式场景下，无法使用单机环境下的锁来对多个节点上的进程进行同步
   可以==使用redis自带的SETNX命令实现分布式锁，先拿setnx来争抢锁，抢到之后，再用expire给锁加一个过期时间防止锁忘记了释放==。
   **另外还有官方提供的RedLock**..
   - [RedLock](RedLock.md)



**参考：** Java 工程师面试突击第 1 季（可能是史上最好的 Java 面试突击课程）
## 参考

- 《Redis 开发与运维》
- Redis 命令总结：http://redisdoc.com/string/set.html
