
# 拉取新代码
git pull

# 构建
docker build -t bitcoin-j-verification  .

# 移除已有的镜像
docker rm -f bitcoin-j-verification

# 缓存目录: 避免每次拉取Maven依赖;
m2path=/tmp/m2cache
mkdir -p $m2path/.m2

# 后台模式运行
docker run -v $m2path/.m2:/root/.m2 -d --name bitcoin-j-verification bitcoin-j-verification

# 查看docker日志
docker logs -n 100 -f bitcoin-j-verification

