# VMQ
## 简介
VMQ是基于RabbitMQ开发，目的是为了提升业务开发效率，支持事务消息、延迟消息以及便利的并发配置

---

## 特征
* 简单的配置
* 延迟消息
* 事务消息
* 灵活的消息重试
* 支持Consumer并发配置
* 基于SPI进行扩展
---

## 依赖版本要求
* JDK1.8及以上版本
* RabbitMq3.7.x及以上版本
---

## 架构
![image](https://github.com/silencerv/vmq/blob/master/docs/images/arch.png)

---

## 基本配置
### ConnectionFactory配置
无论是生产者还是消费者，都需要连接rabbit broker，VMQ通过读取配置文件来得到用户的配置

VMQ默认读取classpath根路径下的vmq.properties文件

文件位置也可以通过虚拟机启动参数来配置，如 -Dvmq.properties.path="/x/xx/x.properties"

```properties
mq.connectionFactory.hosts=xxx.xxx.xxx.xxx
mq.connectionFactory.username=user
mq.connectionFactory.password=password
```
---

## 入门
以下例子均为示意，具体配置参见具体章节。

### 发送消息

```java
MessageProducer messageProducer = new DirectMessageProducer();
        Message message = MessageBuilder.aBasicMessage()
                .withSubject("subject")
                .withAttrs(Collections.emptyMap())
                //发送延迟消息
                .withDelayMills(100)
                .build();
        messageProducer.send(message);
```

### 消费消息

```java
@MqConsumer(subjects = "subjectName", group = "groupName")
public void onMessage(Message message) 
```

---

## 生产者配置
### 概述
VMQ的Producer支持事务消息，保证数据库操作和消息具有事务性（全部成功或者全部失败），同时消息的发送是异步的不会阻塞业务操作。

事务消息还需要配置Task任务来进行消息的失败补偿，详见Task章节的说明

事务消息需要在业务数据库初始化一张数据表
```sql
CREATE DATABASE IF NOT EXISTS mq;
CREATE TABLE mq.message_producer (
 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
 `message_id` varchar(255) NOT NULL COMMENT '消息id',
 `subject` varchar(255) NOT NULL COMMENT '消息主题',
 `content` TEXT NOT NULL COMMENT '消息属性',
 `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
 `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_message_id` (`message_id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8

```

### 配置
Producer的消息发送依赖于事务提交的操作，所以注解和XML均需要配置VMQTransactionManager

#### 注解配置
```java
/**
 * 省略数据源配置
 * 配置VMQProducer，会将Producer直接注册到Spring中，后续可以直接引用
 */
@EnableVMQ(producers = @VMQProducer(dataSource = "dataSource"))
public class VMQConfiguration {

    /**
     * 注意数据源必须是消息要绑定的业务库数据源
     * @param dataSource
     * @return
     */
    @Bean
    public MQTransactionManager buildMQTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new MQTransactionManager(dataSource);
    }
}
```
#### XML配置
XML依然也是配置Producer和TxManager
```xml
<!-- 此处省略数据源配置！ -->
<bean class="com.v.inf.mq.client.tx.MQTransactionManager">
    <constructor-arg ref="dataSource"/>
</bean>

<bean id="messageProducer" class="com.v.inf.mq.client.producer.SingleDbMessageProducer">
    <property name="dataSource" ref="dataSource"/>
</bean>
```

经过上述配置之后，就可以很便捷的发送消息了

```java
@Component
public class MessageSender {

    @Autowired
    private MessageProducer messageProducer;

    public void sendMessage(){
        Message message = MessageBuilder.aBasicMessage()
                .withSubject("subject")
                //发送延迟消息
                .withDelayMills(100)
                .build();
        //消息内容
        message.setAttr("content", "hello");
        messageProducer.send(message);
    }
}
```

另外，如果不需要事务消息，初始化com.v.inf.mq.client.producer.DirectMessageProducer作为Producer即可 (注解@VMQProducer目前暂不支持)

---

## 消费者配置
### Annotation
使用注解前需要先开启注解配置，有两种方案

* 将@EnableVMQ配置到Spring可以扫描到的类上
```java
@EnableVMQ
```
 
* 配置XML元素 &lt;mq:annotation-driven/> 以开启注解配置


VMQ的消息消费者注解配置是非常便捷的

只需在Spring容器托管的Bean的方法上标注@MQConsumer即可配置消息消费者

注意:方法必须只有一个参数且类型为com.v.inf.mq.Message

```java 
@MqConsumer(subjects = "subjectName", group = "groupName")
public void onMessage(Message message) {
        // todo do something
    }
```
@MQConsumer支持便利的并发配置，说明如下

```java

    /**
     * 每次从队列拉取的消息数量
     * 实际上也是消费者线程队列等待数量
     * 
     */
    int prefetchCount() default 1;

    /**
     * 并发消费者数量，配置格式为"5-10"，也可以配置一个固定消费者数量，比如"10"
     * 这个数值即为消费者线程池的线程数量配置
     * 实际上每一个消费者底层都映射了一个Rabbit Channel，每个channel都和一个线程绑定
     */
    int concurrency() default 10;

    /**
     * 最大并发消费者数量
     * 可选配置，和concurrency有重叠的地方
     */
    int maxConcurrentConsumers() default 10;
    
    /**
     * 是否requeue，默认false
     * 如果为true，则消息不会由task重试，而是立即重新加入队列再次投递，请谨慎配置以防出现循环投递
     */
    boolean requeueReject() default false;
```

### XML
XML配置也非常简单，只需注册实现了MessageListener接口实例即可

```java
public class PrintMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // print message
        System.out.println(JsonUtils.toJsonString(message));
    }
}

```

```xml
<bean id="printMessageListener" class="com.v.inf.mq.client.test.listener.PrintMessageListener"/>
<mq:consumer>
     <mq:listener subject="test"
                     group="test"
                     ref="printMessageListener"/>
</mq:consumer>
```

XML标签和注解有相同的并发配置，通过listener标签的属性可以配置listener的并发参数，不再赘述。

---

## SPI扩展
### Listener管理
对于消费端来说一个很常见的场景是，在应用上线（此处是指向应用中心或者反向代理上线）之后才接受消费者的消费或者是在应用下线之后停止所有的消费者，VMQ通过SPI来对这种需求进行扩展

VMQ的消费者（包括注解、XML）都向一个管理类注册，这个类是RabbitAdminService接口的实现类，它的默认实现是com.v.inf.mq.client.admin.RabbitAdminServiceSupport

如果用户相对这个类进行扩展，需要在classpath:META-INF/services目录下以接口名为文件名（即com.v.inf.mq.client.admin.RabbitAdminService）
文件内容为
```
#等号后即实现类全限定名
default=com.xxx.RabbitAdminServiceImpl
```

## TASK部署

叮咚：如果不使用事物消息，则不需要部署Task

* TASK依赖于数据库表来进行消息重试，需要先初始化数据库
```sql
CREATE DATABASE IF NOT EXISTS mq;
CREATE TABLE mq.message (
 `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
 `message_id` varchar(255) NOT NULL COMMENT '消息id',
 `subject` varchar(255) NOT NULL COMMENT '消息主题',
 `content` TEXT NOT NULL COMMENT '消息属性',
 `try_count` INT NOT NULL DEFAULT 0 COMMENT '重试次数',
 `next_retry` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP()  COMMENT '下次重试时间',
 `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() COMMENT '创建时间',
 `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_message_id` (`message_id`),
  KEY `idx_next_retry` (`next_retry`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8
```
* TASK任务整合了Quartz来进行分布式调度，所以还需要初始化Quartz数据库表，初始化SQL放置在vmq-task/src/main/resources/init目录中，需要手动执行SQL来进行初始化

以上两个初始化步骤完成后，即可开启TASK来进行消息重试补偿

---
