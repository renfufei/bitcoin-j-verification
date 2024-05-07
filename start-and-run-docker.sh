
# 拉取新代码
git pull

# 构建
docker build -t bitcoin-j-verification  .

# 移除已有的镜像
docker rm -f bitcoin-j-verification

# 或者:后台模式运行
docker run -d --name bitcoin-j-verification bitcoin-j-verification

