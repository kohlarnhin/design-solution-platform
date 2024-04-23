echo 压缩文件
zip -r -P 密码 /filePath/file.zip /filePath
echo 开始备份
./coscli cp /filePath cos://桶的别名/filePath/ -r
echo 备份成功