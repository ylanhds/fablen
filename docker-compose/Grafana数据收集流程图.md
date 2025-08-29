

1. Prometheus、Grafana 和 mysqld-exporter 协同工作的流程图及文字说明：

```mermaid
sequenceDiagram
participant M as MySQL Database
participant E as mysqld-exporter
participant P as Prometheus
participant G as Grafana

    Note over E: 持续监控MySQL指标
    loop 定时抓取
        E->>M: 查询状态变量<br>(SHOW GLOBAL STATUS/VARIABLES)
        M-->>E: 返回指标数据
        E->>E: 转化为Prometheus格式<br>(暴露HTTP端点:9104/metrics)
    end

    Note over P: 定时拉取+存储
    loop 定时抓取(scrape_interval)
        P->>E: 访问/metrics端点
        E-->>P: 返回指标数据
        P->>P: 存储到时序数据库(TSDB)
    end

    Note over G: 可视化查询
    G->>P: 发送PromQL查询请求
    P-->>G: 返回指标数据
    G->>G: 渲染图表/仪表盘


```

1.1 极简流程图

```angular2html
+------------------+
|     MySQL        |  ← 通过SQL查询监控指标
+------------------+
        ↓
+------------------+
| mysqld-exporter  |  ← 暴露 /metrics (端口:9104)
+------------------+
        ↓
+------------------+
|   Prometheus     |  ← 定时拉取 + 存储 (TSDB)
+------------------+
        ↓
+------------------+
|    Grafana       |  ← 可视化 (PromQL查询)
+------------------+
```
