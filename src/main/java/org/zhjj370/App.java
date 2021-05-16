package org.zhjj370;

import org.zhjj370.ga.ChromosomeForPaper;
import org.zhjj370.ga.Const;
import org.zhjj370.ga.GARorPaper;
import org.zhjj370.plot.DataPlot;

import java.io.IOException;
import java.util.List;

/**
 * @author Zequn ZHANG
 * Debug entry
 */
public class App 
{
    public static void main(String[] args) throws IOException {
        Const c = Const.getaConst();
        int workerLength = c.getEleWorkerList().size();
        int positionLength = c.getElePositionlist().size();
        GARorPaper ga= new GARorPaper(workerLength,positionLength,c.get_popSize(),c.get_maxIterNum(),c.get_mutationRate(),c.get_maxMutationNum());
        //ga.setDdWindow(new DynamicDataWindow("Worker-Match-Task"));

        long startTime=System.currentTimeMillis();   //获取开始时间
        ga.caculte();
        long endTime=System.currentTimeMillis();     //获取结束时间

        System.out.println("==========================");
        System.out.println("The running time of the AG： "+(endTime-startTime)+"ms");
        //
        List<Double> averageFitnessList = ga.getAverageFitnessList();
        DataPlot myDataPlot = new DataPlot();
        myDataPlot.plotAverageFitness(averageFitnessList,ga.getGeneration(),ga.getMinAverageScore(),ga.getMaxAverageScore());
        //
        ChromosomeForPaper chromosome = ga.getBestChromosome();
        chromosome.getMatch();
        c.outAbilityValue();
    }
}
