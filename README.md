

# Introduction

> Written at the beginning: My research direction is manufacturing control system. I am not a professional software engineer, so my programming code may not be easy to read. If this troubles you, I am very sorry.



* This is a simulation program written in JAVA. The version of jdk is 1.8.0_221.

* This simulation program is related to the paper "***\*Research on Workers Integration in Smart Factories with Multi-Agent Control System\****".

* Since the paper is not published, the principle will not be described in detail for now. This article only briefly introduces how to use it.

  

# Instructions for use

You can define parameters in `Const.java` at `org.zhjj370.ga` package.  `task.xml` and `worker.xml` define the attributes of workers and tasks respectively.  

The match between them is as follows, which is defined in `Const.java`.

```java
private final int[][] _Table1 = {
        {10,10,10,10,10,10, 9, 9, 9,10},
        { 9, 8, 8, 8, 9, 9,10,10, 9, 9},
        { 9, 9, 9, 9, 8, 8,10,10,10, 9},
        {10, 9, 9, 9, 9, 9, 9, 9,10,10},
        { 7, 0, 7, 7, 0, 6, 0, 3, 6, 0},
        { 6, 0, 7, 7, 0, 6, 0, 6, 5, 7},
        { 7, 0, 0, 0, 0, 7, 0, 5, 0, 8},
        { 5, 0, 6, 6, 0, 5, 0, 0, 7, 6},
        { 6, 0, 7, 7, 0, 6, 0, 3, 5, 4},
        { 7, 0, 7, 7, 0, 6, 0, 3, 5, 8},
        { 8, 0, 6, 6, 0, 7, 0, 7, 6, 0}};
```

With reference to the data in the paper, relevant variables can be defined before simulation.

```java
private final double _a = 0.7; //交货期临近指数的系数
private final double _b = 0.3; //已经等待时间的系数

private final int _multiple_x = 1; //改变规模，worker的倍数
private final int _multiple_y = 1; //改变规模，task的倍数

private final int _popSize = 30; // 种群规模
private final int _maxIterNum = 100;// 最大迭代次数
private final double _mutationRate = 0.01;//基因变异的概率
private final int _maxMutationNum = 3;//最大变异步长
```

`_multiple_x` and `_multiple_y`can be used to change the scale of workers and tasks. 

The debugging of this example is carried out in ideal. Debugging `App.java` can be simulated. After simulation, the results will be printed, and  picture results are stored in the `output` folder









