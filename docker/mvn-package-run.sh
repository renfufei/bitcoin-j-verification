# 设置时区
# cp /usr/share/zoneinfo/Asia/Shanghai  /etc/localtime
export TZ="Asia/Shanghai"

# 打包Maven项目
mvn clean package -DskipTests

# 运行Spring-Boot项目
mvn spring-boot:run
