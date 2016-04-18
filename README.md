
 0. [Models](#0)
	 1. [InquiryOrder](#01)
	 2. [InquiryOrderEvaluation](#02)
 1. [接口](#1)
	 1. [根据`id`获取普通用户`userId`的问诊单`InquiryOrder`](#101)
	 2. [创建问诊单](#102)
	 3.  [对该问诊单所提供的服务进行评价](#103)
	 4. [获取用户所有的问诊单](#104)
	 5. [上拉加载用户**未完成的**问诊单](#105)
	 6. [下拉刷新用户**未完成的**问诊单列表](#106)
	 7. [上拉加载用户**完成的**问诊单](#107)
	 8. [下拉刷新用户**完成的**问诊单列表](#108)

<span id="0"></span> 
# Models
---
<span id="01"></span>
## InquiryOrder
---
问诊单Model
### Json格式
---

```
{
  "id":123,
  "version":1,
  "createTime":1234444,
  "acceptTime":123144,
  "finishTime":123444,
  "type":"FREE_INQUIRY",
  "state":"NEW",
  "price":50.0,
  "payment":50.0,
  
  "doctorId":123,
  "userId":123,
  "conversationId":123,
  "patientInfo" {
    "age":12,
    "gender":"MALE"
  },
  "content":"this is a inquiry question?",
  "inquiryOrderEvaluation":{
    "time":12313123,
    "ratio":5.0,
    "content":"this is a evaluation"
  },
  "departments":["内科"]
}
```

### 字段说明
---
返回字段值 | 字段类型  | 字段说明
---|--- | --- | --- 
id | int64 | 问诊单identity
version | int32 | 该资源版本号
createTime | int64 | 问诊单创建时间
acceptTime | int64 | 问诊单被医生接受时间
finishTime | int64 | 问诊单完成时间
type       | enum  |问诊单类型`["FREE_INQUIRY", "CHARITY_INQUIRY", "PAY_INQUIRY"]`,分别表示*免费问诊单*，*义诊问诊单*, *付费问诊单* 
state      | enum  |问诊单状态 `["NEW","DISPATCHED","ANSWERED","FINISH","RATED"]`, 分别表示*新建*，*已分配*，*已被回复*，*已经完成*，*已被评价*
price | float | 问诊单价格
payment | float | 问诊单实际付费
doctorId| int64 | 接受该问诊单的医生Id
userId  | int32 | 创建问诊单用户Id
conversationId | int64 | 该问诊单对应的对话Id
patientInfo  | POJO | 病人信息
age | int32 | 病人年龄
gender | enum | 病人性别，`[MALE, FEMALE, UNKNOWN]`, 分别表示男、女、未知。
content | string | 问诊单的问题
inquiryOrderEvaluation | POJO | 对该次服务的评价
time| int64 | 评价时间
ratio| float | 评价分数，5分制
content| string | 评价内容
departments | ARRAY_STRING | 问诊单所属科室


<span id="02"></span>
## InquiryOrderEvaluation
---
问诊单评价Model
### Json格式
---

```
{
  "time":12313123,
  "ratio":5.0,
  "content":"this is a evaluation"
}
```

### 字段说明
---
返回字段值 | 字段类型  | 字段说明
---|--- | --- | --- 
time| int64 | 评价时间
ratio| float | 评价分数，5分制
content| string | 评价内容


<span id="1"></span> 
# 接口
---
<span id="101"></span> 
## /users/{userId}/inquiries/{id}

---
根据`id`获取普通用户`userId`的问诊单`InquiryOrder`

###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围 | 说明
---|--- |----|----
userId  | true | int32 | 用户Id
id      | true | int64 | 需要获取的问诊单Id
### 返回结果
---
[`InquiryOrder`](#01)


<span id="102"></span> 
##/users/{userId}/inquiries
---
创建问诊单

###  URL
---

### 支持格式
---
`JSON`

### HTTP请求方式
---
`POST`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围 | HTTP component|说明
---|--- |----|----|----
userId  | true | int32|PathVariable | 用户Id
InquiryOrder | true | POJO |HTTP body| 创建InquiryOrder的信息：type, price[选填], payment[选填],doctorId[选填]，userId，patientInfo，content，departments[选填]

### 返回结果
---
返回创建成功后的详细信息
[`InquiryOrder`](#01)

<span id="103"></span> 
## /users/{userId}/inquiries/{inquiryId}/ratings
---
对该问诊单所提供的服务进行评价

###  URL
---

### 支持格式
---
`JSON`

### HTTP请求方式
---
`POST`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP component | 说明
---|--- |----|----|----
userId  | true | int32| PathVariable | 用户Id
inquiryId      | true | int64 |PathVariable| 需要评价的问诊单
[`InquiryOrderEvaluation`](#02)| true | POJO |HTTP Body| 评价内容

### 返回结果
---
[`InquiryOrderEvaluation`](#02)


<span id="104"></span> 
## /users/{userId}/inquiries

---
获取用户所有的问诊单

###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
userId  | true | int32 |PathVariable| 用户Id

### 返回结果
---
[`List<InquiryOrder>`](#01)



<span id="105"></span> 
## /users/{userId}/inquiries

---
上拉加载用户未完成的问诊单

###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围 |HTTP Component| 说明
---|--- |----|----|----
userId  | true | int32|PathVariable| 用户Id
lastOrderId | true | int64 |QueryString| 从该问诊单id开始加载
size        | true | int32 | QueryString|加载的数量
state       | true | string |QueryString| 值为`underway`，表示进行中的
### 返回结果
---
[`List<InquiryOrder>`](#01)



<span id="106"></span> 
## /users/{userId}/inquiries
---
下拉刷新用户未完成的问诊单列表
###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围 |HTTP Component| 说明
---|--- |----|----|---
userId  | true | int32 |PathVariable| 用户Id
firstOrderId | true | int64|QueryString |加载用户在该问诊单之后的所有进行中问诊单
state         | true | string |QueryString| 值为 `underway`，表示进行中的
## 返回结果
---
[`List<InquiryOrder>`](#01)


<span id="107"></span> 
## /users/{userId}/inquiries

---
上拉加载用户**完成的**问诊单

###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围 |HTTP Component| 说明
---|--- |----|---- | ----
userId  | true | int32 |PathVariable| 用户Id
lastOrderId | true | int64| QueryString| 从该问诊单id开始加载
size        | true | int32 |QueryString| 加载的数量
state       | true | string |QueryString| 值为`finish`，表示完成的

## 返回结果
---
[`List<InquiryOrder>`](#01)


<span id="108"></span> 
## /users/{userId}/inquiries
---
下拉刷新用户完成的问诊单列表
###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围 | HTTP Component|说明
---|--- |----|----|----
userId  | true | int32 |PathVariable| 用户Id
firstOrderId | true | int64 |QueryString|加载用户在该问诊单之后的所有已经完成的问诊单
state         | true | string |QueryString | 值为 `finish`，表示进行中的

### 返回结果
---
[`List<InquiryOrder>`](#01)


<span id="109"></span> 
## /doctors/{doctorId}/inquiries/{id}
---
依据`id`获取医生`doctorId` 接受的问诊单


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|---- |----
doctorId  | true | int64 | PathVariable|医生Id
id | true | int64 |PathVariable|问诊单Id
### 返回结果
---
[`InquiryOrder`](#01)


<span id="110"></span> 
## /doctors/{doctorId}/inquiries/{id}
---
领取问诊单


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`POST`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
doctorId  | true | int64 |PathVariable |医生Id
id | true | int64 |PathVariable |问诊单Id
action|true|string|QueryString|值为`ACCEPT`，表示领取动作

### 返回结果
---
[`InquiryOrder`](#01)


<span id="111"></span> 
## /doctors/{doctorId}/inquiries/{id}
---
领取问诊单


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`POST`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
doctorId  | true | int64 |PathVariable |医生Id
id | true | int64 |PathVariable |问诊单Id
action|true|string|QueryString|值为`ACCEPT`，表示领取动作

### 返回结果
---
[`InquiryOrder`](#01)

<span id="112"></span> 
## /doctors/{doctorId}/inquiries/{id}
---
医生结束问诊单服务


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`POST`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
doctorId  | true | int64 |PathVariable |医生Id
id | true | int64 |PathVariable |问诊单Id
action|true|string|QueryString|值为`FINISH`，表示结束服务动作

### 返回结果
---
[`InquiryOrder`](#01)

<span id="112"></span> 
## /doctors/{doctorId}/inquiries/{id}
---
医生结束问诊单服务


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`POST`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
doctorId  | true | int64 |PathVariable |医生Id
id | true | int64 |PathVariable |问诊单Id
action|true|string|QueryString|值为`FINISH`，表示结束服务动作

### 返回结果
---
`Void`

<span id="113"></span> 
## /doctors/{doctorId}/inquiries
---
获取医生所有的问诊单


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
doctorId  | true | int64 |PathVariable |医生Id

### 返回结果
---
[`List<InquiryOrder>`](#01)

<span id="113"></span> 
## /doctors/{doctorId}/inquiries
---
获取医生所有的问诊单


###  URL
---

### 支持格式
---
`JSON`
### HTTP请求方式
---
`GET`

### 是否需要登录
---
是

### 请求参数
---

参数 | 必选  | 类型及范围|HTTP Component | 说明
---|--- |----|----|----
doctorId  | true | int64 |PathVariable |医生Id

### 返回结果
---
[`List<InquiryOrder>`](#01)

