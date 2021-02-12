package org.zhjj370.ga;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.zhjj370.ga.element.ElePosition;
import org.zhjj370.ga.element.EleTask;
import org.zhjj370.ga.element.EleWorker;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zequn ZHANG
 * 模拟中用到的数据进行预先加载
 */
public class Const {
    //=========================================================================================================//
    //系统中将用到的常量
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

    private final String taskXML = "task.xml";
    private final String workerXML = "worker.xml";

    private final double _a = 0.7; //交货期临近指数的系数
    private final double _b = 0.3; //已经等待时间的系数

    private final int _multiple_x = 1; //改变规模，worker的倍数
    private final int _multiple_y = 1; //改变规模，task的倍数

    private final int _popSize = 30; // 种群规模
    private final int _maxIterNum = 100;// 最大迭代次数
    private final double _mutationRate = 0.01;//基因变异的概率
    private final int _maxMutationNum = 3;//最大变异步长

    //=========================================================================================================//


    //创建唯一可用对象
    private static Const aConst;

    static {
        try {
            aConst = new Const();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }


    private List<ElePosition> elePositionlist = new ArrayList<>(); //初始化后的职位
    private List<EleWorker> eleWorkerList = new ArrayList<>(); //初始化工人
    private List<EleTask> eleTaskList = new ArrayList<>();//任务集
    private int[][] table;

    private int idPosition = 0;
    private int idTask = 0;
    private int idWorker = 0;


    private Const() throws DocumentException {

        int oneScaleWorker = 0;
        int oneScalePosition = 0;

        //规模扩大
        //worker规模扩大"_multiple_x"倍
        for(int i = 0 ; i<_multiple_x; i++){
            getWorker(workerXML);
            if(i == 0) oneScaleWorker = eleWorkerList.size();
        }
        for(int i = 0 ; i<_multiple_y; i++){
            getPosition(taskXML);
            if(i == 0) oneScalePosition = elePositionlist.size();
        }

        table = new int[eleWorkerList.size()][elePositionlist.size()];
        for(int i = 0 ; i<eleWorkerList.size(); i++){
            for(int j = 0 ; j<elePositionlist.size(); j++){
                table[i][j] = _Table1[i%oneScaleWorker][j%oneScalePosition];
            }
        }

        Iterator<ElePosition> elePositionIterator = elePositionlist.iterator();
        while (elePositionIterator.hasNext()){
            ElePosition ep = elePositionIterator.next();
            System.out.println("idP = " + ep.getIdPosition() + ", idT = " + ep.getIdtask()
                    + ", DNI = " + ep.getdNI() + ", Wt = " + ep.getwT() + ", level = " + ep.getLevel());
        }
        System.out.println("-------------------------");
        Iterator<EleWorker> eleWorkerIterator = eleWorkerList.iterator();
        while (eleWorkerIterator.hasNext()) {
            EleWorker ew = eleWorkerIterator.next();
            System.out.println("id = " + ew.getId() + ", level = " + ew.getLevel()
                    + ", need time =" + ew.getNeedTime());
        }
        System.out.println("-------------------------");

        Iterator<EleTask> eleTaskIterator = eleTaskList.iterator();
        while (eleTaskIterator.hasNext()){
            EleTask eT = eleTaskIterator.next();
            System.out.println("idt = " + eT.getIdtask() + ", DNI = " + eT.getdNI()
                    + ", nor_DNI = " + eT.getdNI_Normalized() + ", Wt = " + eT.getwT() + ", nor_wt = " + eT.getwT_Normalized()
                    + ", nor_x = " + eT.getdNI_wT());
        }
        System.out.println("-------------------------");

        System.out.println(table);

    }

    private void getPosition(String textName) throws DocumentException {
        SAXReader reader = new SAXReader();
        //[2] 读取xml文件
        InputStream INPUTSTREAM = this.getClass().getResourceAsStream(textName);
        Document docMachine = reader.read(INPUTSTREAM);
        //[3] 获取根元素
        Element root = docMachine.getRootElement();
        //[4] 获取根元素下的子元素
        Iterator<Element> it = root.elementIterator();
        //记录职位号

        double max_DNI = 0;
        double max_WaitTime = 0;
        while (it.hasNext()) {
            //取出元素
            Element e = (Element) it.next();
            //获取id属性
            Attribute e_id = e.attribute("id");
            //获取子元素
            Element e_DNI = e.element("DNI");
            Element e_WaitTime = e.element("WaitTime");
            //获取需要的值
            double dNI = Double.parseDouble(e_DNI.getStringValue());
            if(dNI>max_DNI) max_DNI = dNI;
            double waitTime = Double.parseDouble(e_WaitTime.getStringValue());
            if(max_WaitTime<waitTime) max_WaitTime = waitTime;
            Element e_content = e.element("directory");
            Iterator<Element> it_content = e_content.elementIterator();
            while (it_content.hasNext()) {
                Element ee = (Element) it_content.next();
                Element ee_level = ee.element("level");
                int level = Integer.parseInt(ee_level.getStringValue());
                elePositionlist.add(new ElePosition(idPosition,idTask,dNI,waitTime,level));
                idPosition += 1;
            }
            eleTaskList.add(new EleTask(idTask,dNI,waitTime));
            idTask += 1;
        }
        //将任务池中数据进行归一化处理
        Iterator<EleTask> eleTaskIterator = eleTaskList.iterator();
        while (eleTaskIterator.hasNext()){
            eleTaskIterator.next().normalizedBefore(max_DNI,max_WaitTime,_a,_b);
        }
    }

    private void getWorker(String textName) throws DocumentException {
        SAXReader reader = new SAXReader();
        //[2] 读取xml文件
        InputStream INPUTSTREAM = this.getClass().getResourceAsStream(textName);
        Document docMachine = reader.read(INPUTSTREAM);
        //[3] 获取根元素
        Element root = docMachine.getRootElement();
        //[4] 获取根元素下的子元素
        Iterator<Element> it = root.elementIterator();
        //记录员工id
        //int id = 0;
        while (it.hasNext()) {
            //取出元素
            Element e = (Element) it.next();
            //获取id属性
            Attribute e_id = e.attribute("id");
            //获取子元素
            Element e_level = e.element("level");
            Element e_NeedTime = e.element("needTime");
            //获取需要的值
            int level = Integer.parseInt(e_level.getStringValue());
            int needTime = Integer.parseInt(e_NeedTime.getStringValue());
            eleWorkerList.add(new EleWorker(idWorker,level,needTime));
            idWorker += 1;
        }
    }

    /**
     * 获取唯一可用对象
     */
    public static Const getaConst() {
        return aConst;
    }

    public  int[][] get_Table1() {
        return table;
    }

    public List<ElePosition> getElePositionlist(){
        return elePositionlist;
    }

    public List<EleWorker> getEleWorkerList() {
        return eleWorkerList;
    }

    public List<EleTask> getEleTaskList() {
        return eleTaskList;
    }

    /**
     * 重置task列表
     */
    public void resetTask(){
        Iterator<EleTask> eleTaskIterator = eleTaskList.iterator();
        while (eleTaskIterator.hasNext()){
            eleTaskIterator.next().reset(); //重置
        }
    }

    /**
     * 计算当前任务的总价值
     * @return
     */
    public double calculateTaskValue(){
        double value = 0.0;
        Iterator<EleTask> eleTaskIterator = eleTaskList.iterator();
        while (eleTaskIterator.hasNext()){
            EleTask eleTask = eleTaskIterator.next(); //任务
            double x = eleTask.getdNI_wT(); //案例中的分母
            double y = 0;//分子
            if(eleTask.getaMatch() == -1){
                y=0; //任务并未凑够人数
            }
            else{
                double needT = eleTask.getNeedT();
                if(needT == 0){
                    y = 1;//任务凑够人数且所有人均处于空闲状态
                }
                else if(needT<100){
                    y = 0.9;//任务凑够人数，但任务不能立即执行
                }
                else if(needT<200){
                    y = 0.7;
                }
                else{
                    y = 0.5;
                }
            }
            value += y/x;
        }
        return value;
    }

    public double get_mutationRate() {
        return _mutationRate;
    }

    public int get_maxIterNum() {
        return _maxIterNum;
    }

    public int get_maxMutationNum() {
        return _maxMutationNum;
    }

    public int get_popSize() {
        return _popSize;
    }

    public static void main(String[] args) {
        Const c = Const.getaConst();
    }


}
