# bitcoin-j-verification
bitcoin-j的验证者



## 环境搭建


1. 从阿里云或者其他云服务厂商, 购买HW节点的机器

> https://ecs-buy.aliyun.com/ecs#/custom/prepay/cn-hongkong

2. CentOS系统安装Docker

```shell

sudo yum install -y yum-utils

sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

sudo yum install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

```

3. 查看Docker版本信息

```shell

docker -v

# 启动Docker服务
sudo systemctl start docker

# 启动一个展示信息的docker镜像
sudo docker run hello-world

```

4. 安装Git

```shell
sudo yum install -y git

git --version

```

5. 下载代码

```shell

mkdir -p /usr/local/source_code
cd /usr/local/source_code

# 使用 https 链接, 免登录;
git clone https://github.com/renfufei/bitcoin-j-verification.git

```


6. 构建Docker镜像

```shell

cd /usr/local/source_code/bitcoin-j-verification

./build-docker.sh


```


7. 启动 Docker

```shell

docker run --name bitcoin-j-verification -it bitcoin-j-verification


# 或者:后台模式运行
docker run -d --name bitcoin-j-verification bitcoin-j-verification

```




## 其他信息

- [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.0&packaging=jar&jvmVersion=17&groupId=com.cncounter&artifactId=bitcoin-j-verification&name=bitcoin-j-verification&description=bitcoin-j-verification%20project%20for%20Spring%20Boot&packageName=com.cncounter.bitcoin-j-verification&dependencies=lombok,web,jdbc,mybatis,mysql)
- [bitcoinj](https://bitcoinj.org/)
- [bitcoinj examples](https://github.com/bitcoinj/bitcoinj/tree/release-0.16/examples/src/main/java/org/bitcoinj/examples)
- [Install Docker Engine on CentOS](https://docs.docker.com/engine/install/centos/)
- [docker run](https://docs.docker.com/engine/reference/commandline/run/)
