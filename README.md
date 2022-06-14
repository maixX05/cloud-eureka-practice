文章首发：[Spring Cloud Neflix之Eureka入门和实战](https://www.maishuren.top/archives/springcloudneflix-zhi-eureka-ru-men-he-shi-zha)

# spring cloud组件列表

| 组件名称  | 所属醒目               | 组件分类           |
| --------- | ---------------------- | ------------------ |
| Eureka    | spring-cloud-neflix    | 注册中心           |
| Zuul      | spring-cloud-nexflix   | 第一代网关         |
| Sidecar   | spring-cloud-nexflix   | 多语言             |
| Ribbon    | spring-cloud-nexflix   | 负载均衡           |
| Hystrix   | spring-cloud-nexflix   | 熔断器             |
| Turbine   | spring-cloud-nexflix   | 集群监控           |
| Feign     | spring-cloud-openfeign | 声明式HTTP客户端   |
| Consul    | spring-cloud-consul    | 注册中心           |
| Gateway   | spring-cloud-gateway   | 第二代网关         |
| Sleuth    | spring-cloud-sleuth    | 链路追踪           |
| Config    | spring-cloud-config    | 配置中心           |
| Bus       | spring-cloud-bus       | 总线               |
| Pipeline  | spring-cloud-pipelines | 部署管道           |
| Dataflow  | spring-cloud-dataflow  | 数据处理           |
| Nacos     | spring-cloud-alibaba   | 注册中心、配置中心 |
| Sentinel  | spring-cloud-alibaba   | 流量控制和服务降级 |
| Seata     | spring-cloud-alibaba   | 分布式事务         |
| Dubbo RPC | spring-cloud-alibaba   | RPC                |

spring cloud和spring boot的版本对应

| Spring Cloud版本 |         Spring Boot版本          |
| :--------------: | :------------------------------: |
|      Hoxton      | 2.2.x, 2.3.x (Starting with SR5) |
|    Greenwich     |              2.1.x               |
|     Finchley     |              2.0.x               |
|     Edgware      |              1.5.x               |
|     Dalston      |              1.5.x               |

# Eureka入门案例

1. 创建Maven父级pom工程

```xml
	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.3.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <spring.cloud-version>Hoxton.SR3</spring.cloud-version>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring.cloud-version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

2.在父级工程下创建Eureka Server子模块

```xml
   		<dependencies>
           <dependency>
               <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
           </dependency>
       </dependencies>
   
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
               </plugin>
           </plugins>
       </build>
```

启动类

```java
package com.msr.better.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}

```

配置文件

application.yml(**单机版**)

```yaml
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

3.在父级工程下创建Eureka Client子模块

```xml
   		<dependencies>
           <dependency>
            <groupId>org.springframework.cloud</groupId>
               <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        	</dependency>
       </dependencies>
   
       <build>
           <plugins>
               <plugin>
                   <groupId>org.springframework.boot</groupId>
                   <artifactId>spring-boot-maven-plugin</artifactId>
               </plugin>
           </plugins>
    	</build>
```

启动类

   ```java
   package com.msr.better.client;
   
   import org.springframework.boot.SpringApplication;
   import org.springframework.boot.autoconfigure.SpringBootApplication;
   import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
   
   @EnableDiscoveryClient
   @SpringBootApplication
   public class ServiceApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(ServiceApplication.class, args);
       }
   }
   ```

配置文件：`resources/application.yml`

   ```yaml
   server:
     port: 8081
   
   spring:
     application:
       name: cloud-eureka-client1
   
   eureka:
     client:
       serviceUrl:
         defaultZone: http://localhost:8761/eureka/ # 一个Eureka Server
   ```

4.演示效果

启动Eureka Server和Eureka Client。在浏览器访问http://localhost:8761。可以看到有一个Eureka Client成功注册到Eureka Server中来了。

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/springcloud/eureka1.jpg)

访问Eureka Server的REST API接口：http://localhost:8761/eureka/apps，返回结果如下。

```xml
<applications> 
  <versions__delta>1</versions__delta>  
  <apps__hashcode>UP_1_</apps__hashcode>  
  <application> 
    <name>CLOUD-EUREKA-CLIENT1</name>  
    <instance> 
      <instanceId>DESTOP-IUCSN852:cloud-eureka-client1:8081</instanceId>  
      <hostName>DESTOP-IUCSN852</hostName>  
      <app>CLOUD-EUREKA-CLIENT1</app>  
      <ipAddr>192.168.3.2</ipAddr>  
      <status>UP</status>  
      <overriddenstatus>UNKNOWN</overriddenstatus>  
      <port enabled="true">8081</port>  
      <securePort enabled="false">443</securePort>  
      <countryId>1</countryId>  
      <dataCenterInfo class="com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo"> 
        <name>MyOwn</name> 
      </dataCenterInfo>  
      <leaseInfo> 
        <renewalIntervalInSecs>30</renewalIntervalInSecs>  
        <durationInSecs>90</durationInSecs>  
        <registrationTimestamp>1612541898071</registrationTimestamp>  
        <lastRenewalTimestamp>1612541898071</lastRenewalTimestamp>  
        <evictionTimestamp>0</evictionTimestamp>  
        <serviceUpTimestamp>1612541898071</serviceUpTimestamp> 
      </leaseInfo>  
      <metadata> 
        <management.port>8081</management.port> 
      </metadata>  
      <homePageUrl>http://DESTOP-IUCSN852:8081/</homePageUrl>  
      <statusPageUrl>http://DESTOP-IUCSN852:8081/actuator/info</statusPageUrl>  
      <healthCheckUrl>http://DESTOP-IUCSN852:8081/actuator/health</healthCheckUrl>  
      <vipAddress>cloud-eureka-client1</vipAddress>  
      <secureVipAddress>cloud-eureka-client1</secureVipAddress>  
      <isCoordinatingDiscoveryServer>false</isCoordinatingDiscoveryServer>  
      <lastUpdatedTimestamp>1612541898071</lastUpdatedTimestamp>  
      <lastDirtyTimestamp>1612541897993</lastDirtyTimestamp>  
      <actionType>ADDED</actionType> 
    </instance> 
  </application> 
</applications>
```

# Eureka的Rest API简介

​		Eureka是Netflix公司开源的一款服务发现组件，该组件提供的服务发现可以为负载均衡、failover等提供支持。Eureka包括Eureka Server和Eureka Client。Eureka Server提供REST服务，Eureka Client则是Java编写的客户端。

## REST API列表

|             操作              |                           http动作                           |                   描述                   |
| :---------------------------: | :----------------------------------------------------------: | :--------------------------------------: |
|         注册新的实例          |                  POST /eureka/apps/{appId}                   |       可以输入json或xml格式的body        |
|         注销应用实例          |           DELETE /eureka/apps/{appId}/{instanceId}           |               成功返回200                |
|      向应用实例发送心跳       |            PUT /eureka/apps/{appId}/{instanceId}             | 成功返回200，如果instanceId不存在返回404 |
|         查询所有实例          |                       GET /eureka/apps                       |      成功返回200，输出json或xml格式      |
|      查询指定appId的实例      |                   GET /eureka/apps/{appId}                   |      成功返回200，输出json或xml格式      |
| 根据指定appId和instanceId查询 |            GET /eureka/apps/{appId}/{instanceId}             |      成功返回200，输出json或xml格式      |
|    根据指定instanceId查询     |              GET /eureka/instances/{instanceId}              |      成功返回200，输出json或xml格式      |
|         暂停应用实例          | PUT /eureka/apps/{appId}/{instanceId}/status?value=OUT_OF_SERVICE |         成功返回200，失败返回500         |
|         恢复应用实例          | DELETE /eureka/apps/{appId}/{instanceId}/status?value=UP(value参数可以不传) |         成功返回200，失败返回500         |
|          更新元数据           |   PUT /eureka/apps/{appId}/{instanceId}/metadata?key=value   |         成功返回200，失败返回500         |
|        根据vip地址查询        |                GET /eureka/vips/{vipAddress}                 |      成功返回200，输出json或xml格式      |
|       根据svip地址查询        |               GET /eureka/svips/{svipAddress}                |      成功返回200，输出json或xml格式      |



# Eureka核心类

InstanceInfo(com.netflix.appinfo.InstanceInfo)

```java
public class InstanceInfo {
    private static final String VERSION_UNKNOWN = "unknown";
    // 日志
    private static final Logger logger = LoggerFactory.getLogger(InstanceInfo.class);
    // 默认端口
    public static final int DEFAULT_PORT = 7001;
    // https默认端口
    public static final int DEFAULT_SECURE_PORT = 7002;
    public static final int DEFAULT_COUNTRY_ID = 1;
    // 实例id
    private volatile String instanceId;
    // 应用名
    private volatile String appName;
    // 应用所属群组
    @Auto
    private volatile String appGroupName;
    // IP地址
    private volatile String ipAddr;
    private static final String SID_DEFAULT = "na";
    // 被废弃的属性，默认为na
    /** @deprecated */
    @Deprecated
    private volatile String sid;
    // 端口号
    private volatile int port;
    // https端口号
    private volatile int securePort;
    // 应用实例的首页url
    @Auto
    private volatile String homePageUrl;
    // 应用实例的状态页的url
    @Auto
    private volatile String statusPageUrl;
    // 应用实例健康检查的url
    @Auto
    private volatile String healthCheckUrl;
    // 应用实例健康检查https的url
    @Auto
    private volatile String secureHealthCheckUrl;
    // 虚拟ip地址
    @Auto
    private volatile String vipAddress;
    // https的虚拟ip地址
    @Auto
    private volatile String secureVipAddress;
    // 
    @XStreamOmitField
    private String statusPageRelativeUrl;
    // 
    @XStreamOmitField
    private String statusPageExplicitUrl;
    // 
    @XStreamOmitField
    private String healthCheckRelativeUrl;
    // 
    @XStreamOmitField
    private String healthCheckSecureExplicitUrl;
    // 
    @XStreamOmitField
    private String vipAddressUnresolved;
    // 
    @XStreamOmitField
    private String secureVipAddressUnresolved;
    // 
    @XStreamOmitField
    private String healthCheckExplicitUrl;
    // 
    /** @deprecated */
    @Deprecated
    private volatile int countryId;
    // 是否开启https端口
    private volatile boolean isSecurePortEnabled;
    // 是否开启http端口
    private volatile boolean isUnsecurePortEnabled;
    // dataCenter信息，如Neflix或者Amazon或者MyOwn
    private volatile DataCenterInfo dataCenterInfo;
    // 主机名称
    private volatile String hostName;
    // 实例状态，如：UP，DOWN、STARTING、OUT_OFSERVICE、UNKNOWN
    private volatile InstanceInfo.InstanceStatus status;
    // 外界需要强制覆盖的状态值，默认为UNKNOWN
    private volatile InstanceInfo.InstanceStatus overriddenStatus;
    @XStreamOmitField
    private volatile boolean isInstanceInfoDirty;
    // 租约信息
    private volatile LeaseInfo leaseInfo;
    // 首先标识是否discoveryServer，其实标识改discoveryServer是否是响应你请求的实例
    @Auto
    private volatile Boolean isCoordinatingDiscoveryServer;
    // 应用实例的元数据信息
    @XStreamAlias("metadata")
    private volatile Map<String, String> metadata;
    // 状态信息最后更新时间
    @Auto
    private volatile Long lastUpdatedTimestamp;
    // 实例信息最新的过期时间，在Client端用于表示给实例信息是否与Eureka Server一致，在Server端用于多个Eureka Server的信息同步处理
    @Auto
    private volatile Long lastDirtyTimestamp;
    // 表示Eureka Server对改实例执行的操作，包括ADDED、MODIFIED、DELETED这三类
    @Auto
    private volatile InstanceInfo.ActionType actionType;
    // 在AWS的autoscaling group的名称
    @Auto
    private volatile String asgName;
    // 版本
    private String version;
    
    // 无参构造函数
    private InstanceInfo() {
        this.sid = "na";
        this.port = 7001;
        this.securePort = 7002;
        this.countryId = 1;
        this.isSecurePortEnabled = false;
        this.isUnsecurePortEnabled = true;
        this.status = InstanceInfo.InstanceStatus.UP;
        this.overriddenStatus = InstanceInfo.InstanceStatus.UNKNOWN;
        this.isInstanceInfoDirty = false;
        this.isCoordinatingDiscoveryServer = Boolean.FALSE;
        this.version = "unknown";
        this.metadata = new ConcurrentHashMap();
        this.lastUpdatedTimestamp = System.currentTimeMillis();
        this.lastDirtyTimestamp = this.lastUpdatedTimestamp;
    }
    
    // 其他省略...
    
}
```

租约信息类：LeaseInfo(com.netflix.appinfo.LeaseInfo)

```java
public class LeaseInfo {
    // 默认的续约周期时间，单位秒
    public static final int DEFAULT_LEASE_RENEWAL_INTERVAL = 30;
    // 默认的租约有效时长，单位秒
    public static final int DEFAULT_LEASE_DURATION = 90;
    // Client端续约的间隔周期
    private int renewalIntervalInSecs;
    // Client端需要设置的租约的有效时长
    private int durationInSecs;
    // Server端设置该租约的第一次注册时间
    private long registrationTimestamp;
    // Server端设置的该租约的最后一次续约时间
    private long lastRenewalTimestamp;
    // Server端设置的该租约被剔除的时间
    private long evictionTimestamp;
    // Server端设置的该服务实例标记为UP的时间
    private long serviceUpTimestamp;

    // 无参构造函数
    private LeaseInfo() {
        this.renewalIntervalInSecs = 30;
        this.durationInSecs = 90;
    }
}
```

​		service discovery的实例信息的抽象接口，约定了服务发现的实例应用有哪些通用的信息。

```java
public interface ServiceInstance {
    default String getInstanceId() {
        return null;
    }
	// 实例的id
    String getServiceId();
	// 实例的host
    String getHost();
	// 实例端口
    int getPort();
	// 实例是否开启了https
    boolean isSecure();
	// 实例uri地址
    URI getUri();
	// 元数据信息
    Map<String, String> getMetadata();
	// 实例的scheme
    default String getScheme() {
        return null;
    }
}
```

​		由于Spring Cloud Discovery适配了Zookeeper、Consul、Netflix Eureka、Nacos等注册中心，因此其ServiceInstance定义更为抽象和通用，而且采取的是定义方法的方式。Spring Cloud对该接口的实现类为EurekaRegistration(org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration)，EurekaRegistration实现了ServiceInstance接口。

InstanceStatus是InstanceInfo内的枚举类，用于标识服务实例的状态。

```java
public static enum InstanceStatus {
        UP,
        DOWN,
        STARTING,
        OUT_OF_SERVICE,
        UNKNOWN;

        private InstanceStatus() {
        }

        public static InstanceInfo.InstanceStatus toEnum(String s) {
            if (s != null) {
                try {
                    return valueOf(s.toUpperCase());
                } catch (IllegalArgumentException var2) {
                    InstanceInfo.logger.debug("illegal argument supplied to InstanceStatus.valueOf: {}, defaulting to {}", s, UNKNOWN);
                }
            }

            return UNKNOWN;
        }
    }

```

​		从定义可以看得出来，服务实例主要有UP、DOWN、STARTING、OUT_OFSERVICE、UNKNOWN这几个状态。其中OUT_OF_SERVICE标识停止服务，即停止接收请求，处于这个状态的服务实例将不会被路由到，经常用于升级部署的场景。

# 服务的核心操作

对于服务发现来说，围绕服务实例主要有一下几个重要的操作：

- 服务注册(register)
- 服务下线(cancel)
- 服务租约(renew)
- 服务剔除(evict)

围绕这几个功能，Eureka设计了几个核心操作类：

- com.netflix.eureka.lease.LeaseManager
- com.netflix.discovery.shared.LookupService
- com.netflix.eureka.registry.InstanceRegistry
- com.netflix.eureka.registry.AbstractInstanceRegistry
- com.netflix.eureka.registry.PeerAwareInstanceRegistryImpl

Spring CloudEureka在netflix Eureka的基础上，抽象或定义了如下核心类：

- org.springframework.cloud.netflix.eureka.server.InstanceRegistry
- org.springframework.cloud.client.serviceregistry.ServiceRegistry
- org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry
- org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration
- org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration
- org.springframework.cloud.netflix.eureka.EurekaClientConfigBean
- org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean

其中LeaseManager以及LookupService是Eureka关于服务发现相关的操作定义的接口。前者定义了服务写操作相关的方法，后者定义了查询操作相关的方法。

LeaseManager(com.netflix.eureka.lease.LeaseManager)

​		该接口定义了应用服务实例在服务中心的几个操作方法：register、cansel、renew、evict。

```java
public interface LeaseManager<T> {
    // 用于注册服务实例信息
    void register(T var1, int var2, boolean var3);
	// 用于删除服务实例信息
    boolean cancel(String var1, String var2, boolean var3);
	// 用于与Eureka Server进行心跳操作，维持租约
    boolean renew(String var1, String var2, boolean var3);
	// 服务端的一个方法，用于剔除租约过期的服务实例信息
    void evict();
}
```

LookupService(com.netflix.discovery.shared.LookupService)

该接口定义了Eureka Client从服务中心获取服务实例的查询方法。

```java
public interface LookupService<T> {
    Application getApplication(String var1);

    Applications getApplications();

    List<InstanceInfo> getInstancesById(String var1);

    InstanceInfo getNextServerFromEureka(String var1, boolean var2);
}
```

这个接口主要是给Client端使用的，其中定义了获取所有应用信息、根据应用id获取所有服务实例，以及根据visualHostname使用round-robin方式获取下一个服务实例。

# Eureka的设计理念

作为一个服务注册于发现中心，主要解决如下几个问题：

1. **服务实例如何注册到服务中心**

​		本质上就是在服务启动的时候，需要调用Eureka Server的REST API的register方法，去注册应用实例的信息。对于Java应用则是使用Eureka Client封装好的API去调用；对于Spring Cloud应用更加简单，可以使用spring-cloud-starter-netflix-eureka-client，基于Spring Boot的自动配置，自动帮你实现服务信息的注册。

2. **服务实例如何从服务中心剔除**

​		正常情况下服务实例在关闭应用的时候，应该通过狗子方法或其他生命周期回调方法去调用Eureka Server的REST API的de-register方法，来删除自身服务实例。另外对于服务实例挂掉或者其他异常情况没有及时删除自身信息的问题，Eureka Server要求Client端定时进行续约，也就是发送心跳，来证明服务实例是健康的，是可以调用的。如果租约超过一定时间没有进行续约操作，Eureka Server端会主动剔除。这一点Eureka Server采用的就是分布式应用里面经典的心跳模式。

3. **服务实例信息的一致性问题**

​		由于服务注册以及发现中心可能是单点的，其自身必有个集群。下面主要分为AP优于CP、Peer to Peer架构、Zone以及Region设计，SELF PRESERVATION设计四个方面。

## AP优于CP

分布式系统的CAP三个特性：

C(Consistency)：数据一致性，即数据在存在多个副本的情况下，可能由于网络、机器故障、软件系统等问题导致数据写入部分副本成功，部分副本失败，进而造成副本之间数据出现不一致的，存在冲突。满足一致性则要求对数据进行更新操作之后，多副本的数据保持一致。

A(Availability)：可靠性，在任何时候客户端对集群进行读写操作，求情都能正常响应，即在一定的延时延时内完成。

P(Partition Tolerance)：分区容错性，即发生通讯故障的时候，整个集群被分割为多个无法互相通信的分区是，集群仍然可用。

​		对于分布式系统来说，一般网络条件相对不可控，出现网络分区是不可避免的，因此系统必须具备分区容错性。在这个前提下，分布式系统的设计则在CP和AP之间进行选择。不能理解成CAP三者之间必须三选二，他们三者之间不是对等和可以互相替换的。在分布式领域中，P是一个客观存在的事实，不可绕过，所以P与AC之间不是对等关系。

​		对于ZooKeeper，它就是CP的，但又不是严格的强一致性，比如客户端A提交了一个写操作，ZooKeeper在板书节点操作成功之后返回，此时假设客户端B的读操作请求到的是A写操作尚未同步到的节点，那么读取到的就不是客户端A歇菜做成功之后的数据。如果在使用的时候需要强一致性，则需要在读取数据的使用执行以下sync操作，即leader节点先同步下数据，这样才能保证强一致。在极端的情况下发生网络分区的时候，如果leader节点不在non-quorum分区，那么对这个分区上节点的读写操作请求将会报错，无法满足Availability特性。

​		Eureka是在部署AWS的背景下设计的，其设计者认为，在云端特别是在大规模部署的情况下，失败是不可避免的，有可能是Eureka自身部署失败，注册的服务不可用，或者由于出现网络分区导致服务不可用，因此不能回避这个问题。所以要在出现了这些问题了，还要能继续提供服务注册以及发现功能，Eureka才选择满足Availability(可靠性)。所以Eureka会保留可用及过期的数据，总比丢掉可用的数据好。这样的话，应用实例的注册信息在集群中的所有节点并不是强一致的，这就需要客户端能够支持负载均衡以及失败重试。在Netflix的生态里面，有ribbon提供这一个功能。

## Peer to Peer架构

在分布式系统中数据存在过个副本之间的复制，复制方式可分为主从复制和对等复制。

1. **主从复制**

   主从复制也就是广为人知的Master-Slave模式，即有一个主副本，其他副本是从副本。所有数据的写操作都提交到主副本中，最后由主副本更新到其他从副本。有同步更新、异步更新、同步及异步混合。

2. **对等复制**

   ​        即Peer to Peer模式，由于任何副本都可以接收写操作，不存在写操作的压力瓶颈。但是由于每个副本都可以进行写操作处理，个副本之间的数据同步以及冲突处理是一个比较棘手的问题。

   ​        Eureka采用的就是Peer to Peer的复制模式。下面分为Client端和Server端两个角度。

   （1）Client端

   Client端一般通过下面的配置Eureka Server的peer节点：

   ```yaml
   eureka:
     client:
       serviceUrl:
         defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
   ```

   ​        实际上代码里支持preferSameZoneEureka，即有多个分区的话，优先选择与应用实例所在分区一样的其他服务实例，如果没有找到默认使用defaultZone。客户端使用quarantineSet维护一个不可用的Eureka Server列表，进行请求的时候，优先从可用的列表中进行选择，如果请求失败，则切换到下一Eureka Server实例进行充实，重试次数默认是3。

   ​        另外为了防止每个Client都按照上面配置的Server列表指定的顺序来访问，而造成Erueka Server节点请求分布不均的情况，Client端有一个定时任务(默认5分钟执行一次)来刷洗并随机化Eureka Server的列表。

   （2）Server端

   ​        Eureka Server本身就依赖了Eureka Client，因为每个Eureka Server是作为其他Eureka Server的Client。在单个Eureka Server启动的时候，会有一个syncUp的操作，通过Eureka Client请求其他Eureka Server节点中的一个节点获取注册的应用实例信息，然后复制到其他peer节点。

   ​        Eureka Server在执行复制操作的时候，使用HEADER_REPLICATION的http header来讲这个请求操作与普通的应用实例的正常操作区分开来。通过HEADER_REPLICATION来标识是复制请求，这样其他peer节点接收到请求的时候，就不会对它的peer节点进行复制操作，从而避免死循环。

   ​        Eureka Server使用peer ot peer的复制模式，重点解决的是另外一个问题：数据复制冲突。解决方法：

    - lastDirtyTimestamp标识
    - heartbeat

   ​        针对数据的不一致，一般是通过版本号机制来解决的，最后在不同的副本之间判断请求复制数据的版本号与本地数据的版本号的高低就行了。Eureka没有直接使用版本号的属性，而是使用lastDirtyTimestamp的字段来对比。

   如果开启了SyncWhenTimestampDiffers配置(默认开启)，当lastDirtyTimestamp不为空的时候，就会做相应的处理：

    - 如果请求参数的lastDirtyTimestamp值大于Server本地该实例的lastDirtyTimstamp值，则表示EurekaServer之间的数据出现冲突，这个时候就返回404，要求应用实例重新进行register操作。
    - 如果请求参数的lastDirtyTimestamp值小于Server本地该实例的lstDirtyTimestamp值，如果是peer节点的复制请求，则表示数据出现冲突，返回409给peer节点，要求其同步自己最新的数据信息。

   ​        peer节点之间的相互复制不能保证所有操作都成功，所以Eureka还要通过应用实例与Server之间的heartbeat也就是renewLease操作来进行数据的最终修复，如果发现应用实例数据与某个Server的数据出现不一致，则Server返回404，应用实例重新进行register操作。

## Zone及Region设计

​		Netflix的服务大部分在Amazon上，因此Eureka的设计有一部分也是基于Amazon的Zone及Region的基础设施之上。在AmazonEC2托管在全球的各个地方，它用Region来代表一个独立的地理区域，比如Eureka  Server默认设置了个Region : us-east-I、us - west-I、us-west-2、eu-west-1。

​		在每个Region下面，还分了多个AvailabilityZone，一个Region对应多个AvailabilityZone。每一个Region之间是相互独立以及隔离的，默认情况下资源只在单个Region之间的AvailabilityZone进行复制，跨Regin之间不会进行资源复制。Region和AvailabilityZone之间的关系如下：



## SELF PRESERVATION设计

​		在分布式系统设计里面，通常需要对应用实例的存活进行健康检查，比较关键的问题是要处理好网络抖动或短暂不可用时造成的误判。另外Eureka Server端与Client端之间如果出现网络分区问题，在极端情况下会使得Eureka Server清空部分服务的实例列表，这个将严重影响到Eureka Server的Availability属性。因此Erueka Server引入了SELF PRESERVATION机制。
​		Eureka  Client端与Server端之间有个租约，Client要定时发送心跳来维持这个租约，表示自己还存活着。Eureka通过当前注册的实倒数，去计算每分钟应该从应用实例接收到的心跳数，如果最近一分钟接收到的续约的次数小于等于指定阀值的话，则关闭租约失效剔除，禁止定时任务剔除失效的实例，从而保护注册信息。

# Eureka参数调优以及监控

下面主要分为Client端和Server端两大类进行简述，Eureka的几个核心参数

## 客户端参数

### Client端的核心参数

| 参数                                                 | 默认值    | 说明                                                         |
| :--------------------------------------------------- | :-------- | :----------------------------------------------------------- |
| eureka.client.availability-zones                     |           | 告知Client有哪些region以及availability-zones，支持配置修改运行时生效 |
| eureka.client.filter-only-up-instances               | true      | 是否过滤出InstanceStatus为UP的实例                           |
| eureka.client.region                                 | us-east-1 | 指定该应用实例所在的region，AWS datacenters适用              |
| eureka.client.register-with-eureka                   | true      | 是否将该应用实例注册到Eureka Server                          |
| eureka.client.prefer-szme-zone-eureka                | true      | 是否优先使用与该实例处于相同zone的Eureka Server              |
| eureka.client.on-demand-update-status-change         | true      | 是够将本地实例状态更新通过ApplicationInfoManager实时触发同步到Eureka Server |
| eureka.instance.metadata-map                         |           | 指定应用实例的元数据信息                                     |
| eureka.instance.prefer-ip-address                    | false     | 指定优先使用ip地址替代host name作为实例的hostName字段值      |
| eureka.instance.lease-expiration-duration-in-seconds | 90        | 指定Eureka Client间隔多久需要向Eureka Server发送心跳来告知Eureka Server该实例还存活 |

### 定时任务参数

| 参数                                                         | 默认值 | 说明                                                         |
| ------------------------------------------------------------ | ------ | ------------------------------------------------------------ |
| eureka.client.cache-refresh-executor-thread-pool-size        | 2      | 刷新缓存的CacheRefreshThread的线程池大小                     |
| eureka.client.cache-refresh-executor-exponential-back-off-bound | 10     | (刷新缓存)调度任务执行超时时下次的调度的延迟时间             |
| reka.client.heartbeat-executor-thread-pool-size              | 2      | 心跳线程HeartBeatThread的线程池大小                          |
| eureka.client.heartbeat-executor-exponential-back-off-bound  | 10     | (心跳执行)调度任务超时时下次的调度的延时时间                 |
| eureka.client.registry-fetch-interval-seconds                | 30     | CacheRefreshThread线程调度频率                               |
| eureka.client.eureka-service-url-poll-interval-seconds       | 5*60   | AsyncResolver.updateTask刷新Eureka Server地址的时间间隔      |
| eureka.client.initial-instance-info-replication-interval-seconds | 40     | InstanceInfoReplicator将实例信息变更同步到Eureka Server的初始延时时间 |
| eureka.client.instance-info-replication-interval-seconds     | 30     | InstanceInfoReplicator将实例信息变更同步到Eureka Server的时间间隔 |
| ureka.instance.lease-renewal-interval-in-seconds             | 30     | Eureka Client向Eureka Server发送心跳的时间间隔               |

### http参数

Eureka Client底层httpClient与Eureka Server通信，提供的先关参数

| 参数                                                   | 默认值 | 说明                       |
| ------------------------------------------------------ | ------ | -------------------------- |
| eureka.client.eureka-server-connect-timeout-seconds    | 5      | 连接超时时间               |
| eureka.client.eureka-server-read-timeout-seconds       | 8      | 读超时时间                 |
| eureka.client.eureka-server-total-connections          | 200    | 连接池最大活动连接数       |
| eureka.client.eureka-server-total-connections-per-host | 50     | 每个host能使用的最大链接数 |
| eureka.client.eureka-connection-idle-timeout-seconds   | 30     | 连接池中链接的空闲时间     |

## 服务端端参数

主要包含这几类：基本参数、response cache参数、peer相参数、http参数

### 基本参数

| 参数                                                       | 默认值  | 说明                                                         |
| ---------------------------------------------------------- | ------- | ------------------------------------------------------------ |
| eureka.server.enable-self-perservation                     | true    | 是否开启自我保护模式                                         |
| eureka.server.renewal-percent-threshold                    | 0.85    | 指定每分钟需要收到续约次数的阈值                             |
| eureka.instance.registry.expected-number-of-renews-per-min | 1       | 指定每分钟需要接收到的续约次数值，实际该值在其中被写死为count*2，另外也会被更新 |
| eureka.server.renewal-threshold-update-interval-ms         | 15分钟  | 指定updateRenewalThreshold定时任务的调度频率，来动态更新expectedNumberOfRenewsPerMin及numberOfRenewsPerminThreshold值 |
| eureka.server.eviction-interval-timer-in-ms                | 60*1000 | 指定EvictionTask定时任务的调度频率，用于剔除过期的实例       |

### response cache参数

Eureka Server为了提升自身REST API接口的性能，提供了两个缓存：一个是基于ConcurrentMap的readOnlyCacheMap，一个是基于Guava Cache的readWriteCacheMap。其相关参数如下：

| 参数                                                    | 默认值  | 说明                                                         |
| ------------------------------------------------------- | ------- | ------------------------------------------------------------ |
| eureka.server.use-read-only-response-cache              | true    | 是否使用只读的response-cache                                 |
| eureka.server.response-cache-update-interval-ms         | 30*1000 | 设置CacheUpdateTask的调度时间间隔，用于从readWriteCacheMap更新数据到readOnlyCacheMap。仅仅在eureka.server.use-read-only-response-cache为true的时候生效 |
| eureka.server.response-cache-auto-expiration-in-seconds | 180     | 设置readWriteCacheMap的expireAfterWrite参数，指定写入多长时间过过期 |

### peer相关参数

| 参数                                                      | 默认值  | 说明                                                         |
| --------------------------------------------------------- | ------- | ------------------------------------------------------------ |
| eureka.server.peer-eureka-nodes-update-interval-ms        | 10分钟  | 指定peersUpdateTask调度的时间间隔，用于从配置文件刷新peerEurekaNodes节点的配置信息('eureka.client.serviceUrl相关zone的配置') |
| eureka.server.peer-eureka-status-refresh-time-interval-ms | 30*1000 | 指定更新peer node状态信息的时间间隔                          |

### http参数

Eureka Server需要与其他peer节点进行通信，复制实例信息，其底层使用httpClient，提供相关的参数

| 参数                                                    | 默认值 | 说明                       |
| ------------------------------------------------------- | ------ | -------------------------- |
| eureka.server.peer-node-connect-timeout-ms              | 200    | 连接超时时间               |
| eureka.server.peer-node-read-timeout-ms                 | 200    | 读超时时间                 |
| eureka.server.peer-node-total-connections               | 1000   | 连接池最大活动连接数       |
| eureka.server.peer-node-total-connections-per-host      | 500    | 每个host能使用的最大连接数 |
| eureka.server.peer-node-connection-idle-timeout-seconds | 30     | 连接池中连接的空闲时间     |

## 参数调优

### 常见问题

1.为什么服务下线了，Eureka Server接口返回的信息还会存在？

2.为什么服务上线了，Eureka Client不能及时获取到？

3.为什么会有一下提示：

```
EMERGENCY!EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY’RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE
```

解决方法：

1.Eureka Server并不是强一致的，因此registry中会议保留过期的实例信息。原因如下：

* 应用实例异常挂掉，没能在挂掉之前告知Eureka Server要下线掉该服务实例信息。这个就需要依赖Eureka Server的EvictionTask去剔除。
* 应用实例下线是有告知Eureka Server下线，但是由于Eureka Server的REST API有response cache，因此需要等待缓存过期才能更新。
*  由于Eureka Server开启并以入了SELF PRESERVATION(自我保护)模式，导致registry的信息不会因为过期而被剔除掉，直到退出SELF PRESERVATION(自我保护)模式。

针对Client下线而没有通知Eureka Server的问题，可以调整EvictionTask的调度频率，比如把默认的时间间隔60s，调整为5s：

```yaml
eureka:
  server:
    eviction-interval-timer-in-ms: 5000
```

针对response cache的问题，可以根据情况考虑关闭readOnlyCacheMap：

```yaml
eureka:
  server:
    use-read-only-response-cache: false
```

或者调整readWriteCacheMap的过期时间：

```yaml
eureka:
  server:
    response-cache-auto-expiration-in-seconds: 60
```

针对SELF PRESERVATION(自我保护)的问题，在测试环境可以将enable-self-preservation设置为false：

```yaml
eureka:
  server:
    enable-self-preservation: false
```

关闭之后会提示：

```
THE SELF PRESER VAT ION MODE IS TURNED OFF.  THIS MAY NOT PRO TECT INSTANCE EXPIRY IN CASE OF NETWORK/OTHER PROBLEMS.
```

或者：

```
RENEWALS ARE LESSER THAN THE THRESHOLD.THE SELF PRESERVATION MODE IS TURNED OFF.THIS MAY  NOT PROTECT INSTANCE EXPIRY IN CASE OF NETWORK/OTHER PROBLEMS.
```

2.针对新服务上线，Eureka Client获取不及时的问题，在测试环境，可以适当提高client端拉取Server注册信息的频率，例如下面将默认的30s改为5s：

```yaml
eureka:
  client:
    registry-fetch-interval-seconds: 5
```

3.在实际生产过程中，经常会有网络抖动等问题造成服务实例与Eureka Server的心跳未能如期保持，但是服务实例本身是健康的，这个时候如果按照租约剔除机制剔除的话，会造成误判无果大范围误判的话，可能导致整个服务注册列表的大部分注册信息被删除，从而没有可用服务。Eureka为了解决这个问题引入了SELF PRESERVATION机制，当最近一分钟接收到的租约次数小于等于指定阈值的话，则关闭租约失效剔除，禁止定时任务失效的实例，从而保护注册信息。

在生产环境下，可以吧renewwalPercentThreshold及leaseRenewalIntervalInSeconds参数调小一点，从而提高触发SELF PRESERVATION机制的阈值。

```yaml
eureka:
  instance:
    lease-renewal-interval-in-seconds: 10 #默认是30
    renewal-percent-threshold: 0.49       #默认是0.85
```

## 监控指标

Eureka内置了基于servo的指标统计，具体在`com.netflix.eureka.util.EurekaMonitors`。Spring Boot 2.x版本改为使用Micrometer，不再支持Neflix Servo，转而支持Neflix Servo的替代品`Neflix Spectator`。不过对于Servo，可以通过`DefaultMonitorRegistry.getInstance().getRegisteredMonitors`来获取所有注册了的Monitor，进而获取其指标值。

```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.netflix.eureka.util;

import com.netflix.appinfo.AmazonInfo;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.DataCenterInfo;
import com.netflix.appinfo.AmazonInfo.MetaDataKey;
import com.netflix.appinfo.DataCenterInfo.Name;
import com.netflix.servo.DefaultMonitorRegistry;
import com.netflix.servo.annotations.DataSourceType;
import com.netflix.servo.annotations.Monitor;
import com.netflix.servo.monitor.Monitors;
import java.util.concurrent.atomic.AtomicLong;

public enum EurekaMonitors {
    // 自启动以来收到的总续约次数
    RENEW("renewCounter", "Number of total renews seen since startup"),
    // 自启动以来收到的总取消租约次数
    CANCEL("cancelCounter", "Number of total cancels seen since startup"),
    // 自启动以来查询registry的总次数
    GET_ALL_CACHE_MISS("getAllCacheMissCounter", "Number of total registery queries seen since startup"),
    // 自启动以来delta查询registry的总次数
    GET_ALL_CACHE_MISS_DELTA("getAllCacheMissDeltaCounter", "Number of total registery queries for delta seen since startup"),
    // 自启动以来使用remote region查询registry的总次数
    GET_ALL_WITH_REMOTE_REGIONS_CACHE_MISS("getAllWithRemoteRegionCacheMissCounter", "Number of total registry with remote region queries seen since startup"),
  // 自启动以来使用remote region及delta方式查询registry的总次数
  GET_ALL_WITH_REMOTE_REGIONS_CACHE_MISS_DELTA("getAllWithRemoteRegionCacheMissDeltaCounter", "Number of total registry queries for delta with remote region seen since startup"),
    // 自启动以来查询delta的总次数
    GET_ALL_DELTA("getAllDeltaCounter", "Number of total deltas since startup"),
    // 自启动以来传递regions查询delta的总次数
    GET_ALL_DELTA_WITH_REMOTE_REGIONS("getAllDeltaWithRemoteRegionCounter", "Number of total deltas with remote regions since startup"),
    // 自启动以来查询'/{version}/apps'的次数
    GET_ALL("getAllCounter", "Number of total registry queries seen since startup"),
    // 自启动以来传递regions参数查询'/{version}/apps'的次数
    GET_ALL_WITH_REMOTE_REGIONS("getAllWithRemoteRegionCounter", "Number of total registry queries with remote regions, seen since startup"),
    // 自启动以来请求/{version}/apps/{appId}的总次数
    GET_APPLICATION("getApplicationCounter", "Number of total application queries seen since startup"),
    // 自启动以来register的总次数
    REGISTER("registerCounter", "Number of total registers seen since startup"),
    // 自启动以来剔除过期实例的总次数
    EXPIRED("expiredCounter", "Number of total expired leases since startup"),
    // 自启动以来statusUpdate的总次数
    STATUS_UPDATE("statusUpdateCounter", "Number of total admin status updates since startup"),
    // 自启动以来deleteStatusOverride的总次数
    STATUS_OVERRIDE_DELETE("statusOverrideDeleteCounter", "Number of status override removals"),
    // 自启动以来收到cancel请求时对应实例找不到的次数
    CANCEL_NOT_FOUND("cancelNotFoundCounter", "Number of total cancel requests on non-existing instance since startup"),
    // 自启动以来收到renew请求时对应实例找不到的次数
    RENEW_NOT_FOUND("renewNotFoundexpiredCounter", "Number of total renew on non-existing instance since startup"),
    REJECTED_REPLICATIONS("numOfRejectedReplications", "Number of replications rejected because of full queue"),
    FAILED_REPLICATIONS("numOfFailedReplications", "Number of failed replications - likely from timeouts"),
    // 由于开启rate limiter被丢弃的请求数量
    RATE_LIMITED("numOfRateLimitedRequests", "Number of requests discarded by the rate limiter"),
    // 如果开启rate limiter的话，将被丢弃的请求数
    RATE_LIMITED_CANDIDATES("numOfRateLimitedRequestCandidates", "Number of requests that would be discarded if the rate limiter's throttling is activated"),
    // 开启rate limiter时请求全量registry被丢弃的请求数
    RATE_LIMITED_FULL_FETCH("numOfRateLimitedFullFetchRequests", "Number of full registry fetch requests discarded by the rate limiter"),
    // 如果开启rate limiter时请求全量registry将被丢弃的请求数
    RATE_LIMITED_FULL_FETCH_CANDIDATES("numOfRateLimitedFullFetchRequestCandidates", "Number of full registry fetch requests that would be discarded if the rate limiter's throttling is activated");

    private final String name;
    private final String myZoneCounterName;
    private final String description;
    @Monitor(
        name = "count",
        type = DataSourceType.COUNTER
    )
    private final AtomicLong counter = new AtomicLong();
    @Monitor(
        name = "count-minus-replication",
        type = DataSourceType.COUNTER
    )
    private final AtomicLong myZoneCounter = new AtomicLong();

    private EurekaMonitors(String name, String description) {
        this.name = name;
        this.description = description;
        DataCenterInfo dcInfo = ApplicationInfoManager.getInstance().getInfo().getDataCenterInfo();
        if (dcInfo.getName() == Name.Amazon) {
            this.myZoneCounterName = ((AmazonInfo)dcInfo).get(MetaDataKey.availabilityZone) + "." + name;
        } else {
            this.myZoneCounterName = "dcmaster." + name;
        }

    }

    public void increment() {
        this.increment(false);
    }

    public void increment(boolean isReplication) {
        this.counter.incrementAndGet();
        if (!isReplication) {
            this.myZoneCounter.incrementAndGet();
        }

    }

    public String getName() {
        return this.name;
    }

    public String getZoneSpecificName() {
        return this.myZoneCounterName;
    }

    public String getDescription() {
        return this.description;
    }

    public long getCount() {
        return this.counter.get();
    }

    public long getZoneSpecificCount() {
        return this.myZoneCounter.get();
    }

    public static void registerAllStats() {
        EurekaMonitors[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            EurekaMonitors c = var0[var2];
            Monitors.registerObject(c.getName(), c);
        }

    }

    public static void shutdown() {
        EurekaMonitors[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            EurekaMonitors c = var0[var2];
            DefaultMonitorRegistry.getInstance().unregister(Monitors.newObjectMonitor(c.getName(), c));
        }

    }
}
```

# Eureka实战

## Eureka Server在线扩容

准备工作：创建cloud工程

### 创建cloud-config-server子模块。

```xml
	<dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    </dependencies>
```

启动类

```java
package com.msr.batter.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
```

配置文件:`resources/bootstrap.yml`

```yaml
spring:
  profiles:
    active: native
  application:
    name: config-server
server:
  port: 8888
```

这里不是主要讲解`Spring Cloud Config`内容。所以对于配置文件使用native的profile，也就是使用文件来存储配置，默认是放在`resources/config`目录下。在Eureka Server和Eureka Client模块分别引入`spring-cloud-starter-config`。分别都创建一个`ServerController`和`ClientController`。

Eureka Client的配置文件：`resources/config/eureka-client.yml`

```yaml
server:
  port: 8081

spring:
  application:
    name: cloud-eureka-client1

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/ # 一个Eureka Server
```

Eureka Server配置文件：`resources/config/eureka-server-peer1.yml`

```yaml
server:
  port: 8761
spring:
  application:
    name: cloud-eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/ # 一个Eureka Server
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```


### cloud-eureka-server模块

```xml
		<!--    添加    -->
		<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
```

`ServerController`

```java
package com.msr.better.registry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("query")
public class ServerController {

    @Autowired
    EurekaClientConfigBean eurekaClientConfigBean;

    @GetMapping("eureka-server")
    public Object getEurekaServerUrl() {
        return eurekaClientConfigBean.getServiceUrl();
    }
}
```

配置文件：`resources/bootstrap.yml`

```yaml
spring:
  application:
    name: cloud-eureka-server
  cloud:
    config:
      uri: http://localhost:8888
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

配置文件：`resources/application.yml`

```yaml
eureka:
  server:
    peer-eureka-nodes-update-interval-ms: 10000 #默认是10分钟即600000，这里为了验证改为10秒
```

### cloud-erueka-client子模块

```xml
		<!--    添加    -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

`ClientController`

```java
package com.msr.better.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MaiShuRen
 * @date 2020-10-23
 */
@RestController
@RequestMapping("query")
public class ClientController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EurekaClientConfigBean clientConfigBean;

    /**
     * 服务实例信息
     *
     * @return
     */
    @GetMapping("info")
    public Map<String, Object> info() {
        Map<String, Object> map = new HashMap<>(16);
        List<String> services = discoveryClient.getServices();
        String description = discoveryClient.description();
        int order = discoveryClient.getOrder();

        map.put("services", services);
        map.put("description", description);
        map.put("order", order);

        return map;
    }

    /**
     * 根据服务名称获取服务实例列表
     *
     * @param name 服务名
     * @return 返回结果
     */
    @GetMapping("instance/{name}")
    public List<ServiceInstance> getIns(@PathVariable String name) {
        return discoveryClient.getInstances(name);
    }

    /**
     * 获取Eureka Server的url
     *
     * @return
     */
    @GetMapping("eureka-server")
    public Object getServiceUrl() {
        return clientConfigBean.getServiceUrl();
    }
}
```

配置文件：`resources/bootstrap.yml`

```yaml
spring:
  application:
    name: cloud-eureka-client
  cloud:
    config:
      uri: http://localhost:8888
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

费别启动`cloud-config-server`、`cloud-eureka-server`(使用peer1的profile)、`cloud-eureka-client`。

> mvn spring-boot:run                                                          在cloud-config-server目录下执行，启动cloud-config-server服务
>
> mvn spring-boot:run -Dspring-boot.run.profiles=peer1   在cloud-eureka-server目录下执行，启动cloud-eureka-server
>
> mvn spring-boot:run                                                          在cloud-eureka-client目录下执行，启动cloud-eureka-client

### 扩容两个Eureka Server

在`cloud-config-server`下新增配置`cloud-eureka-server-peer2.yml`和`cloud-eureka-server-peer3.yml`

**cloud-eureka-server-peer2.yml**

```yaml
server:
  port: 8762

eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8763/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

启动：`mvn spring-boot:run -Dspring-boot.run.profiles=peer2`

**cloud-eureka-server-peer3.yml**

```yaml
server:
  port: 8763

eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: true
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

启动：mvn spring-boot:run -Dspring.profiles.active=peer3



修改一下`cloud-eureka-client.yml`、`cloud-eureka-server-peer1.yml`的配置文件

**cloud-eureka-server-peer1.yml的修改**

```yaml
server:
  port: 8761
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
  client:
    registerWithEureka: true
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://localhost:8762/eureka/,http://localhost:8763/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

**cloud-eureka-client.yml的修改**

```yaml
server:
  port: 8081

spring:
  application:
    name: cloud-eureka-client1
eureka:
  client:
    serviceUrl:
	  efaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/
```

然后重新启动`cloud-config-server`，使修改的配置生效。然后使用Postman这类型工具通过`POST请求`去访问http://localhost:8081/actuator/refresh、http://localhost:8761/actuator/refresh，分别刷新`cloud-eureka-client`和`cloud-eureka-server-peer1`。或者使用下面的命令

> curl -i -X POST localhost:8081/actuator/refresh
>
> curl -i -X POST localhost:8761/actuator/refresh

然后在浏览器或者使用curl命令去请求，之前`cloud-config-server`写好的一个接口：http://localhost:8081/query/eureka-server。最后返回结果：

>  {"defaultZone": "http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/"}

结果显示扩容成功

### 扩容小结

对于配置文件：在扩容中使用了`spring-cloud-config`这个项目统一来管理所有服务的配置文件。在真实的使用中，这些配置文件可以放在像`gitlab`、`github`这类型的git平台上，在修改了配置之后，不需要重启配置中心。只需要通过消息总线去通知各个服务去获取最新的配置。

对于扩容的实例：在配置中心中配好了，新增的实例的配置信息之后。通过命令去启动打包好的springbot项目的jar包，列如：`java -jar cloud-eureka-server --spring.profiles.active=peer2`。最后刷新正在运行的实例。

## 构建多zone的Eureka Server集群

### 创建cloud-eureka-server-multizone模块

这里启动四个Eureka Server实例。配置两个zone：zone1和zone2，每一个zone都有两个Eureka Server实例，这两个实例配置在同一个的region：region-beijing上。

启动类：

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerMultiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerMultiApplication.class, args);
    }
}
```

配置文件：

application-zone1a.yml

```yaml
server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

application-zone1b.yml

```yaml
server:
  port: 8762
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

application-zone2a.yml

```yaml
server:
  port: 8763
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone2
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

application-zone2b.yml

```yaml
server:
  port: 8764
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone2
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

application.yml

```yaml
eureka:
  server:
    use-read-only-response-cache: false
    response-cache-auto-expiration-in-seconds: 10
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

在上面的四个配置可以看得出，每一个Eureka instance都配置了自己的zone：`ereuka.instance.metadataMap.zone`。那么接下来启动它们看一下：随便访问一个Eureka Server

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=zone1a
mvn spring-boot:run -Dspring-boot.run.profiles=zone1b
mvn spring-boot:run -Dspring-boot.run.profiles=zone2a
mvn spring-boot:run -Dspring-boot.run.profiles=zone2b
```

### 创建cloud-eureka-client-multizone模块

创建次模块获取Eureka Server信息。

配置文件
application-znoe1.yml

```yaml
server:
  port: 8081
spring:
  application:
    name: eureka-client
eureka:
  instance:
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
```

application-znoe2.yml

```yaml
server:
  port: 8082
spring:
  application:
    name: eureka-client
eureka:
  instance:
    metadataMap.zone: zone2
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
```

application.yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

启动类

```java
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientMultiApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientMultiApplication.class, args);
    }
}
```

启动zone1和zone2这两个配置：

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=zone1
mvn spring-boot:run -Dspring-boot.run.profiles=zone2
```

### 创建cloud-zuul-gateway

创建一个zuul实例来演示Eureka使用metadataMap的zone属性时ZoneAffinity特性

**代码**

pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud</artifactId>
        <groupId>com.msr.better</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>cloud-zuul-gateway</artifactId>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

启动类

```java
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
```

配置文件

application-zone1.yml

```yaml
server:
  port: 10001
eureka:
  instance:
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
```

application-zone2.yml

```yaml
server:
  port: 10002
eureka:
  instance:
    metadataMap.zone: zone2
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-beijing: zone1,zone2
```

application.yml

```yaml
spring:
  application:
    name: zuul-gateway
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

启动这两个配置的zuul实例：

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=zone1
mvn spring-boot:run -Dspring-boot.run.profiles=zone2
```

### 测试验证

浏览器访问：http://localhost/eureka-client/actuator/env和http://localhost:10002/eureka-client/actuator/env。分别会返回。

```json
{
    "activeProfiles": [
        "zone1"
    ],
    "propertySources": [...]
}

{
    "activeProfiles": [
        "zone2"
    ],
    "propertySources": [...]
}
```

从结果可以看得出来，通过请求gateway的eureka-clienti/actuator/env，访问的是eureka-client实例的/actuator/env接口，处于zone1的gateway返回的activeProfiles为zone1，处于zone2的gateway返回的activeProfiles为zone2。从这个表象看gateway路由时对client的实例是ZoneAffinity的。

## 支持Remote Region

一下模块的maven坐标和启动类和上面基本一致。代码就不贴出来了。

### 创建cloud-eureka-server-remote-region模块

配置四个Eureka Server，分四个zone。zone1、zone2属于region-beijing；zone3、zone4属于region-guangzhou。region-beijing和region-guangzhou互为remote-region

配置文件

application-zone1.yml

```yaml
server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
    remoteRegionUrlsWithName:
      region-guangzhou: http://localhost:8763/eureka/
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/
      zone2: http://localhost:8762/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  instance:
    hostname: localhost
    metadataMap.zone: zone1
```

application-zone2.yml

```yaml
server:
  port: 8762
spring:
  application:
    name: eureka-server
eureka:
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
    remoteRegionUrlsWithName:
      region-guangzhou: http://localhost:8763/eureka/
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/
      zone2: http://localhost:8762/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  instance:
    hostname: localhost
    metadataMap.zone: zone2

```

application-zone3-region-guangzhou.yml

```yaml
server:
  port: 8763
spring:
  application:
    name: eureka-server
eureka:
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
    remoteRegionUrlsWithName:
      region-beijing: http://localhost:8761/eureka/
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-guangzhou
    service-url:
      zone3: http://localhost:8763/eureka/
      zone4: http://localhost:8764/eureka/
    availability-zones:
      region-guangzhou: zone3,zone4
  instance:
    hostname: localhost
    metadataMap.zone: zone3
```

application-zone4-region-guangzhou.yml

```yaml
server:
  port: 8764
spring:
  application:
    name: eureka-server
eureka:
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
    remoteRegionUrlsWithName:
      region-beijing: http://localhost:8761/eureka/
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-guangzhou
    service-url:
      zone3: http://localhost:8763/eureka/
      zone4: http://localhost:8764/eureka/
    availability-zones:
      region-guangzhou: zone3,zone4
  instance:
    hostname: localhost
    metadataMap.zone: zone4
```

创建配置类：RegionConfig。因为zone3和zone4是属于region-guangzhou，remote-region配置为region-beijing。由于源码里EurekaServerConfigBean的remoteRegionAppWhitelist默认为null；而getRemoteRegionAppWhitelist(String regionName)方法会直接被调用，如果不设置会导致空指针异常。

```java
@Configuration
@AutoConfigureBefore(EurekaServerAutoConfiguration.class)
public class RegionConfig {
    @Bean
    @ConditionalOnMissingBean
    public EurekaServerConfig eurekaServerConfig(EurekaClientConfig clientConfig) {
        EurekaServerConfigBean serverConfigBean = new EurekaServerConfigBean();
        if (clientConfig.shouldRegisterWithEureka()) {
            serverConfigBean.setRegistrySyncRetries(6);
        }
        serverConfigBean.setRemoteRegionAppWhitelist(new HashMap<>());
        return serverConfigBean;
    }
}
```

启动Eureka Server：

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=zone1
mvn spring-boot:run -Dspring-boot.run.profiles=zone2
mvn spring-boot:run -Dspring-boot.run.profiles=zone3-region-guangzhou
mvn spring-boot:run -Dspring-boot.run.profiles=zone4-region-guangzhou
```

### 创建cloud-eureka-client-remote-region模块

四个client，分为四个zone，属于region-beijng和region-guangzhou这两个region。

**配置文件**

application.yml

```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

application-zone1.yml

```yaml
server:
  port: 8071
spring:
  application.name: eureka-client
eureka:
  client:
    prefer-same-zone-eureka: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/
      zone2: http://localhost:8762/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  instance:
    metadataMap.zone: zone1
```

application-zone2.yml

```yaml
server:
  port: 8072
spring:
  application.name: eureka-client
eureka:
  client:
    prefer-same-zone-eureka: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/
      zone2: http://localhost:8762/eureka/
    availability-zones:
      region-beijing: zone1,zone2
  instance:
    metadataMap.zone: zone2
```

application-zone3.yml

```yaml
server:
  port: 8073
spring:
  application.name: eureka-client
eureka:
  client:
    prefer-same-zone-eureka: true
    region: region-guangzhou
    service-url:
      zone3: http://localhost:8763/eureka/
      zone4: http://localhost:8764/eureka/
    availability-zones:
      region-guangzhou: zone3,zone4
  instance:
    metadataMap.zone: zone3
```

application-zone4.yml

```yaml
server:
  port: 8074
spring:
  application.name: eureka-client
eureka:
  client:
    prefer-same-zone-eureka: true
    region: region-guangzhou
    service-url:
      zone3: http://localhost:8763/eureka/
      zone4: http://localhost:8764/eureka/
    availability-zones:
      region-guangzhou: zone3,zone4
  instance:
    metadataMap.zone: zone4
```

**启动**

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=zone1
mvn spring-boot:run -Dspring-boot.run.profiles=zone2
mvn spring-boot:run -Dspring-boot.run.profiles=zone3
mvn spring-boot:run -Dspring-boot.run.profiles=zone4
```

### 创建cloud-zuul-gateway-remote-region模块

配置文件

application.yml

```yaml
spring:
  application:
    name: zuul-gateway
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

application-zone1.yml

```yaml
server:
  port: 10001
eureka:
  instance:
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-beijing
    service-url:
      zone1: http://localhost:8761/eureka/
      zone2: http://localhost:8762/eureka/
    availability-zones:
      region-beijing: zone1,zone2
```

application-zone3-region-guangzhou.yml

```yaml
server:
  port: 10002
eureka:
  instance:
    metadataMap.zone: zone3
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-guangzhou
    service-url:
      zone3: http://localhost:8763/eureka/
      zone4: http://localhost:8764/eureka/
    availability-zones:
      region-guangzhou: zone3,zone4
```

启动

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=zone1
mvn spring-boot:run -Dspring-boot.run.profiles=zone3-region-guangzhou
```

### 测试验证

在全部启动完成之后，分别访问

```
http://localhost:10001/eureka-client/actuator/env
http://localhost:10002/eureka-client/actuator/env
```

10001端口的网关访问的是zone1的eureka-client。10002端口的网关访问的是zone3的eureka-client。现在关掉zone1和zone2的eureka-client，继续访问`http://localhost:10001/eureka-client/actuator/env`，在报错几次之后，就会自动fallback切换到remote-region里的zone。实现了类似于异地多活自动转义请求的效果。

## 开启HTTP Basic认证

Eureka Server是有暴露了自己的REST API，如果没有安全认证，别人就可以通过这些API修改信息，造成服务异常。那就看一下Euerka Server是怎么开启使用HTTP Basic校验，以及Eureka Client是怎么鉴权的。

### 创建cloud-eureka-server-http-basic模块

pom.xml.。引入`spring-boot-starter-security`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>cloud</artifactId>
        <groupId>com.msr.better</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-eureka-server-http-basic</artifactId>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

application.yml。很明显我们在配置文件中配置了用户名和密码，这些配置config-server这些配置中心来使用更加适合。

```yaml
server:
  port: 8761
spring:
  security:
    basic:
      enabled: true
    user:
      name: admin
      password: Xk38CNHigBP5jK75
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

禁用`spring-boot-starter-security`默认开启的csrf功能

```java
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
    }
}
```

在浏览器访问http://localhost:8761或http://localhost:8761/eureka/apps，这些eureka的REST API时，是要求登录的。使用在配置文件中配置好的username和password登录，才可以成功访问。

### 创建cloud-eureka-client-http-basic模块

因为Eureka Server开启了HTTP Basic认证，所以Eureka Client需要配置相对应的用户名和密码。同样这里在配置文件中配置，当然配置config-server使用更好。

**配置文件**

application.yml

```yaml
server:
  port: 8081

spring:
  application:
    name: client1

eureka:
  client:
    security:
      basic:
        user: admin
        password: Xk38CNHigBP5jK75
    serviceUrl:
      defaultZone: http://${eureka.client.security.basic.user}:${eureka.client.security.basic.password}@localhost:8761/eureka/
      #defaultZone: http://localhost:8761/eureka/
```

如果不使用用户名密码的话启动会报错

```
Request execution failure with status code 401; retrying on another server if available
```

浏览器登录之后，访问http://localhost:8761/eureka/apps。可以看到注册进去的eureka-client实例的信息，说明注册成功。

## 启用https

虽然开启了HTTP Basic但是基于base64编码很容易抓包被破解，如果直接暴露在公网上很危险。因此Eureka Server和Client开启https可以有效的解决这类问题。

### 证书准备

1、生成证书

使用jdk的keytool工具

```shell
keytool -genkeypair -alias server -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore eureka.server -validity 365
```

上面的命令生成server端的证书，密码是server

```shell
keytool -genkeypair -alias client -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore eureka.client -validity 365
```

上面的命令生成client端的证书，密码是client

2、导出证书

```shell
#导出server
keytool -export -alias server -file server.crt --keystore eureka.server
#导出client
keytool -export -alias client -file client.crt --keystore eureka.client
```

3、使Client端信任Server

```shell
# 提示输入密码时，输入client的密码：Client
keytool -import -alias server -file server.crt --keystore eureka.client
```

4、使Server信任Client

```shell
# 同理
keytool -import -alias client -file client.crt --keystore eureka.server
```

### 创建cloud-eureka-server-https模块

把生成的server.crt、client.crt和eureka.server复制到resources文件夹下。

配置文件

application.yml

```yaml
server:
  port: 8761
  ssl:
    enabled: true
    key-store: classpath:eureka.server
    key-store-password: server
    key-store-type: PKCS12
    key-alias: server
eureka:
  instance:
    hostname: localhost
    securePort: ${server.port}
    securePortEnabled: true
    nonSecurePortEnabled: false
    homePageUrl: https://${eureka.instance.hostname}:${server.port}/
    statusPageUrl: https://${eureka.instance.hostname}:${server.port}/
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: https://${eureka.instance.hostname}:${server.port}/eureka/
  server:
    waitTimeInMsWhenSyncEmpty: 0
    enableSelfPreservation: false
```

启动之后，分别在浏览器访问http://localhost:8761和https://localhost:8761。使用http访问的时候会出现

```
Bad Request
This combination of host and port requires TLS.
```

使用https访问时正确的访问到了eureka。说明https启用成功

### 创建cloud-eureka-client-https模块

把生成的server.crt、client.crt和eureka.client复制到resources文件夹下。

pom.xml，添加httpclient

```xml
		<dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.5</version>
        </dependency>
```

配置文件

application.yml

```yaml
server:
  port: 8081
spring:
  application:
    name: eureka-client1
eureka:
  client:
    securePortEnabled: true
    ssl:
      key-store: eureka.client
      key-store-password: client
    serviceUrl:
      defaultZone: https://localhost:8761/eureka/
```

在上面的配置文件中，没有配置整个应用开启https，因为这里仅仅是Eureka Server开启了https。通过自定义eureka.client.key-store以及eureka.client.ssl.key.store-password，来执行Eureka Client访问Eureka Server的sslContext配置，所以需要配置一下DiscoveryClient。

最后，启动就可以在Eureka Server上看到注册的Eureka Client了。

## Eureka Admin

在微服务架构中注册中心是必不可少的基础组件，Eureka Server是Spring Cloud技术栈中的基础组件。Spring Cloud中国社区开源了一个对Eureka Server节点进行监控、服务动态启停的管控平台：Eureka Admin。Github上的地址：https://github.com/SpringCloud/eureka-admin。在Github上看见这个仓库最新的更新：Updated on 15 Aug 2019，现在国内用的最多的是Spring Cloud Alibaba Nacos，毕竟Eureka2.x版本已经停止开源了，但是Eureka1.x版本还在持续的维护中。可能Alibaba就是看到了这一点强势开源了Spring Cloud Alibaba，其中的Nacos确实解决了一些“痛点”。

现在来看一下，Eureka Admin！

构建步骤：
1、正常启动您的Eureka以及服务体系
2、由于目前尚未提交中央仓库，需下载源码构建地址：https://github.com/SpringCloud/eureka-admin，打开eureka-admin-starter-server项目配置文件，设置您的eureka注册中心地址以及eureka-admin管控平台端口即可，如下

```yaml
server:
  port: 8080
eureka:
  server: 
    eviction-interval-timer-in-ms: 30000
  client:
    register-with-eureka: false
    fetch-registry: true
    filterOnlyUpInstances: false
    serviceUrl:
       defaultZone: http://localhost:8761/eureka/,http://localhost:8762/eureka/,http://localhost:8763/eureka/,http://localhost:8764/eureka/
logging:
  level:
    com:
      itopener: DEBUG
    org:
      springframework: INFO
```

3、启动EurekaAdminServer。浏览器访问：http://localhost:8080

4、结果

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/springcloud/eureka admin-1.png)

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/springcloud/eureka admin-2.png)

这个估计也就了解一下吧，或者二次开发。

# Eureka故障演练

## Eureka Server全部不可用

### 启动前全部不可用

如果Eureka Server集群在其他需要注册的服务还没启动之前就全部挂掉了或者没有启动，客户端就会一直报错。因为客户端一直连接不上Eureka Server，访问不了service registry的服务信息，不能和其他的服务交互。

```
2021-04-07 11:02:28.734 ERROR 13120 --- [           main] c.n.d.s.t.d.RedirectingEurekaHttpClient  : Request execution error. endpoint=DefaultEn
dpoint{ serviceUrl='http://localhost:8762/eureka/}

com.sun.jersey.api.client.ClientHandlerException: java.net.ConnectException: Connection refused: connect
        at com.sun.jersey.client.apache4.ApacheHttpClient4Handler.handle(ApacheHttpClient4Handler.java:187) ~[jersey-apache-client4-1.19.1.jar:1
.19.1]
        at com.sun.jersey.api.client.filter.GZIPContentEncodingFilter.handle(GZIPContentEncodingFilter.java:123) ~[jersey-client-1.19.1.jar:1.19
.1]
        at com.netflix.discovery.EurekaIdentityHeaderFilter.handle(EurekaIdentityHeaderFilter.java:27) ~[eureka-client-1.9.17.jar:1.9.17]
        at com.sun.jersey.api.client.Client.handle(Client.java:652) ~[jersey-client-1.19.1.jar:1.19.1]
        at com.sun.jersey.api.client.WebResource.handle(WebResource.java:682) ~[jersey-client-1.19.1.jar:1.19.1]
        at com.sun.jersey.api.client.WebResource.access$200(WebResource.java:74) ~[jersey-client-1.19.1.jar:1.19.1]
        at com.sun.jersey.api.client.WebResource$Builder.get(WebResource.java:509) ~[jersey-client-1.19.1.jar:1.19.1]

```

针对以上这种情况，Eureka Server设计了一个eureka.client.backup-registry-impl属性，配置之后可以防止在启动时访问不到Eureka Server的情况，这样客户端就可以访问这个back registry，作为一个fallback处理。改backup-registry-impl比较适合服务端提供负载均衡或者服务ip相对固定的场景。

在客户端添加

```java
@Component("staticBackupServiceRegistry")
public class StaticBackupServiceRegistry implements BackupRegistry {

    private Applications localApps = new Applications();

    public StaticBackupServiceRegistry() {
        Application application = new Application("org");
        InstanceInfo instanceInfo1 = InstanceInfo.Builder.newBuilder()
                .setAppName("org-service")
                .setVIPAddress("org-service")
                .setInstanceId("org-instance-1")
                .setHostName("192.168.79.100")
                .setIPAddr("192.168.79.100")
                .setPort(9090)
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setStatus(InstanceInfo.InstanceStatus.UP)
                .build();

        InstanceInfo instanceInfo2 = InstanceInfo.Builder.newBuilder()
                .setAppName("org-service")
                .setVIPAddress("org-service")
                .setInstanceId("org-instance-1")
                .setHostName("192.168.79.100")
                .setIPAddr("192.168.79.100")
                .setPort(9091)
                .setDataCenterInfo(new MyDataCenterInfo(DataCenterInfo.Name.MyOwn))
                .setStatus(InstanceInfo.InstanceStatus.UP)
                .build();

        application.addInstance(instanceInfo1);
        application.addInstance(instanceInfo2);
        localApps.addApplication(application);
    }

    @Override
    public Applications fetchRegistry() {
        return localApps;
    }

    @Override
    public Applications fetchRegistry(String[] strings) {
        return localApps;
    }
}
```

```yaml
eureka:
  client:
    backup-registry-impl: staticBackupServiceRegistry
```

### 运行时全部不可用

Eureka Client在本地内存中有一个`AtomicReference<Applications>`类型的localRegionApps变量，来维护从Eureka Server拉取回来的注册信息。Client端有个定时任务CacheRefeshThread，会定时从Eureka Server拉取注册信息更新到本地，如果Eureka Server在应用服务运行时挂掉了，Client端本地的CacheRefreshThread会抛出异常，本地的localRegionApps不会得到更新。

```
2021-04-07 11:35:37.979 ERROR 10236 --- [freshExecutor-0] c.n.d.s.t.d.RedirectingEurekaHttpClient  : Request execution error. endpoint=DefaultEn
dpoint{ serviceUrl='http://localhost:8762/eureka/}
```

从错误日志可以看到，报错的是名为`freshExecutor-0`的线程。

## Eureka Server部分不可用

部分不可用的话，Server端和Client端都会出现错误日志。

### Server端

Eureka Server之前相互是peer node，如果有一台Eureka Server挂了，那么Eureka Server之间的replication会受到影响。

PeerEurekaNodes(com.netflix.eureka.cluster.PeerEurekaNodes)有一个定时任务(peersUpdateTask)，会从配置文件拉取availabilityZones以及ServerUrl信息，然后进行更新peerEurekaNodes信息。如果一台Eureka Server挂了，人工介入更改Eureka Server的ServiceUrl信息，可以剔除挂了的peer node。

### Client端

Client端有个定时任务(AsyncResolver.updateTask)去拉取serviceUrl的变更，如果配置文件有变动，运行时可以动态变更。拉去完之后，client端会随机化Server的list。

第一次拉取的时候是按照配置的顺序，之后的定时更新会随机化一次。

Client端在请求Server的时候，维护了一个不可用的Eureka Server列表（quarantineSet,在Connction error或者5xx的情况下会被列入该列表，当该列表的大小超过指定阔值则会重新空）；对可用的Server列表（一般为拉取回来的Server列表剔除不可用的列表，如果剔除后为空，则不会做剔除处理），采用RetryableEurekaHttpClient进行请求，numberOfRetries。也就是说，如果Eureka Server有一台挂掉，则会被纳入不可用列表。那么这个时候获取的服务注册信息是来自健康的Eureka Server。

# 总结

现在更多的人选择去使用Nacos了，可以说Eureka作为第一代Spring Cloud最广泛使用的注册中心，还是很有必要了解一下Eureka的。
