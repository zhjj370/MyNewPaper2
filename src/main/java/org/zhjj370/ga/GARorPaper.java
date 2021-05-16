package org.zhjj370.ga;

import org.zhjj370.plot.DataPlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GARorPaper {
    private List<ChromosomeForPaper> population = new ArrayList<ChromosomeForPaper>();
    /**种群数量*/
    private int popSize;
    /**染色体最大长度*/
    private int geneSizeForWorkers;
    private int geneSizeForTasks;
    /**最大迭代次数*/
    private int maxIterNum;
    /**基因变异的概率*/
    private double mutationRate;
    /**最大变异步长*/
    private int maxMutationNum;
    /**当前遗传到第几代*/
    private int generation = 1;

    private double bestScore;//最好得分
    private double worstScore;//最坏得分
    private double totalScore;//总得分
    private double averageScore;//平均得分

    private double minAverageScore=0;
    private double maxAverageScore=0;

    private List<Double> averageFitnessList = new ArrayList<>();

    //private double x; //记录历史种群中最好的X值
    private double y; //记录历史种群中最好的Y值
    private int geneI = 0;//x y所在代数
    private ChromosomeForPaper bestChromosome;

    private DynamicDataWindow ddWindow;
    private long tp;




    public List<Double> getAverageFitnessList() {
        return averageFitnessList;
    }

    public double getMinAverageScore() {
        return minAverageScore;
    }

    public double getMaxAverageScore() {
        return maxAverageScore;
    }

    public int getGeneration() {
        return generation;
    }

    public ChromosomeForPaper getBestChromosome() {
        return bestChromosome;
    }

    public GARorPaper(int geneSizeForWorkers, int geneSizeForTasks, int popSize, int maxIterNum, double mutationRate, int maxMutationNum){
        this.geneSizeForWorkers = geneSizeForWorkers;
        this.geneSizeForTasks = geneSizeForTasks;
        this.popSize = popSize;
        this.maxIterNum = maxIterNum;
        this.mutationRate = mutationRate;
        this.maxMutationNum = maxMutationNum;
    }

    public void setDdWindow(DynamicDataWindow ddWindow) {
        this.ddWindow = ddWindow;
    }

    public void caculte() {
        //1.初始化种群
        init();
        for(generation = 1; generation <= maxIterNum; generation++) {
            //2.计算种群适应度
            caculteScore();
            //System.out.println("3>验证阈值...");
            //4.种群遗传
            evolve();
            //5.基因突变
            mutation();
            xprint();
        }

    }

    /**
     * @Description: 输出结果
     */
    private void xprint() {
        System.out.println("--------------------------------");
        System.out.println("the generation is:" + generation);
        System.out.println("the best y is:" + bestScore);
        System.out.println("the worst fitness is:" + worstScore);
        System.out.println("the average fitness is:" + averageScore);
        System.out.println("the total fitness is:" + totalScore);

        /*long millis=System.currentTimeMillis();
        if (millis-tp>300) {
            tp=millis;
//        	ddWindow.setyMaxMin(y-10);
            ddWindow.addData(millis, y);
        }

        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    //---------------------------------------------------------------------//
    /**
     * @Description: 初始化种群
     */
    private void init() {
        //System.out.println("1>生成初始种群...");
        //ddWindow.setVisible(true);
        population = new ArrayList<ChromosomeForPaper>();
        for (int i = 0; i < popSize; i++) {
            ChromosomeForPaper chro = new ChromosomeForPaper(geneSizeForWorkers,geneSizeForTasks);
            population.add(chro);
        }
    }

    //---------------------------------------------------------------------//
    /**
     * @Description: 计算种群适应度
     */
    private void caculteScore() {
        //System.out.println("2>计算种群适应度...");
        bestScore=(double)population.get(0).getScore();
        worstScore=(double)population.get(0).getScore();
        totalScore = 0;
        for (ChromosomeForPaper chro : population) {
            if (chro.getScore() > bestScore) { //设置最好基因值
                bestScore = chro.getScore();
                if (y < bestScore) {
                    y = bestScore;
                    geneI = generation;
                    bestChromosome = chro;
                }
            }
            if (chro.getScore() < worstScore) { //设置最坏基因值
                worstScore = chro.getScore();
            }
            totalScore += chro.getScore();
        }
        averageScore = totalScore / popSize;
        averageScore = averageScore > bestScore ? bestScore : averageScore;
        if(averageScore>maxAverageScore) maxAverageScore=averageScore;
        if(minAverageScore == 0) minAverageScore=averageScore;
        if(averageScore<minAverageScore) minAverageScore=averageScore;

        averageFitnessList.add(averageScore);
    }

    //---------------------------------------------------------------------//
    /**
     * @Description:种群进行遗传
     */
    private void evolve() {
        List<ChromosomeForPaper> childPopulation = new ArrayList<ChromosomeForPaper>();
        //生成下一代种群
        while (childPopulation.size() < popSize) {
            ChromosomeForPaper parent1 = getParentChromosome();
            ChromosomeForPaper parent2 = getParentChromosome();
            List<ChromosomeForPaper> children = genetic(parent1, parent2);
            if (children != null) {
                for (ChromosomeForPaper chro : children) {
                    childPopulation.add(chro);
                }
            }
        }
        //System.out.println("4.2>产生子代种群...");
        //新种群替换旧种群
        population.clear();
        population = childPopulation;
    }

    /**
     * @return
     * Email: zhjj370@nuaa.edu.cn
     * @Description: 轮盘赌法选择可以遗传下一代的染色体
     */
    private ChromosomeForPaper getParentChromosome (){
        //System.out.println("4.1>筛选父代种群一次...");
        while (true) {
            double slice = Math.random() * totalScore;
            double sum = 0;
            for (ChromosomeForPaper chro : population) {
                sum += chro.getScore();
                //System.out.println("测试：sum="+sum+"  chro.getScore()="+chro.getScore());
                if (sum > slice && chro.getScore() >= averageScore) {
                    return chro;
                }
            }
        }
    }
    /**
     * @param p1
     * @param p2
     * @Description: 遗传产生下一代
     */
    public List<ChromosomeForPaper> genetic(ChromosomeForPaper p1, ChromosomeForPaper p2) {
        if (p1 == null || p2 == null) { //染色体有一个为空，不产生下一代
            return null;
        }
        if (p1.getGeneForWorker() == null || p2.getGeneForWorker() == null) { //染色体有一个没有基因序列，不产生下一代
            return null;
        }
        if (p1.getGeneForWorker().length != p2.getGeneForWorker().length) { //染色体基因序列长度不同，不产生下一代
            return null;
        }

        //Order Crossover for "geneForWorker"
        //随机产生交叉互换位置
        int workerSize = p1.getGeneForWorker().length;
        int taskSize = p1.getGeneForTask().length;
        int a = ((int) (Math.random() * workerSize)) % workerSize;
        int b = ((int) (Math.random() * workerSize)) % workerSize;
        int minWorker = a > b ? b : a;
        int maxWorker = a > b ? a : b;
        if(minWorker==maxWorker){
            if(minWorker==0){
                maxWorker=maxWorker+1;
            }
            else{
                minWorker = minWorker-1;
            }
        }

//        System.out.println("--------------------------");
//        System.out.println(minWorker + " - "  + maxWorker);
        a = ((int) (Math.random() * taskSize)) % taskSize;
        b = ((int) (Math.random() * taskSize)) % taskSize;
        int minTask = a > b ? b : a;
        int maxTask = a > b ? a : b;
        if(minTask==maxTask){
            if(minTask==0){
                maxTask=maxTask+1;
            }
            else{
                minTask = minTask-1;
            }
        }
//        System.out.println(minTask + " - "  + maxTask);

        int[] c1WorkerGene = orderCrossover(minWorker,maxWorker,p1.getGeneForWorker(),p2.getGeneForWorker());
        int[] c1TaskrGene = orderCrossover(minTask,maxTask,p1.getGeneForTask(),p2.getGeneForTask());
        ChromosomeForPaper c1 = new ChromosomeForPaper(c1WorkerGene,c1TaskrGene);

        int[] c2WorkerGene = orderCrossover(minWorker,maxWorker,p2.getGeneForWorker(),p1.getGeneForWorker());
        int[] c2TaskGene = orderCrossover(minTask,maxTask,p2.getGeneForTask(),p1.getGeneForTask());
        ChromosomeForPaper c2 = new ChromosomeForPaper(c2WorkerGene,c2TaskGene);

        List<ChromosomeForPaper> list = new ArrayList<ChromosomeForPaper>();
        list.add(c1);
        list.add(c2);
        return list;
    }

    /**
     * Order Crossover for "geneForWorker"
     * @param min
     * @param max
     * @param p1
     * @param p2
     * @return
     */
    public int[] orderCrossover(int min, int max, int[] p1, int[] p2){
        int[] child = new int[p1.length];
        int workerSize = child.length;
        List<Integer> xP2 = new ArrayList<Integer>();
        for (int i = 0; i < workerSize; i++) {
            for (int j = min; j< max; j++){
                if(p2[i] == p1[j]){
                    break;
                }
                if(j == (max-1)){
                    xP2.add(p2[i]);
                }
            }
        }
        Iterator<Integer> xP2Iterator = xP2.listIterator();
//        System.out.println(xP2.size());
        for (int i = 0; i < workerSize; i++) {
            if(i>=min && i<max){
                child[i] = p1[i];
            }
            else{
                child[i] = xP2Iterator.next();
            }
        }
        return child;
    }

    //---------------------------------------------------------------------//
    /**
     * 基因突变
     */
    private void mutation() {
        //System.out.println("5>基因突变...");
        for (ChromosomeForPaper chro : population) {
            if (Math.random() < mutationRate) { //发生基因突变
                int mutationNum = (int) (Math.random() * maxMutationNum);
                chro.mutation(mutationNum);
            }
        }
    }

    /*public static void main(String[] args) throws IOException {
        Const c = Const.getaConst();
        int workerLength = c.getEleWorkerList().size();
        int positionLength = c.getElePositionlist().size();
        GARorPaper ga= new GARorPaper(workerLength,positionLength,c.get_popSize(),c.get_maxIterNum(),c.get_mutationRate(),c.get_maxMutationNum());
        //ga.setDdWindow(new DynamicDataWindow("Worker-Match-Task"));
        long startTime=System.currentTimeMillis();   //获取开始时间
        ga.caculte();
        long endTime=System.currentTimeMillis(); //获取结束时间
        System.out.println("==========================");
        System.out.println("The running time of the AG： "+(endTime-startTime)+"ms");
        //
        List<Double> averageFitnessList = ga.getAverageFitnessList();
        DataPlot myDataPlot = new DataPlot();
        myDataPlot.plotAverageFitness(averageFitnessList,ga.getGeneration(),ga.getMinAverageScore(),ga.getMaxAverageScore());
        //
        ChromosomeForPaper chromosome = ga.getBestChromosome();
        chromosome.getMatch();
    }*/
}
