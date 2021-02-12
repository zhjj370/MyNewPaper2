package org.zhjj370.ga;

import java.util.Arrays;

public class Match {
    private int[] workers;
    private int[] tasks;
    private int[][] table;

    private int mTasks=0;
    private int nWorkers=0;
    private Boolean used[];
    private int[] position2Worker;


    public Match(int[] workers, int[] tasks, int[][] table){
        this.workers = workers;
        nWorkers = workers.length;
        this.tasks = tasks;
        mTasks = tasks.length;
        this.table = table;

        used = new Boolean[mTasks];
        position2Worker = new int[mTasks];

        Arrays.fill(used,false);
        Arrays.fill(position2Worker,-1);

        for(int i=0; i < nWorkers; i++){
            Find(workers[i]);
        }

//        for(int i=0; i < mTasks; i++){
//            System.out.println("worker: " +  task2Worker[tasks[i]] + " ---> " + "task: " + tasks[i]);
//        }
    }

    public Boolean Find(int x){
        int i,j;
        for (j=0;j<mTasks;j++){    //扫描每个任务
            if (table[x][tasks[j]]>0 && used[tasks[j]]==false)
            //如果有暧昧并且还没有标记过(这里标记的意思是这次查找曾试图改变过该妹子的归属问题，但是没有成功，所以就不用瞎费工夫了）
            {
                used[tasks[j]]=true;
                if (position2Worker[tasks[j]]==-1 || Find(position2Worker[tasks[j]])) {
                    //名花无主或者能腾出个位置来，这里使用递归
                    position2Worker[tasks[j]]=x;
                    return true;
                }
            }
        }
        return false;
    }

    public void getResult(){
        for(int i=0; i < mTasks; i++){
            System.out.println("worker: " +  position2Worker[tasks[i]] + " ---> " + "task: " + tasks[i]);
        }
    }

    public double Fitness(){
        double f = 0.0;
        for(int i : tasks){
            if(position2Worker[i]>=0){
                f = f+ table[position2Worker[i]][i];
            }
        }
        return f;
    }

    public int[] getPosition2Worker() {
        return position2Worker;
    }

    public static void main(String[] args) {
        //int[] workers = {0,1,2,3,4,5,6,7,8,9,10};
        int[] workers = {0,1,3,2,5,4,6,7,8,9,10};
        int[] tasks = {0,1,2,3,4,6,5,7,8,9};
        Match match = new Match(workers,tasks,Const.getaConst().get_Table1());
    }
}
