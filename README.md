# VMQ
## 简介
VMQ是基于RabbitMQ开发，目的是为了提升业务开发效率，支持事务消息、延迟消息以及便利的并发配置

## 架构

## 特征
* 简单的配置
* 延迟消息
* 事务消息
* 灵活的消息重试
* 支持Consumer并发配置
* 基于SPI进行扩展

## 基本配置
无论是生产者还是消费者，都需要连接rabbit broker，VMQ通过读取配置文件来得到用户的配置

VMQ默认读取classpath根路径下的vmq.properties文件

文件位置也可以通过虚拟机启动参数来配置，如 -Dvmq.properties.path="/x/xx/x.properties"

```properties
mq.connectionFactory.hosts=xxx.xxx.xxx.xxx
mq.connectionFactory.username=user
mq.connectionFactory.password=password
```

## Producer配置
### 概述
VMQ的Producer支持事务消息，保证数据库操作和消息具有事务性（全部成功或者全部失败），同时消息的发送是异步的不会阻塞业务操作。

如果不需要事务消息，只需配置com.v.inf.mq.client.producer.DirectMessageProducer作为类型来初始化即可(注解@VMQProducer目前暂不支持)

事务消息还需要配置Task任务来进行消息的失败补偿，详见Task章节的说明
### 配置
#### 注解配置
```java
@EnableVMQ(producers = @VMQProducer)
```
#### XML配置

```xml
<bean id="messageProducer" class="com.v.inf.mq.client.producer.SingleDbMessageProducer">
        <property name="dataSource" ref="dataSource"/>
</bean>
```

## Consumer配置
### Annotation
使用注解前需要先开启注解配置，有两种方案

* 将@EnableVMQ配置到Spring可以扫描到的类上
```java
@EnableVMQ
```
 
* 配置XML元素 &lt;mq:annotation-driven/> 以开启注解配置

---

VMQ的消息消费者注解配置是非常便捷的

只需在Spring容器托管的Bean的方法上标注@MQConsumer即可配置消息消费者

注意:方法必须只有一个参数且类型为com.v.inf.mq.Message

```java 
@MqConsumer(subjects = "subjectName", group = "groupName")
public void onMessage(Message message) {
        // todo do something
    }
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
<bean id="printMessageListener" class="com.v.PrintMessageListener"/>
<mq:consumer>
     <mq:listener subject="test"
                     group="test"
                     ref="printMessageListener"/>
</mq:consumer>
```


## TASK部署
