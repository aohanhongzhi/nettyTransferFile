# netty transfer file

经过粗略测试，传输一个1.4GB的文件需要31s，也就是速度大概在46MB/s。这个是测试是在本地环境下(server与client同处一台电脑)。因此需要考虑带宽及网络波动，但是毋庸置疑NIO的传输性能确实让人惊讶！
![](./doc/img/netty.png)