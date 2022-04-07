# MedicalGame Documentation

## 4、API文档
应使用restful格式接口，不应使用operationCode(耦合过高)

算法推送-GET：[http://81.70.98.90:8081/MedicalGame/operation]

客户端-POST：[http://81.70.98.90:8081/MedicalGame/OperationServlet]

| 接口功能说明   | 参数                | operationCode | 返回参数 |
|----------|-------------------|---------------|------|
| 注册       | userName&password | 1             |      |
| 登录       | userName&password | 3             |      |
| 语言动作获取   | userName          | 5             |      |
| 保存积分     | userName&points   | 7             |      |
| 获取积分     | userName          | 9             |      |
| 保存游戏进度   | userName&state    | 11            |      |
| 获取游戏进度   | userName          | 13            |      |
| 获取用户详细信息 | userName          | 15            |      |
| 获取用户列表   | userName          | 17            |      |

| 接口功能说明 | 参数            | operationCode | 返回参数 |
| ------------ | --------------- | ------------- | -------- |
| 获取脑电信息 | userName        | 19            |          |
| 保存游戏记录 | userName&record | 53            |          |
| 获取游戏记录 | userName&record | 55            |          |
| 更新用户数据 | ...             | 57            |          |




























