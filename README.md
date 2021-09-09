client
事件 1 注册
数据格式
{
    operationCode:1,   
    userName:"xxx",       
    password:"xxx"       
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":2,
    "errorCode":0,
    "msg":"注册成功"
}
失败
{
    "state":false,
    "operationCode":0,
    "errorCode":-3,
    "msg":"账号以存在"
}
{
    "state":false,
    "operationCode":2,
    "errorCode":500,
    "msg":"服务器错误"
}
-----------------------------------
client
事件 3 登录
数据格式
{
    operationCode:3,   
    userName:"xxx",       
    password:"xxx"       
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":4,
    "errorCode":0,
    "msg":"验证成功"
}
失败
{
    "state":false,
    "operationCode":4,
    "errorCode":-1,
    "msg":"密码错误"
}
{
    "state":false,
    "operationCode":4,
    "errorCode":-2,
    "msg":"账户不存在"
}
-----------------------------------
client
事件 5 客户端请求数据
数据格式
{
    operationCode:5,   
    userName:"xxx",       
    password:"xxx",
    checkType:"xxx",		//需要的数据类型
    storyID:"xxx",
    result:"xx"			事件需要的结果
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":6,
    "checkType":"xxx",		//数据类型 0x01 0x02
    "result":"xxx",		    //字符串格式的数据
    "errorCode":0
    "storyID":"xxx"
}
失败
{
    "state":false,
    "operationCode":6,
    "checkType":"xxx",		//数据类型 0x01 0x02
    "result":"null",
    "storyID":"xxx",
    "errorCode":-1,
    "errorMsg":"username or password error"
}
{
    "state":false,
    "operationCode":6,
    "checkType":key ,		//数据类型 0x01 0x02
    "result":"null",
    "storyID":"xxx",
    "errorCode":-2
    "errorMsg":"key doesn't exist"
}
-----------------------------------
client
事件 7 客户端保存积分
数据格式
{
    operationCode:7,   
    points:xxx		//int类型
    userName:"xxx",       
    password:"xxx",
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":8,
    "errorCode":0,
    "msg":"录入成功"
}
失败
{
    "state":false,
    "operationCode":8,
    "errorCode":-1,
    "errorMsg":"密码错误"
}
{
    "state":false,
    "operationCode":8,
    "errorCode":-2,
    "errorMsg":"账号未注册"
}
{
    "state":false,
    "operationCode":8,
    "errorCode":500,
    "errorMsg":"服务器错误"
}
{
    "state":false,
    "operationCode":8,
    "errorCode":-4,
    "errorMsg":"格式不正确"
}
-----------------------------------
client
事件 9 客户端请求积分
数据格式
{
    operationCode:7,   
    userName:"xxx",       
    password:"xxx"
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":10,
    "points":xxx,		//int类型
    "errorCode":0,
    "msg":"获取成功"
}
失败
{
    "state":false,
    "operationCode":10,
    "result":-1,
    "errorCode":-1,
    "errorMsg":"密码错误"
}
{
    "state":false,
    "operationCode":10,
    "result":-1,
    "errorCode":-2,
    "errorMsg":"账号未注册"
}
{
    "state":false,
    "operationCode":10,
    "result":-1,
    "errorCode":500,
    "errorMsg":"服务器错误"
}
-----------------------------------
client
事件 11 客户端请求保存存档
数据格式
{
    operationCode:7,   
    userName:"xxx",       
    password:"xxx",
    status:"xxx"		//string类型
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":12,
    "errorCode":0,
    "msg":"录入成功"
}
失败
{
    "state":false,
    "operationCode":12,
    "errorCode":-1,
    "errorMsg":"密码错误"
}
{
    "state":false,
    "operationCode":12,
    "errorCode":-2,
    "errorMsg":"账号未注册"
}
{
    "state":false,
    "operationCode":12,
    "errorCode":500,
    "errorMsg":"服务器错误"
}
-----------------------------------
client
事件 13 客户端请求存档
数据格式
{
    operationCode:7,   
    userName:"xxx",       
    password:"xxx"
}
server
返回类型 json
成功
{
    "state":true,
    "operationCode":14,
    "status":"xxx",
    "errorCode":0,
    "msg":"获取成功"
}
失败
{
    "state":false,
    "operationCode":14,
    "result":-1,
    "errorCode":-1,
    "errorMsg":"密码错误"
}
{
    "state":false,
    "operationCode":14,
    "result":-1,
    "errorCode":-2,
    "errorMsg":"账号未注册"
}
{
    "state":false,
    "operationCode":14,
    "result":-1,
    "errorCode":500,
    "errorMsg":"服务器错误"
}
-----------------------------------
算法端
事件 get 接收算法得到的数据
url ID:xxx&Payload:xxx

