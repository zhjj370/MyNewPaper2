package org.zhjj370.ga;


import org.zhjj370.ga.element.ElePosition;
import org.zhjj370.ga.element.EleWorker;

import java.util.List;
import java.util.Random;

/**
 *
 */
public class ChromosomeForPaper {
    private int[] geneForWorker;//基因序列1-工人排序
    private int[] geneForTask;//基因序列2-任务排序
    private double score;//对应的函数得分
    private static Random rand = new Random();
    private Match match;

    public double getScore() {
        return score;
    }

    public int[] getGeneForTask() {
        return geneForTask;
    }

    public int[] getGeneForWorker() {
        return geneForWorker;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setGeneForWorkerX(int x, int value){
        geneForWorker[x] = value;
    }

    public void setGeneForTaskX(int x, int value){
        geneForTask[x] = value;
    }

    public Match getMatch() {
        Match match = new Match(geneForWorker,geneForTask,Const.getaConst().get_Table1());
        match.getResult();
        return match;
    }

    /**
     * @param workerSize
     * @param taskSize
     * 随机生成基因序列
     */
    public ChromosomeForPaper(int workerSize, int taskSize) {
        if (workerSize <= 0 || taskSize <= 0) {
            return;
        }
        initGeneSize(workerSize,taskSize);

        geneForWorker = getSequence(workerSize);
        geneForTask = getSequence(taskSize);
        calculateFitness();
    }

    public ChromosomeForPaper(int[] workers, int[] tasks) {
        this.geneForWorker = workers;
        this.geneForTask = tasks;
        calculateFitness();
    }

    /**
     * @Description: 适应度计算
     */
    private void calculateFitness(){
        Const c = Const.getaConst();
        List<ElePosition> positions = c.getElePositionlist(); //位置列表
        List<EleWorker> workers = c.getEleWorkerList();//工人列表
        Match match = new Match(geneForWorker,geneForTask,c.get_Table1());
        double score1 = match.Fitness(); //匹配合适度得分
        int[] position2Worker = match.getPosition2Worker();
        //轮询任务表单并计算任务总体价值
        for(int i=0;i< position2Worker.length;i++){
            if(position2Worker[i] == -1){
                int idTask = positions.get(i).getIdtask();
                c.getEleTaskList().get(idTask).setaMatch(-1);
            }
            else{
                int idTask = positions.get(i).getIdtask();
                int idWorker = position2Worker[i];
                c.getEleTaskList().get(idTask).setaMatch(idWorker);//配对的工人
                if(workers.get(idWorker).getNeedTime() > c.getEleTaskList().get(idTask).getNeedT()){
                    c.getEleTaskList().get(idTask).setNeedT(workers.get(idWorker).getNeedTime());
                }
            }
        }
        //扫描轮询后的任务
        double score2 = c.calculateTaskValue();
        //
        this.score = score1*score2; //总得分
    }

    /**
     * 生成一个新基因
     */
    public ChromosomeForPaper() {

    }

    /**
     * @param workerSize
     * @param taskSize
     * @Description: 初始化基因长度
     */
    private void initGeneSize(int workerSize, int taskSize) {
        if (workerSize <= 0 || taskSize <= 0) {
            return;
        }
        geneForWorker = new int[workerSize];
        geneForTask = new int[taskSize];
    }

    public static int[] getSequence(int maxnum) {
        int[] sequence = new int[maxnum];
        for(int i = 0; i < maxnum; i++){
            sequence[i] = i;
        }
        Random random = new Random();
        for(int i = 0; i < maxnum; i++){
            int p = random.nextInt(maxnum);
            int tmp = sequence[i];
            sequence[i] = sequence[p];
            sequence[p] = tmp;
        }
        random = null;
        return sequence;
    }


    public void mutation(int mutationNum) {
        //允许变异
        int size1 = geneForWorker.length;
        for (int i = 0; i < mutationNum; i++) {
            //寻找变异位置
            int at = ((int) (Math.random() * size1)) % size1;
            int bt = ((int) (Math.random() * size1)) % size1;
            //变异后的值
            int a = geneForWorker[at];
            geneForWorker[at] =  geneForWorker[bt];
            geneForWorker[bt] =  a;
        }
        int size2 = geneForTask.length;
        for (int i = 0; i < mutationNum; i++) {
            //寻找变异位置
            int at = ((int) (Math.random() * size2)) % size2;
            int bt = ((int) (Math.random() * size2)) % size2;
            //变异后的值
            int a = geneForTask[at];
            geneForTask[at] =  geneForTask[bt];
            geneForTask[bt] =  a;
        }
    }

    public static void main(String[] args) {
        ChromosomeForPaper chromosomeForPaper= new ChromosomeForPaper(11,10);
    }


}
