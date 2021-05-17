# SpringBoot实战派学习笔记



## Wogua_boy



## 用SpringBoot输出Hello World

IDEA菜单栏：

File

New

Project

Default选项

Next

修改Project Name

Next

勾选Lombok和Spring Web

Finish

在demo目录上新建一个HelloWorldController的类

填入代码：

```java
package com.example.demo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @RequestMapping("/hello")
    public String hello () {
        return "Hello,Spring Boot!";
    }
}
```

运行，访问localhost:8080/hello

代码解析：

（1）@RestController：代表这个类是Rest风格的控制器，返回JSON/XML类型的数据。

（2）@RequestMapping：配置URL和方法之间的映射。可注解在类和方法上、注解在方法上的@RequestMapping路径会继承注解在类上的路径。

打包成可执行的JAR包：略。



## MVC模式的Web应用程序

新建一个项目，勾上lombok和Spring Web。

先在pom.xml文件里添加thymeleaf依赖：

```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### 创建实体模型model

创建实体类model，并在model里创建一个User类，表示用户。

```java
package com.example.demo.model;


import lombok.Data;

@Data
public class User {

    private long id;
    private String name;
    private int age;
}
```



### 创建控制器controller

controller层用来实例化model，并传值给视图模板。

demo目录下新建一个controller包，这个包里面存放controller相关类。

```java
package com.example.demo.controller;


import com.example.demo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MVCDemoController {
    @GetMapping ("/mvcdemo")
    public ModelAndView hello () {
        User user = new User();
        user.setName("zhonghua");
        user.setAge(28);

        //定义MVC中的视图模板
        ModelAndView modelAndView = new ModelAndView("mvcdemo");

        //传递user实体对象给视图
        modelAndView.addObject("user",user);
        return modelAndView;
    }
}

```

然后templates目录下新建html文件，展示前端界面。

（一定要templates目录！SpringBoot默认）

取名mvcdemo.html：

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-witdh,initial-scale=1.0">
</head>
<body>
<div>
    <div th:text="${user.name}"></div>
    <div th:text="${user.age}"></div>
</div>
</body>
</html>
```

这样后端取到的数据就会实时在前端展示。

### 数据的自定义验证

这里实现SpringBoot中对数据的快速验证。

首先创建实体User，继承Serializable。

添加依赖：

```xml
<dependency>
   <groupId>org.hibernate</groupId>
   <artifactId>hibernate-validator</artifactId>
   <version>6.0.7.Final</version>
</dependency>
```

加入两个注解：

```java
@NoArgsConstructor
@AllArgsConstructor
```



## 用JDBC连接模板操纵Mysql数据库

### 添加基础配置依赖

```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
```

### 配置properties文件中的数据库连接信息

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1/springboot_demo?usUnicode=true&characterEncodeing=utf-8&serverTimezone=UTC&useSSL=true

spring.datasource.username=root

spring.datasource.password=123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.show-sql=true

```

### 新建实体类RowMapper类，重写mapRow方法

```java
package com.example.demo.Model;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;


import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class User implements RowMapper<User> {

    private int id;
    private String username;
    private String password;

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }
}

```

### 新建测试类，编写测试函数

```java
package com.example.demo;

import com.example.demo.Model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void createUserTable () throws Exception {
        System.out.println("start!");
        String sql = "CREATE TABLE `user`(\n" +
                "`id` int(10) not null auto_increment,\n" +
                "`username` varchar(100) default null,\n" +
                "`password` varchar(100) default null,\n" +
                "primary key(`id`)\n" +
                ") engine = innoDB auto_increment = 1 default charset = utf8;\n" +
                "\n";
        jdbcTemplate.execute(sql);
    }

    @Test
    public void saveUserTest() throws Exception {
        String sql = "insert into user(username,password) values ('zlc','123')";
        int rows = jdbcTemplate.update(sql);
        System.out.println(rows);
    }

    @Test
    public void getUserByName () throws Exception {
        String name = "zlc";
        String sql = "select * from user where username = ?";
        List<User> list = jdbcTemplate.query(sql,new User(),new Object[]{name});
        for (User user : list) {
            System.out.println(user);
        }
    }

}

```

编写完成后点击建表函数旁边的按钮，观察运行后Navicat是否有新表。

## JPA——Java持久层API

### 认识SpringData

Spring Data是Spring的一个子项目，诣在同一和简化各类型数据的持久化存储方法，而不拘于是关系型数据库还是NoSQL数据库。

无论是哪种持久化存储方式，数据访问对象（DAO）都会提供对对象的增加、删除、修改、查询的方法，以及排序和分页的方法等。

SpringData提供了基于这些层面的统一接口，以实现持久化的存储。

SpringData包含多个子模块，主要分为主模块和社区模块。

### 认识JPA

JPA是Java的持久化API，用于对象的持久化。它是一个非常强大的ORM持久化解决方案，免去了使用JDBCTemplate开发的编写脚本工作。JPA通过简单的约定好接口方法的规则自动生成相应的JPQL语句，然后映射成POJO对象。

JPA是一个规范化接口，封装了Hibernate的操作作为默认实现，让用户不通过任何配置就可以完成数据库的操作。

### 使用JPA

要使用JPA，只要加入它的Starter依赖，然后配置数据库连接信息。

添加JPA和Mysql数据库的依赖。

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1/springboot_demo?usUnicode=true&characterEncodeing=utf-8&serverTimezone=UTC&useSSL=true

spring.datasource.username=root

spring.datasource.password=123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5lnnoDBDialect
spring.jpa.show-sql=true
```

### 了解JPA的注解和属性

### 用JPA构建实体数据表

新建Article类，写入以下代码，建一个名为JPA_Article的表。

不用管注解处的报错，运行之后自动生成表。

```java
package com.example.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Table(name = "JPA_Article")
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Column(columnDefinition = "enum('图','图文','文')")
    private String type;


    private Boolean available = Boolean.FALSE;

    @Size(min = 0,max = 20)
    private String keyword;

    @Size(max = 255)
    private String description;

    @Column(nullable = false)
    private String body;

    @Transient
    private List keywordlists;
    public List getKeywordlists () {
        return Arrays.asList(this.keyword.trim().split("|"));
    }
    public void setKeywordlists (List keywordlists) {
        this.keywordlists = keywordlists;
    }
}

```





