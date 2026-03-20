# 铁路售票系统（微服务 + 前后端）

## 模块划分
- `gateway-service`：统一网关、鉴权入口、限流防护。
- `member-service`：会员信息、常用乘车人、权限。
- `ticket-service`：查座位、查余票、预占座、库存扣减编排。
- `order-service`：订单创建、状态机、订单缓存查询。
- `frontend`：简单页面，支持日期/车次/始发站/终到站下拉选择并调用后端。

## 技术栈
- Spring Cloud Alibaba（Nacos + Sentinel）
- Redis（位图/缓存/去重）
- Kafka（seat-update、stock-update、retry）
- MySQL（订单/座位/库存）
- JWT SSO（可在网关扩展过滤器）

## 核心流程
1. **查询阶段**：`ticket-service` 优先走 Redis，做请求合并（collapsed get）防非恶意缓存穿透。
2. **占座阶段**：前端提交用户/车次/日期/席位；后端 Lua 脚本在 Redis 原子执行预占座、预扣减。
3. **建单阶段**：按规则校验 `userId`、生成带校验位的 `orderId`，落订单表；失败则回滚 Redis。
4. **Seat 落库阶段**：消费 `seat-update`，幂等检查 + 行锁互斥 + 失败重试。
5. **库存更新阶段**：基于位图快照计算受影响区间联动扣减；成功/失败都要做 Redis 对账。

## 长区间优先策略（收益最大化）
- 请求进入队列时计算 `priority = 区间长度 * α + 历史成交权重 * β - 价格弹性损失 * γ`。
- 同级请求按到达时间 FIFO。
- 仅在列车剩余可售比例低于阈值时启用该策略，避免早期过度偏置。

## 启动
1. 导入 `sql/schema.sql` 与 `scripts/bootstrap_data.sql`。
2. 启动 Nacos、Redis、Kafka、MySQL。
3. 在 `backend` 下执行 `mvn spring-boot:run -pl gateway-service` 等。
4. 打开 `frontend/index.html`。
