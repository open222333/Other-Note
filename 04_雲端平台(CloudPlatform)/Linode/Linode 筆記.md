# Linode 筆記

## 參考資料

[官方文檔](https://www.linode.com/docs/)

# Linodes

```
Distributions:佈局

Images:選擇系統
Region:選擇伺服器地區

Linode Plan
	Dedicated CPU
	專用 CPU 實例適用於需要一致性能的全負載工作負載。

	Shared CPU
	實例適用於中型工作負載，並且是性能、資源和價格的良好組合。

	High Memory
	高內存實例比其他資源更喜歡 RAM，並且可能有利於內存消耗量大的用例，如緩存和內存數據庫。所有 High Memory 計劃都使用專用 CPU 內核。

	Linode GPU
	Linode GPU 計劃的可用性有限，在您提出請求時可能不可用。訪問這些服務可能需要一些額外的驗證。


Private IP: 開啟內網IP功能，讓內網的伺服器可以溝通。
```

# Firewall

[防火牆](https://www.linode.com/docs/guides/getting-started-with-cloud-firewall/)

```
選擇自建的規則

Rules:
	Inbound Rules:入站規則 Accept(允許) Drop(禁止)

	Outbound Rules:出站規則


Linodes:
伺服器加入規則
```

##################################
            測試連通性 網路
##################################
測試連通性
通過 SSH 登錄您的 Linode。

使用該ip工具確保應用了您在上面設置的地址：

root@localhost:~# ip addr | grep inet
inet 127.0.0.1/8 scope host lo
inet6 ::1/128 scope host
inet 198.51.100.5/24 brd 198.51.100.255 scope global eth0
inet6 2600:3c02::f03c:91ff:fe24:3a2f/64 scope global
inet6 fe80::f03c:91ff:fe24:3a2f/64 scope link
確認您的/etc/resolv.conf存在且正確。其內容將根據 Linux 發行版而有所不同。

root@localhost:~# cat /etc/resolv.conf
nameserver 8.8.8.8
nameserver 2001:4860:4860::8888
domain members.linode.com
options rotate
嘗試 ping 某些內容以確認您已通過 IPv4 和 IPv6 實現完全連接。

ping -c 3 google.com
ping6 -c 3 ipv6.google.com

##################################
        Linode 添加私有IP
##################################
配置設定
https://www.linode.com/docs/guides/linux-static-ip-configuration/
添加IP
https://www.linode.com/docs/guides/managing-ip-addresses/#adding-private-ip-addresses


配置設定
    vim /etc/sysconfig/network-scripts/ifcfg-eth0

    # Add a private IPv4 address.
    IPADDR1=192.168.143.152
    PREFIX1=17

應用網路設定 
    重開機
    或
    centos 7
        systemctl restart network.service
        systemctl restart network

檢測是否有通
    curl -v ip:port

##################################
        Linode 換ip
##################################
創建 Create一個最小的方案主機 5美金
更換 與要更換ip的主機進行更換 Linodes -> Network -> IP Transfer
重啟 更換後的主機reboot
刪除 最小方案主機Delete
