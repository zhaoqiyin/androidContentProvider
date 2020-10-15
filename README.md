# androidContentProvider

这是android content provider demo，里面包括2个应用。

## 1 ContentProvider
该应用是content provider的提供方。存储的数据类型位database。提供了增删改查。

## 2 UseContentProvider
该应用是content provider的使用方法。实现了增删改查方法。默认数据库是有2组数据。1 haha, 2 heihei

- 增 insert  默认往数据库最后插入一条数据，插入的name都为Jack
- 删 delete 默认删除第3条数据
- 改 update 默认更改第3条数据，把name更改为Jack2
- 查 query 可以根据输入框输入的id进行查询

使用说明：
1. 先编译ContentProvider并打开
2. 再编译UseContentProvider，然后打开进行操作
